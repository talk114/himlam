package gateway.core.channel.migs.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class  SourceOfFunds implements Serializable {
    private Provided provided;
    private String type;

    public Provided getProvided() {
        return provided;
    }

    public void setProvided(Provided provided) {
        this.provided = provided;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
