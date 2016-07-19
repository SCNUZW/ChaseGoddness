package zy.chasegoddness.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检查格式是否合法的功能
 */
public class FormatCheckModel {
    /**
     * 判断是否是手机号码<br>
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188<br>
     * 联通：130、131、132、152、155、156、185、186<br>
     * 电信：133、153、180、189、（1349卫通）
     */
    public static boolean isPhoneNumber(String phone) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 判断邮编
     * 6位数字 第一位不为0
     */
    public static boolean isZipNO(String zipString) {
        String str = "^[1-9][0-9]{5}$";
        return Pattern.compile(str).matcher(zipString).matches();
    }

    /**
     * 判断邮箱是否合法
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
}