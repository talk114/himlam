package gateway.core.channel.tcb.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import gateway.core.channel.tcb.dto.TCBConstants;
import lombok.Data;
import org.json.JSONObject;

/**
 * @author sonln
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateStsTransactionReq {
    private String id;
    private String txId;
    private String creDate;
    private String paymentType;
    private String desc;
    private String sign;
    private String customerIdIssuedByTCB;
    private String requestorName;
    private String transId;
    private String channelPayment;
    private String transStatus;
    private String statusCodeTransaction;
    private String statusDescTransaction;
    private String TxnDate;
    private String TxnBnkID;
    private String ChannelTxnId;


    public UpdateStsTransactionReq(JSONObject jsonObject) {
        JSONObject updateStatusReq = new JSONObject(jsonObject.toString()
                .replace("v1:", "").replace("soapenv:", ""));
        JSONObject reqGnlInf = updateStatusReq.getJSONObject("ReqGnlInf");
        this.id = reqGnlInf.get("Id").toString();
        this.txId = reqGnlInf.get("TxId").toString();
        this.creDate = reqGnlInf.get("CreDtTm").toString();
        this.paymentType = reqGnlInf.get("PmtTp").toString();
        this.desc = reqGnlInf.get("Desc").toString();
        this.sign = reqGnlInf.getJSONObject("Sgntr").get("Sgntr1").toString();
        JSONObject envt = updateStatusReq.getJSONObject("Envt");
        this.customerIdIssuedByTCB = envt.getJSONObject("TrgtPty").get("Nm").toString();
        this.requestorName = envt.getJSONObject("SrcPty").get("Nm").toString();

        JSONObject txRcrd = updateStatusReq.getJSONObject("TxRcrd");
        this.transId = txRcrd.getJSONObject("Result").get("PartID").toString().replace(TCBConstants.PARTNER_ID + ".", "");
        this.channelPayment = txRcrd.getJSONObject("Result").get("Channel").toString();
        this.transStatus = txRcrd.getJSONObject("Result").get("TxnStsRemark").toString();
        this.statusCodeTransaction = txRcrd.getJSONObject("Result").get("TxnSts").toString();
        this.statusDescTransaction = txRcrd.getJSONObject("Result").get("TxnDes").toString();
        this.TxnDate = txRcrd.getJSONObject("Result").get("TxnDate").toString();
        this.TxnBnkID = txRcrd.getJSONObject("Result").get("TxnBnkID").toString();
        this.ChannelTxnId = txRcrd.getJSONObject("Result").get("ChannelTxnId").toString();
    }


}
