package gateway.core.channel.evn_south.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetReceiptResponse {

    @JsonProperty("TEN_CTY_DL")
    private String electricityCompanyName;

    @JsonProperty("TEN_DL")
    private String electricityName;

    @JsonProperty("THANG")
    private int month;

    @JsonProperty("NAM")
    private int year;

    @JsonProperty("TEN_KHANG")
    private String customerName;

    @JsonProperty("DIA_CHI")
    private String address;

    @JsonProperty("MA_KHANG")
    private String customerCode;

    @JsonProperty("ID_HDON")
    private String hoaDonId;

    @JsonProperty("TEN_LOAI_CPHI")
    private String billName;

    @JsonProperty("NGAY_PHANH")
    private String publishDay;

    @JsonProperty("SO_TIEN")
    private long amount;

    @JsonProperty("TYLE_THUE")
    private int taxRate;

    @JsonProperty("TIEN_THUE")
    private String taxAmount;

    @JsonProperty("TONG_TIEN")
    private String totalAmount;
}
