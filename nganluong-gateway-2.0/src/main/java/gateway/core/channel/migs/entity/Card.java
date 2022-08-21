package gateway.core.channel.migs.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Card {
    private String number;
    private String scheme;
    private Expiry expiry;
    private String securityCode;
}
