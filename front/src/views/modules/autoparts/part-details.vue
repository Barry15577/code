<template>
  <el-dialog
    title="Auto Part Details"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="80%"
    class="part-details-dialog">
    <div class="detail-header" v-if="partDetails.imageUrl || partDetails.partName">
      <div class="header-content">
        <div class="part-info">
          <h2 class="part-title">{{ partDetails.partName }}</h2>
          <div class="part-subtitle">
            <span class="part-number">{{ partDetails.partNumber }}</span>
            <el-tag v-if="partDetails.status === 1" type="success" size="small">Active</el-tag>
            <el-tag v-else type="info" size="small">Inactive</el-tag>
          </div>
        </div>
        <div class="stock-info">
          <div class="price-info">
            <div class="price-container">
              <div class="price-label">Selling Price</div>
              <div class="price-value">{{ partDetails.sellingPrice ? '$' + partDetails.sellingPrice : 'N/A' }}</div>
            </div>
            <div class="price-container">
              <div class="price-label">Purchase Price</div>
              <div class="price-value secondary">{{ partDetails.purchasePrice ? '$' + partDetails.purchasePrice : 'N/A' }}</div>
            </div>
          </div>
          <div class="stock-container">
            <div class="stock-value" :class="{'low-stock': partDetails.stockQuantity <= partDetails.lowStockThreshold}">
              {{ partDetails.stockQuantity }}
            </div>
            <div class="stock-label">
              <span v-if="partDetails.stockQuantity <= partDetails.lowStockThreshold">Low Stock!</span>
              <span v-else>In Stock</span>
            </div>
          </div>
        </div>
      </div>
      <div class="part-image-container" v-if="partDetails.imageUrl">
        <img :src="partDetails.imageUrl" alt="Part Image" class="part-image-preview">
      </div>
    </div>

    <el-tabs v-model="activeTab" class="detail-tabs">
      <el-tab-pane label="Basic Information" name="basic">
        <el-card shadow="never">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-descriptions :column="1" border class="descriptions-block">
                <el-descriptions-item label="Part ID"><span class="desc-content">{{ partDetails.partId }}</span></el-descriptions-item>
                <el-descriptions-item label="Category"><span class="desc-content">{{ partDetails.category || 'N/A' }}</span></el-descriptions-item>
                <el-descriptions-item label="Subcategory"><span class="desc-content">{{ partDetails.subcategory || 'N/A' }}</span></el-descriptions-item>
                <el-descriptions-item label="Manufacturer"><span class="desc-content">{{ partDetails.manufacturer || 'N/A' }}</span></el-descriptions-item>
                <el-descriptions-item label="OEM Number"><span class="desc-content">{{ partDetails.oemNumber || 'N/A' }}</span></el-descriptions-item>
                <el-descriptions-item label="Condition"><span class="desc-content">{{ partDetails.condition || 'N/A' }}</span></el-descriptions-item>
              </el-descriptions>
            </el-col>
            <el-col :span="12">
              <el-descriptions :column="1" border class="descriptions-block">
                <el-descriptions-item label="Low Stock Threshold"><span class="desc-content">{{ partDetails.lowStockThreshold }}</span></el-descriptions-item>
                <el-descriptions-item label="Location"><span class="desc-content">{{ partDetails.location || 'N/A' }}</span></el-descriptions-item>
                <el-descriptions-item label="Created"><span class="desc-content">{{ formatDate(partDetails.createTime) }}</span></el-descriptions-item>
                <el-descriptions-item label="Last Updated"><span class="desc-content">{{ formatDate(partDetails.updateTime) }}</span></el-descriptions-item>
              </el-descriptions>
            </el-col>
          </el-row>
        </el-card>
      </el-tab-pane>
      
      <el-tab-pane label="Specifications" name="specs">
        <el-card shadow="never">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-descriptions :column="1" border class="descriptions-block">
                <el-descriptions-item label="Compatible Makes"><span class="desc-content">{{ partDetails.compatibleMakes || 'N/A' }}</span></el-descriptions-item>
                <el-descriptions-item label="Compatible Models"><span class="desc-content">{{ partDetails.compatibleModels || 'N/A' }}</span></el-descriptions-item>
                <el-descriptions-item label="Year Range"><span class="desc-content">{{ partDetails.yearRange || 'N/A' }}</span></el-descriptions-item>
              </el-descriptions>
            </el-col>
            <el-col :span="12">
              <el-descriptions :column="1" border class="descriptions-block">
                <el-descriptions-item label="Dimensions"><span class="desc-content">{{ partDetails.dimensions || 'N/A' }}</span></el-descriptions-item>
                <el-descriptions-item label="Weight"><span class="desc-content">{{ partDetails.weight ? `${partDetails.weight} ${partDetails.weightUnit}` : 'N/A' }}</span></el-descriptions-item>
              </el-descriptions>
            </el-col>
          </el-row>
          <el-divider content-position="left">Description</el-divider>
          <div class="description-content">
            {{ partDetails.description || 'No description available' }}
          </div>
        </el-card>
      </el-tab-pane>
      
      <el-tab-pane label="Supplier Information" name="supplier">
        <el-card shadow="never" class="supplier-card">
          <div class="supplier-header">
            <i class="el-icon-office-building"></i>
            <span>{{ partDetails.supplierName || 'No Supplier' }}</span>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="Supplier ID"><span class="desc-content">{{ partDetails.supplierId || 'N/A' }}</span></el-descriptions-item>
            <el-descriptions-item label="Last Restock"><span class="desc-content">{{ formatDate(partDetails.restockDate) || 'N/A' }}</span></el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-tab-pane>
      
      <el-tab-pane label="Inventory Movements" name="movements" v-if="partDetails.partId">
        <inventory-movements :partId="partDetails.partId"></inventory-movements>
      </el-tab-pane>
    </el-tabs>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">Close</el-button>
      <el-button v-if="isAuth('autoparts:part:update')" type="primary" @click="editPartHandle()">
        <i class="el-icon-edit"></i> Edit
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
  import InventoryMovements from './part-inventory-movements'
  import { isAuth } from '@/utils'
  export default {
    components: {
      InventoryMovements
    },
    data () {
      return {
        visible: false,
        activeTab: 'basic',
        partDetails: {
          partId: '',
          partNumber: '',
          partName: '',
          category: '',
          subcategory: '',
          manufacturer: '',
          compatibleMakes: '',
          compatibleModels: '',
          yearRange: '',
          stockQuantity: 0,
          lowStockThreshold: 5,
          purchasePrice: '',
          sellingPrice: '',
          supplierId: '',
          supplierName: '',
          location: '',
          dimensions: '',
          weight: '',
          weightUnit: '',
          condition: '',
          oemNumber: '',
          description: '',
          imageUrl: '',
          status: 1,
          restockDate: '',
          createTime: '',
          updateTime: ''
        }
      }
    },
    methods: {
      isAuth,
      init (id) {
        this.visible = true
        this.activeTab = 'basic' // Reset to the first tab when opening
        this.$nextTick(() => {
          this.getPartDetails(id)
        })
      },
      // Get part details
      getPartDetails (id) {
        this.$http({
          url: this.$http.adornUrl(`/autoparts/part/info/${id}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.partDetails = data.part
          }
        })
      },
      // Format date
      formatDate (date) {
        if (!date) return ''
        let d = new Date(date)
        return d.toLocaleString()
      },
      // Edit part
      editPartHandle () {
        this.visible = false
        this.$emit('edit', this.partDetails.partId)
      }
    }
  }
</script>

<style scoped>
.part-details-dialog .el-dialog__body {
  padding: 0 20px 20px;
}

.detail-header {
  display: flex;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.header-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.part-title {
  margin: 0 0 10px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.part-subtitle {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.part-number {
  margin-right: 10px;
  color: #606266;
  font-size: 14px;
}

.stock-info {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-top: 20px;
}

.price-info {
  display: flex;
  gap: 20px;
}

.price-container {
  display: flex;
  flex-direction: column;
}

.price-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.price-value {
  font-size: 22px;
  font-weight: 600;
  color: #67c23a;
}

.price-value.secondary {
  font-size: 18px;
  color: #909399;
}

.stock-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 4px;
  padding: 8px 12px;
  background-color: #ecf5ff;
}

.stock-value {
  font-size: 28px;
  font-weight: 700;
  color: #409eff;
}

.stock-value.low-stock {
  color: #f56c6c;
}

.stock-label {
  font-size: 14px;
  color: #606266;
}

.part-image-container {
  width: 200px;
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 20px;
  background-color: #fff;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.part-image-preview {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.detail-tabs {
  margin-top: 20px;
}

.descriptions-block {
  margin-bottom: 20px;
}

.desc-content {
  word-break: break-word;
}

.description-content {
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
  min-height: 80px;
  white-space: pre-line;
}

.supplier-card {
  margin-bottom: 20px;
}

.supplier-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: 600;
}

.supplier-header i {
  margin-right: 8px;
  font-size: 20px;
  color: #409eff;
}

.low-stock {
  color: #f56c6c;
}

/* 覆盖Element UI样式 */
.detail-tabs >>> .el-tabs__nav-wrap::after {
  height: 1px;
}

.detail-tabs >>> .el-tabs__item {
  height: 40px;
  line-height: 40px;
}

.detail-tabs >>> .el-descriptions__label {
  width: 140px;
  color: #606266;
}

.detail-tabs >>> .el-card__body {
  padding: 15px;
}
</style> 