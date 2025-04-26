package io.java.modules.customer.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Contact Record Entity
 * Represents customer contact history records in the CRM system
 *
 * @author Jiasi Lu
 */
@Data
@TableName("crm_contact_record")
public class ContactRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Contact record ID (Primary key)
     */
    @TableId
    private Long contactId;
    
    /**
     * Customer ID (Foreign key to crm_customer table)
     */
    private Long customerId;
    
    /**
     * Contact type (Call, Email, Meeting, etc.)
     */
    private String contactType;
    
    /**
     * Contact date and time
     */
    private Date contactDate;
    
    /**
     * Person who made the contact
     */
    private String contactPerson;
    
    /**
     * Summary of the contact
     */
    private String summary;
    
    /**
     * Next follow-up date
     */
    private Date nextFollowUp;
    
    /**
     * Creation time
     */
    private Date createTime;
    
    /**
     * Last update time
     */
    private Date updateTime;
} 