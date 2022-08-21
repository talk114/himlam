package gateway.core.channel.napas.doi_soat;

import java.io.Serializable;

public class DataForDoiSoat implements Serializable {

    private String header;
    private String details;
    private String trailer;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }
}
