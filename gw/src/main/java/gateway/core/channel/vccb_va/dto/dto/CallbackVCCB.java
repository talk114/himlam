package gateway.core.channel.vccb_va.dto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallbackVCCB {

    //1. sẽ do hệ thống khác BVB tạo ra không quan tâm
    private String externalRefNo;

    //2. mã tham chiếu của BVB
    @JsonProperty("trnRefNo")
    private String transactionReferenceNumber;

    //3. Thứ tự giao dịch trong core BVB
    private String acEntrySrNo;

    //4. Tài khoản thanh toán của NL khai báo trên BVB
    @JsonProperty("accNo")
    private String accountNumber;

    @JsonProperty("source")
    private String source;
    
    @JsonProperty("ccy")
    private String currencyCode;

    //7. Loại giao dịch D : Debit; C : Credit
    private String drcr;

    //8. Số tiền
    private String lcyAmount;

    //9. Ngày phát sinh giao dịch trong core (BVB)
    @JsonProperty("valueDt")
    private String valueDate;

    //10. Ngày giao dịch trong core
    @JsonProperty("txnInitDt")
    private String txnInitDate;

    //11. Số tài khoản chuyên thu (VA)
    private String relatedAccount;

    private String relatedAccountName;

    //13. Nội dung giao dịch
    private String narrative;
}
