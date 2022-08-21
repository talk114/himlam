package gateway.core.channel.vccb_va.service.Imp;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.vccb_va.dto.CallbackHolder;
import gateway.core.channel.vccb_va.dto.VCCBRequest;
import gateway.core.channel.vccb_va.dto.VCCBResponse;
import gateway.core.channel.vccb_va.dto.VCCBVAConfig;
import gateway.core.channel.vccb_va.dto.dto.*;
import gateway.core.channel.vccb_va.dto.request.*;
import gateway.core.channel.vccb_va.dto.response.CreateVAHolder;
import gateway.core.channel.vccb_va.service.VCCBVirtualAccountService;
import gateway.core.dto.PGResponse;
import gateway.core.dto.request.NLVARequest;
import gateway.core.util.FilePathUtil;
import gateway.core.util.HttpUtil;
import gateway.core.util.PGSecurity;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.channel.vib.dto.VIBConst;
import vn.nganluong.naba.dto.VirtualAccountDto;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.VirtualAccount;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.service.VirtualAccountService;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author dungla
 */
@Service
public class VCCBVirtualAccountServiceImp implements VCCBVirtualAccountService {
    private static final Logger logger = LogManager.getLogger(VCCBVirtualAccountServiceImp.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int VCCB_BANK_CODE = 18;
    private static final String VCCB_NAME = "VCCB";

    private final VirtualAccountService virtualAccountService;
    private final CommonLogService commonLogService;
    private final PaymentService paymentService;

    @Autowired
    public VCCBVirtualAccountServiceImp(VirtualAccountService virtualAccountService,
                                        CommonLogService commonLogService,
                                        PaymentService paymentService){
        this.virtualAccountService = virtualAccountService;
        this.commonLogService = commonLogService;
        this.paymentService = paymentService;
    }

    @Override
    public PGResponse viewAccountDetails(ChannelFunction channelFunction, String bodyRequest) {
        logger.info(commonLogService.createContentLogStartEndFunction(
                VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_DETAIL_VIRTUAL_ACCOUNT,true));
        try {
            CloseAndReopenDto dto = objectMapper.readValue(bodyRequest,CloseAndReopenDto.class);
            if(!virtualAccountService.isExistVirtualAccount(dto.getAccountNumber())){
                return PGResponse.getInstanceWhenError(PGResponse.VIRTUAL_ACCOUNT_NOT_EXIST);
            }
            CloseAndReopen account = new CloseAndReopen(dto.getAccountNumber());
            VCCBRequest request = VCCBRequest.getInstance(dto.getRequestId(),account);
            Response response = requestApi(channelFunction.getHost()+channelFunction.getUrl(), objectMapper.writeValueAsString(request));
            String responseBody = response.body().string();
            logger.info("Response Body: " + responseBody);
            if(response.isSuccessful()){
                VCCBResponse vccbResponse = objectMapper.readValue(responseBody, VCCBResponse.class);
                CreateVAHolder vaResponse = objectMapper.convertValue(vccbResponse.getData(),CreateVAHolder.class);
                logger.info(commonLogService.createContentLogStartEndFunction(
                        VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_DETAIL_VIRTUAL_ACCOUNT,false));
                return PGResponse.getInstance(PGResponse.SUCCESS,vaResponse.getError(),vaResponse.getMessage(),vccbResponse);
            }
        } catch (IOException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        logger.info(commonLogService.createContentLogStartEndFunction(
                VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_DETAIL_VIRTUAL_ACCOUNT,false));
        return PGResponse.getInstanceWhenError(PGResponse.FAIL);
    }

    @Override
    public PGResponse closeVirtualAccount(ChannelFunction channelFunction, String bodyRequest) {
        logger.info(commonLogService.createContentLogStartEndFunction(
                VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_CLOSE_VIRTUAL_ACCOUNT,true));
        try {
            CloseAndReopenDto dto = objectMapper.readValue(bodyRequest,CloseAndReopenDto.class);
            if(!virtualAccountService.isExistVirtualAccount(dto.getAccountNumber())){
                return PGResponse.getInstanceWhenError(PGResponse.VIRTUAL_ACCOUNT_NOT_EXIST);
            }
            CloseAndReopen account = new CloseAndReopen(dto.getAccountNumber());
            VCCBRequest request = VCCBRequest.getInstance(dto.getRequestId(),account);
            Response response = requestApi(channelFunction.getHost()+channelFunction.getUrl(), objectMapper.writeValueAsString(request));
            String responseBody = response.body().string();
            logger.info("Response Body: " + responseBody);
            if(response.isSuccessful()){
                VCCBResponse vccbResponse = objectMapper.readValue(responseBody, VCCBResponse.class);
                CreateVAHolder vaResponse = objectMapper.convertValue(vccbResponse.getData(),CreateVAHolder.class);
                System.out.println(vaResponse);
                if(vaResponse.getError()==null) {
                    virtualAccountService.updateStatusVirtualAccount(vaResponse.getAccNo(), false);
                }
                logger.info(commonLogService.createContentLogStartEndFunction(
                        VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_CLOSE_VIRTUAL_ACCOUNT,false));
                return PGResponse.getInstance(PGResponse.SUCCESS,vaResponse.getError(),vaResponse.getMessage(),vccbResponse);
            }
        } catch (IOException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        logger.info(commonLogService.createContentLogStartEndFunction(
                VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_CLOSE_VIRTUAL_ACCOUNT,false));
        return PGResponse.getInstanceWhenError(PGResponse.FAIL);
    }

    @Override
    public PGResponse createVirtualAccount(ChannelFunction channelFunction, String bodyRequest) {
        logger.info(commonLogService.createContentLogStartEndFunction(
                VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_CREATE_VIRTUAL_ACCOUNT,true));
        try{
            CreateVirtualAccountDTO accountDTO = objectMapper.readValue(bodyRequest,CreateVirtualAccountDTO.class);
            CreateVirtualAccount account = CreateVirtualAccount.builder()
                    .accountNameSuffix(accountDTO.getAccountNameSuffix())
                    .accountSuffix(accountDTO.getAccountSuffix())
                    .partnerCode(accountDTO.getPartnerCode())
                    .accountType(accountDTO.getAccountType())
                    .build();
            VCCBRequest request = VCCBRequest.getInstance(accountDTO.getRequestId(),account);
            Response response = requestApi(channelFunction.getHost()+channelFunction.getUrl(), objectMapper.writeValueAsString(request));
            assert response.body() != null;
            String responseBody = response.body().string();
            logger.info("Response Body: " + responseBody);
            if(response.isSuccessful()){
                VCCBResponse vccbResponse = objectMapper.readValue(responseBody,VCCBResponse.class);
                CreateVAHolder vaResponse = objectMapper.convertValue(vccbResponse.getData(),CreateVAHolder.class);
                if(vaResponse.getError() == null){
                    String desc = objectMapper.writeValueAsString(vaResponse);
                    VirtualAccountDto saveAccount = VirtualAccountDto.builder()
                            .channelId(channelFunction.getChannel().getId())
                            .virtualAccountNo(vaResponse.getAccNo())
                            .virtualAccountName(vaResponse.getAccName())
                            .merchantCode(VCCBVAConfig.CHANNEL_CODE)
                            .phoneNumber(accountDTO.getPhoneNumber())
                            .description(desc.length() < 254 ? desc: desc.substring(0,254))
                            .build();
                    virtualAccountService.createVirtualAccount(saveAccount);
                }
                logger.info("Response" + responseBody);
                logger.info(commonLogService.createContentLogStartEndFunction(
                        VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_CREATE_VIRTUAL_ACCOUNT,false));
                return PGResponse.getInstance(PGResponse.SUCCESS,vaResponse.getError(),vaResponse.getMessage(),vccbResponse);
            }
        } catch (Exception e){
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return PGResponse.getInstanceWhenError(PGResponse.FAIL);
    }

    @Override
    public PGResponse getListVirtualAccount(ChannelFunction channelFunction, String bodyRequest) {
        System.out.println();
        try{
            GetListVADto dto = objectMapper.readValue(bodyRequest, GetListVADto.class);
            GetListVA requestData = new GetListVA(dto.getPartnerCode() != null?dto.getPartnerCode():VCCBVAConfig.PARTNER_CODE, dto.getPage(), dto.getSize());
            if(requestData.getPage() < 0 || requestData.getSize() < 1){
                return PGResponse.getInstanceWhenError(PGResponse.DATA_INVALID);
            }
            VCCBRequest request = VCCBRequest.getInstance(dto.getRequestId(),requestData);
            Response response = requestApi(channelFunction.getHost() + channelFunction.getUrl(), objectMapper.writeValueAsString(request));
            String responseBody = response.body().string();
            logger.info("Response Body: " + responseBody);
            if(response.isSuccessful()){
                VCCBResponse vccbResponse = objectMapper.readValue(responseBody, VCCBResponse.class);
                return PGResponse.getInstance(PGResponse.SUCCESS,"","",vccbResponse);
            }
        }catch (IOException e){
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return PGResponse.getInstanceWhenError(PGResponse.FAIL);
    }

    @Override
    public PGResponse updateVirtualAccount(ChannelFunction channelFunction, String bodyRequest) {
        logger.info(commonLogService.createContentLogStartEndFunction(
                VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_UPDATE_VIRTUAL_ACCOUNT,true));
        try {
            UpdateVirtualAccountDto dto = objectMapper.readValue(bodyRequest, UpdateVirtualAccountDto.class);
            if(!virtualAccountService.isExistVirtualAccount(dto.getAccountNumber())){
                return PGResponse.getInstanceWhenError(PGResponse.VIRTUAL_ACCOUNT_NOT_EXIST);
            }
            UpdateVirtualAccount dataInput = new UpdateVirtualAccount(dto.getAccountNumber(), dto.getAccountNameSuffix());
            VCCBRequest request = VCCBRequest.getInstance(dto.getRequestId(), dataInput);
            Response response = requestApi(channelFunction.getHost()+channelFunction.getUrl(), objectMapper.writeValueAsString(request));
            String responseBody = response.body().string();
            logger.info("Response Body: " + responseBody);
            if(response.isSuccessful()){
                VCCBResponse vccbResponse = objectMapper.readValue(responseBody, VCCBResponse.class);
                CreateVAHolder vaResponse = objectMapper.convertValue(vccbResponse.getData(),CreateVAHolder.class);
                VirtualAccount va = virtualAccountService.findVirtualAccount(dto.getAccountNumber());
                va.setVirtualAccountName(vaResponse.getAccName());
                va.setPhoneNumber(dto.getPhoneNumber());
                virtualAccountService.updateVirtualAccount(va);
                return PGResponse.getInstance(PGResponse.SUCCESS,vaResponse.getError(),vaResponse.getMessage(),vccbResponse);
            }
            logger.info(commonLogService.createContentLogStartEndFunction(
                    VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_UPDATE_VIRTUAL_ACCOUNT,false));
        } catch (IOException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return PGResponse.getInstanceWhenError(PGResponse.FAIL);
    }

    @Override
    public PGResponse reopenVirtualAccount(ChannelFunction channelFunction, String bodyRequest) {
        logger.info(commonLogService.createContentLogStartEndFunction(
                VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_REOPEN_VIRTUAL_ACCOUNT,true));
        try {
            CloseAndReopenDto dto = objectMapper.readValue(bodyRequest,CloseAndReopenDto.class);
            if(!virtualAccountService.isExistVirtualAccount(dto.getAccountNumber())){
                return PGResponse.getInstanceWhenError(PGResponse.VIRTUAL_ACCOUNT_NOT_EXIST);
            }
            CloseAndReopen account = new CloseAndReopen(dto.getAccountNumber());
            VCCBRequest request = VCCBRequest.getInstance(dto.getRequestId(),account);
            Response response = requestApi(channelFunction.getHost()+channelFunction.getUrl(), objectMapper.writeValueAsString(request));
            String responseBody = response.body().string();
            logger.info("Request Body: " + responseBody);
            if(response.isSuccessful()){
                VCCBResponse vccbResponse = objectMapper.readValue(responseBody, VCCBResponse.class);
                CreateVAHolder vaResponse = objectMapper.convertValue(vccbResponse.getData(),CreateVAHolder.class);
                System.out.println(vaResponse);
                if(vaResponse.getError()==null) {
                    virtualAccountService.updateStatusVirtualAccount(vaResponse.getAccNo(), true);
                }
                logger.info(commonLogService.createContentLogStartEndFunction(
                        VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_REOPEN_VIRTUAL_ACCOUNT,false));
                return PGResponse.getInstance(PGResponse.SUCCESS,vaResponse.getError(),vaResponse.getMessage(),vccbResponse);
            }
        } catch (IOException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        logger.info(commonLogService.createContentLogStartEndFunction(
                VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_REOPEN_VIRTUAL_ACCOUNT,false));
        return PGResponse.getInstanceWhenError(PGResponse.FAIL);
    }

    @Override
    public PGResponse traceCallBack(ChannelFunction channelFunction, String bodyRequest) {
        logger.info(commonLogService.createContentLogStartEndFunction(
                VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_TRACE_VA_PAYMENT,true));
        try{
            TracePaymentDto dto = objectMapper.readValue(bodyRequest, TracePaymentDto.class);
            TracePayment transaction = new TracePayment(dto.getTrnRefNo());
            VCCBRequest request = VCCBRequest.getInstance(dto.getRequestId(),transaction);
            Response response = requestApi(channelFunction.getHost()+channelFunction.getUrl(), objectMapper.writeValueAsString(request));
            String responseBody = response.body().string();
            logger.info("Response Body: " + responseBody);
            if(response.isSuccessful()){
                VCCBResponse vccbResponse = objectMapper.readValue(responseBody, VCCBResponse.class);
                if(vccbResponse.getData() != null) {
                    CreateVAHolder vaResponse = objectMapper.convertValue(vccbResponse.getData(), CreateVAHolder.class);

                    logger.info(commonLogService.createContentLogStartEndFunction(
                            VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME, VCCBVAConfig.FUNCTION_CODE_TRACE_VA_PAYMENT, false));
                    return PGResponse.getInstance(PGResponse.SUCCESS, vaResponse.getError(), vaResponse.getMessage(), vccbResponse);
                }else{
                    logger.info(commonLogService.createContentLogStartEndFunction(
                            VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME, VCCBVAConfig.FUNCTION_CODE_TRACE_VA_PAYMENT, false));
                    return PGResponse.getInstance(PGResponse.SUCCESS, "", "", vccbResponse);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        logger.info(commonLogService.createContentLogStartEndFunction(
                VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_TRACE_VA_PAYMENT,false));
        return PGResponse.getInstanceWhenError(PGResponse.FAIL);
    }

    @Override
    public PGResponse getVAInformationInGateway(String bodyRequest) {
        // todo api noi bo
        return null;
    }

    @Override
    public ResponseEntity<?> notifyVAVCCB(String signature, String request) {
        logger.info(commonLogService.createContentLogStartEndFunction(
                VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_CALLBACK,true));
        String keyPath = FilePathUtil.getAbsolutePath(VCCBVirtualAccountServiceImp.class,"vccb_key/BVB_NGLG_VA_live_2048_pub.pem");
        PublicKey pk = PGSecurity.getPublicKey(keyPath);
        boolean isSignaturePassed = PGSecurity.verifySHA256withRSA(signature, request, pk);
        if(!isSignaturePassed){
            logger.info("signature checked: " + "failed" );
            return new ResponseEntity<>(PGResponse.getInstanceWhenError(PGResponse.DATA_VALIDATION_FAILED), HttpStatus.OK);
        }
        String errorCode = PGResponse.SUCCESS;
        try{
            CallbackHolder requestStore = objectMapper.readValue(request, CallbackHolder.class);
            CallbackVCCB callbackRequest = objectMapper.convertValue(requestStore.getData(), CallbackVCCB.class);
            NLVARequest newRequest = NLVARequest.parse(callbackRequest,VCCB_NAME);

            if("".equals(callbackRequest.getTransactionReferenceNumber()) || callbackRequest.getTransactionReferenceNumber() == null){
                errorCode = PGResponse.TRANSACTION_NOT_EXIST;
            }
            Payment paymentSaved = paymentService.findByMerchantTransactionId(String.valueOf(callbackRequest.getTransactionReferenceNumber()));
            if(paymentSaved != null){
                errorCode = PGResponse.TRANSACTION_EXIST;
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("bank_transaction_id", newRequest.getBankTransactionId());
            params.put("bank_account", newRequest.getBankAccount());
            params.put("transaction_amount", newRequest.getTransactionAmount());
            params.put("cashin_id",newRequest.getCashinId());
            params.put("bank_code", newRequest.getBankCode());
            params.put("bank_time", newRequest.getTransactionDate());

            String dataInput = new JSONObject(params).toString();

            Map<String, Object> paramsBig = new HashMap<>();
            paramsBig.put("status", true);
            paramsBig.put("error_code", errorCode);
            paramsBig.put("message", "");
            paramsBig.put("data", dataInput);
            paramsBig.put("checksum", PGSecurity.sha256(dataInput+PGSecurity.WITH_NGANLUONG_CALLBACK));

            String response = HttpUtil.send(NLVARequest.APP_NOTIFY_URL, paramsBig);
            System.out.println(response);
            logger.info(commonLogService.createContentLogStartEndFunction(
                    VIBConst.CHANNEL_CODE,VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_CALLBACK,false));
            if(errorCode.equals(PGResponse.SUCCESS)){
                PaymentDTO payment = PaymentDTO.builder()
                        .channelId(VCCB_BANK_CODE)
                        .merchantTransactionId(newRequest.getBankTransactionId())
                        .accountNo(newRequest.getBankAccount())
                        .description(callbackRequest.getNarrative())
                        .amount(newRequest.getTransactionAmount())
                        .channelTransactionStatus(1)
                        .merchantTransactionStatus(1)
                        .pgTransactionStatus(1)
                        .rawRequest(request)
                        .build();
                paymentService.createPayment(payment);
            }
            return new ResponseEntity(PGResponse.getInstanceWhenError(errorCode),HttpStatus.OK);
        } catch (IOException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        logger.info(commonLogService.createContentLogStartEndFunction(
                VCCBVAConfig.CHANNEL_CODE, VCCBVAConfig.SERVICE_NAME,VCCBVAConfig.FUNCTION_CODE_CALLBACK,false));
        return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
    }

    private Response requestApi(String url, String bodyRequest) throws IOException {
        logger.info(bodyRequest);
        String signature = getSignature(bodyRequest);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        okhttp3.MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, bodyRequest);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("authorization", MessageFormat.format("Bearer {0}", "43852d88-2002-3d4c-98f3-7d457a7fb017"))
                .addHeader("signature", signature)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println("Sign data "+ bodyRequest);
        System.out.println("signature: "+ signature);

        logger.info(response.toString());
        return response;
    }
    private String getSignature(String rawData) throws IOException {
        try {
            String path = FilePathUtil.getAbsolutePath(VCCBVirtualAccountServiceImp.class, "vccb_key/private_NL_live.pem");
            return PGSecurity.signatureRSASHA256(rawData, PGSecurity.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(new File(path)));
        }catch (GeneralSecurityException e){
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return "";
    }
    private KeyStore readKeyStore(){
        String path = FilePathUtil.getAbsolutePath(VCCBVirtualAccountServiceImp.class,"vccb_key/va.cer");
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path))){
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            while (bis.available() > 0) {
                Certificate cert = cf.generateCertificate(bis);
                ks.setCertificateEntry("fiddler"+bis.available(), cert);
            }
            return ks;
        }catch (Exception e){
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
    private SSLSocketFactory getSSLContext(KeyStore ks){
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(ks);
            KeyManagerFactory keymanagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keymanagerFactory.init(ks, "changeit".toCharArray());
            sslContext.init(keymanagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        }catch (NoSuchAlgorithmException e){
            logger.info(VCCBVirtualAccountServiceImp.class.getName() + " getSSLContext " + "NoSuchAlgorithmException");
        }catch (Exception e ){
            logger.info(VCCBVirtualAccountServiceImp.class.getName() + " getSSLContext " + "Exception");
        }
        return null;
    }
    private X509TrustManager trustCert(){
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
        };
    }
}
