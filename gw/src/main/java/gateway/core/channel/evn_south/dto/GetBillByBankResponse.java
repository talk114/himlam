package gateway.core.channel.evn_south.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBillByBankResponse {
//    @JsonProperty("xmlns")
//    private String xmlns;

    @JsonProperty("table")
    private List<BillInfoByBank> table;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class BillInfoByBank {
    @JsonProperty("MA_HUY_GDICH")
    private String ma_huy_gdich;

    @JsonProperty("diffgr-id")
    private String id;

    @JsonProperty("THANG")
    private String month;

    @JsonProperty("BANK_ID")
    private String BankID;

    @JsonProperty("MA_DVIQLY")
    private String maDonViQuanLy;

    @JsonProperty("KY")
    private String ky;

    @JsonProperty("TEN_KHANG")
    private String customerName;

    @JsonProperty("MA_GDICH")
    private String maGiaoDich;

    @JsonProperty("ID_HDON")
    private String idHoaDon;

    @JsonProperty("NGAY_GDICH")
    private String transactionDate;

    @JsonProperty("SO_TIEN")
    private String amount;

    @JsonProperty("msdatrowOrder")
    private String msdatrowOrder;

    @JsonProperty("DCHI_KHANG")
    private String address;

    @JsonProperty("NAM")
    private String year;

    @JsonProperty("MA_KHANG")
    private String maKhachHang;

    @JsonProperty("LOAI_HDON")
    private String loaiHoaDon;
}