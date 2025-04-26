package io.java.modules.autoparts.controller;

import io.java.common.annotation.SysLog;
import io.java.common.utils.PageUtils;
import io.java.common.utils.R;
import io.java.common.validator.ValidatorUtils;
import io.java.common.validator.group.AddGroup;
import io.java.common.validator.group.UpdateGroup;
import io.java.modules.autoparts.entity.AutoPartEntity;
import io.java.modules.autoparts.service.AutoPartService;
import io.java.modules.autoparts.service.InventoryMovementService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("/autoparts/part")
public class AutoPartController {
    @Autowired
    private AutoPartService autoPartService;

    @Autowired
    private InventoryMovementService inventoryMovementService;

    /**
     * List auto parts with pagination and filters
     */
    @GetMapping("/list")
    @RequiresPermissions("autoparts:part:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = autoPartService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * Get auto part information by ID
     */
    @GetMapping("/info/{partId}")
    @RequiresPermissions("autoparts:part:info")
    public R info(@PathVariable("partId") String partIdStr) {
        AutoPartEntity part = null;

        try {
            // Try to parse as Long and get part
            Long partId = Long.parseLong(partIdStr);
            part = autoPartService.getPartById(partId);

            // If part not found and ID might be truncated (JavaScript precision issue)
            if (part == null && partIdStr.length() >= 15) {
                // Get first 15 digits to search for parts that start with this prefix
                String partIdPrefix = partIdStr.substring(0, 15);
                List<AutoPartEntity> possibleParts = autoPartService.getPartsByIdPrefix(partIdPrefix);

                if (!possibleParts.isEmpty()) {
                    part = possibleParts.get(0); // Get the first match
                }
            }
        } catch (NumberFormatException e) {
            // Handle case where partId is not a valid number
            return R.error("Invalid part ID format");
        }

        // If still no part found
        if (part == null) {
            return R.error("Part not found");
        }

        // Get inventory movement history for this part
        Map<String, Object> params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");
        PageUtils movementHistory = inventoryMovementService.getMovementHistoryByPartId(part.getPartId(), params);

        return R.ok()
            .put("part", part)
            .put("movementHistory", movementHistory);
    }

    /**
     * Save a new auto part
     */
    @SysLog("Save Auto Part")
    @PostMapping("/save")
    @RequiresPermissions("autoparts:part:save")
    public R save(@RequestBody AutoPartEntity part) {
        // Validate part data
        ValidatorUtils.validateEntity(part, AddGroup.class);

        // Check if part number already exists
        if (autoPartService.checkPartNumberExists(part.getPartNumber(), null)) {
            return R.error("Part number already exists");
        }

        autoPartService.savePart(part);

        return R.ok();
    }

    /**
     * Update an existing auto part
     */
    @SysLog("Update Auto Part")
    @PostMapping("/update")
    @RequiresPermissions("autoparts:part:update")
    public R update(@RequestBody AutoPartEntity part) {
        // Validate part data
        ValidatorUtils.validateEntity(part, UpdateGroup.class);

        // Check if part number already exists (excluding current part)
        if (autoPartService.checkPartNumberExists(part.getPartNumber(), part.getPartId())) {
            return R.error("Part number already exists");
        }

        autoPartService.updatePart(part);

        return R.ok();
    }

    /**
     * Delete one or more auto parts
     */
    @SysLog("Delete Auto Part")
    @PostMapping("/delete")
    @RequiresPermissions("autoparts:part:delete")
    public R delete(@RequestBody Long[] partIds) {
        autoPartService.removeByIds(Arrays.asList(partIds));

        return R.ok();
    }

    /**
     * Update stock quantity
     */
    @SysLog("Update Stock")
    @PostMapping("/updateStock")
    @RequiresPermissions("autoparts:part:update")
    public R updateStock(@RequestBody Map<String, Object> params) {
        // Extract parameters
        Long partId = Long.parseLong(params.get("partId").toString());
        Integer quantityChange = Integer.parseInt(params.get("quantityChange").toString());
        String movementType = (String) params.get("movementType");
        String reference = (String) params.get("reference");
        String notes = (String) params.get("notes");

        // Get current username
        String username = SecurityUtils.getSubject().getPrincipal().toString();

        try {
            AutoPartEntity part = autoPartService.updateStock(partId, quantityChange, movementType,
                                                           reference, notes, username);
            return R.ok().put("part", part);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * Check if part number exists
     */
    @GetMapping("/check-number/{partNumber}")
    public R checkPartNumber(@PathVariable("partNumber") String partNumber,
                           @RequestParam(required = false) Long excludeId) {
        boolean exists = autoPartService.checkPartNumberExists(partNumber, excludeId);

        return R.ok().put("exists", exists);
    }

    /**
     * Get paginated list of parts with low stock
     */
    @GetMapping("/lowstock")
    @RequiresPermissions("autoparts:part:list")
    public R getLowStockPartsPaginated(@RequestParam Map<String, Object> params) {
        // Add low stock filter parameter
        params.put("lowStock", true);
        
        // Use the regular queryPage method with low stock filter
        PageUtils page = autoPartService.queryPage(params);
        
        return R.ok().put("page", page);
    }

    /**
     * Get list of parts with low stock (no pagination)
     * This endpoint is kept for backward compatibility
     */
    @GetMapping("/low-stock")
    @RequiresPermissions("autoparts:part:list")
    public R getLowStockParts() {
        List<AutoPartEntity> lowStockParts = autoPartService.getLowStockParts();
        
        return R.ok().put("parts", lowStockParts);
    }

    /**
     * Get list of distinct categories
     */
    @GetMapping("/categories")
    public R getCategories() {
        List<String> categories = new ArrayList<>();

        // If no categories found in database, return hardcoded default categories
        if (categories == null || categories.isEmpty()) {
            categories = Arrays.asList(
                "Engine Parts",
                "Transmission",
                "Braking System",
                "Suspension & Steering",
                "Electrical System",
                "Body Parts",
                "Interior Components",
                "Exhaust System",
                "Cooling System",
                "Fuel System",
                "Filters",
                "Lighting"
            );
        }

        return R.ok().put("categories", categories);
    }

    /**
     * Get list of distinct subcategories for a given category
     */
    @GetMapping("/subcategories")
    public R getSubcategories(@RequestParam String category) {
        List<String> subcategories = autoPartService.getSubcategories(category);

        // If no subcategories found in database, return hardcoded default subcategories based on category
        if (subcategories == null || subcategories.isEmpty()) {
            switch (category) {
                case "Engine Parts":
                    subcategories = Arrays.asList("Pistons", "Crankshafts", "Camshafts", "Valves", "Timing Belts", "Engine Mounts");
                    break;
                case "Transmission":
                    subcategories = Arrays.asList("Gearbox", "Clutch", "Torque Converter", "Flywheel", "Transmission Fluid");
                    break;
                case "Braking System":
                    subcategories = Arrays.asList("Brake Pads", "Brake Discs", "Brake Calipers", "Brake Lines", "ABS Components");
                    break;
                case "Suspension & Steering":
                    subcategories = Arrays.asList("Shock Absorbers", "Struts", "Control Arms", "Tie Rods", "Steering Racks", "Ball Joints");
                    break;
                case "Electrical System":
                    subcategories = Arrays.asList("Batteries", "Alternators", "Starters", "Ignition Coils", "Sensors", "Relays");
                    break;
                case "Body Parts":
                    subcategories = Arrays.asList("Doors", "Bumpers", "Hoods", "Fenders", "Mirrors", "Grilles");
                    break;
                default:
                    subcategories = Arrays.asList("General", "Standard", "Premium", "OEM", "Aftermarket");
            }
        }

        return R.ok().put("subcategories", subcategories);
    }

    /**
     * Get list of distinct manufacturers
     */
    @GetMapping("/manufacturers")
    public R getManufacturers() {
        List<String> manufacturers = autoPartService.getManufacturers();

        // If no manufacturers found in database, return hardcoded default manufacturers
        if (manufacturers == null || manufacturers.isEmpty()) {
            manufacturers = Arrays.asList(
                "Bosch",
                "Denso",
                "Continental",
                "ZF",
                "Magna",
                "Valeo",
                "Aisin",
                "Delphi",
                "NGK",
                "Brembo",
                "Michelin",
                "Bridgestone",
                "Toyota OEM",
                "Honda OEM",
                "Ford OEM",
                "GM OEM",
                "Volkswagen OEM",
                "BMW OEM"
            );
        }

        return R.ok().put("manufacturers", manufacturers);
    }
}
