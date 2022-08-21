package gateway.core.channel.evn_south.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckBillRequest {

    @JsonProperty("bankID")
    private String bankID;

    private String customerCode;

    @JsonProperty("receiptID")
    private String receiptID;

    @Override
    public String toString() {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                "<soapenv:Header/>" +
                    "<soapenv:Body>" +
                        "<tem:CheckBill>" +
                        "<tem:CustomerCode>" + customerCode + "</tem:CustomerCode>" +
                        "<tem:HoaDonID>" + receiptID + "</tem:HoaDonID>" +
                        "<tem:BankID>" + bankID + "</tem:BankID>" +
                    "</tem:CheckBill>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }
}
