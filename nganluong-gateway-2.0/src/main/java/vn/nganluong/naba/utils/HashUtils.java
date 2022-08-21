package vn.nganluong.naba.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtils {

	public static String hashingMD5(String dataToHash) {

		return DigestUtils.md5Hex(dataToHash).toUpperCase();

	}
}
