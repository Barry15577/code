package io.java.modules.oss.cloud;


import io.java.common.validator.group.AliyunGroup;
import io.java.common.validator.group.QcloudGroup;
import io.java.common.validator.group.QiniuGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Cloud storage configuration information
 *
 * @author Jiasi Lu
 */
@Data
public class CloudStorageConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @Range(min=1, max=3, message = "Type Error")
    private Integer type;


    @NotBlank(message="Domain name cannot be empty", groups = QiniuGroup.class)
    @URL(message = "The domain name format is incorrect", groups = QiniuGroup.class)
    private String qiniuDomain;

    private String qiniuPrefix;

    @NotBlank(message="AccessKey cannot be empty", groups = QiniuGroup.class)
    private String qiniuAccessKey;

    @NotBlank(message="SecretKey cannot be empty", groups = QiniuGroup.class)
    private String qiniuSecretKey;

    @NotBlank(message="The space name cannot be empty", groups = QiniuGroup.class)
    private String qiniuBucketName;


    @NotBlank(message="The domain name bound to Alibaba Cloud cannot be empty", groups = AliyunGroup.class)
    @URL(message = "The domain name format bound to Alibaba Cloud is incorrect", groups = AliyunGroup.class)
    private String aliyunDomain;

    private String aliyunPrefix;

    @NotBlank(message="Alibaba Cloud EndPoint cannot be empty", groups = AliyunGroup.class)
    private String aliyunEndPoint;

    @NotBlank(message="Alibaba Cloud AccessKeyId cannot be empty", groups = AliyunGroup.class)
    private String aliyunAccessKeyId;
    //AccessKeySecret
    @NotBlank(message="Alibaba Cloud AccessKeySecret cannot be empty", groups = AliyunGroup.class)
    private String aliyunAccessKeySecret;
    //BucketName
    @NotBlank(message="Alibaba Cloud BucketName cannot be empty", groups = AliyunGroup.class)
    private String aliyunBucketName;


    @NotBlank(message="The domain name bound to Tencent Cloud cannot be empty", groups = QcloudGroup.class)
    @URL(message = "The domain name format bound to Tencent Cloud is incorrect", groups = QcloudGroup.class)
    private String qcloudDomain;

    private String qcloudPrefix;
    //AppId
    @NotNull(message="Tencent Cloud AppId cannot be empty", groups = QcloudGroup.class)
    private Integer qcloudAppId;
    //SecretId
    @NotBlank(message="Tencent Cloud SecretId cannot be empty", groups = QcloudGroup.class)
    private String qcloudSecretId;
    //SecretKey
    @NotBlank(message="Tencent Cloud SecretKey cannot be empty", groups = QcloudGroup.class)
    private String qcloudSecretKey;
    //BucketName
    @NotBlank(message="Tencent Cloud BucketName cannot be empty", groups = QcloudGroup.class)
    private String qcloudBucketName;
    //COS
    @NotBlank(message="The region cannot be empty", groups = QcloudGroup.class)
    private String qcloudRegion;


}
