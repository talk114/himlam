package gateway.core.channel.evn_south.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import gateway.core.util.TimeUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class GetBillByBankRequest {

    @JsonProperty("bankID")
    private String bankID;

    @JsonProperty("ma_don_vi_quan_ly")
    private String maDonViQuanLy;

    @JsonProperty("fromDate")
    private String fromDate;

    @JsonProperty("toDate")
    private String toDate;

    @JsonProperty("transaction_type")
    private String transactionType;

    @SneakyThrows
    @Override
    public String toString() {
        return "  <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                "   <soapenv:Body>" +
                "      <tem:GetBillByBank>" +
                "         <tem:BankID>" + bankID + "</tem:BankID>" +
                "         <tem:MaDonViQL>" + maDonViQuanLy + "</tem:MaDonViQL>" +
                "         <tem:TuNgay>" + TimeUtil.getUTCTime(fromDate, "yyyy-MM-dd") + "</tem:TuNgay>" +
                "         <tem:DenNgay>" + TimeUtil.getUTCTime(toDate, "yyyy-MM-dd") + "</tem:DenNgay>" +
                "         <tem:LoaiGiaoDich>" + transactionType + "</tem:LoaiGiaoDich>" +
                "      </tem:GetBillByBank>" +
                "   </soapenv:Body>" +
                "</soapenv:Envelope>";
    }
}
