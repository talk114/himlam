package gateway.core.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
//import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 * @author nnes
 */
@JsonRootName(value = "data")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DataRequest {

    protected Double amount;

    @JsonProperty("hash_code")
    protected String hashCode;

    @JsonProperty("onus")
    protected String onus;

    @JsonProperty("trans_data")
    protected String data;

    @JsonProperty("process_date")
    protected String processDate;

    @JsonProperty("file")
    protected MultipartFile file;

    public String getOnus() {
        return onus;
    }

    public void setOnus(String onus) {
        this.onus = onus;
    }

    @JsonProperty(value = "fee_amount")
    protected Double feeAmount;

    @JsonProperty("fee_percentage")
    protected String feePercentage;

    @JsonProperty(value = "currency_code")
    protected String currencyCode;

    @JsonProperty(value = "user_id")
    protected String userId; // ma vi dien tu (userId, Phone)

    @JsonProperty(value = "token")
    protected String token;

    @JsonProperty(value = "token_issue_date")
    protected String tokenIssueDate;

    @JsonProperty(value = "pay_method")
    protected String payMethod; // phuong thuc thanh toan (QRCODE)

    @JsonProperty(value = "goods_type")
    protected String goodsType; // loai dich vu: TOPUP...

    @JsonProperty(value = "withdraw_type")
    protected String withdrawType;

    @JsonProperty(value = "code_no1")
    protected String codeNo1;

    @JsonProperty(value = "code_no2")
    protected String codeNo2;

    @JsonProperty(value = "bill_no")
    protected String billNo; // So hoa don

    @JsonProperty(value = "trans_id")
    protected String transId;

    @JsonProperty(value = "bank_trans_id")
    protected String bankTransId;

    @JsonProperty(value = "ref_trans_id")
    protected String refTransId; // gd muốn hoàn tiền

    @JsonProperty("reversal_trans_id")
    protected String reversalTransId; // mã gd muốn đảo

    @JsonProperty("cancel_trans_id")
    protected String cancelTransId; // giao dịch muốn hủy

    @JsonProperty(value = "verify_trans_id")
    protected String verifyTransId; // mã giao dịch verify otp

    @JsonProperty(value = "query_trans_id")
    protected String queryTransId; // transaction query

    @JsonProperty(value = "inquiry_trans_id")
    protected String inquiryTransId; // transaction check card

    @JsonProperty(value = "trans_type")
    protected Short transType;

    @JsonProperty(value = "trans_time")
    protected String transTime;

    protected String otp;

    @JsonProperty(value = "card_number")
    protected String cardNumber;

    @JsonProperty(value = "card_token")
    protected String cardToken; // token the visa/master

    @JsonProperty(value = "card_holder_name")
    protected String cardHolerName;

    @JsonProperty(value = "card_issue_date")
    protected String cardIssueDate;

    @JsonProperty(value = "card_expire_date")
    protected String cardExpireDate;

    @JsonProperty(value = "card_type")
    protected String cardType;

    @JsonProperty("account_number")
    protected String bankAccountNumber;

    @JsonProperty("account_holder_name")
    protected String bankAccountHolderName;

    @JsonProperty(value = "issued_date")
    protected String issuedDate;

    @JsonProperty(value = "source_account")
    protected String sourceAccount;

    @JsonProperty(value = "dest_account")
    protected String destAccount;

    @JsonProperty(value = "account_type")
    protected String accountType;

    @JsonProperty(value = "bank_name")
    protected String bankName;

    @JsonProperty(value = "bank_code")
    protected String bankCode;

    @JsonProperty(value = "bank_branch_code")
    protected String bankBranchCode;

    @JsonProperty(value = "audit_number")
    protected String auditNumber;

    @JsonProperty(value = "description")
    protected String description;

    @JsonProperty(value = "order_code")
    protected String orderCode;

    @JsonProperty(value = "customer_phone_no")
    protected String custPhoneNo;

    @JsonProperty(value = "customer_id_no")
    protected String custIDNo; // so cmt, ho chieu

    @JsonProperty(value = "cust_id_issue_date")
    protected String custIDIssueDate; // ngay cap cmt, ho chieu

    @JsonProperty(value = "cust_id_issue_by")
    protected String custIDIssueBy; // noi cap

    @JsonProperty(value = "cust_gender")
    protected String custGender; // gioi tinh, M/F

    @JsonProperty(value = "cust_birthday")
    protected String custBirthday; // ngay sinh cua KH

    @JsonProperty(value = "cust_email")
    protected String custEmail; // email cua KH

    @JsonProperty(value = "receiver_name")
    protected String benName; // Ten nguoi thu huong (vi -> atm)

    @JsonProperty(value = "receiver_acct_no")
    protected String benAcctNo; // So tk nguoi thu thuong

    @JsonProperty(value = "receiver_bank_id")
    protected String benBankId; // Mã ngân hàng thụ hưởng

    @JsonProperty(value = "receiver_bank_name")
    protected String benBankName; // Tên ngân hàng thụ hưởng

    @JsonProperty(value = "receiver_branch_id")
    protected String benBranchId; // Mã chi nhánh thụ hưởngac

    @JsonProperty(value = "receiver_branch_name")
    protected String benBranchName; // Tên chi nhánh thụ hưởng

    @JsonProperty(value = "receiver_id_no")
    protected String benIDNo; // So cmt ng thu huong

    @JsonProperty(value = "receiver_id_issue_date")
    protected String benIDIssueDate; // Ngay cap cmt

    @JsonProperty(value = "receiver_id_issue_by")
    protected String benIDIssueBy; // Noi cap cmt

    @JsonProperty(value = "receiver_info")
    protected String benAddInfo; // Thong tin bo sung

    @JsonProperty(value = "receiver_mobile_num")
    protected String benMobileNum; // Sdt nguoi nhan cardless

    @JsonProperty(value = "query_type")
    protected String queryType; // 01 -Van tin kq thanh toan/rut tien, 02 - Van
    // tin kq cap toke, 03 - Van tin kq dang ky

    protected String channel;

    protected String version;

    protected String language;

    @JsonProperty(value = "client_ip")
    protected String clientIp;

    protected String mac;

    @JsonProperty(value = "verify_method")
    protected String verifyBy; // Phuong thuc xac thuc OTP: "SMS, RSA"

    @JsonProperty(value = "verify_type")
    protected String verifyType;

    @JsonProperty(value = "query_trans_date")
    protected String queryTransDate;

    @JsonProperty(value = "query_from")
    protected String queryFrom;

    @JsonProperty(value = "query_to")
    protected String queryTo;

    @JsonProperty(value = "query_status")
    protected String queryStatus;

    /* ================================ VAY MUON QR CODE ================*/
    @JsonProperty(value = "supplier_name")
    private String supplierName;

    @JsonProperty(value = "supplier_code")
    private String supplierCode;

    @JsonProperty(value = "supplier_phone")
    private String supplierPhone;

    @JsonProperty(value = "supplier_email")
    private String supplierEmail;

    @JsonProperty(value = "supplier_address")
    private String supplierAddress;

    @JsonProperty(value = "customer_name")
    private String customerName;

    @JsonProperty(value = "customer_phone")
    private String customerPhone;

    @JsonProperty(value = "customer_email")
    private String customerEmail;

    @JsonProperty(value = "customer_id_number")
    private String customerIdNumber;

    @JsonProperty(value = "bill_code")
    private String billCode;


    @JsonProperty(value = "order_payment_method")
    private String orderPaymentMethod;

    @JsonProperty(value = "order_loan_duration")
    private String orderLoanDuration;

    @JsonProperty(value = "order_note")
    private String orderNote;

    @JsonProperty(value = "cashier_name")
    private String cashierName;

    @JsonProperty(value = "cashier_phone")
    private String cashierPhone;

    /* ================================ BIDV ========================== */
    @JsonProperty(value = "user_name")
    protected String userName; // ten ng dung Vi

    @JsonProperty(value = "user_address")
    protected String userAddress; // dia chi ng dung Vi

    @JsonProperty(value = "link_type")
    protected String linkType; // Loai lien ket (0/1) 1: lien ket vi

    // BIDV
    @JsonProperty(value = "more_info")
    protected String moreInfo;

    // Napas
    @JsonProperty(value = "process_code")
    protected String processCode;

    @JsonProperty(value = "apiOperation")
    private String apiOperation;

    @JsonProperty(value = "clientDeviceId")
    private String clientDeviceId;

    @JsonProperty(value = "clientEnviroment")
    private String clientEnviroment;

    @JsonProperty(value = "enable3DSecure")
    private String enable3DSecure;

    // VTB Qrcode
    @JsonProperty(value = "terminal_id")
    protected String terminalId;

    @JsonProperty(value = "product_id")
    protected String productId;

    @JsonProperty(value = "merchant_name")
    protected String merchantName;

    @JsonProperty(value = "merchant_id")
    protected String merchantId;

    // OnePay
    @JsonProperty("vpc_Version")
    private String vpcVersion;

    @JsonProperty(value = "vpc_OrgMerchTxnRef")
    private String vpcOrgMerchTxnRef;

    @JsonProperty(value = "vpc_Operator")
    private String vpcOperator;

    @JsonProperty("vpc_Command")
    private String vpcCommand;

    @JsonProperty("vpc_AccessCode")
    private String vpcAccessCode;

    @JsonProperty("vpc_Merchant")
    private String vpcMerchant;

    @JsonProperty("vpc_MerchTxnRef")
    private String vpcMerchTxnRef;

    @JsonProperty("vpc_OrderInfo")
    private String vpcOrderInfo;

    @JsonProperty("vpc_Amount")
    private String vpcAmount;

    @JsonProperty("vpc_Locale")
    private String vpcLocale;

    @JsonProperty("vpc_Currency")
    private String vpcCurrency;

    @JsonProperty("vpc_TicketNo")
    private String vpcTicketNo;

    @JsonProperty("vpc_ReturnURL")
    private String vpcReturnURL;

    @JsonProperty("vpc_SecureHash")
    private String vpcSecureHash;

    @JsonProperty("AgainLink")
    private String againLink;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("vpc_CustomerUserAgent")
    private String vpcCustomerUserAgent;

    @JsonProperty("vpc_BankId")
    private String vpcBankId;

    @JsonProperty("vpc_CardNo")
    private String vpcCardNo;

    @JsonProperty("vpc_CardName")
    private String vpcCardName;

    @JsonProperty("vpc_CardMonth")
    private String vpcCardMonth;

    @JsonProperty("vpc_CardYear")
    private String vpcCardYear;

    @JsonProperty("vpc_CardType")
    private String vpcCardType;

    @JsonProperty("vpc_AuthMethod")
    private String vpcAuthMethod;

    @JsonProperty("vpc_MobileNumber")
    private String vpcMobileNumber;

    @JsonProperty("vpc_AuthURL")
    private String vpcAuthURL;

    @JsonProperty("vpc_Otp")
    private String vpcOtp;

    @JsonProperty("vpc_User")
    private String vpcUser;

    @JsonProperty("vpc_Password")
    private String vpcPassword;

    // VCB QRCODE
    @JsonProperty("expire_date")
    private String expireDate; // ngay het han qr

    @JsonProperty("purpose")
    private String purpose;

    @JsonProperty("qr_trace")
    private String qrTrace;

    @JsonProperty("type_refund")
    private String typeRefund; // 1 phan, toan phan

    @JsonProperty("service_type")
    private String serviceType;

    /* ================================ CITIBANK ========================== */

    @JsonProperty(value = "grant_type")
    private String grantType;

    @JsonProperty(value = "controlFlowId")
    private String controlFlowId;

    @JsonProperty(value = "linkageConfirmationCode")
    private String linkageConfirmationCode;
    @JsonProperty(value = "notify_url")
    private String notifyUrl;

    @JsonProperty(value = "prepay_id")
    private String prepayId;

    @JsonProperty(value = "scope")
    private String scope;

    @JsonProperty(value = "lastFourDigitsCardNumber")
    private String lastFourDigitsCardNumber;
    @JsonProperty(value = "trans_id_OTP")
    private String transIdOTP;

    @JsonProperty(value = "citiCardHolderPhoneNumber")
    private String citiCardHolderPhoneNumber;

    @JsonProperty(value = "merchantCustomerReferenceId")
    private String merchantCustomerReferenceId;

    @JsonProperty(value = "eligbleLoanAmount")
    private double eligbleLoanAmount;

    @JsonProperty(value = "eppLoanBookingType")
    private String eppLoanBookingType;

    @JsonProperty(value = "tenor")
    private int tenor;

    @JsonProperty(value = "card_id")
    private String cardId;

//    @JsonProperty(value = "eppLoanBooking")
//    private List<pg.channel.citibank.req.body.EppLoanBooking> EppLoanBooking;

    @JsonProperty(value = "transactionAuthorizationCode")
    private String transactionAuthorizationCode;

    @JsonProperty(value = "client_id")
    private String clientId;

    @JsonProperty(value = "client_secret")
    private String clientSecret;

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "chooseLanguage")
    private String chooseLanguage;

    //---- VCB ECOM ATM-----
    @JsonProperty(value = "partnerId")
    private String partnerId;

    @JsonProperty(value = "partnerPassword")
    private String partnerPassword;

    //---- VCB IB-----
    @JsonProperty(value = "partnerRedirectUrl")
    private String partnerRedirectUrl;

    @JsonProperty(value = "transId")
    private String transactionId;

    @JsonProperty(value = "benAddInfo")
    protected String addInfo; // Thong tin bo sung

    //---- MB-----
    @JsonProperty(value = "qrcodeType")
    private int qrcodeType;

    @JsonProperty(value = "initMethod")
    private int initMethod;

    @JsonProperty(value = "billNumber")
    private String billNumber;

    @JsonProperty(value = "additionalAddress")
    private int additionalAddress;

    @JsonProperty(value = "additionalMobile")
    private int additionalMobile;

    @JsonProperty(value = "additionalEmail")
    private int additionalEmail;

    @JsonProperty(value = "consumerLabel")
    private String consumerLabel;

    @JsonProperty(value = "term")
    private String term;

    @JsonProperty(value = "referenceLabelTime")
    private String referenceLabelTime;

    @JsonProperty(value = "referenceLabelCode")
    private String referenceLabelCode;

    @JsonProperty(value = "transactionPurpose")
    private String transactionPurpose;
    @JsonProperty(value = "traceTransfer")
    private String traceTransfer;
    @JsonProperty(value = "merchantCode")
    private String merchantCode;

    public DataRequest() {
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerPassword() {
        return partnerPassword;
    }

    public void setPartnerPassword(String partnerPassword) {
        this.partnerPassword = partnerPassword;
    }

    public String getVpcOperator() {
        return vpcOperator;
    }

    public void setVpcOperator(String vpcOperator) {
        this.vpcOperator = vpcOperator;
    }

    public String getVpcOrgMerchTxnRef() {
        return vpcOrgMerchTxnRef;
    }

    public void setVpcOrgMerchTxnRef(String vpcOrgMerchTxnRef) {
        this.vpcOrgMerchTxnRef = vpcOrgMerchTxnRef;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getProcessDate() {
        return processDate;
    }

    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getVpcUser() {
        return vpcUser;
    }

    public void setVpcUser(String vpcUser) {
        this.vpcUser = vpcUser;
    }

    public String getVpcPassword() {
        return vpcPassword;
    }

    public void setVpcPassword(String vpcPassword) {
        this.vpcPassword = vpcPassword;
    }

    public String getVpcAuthURL() {
        return vpcAuthURL;
    }

    public void setVpcAuthURL(String vpcAuthURL) {
        this.vpcAuthURL = vpcAuthURL;
    }

    public String getVpcOtp() {
        return vpcOtp;
    }

    public void setVpcOtp(String vpcOtp) {
        this.vpcOtp = vpcOtp;
    }

    public String getVpcVersion() {
        return vpcVersion;
    }

    public void setVpcVersion(String vpcVersion) {
        this.vpcVersion = vpcVersion;
    }

    public String getVpcCommand() {
        return vpcCommand;
    }

    public void setVpcCommand(String vpcCommand) {
        this.vpcCommand = vpcCommand;
    }

    public String getVpcAccessCode() {
        return vpcAccessCode;
    }

    public void setVpcAccessCode(String vpcAccessCode) {
        this.vpcAccessCode = vpcAccessCode;
    }

    public String getVpcMerchant() {
        return vpcMerchant;
    }

    public void setVpcMerchant(String vpcMerchant) {
        this.vpcMerchant = vpcMerchant;
    }

    public String getVpcMerchTxnRef() {
        return vpcMerchTxnRef;
    }

    public void setVpcMerchTxnRef(String vpcMerchTxnRef) {
        this.vpcMerchTxnRef = vpcMerchTxnRef;
    }

    public String getVpcOrderInfo() {
        return vpcOrderInfo;
    }

    public void setVpcOrderInfo(String vpcOrderInfo) {
        this.vpcOrderInfo = vpcOrderInfo;
    }

    public String getVpcAmount() {
        return vpcAmount;
    }

    public void setVpcAmount(String vpcAmount) {
        this.vpcAmount = vpcAmount;
    }

    public String getVpcLocale() {
        return vpcLocale;
    }

    public void setVpcLocale(String vpcLocale) {
        this.vpcLocale = vpcLocale;
    }

    public String getVpcCurrency() {
        return vpcCurrency;
    }

    public void setVpcCurrency(String vpcCurrency) {
        this.vpcCurrency = vpcCurrency;
    }

    public String getVpcTicketNo() {
        return vpcTicketNo;
    }

    public void setVpcTicketNo(String vpcTicketNo) {
        this.vpcTicketNo = vpcTicketNo;
    }

    public String getVpcReturnURL() {
        return vpcReturnURL;
    }

    public void setVpcReturnURL(String vpcReturnURL) {
        this.vpcReturnURL = vpcReturnURL;
    }

    public String getVpcSecureHash() {
        return vpcSecureHash;
    }

    public void setVpcSecureHash(String vpcSecureHash) {
        this.vpcSecureHash = vpcSecureHash;
    }

    public String getAgainLink() {
        return againLink;
    }

    public void setAgainLink(String againLink) {
        this.againLink = againLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVpcCustomerUserAgent() {
        return vpcCustomerUserAgent;
    }

    public void setVpcCustomerUserAgent(String vpcCustomerUserAgent) {
        this.vpcCustomerUserAgent = vpcCustomerUserAgent;
    }

    public String getVpcBankId() {
        return vpcBankId;
    }

    public void setVpcBankId(String vpcBankId) {
        this.vpcBankId = vpcBankId;
    }

    public String getVpcCardNo() {
        return vpcCardNo;
    }

    public void setVpcCardNo(String vpcCardNo) {
        this.vpcCardNo = vpcCardNo;
    }

    public String getVpcCardName() {
        return vpcCardName;
    }

    public void setVpcCardName(String vpcCardName) {
        this.vpcCardName = vpcCardName;
    }

    public String getVpcCardMonth() {
        return vpcCardMonth;
    }

    public void setVpcCardMonth(String vpcCardMonth) {
        this.vpcCardMonth = vpcCardMonth;
    }

    public String getVpcCardYear() {
        return vpcCardYear;
    }

    public void setVpcCardYear(String vpcCardYear) {
        this.vpcCardYear = vpcCardYear;
    }

    public String getVpcCardType() {
        return vpcCardType;
    }

    public void setVpcCardType(String vpcCardType) {
        this.vpcCardType = vpcCardType;
    }

    public String getVpcAuthMethod() {
        return vpcAuthMethod;
    }

    public void setVpcAuthMethod(String vpcAuthMethod) {
        this.vpcAuthMethod = vpcAuthMethod;
    }

    public String getVpcMobileNumber() {
        return vpcMobileNumber;
    }

    public void setVpcMobileNumber(String vpcMobileNumber) {
        this.vpcMobileNumber = vpcMobileNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getFeePercentage() {
        return feePercentage;
    }

    public void setFeePercentage(String feePercentage) {
        this.feePercentage = feePercentage;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getBankTransId() {
        return bankTransId;
    }

    public void setBankTransId(String bankTransId) {
        this.bankTransId = bankTransId;
    }

    public String getRefTransId() {
        return refTransId;
    }

    public void setRefTransId(String refTransId) {
        this.refTransId = refTransId;
    }

    public String getReversalTransId() {
        return reversalTransId;
    }

    public void setReversalTransId(String reversalTransId) {
        this.reversalTransId = reversalTransId;
    }

    public String getCancelTransId() {
        return cancelTransId;
    }

    public void setCancelTransId(String cancelTransId) {
        this.cancelTransId = cancelTransId;
    }

    public String getVerifyTransId() {
        return verifyTransId;
    }

    public void setVerifyTransId(String verifyTransId) {
        this.verifyTransId = verifyTransId;
    }

    public String getQueryTransId() {
        return queryTransId;
    }

    public void setQueryTransId(String queryTransId) {
        this.queryTransId = queryTransId;
    }

    public String getInquiryTransId() {
        return inquiryTransId;
    }

    public void setInquiryTransId(String inquiryTransId) {
        this.inquiryTransId = inquiryTransId;
    }

    public Short getTransType() {
        return transType;
    }

    public void setTransType(Short transType) {
        this.transType = transType;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getCardHolerName() {
        return cardHolerName;
    }

    public void setCardHolerName(String cardHolerName) {
        this.cardHolerName = cardHolerName;
    }

    public String getCardIssueDate() {
        return cardIssueDate;
    }

    public void setCardIssueDate(String cardIssueDate) {
        this.cardIssueDate = cardIssueDate;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    // public String getDateTime() {
    // return dateTime;
    // }

    // public void setDateTime(String dateTime) {
    // this.dateTime = dateTime;
    // }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getDestAccount() {
        return destAccount;
    }

    public void setDestAccount(String destAccount) {
        this.destAccount = destAccount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankBranchCode() {
        return bankBranchCode;
    }

    public void setBankBranchCode(String bankBranchCode) {
        this.bankBranchCode = bankBranchCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuditNumber() {
        return auditNumber;
    }

    public void setAuditNumber(String auditNumber) {
        this.auditNumber = auditNumber;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCustPhoneNo() {
        return custPhoneNo;
    }

    public void setCustPhoneNo(String custPhoneNo) {
        this.custPhoneNo = custPhoneNo;
    }

    public String getCustIDNo() {
        return custIDNo;
    }

    public void setCustIDNo(String custIDNo) {
        this.custIDNo = custIDNo;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVerifyBy() {
        return verifyBy;
    }

    public void setVerifyBy(String verifyBy) {
        this.verifyBy = verifyBy;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public String getTokenIssueDate() {
        return tokenIssueDate;
    }

    public void setTokenIssueDate(String tokenIssueDate) {
        this.tokenIssueDate = tokenIssueDate;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getCustIDIssueDate() {
        return custIDIssueDate;
    }

    public void setCustIDIssueDate(String custIDIssueDate) {
        this.custIDIssueDate = custIDIssueDate;
    }

    public String getCustIDIssueBy() {
        return custIDIssueBy;
    }

    public void setCustIDIssueBy(String custIDIssueBy) {
        this.custIDIssueBy = custIDIssueBy;
    }

    public String getCustGender() {
        return custGender;
    }

    public void setCustGender(String custGender) {
        this.custGender = custGender;
    }

    public String getCustBirthday() {
        return custBirthday;
    }

    public void setCustBirthday(String custBirthday) {
        this.custBirthday = custBirthday;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getBenName() {
        return benName;
    }

    public void setBenName(String benName) {
        this.benName = benName;
    }

    public String getBenAcctNo() {
        return benAcctNo;
    }

    public void setBenAcctNo(String benAcctNo) {
        this.benAcctNo = benAcctNo;
    }

    public String getBenBankId() {
        return benBankId;
    }

    public void setBenBankId(String benBankId) {
        this.benBankId = benBankId;
    }

    public String getBenIDNo() {
        return benIDNo;
    }

    public void setBenIDNo(String benIDNo) {
        this.benIDNo = benIDNo;
    }

    public String getBenAddInfo() {
        return benAddInfo;
    }

    public void setBenAddInfo(String benAddInfo) {
        this.benAddInfo = benAddInfo;
    }

    public String getBenMobileNum() {
        return benMobileNum;
    }

    public void setBenMobileNum(String benMobileNum) {
        this.benMobileNum = benMobileNum;
    }

    public String getBenIDIssueDate() {
        return benIDIssueDate;
    }

    public void setBenIDIssueDate(String benIDIssueDate) {
        this.benIDIssueDate = benIDIssueDate;
    }

    public String getBenIDIssueBy() {
        return benIDIssueBy;
    }

    public void setBenIDIssueBy(String benIDIssueBy) {
        this.benIDIssueBy = benIDIssueBy;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getQueryTransDate() {
        return queryTransDate;
    }

    public void setQueryTransDate(String queryTransDate) {
        this.queryTransDate = queryTransDate;
    }

    public String getQueryFrom() {
        return queryFrom;
    }

    public void setQueryFrom(String queryFrom) {
        this.queryFrom = queryFrom;
    }

    public String getQueryTo() {
        return queryTo;
    }

    public void setQueryTo(String queryTo) {
        this.queryTo = queryTo;
    }

    public String getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(String queryStatus) {
        this.queryStatus = queryStatus;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(String withdrawType) {
        this.withdrawType = withdrawType;
    }

    public String getCodeNo1() {
        return codeNo1;
    }

    public void setCodeNo1(String codeNo1) {
        this.codeNo1 = codeNo1;
    }

    public String getCodeNo2() {
        return codeNo2;
    }

    public void setCodeNo2(String codeNo2) {
        this.codeNo2 = codeNo2;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getQrTrace() {
        return qrTrace;
    }

    public void setQrTrace(String qrTrace) {
        this.qrTrace = qrTrace;
    }

    public String getTypeRefund() {
        return typeRefund;
    }

    public void setTypeRefund(String typeRefund) {
        this.typeRefund = typeRefund;
    }

    public String getCardExpireDate() {
        return cardExpireDate;
    }

    public void setCardExpireDate(String cardExpireDate) {
        this.cardExpireDate = cardExpireDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountHolderName() {
        return bankAccountHolderName;
    }

    public void setBankAccountHolderName(String bankAccountHolderName) {
        this.bankAccountHolderName = bankAccountHolderName;
    }

    public String getBenBankName() {
        return benBankName;
    }

    public void setBenBankName(String benBankName) {
        this.benBankName = benBankName;
    }

    public String getBenBranchId() {
        return benBranchId;
    }

    public void setBenBranchId(String benBranchId) {
        this.benBranchId = benBranchId;
    }

    public String getBenBranchName() {
        return benBranchName;
    }

    public void setBenBranchName(String benBranchName) {
        this.benBranchName = benBranchName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerIdNumber() {
        return customerIdNumber;
    }

    public void setCustomerIdNumber(String customerIdNumber) {
        this.customerIdNumber = customerIdNumber;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(String orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public String getOrderLoanDuration() {
        return orderLoanDuration;
    }

    public void setOrderLoanDuration(String orderLoanDuration) {
        this.orderLoanDuration = orderLoanDuration;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getCashierPhone() {
        return cashierPhone;
    }

    public void setCashierPhone(String cashierPhone) {
        this.cashierPhone = cashierPhone;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getControlFlowId() {
        return controlFlowId;
    }

    public void setControlFlowId(String controlFlowId) {
        this.controlFlowId = controlFlowId;
    }

    public String getLinkageConfirmationCode() {
        return linkageConfirmationCode;
    }

    public void setLinkageConfirmationCode(String linkageConfirmationCode) {
        this.linkageConfirmationCode = linkageConfirmationCode;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getLastFourDigitsCardNumber() {
        return lastFourDigitsCardNumber;
    }

    public void setLastFourDigitsCardNumber(String lastFourDigitsCardNumber) {
        this.lastFourDigitsCardNumber = lastFourDigitsCardNumber;
    }

    public String getTransIdOTP() {
        return transIdOTP;
    }

    public void setTransIdOTP(String transIdOTP) {
        this.transIdOTP = transIdOTP;
    }

    public String getCitiCardHolderPhoneNumber() {
        return citiCardHolderPhoneNumber;
    }

    public void setCitiCardHolderPhoneNumber(String citiCardHolderPhoneNumber) {
        this.citiCardHolderPhoneNumber = citiCardHolderPhoneNumber;
    }

    public String getMerchantCustomerReferenceId() {
        return merchantCustomerReferenceId;
    }

    public void setMerchantCustomerReferenceId(String merchantCustomerReferenceId) {
        this.merchantCustomerReferenceId = merchantCustomerReferenceId;
    }

    public double getEligbleLoanAmount() {
        return eligbleLoanAmount;
    }

    public void setEligbleLoanAmount(double eligbleLoanAmount) {
        this.eligbleLoanAmount = eligbleLoanAmount;
    }

    public String getEppLoanBookingType() {
        return eppLoanBookingType;
    }

    public void setEppLoanBookingType(String eppLoanBookingType) {
        this.eppLoanBookingType = eppLoanBookingType;
    }

    public int getTenor() {
        return tenor;
    }

    public void setTenor(int tenor) {
        this.tenor = tenor;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

//    public List<pg.channel.citibank.req.body.EppLoanBooking> getEppLoanBooking() {
//        return EppLoanBooking;
//    }
//
//    public void setEppLoanBooking(List<pg.channel.citibank.req.body.EppLoanBooking> eppLoanBooking) {
//        EppLoanBooking = eppLoanBooking;
//    }

    public String getTransactionAuthorizationCode() {
        return transactionAuthorizationCode;
    }

    public void setTransactionAuthorizationCode(String transactionAuthorizationCode) {
        this.transactionAuthorizationCode = transactionAuthorizationCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getChooseLanguage() {
        return chooseLanguage;
    }

    public void setChooseLanguage(String chooseLanguage) {
        this.chooseLanguage = chooseLanguage;
    }

    public String getPartnerRedirectUrl() {
        return partnerRedirectUrl;
    }

    public void setPartnerRedirectUrl(String partnerRedirectUrl) {
        this.partnerRedirectUrl = partnerRedirectUrl;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(String addInfo) {
        this.addInfo = addInfo;
    }

    public String getApiOperation() {
        return apiOperation;
    }

    public void setApiOperation(String apiOperation) {
        this.apiOperation = apiOperation;
    }

    public String getClientDeviceId() {
        return clientDeviceId;
    }

    public void setClientDeviceId(String clientDeviceId) {
        this.clientDeviceId = clientDeviceId;
    }

    public String getClientEnviroment() {
        return clientEnviroment;
    }

    public void setClientEnviroment(String clientEnviroment) {
        this.clientEnviroment = clientEnviroment;
    }

    public String getEnable3DSecure() {
        return enable3DSecure;
    }

    public void setEnable3DSecure(String enable3DSecure) {
        this.enable3DSecure = enable3DSecure;
    }

    public int getQrcodeType() {
        return qrcodeType;
    }

    public void setQrcodeType(int qrcodeType) {
        this.qrcodeType = qrcodeType;
    }

    public int getInitMethod() {
        return initMethod;
    }

    public void setInitMethod(int initMethod) {
        this.initMethod = initMethod;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public int getAdditionalAddress() {
        return additionalAddress;
    }

    public void setAdditionalAddress(int additionalAddress) {
        this.additionalAddress = additionalAddress;
    }

    public int getAdditionalMobile() {
        return additionalMobile;
    }

    public void setAdditionalMobile(int additionalMobile) {
        this.additionalMobile = additionalMobile;
    }

    public int getAdditionalEmail() {
        return additionalEmail;
    }

    public void setAdditionalEmail(int additionalEmail) {
        this.additionalEmail = additionalEmail;
    }

    public String getConsumerLabel() {
        return consumerLabel;
    }

    public void setConsumerLabel(String consumerLabel) {
        this.consumerLabel = consumerLabel;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getReferenceLabelTime() {
        return referenceLabelTime;
    }

    public void setReferenceLabelTime(String referenceLabelTime) {
        this.referenceLabelTime = referenceLabelTime;
    }

    public String getReferenceLabelCode() {
        return referenceLabelCode;
    }

    public void setReferenceLabelCode(String referenceLabelCode) {
        this.referenceLabelCode = referenceLabelCode;
    }

    public String getTransactionPurpose() {
        return transactionPurpose;
    }

    public void setTransactionPurpose(String transactionPurpose) {
        this.transactionPurpose = transactionPurpose;
    }

    public String getTraceTransfer() {
        return traceTransfer;
    }

    public void setTraceTransfer(String traceTransfer) {
        this.traceTransfer = traceTransfer;
    }
}
