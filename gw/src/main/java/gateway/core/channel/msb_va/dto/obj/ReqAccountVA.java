package gateway.core.channel.msb_va.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author sonln
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ReqAccountVA {
    private String accountNumber;
    private String referenceNumber;
    private String name;
    private String payType;
    private String maxAmount;
    private String minAmount;
    private String equalAmount;
    private String detail1;
    private String detail2;
    private String detail3;
    private String email;
    private String phone;
    private String expiryDate;
    private String regNumber;
    private String status;
    {
        regNumber = "0106001236";
    }

    public String checkNullOrEmpty() {
        String invalidField = "";
        try {
            if (StringUtils.isEmpty(this.accountNumber))
                invalidField = ReqAccountVA.class.getDeclaredField("accountNumber").getName();
            if (StringUtils.isEmpty(this.referenceNumber))
                invalidField = ReqAccountVA.class.getDeclaredField("referenceNumber").getName();
            if (StringUtils.isEmpty(this.name))
                invalidField = ReqAccountVA.class.getDeclaredField("name").getName();
            if (StringUtils.isEmpty(this.payType))
                invalidField = ReqAccountVA.class.getDeclaredField("payType").getName();
//            if (StringUtils.isEmpty(this.maxAmount))
//                invalidField = ReqAccountVA.class.getDeclaredField("maxAmount").getName();
//            if (StringUtils.isEmpty(this.minAmount))
//                invalidField = ReqAccountVA.class.getDeclaredField("minAmount").getName();
//            if (StringUtils.isEmpty(this.equalAmount))
//                invalidField = ReqAccountVA.class.getDeclaredField("equalAmount").getName();
            if (StringUtils.isEmpty(this.phone))
                invalidField = ReqAccountVA.class.getDeclaredField("phone").getName();
//            if (StringUtils.isEmpty(this.regNumber))
//                invalidField = ReqAccountVA.class.getDeclaredField("regNumber").getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invalidField;
    }
}
