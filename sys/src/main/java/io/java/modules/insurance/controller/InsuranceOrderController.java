package io.java.modules.insurance.controller;

import io.java.common.annotation.SysLog;
import io.java.common.utils.PageUtils;
import io.java.common.utils.R;
import io.java.common.validator.ValidatorUtils;
import io.java.modules.insurance.entity.InsuranceOrderEntity;
import io.java.modules.insurance.service.InsuranceOrderService;
import io.java.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Insurance Order Controller
 * Handles requests related to insurance product orders
 *
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("/insurance/order")
public class InsuranceOrderController extends AbstractController {
    @Autowired
    private InsuranceOrderService insuranceOrderService;

    /**
     * List insurance orders with pagination and filters
     */
    @GetMapping("/list")
    @RequiresPermissions("insurance:order:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = insuranceOrderService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * Get order information by ID
     */
    @GetMapping("/info/{orderId}")
    @RequiresPermissions("insurance:order:info")
    public R info(@PathVariable("orderId") Long orderId) {
        InsuranceOrderEntity order = insuranceOrderService.getOrderById(orderId);
        return R.ok().put("order", order);
    }

    /**
     * Create a new insurance order
     */
    @PostMapping("/save")
    @SysLog("Create new insurance order")
    public R save(@RequestBody InsuranceOrderEntity order) {
        ValidatorUtils.validateEntity(order);
        
        insuranceOrderService.saveOrder(order);
        
        return R.ok();
    }

    /**
     * Update insurance order
     */
    @PutMapping("/update")
    @RequiresPermissions("insurance:order:update")
    @SysLog("Update insurance order")
    public R update(@RequestBody InsuranceOrderEntity order) {
        ValidatorUtils.validateEntity(order);
        
        insuranceOrderService.updateOrder(order);
        
        return R.ok();
    }

    /**
     * Cancel an order
     */
    @PostMapping("/cancel/{orderId}")
    @RequiresPermissions("insurance:order:update")
    @SysLog("Cancel insurance order")
    public R cancel(@PathVariable("orderId") Long orderId) {
        insuranceOrderService.cancelOrder(orderId);
        
        return R.ok();
    }

    /**
     * Confirm an order
     */
    @PostMapping("/confirm/{orderId}")
    @RequiresPermissions("insurance:order:update")
    @SysLog("Confirm insurance order")
    public R confirm(@PathVariable("orderId") Long orderId) {
        insuranceOrderService.confirmOrder(orderId, getUser().getUsername());
        
        return R.ok();
    }

    /**
     * Complete an order
     */
    @PostMapping("/complete/{orderId}")
    @RequiresPermissions("insurance:order:update")
    @SysLog("Complete insurance order")
    public R complete(@PathVariable("orderId") Long orderId) {
        insuranceOrderService.completeOrder(orderId, getUser().getUsername());
        
        return R.ok();
    }
} 