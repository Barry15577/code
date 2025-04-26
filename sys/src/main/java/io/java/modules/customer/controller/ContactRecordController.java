package io.java.modules.customer.controller;

import io.java.common.annotation.SysLog;
import io.java.common.utils.PageUtils;
import io.java.common.utils.R;
import io.java.common.validator.ValidatorUtils;
import io.java.common.validator.group.AddGroup;
import io.java.common.validator.group.UpdateGroup;
import io.java.modules.customer.entity.ContactRecordEntity;
import io.java.modules.customer.service.ContactRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * Contact Record Controller
 * Handles all HTTP requests related to customer contact record management
 *
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("/customer/contact")
public class ContactRecordController {
    @Autowired
    private ContactRecordService contactRecordService;

    /**
     * List all contact records with pagination and optional filters
     */
    @GetMapping("/list")
    @RequiresPermissions("customer:contact:list")
    public R list(@RequestParam Map<String, Object> params){
        // Check if requesting contacts for a specific customer
        if (params.containsKey("customerId")) {
            PageUtils page = contactRecordService.queryCustomerContacts(params);
            return R.ok().put("page", page);
        } else {
            PageUtils page = contactRecordService.queryPage(params);
            return R.ok().put("page", page);
        }
    }

    /**
     * Get contact record information by ID
     */
    @GetMapping("/info/{contactId}")
    @RequiresPermissions("customer:contact:info")
    public R info(@PathVariable("contactId") Long contactId){
        ContactRecordEntity contactRecord = contactRecordService.getContactRecordById(contactId);

        return R.ok().put("contactRecord", contactRecord);
    }

    /**
     * Save new contact record
     */
    @SysLog("Add new contact record")
    @PostMapping("/save")
    @RequiresPermissions("customer:contact:save")
    public R save(@RequestBody ContactRecordEntity contactRecord){
        // Validate request data
        ValidatorUtils.validateEntity(contactRecord, AddGroup.class);

        contactRecordService.saveContactRecord(contactRecord);

        return R.ok();
    }

    /**
     * Update existing contact record
     */
    @SysLog("Update contact record")
    @PutMapping("/update")
    @RequiresPermissions("customer:contact:update")
    public R update(@RequestBody ContactRecordEntity contactRecord){
        // Validate request data
        ValidatorUtils.validateEntity(contactRecord, UpdateGroup.class);
        
        contactRecordService.updateContactRecord(contactRecord);
        
        return R.ok();
    }

    /**
     * Delete contact record by ID
     */
    @SysLog("Delete contact record")
    @DeleteMapping("/delete/{contactId}")
    @RequiresPermissions("customer:contact:delete")
    public R delete(@PathVariable("contactId") Long contactId){
        contactRecordService.deleteContactRecord(contactId);

        return R.ok();
    }
    
    /**
     * Batch delete contact records by IDs
     */
    @SysLog("Batch delete contact records")
    @DeleteMapping("/delete")
    @RequiresPermissions("customer:contact:delete")
    public R deleteBatch(@RequestBody Long[] contactIds){
        contactRecordService.removeByIds(Arrays.asList(contactIds));

        return R.ok();
    }
} 