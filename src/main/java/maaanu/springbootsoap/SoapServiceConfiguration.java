package maaanu.springbootsoap;

import maaanu.springbootsoap.controller.CustomerRatingController;
import maaanu.springbootsoap.service.CustomerRatingService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class SoapServiceConfiguration {
    private static final String WEB_SERVICE_WSDL_SUFFIX = ".wsdl";
    private final CustomerRatingService customerRatingService;

    public SoapServiceConfiguration(CustomerRatingService customerRatingService) {
        this.customerRatingService = customerRatingService;
    }

    @Bean(name= Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public CustomerRatingController customerRatingEndpoint() {
        return new CustomerRatingController(customerRatingService);
    }

    @Bean
    public Endpoint endpoint() {
        final EndpointImpl endpoint = new EndpointImpl(springBus(), customerRatingEndpoint());
        endpoint.publish(CustomerRatingController.ENDPOINT_ADDR);
        endpoint.setWsdlLocation(CustomerRatingController.ENDPOINT_ADDR + WEB_SERVICE_WSDL_SUFFIX);
        return endpoint;
    }
}
