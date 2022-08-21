package gateway.core.channel.migs.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrowserDetails {
    @JsonProperty("3DSecureChallengeWindowSize")
    private String _3DSecureChallengeWindowSize;
    private String acceptHeaders;
    private String colorDepth;
    private boolean javaEnabled;
    private String language;
    private String screenHeight;
    private String screenWidth;
    private String timeZone;

    public String get_3DSecureChallengeWindowSize() {
        return _3DSecureChallengeWindowSize;
    }

    public void set_3DSecureChallengeWindowSize(String _3DSecureChallengeWindowSize) {
        this._3DSecureChallengeWindowSize = _3DSecureChallengeWindowSize;
    }

    public String getAcceptHeaders() {
        return acceptHeaders;
    }

    public void setAcceptHeaders(String acceptHeaders) {
        this.acceptHeaders = acceptHeaders;
    }

    public String getColorDepth() {
        return colorDepth;
    }

    public void setColorDepth(String colorDepth) {
        this.colorDepth = colorDepth;
    }

    public boolean isJavaEnabled() {
        return javaEnabled;
    }

    public void setJavaEnabled(boolean javaEnabled) {
        this.javaEnabled = javaEnabled;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(String screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(String screenWidth) {
        this.screenWidth = screenWidth;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
