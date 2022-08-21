package gateway.core.channel.bidv_ecom.controller;

import gateway.core.api.controller.ApiPartnerController;
import gateway.core.channel.bidv_ecom.dto.BidvCallbackRes;
import gateway.core.channel.bidv_ecom.dto.BidvEcomConstants;
import gateway.core.channel.bidv_ecom.service.BIDVEcomService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sonln@nganluong.vn
 */
@RestController
public class Controller {
    @Autowired
    private BIDVEcomService bidvEcomService;
    private static final Logger logger = LogManager.getLogger(ApiPartnerController.class);

    @PostMapping(path = "bidv_ec_notify", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public BidvCallbackRes bidvCallback(@RequestBody String request) {
        try {
            return bidvEcomService.BidvNotify(request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(BidvEcomConstants.CHANNEL_CODE + " -cause :" + e.getMessage());
            return new BidvCallbackRes();
        }

    }
}
