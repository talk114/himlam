package gateway.core.channel.bidv.bidv_transfer_247.object247;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoDS {
    public static final String CHARACTER_DEFAULT = "|";

    private String sumTrans;//Số dòng giao dịch
    private String sumAmount;//Tổng Số tiền
    private String dateTimeFile;//Ngày giờ sinh file

    public InfoDS() {
    }

    public InfoDS(String sumTrans, String sumAmount, String dateTimeFile) {
        this.sumTrans = sumTrans;
        this.sumAmount = sumAmount;
        this.dateTimeFile = dateTimeFile;
    }

    public String getSumTrans() {
        return sumTrans;
    }

    public void setSumTrans(String sumTrans) {
        this.sumTrans = sumTrans;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }

    public String getDateTimeFile() {
        return dateTimeFile;
    }

    public void setDateTimeFile(String dateTimeFile) {
        this.dateTimeFile = dateTimeFile;
    }

    public String dataRaw() {
        return StringUtils.defaultString(sumTrans, "") + CHARACTER_DEFAULT + StringUtils.defaultString(sumAmount, "") + CHARACTER_DEFAULT +
        StringUtils.defaultString(dateTimeFile, "") ;
    }
}
