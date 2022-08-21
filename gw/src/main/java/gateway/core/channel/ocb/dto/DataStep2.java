package gateway.core.channel.ocb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataStep2 {
    private String otpCode;
    private String bankRefNo;
    private String partnerCode;
}
