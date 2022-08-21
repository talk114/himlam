package gateway.core.channel.evn_south.dto;

public class EVNConfig {
    public static final String CHANNEL_CODE = "EVN_SOUTH";
    public static final String WSDL_URL = "http://172.16.39.4/PaymentEVNSPC/PaymentService.svc";


    public static final String GET_CUSTOMER_FEE_INFO = "GetCustomerFeeInfo";
    public static final String PAY_BILL_FEES_BY_CUSTOMER_CODE = "PayBillFeesByCustomerCode";
    public static final String CANCEL_BILL_FEE_BY_CUSTOMER_CODE = "CancelBillFeesByCustomerCode";
    public static final String GET_RECEIPT = "GetReceipt";
    public static final String CHECK_BILL = "CheckBill";
    public static final String GET_BILL_BY_BANK = "GetBillByBank";

    private EVNConfig(){}
}
