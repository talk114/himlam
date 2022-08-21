package gateway.core.channel.napas.doi_soat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class DoiSoatRequest implements Serializable {

    @JsonProperty(value = "data")
    private List<DoiSoatDetails> data;

    public List<DoiSoatDetails> getData() {
        return data;
    }

    public void setData(List<DoiSoatDetails> data) {
        this.data = data;
    }
}
