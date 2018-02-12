package maaanu.springbootsoap.exception;

public class CustomerNotFoundMessage {
    private String test;

    public CustomerNotFoundMessage() {
    }

    public CustomerNotFoundMessage(String test) {
        this.test = test;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
