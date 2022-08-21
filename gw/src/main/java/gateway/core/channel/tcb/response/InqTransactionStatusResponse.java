/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.tcb.response;

import org.json.JSONObject;


/**
 *
 * @author taind
 */
public class InqTransactionStatusResponse {

    private String TxnStsRemark;
    private String FeeCode;
    private String PartID;
    private String ChannelTxnId;
    private String TxnStsRemarkDes;
    private String TxnSts;
    private String TxnDes;
    private String TxnBnkID;
    private String TxnDate;
    private String FeeAmt;
    private String Channel;

    public InqTransactionStatusResponse(JSONObject json) {
        this.TxnStsRemark = json.get("v1:TxnStsRemark").toString();
        this.FeeCode = json.get("v1:FeeCode").toString();
        this.PartID = json.get("v1:PartID").toString();
        this.ChannelTxnId = json.get("v1:ChannelTxnId").toString();
        this.TxnStsRemarkDes = json.get("v1:TxnStsRemarkDes").toString();
        this.TxnSts = json.get("v1:TxnSts").toString();
        this.TxnDes = json.get("v1:TxnDes").toString();
        this.TxnBnkID = json.get("v1:TxnBnkID").toString();
        this.TxnDate = json.get("v1:TxnDate").toString();
        this.FeeAmt = json.get("v1:FeeAmt").toString();
        this.Channel = json.get("v1:Channel").toString();
    }

    public InqTransactionStatusResponse() {
    }

    public String getTxnStsRemark() {
        return TxnStsRemark;
    }

    public void setTxnStsRemark(String TxnStsRemark) {
        this.TxnStsRemark = TxnStsRemark;
    }

    public String getFeeCode() {
        return FeeCode;
    }

    public void setFeeCode(String FeeCode) {
        this.FeeCode = FeeCode;
    }

    public String getPartID() {
        return PartID;
    }

    public void setPartID(String PartID) {
        this.PartID = PartID;
    }

    public String getChannelTxnId() {
        return ChannelTxnId;
    }

    public void setChannelTxnId(String ChannelTxnId) {
        this.ChannelTxnId = ChannelTxnId;
    }

    public String getTxnStsRemarkDes() {
        return TxnStsRemarkDes;
    }

    public void setTxnStsRemarkDes(String TxnStsRemarkDes) {
        this.TxnStsRemarkDes = TxnStsRemarkDes;
    }

    public String getTxnSts() {
        return TxnSts;
    }

    public void setTxnSts(String TxnSts) {
        this.TxnSts = TxnSts;
    }

    public String getTxnDes() {
        return TxnDes;
    }

    public void setTxnDes(String TxnDes) {
        this.TxnDes = TxnDes;
    }

    public String getTxnBnkID() {
        return TxnBnkID;
    }

    public void setTxnBnkID(String TxnBnkID) {
        this.TxnBnkID = TxnBnkID;
    }

    public String getTxnDate() {
        return TxnDate;
    }

    public void setTxnDate(String TxnDate) {
        this.TxnDate = TxnDate;
    }

    public String getFeeAmt() {
        return FeeAmt;
    }

    public void setFeeAmt(String FeeAmt) {
        this.FeeAmt = FeeAmt;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String Channel) {
        this.Channel = Channel;
    }
}
