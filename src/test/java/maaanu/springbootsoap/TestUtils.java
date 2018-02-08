package maaanu.springbootsoap;

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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.cxf.helpers.FileUtils.readLines;

public class TestUtils {

    public static String readFileAsString(File file) throws Exception {
       return String.join("", readLines(file));
    }

    public static <ReturnType> ReturnType parseSOAPResponse(InputStream stream, Class clazz) throws JAXBException, ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller u = jc.createUnmarshaller();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        Document doc = documentBuilderFactory.newDocumentBuilder().parse(stream);
        doc.getDocumentElement().normalize();
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList) xPath.evaluate("//return", doc.getDocumentElement(), XPathConstants.NODESET);
        Node node = nodes.item(0);

        JAXBElement<ReturnType> response = u.unmarshal(node, clazz);
        return response.getValue();
    }

}
