/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway.core.channel.cybersouce.util;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Zon
 */
public class CybsUtil {

    public String viewCardNumber(String card_number) {
        String result = card_number.substring(card_number.length() - 4, card_number.length());
        if (card_number.length() < 10) {
            return card_number;
        }
        return "XXXX..." + result;
    }

    public static String encryptCardNumber(String cardnumber) {
        if (cardnumber == null) {
            cardnumber = "";
        }
        return "XXXX..." + cardnumber.substring(cardnumber.length() - 4, cardnumber.length());
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (null != ip && !"".equals(ip.trim())
                && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (null != ip && !"".equals(ip.trim())
                && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
    
    public static String signedPARes(String s){
        return s.replace("\n", "").replace("\r","").replace("\t", "").replace(" ","").replace("\\o","").replace("\\xOB", "");
    }
    
    public static String subStringData(String s){
        String replace = s.replaceAll("c:", "");
        String[] data = replace.split("/");
        return data[data.length-1];
    }
}
