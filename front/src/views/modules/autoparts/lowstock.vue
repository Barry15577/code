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
            <el-button @click="getDataList()">Search</el-button>
            <el-button v-if="isAuth('autoparts:lowstock:export')" type="success" @click="exportLowStock()">Export</el-button>
            <el-button type="primary" @click="restockAllHandle()">Restock Selected</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <el-alert
        title="Low Stock Alert"
        type="warning"
        :closable="false"
        show-icon>
        <span>The following parts are below their stock threshold levels and require attention.</span>
      </el-alert>
      
      <el-table
        :data="dataList"
        border
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%; margin-top: 15px;"
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
          label="Current Stock"
          sortable>
          <template slot-scope="scope">
            <el-progress 
              :percentage="calculateStockPercentage(scope.row)" 
              :status="getProgressStatus(scope.row)"
              :stroke-width="18"
              :format="() => scope.row.stockQuantity">
            </el-progress>
          </template>
        </el-table-column>
        <el-table-column 
          prop="lowStockThreshold" 
          header-align="center" 
          align="center" 
          label="Threshold"
          sortable>
        </el-table-column>
        <el-table-column 
          header-align="center" 
          align="center" 
          label="Missing"
          sortable>
          <template slot-scope="scope">
            {{ Math.max(scope.row.lowStockThreshold - scope.row.stockQuantity, 0) }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="sellingPrice" 
          header-align="center" 
          align="center" 
          label="Price">
          <template slot-scope="scope">
            {{ scope.row.sellingPrice ? '$' + scope.row.sellingPrice : '' }}
          </template>
        </el-table-column>
        <el-table-column 
          prop="supplierName" 
          header-align="center" 
          align="center" 
          label="Supplier">
        </el-table-column>
        <el-table-column 
          fixed="right"
          header-align="center" 
          align="center" 
          width="150" 
          label="Operations">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="viewDetailsHandle(scope.row.partId)">Details</el-button>
            <el-button type="text" size="small" @click="restockHandle(scope.row)">Restock</el-button>
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
      
      <!-- Restock dialog -->
      <el-dialog
        title="Restock Inventory"
        :visible.sync="restockVisible"
        width="600px">
        <el-form :model="restockForm" :rules="restockRules" ref="restockForm" label-width="150px">
          <el-form-item label="Part" prop="partName">
            <el-input v-model="restockForm.partName" disabled></el-input>
          </el-form-item>
          <el-form-item label="Current Stock">
            <el-input-number v-model="restockForm.currentStock" disabled></el-input-number>
          </el-form-item>
          <el-form-item label="Threshold">
            <el-input-number v-model="restockForm.threshold" disabled></el-input-number>
          </el-form-item>
          <el-form-item label="Quantity to Add" prop="quantityToAdd">
            <el-input-number 
              v-model="restockForm.quantityToAdd" 
              :min="1"
              :step="1"
              :precision="0"
              :placeholder="`Minimum ${restockForm.threshold - restockForm.currentStock}`"
              style="width: 100%">
            </el-input-number>
          </el-form-item>
          <el-form-item label="Reference" prop="reference">
            <el-input v-model="restockForm.reference" placeholder="Order or Document Reference"></el-input>
          </el-form-item>
          <el-form-item label="Notes" prop="notes">
            <el-input type="textarea" v-model="restockForm.notes" :rows="3"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="restockVisible = false">Cancel</el-button>
          <el-button type="primary" @click="submitRestock">Confirm</el-button>
        </span>
      </el-dialog>
      
      <!-- Batch restock dialog -->
      <el-dialog
        title="Batch Restock Selected Parts"
        :visible.sync="batchRestockVisible"
        width="650px">
        <div v-if="dataListSelections.length === 0" class="no-selection-message">
          <i class="el-icon-warning"></i>
          <span>Please select parts to restock.</span>
        </div>
        <div v-else>
          <el-alert
            title="You are about to restock the following parts"
            type="info"
            :closable="false"
            show-icon>
          </el-alert>
          
          <el-table
            :data="dataListSelections"
            style="width: 100%; margin-top: 15px;"
            max-height="300px">
            <el-table-column 
              prop="partNumber" 
              label="Part Number">
            </el-table-column>
            <el-table-column 
              prop="partName" 
              label="Part Name">
            </el-table-column>
            <el-table-column 
              prop="stockQuantity" 
              label="Current Stock">
            </el-table-column>
            <el-table-column 
              prop="lowStockThreshold" 
              label="Threshold">
            </el-table-column>
            <el-table-column 
              label="To Add">
              <template slot-scope="scope">
                <el-input-number 
                  v-model="batchRestockQuantities[scope.row.partId]" 
                  :min="1"
                  :step="1"
                  :precision="0"
                  size="mini"
                  controls-position="right"
                  @change="val => updateBatchRestockQuantity(scope.row.partId, val)">
                </el-input-number>
              </template>
            </el-table-column>
          </el-table>
          
          <el-form :model="batchRestockForm" :rules="restockRules" ref="batchRestockForm" label-width="150px" style="margin-top: 20px;">
            <el-form-item label="Reference" prop="reference">
              <el-input v-model="batchRestockForm.reference" placeholder="Order or Document Reference"></el-input>
            </el-form-item>
            <el-form-item label="Notes" prop="notes">
              <el-input type="textarea" v-model="batchRestockForm.notes" :rows="3"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="batchRestockVisible = false">Cancel</el-button>
          <el-button type="primary" @click="submitBatchRestock" :disabled="dataListSelections.length === 0">Confirm</el-button>
        </span>
      </el-dialog>
      
      <!-- Part details -->
      <part-details v-if="detailsVisible" ref="partDetails"></part-details>
    </el-card>
  </div>
</template>

<script>
  import PartDetails from './part-details'
  import { isAuth } from '@/utils'
  
  export default {
    components: {
      PartDetails
    },
    data () {
      return {
        tableHeight: window.innerHeight - 250,
        dataForm: {
          partNumber: '',
          partName: '',
          category: '',
          manufacturer: ''
        },
        categoryOptions: [],
        manufacturerOptions: [],
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        detailsVisible: false,
        restockVisible: false,
        batchRestockVisible: false,
        restockForm: {
          partId: '',
          partName: '',
          currentStock: 0,
          threshold: 0,
          quantityToAdd: 0,
          reference: '',
          notes: ''
        },
        batchRestockForm: {
          reference: '',
          notes: ''
        },
        batchRestockQuantities: {},
        restockRules: {
          quantityToAdd: [
            { required: true, message: 'Please enter quantity to add', trigger: 'blur' },
            { type: 'number', min: 1, message: 'Quantity must be at least 1', trigger: 'blur' }
          ]
        }
      }
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
      // Calculate stock percentage for progress bar
      calculateStockPercentage (row) {
        if (row.lowStockThreshold === 0) return 0
        const percentage = (row.stockQuantity / row.lowStockThreshold) * 100
        return Math.min(percentage, 100)
      },
      // Get progress bar status
      getProgressStatus (row) {
        const percentage = this.calculateStockPercentage(row)
        if (percentage < 30) return 'exception'
        if (percentage < 80) return 'warning'
        return 'success'
      },
      // Get data list - only low stock items
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/autoparts/part/lowstock'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'partNumber': this.dataForm.partNumber,
            'partName': this.dataForm.partName,
            'category': this.dataForm.category,
            'manufacturer': this.dataForm.manufacturer
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
        // Initialize batch restock quantities
        this.batchRestockQuantities = {}
        val.forEach(item => {
          const recommended = Math.max(item.lowStockThreshold - item.stockQuantity, 1)
          this.batchRestockQuantities[item.partId] = recommended
        })
      },
      // Update batch restock quantity
      updateBatchRestockQuantity (partId, value) {
        this.batchRestockQuantities[partId] = value
      },
      // View details
      viewDetailsHandle (id) {
        this.detailsVisible = true
        this.$nextTick(() => {
          this.$refs.partDetails.init(id)
        })
      },
      // Restock handle
      restockHandle (row) {
        this.restockForm = {
          partId: row.partId,
          partName: row.partName,
          currentStock: row.stockQuantity,
          threshold: row.lowStockThreshold,
          quantityToAdd: Math.max(row.lowStockThreshold - row.stockQuantity, 1),
          reference: '',
          notes: 'Restocked due to low inventory alert'
        }
        this.restockVisible = true
        this.$nextTick(() => {
          this.$refs['restockForm'].resetFields()
        })
      },
      // Submit restock
      submitRestock () {
        this.$refs['restockForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl('/autoparts/movement/save'),
              method: 'post',
              data: this.$http.adornData({
                'partId': this.restockForm.partId,
                'movementType': 'STOCK_IN',
                'quantityChanged': this.restockForm.quantityToAdd,
                'reference': this.restockForm.reference,
                'notes': this.restockForm.notes,
                'movementDate': new Date()
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: 'Restock operation successful',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.restockVisible = false
                    this.getDataList()
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      },
      // Batch restock handle
      restockAllHandle () {
        if (this.dataListSelections.length === 0) {
          this.$message({
            message: 'Please select items to restock',
            type: 'warning'
          })
          return
        }
        
        this.batchRestockForm = {
          reference: '',
          notes: 'Batch restock due to low inventory alert'
        }
        this.batchRestockVisible = true
        this.$nextTick(() => {
          if (this.$refs['batchRestockForm']) {
            this.$refs['batchRestockForm'].resetFields()
          }
        })
      },
      // Submit batch restock
      submitBatchRestock () {
        this.$refs['batchRestockForm'].validate((valid) => {
          if (valid) {
            const movementRequests = this.dataListSelections.map(item => {
              return {
                'partId': item.partId,
                'movementType': 'STOCK_IN',
                'quantityChanged': this.batchRestockQuantities[item.partId] || 1,
                'reference': this.batchRestockForm.reference,
                'notes': this.batchRestockForm.notes,
                'movementDate': new Date()
              }
            })
            
            this.$http({
              url: this.$http.adornUrl('/autoparts/movement/batch'),
              method: 'post',
              data: this.$http.adornData(movementRequests)
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: 'Batch restock operation successful',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.batchRestockVisible = false
                    this.getDataList()
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      },
      // Export low stock data
      exportLowStock () {
        let params = {
          'partNumber': this.dataForm.partNumber,
          'partName': this.dataForm.partName,
          'category': this.dataForm.category,
          'manufacturer': this.dataForm.manufacturer
        }
        
        // Create a download link for the export
        let url = this.$http.adornUrl('/autoparts/part/lowstock/export')
        let queryParams = this.$http.adornParams(params)
        let queryString = Object.keys(queryParams).map(key => {
          return encodeURIComponent(key) + '=' + encodeURIComponent(queryParams[key])
        }).join('&')
        
        window.location.href = url + '?' + queryString
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
.no-selection-message {
  text-align: center;
  padding: 30px;
  color: #909399;
}
.no-selection-message i {
  font-size: 40px;
  margin-bottom: 10px;
  display: block;
}
</style> 