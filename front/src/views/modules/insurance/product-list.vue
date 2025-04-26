<template>
  <div class="mod-insurance-product">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="searchForm.productName" placeholder="Product Name" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.productType" placeholder="Product Type" clearable>
          <el-option label="Life Insurance" value="Life"></el-option>
          <el-option label="Health Insurance" value="Health"></el-option>
          <el-option label="Property Insurance" value="Property"></el-option>
          <el-option label="Auto Insurance" value="Auto"></el-option>
          <el-option label="Travel Insurance" value="Travel"></el-option>
          <el-option label="Business Insurance" value="Business"></el-option>
          <el-option label="Other" value="Other"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.status" placeholder="Status" clearable>
          <el-option label="Active" :value="1"></el-option>
          <el-option label="Inactive" :value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchForm.provider" placeholder="Provider" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">Search</el-button>
        <el-button @click="resetSearchForm()">Reset</el-button>
        <el-button v-if="isAuth('insurance:product:save')" type="primary" @click="addOrUpdateHandle()">Add</el-button>
        <el-button v-if="isAuth('insurance:product:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">Batch Delete</el-button>
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
        prop="productId"
        header-align="center"
        align="center"
        width="80"
        label="ID">
      </el-table-column>
      <el-table-column
        prop="productCode"
        header-align="center"
        align="center"
        width="120"
        label="Product Code">
      </el-table-column>
      <el-table-column
        prop="productName"
        header-align="center"
        align="left"
        label="Product Name">
      </el-table-column>
      <el-table-column
        prop="productType"
        header-align="center"
        align="center"
        width="120"
        label="Type">
        <template slot-scope="scope">
          <el-tag :type="getProductTypeTagType(scope.row.productType)">{{ scope.row.productType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="provider"
        header-align="center"
        align="center"
        width="150"
        label="Provider">
      </el-table-column>
      <el-table-column
        prop="minPremium"
        header-align="center"
        align="center"
        width="120"
        label="Min Premium">
        <template slot-scope="scope">
          {{ scope.row.minPremium ? '$' + scope.row.minPremium : '–' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="maxCoverage"
        header-align="center"
        align="center"
        width="150"
        label="Max Coverage">
        <template slot-scope="scope">
          {{ scope.row.maxCoverage ? '$' + scope.row.maxCoverage : '–' }}
        </template>
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        width="100"
        label="Status">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? 'Active' : 'Inactive' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        width="180"
        label="Create Time">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="Operations">
        <template slot-scope="scope">
          <el-button v-if="isAuth('insurance:product:info')" type="text" size="small" @click="viewDetailsHandle(scope.row.productId)">View</el-button>
          <el-button v-if="isAuth('insurance:product:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.productId)">Edit</el-button>
          <el-button v-if="isAuth('insurance:product:delete')" type="text" size="small" @click="deleteHandle(scope.row.productId)">Delete</el-button>
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
    
    <!-- Add/Edit dialog -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    
    <!-- View details dialog -->
    <product-detail v-if="detailVisible" ref="productDetail"></product-detail>
  </div>
</template>

<script>
import AddOrUpdate from './product-add-or-update'
import ProductDetail from './product-detail'
import { isAuth } from '@/utils'

export default {
  components: {
    AddOrUpdate,
    ProductDetail
  },
  data () {
    return {
      searchForm: {
        productName: '',
        productType: '',
        provider: '',
        status: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      detailVisible: false
    }
  },
  created () {
    this.getDataList()
  },
  methods: {
    // Check permission for buttons
    isAuth,
    
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
    
    // Get data list
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/insurance/product/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'productName': this.searchForm.productName,
          'productType': this.searchForm.productType,
          'provider': this.searchForm.provider,
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
        productName: '',
        productType: '',
        provider: '',
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
    
    // Multi-select callback
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    
    // View product details
    viewDetailsHandle (id) {
      this.detailVisible = true
      this.$nextTick(() => {
        this.$refs.productDetail.init(id)
      })
    },
    
    // Add / Edit
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    
    // Delete
    deleteHandle (id) {
      let ids = id ? [id] : this.dataListSelections.map(item => {
        return item.productId
      })
      
      this.$confirm(`Are you sure you want to ${id ? 'delete this product' : 'batch delete selected products'}?`, 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/insurance/product/delete' + (id ? `/${id}` : '')),
          method: 'delete',
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
            this.$message.error(data.msg)
          }
        })
      })
    }
  }
}
</script>

<style scoped>
.mod-insurance-product {
  padding: 20px;
}
</style> 