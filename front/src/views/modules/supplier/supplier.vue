<template>
  <div class="mod-supplier">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="searchForm.supplierName" placeholder="Supplier Name" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.supplierType" placeholder="Supplier Type" clearable>
          <el-option v-for="type in supplierTypes" :key="type" :label="type" :value="type"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.contactPerson" placeholder="Contact Person" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.status" placeholder="Status" clearable>
          <el-option label="Active" :value="1"></el-option>
          <el-option label="Inactive" :value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">Search</el-button>
        <el-button @click="resetSearchForm()">Reset</el-button>
        <el-button v-if="isAuth('supplier:supplier:save')" type="primary" @click="addOrUpdateHandle()">Add</el-button>
        <el-button v-if="isAuth('supplier:supplier:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">Delete</el-button>
      </el-form-item>
    </el-form>
    
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="supplierId"
        header-align="center"
        align="center"
        width="80"
        label="ID">
      </el-table-column>
      <el-table-column
        prop="supplierCode"
        header-align="center"
        align="center"
        width="120"
        label="Code">
      </el-table-column>
      <el-table-column
        prop="supplierName"
        header-align="center"
        align="left"
        label="Name">
      </el-table-column>
      <el-table-column
        prop="supplierType"
        header-align="center"
        align="center"
        width="120"
        label="Type">
        <template slot-scope="scope">
          <el-tag size="medium" type="info">{{ scope.row.supplierType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="contactPerson"
        header-align="center"
        align="center"
        width="120"
        label="Contact Person">
      </el-table-column>
      <el-table-column
        prop="contactPhone"
        header-align="center"
        align="center"
        width="120"
        label="Phone">
      </el-table-column>
      <el-table-column
        prop="rating"
        header-align="center"
        align="center"
        width="100"
        label="Rating">
        <template slot-scope="scope">
          <el-rate 
            v-model="scope.row.rating" 
            disabled 
            show-score 
            text-color="#ff9900"
            score-template="{value}">
          </el-rate>
        </template>
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        width="100"
        label="Status">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? 'Active' : 'Inactive' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="180"
        label="Operations">
        <template slot-scope="scope">
          <el-button v-if="isAuth('supplier:supplier:info')" type="text" size="small" @click="detailHandle(scope.row.supplierId)">View</el-button>
          <el-button v-if="isAuth('supplier:supplier:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.supplierId)">Edit</el-button>
          <el-button v-if="isAuth('supplier:supplier:delete')" type="text" size="small" class="text-danger" @click="deleteHandle(scope.row.supplierId)">Delete</el-button>
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
    
    <!-- Supplier Detail Dialog -->
    <el-dialog
      title="Supplier Details"
      :close-on-click-modal="false"
      :visible.sync="detailVisible"
      width="70%"
      class="supplier-detail-dialog">
      <div v-if="supplierDetails" class="supplier-detail-container">
        <!-- Header with supplier name and status -->
        <div class="supplier-header">
          <div class="supplier-title">
            <h2>{{ supplierDetails.supplierName }}</h2>
            <el-tag size="medium" :type="supplierDetails.status === 1 ? 'success' : 'danger'">
              {{ supplierDetails.status === 1 ? 'Active' : 'Inactive' }}
            </el-tag>
          </div>
          <div class="supplier-code">
            <span>Code: {{ supplierDetails.supplierCode }}</span>
            <el-rate 
              v-model="supplierDetails.rating" 
              disabled 
              show-score 
              text-color="#ff9900"
              score-template="{value}">
            </el-rate>
          </div>
        </div>
        
        <!-- Supplier Information Sections -->
        <div class="supplier-info-section">
          <div class="section">
            <h3><i class="el-icon-user"></i> Contact Information</h3>
            <div class="section-content">
              <div class="info-item">
                <span class="label">Contact Person:</span>
                <span class="value">{{ supplierDetails.contactPerson || 'Not provided' }}</span>
              </div>
              <div class="info-item">
                <span class="label">Phone:</span>
                <span class="value">{{ supplierDetails.contactPhone || 'Not provided' }}</span>
              </div>
              <div class="info-item">
                <span class="label">Email:</span>
                <span class="value">{{ supplierDetails.contactEmail || 'Not provided' }}</span>
              </div>
              <div class="info-item">
                <span class="label">Website:</span>
                <span class="value">
                  <a v-if="supplierDetails.website" :href="getWebsiteUrl(supplierDetails.website)" target="_blank">{{ supplierDetails.website }}</a>
                  <span v-else>Not provided</span>
                </span>
              </div>
            </div>
          </div>
          
          <div class="section">
            <h3><i class="el-icon-office-building"></i> Company Information</h3>
            <div class="section-content">
              <div class="info-item">
                <span class="label">Type:</span>
                <span class="value">{{ supplierDetails.supplierType || 'Not specified' }}</span>
              </div>
              <div class="info-item">
                <span class="label">Industry:</span>
                <span class="value">{{ supplierDetails.industry || 'Not specified' }}</span>
              </div>
              <div class="info-item">
                <span class="label">Registration #:</span>
                <span class="value">{{ supplierDetails.registrationNumber || 'Not provided' }}</span>
              </div>
              <div class="info-item">
                <span class="label">Address:</span>
                <span class="value">{{ supplierDetails.address || 'Not provided' }}</span>
              </div>
            </div>
          </div>
        </div>
        
        <div class="supplier-info-section">
          <div class="section">
            <h3><i class="el-icon-data-analysis"></i> Business Information</h3>
            <div class="section-content">
              <div class="info-item">
                <span class="label">Established:</span>
                <span class="value">{{ supplierDetails.establishedYear || 'Not provided' }}</span>
              </div>
              <div class="info-item">
                <span class="label">Employees:</span>
                <span class="value">{{ supplierDetails.employeeCount || 'Not provided' }}</span>
              </div>
              <div class="info-item">
                <span class="label">Annual Revenue:</span>
                <span class="value">{{ formatCurrency(supplierDetails.annualRevenue) }}</span>
              </div>
              <div class="info-item">
                <span class="label">Payment Terms:</span>
                <span class="value">{{ supplierDetails.paymentTerms || 'Not specified' }}</span>
              </div>
            </div>
          </div>
          
          <div class="section">
            <h3><i class="el-icon-box"></i> Products & Services</h3>
            <div class="section-content">
              <div class="info-box">
                {{ supplierDetails.mainProducts || 'No product/service information provided' }}
              </div>
            </div>
          </div>
        </div>
        
        <!-- Description Section -->
        <div class="section description-section" v-if="supplierDetails.description">
          <h3><i class="el-icon-document"></i> Description</h3>
          <div class="section-content">
            <div class="info-box">
              {{ supplierDetails.description }}
            </div>
          </div>
        </div>
        
        <!-- Created/Updated Info -->
        <div class="timestamps">
          <span>Created: {{ formatDate(supplierDetails.createTime) }}</span>
          <span v-if="supplierDetails.updateTime && supplierDetails.updateTime !== supplierDetails.createTime">
            | Last updated: {{ formatDate(supplierDetails.updateTime) }}
          </span>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">Close</el-button>
        <el-button v-if="isAuth('supplier:supplier:update')" type="primary" @click="addOrUpdateHandle(supplierDetails.supplierId)">Edit</el-button>
      </div>
    </el-dialog>
    
    <!-- Add or Update Dialog -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from './supplier-add-or-update'
import { isAuth } from '@/utils'
import { formatDate } from '@/utils/date'

export default {
  components: {
    AddOrUpdate
  },
  data () {
    return {
      searchForm: {
        supplierName: '',
        supplierType: '',
        contactPerson: '',
        status: ''
      },
      supplierTypes: ['Product', 'Service', 'Technology', 'Consulting', 'Manufacturing', 'Distribution', 'Other'],
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      detailVisible: false,
      supplierDetails: null
    }
  },
  created () {
    this.getDataList()
  },
  methods: {
    isAuth,
    formatDate,
    
    // Format currency values
    formatCurrency (value) {
      if (!value) return 'Not provided'
      return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(value)
    },
    
    // Get formatted website URL
    getWebsiteUrl (website) {
      if (!website) return '#'
      return website.startsWith('http') ? website : 'https://' + website
    },
    
    // Get data list
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/supplier/supplier/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'supplierName': this.searchForm.supplierName,
          'supplierType': this.searchForm.supplierType,
          'contactPerson': this.searchForm.contactPerson,
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
        supplierName: '',
        supplierType: '',
        contactPerson: '',
        status: ''
      }
      this.getDataList()
    },
    
    // Handle page size change
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    
    // Handle current page change
    currentChangeHandle (val) {
      this.pageIndex = val
      this.getDataList()
    },
    
    // Handle selection change
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    
    // View supplier details
    detailHandle (id) {
      this.$http({
        url: this.$http.adornUrl(`/supplier/supplier/info/${id}`),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.supplierDetails = data.supplier
          this.detailVisible = true
        } else {
          this.$message.error(data.msg || 'Failed to get supplier details')
        }
      })
    },
    
    // Add or update
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    
    // Delete
    deleteHandle (id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.supplierId
      })
      this.$confirm(`Are you sure you want to delete the selected ${ids.length > 1 ? 'items' : 'item'}?`, 'Warning', {
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/supplier/supplier/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: 'Operation successful',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          } else {
            this.$message.error(data.msg || 'Operation failed')
          }
        })
      })
    }
  }
}
</script>

<style scoped>
.mod-supplier {
  padding: 20px;
}

.text-danger {
  color: #F56C6C;
}

/* Supplier Detail Styles */
.supplier-detail-dialog /deep/ .el-dialog__header {
  padding: 20px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.supplier-detail-dialog /deep/ .el-dialog__title {
  font-size: 18px;
  font-weight: 600;
}

.supplier-detail-dialog /deep/ .el-dialog__body {
  padding: 0;
}

.supplier-detail-container {
  padding: 20px;
}

.supplier-header {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.supplier-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.supplier-title h2 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.supplier-code {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #606266;
}

.supplier-info-section {
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

.description-section {
  margin: 0 0 20px 0;
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
  width: 120px;
  color: #606266;
  font-weight: 500;
}

.info-item .value {
  flex: 1;
  color: #303133;
  word-break: break-word;
}

.info-item .value a {
  color: #409EFF;
  text-decoration: none;
}

.info-box {
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 10px;
  min-height: 60px;
  max-height: 150px;
  overflow-y: auto;
  white-space: pre-line;
  line-height: 1.6;
  color: #606266;
}

.timestamps {
  text-align: right;
  color: #909399;
  font-size: 12px;
  margin-top: 20px;
}

.dialog-footer {
  padding: 20px;
  border-top: 1px solid #e4e7ed;
}

@media screen and (max-width: 992px) {
  .supplier-info-section {
    flex-direction: column;
  }
  
  .section {
    margin: 10px 0;
  }
}
</style> 