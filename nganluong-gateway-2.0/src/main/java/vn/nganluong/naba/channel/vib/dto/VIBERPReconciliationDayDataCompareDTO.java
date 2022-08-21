package vn.nganluong.naba.channel.vib.dto;

import java.math.BigDecimal;
import java.util.Date;

import vn.nganluong.naba.dto.ReconciliationDayDataCompareDTO;

public class VIBERPReconciliationDayDataCompareDTO extends ReconciliationDayDataCompareDTO {

	private String napasResult;

	public VIBERPReconciliationDayDataCompareDTO() {
		super();
	}

	public VIBERPReconciliationDayDataCompareDTO(String toAcct, BigDecimal tranAmt, String description,
			String clientTranId, Date tranTime) {
		super();
	}

	public String getNapasResult() {
		return napasResult;
	}

	public void setNapasResult(String napasResult) {
		this.napasResult = napasResult;
	}

}
