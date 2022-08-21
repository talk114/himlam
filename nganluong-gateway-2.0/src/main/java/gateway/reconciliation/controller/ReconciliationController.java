package gateway.reconciliation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
//import gateway.reconciliation.service.MBReconciliationService;
import gateway.core.dto.PGRequest;
import gateway.core.dto.PGResponse;
import gateway.core.service.PGValidate;
//import gateway.reconciliation.service.VIBIBFTReconciliationService;
//import gateway.reconciliation.service.VIBVAReconciliationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gateway.core.channel.mb.dto.MBConst;
import vn.nganluong.naba.channel.vib.dto.VIBConst;
import vn.nganluong.naba.entities.*;
import vn.nganluong.naba.service.*;


@RestController
@RequestMapping(value = "/reconciliation")
public class ReconciliationController {

    private static final Logger logger = LogManager.getLogger(ReconciliationController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelFunctionService channelFunctionService;

    @Autowired
    private PgFunctionService pgFunctionService;

    @Autowired
    private PaymentAccountService paymentAccountService;

//    @Autowired
//    private MBReconciliationService mbReconciliationService;

//    @Autowired
//    private VIBIBFTReconciliationService vibibftReconciliationService;

//    @Autowired
//    private VIBVAReconciliationService vibvaReconciliationService;

    @Autowired
    private PgEndpointService pgEndpointService;

    @Autowired
    private PgUserService pgUserService;

    @Autowired
    private CommonPGResponseService commonPGResponseService;


    @PostMapping(path = "/request", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<?> request(@RequestBody String data) {
        ResponseEntity<PGResponse> responseEntity = null;
        try {
            PGRequest req = objectMapper.readValue(data, PGRequest.class);

            PgUser pgUser = pgUserService.findByCode(req.getPgUserCode());
            PgEndpoint pgEndpoint = pgEndpointService.findById(pgUser.getEndpointId());
            if (!PGValidate.validateChecksum(req, pgEndpoint)) {
                System.out.println("Checksum invalid.");
                // TODO Uncomment
                // return commonPGResponseService.returnBadRequets_WithCause("Checksum invalid.");
            }

            PgFunction pgFunction = pgFunctionService.findByCodeAndChannelCode(req.getFnc(), req.getChannelName());
            Channel channel = channelService.findById(pgFunction.getChannelId());
            String fnc = pgFunction.getCode();

//            ChannelFunction channelFunction = channelFunctionService.findChannelFunctionByName(req.getChannelName());
//            PaymentAccount paymentAccount = paymentAccountService
//                    .getPaymentAccountByUserIdAndChannelId(pgUser.getId(), channel.getId());

            switch (channel.getCode()) {
//                case MBConst.CHANNEL_CODE:
//                    responseEntity = callActionMBBank(fnc, req);
//                    break;
                case VIBConst.CHANNEL_CODE:
                    responseEntity = callActionVIBBank(fnc, req);
                    break;
                default:
                    break;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return responseEntity;
    }

//    private ResponseEntity<PGResponse> callActionMBBank(String fnc, PGRequest pgRequest) {
//        ResponseEntity<PGResponse> responseEntity = null;
//        switch (fnc) {
//            case MBConst.FUNCTION_CODE_RECONCILIATION_DAY_STEP_1:
//                responseEntity = mbReconciliationService.doDayReconciliationMBEcomStep1(pgRequest);
//                break;
//            case MBConst.FUNCTION_CODE_RECONCILIATION_DAY_STEP_2:
//                responseEntity = mbReconciliationService.doDayReconciliationMBEcomStep2(pgRequest);
//                break;
//
//            case MBConst.FUNCTION_CODE_RECONCILIATION_MONTH:
//                responseEntity = mbReconciliationService.doMonthReconciliationMBEcom(pgRequest);
//                break;
//            default:
//                break;
//        }
//        return responseEntity;
//    }

    private ResponseEntity<PGResponse> callActionVIBBank(String fnc, PGRequest pgRequest) {
        ResponseEntity<PGResponse> responseEntity = null;
        switch (fnc) {
//            case VIBConst.FUNCTION_CODE_IBFT_RECONCILIATION_DAY:
//                responseEntity = vibibftReconciliationService.doDayReconciliationVIBIBFT(pgRequest);
//                break;
//
//            case VIBConst.FUNCTION_CODE_IBFT_RECONCILIATION_MONTH:
//                responseEntity = vibibftReconciliationService.doMonthReconciliationVIBIBFT(pgRequest);
//                break;

//            case VIBConst.FUNCTION_CODE_VA_RECONCILIATION_DAY:
//                responseEntity = vibvaReconciliationService.doDayReconciliationVIBVA(pgRequest);
//                break;
//
//            case VIBConst.FUNCTION_CODE_VA_RECONCILIATION_MONTH:
//                responseEntity = vibvaReconciliationService.doMonthReconciliationVIBVA(pgRequest);
//                break;
            default:
                break;
        }
        return responseEntity;
    }

}
