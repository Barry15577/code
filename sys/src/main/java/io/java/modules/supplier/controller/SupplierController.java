package io.java.modules.supplier.controller;

import io.java.common.annotation.SysLog;
import io.java.common.utils.PageUtils;
import io.java.common.utils.R;
import io.java.common.validator.ValidatorUtils;
import io.java.common.validator.group.AddGroup;
import io.java.common.validator.group.UpdateGroup;
import io.java.modules.supplier.entity.SupplierEntity;
import io.java.modules.supplier.service.SupplierService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Supplier Controller
 * Handles HTTP requests for supplier management
 *
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("/supplier/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;
    
    /**
     * List suppliers with pagination and filters
     */
    @GetMapping("/list")
    @RequiresPermissions("supplier:supplier:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = supplierService.queryPage(params);
        
        return R.ok().put("page", page);
    }
    
    /**
     * List all active suppliers without pagination
     * Used for dropdown selection
     */
    @GetMapping("/list/all")
    public R listAll() {
        List<SupplierEntity> supplierList = supplierService.listAllActiveSuppliers();
        
        return R.ok().put("list", supplierList);
    }
    
    /**
     * Get supplier information by ID
     */
    @GetMapping("/info/{supplierId}")
    @RequiresPermissions("supplier:supplier:info")
    public R info(@PathVariable("supplierId") Long supplierId) {
        SupplierEntity supplier = supplierService.getSupplierById(supplierId);
        
        return R.ok().put("supplier", supplier);
    }
    
    /**
     * Save a new supplier
     */
    @SysLog("Save Supplier")
    @PostMapping("/save")
    @RequiresPermissions("supplier:supplier:save")
    public R save(@RequestBody SupplierEntity supplier) {
        // Validate supplier data
        ValidatorUtils.validateEntity(supplier, AddGroup.class);
        
        // Check if supplier code already exists
        if (supplierService.checkSupplierCodeExists(supplier.getSupplierCode(), null)) {
            return R.error("Supplier code already exists");
        }
        
        supplierService.saveSupplier(supplier);
        
        return R.ok();
    }
    
    /**
     * Update an existing supplier
     */
    @SysLog("Update Supplier")
    @PostMapping("/update")
    @RequiresPermissions("supplier:supplier:update")
    public R update(@RequestBody SupplierEntity supplier) {
        // Validate supplier data
        ValidatorUtils.validateEntity(supplier, UpdateGroup.class);
        
        // Check if supplier code already exists (excluding current supplier)
        if (supplierService.checkSupplierCodeExists(supplier.getSupplierCode(), supplier.getSupplierId())) {
            return R.error("Supplier code already exists");
        }
        
        supplierService.updateSupplier(supplier);
        
        return R.ok();
    }
    
    /**
     * Delete one or more suppliers
     */
    @SysLog("Delete Supplier")
    @PostMapping("/delete")
    @RequiresPermissions("supplier:supplier:delete")
    public R delete(@RequestBody Long[] supplierIds) {
        supplierService.removeByIds(Arrays.asList(supplierIds));
        
        return R.ok();
    }
    
    /**
     * Check if supplier code exists
     */
    @GetMapping("/check-code/{supplierCode}")
    public R checkSupplierCode(@PathVariable("supplierCode") String supplierCode, 
                              @RequestParam(required = false) Long excludeId) {
        boolean exists = supplierService.checkSupplierCodeExists(supplierCode, excludeId);
        
        return R.ok().put("exists", exists);
    }
} 