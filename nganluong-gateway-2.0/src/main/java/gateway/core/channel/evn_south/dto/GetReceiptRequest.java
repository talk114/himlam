package gateway.core.channel.evn_south.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetReceiptRequest {

    @JsonProperty("bankID")
    private String bankID;

    @JsonProperty("transactionCode")
    private String transactionCode;

    @JsonProperty("receiptType")
    private String receiptType;

    @JsonProperty("phatSinh")
    private String phatSinh;

    @JsonProperty("receiptID")
    private String receiptID;

    @JsonProperty("amount")
    private long amount;

    public String body(String trigger){
        switch (trigger){
            case "TD":
                return electricBill();
            case "VC":
                return congSuatPhanKhangBill();
            default:
                return serviceBill();
        }
    }

    private String electricBill(){
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                "<soapenv:Header />" +
                 "<soapenv:Body>" +
                    "<tem:GetPhieuThuTienDien>" +
                        "<tem:BankID>"+ bankID +"</tem:BankID>" +
                        "<tem:TransactionCode>" + transactionCode + "</tem:TransactionCode>" +
                        "<tem:HoaDonID>" + receiptID + "</tem:HoaDonID>" +
                        "<tem:SoTien>" + amount + "</tem:SoTien>" +
                    "</tem:GetPhieuThuTienDien>" +
                 "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }

    private String serviceBill(){
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                "<soapenv:Header />" +
                    "<soapenv:Body>" +
                    "<tem:GetPhieuThuTienDichVu>" +
                        "<tem:BankID>" + bankID + "</tem:BankID>" +
                        "<tem:TransactionCode>" + transactionCode + "</tem:TransactionCode>" +
                        "<tem:LoaiHDon>" + receiptType + "</tem:LoaiHDon>" +
                        "<tem:LoaiPSinh>" + phatSinh +"</tem:LoaiPSinh>" +
                        "<tem:HoaDonID>" + receiptID +"</tem:HoaDonID>" +
                        "<tem:SoTien>" + amount + "</tem:SoTien>" +
                    "</tem:GetPhieuThuTienDichVu>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }

    private String congSuatPhanKhangBill(){
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                "<soapenv:Header />" +
                "<soapenv:Body>" +
                "<tem:GetPhieuThuTienCSPK>" +
                "<tem:BankID>"+ bankID +"</tem:BankID>" +
                "<tem:TransactionCode>" + transactionCode + "</tem:TransactionCode>" +
                "<tem:HoaDonID>" + receiptID + "</tem:HoaDonID>" +
                "<tem:SoTien>" + amount + "</tem:SoTien>" +
                "</tem:GetPhieuThuTienCSPK>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }
}
