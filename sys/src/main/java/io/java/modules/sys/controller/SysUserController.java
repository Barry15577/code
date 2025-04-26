package io.java.modules.sys.controller;

import io.java.common.annotation.SysLog;
import io.java.common.utils.Constant;
import io.java.common.utils.PageUtils;
import io.java.common.utils.R;
import io.java.common.validator.Assert;
import io.java.common.validator.ValidatorUtils;
import io.java.common.validator.group.AddGroup;
import io.java.common.validator.group.UpdateGroup;
import io.java.modules.sys.entity.SysUserEntity;
import io.java.modules.sys.form.PasswordForm;
import io.java.modules.sys.service.SysUserRoleService;
import io.java.modules.sys.service.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * System users
 *
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;


	/**
	 * List of all users
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam Map<String, Object> params){
		//Only super administrators can view the list of all administrators
		if(getUserId() != Constant.SUPER_ADMIN){
			params.put("createUserId", getUserId());
		}
		PageUtils page = sysUserService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	/**
	 * Get logged in user information
	 */
	@GetMapping("/info")
	public R info(){
		return R.ok().put("user", getUser());
	}
	
	/**
	 * Edit login user password
	 */
	@SysLog("Edit Password")
	@PostMapping("/password")
	public R password(@RequestBody PasswordForm form){
		Assert.isBlank(form.getNewPassword(), "New password cannot be empty");
		

		String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();

		String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();

		//Update password
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return R.error("The original password is incorrect");
		}
		
		return R.ok();
	}
	
	/**
	 * User information
	 */
	@GetMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.getById(userId);

//Get the list of roles to which the user belongs
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return R.ok().put("user", user);
	}
	
	/**
	 * Save user
	 */
	@SysLog("Save User")
	@PostMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		
		user.setCreateUserId(getUserId());
		sysUserService.saveUser(user);
		
		return R.ok();
	}
	
	/**
	 * Edit User
	 */
	@SysLog("Edit User")
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		user.setCreateUserId(getUserId());
		sysUserService.update(user);
		
		return R.ok();
	}
	
	/**
	 * Delete user
	 */
	@SysLog("Delete user")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("System administrators cannot delete");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("The current user cannot delete");
		}
		
		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}
}
