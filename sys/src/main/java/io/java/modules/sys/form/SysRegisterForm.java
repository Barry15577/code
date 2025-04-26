

package io.java.modules.sys.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Registration form
 */
@Data
public class SysRegisterForm {

    @NotBlank(message="Username cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,20}$", message = "Username must be 4-20 letters, numbers, underscores or hyphens")
    private String username;

    @NotBlank(message="Password cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{6,20}$", message = "Password must be 6-20 letters, numbers, underscores or hyphens")
    private String password;

    @NotBlank(message="Email cannot be empty")
    @Email(message="Email format is incorrect")
    private String email;

    @NotBlank(message="Verification code cannot be empty")
    private String captcha;

    @NotBlank(message="UUID cannot be empty")
    private String uuid;

    private String role = "user"; // defaults to ordinary user
} 