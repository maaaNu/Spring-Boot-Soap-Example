package maaanu.springbootsoap.controller;

import maaanu.springbootsoap.TestUtils;
import maaanu.springbootsoap.model.Rating;
import maaanu.springbootsoap.model.RatingResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static maaanu.springbootsoap.TestUtils.parseSOAPResponse;
import static maaanu.springbootsoap.TestUtils.readFileAsString;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerRatingControllerIT {

    @Value("${cxf.path}")
    private String serviceUrl;
    @Value("${local.server.port}")
    private int serverPort;
    private String endpointPath = CustomerRatingController.ENDPOINT_ADDR;
    private String rateCustomerPath = "/rateCustomer";

    @Autowired
    private TestRestTemplate httpClient;
    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void withValidXmlRequest_return200AndValidResponse() throws Exception {
        ResponseEntity<String> response = httpClient.postForEntity(
                createRateCustomerURL(),
                readFileAsString(resourceLoader.getResource("classpath:RateCustomer_ValidRequest.xml").getFile()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        RatingResponse ratingResponse = parseSOAPResponse(new ByteArrayInputStream(response.getBody().getBytes()), RatingResponse.class);
        assertThat(ratingResponse.getRating()).isEqualTo(Rating.A);
        assertThat(ratingResponse.getLoanLimit()).isEqualTo(11999);
    }

    @Test
    public void withInvalidXmlRequest_return500AndErrorMsg() throws Exception {
        ResponseEntity<String> response = httpClient.postForEntity(
                createRateCustomerURL(),
                readFileAsString(resourceLoader.getResource("classpath:RateCustomer_InvalidRequest.xml").getFile()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        InputStream is = new ByteArrayInputStream(response.getBody().getBytes());
        SOAPMessage request = MessageFactory.newInstance().createMessage(null, is);
        System.out.println(request);
        SOAPEnvelope env = request.getSOAPPart().getEnvelope();
        SOAPBody body = env.getBody();
        SOAPFault fault = body.getFault();
        DetailEntry entry = (DetailEntry) fault.getDetail().getDetailEntries().next();
        System.out.println(entry);
    }

    private String createRateCustomerURL() {
        return "http://localhost:" + serverPort + "/" + serviceUrl + endpointPath + rateCustomerPath;
    }

}