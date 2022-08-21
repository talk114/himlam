package gateway.core.channel.evn_south.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillInfoRequest {

    {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.payDate = format.format(new Date());
    }

    private String bankID;
    private String customerCode;
    private String billCode;
    private long amount;
    private String payDate;
    private String transactionCode;
    private String billSymbol;
    private String departCode;
    private String receiptPrinted;

    @Override
    public String toString() {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "    <soapenv:Body>" +
                "        <PayBillFeesByCustomerCode xmlns=\"http://tempuri.org/\">" +
                "            <BankID>" + bankID + "</BankID>" +
                "            <CustomerCode>" + customerCode + "</CustomerCode>" +
                "            <BillCodes>" +
                "                <arr:string xmlns:arr=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\">" + billCode + "</arr:string>" +
                "            </BillCodes>" +
                "            <Amount>" +
                "                <arr:long xmlns:arr=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\">"+ amount +"</arr:long>" +
                "            </Amount>" +
                "            <PayDate xsi:type=\"xsd:dateTime\">" + payDate + "</PayDate>" +
                "            <TransactionCode>" +
                "                <arr:string xmlns:arr=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\">" + transactionCode + "</arr:string>" +
                "            </TransactionCode>" +
                "            <KyHieuHoaDon>" + billSymbol + "</KyHieuHoaDon>" +
                "            <DepartCode>" + departCode + "</DepartCode>" +
                "            <DaInHD>" + receiptPrinted + "</DaInHD>" +
                "        </PayBillFeesByCustomerCode>" +
                "    </soapenv:Body>" +
                "</soapenv:Envelope>";
    }
}
