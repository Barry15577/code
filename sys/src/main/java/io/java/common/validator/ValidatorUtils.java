package io.java.common.validator;

import io.java.common.exception.RRException;
import io.java.common.utils.Constant;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 *
 * @author Jiasi Lu
 */
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * @param object        Object to be verified
     * @param groups        Group to be verified
     * @throws RRException  If the verification fails, an RRException will be reported.
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws RRException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                msg.append(constraint.getMessage()).append("<br>");
            }
            throw new RRException(msg.toString());
        }
    }

    public static void validateEntity(Object object, Constant.CloudService type) {
        validateEntity(object, type.getValidatorGroupClass());
    }
}
