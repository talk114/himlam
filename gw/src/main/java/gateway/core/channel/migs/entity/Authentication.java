package gateway.core.channel.migs.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Authentication {
    private String acceptVersions;
    private String channel;
    private String purpose ;
    private String redirectResponseUrl;
    private String transactionId;

    }