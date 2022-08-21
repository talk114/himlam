package gateway.core.channel.evn_south.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerInfoResponse {

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Bills")
    private Bills bills;

    @JsonProperty("CustomerCode")
    private String customerCode;

    @JsonProperty("DanhSo")
    private String danhSo;

    @JsonProperty("MaSoThue")
    private String maSoThue;

    @JsonProperty("MaTram")
    private String maTram;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("NganhNghe")
    private String nganhNghe;

    @JsonProperty("PhienGCS")
    private String phieuGCS;

    @JsonProperty("SoCongTo")
    private String soCongTo;

    @JsonProperty("SoGhiChiSo")
    private String soGhiChiSo;

}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Bills{
    @JsonProperty("BillInfo")
    private Object billInfo;

    public Bills(String a) {
    }
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class BillInfo{

    @JsonProperty("Amount")
    private long amount;

    @JsonProperty("BillCode")
    private String billCode;

    @JsonProperty("HoaDonID")
    private String hoaDonID;

    @JsonProperty("Month")
    private int month;

    @JsonProperty("SoHo")
    private String soHo;

    @JsonProperty("Year")
    private int year;
}
