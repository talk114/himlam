package gateway.core.channel.napas.dto.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardInfo2 {

    private String brand;
    private String nameOnCard;
    private String issueDate;
    private String number;
    private String scheme;

    public CardInfo2() {
    }

    public CardInfo2(String brand, String nameOnCard, String issueDate, String number, String scheme) {
        this.brand = brand;
        this.nameOnCard = nameOnCard;
        this.issueDate = issueDate;
        this.number = number;
        this.scheme = scheme;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @JsonProperty("nameOnCard")
    public String getNameOnCard() {
        return nameOnCard;
    }

    @JsonProperty("nameOnCard")
    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    @JsonProperty("issueDate")
    public String getIssueDate() {
        return issueDate;
    }

    @JsonProperty("issueDate")
    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(String number) {
        this.number = number;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
