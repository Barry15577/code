package io.java.modules.autoparts.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Inventory Movement Entity
 * Tracks changes in inventory levels for auto parts
 *
 * @author Jiasi Lu
 */
@Data
@TableName("crm_inventory_movement")
public class InventoryMovementEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Movement ID
     */
    @TableId
    private Long movementId;

    /**
     * Part ID
     */
    private Long partId;

    /**
     * Part number
     */
    private String partNumber;

    /**
     * Part name
     */
    private String partName;
    
    /**
     * Movement type: Stock In, Stock Out, Adjustment, Return
     */
    private String movementType;
    
    /**
     * Quantity changed (positive for in, negative for out)
     */
    private Integer quantityChanged;
    
    /**
     * Previous stock quantity
     */
    private Integer previousQuantity;
    
    /**
     * Current stock quantity after change
     */
    private Integer currentQuantity;
    
    /**
     * Date of movement
     */
    private Date movementDate;
    
    /**
     * Reference document (e.g., Order #, Return #)
     */
    private String reference;
    
    /**
     * Notes about this movement
     */
    private String notes;
    
    /**
     * User who performed this movement
     */
    private String createdBy;
    
    /**
     * Creation time
     */
    private Date createTime;
} 