package io.java.modules.oss.controller;

import com.google.gson.Gson;
import io.java.common.exception.RRException;
import io.java.common.utils.ConfigConstant;
import io.java.common.utils.Constant;
import io.java.common.utils.PageUtils;
import io.java.common.utils.R;
import io.java.common.validator.ValidatorUtils;
import io.java.modules.oss.cloud.CloudStorageConfig;
import io.java.modules.oss.cloud.OSSFactory;
import io.java.modules.oss.entity.SysOssEntity;
import io.java.modules.oss.service.SysOssService;
import io.java.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("sys/oss")
public class SysOssController {
	@Autowired
	private SysOssService sysOssService;
    @Autowired
    private SysConfigService sysConfigService;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;
	
	/**
	 * List
	 */
	@GetMapping("/list")
	//@RequiresPermissions("sys:oss:all")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysOssService.queryPage(params);

		return R.ok().put("page", page);
	}


    /**
	 * Cloud storage configuration information
     */
    @GetMapping("/config")
    //@RequiresPermissions("sys:oss:all")
    public R config(){
        CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

        return R.ok().put("config", config);
    }


	/**
	 * Save cloud storage configuration information
	 */
	@PostMapping("/saveConfig")
	//@RequiresPermissions("sys:oss:all")
	public R saveConfig(@RequestBody CloudStorageConfig config){
//Check type
		ValidatorUtils.validateEntity(config);
		ValidatorUtils.validateEntity(config, Constant.CloudService.getByValue(config.getType()));

        sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));

		return R.ok();
	}
	

	/**
	 * Upload files
	 */
	@PostMapping("/upload")
	//@RequiresPermissions("sys:oss:all")
	public R upload(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("Upload file cannot be empty");
		}

		//Upload file
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);

        //Save file information
		SysOssEntity ossEntity = new SysOssEntity();
		ossEntity.setUrl(url);
		ossEntity.setCreateDate(new Date());
		sysOssService.save(ossEntity);

		return R.ok().put("url", url);
	}


	/**
	 * Delete
	 */
	@PostMapping("/delete")
	//@RequiresPermissions("sys:oss:all")
	public R delete(@RequestBody Long[] ids){
		sysOssService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}

}
