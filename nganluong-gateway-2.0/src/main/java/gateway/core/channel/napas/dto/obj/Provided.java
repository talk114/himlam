package gateway.core.channel.napas.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Provided {
	private CardInfo2 card;

	public CardInfo2 getCard() {
		return card;
	}

	public void setCard(CardInfo2 card) {
		this.card = card;
	}

}
