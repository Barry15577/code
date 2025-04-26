

package io.java.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.modules.sys.entity.SysCaptchaEntity;

import java.awt.image.BufferedImage;

/**
 * Verification code
 *
 * @author Jiasi Lu
 */
public interface SysCaptchaService extends IService<SysCaptchaEntity> {

    /**
     * Get image verification code
     */
    BufferedImage getCaptcha(String uuid);

    /**
     * Verification code validation
     * @param uuid uuid
     * @param code verification code
     * @return true: success false: failure
     */
    boolean validate(String uuid, String code);
}
