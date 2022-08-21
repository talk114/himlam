package gateway.core.channel.napas.doi_soat;

import java.io.Serializable;
import java.util.List;

public class FileGDSuccessNapas implements Serializable {

    private Header header;
    private List<DoiSoatDetails> details;
    private Trailer trailer;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<DoiSoatDetails> getDetails() {
        return details;
    }

    public void setDetails(List<DoiSoatDetails> details) {
        this.details = details;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }
}
