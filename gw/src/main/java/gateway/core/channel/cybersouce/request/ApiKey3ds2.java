package gateway.core.channel.cybersouce.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiKey3ds2 implements Serializable {
    private String apiKeySecretBytes;
    private String API_IDENTIFIER;
    private String ORG_UNIT;
}
