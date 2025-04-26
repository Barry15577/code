<template>
  <div class="mod-config">
    <el-card shadow="never" class="aui-card--fill">
      <div class="mod-config__top">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
          <el-form-item>
            <el-input v-model="dataForm.partNumber" placeholder="Part Number" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="dataForm.partName" placeholder="Part Name" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="dataForm.category" placeholder="Category" clearable>
              <el-option v-for="category in categoryOptions" :key="category" :label="category" :value="category"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="dataForm.manufacturer" placeholder="Manufacturer" clearable>
              <el-option v-for="manufacturer in manufacturerOptions" :key="manufacturer" :label="manufacturer" :value="manufacturer"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="dataForm.status" placeholder="Status" clearable>
              <el-option label="Active" :value="1"></el-option>
              <el-option label="Inactive" :value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="getDataList()">Search</el-button>
            <el-button v-if="isAuth('autoparts:part:save')" type="primary" @click="addOrUpdateHandle()">Add</el-button>
            <el-button v-if="isAuth('autoparts:part:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">Batch Delete</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="dataList"
        border
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;"
        :max-height="tableHeight">
        <el-table-column 
          type="selection" 
          header-align="center" 
          align="center" 
          width="50">
        </el-table-column>
        <el-table-column 
          prop="partNumber" 
          header-align="center" 
          align="center" 
          label="Part Number">
        </el-table-column>
        <el-table-column 
          prop="partName" 
          header-align="center" 
          align="center" 
          label="Part Name">
        </el-table-column>
        <el-table-column 
          prop="category" 
          header-align="center" 
          align="center" 
          label="Category">
        </el-table-column>
        <el-table-column 
          prop="manufacturer" 
          header-align="center" 
          align="center" 
          label="Manufacturer">
        </el-table-column>
        <el-table-column 
          prop="stockQuantity" 
          header-align="center" 
          align="center" 
          label="Stock Quantity">
          <template slot-scope="scope">
            <span :class="{'text-danger': scope.row.stockQuantity <= scope.row.lowStockThreshold}">
              {{ scope.row.stockQuantity }}
            </span>
          </template>
        </el-table-column>
        <el-table-column 
          prop="sellingPrice" 
          header-align="center" 
          align="center" 
          label="Selling Price">
          <template slot-scope="scope">
            {{ scope.row.sellingPrice ? '$' + scope.row.sellingPrice : '' }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="status" 
          header-align="center" 
          align="center" 
          label="Status">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 1" type="success">Active</el-tag>
            <el-tag v-else type="info">Inactive</el-tag>
          </template>
        </el-table-column>
        <el-table-column 
          fixed="right"
          header-align="center" 
          align="center" 
          width="180" 
          label="Operations">
          <template slot-scope="scope">
            <el-button v-if="isAuth('autoparts:part:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.partId)">Edit</el-button>
            <el-button v-if="isAuth('autoparts:part:info')" type="text" size="small" @click="viewDetailsHandle(scope.row.partId)">Details</el-button>
            <el-button v-if="isAuth('autoparts:part:delete')" type="text" size="small" @click="deleteHandle(scope.row.partId)">Delete</el-button>
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
      <!-- Add/Edit form -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
      <!-- Part details -->
      <part-details v-if="detailsVisible" ref="partDetails"></part-details>
    </el-card>
  </div>
</template>

<script>
  import AddOrUpdate from './part-add-or-update'
  import PartDetails from './part-details'
  import { isAuth } from '@/utils'
  export default {
    data () {
      return {
        tableHeight: window.innerHeight - 250,
        dataForm: {
          partNumber: '',
          partName: '',
          category: '',
          manufacturer: '',
          status: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        detailsVisible: false,
        categoryOptions: [],
        manufacturerOptions: []
      }
    },
    components: {
      AddOrUpdate,
      PartDetails
    },
    created () {
      this.getDataList()
      this.getCategoryOptions()
      this.getManufacturerOptions()
      window.addEventListener('resize', this.handleResize)
    },
    beforeDestroy () {
      window.removeEventListener('resize', this.handleResize)
    },
    methods: {
      isAuth,
      handleResize () {
        this.tableHeight = window.innerHeight - 250
      },
      // Get categories for dropdown
      getCategoryOptions () {
        this.$http({
          url: this.$http.adornUrl('/autoparts/part/categories'),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.categoryOptions = data.categories
          }
        })
      },
      // Get manufacturers for dropdown
      getManufacturerOptions () {
        this.$http({
          url: this.$http.adornUrl('/autoparts/part/manufacturers'),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.manufacturerOptions = data.manufacturers
          }
        })
      },
      // Get data list
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/autoparts/part/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'partNumber': this.dataForm.partNumber,
            'partName': this.dataForm.partName,
            'category': this.dataForm.category,
            'manufacturer': this.dataForm.manufacturer,
            'status': this.dataForm.status
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
      // Size change
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // Current change
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // Selection change
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // Add / Edit
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // View details
      viewDetailsHandle (id) {
        this.detailsVisible = true
        this.$nextTick(() => {
          this.$refs.partDetails.init(id)
        })
      },
      // Delete
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.partId
        })
        this.$confirm(`Are you sure you want to delete the selected ${ids.length > 1 ? 'items' : 'item'}?`, 'Warning', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/autoparts/part/delete'),
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
              this.$message.error(data.msg)
            }
          })
        })
      }
    }
  }
</script>

<style scoped>
.mod-config {
  min-height: calc(100vh - 50px);
  background-color: #f0f2f5;
  padding: 10px;
}
.mod-config__top {
  margin-bottom: 15px;
}
.text-danger {
  color: #f56c6c;
  font-weight: bold;
}
</style> 