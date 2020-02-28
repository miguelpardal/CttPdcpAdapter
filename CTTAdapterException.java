//package pt.ctt.pdcp.adapter;

public class CTTAdapterException extends Exception {

    private static final long serialVersionUID = 1L;

    CTTAdapterException() {
        super();
    }
    
    CTTAdapterException(String errorMsg, Exception e) {
        super(errorMsg, e);
    }

}
