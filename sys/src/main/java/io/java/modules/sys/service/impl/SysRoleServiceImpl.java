package io.java.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.java.common.exception.RRException;
import io.java.common.utils.Constant;
import io.java.common.utils.PageUtils;
import io.java.common.utils.Query;
import io.java.modules.sys.dao.SysRoleDao;
import io.java.modules.sys.dao.SysUserDao;
import io.java.modules.sys.entity.SysRoleEntity;
import io.java.modules.sys.service.SysRoleMenuService;
import io.java.modules.sys.service.SysRoleService;
import io.java.modules.sys.service.SysUserRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Role
 *
 * @author Jiasi Lu
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleService sysUserRoleService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String)params.get("roleName");
		Long createUserId = (Long)params.get("createUserId");

		IPage<SysRoleEntity> page = this.page(
			new Query<SysRoleEntity>().getPage(params),
			new QueryWrapper<SysRoleEntity>()
				.like(StringUtils.isNotBlank(roleName),"role_name", roleName)
				.eq(createUserId != null,"create_user_id", createUserId)
		);

		return new PageUtils(page);
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRoleEntity role) {
        role.setCreateTime(new Date());
        this.save(role);

		//Check whether the permission is exceeded
		checkPrems(role);

        //Save the relationship between role and menu
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleEntity role) {
        this.updateById(role);

		// Check if the permission is exceeded
		checkPrems(role);

        // Update the relationship between role and menu
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
		 //Delete role
		this.removeByIds(Arrays.asList(roleIds));

         //Delete role and menu association
		sysRoleMenuService.deleteBatch(roleIds);

         //Delete role and user association
		sysUserRoleService.deleteBatch(roleIds);
    }


    @Override
	public List<Long> queryRoleIdList(Long createUserId) {
		return baseMapper.queryRoleIdList(createUserId);
	}

	/**
	 * Check if the permission is exceeded
	 */
	private void checkPrems(SysRoleEntity role){
        //If not a super administrator, you need to determine whether the role's permissions exceed your own permissions
		if(role.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}

        //Query the menu list owned by the user
		List<Long> menuIdList = sysUserDao.queryAllMenuId(role.getCreateUserId());

        //Judge whether it is exceeded
		if(!menuIdList.containsAll(role.getMenuIdList())){
			throw new RRException("Add role's permissions exceed your permissions");
		}
	}
}