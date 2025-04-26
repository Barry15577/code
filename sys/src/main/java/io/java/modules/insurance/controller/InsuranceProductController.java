package io.java.modules.insurance.controller;

import io.java.common.annotation.SysLog;
import io.java.common.utils.PageUtils;
import io.java.common.utils.R;
import io.java.common.validator.ValidatorUtils;
import io.java.common.validator.group.AddGroup;
import io.java.common.validator.group.UpdateGroup;
import io.java.modules.insurance.entity.InsuranceProductEntity;
import io.java.modules.insurance.service.InsuranceProductService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * Insurance Product Controller
 * Handles all HTTP requests related to insurance product management
 *
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("/insurance/product")
public class InsuranceProductController {
    @Autowired
    private InsuranceProductService insuranceProductService;

    /**
     * List insurance products with pagination and optional filters
     */
    @GetMapping("/list")
    @RequiresPermissions("insurance:product:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = insuranceProductService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * Get insurance product information by ID
     */
    @GetMapping("/info/{productId}")
    @RequiresPermissions("insurance:product:info")
    public R info(@PathVariable("productId") Long productId){
        InsuranceProductEntity product = insuranceProductService.getProductById(productId);

        return R.ok().put("product", product);
    }

    /**
     * Check if product code exists
     */
    @GetMapping("/check-code/{productCode}")
    @RequiresPermissions("insurance:product:list")
    public R checkProductCode(@PathVariable("productCode") String productCode, @RequestParam(required = false) Long excludeId){
        boolean exists = insuranceProductService.checkProductCodeExists(productCode, excludeId);

        return R.ok().put("exists", exists);
    }

    /**
     * Save new insurance product
     */
    @SysLog("Add new insurance product")
    @PostMapping("/save")
    @RequiresPermissions("insurance:product:save")
    public R save(@RequestBody InsuranceProductEntity product){
        // Validate request data
        ValidatorUtils.validateEntity(product, AddGroup.class);
        
        // Check if product code already exists
        if (insuranceProductService.checkProductCodeExists(product.getProductCode(), null)) {
            return R.error("Product code already exists");
        }

        insuranceProductService.saveProduct(product);

        return R.ok();
    }

    /**
     * Update existing insurance product
     */
    @SysLog("Update insurance product information")
    @PutMapping("/update")
    @RequiresPermissions("insurance:product:update")
    public R update(@RequestBody InsuranceProductEntity product){
        // Validate request data
        ValidatorUtils.validateEntity(product, UpdateGroup.class);
        
        // Check if product code already exists (excluding current product)
        if (insuranceProductService.checkProductCodeExists(product.getProductCode(), product.getProductId())) {
            return R.error("Product code already exists");
        }
        
        insuranceProductService.updateProduct(product);
        
        return R.ok();
    }

    /**
     * Delete insurance product by ID
     */
    @SysLog("Delete insurance product")
    @DeleteMapping("/delete/{productId}")
    @RequiresPermissions("insurance:product:delete")
    public R delete(@PathVariable("productId") Long productId){
        insuranceProductService.deleteProduct(productId);

        return R.ok();
    }
    
    /**
     * Batch delete insurance products by IDs
     */
    @SysLog("Batch delete insurance products")
    @DeleteMapping("/delete")
    @RequiresPermissions("insurance:product:delete")
    public R deleteBatch(@RequestBody Long[] productIds){
        insuranceProductService.removeByIds(Arrays.asList(productIds));

        return R.ok();
    }
} 