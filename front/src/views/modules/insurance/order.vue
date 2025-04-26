<template>
  <div class="mod-insurance-order">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="searchForm.customerName" placeholder="Customer Name" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.productName" placeholder="Product Name" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.status" placeholder="Status" clearable>
          <el-option label="Pending" :value="0"></el-option>
          <el-option label="Confirmed" :value="1"></el-option>
          <el-option label="Cancelled" :value="2"></el-option>
          <el-option label="Completed" :value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">Search</el-button>
        <el-button @click="resetSearchForm()">Reset</el-button>
      </el-form-item>
    </el-form>
    
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      style="width: 100%">
      <el-table-column
        prop="orderId"
        header-align="center"
        align="center"
        width="80"
        label="ID">
      </el-table-column>
      <el-table-column
        prop="customerName"
        header-align="center"
        align="left"
        width="120"
        label="Customer">
      </el-table-column>
      <el-table-column
        prop="productName"
        header-align="center"
        align="left"
        label="Product">
      </el-table-column>
      <el-table-column
        prop="appointmentDate"
        header-align="center"
        align="center"
        width="160"
        label="Appointment Date">
        <template slot-scope="scope">
          {{ formatDate(scope.row.appointmentDate) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="contactPhone"
        header-align="center"
        align="center"
        width="130"
        label="Phone">
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        width="100"
        label="Status">
        <template slot-scope="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        width="160"
        label="Create Time">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="220"
        label="Operations">
        <template slot-scope="scope">
          <el-button v-if="isAuth('insurance:order:info')" type="text" size="small" @click="viewDetailsHandle(scope.row.orderId)">View</el-button>
          <el-button v-if="isAuth('insurance:order:update') && scope.row.status === 0" type="text" size="small" @click="confirmHandle(scope.row.orderId)">Confirm</el-button>
          <el-button v-if="isAuth('insurance:order:update') && scope.row.status === 1" type="text" size="small" @click="completeHandle(scope.row.orderId)">Complete</el-button>
          <el-button v-if="isAuth('insurance:order:update') && scope.row.status === 0" type="text" size="small" class="text-danger" @click="cancelHandle(scope.row.orderId)">Cancel</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    
    <!-- Order Details Dialog -->
    <el-dialog
      title="Order Details"
      :close-on-click-modal="false"
      :visible.sync="detailVisible"
      width="70%"
      class="order-detail-dialog">
      <div v-if="orderDetails" class="order-detail-container">
        <!-- Order Header with Status -->
        <div class="order-header">
          <div class="order-id">
            <span class="label">Order #</span>
            <span class="value">{{ orderDetails.orderId }}</span>
          </div>
          <div class="order-status">
            <el-tag size="medium" :type="getStatusTagType(orderDetails.status)">
              {{ getStatusText(orderDetails.status) }}
            </el-tag>
          </div>
        </div>
        
        <!-- Order Progress Timeline -->
        <div class="order-timeline" v-if="orderDetails.status !== 2">
          <el-steps :active="getOrderProgressStep(orderDetails.status)" finish-status="success" simple>
            <el-step title="Submitted" :icon="orderDetails.status >= 0 ? 'el-icon-check' : 'el-icon-time'"></el-step>
            <el-step title="Confirmed" :icon="orderDetails.status >= 1 ? 'el-icon-check' : 'el-icon-time'"></el-step>
            <el-step title="Completed" :icon="orderDetails.status >= 3 ? 'el-icon-check' : 'el-icon-time'"></el-step>
          </el-steps>
        </div>
        
        <!-- Order Cancelled Notice -->
        <div v-if="orderDetails.status === 2" class="order-cancelled">
          <i class="el-icon-circle-close"></i>
          <span>This order has been cancelled</span>
        </div>

        <!-- Order Details Sections -->
        <div class="order-details-section">
          <div class="section">
            <h3><i class="el-icon-user"></i> Customer Information</h3>
            <div class="section-content">
              <div class="info-item">
                <span class="label">Name:</span>
                <span class="value">{{ orderDetails.customerName }}</span>
              </div>
              <div class="info-item">
                <span class="label">Phone:</span>
                <span class="value">{{ orderDetails.contactPhone }}</span>
              </div>
              <div class="info-item">
                <span class="label">Email:</span>
                <span class="value">{{ orderDetails.contactEmail || 'Not provided' }}</span>
              </div>
            </div>
          </div>
          
          <div class="section">
            <h3><i class="el-icon-document"></i> Product Information</h3>
            <div class="section-content">
              <div class="info-item">
                <span class="label">Product:</span>
                <span class="value">{{ orderDetails.productName }}</span>
              </div>
              <div class="info-item">
                <span class="label">Premium:</span>
                <span class="value">{{ orderDetails.premiumAmount ? '$' + orderDetails.premiumAmount : 'Not set' }}</span>
              </div>
              <div class="info-item">
                <span class="label">Coverage:</span>
                <span class="value">{{ orderDetails.coverageAmount ? '$' + orderDetails.coverageAmount : 'Not set' }}</span>
              </div>
            </div>
          </div>
        </div>
        
        <div class="order-details-section">
          <div class="section">
            <h3><i class="el-icon-date"></i> Appointment Details</h3>
            <div class="section-content">
              <div class="info-item">
                <span class="label">Date:</span>
                <span class="value">{{ formatDate(orderDetails.appointmentDate) }}</span>
              </div>
              <div class="info-item">
                <span class="label">Created on:</span>
                <span class="value">{{ formatDate(orderDetails.createTime) }}</span>
              </div>
              <div v-if="orderDetails.processedBy" class="info-item">
                <span class="label">Processed by:</span>
                <span class="value">{{ orderDetails.processedBy }} at {{ formatDate(orderDetails.processedTime) }}</span>
              </div>
            </div>
          </div>
          
          <div class="section">
            <h3><i class="el-icon-chat-line-square"></i> Comments</h3>
            <div class="section-content">
              <div class="comments-box">
                {{ orderDetails.comments || 'No comments provided' }}
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">Close</el-button>
        <el-button v-if="isAuth('insurance:order:update') && orderDetails && orderDetails.status === 0" type="primary" @click="confirmHandle(orderDetails.orderId)">
          <i class="el-icon-check"></i> Confirm
        </el-button>
        <el-button v-if="isAuth('insurance:order:update') && orderDetails && orderDetails.status === 1" type="success" @click="completeHandle(orderDetails.orderId)">
          <i class="el-icon-finished"></i> Complete
        </el-button>
        <el-button v-if="isAuth('insurance:order:update') && orderDetails && orderDetails.status === 0" type="danger" @click="cancelHandle(orderDetails.orderId)">
          <i class="el-icon-close"></i> Cancel
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { isAuth } from '@/utils'
import { formatDate } from '@/utils/date'

export default {
  data () {
    return {
      searchForm: {
        customerName: '',
        productName: '',
        status: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      detailVisible: false,
      orderDetails: null
    }
  },
  created () {
    this.getDataList()
  },
  methods: {
    isAuth,
    formatDate,
    
    // Get status text
    getStatusText (status) {
      const statusMap = {
        0: 'Pending',
        1: 'Confirmed',
        2: 'Cancelled',
        3: 'Completed'
      }
      return statusMap[status] || 'Unknown'
    },
    
    // Get status tag type
    getStatusTagType (status) {
      const typeMap = {
        0: 'info',
        1: 'primary',
        2: 'danger',
        3: 'success'
      }
      return typeMap[status] || 'info'
    },
    
    // Get order progress step
    getOrderProgressStep (status) {
      if (status === 3) return 3
      if (status === 1) return 2
      if (status === 0) return 1
      return 0
    },
    
    // Get data list
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/insurance/order/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'customerName': this.searchForm.customerName,
          'productName': this.searchForm.productName,
          'status': this.searchForm.status
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },
    
    // Reset search form
    resetSearchForm () {
      this.searchForm = {
        customerName: '',
        productName: '',
        status: ''
      }
      this.getDataList()
    },
    
    // Page size change
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    
    // Current page change
    currentChangeHandle (val) {
      this.pageIndex = val
      this.getDataList()
    },
    
    // View order details
    viewDetailsHandle (id) {
      this.$http({
        url: this.$http.adornUrl(`/insurance/order/info/${id}`),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.orderDetails = data.order
          this.detailVisible = true
        } else {
          this.$message.error(data.msg || 'Failed to get order details')
        }
      })
    },
    
    // Confirm order
    confirmHandle (id) {
      this.$confirm('Are you sure you want to confirm this order?', 'Confirm', {
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl(`/insurance/order/confirm/${id}`),
          method: 'post'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: 'Order confirmed successfully',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
                if (this.detailVisible) {
                  this.viewDetailsHandle(id)
                }
              }
            })
          } else {
            this.$message.error(data.msg || 'Failed to confirm order')
          }
        })
      })
    },
    
    // Complete order
    completeHandle (id) {
      this.$confirm('Are you sure you want to mark this order as completed?', 'Complete', {
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl(`/insurance/order/complete/${id}`),
          method: 'post'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: 'Order completed successfully',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
                if (this.detailVisible) {
                  this.viewDetailsHandle(id)
                }
              }
            })
          } else {
            this.$message.error(data.msg || 'Failed to complete order')
          }
        })
      })
    },
    
    // Cancel order
    cancelHandle (id) {
      this.$confirm('Are you sure you want to cancel this order?', 'Cancel', {
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        type: 'danger'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl(`/insurance/order/cancel/${id}`),
          method: 'post'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: 'Order cancelled successfully',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
                if (this.detailVisible) {
                  this.viewDetailsHandle(id)
                }
              }
            })
          } else {
            this.$message.error(data.msg || 'Failed to cancel order')
          }
        })
      })
    }
  }
}
</script>

<style scoped>
.mod-insurance-order {
  padding: 20px;
}

/* Order Details Dialog Styles */
.order-detail-dialog /deep/ .el-dialog__header {
  padding: 20px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.order-detail-dialog /deep/ .el-dialog__title {
  font-size: 18px;
  font-weight: 600;
}

.order-detail-dialog /deep/ .el-dialog__body {
  padding: 0;
}

.order-detail-container {
  padding: 20px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.order-id {
  display: flex;
  align-items: center;
}

.order-id .label {
  font-size: 16px;
  color: #606266;
  margin-right: 8px;
}

.order-id .value {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.order-timeline {
  margin: 30px 0;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.order-cancelled {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 30px 0;
  padding: 15px;
  background-color: #fef0f0;
  color: #f56c6c;
  border-radius: 4px;
  border: 1px solid #fbc4c4;
  font-size: 16px;
}

.order-cancelled i {
  font-size: 22px;
  margin-right: 10px;
}

.order-details-section {
  display: flex;
  margin-bottom: 20px;
}

.section {
  flex: 1;
  padding: 15px;
  background-color: #ffffff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  margin: 0 10px;
}

.section:first-child {
  margin-left: 0;
}

.section:last-child {
  margin-right: 0;
}

.section h3 {
  font-size: 16px;
  color: #303133;
  margin-top: 0;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  align-items: center;
}

.section h3 i {
  margin-right: 8px;
  color: #409EFF;
}

.section-content {
  padding: 0 5px;
}

.info-item {
  margin-bottom: 12px;
  display: flex;
}

.info-item .label {
  width: 100px;
  color: #606266;
  font-weight: 500;
}

.info-item .value {
  flex: 1;
  color: #303133;
  word-break: break-word;
}

.comments-box {
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 10px;
  min-height: 80px;
  max-height: 150px;
  overflow-y: auto;
  white-space: pre-line;
  line-height: 1.6;
  color: #606266;
}

.dialog-footer {
  padding: 20px;
  border-top: 1px solid #e4e7ed;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer .el-button {
  margin-left: 10px;
}

.text-danger {
  color: #F56C6C;
}

@media screen and (max-width: 992px) {
  .order-details-section {
    flex-direction: column;
  }
  
  .section {
    margin: 10px 0;
  }
}
</style> 