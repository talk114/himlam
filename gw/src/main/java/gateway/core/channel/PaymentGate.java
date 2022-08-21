package gateway.core.channel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.util.PGBeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public abstract class PaymentGate {

    protected static ObjectMapper objectMapper;
    protected static DecimalFormat df;

    protected static PGBeanUtils pGBeanUtils;

    private static final Logger logger = LogManager.getLogger(PaymentGate.class);

    protected static final String DEFAULT_VALUE = "";
    protected static final String COMMENT_LOG_BEGIN = "############################ ";
    protected static final String COMMENT_LOG_END = " ############################";

    static {
        df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);

        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        pGBeanUtils = new PGBeanUtils();
    }

    protected static void WriteInfoLog(String input) {
        logger.info(COMMENT_LOG_BEGIN + input + COMMENT_LOG_END);
    }

    protected void WriteInfoLog(String header, String body) {
        logger.info(COMMENT_LOG_BEGIN + header + COMMENT_LOG_END + body);
    }

    protected void WriteErrorLog(String input) {
        logger.error(COMMENT_LOG_BEGIN + input + COMMENT_LOG_END);
    }

    protected void WriteWarnLog(String input) {
        logger.warn(COMMENT_LOG_BEGIN + input + COMMENT_LOG_END);
    }

}
