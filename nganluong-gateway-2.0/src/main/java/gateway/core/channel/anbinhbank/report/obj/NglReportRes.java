package gateway.core.channel.anbinhbank.report.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NglReportRes {

	@JsonProperty("data")
	private List<TrxNglAbbInfo> reportLst;

	public List<TrxNglAbbInfo> getReportLst() {
		return reportLst;
	}

	public void setReportLst(List<TrxNglAbbInfo> reportLst) {
		this.reportLst = reportLst;
	}

}
