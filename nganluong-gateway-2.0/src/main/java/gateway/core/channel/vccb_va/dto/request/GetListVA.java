package gateway.core.channel.vccb_va.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListVA {
    private String partnerCode;
    private int page;
    private int size;
}
