package org.hwj;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 *
 * @description
 * @author: huangwenjun
 * @create: 2019-05-14 14:47
 **/
public class RegularUtil {

    public static String extractByStartAndEnd(String str, String startStr, String endStr) {
        String regEx = startStr + ".*?"+endStr;
        String group = findMatchString(str, regEx);
        String trim = group.replace(startStr, "").replace(endStr, "").trim();
        return trim(trim);
    }

    public static String findMatchString(String str, String regEx) {
        try {
            // 编译正则表达式
            Pattern pattern = Pattern.compile(regEx);
            // 忽略大小写的写法
            Matcher matcher = pattern.matcher(str);
            // 字符串是否与正则表达式相匹配
            return findFristGroup(matcher);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String findFristGroup(Matcher matcher) {
        matcher.find();
        return matcher.group(0);
    }

    /**
     * 去除字符串中所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     * @param s
     * @return
     */
    public static String removeAllBlank(String s){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("[　*| *| *|//s*]*", "");
        }
        return result;
    }

    /**
     * 去除字符串中头部和尾部所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     * @param s
     * @return
     */
    public static String trim(String s){
        String result = "";
        if(null!=s && !"".equals(s)){
            result = s.replaceAll("^[　*| *| *|//s*]*", "").replaceAll("[　*| *| *|//s*]*$", "");
        }
        return result;
    }
}
