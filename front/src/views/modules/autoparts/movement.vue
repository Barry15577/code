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
            <el-select v-model="dataForm.movementType" placeholder="Movement Type" clearable>
              <el-option label="Stock In" value="STOCK_IN"></el-option>
              <el-option label="Stock Out" value="STOCK_OUT"></el-option>
              <el-option label="Adjustment" value="ADJUSTMENT"></el-option>
              <el-option label="Return" value="RETURN"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="to"
              start-placeholder="Start Date"
              end-placeholder="End Date"
              value-format="yyyy-MM-dd"
              style="width: 240px">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button @click="getDataList()">Search</el-button>
            <el-button v-if="isAuth('autoparts:movement:save')" type="primary" @click="addOrUpdateHandle()">Add Movement</el-button>
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
          prop="movementId" 
          header-align="center" 
          align="center" 
          width="80" 
          label="ID">
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
          prop="movementType" 
          header-align="center" 
          align="center" 
          label="Movement Type">
          <template slot-scope="scope">
            <el-tag 
              :type="scope.row.movementType === 'STOCK_IN' ? 'success' : 
                    scope.row.movementType === 'STOCK_OUT' ? 'danger' : 
                    scope.row.movementType === 'RETURN' ? 'warning' : 'info'">
              {{ scope.row.movementType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column 
          prop="quantityChanged" 
          header-align="center" 
          align="center" 
          label="Quantity Changed">
          <template slot-scope="scope">
            <span :class="{'text-positive': scope.row.quantityChanged > 0, 'text-negative': scope.row.quantityChanged < 0}">
              {{ scope.row.quantityChanged > 0 ? '+' + scope.row.quantityChanged : scope.row.quantityChanged }}
            </span>
          </template>
        </el-table-column>
        <el-table-column 
          prop="previousQuantity" 
          header-align="center" 
          align="center" 
          label="Previous Qty">
        </el-table-column>
        <el-table-column 
          prop="currentQuantity" 
          header-align="center" 
          align="center" 
          label="Current Qty">
        </el-table-column>
        <el-table-column 
          prop="movementDate" 
          header-align="center" 
          align="center" 
          width="150" 
          label="Date">
          <template slot-scope="scope">
            {{ formatDate(scope.row.movementDate) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="reference" 
          header-align="center" 
          align="center" 
          label="Reference">
        </el-table-column>
        <el-table-column 
          fixed="right"
          header-align="center" 
          align="center" 
          width="150" 
          label="Operations">
          <template slot-scope="scope">
            <el-button v-if="isAuth('autoparts:movement:info')" type="text" size="small" @click="viewDetailsHandle(scope.row.movementId)">Details</el-button>
            <el-button v-if="isAuth('autoparts:movement:delete')" type="text" size="small" @click="deleteHandle(scope.row.movementId)">Delete</el-button>
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
      <!-- Movement details -->
      <movement-details v-if="detailsVisible" ref="movementDetails"></movement-details>
    </el-card>
  </div>
</template>

<script>
  import AddOrUpdate from './movement-add-or-update'
  import MovementDetails from './movement-details'
  import { isAuth } from '@/utils'
  
  export default {
    data () {
      return {
        tableHeight: window.innerHeight - 250,
        dataForm: {
          partNumber: '',
          partName: '',
          movementType: ''
        },
        dateRange: [],
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        detailsVisible: false
      }
    },
    components: {
      AddOrUpdate,
      MovementDetails
    },
    created () {
      this.getDataList()
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
      // Format date
      formatDate (date) {
        if (!date) return ''
        let d = new Date(date)
        return d.toLocaleString()
      },
      // Get data list
      getDataList () {
        this.dataListLoading = true
        let params = {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'partNumber': this.dataForm.partNumber,
          'partName': this.dataForm.partName,
          'movementType': this.dataForm.movementType
        }
        
        // Add date range if selected
        if (this.dateRange && this.dateRange.length === 2) {
          params.startDate = this.dateRange[0]
          params.endDate = this.dateRange[1]
        }
        
        this.$http({
          url: this.$http.adornUrl('/autoparts/movement/list'),
          method: 'get',
          params: this.$http.adornParams(params)
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
      addOrUpdateHandle () {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init()
        })
      },
      // View details
      viewDetailsHandle (id) {
        this.detailsVisible = true
        this.$nextTick(() => {
          this.$refs.movementDetails.init(id)
        })
      },
      // Delete
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.movementId
        })
        this.$confirm(`Are you sure you want to delete the selected ${ids.length > 1 ? 'items' : 'item'}?`, 'Warning', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/autoparts/movement/delete'),
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
.text-positive {
  color: #67c23a;
  font-weight: bold;
}
.text-negative {
  color: #f56c6c;
  font-weight: bold;
}
</style> 