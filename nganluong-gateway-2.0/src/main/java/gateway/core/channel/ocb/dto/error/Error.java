package gateway.core.channel.ocb.dto.error;

import lombok.Data;

@Data
public class Error {
    private String type;
    private String code;
    private String details;
    private String location;
}
