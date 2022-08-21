package gateway.reconciliation.controller;

import gateway.core.channel.napas.service.schedule.WLScheduleService;
import gateway.core.channel.vccb_va.service.schedule.VCCBSchedule;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import gateway.core.channel.vib.service.VIBSchedule;


/**
 *
 *
 * {@code ReconciliationController}.
 * Api này sinh ra để chủ động test luồng đối soát
 * đồng thời chủ động chạy lại đối soát trong trường
 * hợp phía đối tác đẩy tay file đối soát sau thời
 * gian cronjob chạy
 *
 * @author dungla@nganluong.vn
 */

@RestController
@RequestMapping(value="/channel/reconciliation")
public class ApiReconciliationController {

//    private final VIBSchedule vibSchedule;
    private final WLScheduleService wlScheduleService;
    private final VCCBSchedule vccbSchedule;

    @Autowired
    public ApiReconciliationController(
                                        WLScheduleService wlScheduleService,
                                        VCCBSchedule vccbSchedule){
        this.wlScheduleService = wlScheduleService;
        this.vccbSchedule = vccbSchedule;
    }
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> callReconciliation(@RequestBody String data){
        JSONObject jsonObject = new JSONObject(data);
        String channel = jsonObject.getString("channel");
        String date = jsonObject.getString("date");
        switch (channel){
            case "NAPASWL":
                boolean[] rt = wlScheduleService.callRec(date);
                return new ResponseEntity(HttpStatus.OK);
            case "VIB":
                break;
            case "VCCB":
//                vccbSchedule.callRec();
                break;
        }
//        vibSchedule.vibSchedule();
        System.out.println("Chào mừng");
        return null;
    }
}
