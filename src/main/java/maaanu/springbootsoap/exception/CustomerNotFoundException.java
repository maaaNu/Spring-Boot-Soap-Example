package maaanu.springbootsoap.exception;

import maaanu.springbootsoap.controller.jaxws.CustomerNotFoundExceptionBean;

import javax.xml.ws.WebFault;

@WebFault
public class CustomerNotFoundException extends Exception {
    private CustomerNotFoundMessage test;

    public CustomerNotFoundException() { }

    public CustomerNotFoundException(String message, CustomerNotFoundMessage test) {
        super(message);
        this.test = test;
    }

    public CustomerNotFoundException(String message, Throwable cause, CustomerNotFoundMessage test) {
        super(message, cause);
        this.test = test;
    }

    public CustomerNotFoundMessage getTest() {
        return test;
    }


}
