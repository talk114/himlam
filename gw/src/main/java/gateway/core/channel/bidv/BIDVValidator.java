package gateway.core.channel.bidv;

import gateway.core.dto.request.DataRequest;
import org.apache.commons.lang3.StringUtils;

import gateway.core.util.PGValidator;

public class BIDVValidator extends PGValidator {

    public static String validateLinkCardParam(DataRequest param) {
        StringBuilder errors = new StringBuilder();
        if (StringUtils.isEmpty(param.getUserId())) {
            errors.append("UserId is required;");
        }
        if (StringUtils.isEmpty(param.getTransId())) {
            errors.append("TransId is required;");
        }
        if (StringUtils.isEmpty(param.getTransTime())) {
            errors.append("TransTime is required (format: YYMMDD);");
        }
        if (StringUtils.isEmpty(param.getChannel())) {
            errors.append("Channel is required (Mobile,Web,SMS);");
        }
        if (StringUtils.isEmpty(param.getLinkType())) {
            errors.append("LinkType is required (Default: 1);");
        }

        return errors.toString();
    }

    public static String validateUnLinkCardParam(DataRequest param) {
        StringBuilder errors = new StringBuilder();
        if (StringUtils.isEmpty(param.getUserId())) {
            errors.append("UserId is required;");
        }
        if (StringUtils.isEmpty(param.getTransId())) {
            errors.append("TransId is required;");
        }
        if (StringUtils.isEmpty(param.getTransTime())) {
            errors.append("TransTime is required (format: YYMMDD);");
        }
        if (StringUtils.isEmpty(param.getChannel())) {
            errors.append("Channel is required (Mobile,Web,SMS);");
        }
        if (StringUtils.isEmpty(param.getLinkType())) {
            errors.append("LinkType is required (Default: 1);");
        }
        if (StringUtils.isEmpty(param.getMoreInfo())) {
            errors.append("More is required (0: User unlink, 1: Provider unlink);");
        }

        return errors.toString();
    }

    public static String validateCheckLinkCardParam(DataRequest param) {
        StringBuilder errors = new StringBuilder();
        if (StringUtils.isEmpty(param.getUserId())) {
            errors.append("UserId is required;");
        }
        if (StringUtils.isEmpty(param.getTransId())) {
            errors.append("TransId is required;");
        }
        if (StringUtils.isEmpty(param.getTransTime())) {
            errors.append("TransTime is required (format: YYMMDD);");
        }
        if (StringUtils.isEmpty(param.getChannel())) {
            errors.append("Channel is required (Mobile,Web,SMS);");
        }
        if (StringUtils.isEmpty(param.getLinkType())) {
            errors.append("LinkType is required (Default: 1);");
        }

        return errors.toString();
    }

    public static String validateTransferBankAccParam(DataRequest param) {
        StringBuilder errors = new StringBuilder();
        if (StringUtils.isEmpty(param.getUserId())) {
            errors.append("UserId is required;");
        }
        if (StringUtils.isEmpty(param.getTransId())) {
            errors.append("TransId is required;");
        }
        if (param.getAmount() == null || param.getAmount() <= 0.0) {
            errors.append("Amount invalid;");
        }
        if (StringUtils.isEmpty(param.getTransTime())) {
            errors.append("TransTime is required (format: YYMMDD);");
        }
        if (StringUtils.isEmpty(param.getChannel())) {
            errors.append("Channel is required (Mobile,Web,SMS);");
        }

        return errors.toString();
    }

    public static String validatePaymentParam(DataRequest param) {
        StringBuilder errors = new StringBuilder();
        if (StringUtils.isEmpty(param.getUserId())) {
            errors.append("UserId is required;");
        }
        if (StringUtils.isEmpty(param.getTransId())) {
            errors.append("TransId is required;");
        }
        if (param.getAmount() == null || param.getAmount() <= 0.0) {
            errors.append("Amount invalid;");
        }
        if (StringUtils.isEmpty(param.getTransTime())) {
            errors.append("TransTime is required (format: YYMMDD);");
        }
        if (StringUtils.isEmpty(param.getChannel())) {
            errors.append("Channel is required (Mobile,Web,SMS);");
        }

        return errors.toString();
    }

    public static String validateVerifyOTPParam(DataRequest param) {
        StringBuilder errors = new StringBuilder();
        if (StringUtils.isEmpty(param.getUserId())) {
            errors.append("UserId is required;");
        }
        if (StringUtils.isEmpty(param.getVerifyTransId())) {
            errors.append("Verify TransId is required;");
        }
        if (param.getAmount() == null || param.getAmount() <= 0.0) {
            errors.append("Amount invalid;");
        }
        if (StringUtils.isEmpty(param.getOtp())) {
            errors.append("Otp is required;");
        }
        if (StringUtils.isEmpty(param.getTransTime())) {
            errors.append("TransTime is required (format: YYMMDD);");
        }
        if (StringUtils.isEmpty(param.getChannel())) {
            errors.append("Channel is required (Mobile,Web,SMS);");
        }

        return errors.toString();
    }

    public static String validateQueryParam(DataRequest param) {
        StringBuilder errors = new StringBuilder();
        if (StringUtils.isEmpty(param.getTransId())) {
            errors.append("TransId is required;");
        }
        if (StringUtils.isEmpty(param.getTransTime())) {
            errors.append("TransTime is required (format: YYMMDD);");
        }

        return errors.toString();
    }
}
