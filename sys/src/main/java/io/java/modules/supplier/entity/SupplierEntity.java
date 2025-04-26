package io.java.modules.supplier.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Supplier Entity
 * Represents a supplier in the CRM system
 *
 * @author Jiasi Lu
 */
@Data
@TableName("crm_supplier")
public class SupplierEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Supplier ID
     */
    @TableId
    private Long supplierId;

    /**
     * Supplier code - unique identifier
     */
    private String supplierCode;

    /**
     * Supplier name
     */
    private String supplierName;

    /**
     * Supplier type: e.g., Product, Service, Technology
     */
    private String supplierType;

    /**
     * Contact person name at the supplier company
     */
    private String contactPerson;

    /**
     * Contact phone number
     */
    private String contactPhone;

    /**
     * Contact email address
     */
    private String contactEmail;

    /**
     * Supplier address
     */
    private String address;

    /**
     * Supplier website URL
     */
    private String website;

    /**
     * Registration/license number
     */
    private String registrationNumber;

    /**
     * Year established
     */
    private Integer establishedYear;

    /**
     * Annual revenue
     */
    private BigDecimal annualRevenue;

    /**
     * Employee count
     */
    private Integer employeeCount;

    /**
     * Industry sector
     */
    private String industry;

    /**
     * Main products or services offered
     */
    private String mainProducts;

    /**
     * Payment terms
     */
    private String paymentTerms;

    /**
     * Description or additional notes
     */
    private String description;

    /**
     * Supplier rating (1-5)
     */
    private Integer rating;

    /**
     * Status: 0-Inactive, 1-Active
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