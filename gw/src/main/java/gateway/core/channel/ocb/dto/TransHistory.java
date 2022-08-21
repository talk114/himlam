package gateway.core.channel.ocb.dto;

import gateway.core.channel.ocb.dto.DataTransHis;
import gateway.core.channel.ocb.dto.Trace;
import gateway.core.channel.ocb.dto.request.BaseRequest;
import lombok.Data;

@Data
public class TransHistory extends BaseRequest {
    private static final String D_QUOTE = "\"";
    private Trace trace;
    private DataTransHis data;

    @Override
    public String toString() {
        return "{" +
                "\"trace\":{" +
                    "\"clientTransId\":" + D_QUOTE + trace.getClientTransId() + D_QUOTE +
                    ",\"clientTimestamp\":" + D_QUOTE + trace.getClientTimestamp()+ D_QUOTE+
                "},\"data\":{\"conditions\":{\"transactionTime\":{\"fromDate\":" + D_QUOTE + data.getConditions().getTransactionTime().getFromDate()+ D_QUOTE +","+
                                                        "\"toDate\":"+ D_QUOTE + data.getConditions().getTransactionTime().getToDate()+ D_QUOTE + "},"+
                        "\"AccountNumber\":"+ D_QUOTE + data.getConditions().getAccountNumber()+ D_QUOTE + "}}"+
                '}';
    }
}
