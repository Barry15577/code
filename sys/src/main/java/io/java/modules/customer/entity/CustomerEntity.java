package io.java.modules.customer.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Customer Entity
 * Represents customer information in the CRM system
 *
 * @author Jiasi Lu
 */
@Data
@TableName("crm_customer")
public class CustomerEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Customer ID (Primary key)
     */
    @TableId
    private Long customerId;
    
    /**
     * Customer name
     */
    private String customerName;
    
    /**
     * Email address
     */
    private String email;
    
    /**
     * Phone number
     */
    private String phone;
    
    /**
     * Company name
     */
    private String company;
    
    /**
     * Address
     */
    private String address;
    
    /**
     * Status (1: Active, 0: Inactive)
     */
    private Integer status;
    
    /**
     * Industry
     */
    private String industry;
    
    /**
     * Customer source (how they found us)
     */
    private String source;
    
    /**
     * Notes or comments about the customer
     */
    private String notes;
    
    /**
     * Creation time
     */
    private Date createTime;
    
    /**
     * Last update time
     */
    private Date updateTime;
} 