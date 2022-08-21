package gateway.core.channel.anbinhbank.report.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VimoReportRes {

	@JsonProperty("data")
	private List<TrxVimoAbbInfo> reportLst;

	public List<TrxVimoAbbInfo> getReportLst() {
		return reportLst;
	}

	public void setReportLst(List<TrxVimoAbbInfo> reportLst) {
		this.reportLst = reportLst;
	}

	
}
