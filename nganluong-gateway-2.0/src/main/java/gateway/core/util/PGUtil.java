package gateway.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.dto.request.NotifyReq;
import gateway.core.dto.request.SendEmailReq;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PGUtil {
    private static final Logger logger = LogManager.getLogger(PGUtil.class);

    protected static final String DEFAULT_VALUE = "";
    protected static final String COMMENT_LOG_BEGIN = "############################ ";
    protected static final String COMMENT_LOG_END = " ############################";
    public static final String CHARACTER = "    |    ";

    private PGUtil(){}
    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    public static boolean isNumberic(String str){
        return StringUtils.isNumeric(str);
    }

    public static String removeDiacritical(String str) {
        if (str == null) {
            return str;
        }
        str = str.replaceAll("(à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ)", "a");
        str = str.replaceAll("(è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ)", "e");
        str = str.replaceAll("(ì|í|ị|ỉ|ĩ)", "i");
        str = str.replaceAll("(ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ)", "o");
        str = str.replaceAll("(ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ)", "u");
        str = str.replaceAll("(ỳ|ý|ỵ|ỷ|ỹ)", "y");
        str = str.replaceAll("(đ)", "d");
        str = str.replaceAll("(À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ)", "A");
        str = str.replaceAll("(È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ)", "E");
        str = str.replaceAll("(Ì|Í|Ị|Ỉ|Ĩ)", "I");
        str = str.replaceAll("(Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ)", "O");
        str = str.replaceAll("(Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ)", "U");
        str = str.replaceAll("(Ỳ|Ý|Ỵ|Ỷ|Ỹ)", "Y");
        str = str.replaceAll("(Đ)", "D");
        return str;
    }

    public static void sendEmail(String content) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            NotifyReq req = new NotifyReq();
            req.setFnc("sendEmail");

            SendEmailReq email = new SendEmailReq();
            email.setFromEmail("support@nganluong.vn");
            email.setSubject(content + "\t" + df.format(new Date()));
            email.setContent(content + "\t" + df.format(new Date()));
            email.setToEmail("vinhnt@peacesoft.net");
            email.setAlias("GATEWAY");
            req.setData(email);

            String url = "http://10.0.0.26:8080/EmailSmsGW?fnc=" + req.getFnc() + "&data=";
            ObjectMapper mapper = new ObjectMapper();
            String pathApi = url + URLEncoder.encode(mapper.writeValueAsString(req.getData()), "UTF-8") ;
            System.out.println("PathApi: " + pathApi.replace("\"",	"\'"));
            System.out.println(HttpUtil.sendGet(pathApi));
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
    }

    //Format Date
    public static String formatDateTime(String partern, long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(partern);
        return simpleDateFormat.format(new Date(time));
    }

    public static void WriteInfoLog(String header, String body) {
        logger.info(COMMENT_LOG_BEGIN + header + COMMENT_LOG_END + "\n" + body);
    }

    //convert Object to Map
    public static Map<String, Object> objToMapParameters(Object obj) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(obj) != null) {
                    map.put(field.getName(), field.get(obj));
                }
            } catch (Exception e) {
            }
        }
        return map;
    }

    //sort map by keys and convert value to string
    public static String sortMap(Map<String, Object> res) {
        Map<String, Object> result = res.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        String filter = result.entrySet().stream()
                .map(map -> map.getValue()).map(String::valueOf).collect(Collectors.joining());
        return filter;
    }
}
