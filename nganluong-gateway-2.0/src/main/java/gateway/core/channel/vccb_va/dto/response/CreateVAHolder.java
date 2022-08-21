package gateway.core.channel.vccb_va.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateVAHolder {
    //Tên tài khoản
    private String accName;
    //Số tài khoản chuyên thu
    private String accNo;
    //Trạng thái tài khoản C: Đã đóng / O: Đang hoạt động / F: Đóng băng
    private String accStat;
    //Loại tài khoản O - One time / M - Many time
    private String accType;
    //Loại tiền VND
    private String ccy;
    //Người duyệt
    private String checkerId;
    //Người tạo
    private String makerId;
    private String openDate;
    //Tài khoản chính hạch toán FC
    private String partnerAccNo;
    //Mã đối tác
    private String partnerCode;
    //return when run close va api
    private String closeDate;
    //return when run reOpen va api
    private String reOpenDate;

    //Thứ tự giao dịch trong core BVB
    private String acEntrySrNo;
    //Dữ liệu trả về
    private String source;
    //Loại giao dịch D : Debit, C : Credit
    private String drcr;
    //Số tiền
    private String lcyAmount;
    //Ngày phát sinh giao dịch trong core
    private String valueDt;
    //Ngày giao dịch trong core
    private String txnInitDt;
    //Số tài khoản chuyên thu
    private String relatedAccount;
    private String relatedAccountName;
    //Nội dung giao dịch
    private String narrative;
    //Mã giao dịch
    private String externalRefNo;
    //Mã giao dịch trong core của BVB
    private String trnRefNo;

    private String error;
    private String message;
}
