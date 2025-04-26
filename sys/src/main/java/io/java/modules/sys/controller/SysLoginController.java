package io.java.modules.sys.controller;

import io.java.common.utils.R;
import io.java.modules.sys.entity.SysUserEntity;
import io.java.modules.sys.form.SysLoginForm;
import io.java.modules.sys.form.SysRegisterForm;
import io.java.modules.sys.service.SysCaptchaService;
import io.java.modules.sys.service.SysUserRoleService;
import io.java.modules.sys.service.SysUserService;
import io.java.modules.sys.service.SysUserTokenService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Login related
 *
 * @author Jiasi Lu
 */
@RestController
public class SysLoginController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysCaptchaService sysCaptchaService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * Verification code
	 */
	@GetMapping("captcha.jpg")
	public void captcha(HttpServletResponse response, String uuid)throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//Get the image verification code
		BufferedImage image = sysCaptchaService.getCaptcha(uuid);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * Log in
	 */
	@PostMapping("/sys/login")
	public Map<String, Object> login(@RequestBody SysLoginForm form)throws IOException {
		boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
		if(!captcha){
			return R.error("Incorrect verification code");
		}

		//User information
		SysUserEntity user = sysUserService.queryByUserName(form.getUsername());

		//Account does not exist, password is wrong
		if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
			return R.error("Incorrect account or password");
		}

		//Account locked
		if(user.getStatus() == 0){
			return R.error("The account has been locked, please contact the administrator");
		}

		//Generate a token and save it to the database
		R r = sysUserTokenService.createToken(user.getUserId());
		return r;
	}

	/**
	 * register
	 */
	@PostMapping("/sys/register")
	public R register(@RequestBody @Valid SysRegisterForm form) {
		// 验证码校验
		boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
		if(!captcha){
			return R.error("Incorrect verification code");
		}

		// Check if the username already exists
		SysUserEntity existUser = sysUserService.queryByUserName(form.getUsername());
		if(existUser != null) {
			return R.error("This user name already exists");
		}

		// Create a user
		SysUserEntity user = new SysUserEntity();
		user.setUsername(form.getUsername());
		user.setEmail(form.getEmail());
		user.setStatus(1); // 正常状态
		user.setCreateTime(new Date());

		// Generate salt
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setSalt(salt);

		// Encrypted password
		String password = new Sha256Hash(form.getPassword(), salt).toHex();
		user.setPassword(password);

		// Save the user
		sysUserService.save(user);

		// Assign roles
		Long userId = user.getUserId();
		List<Long> roleIdList = new ArrayList<>();

		// Assign roles based on the role field in the registration form
        // Here we assume that role ID 2 is a normal user role
		if("user".equals(form.getRole())) {
			roleIdList.add(2L); // Common user role ID
		}

		//Save the relationship between user and role
		sysUserRoleService.saveOrUpdate(userId, roleIdList);
		
		return R.ok();
	}

	/**
	 * logout
	 */
	@PostMapping("/sys/logout")
	public R logout() {
		sysUserTokenService.logout(getUserId());
		return R.ok();
	}
	
}
