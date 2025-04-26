

package io.java.common.validator;

import io.java.common.exception.RRException;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jiasi Lu
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new RRException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RRException(message);
        }
    }
}
