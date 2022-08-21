package gateway.core.channel.viettelpay;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class VTTPayUtil {

	public static String convertFormData2Json(String formData) throws UnsupportedEncodingException {
		String params[] = URLDecoder.decode(formData, "UTF-8").split("&");
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < params.length; i++) {
			String arr2[] = params[i].split("=", 2);
			map.put(arr2[0], arr2[1]);
		}
		JSONObject obj = new JSONObject(map);
		return obj.toString();
	}
}
