<template>
  <el-dialog
    title="Insurance Product Details"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="70%"
    class="insurance-product-detail-dialog">
    <div v-if="productInfo.productId" class="detail-header">
      <div class="product-badge" :class="{'active': productInfo.status === 1, 'inactive': productInfo.status === 0}">
        {{ productInfo.status === 1 ? 'Active' : 'Inactive' }}
      </div>
      <h2 class="product-title">{{ productInfo.productName }}</h2>
      <div class="product-meta">
        <span class="product-code">Code: {{ productInfo.productCode }}</span>
        <el-tag :type="getProductTypeTagType(productInfo.productType)" size="medium" class="product-type-tag">
          {{ productInfo.productType }}
        </el-tag>
        <span class="product-provider">Provider: {{ productInfo.provider }}</span>
      </div>
    </div>
    
    <el-divider content-position="left">Basic Information</el-divider>
    
    <el-row :gutter="20" class="info-section">
      <el-col :span="12">
        <div class="info-item">
          <div class="info-label">ID</div>
          <div class="info-value">{{ productInfo.productId }}</div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="info-item">
          <div class="info-label">Provider</div>
          <div class="info-value">{{ productInfo.provider }}</div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="info-item">
          <div class="info-label">Min Premium</div>
          <div class="info-value premium">{{ productInfo.minPremium ? '$' + productInfo.minPremium : '—' }}</div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="info-item">
          <div class="info-label">Max Coverage</div>
          <div class="info-value coverage">{{ productInfo.maxCoverage ? '$' + productInfo.maxCoverage : '—' }}</div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="info-item">
          <div class="info-label">Create Time</div>
          <div class="info-value date">{{ productInfo.createTime }}</div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="info-item">
          <div class="info-label">Last Updated</div>
          <div class="info-value date">{{ productInfo.updateTime || '—' }}</div>
        </div>
      </el-col>
    </el-row>
    
    <el-divider content-position="left">Product Description</el-divider>
    
    <div class="description-content">
      <div v-if="productInfo.description" class="description-text">
        {{ productInfo.description }}
      </div>
      <div v-else class="no-content">
        No description provided
      </div>
    </div>
    
    <el-tabs type="border-card" class="detail-tabs">
      <el-tab-pane label="Coverage Details">
        <div v-if="productInfo.coverageDetails" class="tab-content" v-html="formatTextWithLineBreaks(productInfo.coverageDetails)"></div>
        <div v-else class="no-content">No coverage details provided</div>
      </el-tab-pane>
      <el-tab-pane label="Terms & Conditions">
        <div v-if="productInfo.terms" class="tab-content" v-html="formatTextWithLineBreaks(productInfo.terms)"></div>
        <div v-else class="no-content">No terms and conditions provided</div>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false" plain>Close</el-button>
      <el-button v-if="isAuth('insurance:product:update')" type="primary" @click="editProduct" icon="el-icon-edit">Edit</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { isAuth } from '@/utils'

export default {
  data () {
    return {
      visible: false,
      productId: null,
      productInfo: {
        productId: '',
        productCode: '',
        productName: '',
        productType: '',
        provider: '',
        minPremium: '',
        maxCoverage: '',
        description: '',
        coverageDetails: '',
        terms: '',
        status: '',
        createTime: '',
        updateTime: ''
      }
    }
  },
  methods: {
    // Check permission for buttons
    isAuth,
    
    init (id) {
      this.productId = id
      this.visible = true
      this.getProductInfo()
    },
    
    // Get product type tag color
    getProductTypeTagType (type) {
      const typeMap = {
        'Life': 'primary',
        'Health': 'success',
        'Property': 'warning',
        'Auto': 'danger',
        'Travel': 'info',
        'Business': '',
        'Other': 'info'
      }
      return typeMap[type] || 'info'
    },
    
    // Format text with line breaks
    formatTextWithLineBreaks (text) {
      if (!text) return 'Not provided'
      return text.replace(/\n/g, '<br>')
    },
    
    // Get product information
    getProductInfo () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl(`/insurance/product/info/${this.productId}`),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        this.dataListLoading = false
        if (data && data.code === 0) {
          this.productInfo = data.product
        } else {
          this.$message.error('Failed to get product details')
          this.visible = false
        }
      }).catch(() => {
        this.dataListLoading = false
        this.$message.error('Error loading product details')
      })
    },
    
    // Edit product
    editProduct () {
      this.visible = false
      this.$nextTick(() => {
        this.$emit('edit', this.productId)
      })
    }
  }
}
</script>

<style scoped>
.insurance-product-detail-dialog >>> .el-dialog__body {
  padding: 0 20px 20px;
}

.insurance-product-detail-dialog >>> .el-dialog__header {
  padding: 15px 20px;
  background: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.detail-header {
  padding: 20px 0;
  position: relative;
}

.product-badge {
  position: absolute;
  top: 20px;
  right: 0;
  padding: 5px 15px;
  border-radius: 15px;
  font-size: 12px;
  font-weight: bold;
  text-transform: uppercase;
}

.product-badge.active {
  background-color: #f0f9eb;
  color: #67c23a;
  border: 1px solid #c2e7b0;
}

.product-badge.inactive {
  background-color: #f4f4f5;
  color: #909399;
  border: 1px solid #e9e9eb;
}

.product-title {
  margin: 0;
  padding: 0;
  font-size: 22px;
  color: #303133;
  margin-bottom: 10px;
}

.product-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
  color: #606266;
  font-size: 14px;
}

.product-type-tag {
  font-size: 12px;
}

.info-section {
  margin: 20px 0;
}

.info-item {
  margin-bottom: 15px;
}

.info-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 5px;
}

.info-value {
  font-size: 14px;
  color: #303133;
}

.info-value.premium, 
.info-value.coverage {
  font-weight: bold;
  color: #409EFF;
}

.info-value.date {
  color: #606266;
  font-size: 13px;
}

.description-content {
  background: #f8f8f8;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.description-text {
  white-space: pre-line;
  line-height: 1.6;
  color: #303133;
}

.detail-tabs {
  margin-top: 20px;
  margin-bottom: 20px;
}

.tab-content {
  line-height: 1.6;
  padding: 10px;
  max-height: 250px;
  overflow-y: auto;
}

.no-content {
  color: #909399;
  font-style: italic;
  padding: 15px;
  text-align: center;
  background: #f8f8f8;
  border-radius: 4px;
}

.el-divider__text {
  font-size: 16px;
  font-weight: bold;
  color: #409EFF;
}
</style> 