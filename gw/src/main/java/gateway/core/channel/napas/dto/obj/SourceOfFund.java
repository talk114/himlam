package gateway.core.channel.napas.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SourceOfFund extends SourceOfFundBase{

	private Provided provided;

	public Provided getProvided() {
		return provided;
	}

	public void setProvided(Provided provided) {
		this.provided = provided;
	}

}
