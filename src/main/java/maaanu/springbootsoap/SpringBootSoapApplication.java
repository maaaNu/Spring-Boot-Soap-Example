package maaanu.springbootsoap;

import maaanu.springbootsoap.connector.CustomerRepository;
import maaanu.springbootsoap.model.CustomerDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootSoapApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSoapApplication.class, args);
	}

	@Bean
    CommandLineRunner populateDatabase(CustomerRepository customerRepository) {
	    return args -> {
	        customerRepository.save(new CustomerDto("Harry", "Potter", 1.00, 1000));
	        customerRepository.save(new CustomerDto("Donald", "Duck", 0.5, 2000));
        };
    }
}
