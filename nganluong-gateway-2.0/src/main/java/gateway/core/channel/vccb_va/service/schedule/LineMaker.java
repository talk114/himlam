package gateway.core.channel.vccb_va.service.schedule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineMaker {
    private String refNo;
    private String date;
    private String partnerCode;
    private String relatedAccount;
    private String creditAccount;
    private String amount;
    private String currency;
    private String xxxxx;
    private String receipt;

    public LineMaker(String line){
        String[] fields = line.split("\\|");
        refNo = fields[0];
        date = fields[1];
        partnerCode = fields[2];
        relatedAccount = fields[3];
        creditAccount = fields[4];
        amount = fields[5];
        currency = fields[6];
        xxxxx = fields[7];
        Pattern pattern = Pattern.compile("(NL........)");
        Matcher matcher = pattern.matcher(fields[8]);
        String cashinId2 = matcher.find() ? matcher.group(1) : "";
        receipt = cashinId2;
    }

    @Override
    public String toString() {

        return new StringBuilder()
                .append(refNo).append(",")
                .append("'").append(date).append(",")
                .append(partnerCode).append(",")
                .append("'").append(relatedAccount).append(",")
                .append("'").append(creditAccount).append(",")
                .append("'").append(amount).append(",")
                .append(currency).append(",")
                .append(xxxxx).append(",")
                .append(receipt).append("\n")
                .toString();
    }
}
