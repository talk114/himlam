package gateway.core.channel.napas.controller;

import gateway.core.channel.napas.service.NapasService;
import gateway.core.channel.napas.service.NapasWLService;
import gateway.core.dto.PGResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.nganluong.naba.service.CommonPGResponseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/restful/api")
public class NapasController {
    @Autowired
    NapasService napasService;
    @Autowired
    NapasWLService napasWLService;
    @Autowired
    CommonPGResponseService commonPGResponseService;

    private static final Logger logger = LogManager.getLogger(NapasController.class);

    @PostMapping(path = "/napasresult", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    PGResponse resultPurchaseOtp(@RequestBody String request) {
        try {
            PGResponse pgResponse = napasService.resultPurchaseOtp(request);
            return pgResponse;
        } catch (Exception e) {
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @PostMapping(path = "/napasIpnWL", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    PGResponse napasIPNWL(@RequestBody String request) {
        try {
            PGResponse pgResponse = napasWLService.ipnPurchaseOtpWL(request);
            return pgResponse;
        } catch (Exception e) {
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @RequestMapping(path = "/napasReturnWL", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    String returnUrlPurchaseOtp(@RequestBody String request) {
        return "nothing";
    }

    @PostMapping(path = "/napasReturnUrlWL", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    String returnUrlPurchaseOtp2(@RequestBody String request) {
        logger.info("napasReturnWL");
        try {
            String pgResponse = napasWLService.returnUrlPurchaseOtpWL(request);
            return pgResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
