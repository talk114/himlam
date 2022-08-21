package gateway.core.channel.migs.controller;

import gateway.core.channel.migs.service.MIGSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.nganluong.naba.service.CommonPGResponseService;

@RestController
@RequestMapping(value = "/restful/api")
public class MigsController {
    @Autowired
    MIGSService migsService;
    @Autowired
    CommonPGResponseService commonPGResponseService;

    @PostMapping(path = "/migs/pares", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    String result(@RequestBody String request) {
        try {
            String pgResponse = migsService.resultCheckEnroll(request);
            return pgResponse;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
