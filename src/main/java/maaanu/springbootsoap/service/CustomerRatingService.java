package maaanu.springbootsoap.service;

import maaanu.springbootsoap.connector.CustomerRepository;
import maaanu.springbootsoap.exception.CustomerNotFoundException;
import maaanu.springbootsoap.model.CustomerDto;
import maaanu.springbootsoap.model.Rating;
import maaanu.springbootsoap.model.RatingResponse;
import maaanu.springbootsoap.model.RatingResponseBuilder;
import org.springframework.stereotype.Service;

import static java.lang.Math.round;

@Service
public class CustomerRatingService {
    private final CustomerRepository customerRepository;

    public CustomerRatingService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public RatingResponse rateCustomer (String firstName, String lastName) throws CustomerNotFoundException {
        return customerRepository.findByFirstNameAndLastName(firstName, lastName)
                .map(this::createRatingResponse)
                .orElseThrow(CustomerNotFoundException::new);
    }

    private RatingResponse createRatingResponse(CustomerDto customer) {
        Rating rating = calculateRating(customer);
        int loanLimit = calcuteLoanLimit(customer);

        return RatingResponseBuilder.builder()
                .withLimit(loanLimit)
                .withRating(rating)
                .create();
    }

    private Rating calculateRating(CustomerDto customer) {
        double monthlyIncome = customer.getMonthlyIncome();
        double liabilites = customer.getLiabilities();
        if(monthlyIncome > 1500 && liabilites == 0) return Rating.AAA;
        else if(monthlyIncome * 12 > liabilites) return Rating.A;
        else if(monthlyIncome * 24 > liabilites) return Rating.BB;
        else return Rating.C;
    }

    private int calcuteLoanLimit(CustomerDto customer) {
        return (int) round(customer.getMonthlyIncome() * 12 - customer.getLiabilities());
    }
}
