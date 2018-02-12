package maaanu.springbootsoap.utils;

public class DefaultSoapFault {
    private String faultcode;
    private String faultString;

    public DefaultSoapFault(String faultcode, String faultString) {
        this.faultcode = faultcode;
        this.faultString = faultString;
    }

    public String getFaultcode() {
        return faultcode;
    }

    public String getFaultString() {
        return faultString;
    }

    @Override
    public String toString() {
        return "DefaultSoapFault{" +
                "faultcode='" + faultcode + '\'' +
                ", faultString='" + faultString + '\'' +
                '}';
    }
}
