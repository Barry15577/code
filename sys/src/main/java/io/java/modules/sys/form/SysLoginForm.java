

package io.java.modules.sys.form;

import lombok.Data;

/**
 * Login form
 *
 * @author Jiasi Lu
 */
@Data
public class SysLoginForm {
    private String username;
    private String password;
    private String captcha;
    private String uuid;


}
