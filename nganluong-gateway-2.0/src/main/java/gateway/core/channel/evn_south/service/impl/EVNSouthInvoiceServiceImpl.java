package gateway.core.channel.evn_south.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.evn_south.dto.*;
import gateway.core.channel.evn_south.service.EVNSouthInvoiceService;
import gateway.core.dto.PGResponse;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.service.CommonLogService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;

@Service
public class EVNSouthInvoiceServiceImpl implements EVNSouthInvoiceService {
    private static final Logger logger = LogManager.getLogger(EVNSouthInvoiceServiceImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final CommonLogService commonLogService;

    @Autowired
    public EVNSouthInvoiceServiceImpl(CommonLogService commonLogService){
        this.commonLogService = commonLogService;
    }

    @Override
    public PGResponse getCustomerFeeInfo(ChannelFunction channelFunction, String data) {
        logger.info(commonLogService.createContentLogStartEndFunction(
                EVNConfig.CHANNEL_CODE, " - ",EVNConfig.GET_CUSTOMER_FEE_INFO,true));
        try {
            CustomerInfoRequest customerInfoRequest = objectMapper.readValue(data, CustomerInfoRequest.class);
            String[] namespace = {"xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"","xmlns:tem=\"http://tempuri.org/\""};
            String body = this.wrapper(this.makeSOAP(customerInfoRequest),namespace);

            String url = channelFunction.getHost() + channelFunction.getUrl();
            String soapAction =  "GetCustomerFeeInfo";

            HTTPConnection connection = new HTTPConnection(url, body, soapAction);
            Response response = connection.execute();
            String rawBody = response.body().string();
            logger.info(" EVN RESPONSE: " +rawBody);
            String jsonBody = StringUtils.isEmpty(rawBody) ?
                    "" : convert(new CustomerInfoResponse(),rawBody,"<GetCustomerFeeInfoResult","</GetCustomerFeeInfoResponse>");
            return PGResponse.getInstanceWhenSuccess(String.valueOf(response.code()),jsonBody);
        }catch (IOException | JAXBException e){
            logger.error(e.getMessage());
            return PGResponse.getInstanceWhenError(PGResponse.FAIL);
        } finally {
            logger.info(commonLogService.createContentLogStartEndFunction(
                    EVNConfig.CHANNEL_CODE, " - ",EVNConfig.GET_CUSTOMER_FEE_INFO,false));
        }
    }

    @Override
    public PGResponse payBillFeesByCustomerCode(ChannelFunction channelFunction, String data) {
        logger.info(commonLogService.createContentLogStartEndFunction(
                EVNConfig.CHANNEL_CODE, " - ",EVNConfig.PAY_BILL_FEES_BY_CUSTOMER_CODE,true));
        try{
            String url = channelFunction.getHost() + channelFunction.getUrl();
            String soapAction =  "PayBillFeesByCustomerCode";
            BillInfoRequest body = new ObjectMapper().readValue(data,BillInfoRequest.class);

            HTTPConnection connection = new HTTPConnection(url, body.toString(), soapAction);
            Response response = connection.execute();
            String rawBody = response.body().string();
            logger.info(" EVN RESPONSE: " + rawBody);
            String jsonBody = StringUtils.isEmpty(rawBody) ?
                    "" : convert(new BillInfoResponse(),rawBody,"<table","</DocumentElement>");
            return PGResponse.getInstanceWhenSuccess(String.valueOf(response.code()),jsonBody);
        }catch(IOException e){
            logger.error(e.getMessage());
            return PGResponse.getInstanceWhenError(PGResponse.FAIL);
        }finally {
            logger.info(commonLogService.createContentLogStartEndFunction(
                    EVNConfig.CHANNEL_CODE, " - ",EVNConfig.PAY_BILL_FEES_BY_CUSTOMER_CODE,false));
        }
    }

    @Override
    public PGResponse cancelBillFeesByCustomerCode(ChannelFunction channelFunction, String data) {
        try{
            CancelPaymentBillRequest body = objectMapper.readValue(data, CancelPaymentBillRequest.class);
            String url = channelFunction.getHost() + channelFunction.getUrl();
            String soapAction =  "CancelBillFeesByCustomerCode";

            HTTPConnection connection = new HTTPConnection(url, body.toString(), soapAction);
            Response response = connection.execute();
            String rawBody = response.body().string();
            logger.info(" EVN RESPONSE: " + rawBody);
            String jsonBody = StringUtils.isEmpty(rawBody) ?
                    "" : convert(new CancelPaymentBillResponse(),rawBody,"<CancelBillFeesByCustomerCodeResponse","</Body>");
            return PGResponse.getInstanceWhenSuccess(String.valueOf(response.code()),jsonBody);
        } catch (IOException e){
            logger.error(e.getMessage());
            return PGResponse.getInstanceWhenError(PGResponse.FAIL);
        }
    }

    @Override
    public PGResponse getReceipt(ChannelFunction channelFunction, String data) {
        try {
            GetReceiptRequest getReceiptRequest = objectMapper.readValue(data, GetReceiptRequest.class);
            String url = channelFunction.getHost() + channelFunction.getUrl();
            String soapAction = "";
            String body = "";

            String trigger = getReceiptRequest.getReceiptType() != null ? getReceiptRequest.getReceiptType() : "";

            switch (trigger){
                case "DC":
                case "DD":
                case "TC":
                case "CD":
                    soapAction =  "GetPhieuThuTienDichVu";
                    body = getReceiptRequest.body("");
                    break;
                case "VC":
                    soapAction =  "GetPhieuThuTienCSPK";
                    body = getReceiptRequest.body("VC");
                    break;
                default:
                    soapAction =  "GetPhieuThuTienDien";
                    body = getReceiptRequest.body("TD");
            }

            HTTPConnection connection = new HTTPConnection(url, body, soapAction);
            Response response = connection.execute();
            String rawBody = response.body().string();
            logger.info(" EVN RESPONSE: " + rawBody);
            String jsonBody = StringUtils.isEmpty(rawBody) ?
                    "" : convert(new GetReceiptResponse(),rawBody,"<Table","</NewDataSet");
            return PGResponse.getInstanceWhenSuccess(String.valueOf(response.code()),jsonBody);
        } catch (IOException e){
            logger.error(e.getMessage());
            return PGResponse.getInstanceWhenError(PGResponse.FAIL);
        }
    }

    @Override
    public PGResponse checkBill(ChannelFunction channelFunction, String data) {
        try {
            CheckBillRequest checkBill = objectMapper.readValue(data, CheckBillRequest.class);
            String url = channelFunction.getHost() + channelFunction.getUrl();
            String soapAction = "CheckBill";
            String body = checkBill.toString();

            HTTPConnection connection = new HTTPConnection(url, body, soapAction);
            Response response = connection.execute();
            String rawBody = response.body().string();
            logger.info(" EVN RESPONSE: " + rawBody);
            String jsonBody = StringUtils.isEmpty(rawBody) ?
                    "" : convert(new CheckBillResponse(),rawBody,"<table","</DocumentElement");
            return PGResponse.getInstanceWhenSuccess(String.valueOf(response.code()),jsonBody);
        } catch (IOException e){
            logger.error(e.getMessage());
            return PGResponse.getInstanceWhenError(PGResponse.FAIL);
        }
    }

    @Override
    public PGResponse getBillByBank(ChannelFunction channelFunction, String data) {
        try {
            GetBillByBankRequest getBillByBankRequest = objectMapper.readValue(data,GetBillByBankRequest.class);
            String url = channelFunction.getHost() + channelFunction.getUrl();
            String soapAction = "GetBillByBank";
            String body = getBillByBankRequest.toString();

            HTTPConnection connection = new HTTPConnection(url, body, soapAction);
            Response response = connection.execute();
            String rawBody = response.body().string();
            logger.info(" EVN RESPONSE: " + rawBody);
            String jsonBody = StringUtils.isEmpty(rawBody) ?
                    "" : convert(new GetBillByBankResponse(),rawBody,"<DocumentElement","</diffgr:diffgram>");
            return PGResponse.getInstanceWhenSuccess(String.valueOf(response.code()),jsonBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String wrapper(String body, String[] namespaces){

        StringBuilder envelope = new StringBuilder("<soapenv:Envelope ");
        for(String name : namespaces) {
            envelope.append(name).append(" ");
        }
        envelope.append("> <soapenv:Body>");
        envelope.append(body);
        envelope.append("</soapenv:Body></soapenv:Envelope>");
        return envelope.toString();
    }

    private  <T> String makeSOAP(T t) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(t.getClass());
        StringWriter sw = new StringWriter();
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(t, sw);
        return sw.toString().replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>","");
    }

    private <T> String convert(T t, String body, String begin, String end) throws IOException {
        String xmlStringReplace = body.replaceAll("s:", "")
                .replaceAll("a:", "")
                .replaceAll("i:", "");
        int beginIndex = xmlStringReplace.indexOf(begin);
        int endIndex = xmlStringReplace.indexOf(end);
        String concat = xmlStringReplace.substring(beginIndex, endIndex);
        JSONObject json = XML.toJSONObject(concat); // converts xml to json
        String jsonPrettyPrintString = json.getJSONObject(begin.substring(1)).toString().replace("diffgr:id","diffgr-id"); // json pretty print
        T customer = (T) new ObjectMapper().readValue(jsonPrettyPrintString,t.getClass());
        return new ObjectMapper().writeValueAsString(customer);
    }

}