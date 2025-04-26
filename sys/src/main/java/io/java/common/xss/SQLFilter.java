package io.java.common.xss;

import io.java.common.exception.RRException;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jiasi Lu
 */
public class SQLFilter {

    /**
     * SQL injection filtering
     * @param str  The string to be verified
     */
    public static String sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }

        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //Convert to lowercase
        str = str.toLowerCase();

        //Illegal characters
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

        //Determine whether it contains illegal characters
        for(String keyword : keywords){
            if(str.indexOf(keyword) != -1){
                throw new RRException("Contains illegal characters");
            }
        }

        return str;
    }
}
