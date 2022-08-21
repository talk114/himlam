package gateway.core.channel.cybersouce.util;

import com.google.common.base.Strings;
import gateway.core.channel.cybersouce.dto.CybersourceConfig;
import gateway.core.channel.cybersouce.request.*;
import gateway.core.channel.cybersouce.response.Response;
import gateway.core.channel.cybersouce.soap.SOAPUtil;
import gateway.core.dto.PGResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import vn.nganluong.naba.service.CybersourceResponseService;
import vn.nganluong.naba.utils.Constant;
import vn.nganluong.naba.utils.CybersourceError;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author TaiND
 */
@Service
public class CybersourceUtil {

    @Autowired
    private CybersourceResponseService cybersourceResponseService;
    private final static String RESPONSE = "RESPONSE";
    private final static String TEMPDATA = "TEMPDATA";

    public PGResponse createCustomerProfile(Map<String, String> map) {
        Map<String, Object> result = baseMethod(map, Constant.AUTHORIZE_CARD);
        Response rs = (Response) result.get(RESPONSE);
        Node temp = (Node) result.get(TEMPDATA);
        if (rs.isSuccess()) {
            rs.getData().put("subscriptionId", SOAPUtil.getValue(temp, "paySubscriptionCreateReply.subscriptionID"));
        } else if (rs.getResponseCode().equals("475")) {
            rs.getData().put("acsURL", SOAPUtil.getValue(temp, "payerAuthEnrollReply.acsURL"));
            rs.getData().put("paReq", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paReq"));
            rs.getData().put("xid", SOAPUtil.getValue(temp, "payerAuthEnrollReply.xid"));
        } else if (rs.getResponseCode().equals("102")) {
            rs.getData().put("invalidField", CybsUtil.subStringData(SOAPUtil.getValue(temp, "invalidField")));
        } else if (rs.getResponseCode().equals("101")) {
            rs.getData().put("missingField", CybsUtil.subStringData(SOAPUtil.getValue(temp, "missingField")));
        }
        rs.getData().put("transactionReferenceId", SOAPUtil.getValue(temp, "merchantReferenceCode"));
        rs.getData().put("resultMessage", getResponseMessage(rs.getResponseCode()));
        PGResponse response = new PGResponse();
        response.setData(rs);
        response.setStatus(true);
        response.setChannelErrorCode(rs.getResponseCode());
        response.setChannelMessage(getResponseMessage(rs.getResponseCode()));
        response.setErrorCode(CybersourceError.SUCCESS.getCode());
        response.setMessage(CybersourceError.SUCCESS.getMessage());
        return response;
    }

    private Map<String, Object> baseMethod(Map<String, String> map, final String operation) {
        Map<String, Object> result = new HashMap<>();
        Node temp = SOAPUtil.runProcessGetReplyMessage(map);
        String code = SOAPUtil.getValue(temp, "reasonCode");
        String decision = SOAPUtil.getValue(temp, "decision");
        String requestID = SOAPUtil.getValue(temp, "requestID");
        String requestToken = SOAPUtil.getValue(temp, "requestToken");
        String authorizationCode = SOAPUtil.getValue(temp, "ccAuthReply.authorizationCode");
        String eci = SOAPUtil.getValue(temp, "payerAuthValidateReply.eciRaw");
        if (!Strings.isNullOrEmpty(eci)) {
            eci = SOAPUtil.getValue(temp, "payerAuthEnrollReply.eciRaw");
        } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafCollectionIndicator"))) {
            eci = SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafCollectionIndicator");
        } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.eci"))) {
            eci = SOAPUtil.getValue(temp, "payerAuthEnrollReply.eci");
        } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthValidateReply.eci"))) {
            eci = SOAPUtil.getValue(temp, "payerAuthValidateReply.eci");
        } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthValidateReply.ucafCollectionIndicator"))) {
            eci = SOAPUtil.getValue(temp, "payerAuthValidateReply.ucafCollectionIndicator");
        }
        Map<String, String> keyInfo = SOAPUtil.getCyberKeyInfo(map.get("merchantID"));
        Response res = new Response("100".equals(code), decision, code, requestID, authorizationCode, eci, operation,
                keyInfo.get("MID"), CybersourceConfig.ENVIROMENT_KEY, null);
        Map<String, String> data = new HashMap<>();
        res.setData(data);

        data.put("requestToken", requestToken);
        result.put("RESPONSE", res);
        result.put("TEMPDATA", temp);
        return result;
    }

    public PGResponse runTransaction(final Map<String, String> request, final String operation) {
        Map<String, String> keyInfo = SOAPUtil.getCyberKeyInfo(request.get("merchantID"));
        Response response = new Response(keyInfo.get("MID"), CybersourceConfig.ENVIROMENT_KEY);
        try {
            Map<String, Object> result = baseMethod(request, operation);
            response = (Response) result.get(RESPONSE);
            Node temp = (Node) result.get(TEMPDATA);
            if (response.isSuccess()) {
                response.getData().put("amount", SOAPUtil.getValue(temp, "ccAuthReply.amount"));
                response.getData().put("currency", SOAPUtil.getValue(temp, "purchaseTotals.currency"));
            } else if (response.getResponseCode().equals("475")) {
                response.getData().put("acsURL", SOAPUtil.getValue(temp, "payerAuthEnrollReply.acsURL"));
                response.getData().put("paReq", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paReq"));
                response.getData().put("xid", SOAPUtil.getValue(temp, "payerAuthEnrollReply.xid"));
                response.getData().put("TransactionID", SOAPUtil.getValue(temp, "payerAuthEnrollReply.authenticationTransactionID"));
            } else if (response.getResponseCode().equals("102")) {
                response.getData().put("invalidField", CybsUtil.subStringData(SOAPUtil.getValue(temp, "invalidField")));
            } else if (response.getResponseCode().equals("101")) {
                response.getData().put("missingField", CybsUtil.subStringData(SOAPUtil.getValue(temp, "missingField")));
            }
            response.getData().put("transactionReferenceId", SOAPUtil.getValue(temp, "merchantReferenceCode"));
            response.getData().put("resultMessage", getResponseMessage(response.getResponseCode()));

        } catch (Exception e) {
            response.getData().put("resultMessage", getResponseMessage(response.getResponseCode()));
        }
        PGResponse rs = new PGResponse();
        rs.setData(response);
        rs.setStatus(true);
        rs.setChannelErrorCode(response.getResponseCode());
        rs.setChannelMessage(getResponseMessage(response.getResponseCode()));
        rs.setErrorCode(CybersourceError.SUCCESS.getCode());
        rs.setMessage(CybersourceError.SUCCESS.getMessage());
        return rs;
    }

    public PGResponse checkEnrollment(Map<String, String> map) {
        Map<String, Object> result = baseMethod(map, Constant.CHECK_ENROLLMENT);
        Response rs = (Response) result.get(RESPONSE);
        Node temp = (Node) result.get(TEMPDATA);
        if (rs.getResponseCode().equals("475")) {
            rs.getData().put("acsURL", SOAPUtil.getValue(temp, "payerAuthEnrollReply.acsURL"));
            rs.getData().put("paReq", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paReq"));
            rs.getData().put("xid", SOAPUtil.getValue(temp, "payerAuthEnrollReply.xid"));
        } else if (rs.getResponseCode().equals("102")) {
            rs.getData().put("invalidField", CybsUtil.subStringData(SOAPUtil.getValue(temp, "invalidField")));
        } else if (rs.getResponseCode().equals("101")) {
            rs.getData().put("missingField", CybsUtil.subStringData(SOAPUtil.getValue(temp, "missingField")));
        }
        rs.getData().put("transactionReferenceId", SOAPUtil.getValue(temp, "merchantReferenceCode"));
        rs.getData().put("resultMessage", getResponseMessage(rs.getResponseCode()));
        PGResponse response = new PGResponse();
        response.setData(rs);
        response.setStatus(true);
        response.setChannelErrorCode(rs.getResponseCode());
        response.setChannelMessage(getResponseMessage(rs.getResponseCode()));
        response.setErrorCode(CybersourceError.SUCCESS.getCode());
        response.setMessage(CybersourceError.SUCCESS.getMessage());
        return response;
    }

    public void addInvoiceHeader(final Map<String, String> request, final InvoiceHeader invoiceHeader, String channel,Card card) {
        if(invoiceHeader == null){
            return;
        }
        request.put("invoiceHeader", null);
        request.put("invoiceHeader.merchantDescriptor", invoiceHeader.getMerchantDescriptor());
        request.put("invoiceHeader.merchantDescriptorContact", invoiceHeader.getMerchantDescriptorContact());
        request.put("invoiceHeader.merchantDescriptorAlternate", invoiceHeader.getMerchantDescriptorAlternate());

        request.put("invoiceHeader.merchantDescriptorStreet", invoiceHeader.getMerchantDescriptorStreet());

        if(invoiceHeader.getSubMidId() !=null) {
            request.put("invoiceHeader.merchantDescriptorCity", invoiceHeader.getMerchantDescriptorCity());
            request.put("invoiceHeader.merchantDescriptorCountry", invoiceHeader.getMerchantDescriptorCountry());
            request.put("invoiceHeader.submerchantID",invoiceHeader.getSubMidId());
        } else {
            request.put("invoiceHeader.merchantDescriptorCity", null);
            request.put("invoiceHeader.merchantDescriptorState", null);
            request.put("invoiceHeader.merchantDescriptorPostalCode", null);
            request.put("invoiceHeader.merchantDescriptorCountry", null);
        }

    }

    public void addBillAddress(final Map<String, String> request, final BillAddress address) {
        if (address != null) {
            validAddress(address);
            request.put("billTo", null);
            request.put("billTo.firstName", AlepayStringUtils.removeDiacritical(address.getFirstName()));
            request.put("billTo.lastName", AlepayStringUtils.removeDiacritical(address.getLastName()));
            request.put("billTo.street1", AlepayStringUtils.removeDiacritical(address.getStreet()));
            request.put("billTo.city", AlepayStringUtils.removeDiacritical(address.getCity()));
            if (address.getState() != null && !"".equals(address.getState())) {
                request.put("billTo.state", AlepayStringUtils.removeDiacritical(address.getState()));
            }
            if (address.getPostalCode() != null) {
                request.put("billTo.postalCode", address.getPostalCode());
            }
            request.put("billTo.country", AlepayStringUtils.removeDiacritical(address.getCountry()));
            request.put("billTo.phoneNumber", AlepayStringUtils.removeDiacritical(address.getPhoneNumber()));
            request.put("billTo.email", address.getEmail());
        }
    }

    public void addCurrency(final Map<String, String> request, final String currency){
        request.put("purchaseTotals",null);
        request.put("purchaseTotals.currency", currency);
    }

    public void addCard(final Map<String, String> request, final Card card) {
        if (card != null) {
            validCard(card);
            request.put("card", null);
            request.put("card.accountNumber", card.getCardNumber());
            request.put("card.expirationMonth", card.getCardExpireMonth());
            request.put("card.expirationYear", card.getCardExpireYear());
            if (card.getCardCvv2Code() != null) {
                request.put("card.cvNumber", card.getCardCvv2Code());
            }
            request.put("card.cardType", Constant.getCardType(card.getCardType()));
        }
    }


    public void addMerchantDefinedDataField(Map<String,String> request, String... fields) {
        if(fields[0] == null){
            return;
        }
        request.put("merchantDefinedData", null);
        request.put("merchantDefinedData.mddField", fields[0]);
        request.put("merchantDefinedData.mddField_id", "5");
        request.put("merchantDefinedData.mddField2", fields[1]);
        request.put("merchantDefinedData.mddField2_id", "6");
    }

    public void addAuthServiceDependChannel(final Map<String, String> request, String channel){
        /* 001-VISA, 002-MASTERCARD, 003-AMERICAN_EXPRESS, 007-JCB */
        String cardType = request.get("card.cardType") != null ? request.get("card.cardType") : "";
        if(channel.contains("STB")){
            switch(cardType){
                case "001":
                    request.put("ccAuthService.aggregatorID", "10078767");
                    break;
                case "002":
                    request.put("ccAuthService.aggregatorID", "258116");
                    break;
                case "003":
                    break;
                case "007":
                    break;
            }
        }
    }

    public void validAuthorizeCard(AuthorizeCard authorizeCard) {
        if (authorizeCard.getOrderCurrency() == null || "".equals(authorizeCard.getOrderCurrency())) {
            cybersourceResponseService.validateResponse(CybersourceError.CURRENCY_EMPTY);
        }
        if (authorizeCard.getCard() == null) {
            cybersourceResponseService.validateResponse(CybersourceError.CARD_INFO_EMPTY);
        } else {
            validCard(authorizeCard.getCard());
        }
        if (authorizeCard.getBillAddress() == null) {
            cybersourceResponseService.validateResponse(CybersourceError.CUSTOMER_INFO_EMPTY);
        } else {
            validAddress(authorizeCard.getBillAddress());
        }
    }

    private void validCard(Card card) {
        if (card.getCardNumber() == null || "".equals(card.getCardNumber())) {
            cybersourceResponseService.validateResponse(CybersourceError.CARD_NUMBER_EMPTY);
        } else if (card.getCardExpireMonth() == null || "".equals(card.getCardExpireMonth())) {
            cybersourceResponseService.validateResponse(CybersourceError.MONTH_EXPIRED_EMPTY);
        } else if (card.getCardExpireYear() == null || "".equals(card.getCardExpireYear())) {
            cybersourceResponseService.validateResponse(CybersourceError.YEAR_EXPIRED_EMPTY);
        } else if (card.getCardType() == null || "".equals(card.getCardType())) {
            cybersourceResponseService.validateResponse(CybersourceError.CARD_TYPE_EMPTY);
        }
    }

    private void validAddress(BillAddress address) {
        if (address.getFirstName() == null || "".equals(address.getFirstName())) {
            cybersourceResponseService.validateResponse(CybersourceError.NAME_EMPTY);
        } else if (address.getLastName() == null || "".equals(address.getLastName())) {
            cybersourceResponseService.validateResponse(CybersourceError.FULLNAME_EMPTY);
        } else if (address.getEmail() == null || "".equals(address.getEmail())) {
            cybersourceResponseService.validateResponse(CybersourceError.EMAIL_EMPTY);
        } else if (address.getCity() == null || "".equals(address.getCity())) {
            cybersourceResponseService.validateResponse(CybersourceError.CITY_EMPTY);
        } else if (address.getCountry() == null || "".equals(address.getCountry())) {
            cybersourceResponseService.validateResponse(CybersourceError.COUNTRY_EMPTY);
        } else if (address.getPhoneNumber() == null || "".equals(address.getPhoneNumber())) {
            cybersourceResponseService.validateResponse(CybersourceError.PHONENUMBER_EMPTY);
        } else if (address.getStreet() == null || "".equals(address.getStreet())) {
            cybersourceResponseService.validateResponse(CybersourceError.STREET_EMPTY);
        }
    }

    private String getResponseMessage(String resCode) {
        if (Constant.ERROR_CODE_DESC_MAP.get(resCode) != null) {
            return Constant.ERROR_CODE_DESC_MAP.get(resCode);
        }
        return CybersourceError.ERROR_SYSTEM.getMessage();
    }

    private String getECIMessage(String eciCode){
        if(CybersourceConfig.ECI_ERROR_CODE_MAP.get(eciCode) !=null){
            return CybersourceConfig.ECI_ERROR_CODE_MAP.get(eciCode);
        }
        return CybersourceConfig.ECI_ERROR_CODE_MAP.get("07");
    }

    public void validAuthorizeCard3D(AuthorizeCard3D authorizeCard3D) {
        if (authorizeCard3D.getOrderCurrency() == null || "".equals(authorizeCard3D.getOrderCurrency())) {
            cybersourceResponseService.validateResponse(CybersourceError.CURRENCY_EMPTY);
        }
        if (authorizeCard3D.getBillAddress() == null) {
            cybersourceResponseService.validateResponse(CybersourceError.CUSTOMER_INFO_EMPTY);
        } else {
            validAddress(authorizeCard3D.getBillAddress());
        }
        if (authorizeCard3D.getCard() == null) {
            cybersourceResponseService.validateResponse(CybersourceError.CARD_INFO_EMPTY);
        } else {
            validCard(authorizeCard3D.getCard());
        }
        if (authorizeCard3D.getSignedPARes() == null || "".equals(authorizeCard3D.getSignedPARes())) {
            cybersourceResponseService.validateResponse(CybersourceError.AUTHENTICATION_CODE);
        }
    }

    public void validAuthorize(Authorize authorize) {
        if (authorize.getOrderCurrency() == null || "".equals(authorize.getOrderCurrency())) {
            cybersourceResponseService.validateResponse(CybersourceError.CURRENCY_EMPTY);
        }
        if (authorize.getPaymentAmount() < 0) {
            cybersourceResponseService.validateResponse(CybersourceError.AMOUNT_INVALID);
        }
        if (authorize.getBillAddress() == null) {
            cybersourceResponseService.validateResponse(CybersourceError.CUSTOMER_INFO_EMPTY);
        } else {
            validAddress(authorize.getBillAddress());
        }
        if (authorize.getCard() == null) {
            cybersourceResponseService.validateResponse(CybersourceError.CARD_INFO_EMPTY);
        } else {
            validCard(authorize.getCard());
        }
    }

    public void validAuthorize3D(Authorize3D authorize3D) {
        if (authorize3D.getOrderCurrency() == null || "".equals(authorize3D.getOrderCurrency())) {
            cybersourceResponseService.validateResponse(CybersourceError.CURRENCY_EMPTY);
        }
        if (authorize3D.getPaymentAmount() < 0) {
            cybersourceResponseService.validateResponse(CybersourceError.AMOUNT_INVALID);
        }
        if (authorize3D.getBillAddress() == null) {
            cybersourceResponseService.validateResponse(CybersourceError.CUSTOMER_INFO_EMPTY);
        } else {
            validAddress(authorize3D.getBillAddress());
        }
        if (authorize3D.getCard() == null) {
            cybersourceResponseService.validateResponse(CybersourceError.CARD_INFO_EMPTY);
        } else {
            validCard(authorize3D.getCard());
        }
//        if (authorize3D.getSignedPARes() == null || "".equals(authorize3D.getSignedPARes())) {
//            cybersourceResponseService.validateResponse(CybersourceError.AUTHENTICATION_CODE);
//        }
    }

    public void validDeleteProfile(CancelAuthorizeCard profile) {
        if (profile.getSubscriptionId() == null || "".equals(profile.getSubscriptionId())) {
            cybersourceResponseService.validateResponse(CybersourceError.CODE_LINKED_CARD_EMPTY);
        }
    }

    public PGResponse createCustomerProfile3ds2(Map<String, String> map) {
        Map<String, Object> result = baseMethod(map, Constant.AUTHORIZE_CARD);
        Response rs = (Response) result.get(RESPONSE);
        Node temp = (Node) result.get(TEMPDATA);
        if (rs.isSuccess()) {
            rs.getData().put("subscriptionId", SOAPUtil.getValue(temp, "paySubscriptionCreateReply.subscriptionID"));
        } else if (rs.getResponseCode().equals("475")) {
            rs.getData().put("acsURL", SOAPUtil.getValue(temp, "payerAuthEnrollReply.acsURL"));
            rs.getData().put("paReq", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paReq"));
            rs.getData().put("xid", SOAPUtil.getValue(temp, "payerAuthEnrollReply.xid"));
        } else if (rs.getResponseCode().equals("102")) {
            String value = SOAPUtil.getValue(temp, "invalidField");
            if(value != null) {
                rs.getData().put("invalidField", CybsUtil.subStringData(value));
            }
        } else if (rs.getResponseCode().equals("101")) {
            String value = SOAPUtil.getValue(temp, "missingField");
            if(value != null) {
                rs.getData().put("missingField", CybsUtil.subStringData(value));
            }
        }
        if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.eciRaw"))) {
            rs.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthEnrollReply.eciRaw"));
        } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafCollectionIndicator"))) {
            rs.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafCollectionIndicator"));
        } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.eci"))) {
            rs.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthEnrollReply.eci"));
        }
        String cardType = SOAPUtil.getValue(temp,"payerAuthValidateReply.cardTypeName");

        eciMessage(rs, cardType);

        rs.getData().put("transactionReferenceId", SOAPUtil.getValue(temp, "merchantReferenceCode"));
        rs.getData().put("deviceDataCollectionURL", SOAPUtil.getValue(temp, "payerAuthSetupReply.deviceDataCollectionURL"));
        rs.getData().put("referenceID", SOAPUtil.getValue(temp, "payerAuthSetupReply.referenceID"));
        rs.getData().put("accessToken", SOAPUtil.getValue(temp, "payerAuthSetupReply.accessToken"));
        rs.getData().put("resultMessage", getResponseMessage(rs.getResponseCode()));
        rs.getData().put("TransactionID", SOAPUtil.getValue(temp, "payerAuthEnrollReply.authenticationTransactionID"));
        rs.getData().put("eciMessage",getECIMessage(rs.getData().get("eci")));
        PGResponse response = new PGResponse();
        response.setStatus(true);
        response.setData(rs);
        response.setChannelErrorCode(rs.getResponseCode());
        response.setChannelMessage(getResponseMessage(rs.getResponseCode()));
        response.setErrorCode(CybersourceError.SUCCESS.getCode());
        response.setMessage(CybersourceError.SUCCESS.getMessage());
        return response;
    }

    public PGResponse authorizeSubscription3D(Map<String, String> map) {
        Map<String, Object> result = baseMethod(map, Constant.TOKENIZATION_PAYMENT);
        Response rs = (Response) result.get(RESPONSE);
        Node temp = (Node) result.get(TEMPDATA);
        if (rs.getResponseCode().equals("475")) {
            rs.getData().put("acsURL", SOAPUtil.getValue(temp, "payerAuthEnrollReply.acsURL"));
            rs.getData().put("paReq", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paReq"));
        } else if (rs.getResponseCode().equals("102")) {
            String value = SOAPUtil.getValue(temp, "invalidField");
            if(value != null) {
                String StringData = CybsUtil.subStringData(value);
                rs.getData().put("invalidField", StringData);
            }
        } else if (rs.getResponseCode().equals("101")) {
            String value = SOAPUtil.getValue(temp, "invalidField");
            if(value != null) {
                String StringData = CybsUtil.subStringData(value);
                rs.getData().put("invalidField", StringData);
            }
        }
        rs.getData().put("acsURL", SOAPUtil.getValue(temp, "payerAuthEnrollReply.acsURL"));
        if (SOAPUtil.getValue(temp, "payerAuthEnrollReply.cavv") != null) {
            rs.getData().put("cavv/avv", SOAPUtil.getValue(temp, "payerAuthEnrollReply.cavv"));
        } else {
            rs.getData().put("cavv/avv", SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafAuthenticationData"));
        }
        rs.getData().put("paresStatus", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paresStatus"));
        rs.getData().put("paReq", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paReq"));
        rs.getData().put("xid", SOAPUtil.getValue(temp, "payerAuthEnrollReply.xid"));
        rs.getData().put("commerceIndicator", SOAPUtil.getValue(temp, "payerAuthEnrollReply.commerceIndicator"));
        rs.getData().put("veresEnrolled", SOAPUtil.getValue(temp, "payerAuthEnrollReply.veresEnrolled"));
        rs.getData().put("3DSTransactionID", SOAPUtil.getValue(temp, "payerAuthEnrollReply.directoryServerTransactionID"));
        if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.eciRaw"))) {
            rs.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthEnrollReply.eciRaw"));
        } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafCollectionIndicator"))) {
            rs.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafCollectionIndicator"));
        } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.eci"))) {
            rs.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthEnrollReply.eci"));
        }
        rs.getData().put("TransactionID", SOAPUtil.getValue(temp, "payerAuthEnrollReply.authenticationTransactionID"));
        rs.getData().put("version", SOAPUtil.getValue(temp, "payerAuthEnrollReply.specificationVersion"));
        rs.getData().put("transactionReferenceId", SOAPUtil.getValue(temp, "merchantReferenceCode"));
        rs.getData().put("resultMessage", getResponseMessage(rs.getResponseCode()));
        String cardType = SOAPUtil.getValue(temp,"payerAuthValidateReply.cardTypeName");

        eciMessage(rs, cardType);

        return PGResponse.builder().
                status(true).
                data(rs).
                channelErrorCode(rs.getResponseCode()).
                channelMessage(getResponseMessage(rs.getResponseCode())).
                errorCode(CybersourceError.SUCCESS.getCode()).
                message(CybersourceError.SUCCESS.getMessage()).
                build();
    }

    public PGResponse checkEnrollment3ds2(Map<String, String> map) {
        Map<String, Object> result = baseMethod(map, Constant.CHECK_ENROLLMENT);
        Response rs = (Response) result.get(RESPONSE);
        Node temp = (Node) result.get(TEMPDATA);
        if (rs.getResponseCode().equals("475")) {
            rs.getData().put("acsURL", SOAPUtil.getValue(temp, "payerAuthEnrollReply.acsURL"));
            rs.getData().put("paReq", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paReq"));
        } else if (rs.getResponseCode().equals("102")) {
            String value = SOAPUtil.getValue(temp, "invalidField");
            if(value != null) {
                String StringData = CybsUtil.subStringData(value);
                rs.getData().put("invalidField", StringData);
            }
        } else if (rs.getResponseCode().equals("101")) {
            String value = SOAPUtil.getValue(temp, "invalidField");
            if(value != null) {
                String StringData = CybsUtil.subStringData(value);
                rs.getData().put("invalidField", StringData);
            }
        }
        rs.getData().put("acsURL", SOAPUtil.getValue(temp, "payerAuthEnrollReply.acsURL"));
        if (SOAPUtil.getValue(temp, "payerAuthEnrollReply.cavv") != null) {
            rs.getData().put("cavv/avv", SOAPUtil.getValue(temp, "payerAuthEnrollReply.cavv"));
        } else {
            rs.getData().put("cavv/avv", SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafAuthenticationData"));
        }
        rs.getData().put("paresStatus", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paresStatus"));
        rs.getData().put("paReq", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paReq"));
        rs.getData().put("xid", SOAPUtil.getValue(temp, "payerAuthEnrollReply.xid"));
        rs.getData().put("commerceIndicator", SOAPUtil.getValue(temp, "payerAuthEnrollReply.commerceIndicator"));
        rs.getData().put("veresEnrolled", SOAPUtil.getValue(temp, "payerAuthEnrollReply.veresEnrolled"));
        rs.getData().put("3DSTransactionID", SOAPUtil.getValue(temp, "payerAuthEnrollReply.directoryServerTransactionID"));
        if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.eciRaw"))) {
            rs.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthEnrollReply.eciRaw"));
        } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafCollectionIndicator"))) {
            rs.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafCollectionIndicator"));
        } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.eci"))) {
            rs.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthEnrollReply.eci"));
        }
        rs.getData().put("TransactionID", SOAPUtil.getValue(temp, "payerAuthEnrollReply.authenticationTransactionID"));
        rs.getData().put("version", SOAPUtil.getValue(temp, "payerAuthEnrollReply.specificationVersion"));
        rs.getData().put("transactionReferenceId", SOAPUtil.getValue(temp, "merchantReferenceCode"));
        rs.getData().put("resultMessage", getResponseMessage(rs.getResponseCode()));

        return PGResponse.builder().
                status(true).
                data(rs).
                channelErrorCode(rs.getResponseCode()).
                channelMessage(getResponseMessage(rs.getResponseCode())).
                errorCode(CybersourceError.SUCCESS.getCode()).
                message(CybersourceError.SUCCESS.getMessage()).
                build();
    }

    public PGResponse deleteToken(Map<String, String> map) {
        Map<String, Object> result = baseMethod(map, Constant.DELETE_TOKENIZATION);
        Response rs = (Response) result.get(RESPONSE);
        Node temp = (Node) result.get(TEMPDATA);
        if (rs.getResponseCode().equals("475")) {
            rs.getData().put("acsURL", SOAPUtil.getValue(temp, "payerAuthEnrollReply.acsURL"));
            rs.getData().put("paReq", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paReq"));
        } else if (rs.getResponseCode().equals("102")) {
            String value = SOAPUtil.getValue(temp, "invalidField");
            if(value != null) {
                String StringData = CybsUtil.subStringData(value);
                rs.getData().put("invalidField", StringData);
            }
        } else if (rs.getResponseCode().equals("101")) {
            String value = SOAPUtil.getValue(temp, "invalidField");
            if(value != null) {
                String StringData = CybsUtil.subStringData(value);
                rs.getData().put("invalidField", StringData);
            }
        }
        rs.getData().put("acsURL", SOAPUtil.getValue(temp, "payerAuthEnrollReply.acsURL"));
        if (SOAPUtil.getValue(temp, "payerAuthEnrollReply.cavv") != null) {
            rs.getData().put("cavv/avv", SOAPUtil.getValue(temp, "payerAuthEnrollReply.cavv"));
        } else {
            rs.getData().put("cavv/avv", SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafAuthenticationData"));
        }
        rs.getData().put("paresStatus", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paresStatus"));
        rs.getData().put("paReq", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paReq"));
        rs.getData().put("xid", SOAPUtil.getValue(temp, "payerAuthEnrollReply.xid"));
        rs.getData().put("commerceIndicator", SOAPUtil.getValue(temp, "payerAuthEnrollReply.commerceIndicator"));
        rs.getData().put("veresEnrolled", SOAPUtil.getValue(temp, "payerAuthEnrollReply.veresEnrolled"));
        rs.getData().put("3DSTransactionID", SOAPUtil.getValue(temp, "payerAuthEnrollReply.directoryServerTransactionID"));
        if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.eciRaw"))) {
            rs.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthEnrollReply.eciRaw"));
        } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafCollectionIndicator"))) {
            rs.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthEnrollReply.ucafCollectionIndicator"));
        } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthEnrollReply.eci"))) {
            rs.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthEnrollReply.eci"));
        }
        rs.getData().put("TransactionID", SOAPUtil.getValue(temp, "payerAuthEnrollReply.authenticationTransactionID"));
        rs.getData().put("version", SOAPUtil.getValue(temp, "payerAuthEnrollReply.specificationVersion"));
        rs.getData().put("transactionReferenceId", SOAPUtil.getValue(temp, "merchantReferenceCode"));
        rs.getData().put("resultMessage", getResponseMessage(rs.getResponseCode()));

        return PGResponse.builder().
                status(true).
                data(rs).
                channelErrorCode(rs.getResponseCode()).
                channelMessage(getResponseMessage(rs.getResponseCode())).
                errorCode(CybersourceError.SUCCESS.getCode()).
                message(CybersourceError.SUCCESS.getMessage()).
                build();
    }

    public PGResponse runTransaction3ds2(final Map<String, String> request, final String operation) {
        Map<String, String> keyInfo = SOAPUtil.getCyberKeyInfo(request.get("merchantID"));
        Response response = new Response(keyInfo.get("MID"), CybersourceConfig.ENVIROMENT_KEY);
        try {
            Map<String, Object> result = baseMethod(request, operation);
            response = (Response) result.get(RESPONSE);
            Node temp = (Node) result.get(TEMPDATA);
            if (response.isSuccess()) {
                response.getData().put("amount", SOAPUtil.getValue(temp, "ccAuthReply.amount"));
                response.getData().put("currency", SOAPUtil.getValue(temp, "purchaseTotals.currency"));
                response.getData().put("xid", SOAPUtil.getValue(temp, "payerAuthValidateReply.xid"));
                response.getData().put("commerceIndicator", SOAPUtil.getValue(temp, "payerAuthValidateReply.commerceIndicator"));
                response.getData().put("specificationVersion", SOAPUtil.getValue(temp, "payerAuthValidateReply.specificationVersion"));
                response.getData().put("3DSTransactionID", SOAPUtil.getValue(temp, "payerAuthValidateReply.directoryServerTransactionID"));
            } else if (response.getResponseCode().equals("475")) {
                response.getData().put("acsURL", SOAPUtil.getValue(temp, "payerAuthEnrollReply.acsURL"));
                response.getData().put("paReq", SOAPUtil.getValue(temp, "payerAuthEnrollReply.paReq"));
                response.getData().put("xid", SOAPUtil.getValue(temp, "payerAuthEnrollReply.xid"));
            } else if (response.getResponseCode().equals("102")) {
                response.getData().put("invalidField", CybsUtil.subStringData(SOAPUtil.getValue(temp, "invalidField")));
            } else if (response.getResponseCode().equals("101")) {
                response.getData().put("missingField", CybsUtil.subStringData(SOAPUtil.getValue(temp, "missingField")));
            } else if (response.getResponseCode().equals("476")) {
                response.getData().put("status", CybsUtil.subStringData(SOAPUtil.getValue(temp, "missingField")));
            }
            if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthValidateReply.eciRaw"))) {
                response.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthValidateReply.eciRaw"));
            } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthValidateReply.ucafCollectionIndicator"))) {
                response.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthValidateReply.ucafCollectionIndicator"));
            } else if (!Strings.isNullOrEmpty(SOAPUtil.getValue(temp, "payerAuthValidateReply.eci"))) {
                response.getData().put("eci", SOAPUtil.getValue(temp, "payerAuthValidateReply.eci"));
            }
            if (SOAPUtil.getValue(temp, "payerAuthValidateReply.cavv") != null) {
                response.getData().put("cavv/avv", SOAPUtil.getValue(temp, "payerAuthValidateReply.cavv"));
            } else {
                response.getData().put("cavv/avv", SOAPUtil.getValue(temp, "payerAuthValidateReply.ucafAuthenticationData"));
            }
            response.getData().put("paresStatus", SOAPUtil.getValue(temp, "payerAuthValidateReply.paresStatus"));
            response.getData().put("transactionReferenceId", SOAPUtil.getValue(temp, "merchantReferenceCode"));
            response.getData().put("resultMessage", getResponseMessage(response.getResponseCode()));

        } catch (Exception e) {
            response.getData().put("resultMessage", getResponseMessage(response.getResponseCode()));
        }
        PGResponse rs = new PGResponse();
        rs.setData(response);
        rs.setStatus(true);
        rs.setChannelErrorCode(response.getResponseCode());
        rs.setChannelMessage(getResponseMessage(response.getResponseCode()));
        rs.setErrorCode(CybersourceError.SUCCESS.getCode());
        rs.setMessage(CybersourceError.SUCCESS.getMessage());
        return rs;
    }
    public Map<String, String> getLogRequest(Map<String, String> requestMap){
        Map<String, String> logMap = new LinkedHashMap<>(requestMap);
        if(logMap.get("card.accountNumber") == null){
            return logMap;
        }
        String cardName = logMap.get("card.accountNumber");
        String cvv = null;
        if(logMap.get("card.cvNumber") !=null){
            cvv = logMap.get("card.cvNumber");
        }
        logMap.put("card.accountNumber",cardName.substring(0,4)+"-XXXXXX-"+cardName.substring(cardName.length()-6));
        logMap.put("card.cvNumber","XXXX"+cvv.substring(cvv.length()-2));
        return logMap;
    }

    public PGResponse convertTransaction(Map<String, String> map){
        Map<String, Object> result = baseMethod(map, Constant.CONVERT_TRANSACTION);
        Response rs = (Response) result.get(RESPONSE);
        Node temp = (Node) result.get(TEMPDATA);
        if (rs.isSuccess()) {
            rs.getData().put("subscriptionId", SOAPUtil.getValue(temp, "paySubscriptionCreateReply.subscriptionID"));
        } else if (rs.getResponseCode().equals("102")) {
            rs.getData().put("invalidField", CybsUtil.subStringData(SOAPUtil.getValue(temp, "invalidField")));
        } else if (rs.getResponseCode().equals("101")) {
            rs.getData().put("missingField", CybsUtil.subStringData(SOAPUtil.getValue(temp, "missingField")));
        }
        rs.getData().put("transactionReferenceId", SOAPUtil.getValue(temp, "merchantReferenceCode"));
        rs.getData().put("resultMessage", getResponseMessage(rs.getResponseCode()));

        PGResponse response = new PGResponse();
        response.setStatus(true);
        response.setData(rs);
        response.setChannelErrorCode(rs.getResponseCode());
        response.setChannelMessage(getResponseMessage(rs.getResponseCode()));
        response.setErrorCode(CybersourceError.SUCCESS.getCode());
        response.setMessage(CybersourceError.SUCCESS.getMessage());
        return response;
    }

    private void eciMessage(final Response rs, String cardType){
        if(rs.getEci() == null){
            if(cardType != null){
                switch(cardType){
                    case "VISA":
                        rs.setEci("07");
                        break;
                    case "MASTERCARD":
                        rs.setEci("00");
                        break;
                    default:
                        rs.setEci("07");
                }
            }else{
                rs.setEci("07");
            }
        }

        if(rs.getData().get("eci") == null){
            rs.getData().put("eci",rs.getEci());
        }
        rs.getData().put("eciMessage",getECIMessage(rs.getData().get("eci")));
    }
}
