package io.java.modules.customer.controller;

import io.java.common.annotation.SysLog;
import io.java.common.utils.PageUtils;
import io.java.common.utils.R;
import io.java.common.validator.ValidatorUtils;
import io.java.common.validator.group.AddGroup;
import io.java.common.validator.group.UpdateGroup;
import io.java.modules.customer.entity.CustomerEntity;
import io.java.modules.customer.service.CustomerService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * Customer Controller
 * Handles all HTTP requests related to customer management
 *
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    /**
     * List customers with pagination and optional filters
     */
    @GetMapping("/list")
    @RequiresPermissions("customer:customer:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = customerService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * Get customer information by ID
     */
    @GetMapping("/info/{customerId}")
    @RequiresPermissions("customer:customer:info")
    public R info(@PathVariable("customerId") Long customerId){
        CustomerEntity customer = customerService.getCustomerById(customerId);

        return R.ok().put("customer", customer);
    }

    /**
     * Save new customer
     */
    @SysLog("Add new customer")
    @PostMapping("/save")
    @RequiresPermissions("customer:customer:save")
    public R save(@RequestBody CustomerEntity customer){
        // Validate request data
        ValidatorUtils.validateEntity(customer, AddGroup.class);

        customerService.saveCustomer(customer);

        return R.ok();
    }

    /**
     * Update existing customer
     */
    @SysLog("Update customer information")
    @PutMapping("/update")
    @RequiresPermissions("customer:customer:update")
    public R update(@RequestBody CustomerEntity customer){
        // Validate request data
        ValidatorUtils.validateEntity(customer, UpdateGroup.class);
        
        customerService.updateCustomer(customer);
        
        return R.ok();
    }

    /**
     * Delete customer(s) by ID(s)
     */
    @SysLog("Delete customer")
    @DeleteMapping("/delete/{customerId}")
    @RequiresPermissions("customer:customer:delete")
    public R delete(@PathVariable("customerId") Long customerId){
        customerService.deleteCustomer(customerId);

        return R.ok();
    }
    
    /**
     * Batch delete customers by IDs
     */
    @SysLog("Batch delete customers")
    @DeleteMapping("/delete")
    @RequiresPermissions("customer:customer:delete")
    public R deleteBatch(@RequestBody Long[] customerIds){
        customerService.removeByIds(Arrays.asList(customerIds));

        return R.ok();
    }
} 