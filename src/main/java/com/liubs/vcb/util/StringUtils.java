package com.liubs.vcb.util;

import java.util.UUID;

/**
 * @author Liubsyy
 * @date 2024/5/14
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return null == str || str.isEmpty();
    }
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }


    public static boolean isNumeric(String string) {
        if (string != null && string.length() != 0) {
            int l = string.length();

            for(int i = 0; i < l; ++i) {
                if (!Character.isDigit(string.codePointAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
