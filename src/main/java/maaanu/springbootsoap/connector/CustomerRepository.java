package maaanu.springbootsoap.connector;

import maaanu.springbootsoap.model.CustomerDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerDto, Long> {

    List<CustomerDto> findByLastName(String lastName);
    Optional<CustomerDto> findByFirstNameAndLastName(String firstName, String lastName);
}
