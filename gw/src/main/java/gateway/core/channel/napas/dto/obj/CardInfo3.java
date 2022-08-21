package gateway.core.channel.napas.dto.obj;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardInfo3 {
    private String brand;
    private Expiry expiry;
    private String issuer;
    private String number;
    private String scheme;
    private boolean status3ds;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Expiry getExpiry() {
        return expiry;
    }

    public void setExpiry(Expiry expiry) {
        this.expiry = expiry;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public boolean isStatus3ds() {
        return status3ds;
    }

    public void setStatus3ds(boolean status3ds) {
        this.status3ds = status3ds;
    }

    static class Expiry {
        private String month;
        private String year;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }
}
