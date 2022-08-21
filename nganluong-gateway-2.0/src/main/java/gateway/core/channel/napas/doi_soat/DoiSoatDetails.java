package gateway.core.channel.napas.doi_soat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DoiSoatDetails implements Serializable {

    private static final String recordType = "DR"; // details record
    @JsonProperty("MTI")
    private String MTI;
    private String cardNumber; // so the
    private String processingCode; //trans_id
    private String serviceCode; // do napas quy dinh
    private String transactionChannelCode; // ma kenh thuc hien GD
    private String transactionAmount; //7
    private String realTransactionAmount; //8
    private String currencyCode; //9
    private String amountSettlement; // so tien quyet toan /10
    private String amountSettlementCurrencyCode; // loai tien quyet toan /11
    private String rate; //12
    private String amountOfCardholder; // so tien chu the //13
    private String realCardHolderAmount; //Số tiền chủ thẻ thực tế bị trừ //14
    private String currencyCodeCardHolder; //Mã tiền tệ đồng tiền chủ thẻ //15
    private String rateAmountCardHolder; // Tỷ giá chuyển dổi sang đồngtiền chủ thẻ (F10) //16
    private String systemTrace;  //Số Trace hệthống (F11) //17
    private String transactionTime; //Giờ giao dịch hhmmss //18
    private String transactionDate; // ngay giao dich MMdd //19
    private String settlementDate; // ngay quyet toan //20
    private String agencyCode; // ma dai ly //21
    @JsonProperty("PANNumber")
    private String PANNumber; //22- Chế độ tại điểm truy cập dịch vụ Mã xác định số PAN được thu nhận cũng như khả năng nhập số PIN tại điểm chấp nhận dịch vụ
    private String conditionalCodeAtServiceAccessPoint; //23- Mã có điều kiện tại điểm truy cập dịch vụ
    private String agencyCodeIdentify; //24 ma dinh danh dai ly
    private String acquirerIdentify; // 25- Mã số của ngân hàng đăng ký tại Napas
    private String issuerIdentify; //26 Mã tổ chức phát hành thẻ, Mã số của ngân hàng đăng ký tại  NAPAS.
    private String identifyCodeAcceptsCard; //27-Mã xác định đơn vị chấp nhận thẻ
    private String beneficiaryIdentify; //28-Mã Ngân hàng thụ hưởng
    private String accountNumberMaster; //29 Số tài khoản nguồn
    private String destinationAccountNumberOrCardNumber;  // 30-Số thẻ/ số tài khoản đích
    @JsonProperty("SVFISSNP")
    private String SVFISSNP;
    @JsonProperty("IRFISSACQ")
    private String IRFISSACQ;
    @JsonProperty("IRFISSBNB")
    private String IRFISSBNB;
    @JsonProperty("SVFACQNP")
    private String SVFACQNP;
    @JsonProperty("IRFACQISS")
    private String IRFACQISS;
    @JsonProperty("IRFACQBNB")
    private String IRFACQBNB;
    @JsonProperty("SVFBNBNP")
    private String SVFBNBNP;
    @JsonProperty("IRFBNBISS")
    private String IRFBNBISS;
    @JsonProperty("IRFBNBACQ")
    private String IRFBNBACQ;

    private String retrievalReferenceNumber; //31 Số tham chiếu tra soát của các giao dịch
    private String authorizationNumber; // 32-Số cấp phép của giao dịch do ngân hàng phát hành trả về.
    private String transactionReferenceNumber; //33- Số định danh giao dịch do hệ thống của NAPAS sinh ra khi giao dịch được xử lý qua hệ thống.
    private String reconciliationResponseCode; // 34-Mã đối soát của giao dịch được quy định trong quy trình Thanh quyết toán dành cho tổ chức thành viên.
    private String reserveInformation1; //Thông tin dự trữ 1
    private String reserveInformation2;//Thông tin dự trữ 2
    private String reserveInformation3;//Thông tin dự trữ 3
    private String checksumLine; // checksum cho tung dong
    private String dataFormatWithChecksum; // dung de

    @Override
    public String toString() {
        return "DR[MTI]";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof DoiSoatDetails) {
            DoiSoatDetails payment_rec = (DoiSoatDetails) o;
            return payment_rec.getTransactionDate().trim().equals(this.transactionDate.trim())
                    && payment_rec.getTransactionTime().trim().equals(this.transactionTime.trim())
                    && payment_rec.getRetrievalReferenceNumber().trim().equals(this.retrievalReferenceNumber.trim())
                    && Long.parseLong(payment_rec.getTransactionAmount()) == (Long.parseLong(this.transactionAmount));
        }
        return false;
    }
}