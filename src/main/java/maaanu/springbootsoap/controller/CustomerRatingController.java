package maaanu.springbootsoap.controller;

import maaanu.springbootsoap.exception.CustomerNotFoundException;
import maaanu.springbootsoap.model.RatingRequest;
import maaanu.springbootsoap.model.RatingResponse;
import maaanu.springbootsoap.service.CustomerRatingService;
import maaanu.springbootsoap.util.Route;
import maaanu.springbootsoap.util.SoapService;
import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;


@WebService
@SchemaValidation
@SoapService
public class CustomerRatingController {
    @Route public static String ENDPOINT_ADDR = "/CustomerRating";
    private CustomerRatingService customerRatingService;

    public CustomerRatingController () {};

    @Autowired
    public CustomerRatingController(CustomerRatingService customerRatingService) {
        this.customerRatingService = customerRatingService;
    }

    @WebMethod
    public RatingResponse rateCustomer(@XmlElement(required = true) RatingRequest ratingRequest) throws CustomerNotFoundException {
        String firstName = ratingRequest.getFirstName();
        String lastName = ratingRequest.getLastName();
        return customerRatingService.rateCustomer(firstName, lastName);
    }


}
