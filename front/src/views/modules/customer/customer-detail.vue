<template>
  <el-dialog
    title="Customer Details"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="60%">
    <el-card v-if="customer" shadow="never" class="detail-card">
      <div slot="header" class="detail-header">
        <span>{{ customer.customerName }}</span>
        <el-tag v-if="customer.status === 1" type="success" size="small">Active</el-tag>
        <el-tag v-else type="info" size="small">Inactive</el-tag>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="detail-item">
            <label>Customer ID:</label>
            <span>{{ customer.customerId }}</span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="detail-item">
            <label>Created At:</label>
            <span>{{ customer.createTime }}</span>
          </div>
        </el-col>
      </el-row>
      
      <el-divider></el-divider>
      
      <h4 class="detail-section-title">Contact Information</h4>
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="detail-item">
            <label>Email:</label>
            <span>{{ customer.email }}</span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="detail-item">
            <label>Phone:</label>
            <span>{{ customer.phone }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="detail-item">
            <label>Address:</label>
            <span>{{ customer.address || 'Not provided' }}</span>
          </div>
        </el-col>
      </el-row>
      
      <el-divider></el-divider>
      
      <h4 class="detail-section-title">Company Information</h4>
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="detail-item">
            <label>Company:</label>
            <span>{{ customer.company || 'Not provided' }}</span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="detail-item">
            <label>Industry:</label>
            <span>{{ customer.industry || 'Not specified' }}</span>
          </div>
        </el-col>
      </el-row>
      
      <el-divider></el-divider>
      
      <h4 class="detail-section-title">Additional Information</h4>
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="detail-item">
            <label>Customer Source:</label>
            <span>{{ customer.source || 'Not specified' }}</span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="detail-item">
            <label>Last Updated:</label>
            <span>{{ customer.updateTime || customer.createTime }}</span>
          </div>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" v-if="customer.notes">
        <el-col :span="24">
          <div class="detail-item notes-item">
            <label>Notes:</label>
            <p>{{ customer.notes }}</p>
          </div>
        </el-col>
      </el-row>
      
      <el-divider></el-divider>
      
      <div class="detail-actions">
        <el-button v-if="isAuth('customer:customer:update')" type="primary" size="small" @click="editHandle()">
          <i class="el-icon-edit"></i> Edit Customer
        </el-button>
        <el-button v-if="isAuth('customer:contact:list')" size="small" @click="contactsHandle()">
          <i class="el-icon-document"></i> View Contacts
        </el-button>
      </div>
      
    </el-card>
    <div v-else class="loading-container">
      <el-skeleton :rows="10" animated />
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">Close</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { isAuth } from '@/utils'
  export default {
    data () {
      return {
        visible: false,
        customer: null
      }
    },
    methods: {
      // Check permission for buttons
      isAuth,
      
      // Initialize and load customer data
      init (id) {
        this.visible = true
        this.customer = null
        
        this.$nextTick(() => {
          this.getCustomerInfo(id)
        })
      },
      
      // Fetch customer details from API
      getCustomerInfo (id) {
        this.$http({
          url: this.$http.adornUrl(`/customer/info/${id}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.customer = data.customer
          } else {
            this.$message.error('Failed to load customer information')
            this.visible = false
          }
        })
      },
      
      // Handle edit button click
      editHandle () {
        this.visible = false
        this.$emit('edit', this.customer.customerId)
      },
      
      // Handle contacts button click
      contactsHandle () {
        this.visible = false
        this.$emit('contacts', this.customer.customerId, this.customer.customerName)
      }
    }
  }
</script>

<style lang="scss">
  .detail-card {
    .detail-header {
      display: flex;
      align-items: center;
      
      .el-tag {
        margin-left: 10px;
      }
    }
    
    .detail-section-title {
      margin: 10px 0;
      color: #2c3e50;
      font-weight: 600;
    }
    
    .detail-item {
      margin-bottom: 12px;
      
      label {
        font-weight: 500;
        color: #606266;
        display: inline-block;
        width: 140px;
      }
      
      span {
        color: #2c3e50;
      }
      
      &.notes-item {
        margin-top: 10px;
        
        p {
          margin-top: 5px;
          white-space: pre-line;
          color: #2c3e50;
        }
      }
    }
    
    .detail-actions {
      display: flex;
      justify-content: flex-start;
      margin-top: 20px;
      
      .el-button {
        margin-right: 10px;
      }
    }
  }
  
  .loading-container {
    padding: 20px;
  }
</style> 