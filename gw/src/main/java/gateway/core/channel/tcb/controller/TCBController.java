package gateway.core.channel.tcb.controller;

import gateway.core.channel.anbinhbank.report.BaseReport;
import gateway.core.channel.tcb.dto.TCBConstants;
import gateway.core.channel.tcb.service.TCBService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.nganluong.naba.entities.Channel;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PgFunction;
import vn.nganluong.naba.service.ChannelFunctionService;
import vn.nganluong.naba.service.ChannelService;
import vn.nganluong.naba.service.PgFunctionService;

/**
 * @author sonln
 */
@RestController
@RequestMapping(value = "/restful/api")
public class TCBController {
    @Autowired
    private TCBService tcbService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelFunctionService channelFunctionService;
    @Autowired
    private PgFunctionService pgFunctionService;

    private static final Logger logger = LogManager.getLogger(TCBController.class);


    @RequestMapping(value = "/TCB/UpdateTransactionStatus", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    String updateTransStatus(@RequestBody String req) {
        try {
            logger.info("https://gateway05.nganluong.vn/gateway/restful/api/TCB/UpdateTransactionStatus");
            logger.info(req);
            PgFunction pgFunction = pgFunctionService.findByCodeAndChannelCode(TCBConstants.UPDATE_TRANSACTION_STATUS, "TCB");
            Channel channel = channelService.findById(pgFunction.getChannelId());
            String fnc = pgFunction.getCode();
            ChannelFunction channelFunction = channelFunctionService.findChannelFunctionByCodeAndChannelId(fnc, channel.getId());
            return tcbService.updateTransStatus(req, channelFunction);
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            return e.getMessage();
        }
    }

}
