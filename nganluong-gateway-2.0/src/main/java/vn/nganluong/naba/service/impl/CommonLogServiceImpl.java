package vn.nganluong.naba.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.nganluong.naba.entities.PgFunction;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.PgFunctionService;

import java.text.MessageFormat;

@Service
@Transactional
public class CommonLogServiceImpl implements CommonLogService {

    private static final String CHARACTER_CONNECT = " - ";
    private static final String MSG_START_FUNCTION = " function Start";
    private static final String MSG_END_FUNCTION = " function End";
    @Autowired
    private PgFunctionService pgFunctionService;

    /**
     * Ghi log kèm transaction ID
     *
     * @param transId
     * @param content
     */
    @Override
    public void logInfoWithTransId(Logger logger, String transId, String content) {
        ThreadContext.put("trans_id", transId);
        logger.info(content);
        ThreadContext.clearAll();
    }

    /**
     * Create content log.
     *
     * @param channelCode     Mã channel
     * @param serviceName     Tên service
     * @param functionCode    function code muốn ghi log (Match với dữ liệu trong
     *                        bảng pg_function.code)
     * @param isSuccessResult Ghi log trong trạng thái thành công/thất bại
     * @param isRequest       Ghi log cho phần request
     * @param isResponse      Ghi log cho phần response (Chỉ true 1 trong 2 params
     *                        isRequest hoặc isResponse)
     * @param params          Các biến truyền vào cho nội dung message trong các cột
     *                        tương ứng pg_function.log_msg_request_error,
     *                        log_msg_request_success, log_msg_response_success,
     *                        log_msg_response_error
     * @return Nội dung content log
     */
    @Override
    public String createContentLog(String channelCode, String serviceName, String functionCode, boolean isSuccessResult,
                                   boolean isRequest, boolean isResponse, String[] params) {
        try {
            StringBuilder builderContentLog = new StringBuilder();
            if (StringUtils.isNotBlank(channelCode)) {
                builderContentLog.append(channelCode).append(CHARACTER_CONNECT);
            }
            if (StringUtils.isNotBlank(serviceName)) {
                builderContentLog.append(serviceName).append(CHARACTER_CONNECT);
            }
            if (StringUtils.isNotBlank(functionCode)) {
                builderContentLog.append(functionCode).append(CHARACTER_CONNECT);
            }

            PgFunction pgFunction = pgFunctionService.findByCodeAndChannelCode(functionCode, channelCode);
            String pattern = StringUtils.EMPTY;
            if (isSuccessResult) {
                if (isRequest) {
                    pattern = pgFunction.getLogMsgPatternRequestSuccess();
                } else if (isResponse) {
                    pattern = pgFunction.getLogMsgPatternResponseSuccess();
                }
            } else {
                if (isRequest) {
                    pattern = pgFunction.getLogMsgPatternRequestError();
                } else if (isResponse) {
                    pattern = pgFunction.getLogMsgPatternResponseError();
                }
            }

            if (params.length > 0) {
                builderContentLog.append(MessageFormat.format(pattern, params));
            }
            return builderContentLog.toString();

        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * Create content log start hoặc end function
     *
     * @param channelCode     Mã channel
     * @param serviceName     Tên service
     * @param functionCode    function code muốn ghi log (Match với dữ liệu trong
     *                        bảng pg_function.code)
     * @param isStartFunction true thì Start, false thì End function
     * @return Nội dung content log
     */
    @Override
    public String createContentLogStartEndFunction(String channelCode, String serviceName, String functionCode,
                                                   boolean isStartFunction) {
        try {
            StringBuilder builderContentLog = new StringBuilder();
            if (StringUtils.isNotBlank(channelCode)) {
                builderContentLog.append(channelCode).append(CHARACTER_CONNECT);
            }
            if (StringUtils.isNotBlank(serviceName)) {
                builderContentLog.append(serviceName).append(CHARACTER_CONNECT);
            }
            if (StringUtils.isNotBlank(functionCode)) {
                builderContentLog.append(functionCode).append(CHARACTER_CONNECT);
            }

            PgFunction pgFunction = pgFunctionService.findByCodeAndChannelCode(functionCode, channelCode);

            if (pgFunction != null) {
                builderContentLog.append(pgFunction.getName());

            }

            if (isStartFunction) {
                builderContentLog.append(MSG_START_FUNCTION);
            } else {
                builderContentLog.append(MSG_END_FUNCTION);
            }

            return builderContentLog.toString();

        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }
}