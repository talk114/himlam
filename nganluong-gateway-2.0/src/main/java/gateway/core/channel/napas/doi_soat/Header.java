package gateway.core.channel.napas.doi_soat;

import java.io.Serializable;

import static gateway.core.channel.napas.service.schedule.NapasScheduleService.*;
import static gateway.core.channel.napas.doi_soat.DoiSoatConstants.*;

public class Header implements Serializable {

    private static final String recordType = "HR";
    private String receiverBankCode; //Mã Ngân hàng gửi file
    private String lastTransactionDate; //Ngày giao dịch của giao dịch cuối cùng trong file

    public String getRecordType() {
        return recordType;
    }

    public String getReceiverBankCode() {
        return receiverBankCode;
    }

    public void setReceiverBankCode(String receiverBankCode) {
        this.receiverBankCode = receiverBankCode;
    }

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    @Override
    public String toString() {
        return "HR[REV]" + genWhiteSpaceOrZero(this.receiverBankCode, 8 - this.receiverBankCode.length(), WHITE_SPACE)
                + "[DATE]" + formatDateTimeByPattern("ddMMyyyy", this.lastTransactionDate);
    }
}
