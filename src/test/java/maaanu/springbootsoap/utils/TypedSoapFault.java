package maaanu.springbootsoap.utils;

public class TypedSoapFault<FaultType> extends DefaultSoapFault {
    private FaultType faultType;

    public TypedSoapFault(String faultcode, String faultString, FaultType faultType) {
        super(faultcode, faultString);
        this.faultType = faultType;
    }

    public FaultType getFaultType() {
        return faultType;
    }
}
