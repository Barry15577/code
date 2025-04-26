package io.java.modules.autoparts.controller;

import io.java.common.utils.PageUtils;
import io.java.common.utils.R;
import io.java.modules.autoparts.entity.InventoryMovementEntity;
import io.java.modules.autoparts.service.AutoPartService;
import io.java.modules.autoparts.service.InventoryMovementService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("/autoparts/movement")
public class InventoryMovementController {
    @Autowired
    private InventoryMovementService inventoryMovementService;

    @Autowired
    private AutoPartService autoPartService;

    /**
     * List inventory movements with pagination and filters
     */
    @GetMapping("/list")
    @RequiresPermissions("autoparts:movement:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = inventoryMovementService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * Get inventory movement details by ID
     */
    @GetMapping("/info/{movementId}")
    @RequiresPermissions("autoparts:movement:info")
    public R info(@PathVariable("movementId") Long movementId) {
        InventoryMovementEntity movement = inventoryMovementService.getMovementById(movementId);

        return R.ok().put("movement", movement);
    }

    /**
     * Get inventory movement history for a specific part
     */
    @GetMapping("/history/{partId}")
    @RequiresPermissions("autoparts:movement:list")
    public R getMovementHistory(@PathVariable("partId") Long partId, @RequestParam Map<String, Object> params) {
        PageUtils page = inventoryMovementService.getMovementHistoryByPartId(partId, params);

        return R.ok().put("page", page);
    }

    /**
     * Get recent movements across all parts
     */
    @GetMapping("/recent")
    @RequiresPermissions("autoparts:movement:list")
    public R getRecentMovements(@RequestParam(defaultValue = "10") int limit) {
        List<InventoryMovementEntity> movements = inventoryMovementService.getRecentMovements(limit);

        return R.ok().put("movements", movements);
    }

    /**
     * Get summary of movement types for reporting
     */
    @GetMapping("/summary")
    @RequiresPermissions("autoparts:movement:list")
    public R getMovementTypeSummary(@RequestParam Map<String, Object> params) {
        Map<String, Object> summary = inventoryMovementService.getMovementTypeSummary(params);

        return R.ok().put("summary", summary);
    }

    /**
     * Save a new inventory movement
     * This endpoint is used for direct movement operations
     */
    @PostMapping("/save")
    //@RequiresPermissions("autoparts:movement:save")
    public R save(@RequestBody Map<String, Object> params) {
        try {
            // Extract required parameters
            Long partId = Long.parseLong(params.get("partId").toString());
            Integer quantityChanged = Integer.parseInt(params.get("quantityChanged").toString());
            String movementType = (String) params.get("movementType");
            String reference = (String) params.get("reference");
            String notes = (String) params.get("notes");

            // Get current username
            String username = SecurityUtils.getSubject().getPrincipal().toString();

            // Use the AutoPartService to update stock and create movement record
            autoPartService.updateStock(partId, quantityChanged, movementType, reference, notes, username);

            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * Process batch inventory movements
     */
    @PostMapping("/batch")
    //@RequiresPermissions("autoparts:movement:save")
    public R batchSave(@RequestBody List<Map<String, Object>> movementsList) {
        try {
            // Get current username
            String username = SecurityUtils.getSubject().getPrincipal().toString();

            // Process each movement request
            for (Map<String, Object> params : movementsList) {
                Long partId = Long.parseLong(params.get("partId").toString());
                Integer quantityChanged = Integer.parseInt(params.get("quantityChanged").toString());
                String movementType = (String) params.get("movementType");
                String reference = (String) params.get("reference");
                String notes = (String) params.get("notes");

                autoPartService.updateStock(partId, quantityChanged, movementType, reference, notes, username);
            }

            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}
