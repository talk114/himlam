package gateway.core.channel.bidv.bidv_transfer_247.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import gateway.core.channel.bidv.bidv_transfer_247.object247.InfoDS;
import gateway.core.channel.bidv.bidv_transfer_247.object247.TransInfoDS;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DSResponse {

    private List<TransInfoDS> transInfoDS;
    private InfoDS infoDS;

    public DSResponse() {
    }

    public DSResponse(List<TransInfoDS> transInfoDS, InfoDS infoDS) {
        this.transInfoDS = transInfoDS;
        this.infoDS = infoDS;
    }

    public List<TransInfoDS> getTransInfoDS() {
        return transInfoDS;
    }

    public void setTransInfoDS(List<TransInfoDS> transInfoDS) {
        this.transInfoDS = transInfoDS;
    }

    public InfoDS getInfoDS() {
        return infoDS;
    }

    public void setInfoDS(InfoDS infoDS) {
        this.infoDS = infoDS;
    }
}
