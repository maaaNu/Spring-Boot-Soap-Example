package maaanu.springbootsoap.model;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class CustomerDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private double liabilities = 0.00;
    private double monthlyIncome = 0.00;

    private CustomerDto() { //WHY JPA WHY
    }

    public CustomerDto(String firstName, String lastName, double liabilities, double monthlyIncome) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.liabilities = liabilities;
        this.monthlyIncome = monthlyIncome;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public double getLiabilities() {
        return liabilities;
    }

    @Override
    public String toString() {
        return "CustomerDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", liabilities=" + liabilities +
                '}';
    }
}
