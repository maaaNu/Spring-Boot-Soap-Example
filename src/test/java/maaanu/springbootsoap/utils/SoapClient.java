package maaanu.springbootsoap.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static maaanu.springbootsoap.TestUtils.readFileAsString;

@Service
public class SoapClient {

    private TestRestTemplate httpClient;

    public SoapClient(TestRestTemplate httpClient) {
        this.httpClient = httpClient;
    }

    public Stage0 newRequest() {
        return new Stage0();
    }

    public class Stage0 {
        Stage0() { }

        public Stage1 withServer(String url) {
            return new Stage1(url);
        }
    }


    public class Stage1 {
        private String url;

        Stage1(String url) {
            this.url = url;
        }

        public Stage2 withRequestBody(String body) {
            return new Stage2(url, body);
        }

        public Stage2 withRequestBody(Resource xmlFile) throws Exception {
            return new Stage2(url, readFileAsString(xmlFile.getFile()));
        }
    }

    public class Stage2 {
        private String url;
        private String body;

        Stage2(String url, String body) {
            this.url = url;
            this.body = body;
        }

        public <ResponseType> ResponseEntity<ResponseType> getAsType(Class clazz) throws JAXBException, ParserConfigurationException, IOException, SAXException, XPathExpressionException {
            return getAsType(clazz, "return");
        }

        public <ResponseType> ResponseEntity<ResponseType> getAsType(Class clazz, String wrappingElement) throws JAXBException, ParserConfigurationException, IOException, SAXException, XPathExpressionException {
            ResponseEntity<String> response = httpClient.postForEntity(url, body, String.class);
            ByteArrayInputStream stream = new ByteArrayInputStream(response.getBody().getBytes());
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller u = jc.createUnmarshaller();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            Document doc = documentBuilderFactory.newDocumentBuilder().parse(stream);
            doc.getDocumentElement().normalize();
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xPath.evaluate("//" + wrappingElement, doc.getDocumentElement(), XPathConstants.NODESET);
            Node node = nodes.item(0);
            JAXBElement<ResponseType> jaxbElement = u.unmarshal(node, clazz);
            return new ResponseEntity<>(jaxbElement.getValue(), response.getStatusCode());
        }

        public DefaultSoapFault getAsDefaultFault() throws SOAPException, IOException {
            SOAPFault fault = getSoapFault();
            return new DefaultSoapFault(fault.getFaultCode(), fault.getFaultString());
        }

        public <FaultType> TypedSoapFault<FaultType> getAsTypedFault(Class clazz) throws IOException, SOAPException, JAXBException {
            SOAPFault fault = getSoapFault();
            DetailEntry entry = fault.getDetail().getDetailEntries().next();
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<FaultType> jaxbElement = u.unmarshal(entry.getFirstChild(), clazz);
            return new TypedSoapFault<>(fault.getFaultCode(), fault.getFaultString(), jaxbElement.getValue());
        }

        private SOAPFault getSoapFault() throws SOAPException, IOException {
            ResponseEntity<String> response = httpClient.postForEntity(url, body, String.class);
            InputStream is = new ByteArrayInputStream(response.getBody().getBytes());
            SOAPMessage request = MessageFactory.newInstance().createMessage(null, is);
            SOAPEnvelope env = request.getSOAPPart().getEnvelope();
            return env.getBody().getFault();
        }


    }
}
