package io.java.modules.insurance.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Insurance Product Entity
 *
 * @author Jiasi Lu
 */
@Data
@TableName("crm_insurance_product")
public class InsuranceProductEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Product ID (Primary key)
     */
    @TableId
    private Long productId;
    
    /**
     * Product code
     */
    private String productCode;
    
    /**
     * Product name
     */
    private String productName;
    
    /**
     * Product type (Life, Health, Auto, Property, etc.)
     */
    private String productType;
    
    /**
     * Insurance provider/company
     */
    private String provider;
    
    /**
     * Minimum premium amount
     */
    private BigDecimal minPremium;
    
    /**
     * Maximum coverage amount
     */
    private BigDecimal maxCoverage;
    
    /**
     * Product description
     */
    private String description;
    
    /**
     * Coverage details
     */
    private String coverageDetails;
    
    /**
     * Policy terms and conditions
     */
    private String terms;
    
    /**
     * Status (1: Active, 0: Inactive)
     */
    private Integer status;
    
    /**
     * Creation time
     */
    private Date createTime;
    
    /**
     * Last update time
     */
    private Date updateTime;
} 