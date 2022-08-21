package gateway.core.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.anbinhbank.dto.ABBankConstants;
import gateway.core.channel.anbinhbank.service.ABBEcomService;
import gateway.core.channel.bidv.bidv_transfer_247.dto.BIDVTransferConstants;
import gateway.core.channel.bidv.bidv_transfer_247.service.BIDVTransfer247Service;
import gateway.core.channel.bidv_ecom.dto.BidvEcomConstants;
import gateway.core.channel.bidv_ecom.service.BIDVEcomService;
import gateway.core.channel.cybersouce.dto.CybersourceConfig;
import gateway.core.channel.cybersouce.service.CybersouceService;
import gateway.core.channel.dong_a_bank.dto.DABConstants;
import gateway.core.channel.dong_a_bank.service.DongABankService;
import gateway.core.channel.mb.dto.MBConst;
import gateway.core.channel.mb.service.MBEcomService;
import gateway.core.channel.mb_qrcode.dto.MBBankVNPayOffUsConstant;
import gateway.core.channel.mb_qrcode.service.MBBankQRCodeOffusService;
import gateway.core.channel.migs.config.MIGSConfig;
import gateway.core.channel.migs.service.MIGSService;
import gateway.core.channel.msb_ecom.dto.MSBEcomConstant;
import gateway.core.channel.msb_ecom.service.MSBEcomService;
import gateway.core.channel.msb_offus.dto.MSBOFFUSConstant;
import gateway.core.channel.msb_offus.service.MSBOFFUSService;
import gateway.core.channel.msb_onus.dto.MSBONUSConstant;
import gateway.core.channel.msb_onus.service.MSBONUSService;
import gateway.core.channel.msb_qr.dto.MsbConstants;
import gateway.core.channel.msb_qr.service.MSBQRCodeService;
import gateway.core.channel.msb_va.dto.MSBVAContants;
import gateway.core.channel.msb_va.service.MSBVAService;
import gateway.core.channel.napas.dto.NapasConstans;
import gateway.core.channel.napas.service.NapasService;
import gateway.core.channel.napas.service.NapasWLService;
import gateway.core.channel.ocb.dto.OCBConstants;
import gateway.core.channel.ocb.service.OCBService;
import gateway.core.channel.onepay.dto.OnePayConstants;
import gateway.core.channel.onepay.service.OnePayService;
import gateway.core.channel.smart_pay.dto.SmartPayConstants;
import gateway.core.channel.smart_pay.service.SmartPayService;
import gateway.core.channel.stb_ecom.dto.STBConstants;
import gateway.core.channel.stb_ecom.service.STBEcomNglService;
import gateway.core.channel.tcb.dto.TCBConstants;
import gateway.core.channel.tcb.service.TCBService;
import gateway.core.channel.tcb_qrcode.dto.TCB_QrcodeConstants;
import gateway.core.channel.tcb_qrcode.service.TCB_QrcodeService;
import gateway.core.channel.vcb_ecom_atm.dto.VCBEcomAtmConstants;
import gateway.core.channel.vcb_ecom_atm.service.VCBEcomAtmService;
import gateway.core.channel.vcb_ib.dto.VCBIbConstants;
import gateway.core.channel.vcb_ib.service.VCBIbService;
import gateway.core.channel.vccb.dto.VCCBConstants;
import gateway.core.channel.vccb.service.VCCBService;
import gateway.core.channel.vccb_va.dto.VCCBVAConfig;
import gateway.core.channel.vccb_va.service.VCCBVirtualAccountService;
import gateway.core.channel.vib.service.VIBIBFTService;
import gateway.core.channel.vib.service.VIBVAService;
import gateway.core.dto.PGRequest;
import gateway.core.dto.PGResponse;
import gateway.core.service.PGValidate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.nganluong.naba.channel.vib.dto.VIBConst;
import vn.nganluong.naba.entities.*;
import vn.nganluong.naba.service.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/restful/api")
@CrossOrigin("*")
public class ApiInternalController {

    private static final Logger logger = LogManager.getLogger(ApiInternalController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String METHOD_NOT_FOUND = "FUNCTION BANK NOT FOUND";

    @Autowired
    private CommonLogService commonLogService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelFunctionService channelFunctionService;
    @Autowired
    private PgFunctionService pgFunctionService;
    @Autowired
    private PaymentAccountService paymentAccountService;
    @Autowired
    private MBEcomService mbEcomService;
    @Autowired
    private PgEndpointService pgEndpointService;
    @Autowired
    private PgUserService pgUserService;
    @Autowired
    private CommonPGResponseService commonPGResponseService;
    @Autowired
    private DongABankService dongABankService;
    @Autowired
    private VIBIBFTService vibibftService;
    @Autowired
    private VIBVAService vibvaService;
    @Autowired
    private OnePayService onePayService;
    @Autowired
    private VCCBService vccbService;
    @Autowired
    private MSBQRCodeService msbqrCodeService;
    @Autowired
    private MSBEcomService msbEcomService;
    @Autowired
    private ABBEcomService abbEcomService;
    @Autowired
    private STBEcomNglService stbEcomNglService;
    @Autowired
    private MBBankQRCodeOffusService mbBankQRCodeOffusService;
    @Autowired
    private SmartPayService smartPayService;
    @Autowired
    private VCBEcomAtmService vcbEcomAtmService;
    @Autowired
    private VCBIbService vcbIbService;
    @Autowired
    private VCCBVirtualAccountService vccbVirtualAccountService;
    @Autowired
    private BIDVTransfer247Service bidvTransfer247Service;
    @Autowired
    private CybersouceService cybersouceService;
    @Autowired
    private MSBOFFUSService mSBOFFUSService;
    @Autowired
    private TCBService tcbTransferService;
    @Autowired
    private NapasService napasService;
    @Autowired
    private MIGSService migsService;
    @Autowired
    private OCBService ocbService;
    @Autowired
    private MSBONUSService msbonusService;
    @Autowired
    private NapasWLService napasWLService;
    @Autowired
    private MSBVAService msbvaService;
    @Autowired
    private BIDVEcomService bidvEcomService;

    @Autowired
    private TCB_QrcodeService tcb_qrcodeService;

    @PostMapping(path = "/request", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<?> request(@RequestBody String data) {
        String requestId = UUID.randomUUID().toString();
        logger.info("request_id: " + requestId);
        logger.info("Request API: " + data);
        ResponseEntity<PGResponse> responseEntity = null;
        try {
            PGRequest req = objectMapper.readValue(data, PGRequest.class);

            PgUser pgUser = pgUserService.findByCode(req.getPgUserCode());
            PgEndpoint pgEndpoint = pgEndpointService.findById(pgUser.getEndpointId());
            if (!PGValidate.validateChecksum(req, pgEndpoint)) {
                logger.info("Checksum invalid.");
                return commonPGResponseService.returnBadRequets_WithCause("Checksum invalid.");
            }

            PgFunction pgFunction = pgFunctionService.findByCodeAndChannelCode(req.getFnc(), req.getChannelName());
            Channel channel = channelService.findById(pgFunction.getChannelId());
            String fnc = pgFunction.getCode();

            // Gateway 1 thì thiết lập PgFunction Code và ChannelFunction Code có giá trị giống nhau
            ChannelFunction channelFunction = channelFunctionService.findChannelFunctionByCodeAndChannelId(fnc, channel.getId());
            PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountByUserIdAndChannelId(pgUser.getId(), channel.getId());

            switch (channel.getCode()) {
                case DABConstants.CHANNEL_CODE:
                    responseEntity = callActionDongABank(channelFunction, paymentAccount, fnc, req);
                    break;
                case MBConst.CHANNEL_CODE:
                    responseEntity = callActionMBBank(channelFunction, paymentAccount, fnc, req);
                    break;
                case VIBConst.CHANNEL_CODE:
                    responseEntity = callActionVIBBank(fnc, req);
                    break;
                case OnePayConstants.CHANNEL_CODE:
                    responseEntity = callActionOnePay(channelFunction, paymentAccount, fnc, req);
                    break;
                case VCCBConstants.CHANNEL_CODE:
                    responseEntity = callActionVCCB(channelFunction, fnc, req);
                    break;
                case VCCBConstants.CHANNEL_CODE_VA:
                    responseEntity = this.callActionVCCBVA(channelFunction, fnc, req);
                    break;
                case MsbConstants.CHANNEL_CODE:
                    responseEntity = callActionMSBQR(channel.getId(), paymentAccount, fnc, req);
                    break;
                case MSBEcomConstant.CHANNEL_CODE:
                    responseEntity = callActionMSBEcom(channelFunction, paymentAccount, fnc, req);
                    break;
                case ABBankConstants.CHANNEL_CODE:
                    responseEntity = callActionAnBinhBank(channelFunction, paymentAccount, fnc, req);
                    break;
                case STBConstants.CHANNEL_CODE:
                    responseEntity = callActionSTBEcom(paymentAccount, fnc, req);
                    break;
                case MBBankVNPayOffUsConstant.CHANNEL_CODE:
                    responseEntity = callActionMBBankQR(fnc, req);
                    break;
                case SmartPayConstants.CHANNEL_CODE:
                    responseEntity = callActionSmartPay(fnc, req);
                    break;
                case VCBEcomAtmConstants.CHANNEL_CODE:
                    responseEntity = callActionVCBEcomAtm(fnc, req);
                    break;
                case VCBIbConstants.CHANNEL_CODE:
                    responseEntity = callActionVCBIb(channelFunction, paymentAccount, fnc, req);
                    break;
                case BIDVTransferConstants.CHANNEL_CODE:
                    responseEntity = callActionBIDVTransfer247(paymentAccount, fnc, req);
                    break;
                case CybersourceConfig.CHANNEL_CODE:
                    responseEntity = callPaymentCybersource(channelFunction, fnc, req);
                    break;
                case MSBOFFUSConstant.CHANNEL_CODE:
                    responseEntity = callPaymentMSBOFFUS(paymentAccount, channelFunction, fnc, req);
                    break;
                case TCBConstants.CHANNEL_CODE:
                    responseEntity = callTCBTransfer(paymentAccount, channelFunction, fnc, req);
                    break;
                case NapasConstans.CHANNEL_CODE:
                    responseEntity = callNapas(channelFunction, paymentAccount, fnc, req);
                    break;
                case NapasConstans.CHANNEL_CODE_WL:
                    responseEntity = callNapasWL(channelFunction, paymentAccount, fnc, req);
                    break;
                case MIGSConfig.CHANNEL_CODE:
                    responseEntity = callMpgs(channelFunction, paymentAccount, fnc, req);
                    break;
                case OCBConstants.CHANNEL_CODE:
                    responseEntity = callOCB(channelFunction, paymentAccount, fnc, req);
                    break;
                case MSBONUSConstant.CHANNEL_CODE:
                    responseEntity = callMSBONUS(channelFunction, paymentAccount, fnc, req);
                    break;
                case BidvEcomConstants.CHANNEL_CODE:
                    responseEntity = callBidvEcom(channelFunction, paymentAccount, fnc, req);
                    break;
                case MSBVAContants.CHANNEL_CODE:
                    responseEntity = callMSBVA(channelFunction, paymentAccount, fnc, req);
                    break;
                case TCB_QrcodeConstants.CHANNEL_CODE:
                    responseEntity = callTCBQrCodeAction(channelFunction, paymentAccount, fnc, req);
                    break;
                default:
                    logger.info("CHANNEL NOT FOUND");
                    responseEntity = commonPGResponseService.returnBadGatewayWithCause("NO CHANNEL FOUND");
                    break;
            }

        } catch (Exception e) {
            String error = "GATEWAY REQUEST ERROR " + ExceptionUtils.getStackTrace(e);
            logger.error(error, e);
            return commonPGResponseService.returnBadGatewayWithCause(error,requestId);
        }
        responseEntity.getBody().setRequestId(requestId);
        logger.info(responseEntity.toString());
        return responseEntity;
    }


    private ResponseEntity<PGResponse> callActionDongABank(ChannelFunction channelFunction, PaymentAccount paymentAccount,
                                                           String fnc, PGRequest pgRequest) {
        try {
            ResponseEntity<PGResponse> responseEntity = null;
            String pgRes = null;
            PGResponse pgResponse = null;
            switch (fnc) {
                case DABConstants.FUNCTION_CODE_CREATE_ORDER:
                    pgResponse = dongABankService.createOrder(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case DABConstants.FUNCTION_CODE_UPDATE_ORDER:
                    pgResponse = dongABankService.updateOrderStatus(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case DABConstants.FUNCTION_CODE_CHECK_ORDER:
                    pgResponse = dongABankService.checkOderStatus(channelFunction,paymentAccount,pgRequest.getData());
                    break;
                case DABConstants.FUNCTION_CODE_GET_BILL_INFO:
                    pgResponse = dongABankService.getBillInfo(channelFunction,paymentAccount,pgRequest.getData());
                    break;
                case DABConstants.FUNCTION_CODE_GET_BILL_NL_INFO:
                    pgResponse = dongABankService.getBillInfoNL(channelFunction,paymentAccount,pgRequest.getData());
                    break;
                case DABConstants.FUNCTION_CODE_GET_LIST_BILLING_SIZE:
                    pgResponse = dongABankService.getListBillingSize(channelFunction,paymentAccount,pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }

            // Chuyển đổi mã response code từ gateway 1 -> gateway 2
            // Trường hợp bank Đông Á, thành công trả về error_code 0 -> Chuyển error code 0 sang channelErrorCode
//            PGResponse pgResponse = objectMapper.readValue(pgRes, PGResponse.class);
//
//            if (StringUtils.equals(pgResponse.getErrorCode(), DABConstants.CREATE_ORDER_RESPONSE_CODE_SUCCESS)) {
//                pgResponse.setChannelErrorCode(pgResponse.getErrorCode());
//                pgResponse.setChannelMessage(pgResponse.getMessage());
//                PGResponse prefixResult = commonPGResponseService.returnGatewayRequestSuccessPrefix();
//                pgResponse.setMessage(prefixResult.getMessage());
//                pgResponse.setErrorCode(prefixResult.getErrorCode());
//            }
//            else {
//                PGResponse prefixResult = commonPGResponseService.returnChannelBadRequestPrefix();
//                pgResponse.setMessage(prefixResult.getMessage());
//                pgResponse.setErrorCode(prefixResult.getErrorCode());
//            }
            // return new ResponseEntity<PGResponse>(objectMapper.readValue(pgRes, PGResponse.class), HttpStatus.OK);
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }
    private ResponseEntity<PGResponse> callTCBQrCodeAction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, PGRequest req) {
        PGResponse pgResponse;
        try {
            switch (fnc) {
                case TCB_QrcodeConstants.FUNCTION_CODE_GET_QR_BANK_CODE:
                    pgResponse = tcb_qrcodeService.GetQrBankCode(channelFunction,paymentAccount,req.getData());
                    break;
                case TCB_QrcodeConstants.FUNCTION_CODE_CREATE_QR_CODE:
                    pgResponse = tcb_qrcodeService.CreateQrCode(channelFunction,paymentAccount,req.getData());
                    break;
                case TCB_QrcodeConstants.FUNCTION_CODE_CHECK_PAYMENT:
                    pgResponse = tcb_qrcodeService.CheckPayment(channelFunction,paymentAccount,req.getData());
                    break;

                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callActionMBBank(ChannelFunction channelFunction, PaymentAccount paymentAccount,
                                                        String fnc, PGRequest pgRequest) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case MBConst.FUNCTION_CODE_ADD_TRANSACTION:
                    pgResponse = mbEcomService.addTransaction(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION:
                    pgResponse = mbEcomService.confirmTransaction(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case MBConst.FUNCTION_CODE_STATUS_TRANSACTION:
                    pgResponse = mbEcomService.statusTransaction(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case MBConst.FUNCTION_CODE_REVERT_TRANSACTION:
                    pgResponse = mbEcomService.revertTransaction(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case MBConst.FUNCTION_CODE_REFUND_TRANSACTION:
                    pgResponse = mbEcomService.refund(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            String[] paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, MBConst.SERVICE_NAME,
                    fnc, false, false, true, paramsLog));
            return commonPGResponseService.returnBadGatewayWithCause(ExceptionUtils.getMessage(e));
        }
    }

    private ResponseEntity<PGResponse> callActionVIBBank(String fnc, PGRequest pgRequest) {
        ResponseEntity<PGResponse> responseEntity = null;
        switch (fnc) {
            case VIBConst.FUNCTION_CODE_ADD_TRANSACTION:
                responseEntity = (ResponseEntity<PGResponse>) vibibftService.addTransaction(pgRequest);
                break;
            case VIBConst.FUNCTION_CODE_HISTORY_TRANSACTION:
                responseEntity = (ResponseEntity<PGResponse>) vibibftService.getHistoryTransaction(pgRequest);
                break;
            case VIBConst.FUNCTION_CODE_VALID_ACCOUNT:
                responseEntity = (ResponseEntity<PGResponse>) vibibftService.checkInvalidAccount(pgRequest);
                break;
            case VIBConst.FUNCTION_CODE_STATUS_TRANSACTION:
                responseEntity = (ResponseEntity<PGResponse>) vibibftService.getTransactionStatus(pgRequest);
                break;
            case VIBConst.FUNCTION_CODE_BALANCE_ACCOUNT:
                responseEntity = (ResponseEntity<PGResponse>) vibibftService.getAccountBalance(pgRequest);
                break;
            case VIBConst.FUNCTION_CODE_CREATE_VA:
                responseEntity = (ResponseEntity<PGResponse>) vibvaService.createVirtualAccount(pgRequest);
                break;
            case VIBConst.FUNCTION_CODE_DELETE_VA:
                responseEntity = (ResponseEntity<PGResponse>) vibvaService.deleteVirtualAccount(pgRequest);
                break;
            case VIBConst.FUNCTION_CODE_ENABLE_VA:
                responseEntity = (ResponseEntity<PGResponse>) vibvaService.enableVirtualAccount(pgRequest);
                break;
            case VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_VA:
                responseEntity = (ResponseEntity<PGResponse>) vibvaService.getHistoryTransactionVA(pgRequest);
                break;
            case VIBConst.FUNCTION_CODE_VA_ERROR_HISTORY_TRANSACTION_VA:
                responseEntity = (ResponseEntity<PGResponse>) vibvaService.getErrorHistoryTransactionOfVACode(pgRequest);
                break;
            case VIBConst.FUNCTION_CODE_VA_HISTORY_TRANSACTION_OF_REAL_ACCOUNT:
                responseEntity = (ResponseEntity<PGResponse>) vibvaService.getHistoryTransactionOfRealAccount(pgRequest);
                break;
            case VIBConst.FUNCTION_CODE_VA_LIST_VA:
                responseEntity = (ResponseEntity<PGResponse>) vibvaService.getListVirtualAccount(pgRequest);
                break;
            case VIBConst.FUNCTION_CODE_VA_DETAIL_VA:
                responseEntity = (ResponseEntity<PGResponse>) vibvaService.getDetailVirtualAccount(pgRequest);
                break;
            default:
                logger.info(METHOD_NOT_FOUND);
                responseEntity = commonPGResponseService.returnBadGatewayWithCause("callActionVIBBank");
                break;
        }
        return responseEntity;
    }

    private ResponseEntity<PGResponse> callActionOnePay(ChannelFunction channelFunction, PaymentAccount paymentAccount,
                                                        String fnc, PGRequest pgRequest) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case OnePayConstants.FUNCTION_CODE_VERIFY_CARD:
                    pgResponse = onePayService.verifyCard(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case OnePayConstants.FUNCTION_CODE_VERIFY_AUTHEN:
                    pgResponse = onePayService.verifyAuthen(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case OnePayConstants.FUNCTION_CODE_QUERY_ORDER:
                    pgResponse = onePayService.query(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case OnePayConstants.FUNCTION_CODE_REFUND:
                    pgResponse = onePayService.refund(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            String[] paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(OnePayConstants.CHANNEL_CODE, OnePayConstants.SERVICE_NAME,
                    fnc, false, false, true, paramsLog));
            return commonPGResponseService.returnBadGatewayWithCause(ExceptionUtils.getMessage(e));
        }
    }

    private ResponseEntity<PGResponse> callActionVCCB(ChannelFunction channelFunction, String fnc, PGRequest pgRequest) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case VCCBConstants.FUNCTION_CODE_TRANSFER_CARD_IBFT:
                    pgResponse = vccbService.TransferCardIBFT(channelFunction, pgRequest.getData());
                    break;
                case VCCBConstants.FUNCTION_CODE_TRANSFER_BANK_ACC_IBFT:
                    pgResponse = vccbService.TransferBankAccIBFT(channelFunction, pgRequest.getData());
                    break;
                case VCCBConstants.FUNCTION_CODE_CHECK_BALANCE_BANK_ACC_IBFT:
                    pgResponse = vccbService.CheckBalanceBankAccIBFT(channelFunction, pgRequest.getData());
                    break;
                case VCCBConstants.FUNCTION_CODE_TRANSFER_BANK_ACC_VCCB:
                    pgResponse = vccbService.TransferBankAccVCCB(channelFunction, pgRequest.getData());
                    break;
                case VCCBConstants.FUNCTION_CODE_CHECK_BANK_ACC_VCCB:
                    pgResponse = vccbService.CheckBankAccVCCB(channelFunction, pgRequest.getData());
                    break;
                case VCCBConstants.FUNCTION_CODE_CHECK_CARD_VCCB:
                    pgResponse = vccbService.CheckCardVCCB(channelFunction, pgRequest.getData());
                    break;
                case VCCBConstants.FUNCTION_CODE_TRANSFER_CARD_VCCB:
                    pgResponse = vccbService.TransferCardVCCB(channelFunction, pgRequest.getData());
                    break;
                case VCCBConstants.FUNCTION_CODE_CHECK_CARD_IBFT:
                    pgResponse = vccbService.CheckCardIBFT(channelFunction, pgRequest.getData());
                    break;
                case VCCBConstants.FUNCTION_CODE_CHECK_BALANCE:
                    pgResponse = vccbService.CheckBalance(channelFunction, pgRequest.getData());
                    break;
                case VCCBConstants.FUNCTION_CODE_CHECK_BANK_ACC_IBFT:
                    pgResponse = vccbService.CheckBankAccIBFT(channelFunction, pgRequest.getData());
                    break;
                case VCCBConstants.FUNCTION_CODE_UPLOAD_RECONCILIATION_NGLA:
                    pgResponse = vccbService.UploadReconciliationNGLA(channelFunction, pgRequest.getData());
                    break;
                case VCCBConstants.FUNCTION_CODE_UPLOAD_RECONCILIATION_NGLB:
                    pgResponse = vccbService.UploadReconciliationNGLB(channelFunction, pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND IN VCCB MERCHANT").getBody();
                    break;
            }
            return new ResponseEntity<>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            String[] paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                    fnc, false, false, true, paramsLog));
            return commonPGResponseService.returnBadGatewayWithCause(ExceptionUtils.getMessage(e));
        }
    }

    private ResponseEntity<PGResponse> callActionVCCBVA(ChannelFunction channelFunction, String fnc, PGRequest pgRequest) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case VCCBVAConfig.FUNCTION_CODE_CREATE_VIRTUAL_ACCOUNT:
                    pgResponse = vccbVirtualAccountService.createVirtualAccount(channelFunction, pgRequest.getData());
                    break;
                case VCCBVAConfig.FUNCTION_CODE_UPDATE_VIRTUAL_ACCOUNT:
                    pgResponse = vccbVirtualAccountService.updateVirtualAccount(channelFunction, pgRequest.getData());
                    break;
                case VCCBVAConfig.FUNCTION_CODE_CLOSE_VIRTUAL_ACCOUNT:
                    pgResponse = vccbVirtualAccountService.closeVirtualAccount(channelFunction, pgRequest.getData());
                    break;
                case VCCBVAConfig.FUNCTION_CODE_REOPEN_VIRTUAL_ACCOUNT:
                    pgResponse = vccbVirtualAccountService.reopenVirtualAccount(channelFunction, pgRequest.getData());
                    break;
                case VCCBVAConfig.FUNCTION_CODE_DETAIL_VIRTUAL_ACCOUNT:
                    pgResponse = vccbVirtualAccountService.viewAccountDetails(channelFunction, pgRequest.getData());
                    break;
                case VCCBVAConfig.FUNCTION_CODE_GET_LIST_VIRTUAL_ACCOUNT:
                    pgResponse = vccbVirtualAccountService.getListVirtualAccount(channelFunction, pgRequest.getData());
                    break;
                case VCCBVAConfig.FUNCTION_CODE_TRACE_VA_PAYMENT:
                    pgResponse = vccbVirtualAccountService.traceCallBack(channelFunction, pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND IN VCCB MERCHANT").getBody();
                    break;
            }
            return new ResponseEntity<>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            String[] paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                    fnc, false, false, true, paramsLog));
            return commonPGResponseService.returnBadGatewayWithCause(ExceptionUtils.getMessage(e));
        }
    }

    private ResponseEntity<PGResponse> callActionMSBQR(Integer channelId, PaymentAccount paymentAccount,
                                                       String fnc, PGRequest pgRequest) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case MsbConstants.FUNCTION_CODE_QR_CODE_PAYMENT:
                    pgResponse = msbqrCodeService.QrCodePayment(channelId, paymentAccount, pgRequest.getData());
                    break;
                case MsbConstants.FUNCTION_CODE_CHECK_QR_ORDER:
                    pgResponse = msbqrCodeService.CheckQrOrder(paymentAccount, pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callActionMSBEcom(ChannelFunction channelFunction, PaymentAccount paymentAccount,
                                                         String fnc, PGRequest pgRequest) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case MSBEcomConstant.FUNCTION_CODE_CREATE_PAYMENT:
                    pgResponse = msbEcomService.createPayment(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case MSBEcomConstant.FUNCTION_CODE_VERIFY_TRANSACTION:
                    pgResponse = msbEcomService.verifyTransaction(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case MSBEcomConstant.FUNCTION_CODE_INQUIRY_TRANSACTION:
                    pgResponse = msbEcomService
                            .inquiryTransaction(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callActionAnBinhBank(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, PGRequest pgRequest) {
        ResponseEntity<PGResponse> responseEntity = null;
        String pgRes = null;
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case ABBankConstants.FUNCTION_CODE_VERIFY_OTP:
                    pgResponse = abbEcomService.VerifyOtp(paymentAccount, pgRequest.getData());
                    break;
                case ABBankConstants.FUNCTION_CODE_GET_BALANCE:
                    pgResponse = abbEcomService.GetBalance(paymentAccount, pgRequest.getData());
                    break;
                case ABBankConstants.FUNCTION_CODE_CHECK_TRANS_STATUS:
                    pgResponse = abbEcomService.CheckTransStatus(paymentAccount, pgRequest.getData());
                    break;
                case ABBankConstants.FUNCTION_CODE_PAYMENT_WITH_BANK_ACC:
                    pgResponse = abbEcomService.PaymentWithBankAcc(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case ABBankConstants.FUNCTION_CODE_DOI_SOAT:
                    abbEcomService.DoiSoat();
                    break;
//                case ABBankConstants.FUNCTION_CODE_DOI_SOAT_GW:
//                    abbEcomService.DoiSoatGW(pgRequest.getData());
//                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callActionSTBEcom(PaymentAccount paymentAccount, String fnc, PGRequest pgRequest) {
        ResponseEntity<PGResponse> responseEntity = null;
        String pgRes = null;
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case STBConstants.FUNCTION_CODE_REQUEST_OTP:
                    pgResponse = stbEcomNglService.RequestOTP(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_REQUEST_OTP_ACC:
                    pgResponse = stbEcomNglService.RequestOTPAcc(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_TOP_UP_BY_CARD:
                    pgResponse = stbEcomNglService.TopUpByCard(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_TOP_UP_BY_ACCOUNT:
                    pgResponse = stbEcomNglService.TopUpByAccount(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_PURCHASE_BY_CARD:
                    pgResponse = stbEcomNglService.PurchaseByCard(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_PURCHASE_BY_ACCOUNT:
                    pgResponse = stbEcomNglService.PurchaseByAccount(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_SUBSCRIPTION_INQUIRY:
                    pgResponse = stbEcomNglService.SubscriptionInquiry(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_CASH_OUT_SUBSCRIPTION:
                    pgResponse = stbEcomNglService.CashOutSubscription(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_CARD_INQUIRY:
                    pgResponse = stbEcomNglService.CardInquiry(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_TRANSFER_TO_STB_CARD:
                    pgResponse = stbEcomNglService.FundTransferToSTBCard(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_ACCOUNT_INQUIRY:
                    pgResponse = stbEcomNglService.AccountInquiry(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_TRANSFER_TO_STB_ACCOUNT:
                    pgResponse = stbEcomNglService.FundTransferToSTBAccount(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNC_CANCEL_SUBSCRIPTION:
                    pgResponse = stbEcomNglService.CancelSubscription(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_LINK_CARD:
                    pgResponse = stbEcomNglService.LinkCard(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_DOMESTIC_INQUIRY:
                    pgResponse = stbEcomNglService.DomesticInquiry(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_DOMESTIC_FUND_TRANSFER:
                    pgResponse = stbEcomNglService.DomesticFundTransfer(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_DOMESTIC_ACCOUNT_INQUIRY:
                    pgResponse = stbEcomNglService.DomesticAccountInquiry(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_DOMESTIC_ACCOUNT_FUND_TRANSFER:
                    pgResponse = stbEcomNglService.DomesticAccountFundTransfer(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_VISA_INQUIRY:
                    pgResponse = stbEcomNglService.VisaInquiry(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_VISA_TRANSFER:
                    pgResponse = stbEcomNglService.VisaTransfer(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_TRANSACTION_QUERY:
                    pgResponse = stbEcomNglService.TransactionQuery(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_PAYMENT:
                    pgResponse = stbEcomNglService.Payment(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_VERIFY_OTP_PAYMENT:
                    pgResponse = stbEcomNglService.VerifyOtpPayment(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_REVERSAL_PAYMENT:
                    pgResponse = stbEcomNglService.ReversalPayment(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_CHECK_CARD:
                    pgResponse = stbEcomNglService.CheckCard(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_TRANSFER_TO_CARD:
                    pgResponse = stbEcomNglService.TransferToCard(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_REVERSAL_TRANSFER_TO_CARD:
                    pgResponse = stbEcomNglService.ReversalTransferToCard(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_CHECK_CARD_IBFT:
                    pgResponse = stbEcomNglService.CheckCardIBFT(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_TRANSFER_TO_CARD_IBFT:
                    pgResponse = stbEcomNglService.TransferToCardIBFT(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_CHECK_VISA_MASTERCARD:
                    pgResponse = stbEcomNglService.CheckVisaMasterCard(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_TRANSFER_TO_VISA_MASTERCARD:
                    pgResponse = stbEcomNglService.TransferToVisaMasterCard(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_CHECK_BANK_ACC_STB:
                    pgResponse = stbEcomNglService.CheckBankAccSTB(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_TRANSFER_TO_BANK_ACC_STB:
                    pgResponse = stbEcomNglService.TransferToBankAccSTB(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_CHECK_BANK_ACC_IBFT:
                    pgResponse = stbEcomNglService.CheckBankAccIBFT(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_TRANSFER_TO_BANK_ACC_IBFT:
                    pgResponse = stbEcomNglService.TransferToBankAccIBFT(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_QUERY:
                    pgResponse = stbEcomNglService.Query(paymentAccount, pgRequest.getData());
                    break;
                case STBConstants.FUNCTION_CODE_QUERYBALANCE:
                    pgResponse = stbEcomNglService.QueryBalance(paymentAccount, pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callActionMBBankQR(String fnc, PGRequest pgRequest) {
        ResponseEntity<PGResponse> responseEntity = null;
        String pgRes = null;
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case MBBankVNPayOffUsConstant.FUNCTION_CODE_CREATE_QR_CODE:
                    pgResponse = mbBankQRCodeOffusService.createQRCode(pgRequest.getData());
                    break;
                case MBBankVNPayOffUsConstant.FUNCTION_CODE_CHECK_ORDER:
                    pgResponse = mbBankQRCodeOffusService.checkOrder(pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callActionSmartPay(String fnc, PGRequest pgRequest) {
        ResponseEntity<PGResponse> responseEntity = null;
        String pgRes = null;
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case SmartPayConstants.FUNCTION_CODE_CREATE_ORDER:
                    pgResponse = smartPayService.createOrder(pgRequest.getData());
                    break;
                case SmartPayConstants.FUNCTION_CODE_QUERY_ORDER:
                    pgResponse = smartPayService.queryOrder(pgRequest.getData());
                    break;
                case SmartPayConstants.FUNCTION_CODE_SEND_OTP:
                    pgResponse = smartPayService.sendOTP(pgRequest.getData());
                    break;
                case SmartPayConstants.FUNCTION_CODE_VERIFY_OTP:
                    pgResponse = smartPayService.verifyOTP(pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callActionVCBEcomAtm(String fnc, PGRequest pgRequest) {
        ResponseEntity<PGResponse> responseEntity = null;
        String pgRes = null;
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case VCBEcomAtmConstants.FUNCTION_CODE_VERIFY_CARD:
                    pgResponse = vcbEcomAtmService.VerifyCard(pgRequest.getData());
                    break;
                case VCBEcomAtmConstants.FUNCTION_CODE_VERIFY_CARD_2:
                    pgResponse = vcbEcomAtmService.VerifyCard2(pgRequest.getData());
                    break;
                case VCBEcomAtmConstants.FUNCTION_CODE_VERIFY_OTP:
                    pgResponse = vcbEcomAtmService.VerifyOtp(pgRequest.getData());
                    break;
                case VCBEcomAtmConstants.FUNCTION_CODE_VERIFY_OTP_2:
                    pgResponse = vcbEcomAtmService.VerifyOtp2(pgRequest.getData());
                    break;
                case VCBEcomAtmConstants.FUNCTION_CODE_QUERY:
                    pgResponse = vcbEcomAtmService.Query(pgRequest.getData());
                    break;
                case VCBEcomAtmConstants.FUNCTION_CODE_REFUND:
                    pgResponse = vcbEcomAtmService.Refund(pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callActionVCBIb(ChannelFunction channelFunction, PaymentAccount paymentAccount,
                                                       String fnc, PGRequest pgRequest) {
        ResponseEntity<PGResponse> responseEntity = null;
        String pgRes = null;
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case VCBIbConstants.FUNCTION_CODE_QUERY:
                    pgResponse = vcbIbService.query(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case VCBIbConstants.FUNCTION_CODE_VERIFY_PAYMENT:
                    pgResponse = vcbIbService.verifyPayment(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case VCBIbConstants.FUNCTION_CODE_REFUND:
                    pgResponse = vcbIbService.refund(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            String[] paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                    fnc, false, false, true, paramsLog));
            return commonPGResponseService.returnBadGatewayWithCause(ExceptionUtils.getMessage(e));
        }
    }

    private ResponseEntity<PGResponse> callActionBIDVTransfer247(PaymentAccount paymentAccount, String fnc, PGRequest pgRequest) {
        ResponseEntity<PGResponse> responseEntity = null;
        String pgRes = null;
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case BIDVTransferConstants.FUNCTION_CODE_GET_FILE_BIDV_DAILY:
                    pgResponse = bidvTransfer247Service.getFileBIDVDaily(pgRequest.getData());
                    break;
                case BIDVTransferConstants.FUNCTION_CODE_GET_LIST_BANK_247:
                    pgResponse = bidvTransfer247Service.getListBank247(paymentAccount, pgRequest.getData());
                    break;
                case BIDVTransferConstants.FUNCTION_CODE_GET_NAME_247:
                    pgResponse = bidvTransfer247Service.getName247(paymentAccount, pgRequest.getData());
                    break;
                case BIDVTransferConstants.FUNCTION_CODE_TRANSFER2_BANK_247:
                    pgResponse = bidvTransfer247Service.tranfer2Bank247(paymentAccount, pgRequest.getData());
                    break;
                case BIDVTransferConstants.FUNCTION_CODE_GET_NAME_BIDV:
                    pgResponse = bidvTransfer247Service.getNameBidv(paymentAccount, pgRequest.getData());
                    break;
                case BIDVTransferConstants.FUNCTION_CODE_INQUERY:
                    pgResponse = bidvTransfer247Service.inquery(paymentAccount, pgRequest.getData());
                    break;
                case BIDVTransferConstants.FUNCTION_CODE_CHECK_PROVIDER_BALANCE:
                    pgResponse = bidvTransfer247Service.checkProviderBalance(paymentAccount, pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callPaymentCybersource(ChannelFunction channelFunction, String fnc, PGRequest pgRequest) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case CybersourceConfig.FUNCTION_AUTHOIZATION_CARD:
                    pgResponse = cybersouceService.authorizeCard(pgRequest.getData());
                    break;
                case CybersourceConfig.FUNCTION_AUTHOIZATION_CARD_3D:
                    pgResponse = cybersouceService.authorizeCard3D(pgRequest.getData());
                    break;
                case CybersourceConfig.FUNCTION_PAYMENT:
                    pgResponse = cybersouceService.authorize(pgRequest.getData(), channelFunction);
                    break;
                case CybersourceConfig.FUNCTION_PAYMENT_3D:
                    pgResponse = cybersouceService.authorize3D(pgRequest.getData());
                    break;
                case CybersourceConfig.FUNCTION_VALIDATE_3DS:
                    pgResponse = cybersouceService.validate3Ds2(pgRequest.getData(), pgRequest.getPgUserCode());
                    break;
                case CybersourceConfig.VALIDATE_JWT:
                    pgResponse = cybersouceService.validateJWT(pgRequest, pgRequest.getPgUserCode());
                    break;
                case CybersourceConfig.FUNTION_CREATE_REQUEST_JWT:
                    pgResponse = cybersouceService.createRequestJwt(pgRequest.getData(), pgRequest.getPgUserCode());
                    break;
                case CybersourceConfig.FUNTION_DECODE_JWT_RESPONE:
                    pgResponse = cybersouceService.decodeJWT(pgRequest.getData(), pgRequest.getPgUserCode());
                    break;
                case CybersourceConfig.FUNCTION_CANCEL_AUTHOIZATION:
                    pgResponse = cybersouceService.cancelAuthorizeCard(pgRequest.getData());
                    break;
                case CybersourceConfig.FUNCTION_CHECK_ENROLLMENT:
                    pgResponse = cybersouceService.checkEnrollment(pgRequest.getData());
                    break;
                case CybersourceConfig.FUNCTION_SSP_CREATE_TRANSACTION:
                    pgResponse = cybersouceService.createTransaction(channelFunction, pgRequest.getData());
                    break;
                case CybersourceConfig.FUNCTION_SSP_RETRIEVE_PAYMENT_CREDENTIAL:
                    pgResponse = cybersouceService.CBSDecodePaymentCredential(channelFunction, pgRequest.getData());
                    break;
                case CybersourceConfig.FUNCTION_SSP_RETRIEVE_PAYMENT_CREDENTIAL_DECODE:
                    pgResponse = cybersouceService.NLDecodePaymentCredential(channelFunction, pgRequest.getData());
                    break;
                case CybersourceConfig.FUNCTION_SSP_NOTIFY_PAYMENT_RESULT:
                    pgResponse = cybersouceService.notifyPaymentResult(channelFunction, pgRequest.getData());
                    break;
                case CybersourceConfig.FUNCTION_SSP_HEALTH_CHECK:
                    pgResponse = cybersouceService.healthCheck();
                    break;
                case CybersourceConfig.FUNCTION_CHECK_ENROLLMENT_3DS2:
                    pgResponse = cybersouceService.checkEnrollment3ds(pgRequest.getData(), pgRequest.getPgUserCode());
                    break;
                case CybersourceConfig.FUNCTION_SETUP_3DS2:
                    pgResponse = cybersouceService.payerAuthenticationSetup3ds(pgRequest.getData(), pgRequest.getPgUserCode());
                    break;
//                case CybersourceConfig.FUNCTION_VALIDATE_3DS:
//                    pgResponse = cybersouceService.validate3Ds2(pgRequest.getData());
//                    break;
                case CybersourceConfig.AUTHORIZE_SUBSCRIPTION_3D:
                    pgResponse = cybersouceService.authorizeSubscription3D(pgRequest.getData(), pgRequest.getPgUserCode());
                    break;
                case CybersourceConfig.DELETE_TOKEN:
                    pgResponse = cybersouceService.deleteToken(pgRequest.getData(), pgRequest.getPgUserCode());
                    break;
                case CybersourceConfig.FUNCTION_CONVERT_TRANSACTION_TO_PROFILE:
                    pgResponse = cybersouceService.convertTransaction2Profile(pgRequest.getData(), pgRequest.getPgUserCode());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callPaymentMSBOFFUS(PaymentAccount paymentAccount, ChannelFunction channelFunction, String fnc, PGRequest pgRequest) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case MSBOFFUSConstant.FUNCTION_CREATE_ORDER:
                    pgResponse = mSBOFFUSService.createOrder(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case MSBOFFUSConstant.FUNCTION_CREATE_ORDER_ECOM:
                    pgResponse = mSBOFFUSService.createOrderEcom(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case MSBOFFUSConstant.FUNCTION_GET_TRANSACTION:
                    pgResponse = mSBOFFUSService.getTransaction(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case MSBOFFUSConstant.FUNCTION_GET_TRANSACTION_NL:
                    pgResponse = mSBOFFUSService.getTransactionByCashin(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case MSBOFFUSConstant.FUNCTION_CREATE_REFUND_TRANSACTION:
                    pgResponse = mSBOFFUSService.createReversalTransaction(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case MSBOFFUSConstant.FUNCTION_GET_TRANSACTION_REFUND:
                    pgResponse = mSBOFFUSService.getInquiryRefundTransaction(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case MSBOFFUSConstant.FUNCTION_UPDATE_TRANSACTION:
                    pgResponse = mSBOFFUSService.updateTransaction(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callTCBTransfer(PaymentAccount paymentAccount, ChannelFunction channelFunction, String fnc, PGRequest pgRequest) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case TCBConstants.FUNCTION_LIST_BANK_INFO:
                    pgResponse = tcbTransferService.InqListBankInfo(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case TCBConstants.FUNCTION_GET_TRANSACTION_STATUS:
                    pgResponse = tcbTransferService.InqTransactionStatus(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                case TCBConstants.FUNCTION_FUN_TRANSFER:
                    pgResponse = tcbTransferService.FundTransfer(channelFunction, paymentAccount, pgRequest.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callNapas(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, PGRequest req) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case NapasConstans.FUNTION_PURCHASE_OTP:
                    pgResponse = napasService.purchaseOtp(channelFunction, paymentAccount, req.getData());
                    break;
                case NapasConstans.FUNTION_QUERY_TRANSACTION:
                    pgResponse = napasService.queryTransDomestic(channelFunction, req.getData());
                    break;
                case NapasConstans.FUNTION_REFUND_DOMESTIC:
                    pgResponse = napasService.refundDomestic(channelFunction, req.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callNapasWL(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, PGRequest req) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case NapasConstans.FUNTION_PURCHASE_OTP_WL:
                    pgResponse = napasWLService.purchaseOtpWL(channelFunction, paymentAccount, req.getData());
                    break;
                case NapasConstans.FUNTION_QUERY_TRANSACTION_WL:
                    pgResponse = napasWLService.queryTransDomesticWL(channelFunction, req.getData());
                    break;
                case NapasConstans.FUNTION_REFUND_DOMESTIC_WL:
                    pgResponse = napasWLService.refundDomesticWL(channelFunction, req.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callMpgs(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, PGRequest req) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case MIGSConfig.FUNTION_INITIATE_AUTHENTICATION:
                    pgResponse = migsService.initiateAuthentication(channelFunction, paymentAccount, req.getData());
                    break;
                case MIGSConfig.FUNTION_AUTHENTICATE_PAYER:
                    pgResponse = migsService.authenticatePayer(channelFunction, paymentAccount, req.getData());
                    break;
                case MIGSConfig.FUNTION_PAY_3DS2:
                    pgResponse = migsService.pay3DS2(channelFunction, paymentAccount, req.getData());
                    break;
                case MIGSConfig.FUNTION_PAY_3DS1:
                    pgResponse = migsService.pay3DS1(channelFunction, paymentAccount, req.getData());
                    break;
                case MIGSConfig.FUNTION_CHECK_3DS_ENROLLMENT:
                    pgResponse = migsService.check3DSEnrollment(channelFunction, paymentAccount, req.getData());
                    break;
                case MIGSConfig.FUNTION_PROCESS_ACS_RESULT:
                    pgResponse = migsService.processAcsResult(channelFunction, paymentAccount, req.getData());
                    break;
                case MIGSConfig.FUNTION_REFUND_TRANSACTION:
                    pgResponse = migsService.refundTrans(channelFunction, paymentAccount, req.getData());
                    break;
                case MIGSConfig.FUNTION_QUERY_TRANSACTION:
                    pgResponse = migsService.queryTrans(channelFunction, paymentAccount, req.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callOCB(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, PGRequest req) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case OCBConstants.FUNCTION_PAYMENT_STEP_1:
                    pgResponse = ocbService.paymentStep1(channelFunction, paymentAccount, req.getData());
                    break;
                case OCBConstants.FUNCTION_PAYMENT_STEP_2:
                    pgResponse = ocbService.paymentStep2(channelFunction, paymentAccount, req.getData());
                    break;
                case OCBConstants.FUNCTION_PAYMENT_STATUS:
                    pgResponse = ocbService.statusPayment(channelFunction, paymentAccount, req.getData());
                    break;
                case OCBConstants.FUNCTION_RESEND_OTP:
                    pgResponse = ocbService.resendOTP(channelFunction, paymentAccount, req.getData());
                    break;
                case OCBConstants.FUNCTION_TRANSACTION_HISTORY:
                    pgResponse = ocbService.checkTransHistory(channelFunction, paymentAccount, req.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callMSBONUS(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, PGRequest req) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case MSBONUSConstant.FUNCTION_CREATE_TRANSACTION:
                    pgResponse = msbonusService.createOrder(channelFunction, paymentAccount, req.getData());
                    break;
                case MSBONUSConstant.FUNCTION_VERIFY_TRANSACTION:
                    pgResponse = msbonusService.verifyTransaction(channelFunction, paymentAccount, req.getData());
                    break;
                case MSBONUSConstant.FUNCTION_GET_TRANSACTION:
                    pgResponse = msbonusService.getTransaction(channelFunction, paymentAccount, req.getData());
                    break;
                case MSBONUSConstant.FUNCTION_NOTI_TRANSACTION_STATUS:
                    pgResponse = msbonusService.notifyTransStatus(channelFunction, paymentAccount, req.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callBidvEcom(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, PGRequest req) {
        PGResponse pgResponse;
        try {
            switch (fnc) {
                case BidvEcomConstants.FNC_INIT_TRANS:
                    pgResponse = bidvEcomService.inittrans(channelFunction, paymentAccount, fnc, req.getData());
                    break;
                case BidvEcomConstants.FNC_VERIFY:
                    pgResponse = bidvEcomService.verify(channelFunction, paymentAccount, fnc, req.getData());
                    break;
                case BidvEcomConstants.FNC_INQUIRY:
                    pgResponse = bidvEcomService.inquiry(channelFunction, paymentAccount, fnc, req.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }

    private ResponseEntity<PGResponse> callMSBVA(ChannelFunction channelFunction, PaymentAccount paymentAccount, String fnc, PGRequest req) {
        PGResponse pgResponse = null;
        try {
            switch (fnc) {
                case MSBVAContants.FNC_LOGIN:
                    pgResponse = msbvaService.loginMSB(channelFunction);
                    break;
                case MSBVAContants.FNC_TRANSACTION_HISTORY:
                    pgResponse = msbvaService.getTransactionHistory(channelFunction, req.getData());
                    break;
                case MSBVAContants.FNC_CREATE_VA:
                    pgResponse = msbvaService.createMSBVA(channelFunction, req.getData());
                    break;
                case MSBVAContants.FNC_UPDATE_VA:
                    pgResponse = msbvaService.updateMSBVA(channelFunction, req.getData());
                    break;
                default:
                    logger.info(METHOD_NOT_FOUND);
                    pgResponse = commonPGResponseService.returnBadGatewayWithCause("NO FUNCTION FOUND").getBody();
                    break;
            }
            return new ResponseEntity<PGResponse>(pgResponse, HttpStatus.OK);
        } catch (Exception e) {
            return (ResponseEntity<PGResponse>) commonPGResponseService.returnBadRequets_WithCause(e.getMessage());
        }
    }
}
