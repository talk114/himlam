package gateway.core.channel.napas.dto.obj;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Provided2 {
    private CardInfo3 card;

    public CardInfo3 getCard() {
        return card;
    }

    public void setCard(CardInfo3 card) {
        this.card = card;
    }
}
