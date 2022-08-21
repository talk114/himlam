package vn.nganluong.naba.service;

import org.apache.logging.log4j.Logger;

public interface CommonLogService {

	/**
	 * Ghi log kèm transaction ID
	 * @param transId
	 * @param content
	 */
	public void logInfoWithTransId(Logger logger, String transId, String content);

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
	public String createContentLog(String channelCode, String serviceName, String functionCode, boolean isSuccessResult,
			boolean isRequest, boolean isResponse, String[] params);

	/**
	 * Create content log start hoặc end function
	 * @param channelCode     Mã channel
	 * @param serviceName     Tên service
	 * @param functionCode    function code muốn ghi log (Match với dữ liệu trong
	 *                        bảng pg_function.code)
	 * @param isStartFunction true thì Start, false thì End function
	 * @return Nội dung content log
	 */
	public String createContentLogStartEndFunction(String channelCode, String serviceName, String functionCode, boolean isStartFunction);
}