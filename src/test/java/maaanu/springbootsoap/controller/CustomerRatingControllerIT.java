package maaanu.springbootsoap.controller;

import maaanu.springbootsoap.exception.CustomerNotFoundException;
import maaanu.springbootsoap.exception.CustomerNotFoundMessage;
import maaanu.springbootsoap.model.Rating;
import maaanu.springbootsoap.model.RatingResponse;
import maaanu.springbootsoap.utils.DefaultSoapFault;
import maaanu.springbootsoap.utils.SoapClient;
import maaanu.springbootsoap.utils.TypedSoapFault;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

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
    private SoapClient soapClient;
    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void withValidXmlRequest_return200AndValidResponse() throws Exception {
        ResponseEntity<RatingResponse> response = soapClient.newRequest()
                .withServer(createRateCustomerURL())
                .withRequestBody(resourceLoader.getResource("classpath:RateCustomer_ValidRequest.xml"))
                .getAsType(RatingResponse.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        RatingResponse ratingResponse = response.getBody();
        assertThat(ratingResponse.getRating()).isEqualTo(Rating.A);
        assertThat(ratingResponse.getLoanLimit()).isEqualTo(11999);
    }


    @Test
    public void withInvalidXmlRequest_return500AndErrorMsg() throws Exception {
        DefaultSoapFault soapFault = soapClient.newRequest()
                .withServer(createRateCustomerURL())
                .withRequestBody(resourceLoader.getResource("classpath:RateCustomer_InvalidRequest.xml"))
                .getAsDefaultFault();

        assertThat(soapFault.getFaultcode()).isEqualTo("soap:Client");
        assertThat(soapFault.getFaultString()).contains("Unmarshalling Error");
        assertThat(soapFault.getFaultString()).contains("'{lastName}' wird erwartet.");
    }

    @Test
    public void withInvalidCustomer_return500AndCustomerNotFoundException() throws Exception {
        TypedSoapFault<CustomerNotFoundMessage> soapFault = soapClient.newRequest()
                .withServer(createRateCustomerURL())
                .withRequestBody(resourceLoader.getResource("classpath:RateCustomer_CustomerNotFound.xml"))
                .getAsTypedFault(CustomerNotFoundMessage.class);

        assertThat(soapFault.getFaultType().getTest()).isEqualTo("Sometimes, just one second.");
    }

    private String createRateCustomerURL() {
        return "http://localhost:" + serverPort + "/" + serviceUrl + endpointPath + rateCustomerPath;
    }

}