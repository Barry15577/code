package io.java.common.utils;

import io.java.common.validator.group.AliyunGroup;
import io.java.common.validator.group.QcloudGroup;
import io.java.common.validator.group.QiniuGroup;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * constant
 *
 * @author Jiasi Lu
 */
public class Constant {
    /**
     * Super Administrator ID
     */
    public static final int SUPER_ADMIN = 1;
    /**
     * Current page number
     */
    public static final String PAGE = "page";
    /**
     * Number of records displayed per page
     */
    public static final String LIMIT = "limit";
    /**
     * Sort Field
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * sort by
     */
    public static final String ORDER = "order";
    /**
     * Ascending
     */
    public static final String ASC = "asc";

    /**
     * Menu Type
     *
     * @author Jiasi Lu
     *
     */
    public enum MenuType {
        /**
         * Table of contents
         */
        CATALOG(0),
        /**
         * Menu
         */
        MENU(1),
        /**
         * BUTTON
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * ScheduleStatus
     *
     * @author Jiasi Lu
     */
    public enum ScheduleStatus {

        NORMAL(0),

        PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public enum CloudService {

        QINIU(1, QiniuGroup.class),

        ALIYUN(2, AliyunGroup.class),

        QCLOUD(3, QcloudGroup.class);

        private int value;

        private Class<?> validatorGroupClass;

        CloudService(int value, Class<?> validatorGroupClass) {
            this.value = value;
            this.validatorGroupClass = validatorGroupClass;
        }

        public int getValue() {
            return value;
        }

        public Class<?> getValidatorGroupClass() {
            return this.validatorGroupClass;
        }

        public static CloudService getByValue(Integer value) {
            Optional<CloudService> first = Stream.of(CloudService.values()).filter(cs -> value.equals(cs.value)).findFirst();
            if (!first.isPresent()) {
                throw new IllegalArgumentException("Illegal enumeration value:" + value);
            }
            return first.get();
        }
    }

}
