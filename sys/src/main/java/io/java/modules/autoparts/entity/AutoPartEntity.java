package io.java.modules.autoparts.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Auto Part Entity
 * Represents an automotive part in the inventory system
 *
 * @author Jiasi Lu
 */
@Data
@TableName("crm_auto_part")
public class AutoPartEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Part ID
     */
    @TableId
    private Long partId;

    /**
     * Part number - unique identifier
     */
    private String partNumber;

    /**
     * Part name
     */
    private String partName;

    /**
     * Part category: e.g., Engine, Transmission, Brakes, Electrical
     */
    private String category;

    /**
     * Part subcategory for more specific classification
     */
    private String subcategory;

    /**
     * Manufacturer name
     */
    private String manufacturer;

    /**
     * Compatible car makes (comma-separated)
     */
    private String compatibleMakes;

    /**
     * Compatible car models (comma-separated)
     */
    private String compatibleModels;

    /**
     * Compatible year range (e.g., "2010-2022")
     */
    private String yearRange;

    /**
     * Current stock quantity
     */
    private Integer stockQuantity;

    /**
     * Low stock threshold for alerts
     */
    private Integer lowStockThreshold;

    /**
     * Purchase price
     */
    private BigDecimal purchasePrice;

    /**
     * Selling price
     */
    private BigDecimal sellingPrice;

    /**
     * Supplier ID
     */
    private Long supplierId;

    /**
     * Supplier name
     */
    private String supplierName;

    /**
     * Location in warehouse
     */
    private String location;

    /**
     * Product dimensions
     */
    private String dimensions;

    /**
     * Product weight
     */
    private BigDecimal weight;

    /**
     * Weight unit (kg, g, lb)
     */
    private String weightUnit;

    /**
     * Part condition: New, Used, Refurbished
     */
    @TableField("`condition`")
    private String condition;

    /**
     * OEM (Original Equipment Manufacturer) number
     */
    private String oemNumber;

    /**
     * Part description or additional notes
     */
    private String description;

    /**
     * Image URL
     */
    private String imageUrl;

    /**
     * Status: 0-Inactive, 1-Active
     */
    private Integer status;

    /**
     * Last restock date
     */
    private Date restockDate;

    /**
     * Creation time
     */
    private Date createTime;

    /**
     * Last update time
     */
    private Date updateTime;
} 