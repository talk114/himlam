/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.tcb.service.schedule;

/**
 *
 * @author taind
 */
 public class ResponseDataVIBIBFT {
    
    private String type;
    private String compareCode;
    private String transactionType;
    private String accountSender;
    private String accountReceiver;
    private String amount;
    private String currency;
    private String description;
    private String time;
    private String date;
    private String authenNumber;
    private String traceNL;
    private String fee;
    private String napasResponse;
    private String resultCompare;
    private String checksum;

    public ResponseDataVIBIBFT(String[] data) {
        this.type = data[0].toString();
        this.compareCode = data[1].toString();
        this.transactionType = data[2].toString();
        this.accountSender = data[3].toString();
        this.accountReceiver = data[4].toString();
        this.amount = data[5].toString();
        this.currency = data[6].toString();
        this.description = data[7].toString();
        this.time = data[8].toString();
        this.date = data[9].toString();
        this.authenNumber = data[10].toString();
        this.traceNL = data[11].toString();
        this.fee = data[12].toString();
        this.napasResponse = data[13].toString();
        this.resultCompare = data[14].toString();
        this.checksum = data[15].toString();
    }

    public ResponseDataVIBIBFT() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompareCode() {
        return compareCode;
    }

    public void setCompareCode(String compareCode) {
        this.compareCode = compareCode;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAccountSender() {
        return accountSender;
    }

    public void setAccountSender(String accountSender) {
        this.accountSender = accountSender;
    }

    public String getAccountReceiver() {
        return accountReceiver;
    }

    public void setAccountReceiver(String accountReceiver) {
        this.accountReceiver = accountReceiver;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthenNumber() {
        return authenNumber;
    }

    public void setAuthenNumber(String authenNumber) {
        this.authenNumber = authenNumber;
    }

    public String getTraceNL() {
        return traceNL;
    }

    public void setTraceNL(String traceNL) {
        this.traceNL = traceNL;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getNapasResponse() {
        return napasResponse;
    }

    public void setNapasResponse(String napasResponse) {
        this.napasResponse = napasResponse;
    }

    public String getResultCompare() {
        return resultCompare;
    }

    public void setResultCompare(String resultCompare) {
        this.resultCompare = resultCompare;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
