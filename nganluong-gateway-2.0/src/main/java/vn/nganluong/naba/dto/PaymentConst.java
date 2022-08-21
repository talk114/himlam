package vn.nganluong.naba.dto;

import org.apache.commons.lang3.StringUtils;

public class PaymentConst {
	public enum EnumPaymentType {
		ACCOUNT_NO("ACCOUNT_NO"), VIRTUAL_ACCOUNT_NO("VIRTUAL_ACCOUNT_NO");

		private String typeName;

		EnumPaymentType(String typeName) {
			this.typeName = typeName;
		}

		public String typeName() {
			return typeName;
		}

		public int code() {
			switch (this) {
			case ACCOUNT_NO:
				return 1;
			case VIRTUAL_ACCOUNT_NO:
				return 2;
			default:
				return 0;
			}
		}
	}

	public enum EnumPaymentRevertStatus {
		NOT("NOT"), REVERTED("REVERTED");

		private String status;

		EnumPaymentRevertStatus(String status) {
			this.status = status;
		}

		public String status() {
			return status;
		}

		public int code() {
			switch (this) {
			case NOT:
				return 0;
			case REVERTED:
				return 1;
			default:
				return 0;
			}
		}
	}

	public enum EnumBankStatus {
		FAILED("FAILED"), SUCCEEDED("SUCCEEDED"), COMPLETED("COMPLETED"), CANCELED("CANCELED"), REVERTED("REVERTED"), PENDING(null),
		EMPTY(StringUtils.EMPTY);
	
		private String status;
	
		EnumBankStatus(String status) {
			this.status = status;
		}
	
		public String status() {
			return status;
		}
	
		public Integer code() {
			switch (this) {
			case SUCCEEDED:
				return 1;
			case COMPLETED:
				return 2;
			case CANCELED:
				return 3;
			case FAILED:
				return 4;
			case REVERTED:
				return 5;
			default:
				return 0;
			}
		}
	
		public String statusByCode(Integer codeInput) {
	
			String status = "PENDING";
			if (EnumBankStatus.SUCCEEDED.code().equals(codeInput)) {
				status = EnumBankStatus.SUCCEEDED.status();
			} else if (EnumBankStatus.COMPLETED.code().equals(codeInput)) {
				status = EnumBankStatus.COMPLETED.status();
			} else if (EnumBankStatus.CANCELED.code().equals(codeInput)) {
				status = EnumBankStatus.CANCELED.status();
			} else if (EnumBankStatus.FAILED.code().equals(codeInput)) {
				status = EnumBankStatus.FAILED.status();
			} else if (EnumBankStatus.REVERTED.code().equals(codeInput)) {
				status = EnumBankStatus.REVERTED.status();
			}
			return status;
	
		}

		public enum EnumCardType {
			DEBIT("DEBIT"), CREDIT("CREDIT");

			private String typeCard;

			EnumCardType(String typeCard) {
				this.typeCard = typeCard;
			}

			public String typeCard() {
				return typeCard;
			}

			public int code() {
				switch (this) {
					case CREDIT:
						return 1;
					case DEBIT:
						return 2;
					default:
						return 0;
				}
			}
		}
	}

	public static final String DATE_FORMAT_PAYMENT_SEARCH = "yyyy-MM-dd HH:mm:ss";
	
	public static final String MONTH_FORMAT_DISPAY = "MM-yyyy";

	public static final String SYSTEM_NL_NAME = "NGAN LUONG";
}
