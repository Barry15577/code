

package io.java.modules.oss.cloud;

import io.java.common.utils.DateUtils;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Jiasi Lu
 */
public abstract class CloudStorageService {
    /** Cloud storage configuration information */
    CloudStorageConfig config;

    /**
     * File path
     * @param prefix prefix
     * @param suffix suffix
     * @return return upload path
     */
    public String getPath(String prefix, String suffix) {
//Generate uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //File path
        String path = DateUtils.format(new Date(), "yyyyMMdd") + "/" + uuid;

        if(StringUtils.isNotBlank(prefix)){
            path = prefix + "/" + path;
        }

        return path + suffix;
    }
    /**
     * File upload
     * @param data file byte array
     * @param path file path, including file name
     * @return return http address
     */
    public abstract String upload(byte[] data, String path);

    /**
     * File upload
     * @param data file byte array
     * @param suffix suffix
     * @return return http address
     */
    public abstract String uploadSuffix(byte[] data, String suffix);

    /**
     * File upload
     * @param inputStream byte stream
     * @param path file path, including file name
     * @return return http address
     */
    public abstract String upload(InputStream inputStream, String path);

    /**
     * File upload
     * @param inputStream byte stream
     * @param suffix suffix
     * @return return http address
     */
    public abstract String uploadSuffix(InputStream inputStream, String suffix);

}
