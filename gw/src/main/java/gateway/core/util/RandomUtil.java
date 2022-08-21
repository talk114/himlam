package gateway.core.util;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {

    private static final byte[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes();
    private static final byte[] alphaOnly = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes();
    private static final byte[] digitOnly = "0123456789".getBytes();
    private static final byte[] uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes();
    private static final byte[] lowercase = "abcdefghijklmnopqrstuvwxyz".getBytes();
    private static final byte[] upAlphaDigit = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes();
    private static final byte[] lowAlphaDigit = "abcdefghijklmnopqrstuvwxyz0123456789".getBytes();

    private static String randomString(byte[] alphabet, int length, String prefix) {

        if (length <= 0) {
            return prefix;
        }

        byte[] chars = new byte[length];
        Random random = new Random();
        int alphabetSize = alphabet.length;
        for (int i = 0; i < length; ++i) {
            chars[i] = alphabet[random.nextInt(alphabetSize)];
        }

        if (prefix == null) {
            prefix = "";
        }

        return prefix + new String(chars);

    }

    public static String randomAlphaString(int length, String prefix) {
        return randomString(alphaOnly, length, prefix);
    }

    public static String randomDigitString(int length, String prefix) {
        return randomString(digitOnly, length, prefix);
    }

    public static String randomAlphaDigits(int length, String prefix) {
        return randomString(alphabet, length, prefix);
    }

    public static String randomUppercaseAlpha(int length, String prefix) {
        return randomString(uppercase, length, prefix);
    }

    public static String randomLowercaseAlpha(int length, String prefix) {
        return randomString(lowercase, length, prefix);
    }

    public static String randomUpAlphaDigits(int length, String prefix) {
        return randomString(upAlphaDigit, length, prefix);
    }

    public static String randomLowAlphaDigits(int length, String prefix) {
        return randomString(lowAlphaDigit, length, prefix);
    }

    public static String randomAlphaString(int length) {
        return randomString(alphaOnly, length, "");
    }

    public static String randomDigitString(int length) {
        return randomString(digitOnly, length, "");
    }

    public static String randomAlphaDigits(int length) {
        return randomString(alphabet, length, "");
    }

    public static String randomUppercaseAlpha(int length) {
        return randomString(uppercase, length, "");
    }

    public static String randomLowercaseAlpha(int length) {
        return randomString(lowercase, length, "");
    }

    public static String randomUpAlphaDigits(int length) {
        return randomString(upAlphaDigit, length, "");
    }

    public static String randomLowAlphaDigits(int length) {
        return randomString(lowAlphaDigit, length, "");
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static long randLong() {
        long longVal = Math.abs(new Random().nextLong());
        return longVal;
    }

    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public static String genIpClient(){
        Random r = new Random();
        return r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
    }
}
