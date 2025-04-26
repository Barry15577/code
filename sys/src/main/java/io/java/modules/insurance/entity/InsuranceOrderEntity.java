package io.java.modules.insurance.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Insurance Order Entity
 * Represents a customer order for an insurance product
 *
 * @author Jiasi Lu
 */
@Data
@TableName("crm_insurance_order")
public class InsuranceOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Order ID
     */
    @TableId
    private Long orderId;

    /**
     * Product ID
     */
    private Long productId;

    /**
     * Product name at the time of order
     */
    private String productName;

    /**
     * Customer ID
     */
    private Long customerId;

    /**
     * Customer name
     */
    private String customerName;

    /**
     * Contact phone
     */
    private String contactPhone;

    /**
     * Contact email
     */
    private String contactEmail;

    /**
     * Premium amount
     */
    private BigDecimal premiumAmount;

    /**
     * Coverage amount
     */
    private BigDecimal coverageAmount;

    /**
     * Order status: 0-Pending, 1-Confirmed, 2-Cancelled, 3-Completed
     */
    private Integer status;

    /**
     * Appointment date (when the customer wants to meet with an agent)
     */
    private Date appointmentDate;

    /**
     * Comments or special requirements from customer
     */
    private String comments;

    /**
     * Who processed this order
     */
    private String processedBy;

    /**
     * When the order was processed
     */
    private Date processedTime;

    /**
     * Creation time
     */
    private Date createTime;

    /**
     * Update time
     */
    private Date updateTime;
} 