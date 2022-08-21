package gateway.core.channel.evn_south.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
public class CancelPaymentBillRequest {

    private String bankID;
    private String customerCode;
    private String billCode;
    private Long amount;
    private String cancelDate;
    private String transactionCode;

    {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.cancelDate = format.format(new Date()) + "+07:00";
    }

    @Override
    public String toString() {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\" xmlns:arr=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\">" +
                "   <soapenv:Header/>" +
                "   <soapenv:Body>" +
                "      <tem:CancelBillFeesByCustomerCode>" +
                "         <tem:BankID>" + bankID + "</tem:BankID>" +
                "         <tem:CustomerCode>" + customerCode + "</tem:CustomerCode>" +
                "         <tem:BillCodes>" +
                "            <arr:string>" + billCode + "</arr:string>" +
                "         </tem:BillCodes>" +
                "         <tem:Amount>" +
                "            <arr:long>" + amount + "</arr:long>" +
                "         </tem:Amount>" +
                "         <tem:CancelDate>" + cancelDate + "</tem:CancelDate>" +
                "         <tem:TransactionCode>" +
                "            <arr:string>" + transactionCode + "</arr:string>" +
                "         </tem:TransactionCode>" +
                "      </tem:CancelBillFeesByCustomerCode>" +
                "   </soapenv:Body>" +
                "</soapenv:Envelope>";
    }
}
