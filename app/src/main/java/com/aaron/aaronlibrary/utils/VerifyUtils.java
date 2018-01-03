package com.aaron.aaronlibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 各种验证工具
 *
 * @author Aaron
 */
public class VerifyUtils {

    /**
     * 手机验证
     */
    public static boolean isPhone(String phone) {
        Pattern p = Pattern.compile("^1[0-9]{10}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

}
