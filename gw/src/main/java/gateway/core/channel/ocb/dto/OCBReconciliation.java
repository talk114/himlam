package gateway.core.channel.ocb.dto;

import gateway.core.channel.anbinhbank.report.BaseReport;
import gateway.core.channel.ocb.dto.error.ErrorResponse;
import gateway.core.channel.ocb.dto.request.PaymentStep2Req;
import gateway.core.channel.ocb.dto.response.ResponseStep2;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Response;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author dungla@nganluong.vn
 */

@Getter
@Setter
public class OCBReconciliation {
    private static final Logger logger = LogManager.getLogger(OCBReconciliation.class);

    private static final String COMMA = ",";
    private static final String AUTH_KEY = "04392bb24a98c047dce89046f08ad188";

    //Mã giao dịch ngân lượng clientTransId step 2
    private String nganluongTransacId;

    //Mã giao dịch ngân hàng bankRefNo step 2
    private String ocbTransId;

    //FT bank bankTransactionId step 2
    private String ocbFt;

    //dd/MM/yyyy
    private String transDate;
    private String amount;
    //Loại tiền tệ VND
    private String ccy;
    private String rrc;
    private String checkSum;

    public OCBReconciliation(String nganluongTransacId, String ocbTransId, String ocbFt, String transDate, String amount, String ccy, String rrc) {
        this.nganluongTransacId = nganluongTransacId;
        this.ocbTransId = ocbTransId;
        this.ocbFt = ocbFt;
        this.transDate = transDate;
        this.amount = amount;
        this.ccy = ccy;
        this.rrc = rrc;
        if(rrc == null){
            this.rrc = "";
        }
    }
    public OCBReconciliation(PaymentStep2Req req, String amount){
        this.nganluongTransacId = req.getTrace().getClientTransId();
        this.ocbTransId = "";
        this.ocbFt = "";
        this.transDate = "";
        this.amount = amount;
        this.ccy = "VND";
        this.rrc = "";
    }
    public OCBReconciliation(PaymentStep2Req req, ErrorResponse response, String amount){
        this.nganluongTransacId = req.getTrace().getClientTransId();
        this.ocbTransId = response.getTrace().getBankRefNo();
        this.ocbFt = "";
        this.transDate = response.getTrace().getClientTimestamp().substring(0,8);
        this.amount = amount;
        this.ccy = "VND";
        this.rrc = "";
    }

    public OCBReconciliation(PaymentStep2Req req, ResponseStep2 response, String amount) {
        this.nganluongTransacId = req.getTrace().getClientTransId();
        this.ocbTransId = response.getTrace().getBankRefNo();
        this.ocbFt = response.getData().getBankTransactionId();
        this.transDate = response.getTrace().getClientTimestamp().substring(0,8);
        this.amount = amount;
        this.ccy = "VND";
        this.rrc = "";
    }
    public String getDataToCheckSum(){
        return nganluongTransacId + COMMA
                + ocbTransId + COMMA
                + ocbFt + COMMA
                + transDate + COMMA
                + amount + COMMA
                + ccy + COMMA
                + rrc;
    }

    private String md5(String data){
        MessageDigest instance = null;
        try {
            instance = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        byte[] byteData = null;
        try {
            byteData = instance.digest(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        String hexString = DatatypeConverter.printHexBinary(byteData);
        return hexString.toLowerCase();

    }

    private String getCheckSum(){
        return md5(getDataToCheckSum() + AUTH_KEY);
    }
    @Override
    public String toString() {
        if(rrc == null) rrc = "";
        return nganluongTransacId + COMMA
                + ocbTransId + COMMA
                + ocbFt + COMMA
                + transDate + COMMA
                + amount + COMMA
                + ccy + COMMA
                + rrc + COMMA
                + getCheckSum();
    }

}

