package gateway.core.channel.evn_south.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name= "tem:GetCustomerFeeInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerInfoRequest {
    @XmlElement(name = "tem:BankID")
    @JsonProperty("bankID")
    private String bankID;

    @XmlElement(name = "tem:CustomerCode")
    @JsonProperty("customerCode")
    private String customerCode;

    public CustomerInfoRequest() {
    }

    public CustomerInfoRequest(String bankID, String customerCode) {
        this.bankID = bankID;
        this.customerCode = customerCode;
    }
}
