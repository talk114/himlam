package gateway.core.channel.napas.doi_soat;

import lombok.Data;

import java.io.Serializable;

@Data
public class TranxNglNapasInfo implements Serializable {

    private String card_number;
    private String amount;
    private String time_created;
    private String nl_trans_id;
    private String order_id;
    private String bank_trans_id;


//    @Override
//    public int hashCode() {
//        return super.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o != null && o instanceof TranxNglNapasInfo) {
//            TranxNglNapasInfo tranxNglNapasInfo = (TranxNglNapasInfo) o;
//            String createTimeNapas = tranxNglNapasInfo.getTime_created().length() > 4 ? convertDateTimeToPattern("MMdd", tranxNglNapasInfo.getTime_created()) : tranxNglNapasInfo.getTime_created();
//            String createTimeNL = this.time_created.length() > 4 ? convertDateTimeToPattern("MMdd", this.time_created) : this.time_created;
//            return //tranxNglNapasInfo.getMerchantID().equals(this.merchantID) &&
//                    Long.parseLong(tranxNglNapasInfo.getAmount()) == Long.parseLong(this.amount)
//                            && tranxNglNapasInfo.getBank_trans_id().equals(this.bank_trans_id) && createTimeNapas.equals(createTimeNL);
//        }
//        return false;
//    }
//
//    private String convertDateTimeToPattern(String pattern, String time) {
//        Date d = new Date(Long.parseLong(time) * 1000);
//        DateFormat f = new SimpleDateFormat(pattern);
//        return f.format(d);
//    }
}
