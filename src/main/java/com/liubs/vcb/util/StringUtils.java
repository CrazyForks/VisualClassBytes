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
        if (string != null && !string.isEmpty()) {
            for(int i = 0,len=string.length(); i < len; ++i) {
                if(i == 0 && len>1 && string.codePointAt(i)=='-') {
                    continue;
                }
                if (!Character.isDigit(string.codePointAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static String escape(String str)  {
        if(null == str || str.isEmpty()) {
            return str;
        }
        StringBuilder strBuilder = new StringBuilder();

        for(int i = 0,len=str.length(); i < len; ++i) {
            char ch = str.charAt(i);
            switch(ch) {
                case '\"':
                    strBuilder.append("\\\"");
                    break;
                case '\'':
                    strBuilder.append("'");
                    break;
                case '\\':
                    strBuilder.append("\\\\");
                    break;
                case '\b':
                    strBuilder.append("\\b");
                    break;
                case '\f':
                    strBuilder.append("\\f");
                    break;
                case '\n':
                    strBuilder.append("\\n");
                    break;
                case '\r':
                    strBuilder.append("\\r");
                    break;
                case '\t':
                    strBuilder.append("\\t");
                    break;
                default:
                    strBuilder.append(ch);
                    break;

            }
        }
        return strBuilder.toString();
    }

    public static String unescape(String str) {
        if(null == str || str.isEmpty()) {
            return str;
        }
        StringBuilder strBuilder = new StringBuilder();
        boolean hadSlash = false;

        for(int i = 0,len=str.length(); i < len; ++i) {
            char ch = str.charAt(i);
            if (hadSlash) {
                hadSlash = false;
                switch(ch) {
                    case '\"':
                        strBuilder.append('\"');
                        break;
                    case '\'':
                        strBuilder.append('\'');
                        break;
                    case '\\':
                        strBuilder.append('\\');
                        break;
                    case 'b':
                        strBuilder.append('\b');
                        break;
                    case 'f':
                        strBuilder.append('\f');
                        break;
                    case 'n':
                        strBuilder.append('\n');
                        break;
                    case 'r':
                        strBuilder.append('\r');
                        break;
                    case 't':
                        strBuilder.append('\t');
                        break;
                    default:
                        strBuilder.append(ch);
                }
            } else if (ch == '\\') {
                hadSlash = true;
            } else {
                strBuilder.append(ch);
            }
        }

        if (hadSlash) {
            strBuilder.append('\\');
        }

        return strBuilder.toString();
    }
}
