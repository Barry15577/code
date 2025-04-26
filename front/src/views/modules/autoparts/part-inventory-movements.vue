<template>
  <div>
    <div class="filter-container">
      <el-form :inline="true" :model="dataForm">
        <el-form-item>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="to"
            start-placeholder="Start Date"
            end-placeholder="End Date"
            value-format="yyyy-MM-dd">
          </el-date-picker>
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
          <el-button @click="getDataList">Search</el-button>
          <el-button v-if="isAuth('autoparts:movement:save')" type="primary" @click="addMovementHandle">Add Movement</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      style="width: 100%">
      <el-table-column
        prop="movementId"
        header-align="center"
        align="center"
        width="80"
        label="ID">
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
        label="Previous Quantity">
      </el-table-column>
      <el-table-column
        prop="currentQuantity"
        header-align="center"
        align="center"
        label="Current Quantity">
      </el-table-column>
      <el-table-column
        prop="movementDate"
        header-align="center"
        align="center"
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
        prop="createdBy"
        header-align="center"
        align="center"
        label="Created By">
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
      :page-sizes="[10, 20, 50]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    
    <!-- Movement form dialog -->
    <el-dialog
      title="Add Inventory Movement"
      :visible.sync="dialogVisible"
      width="500px">
      <el-form :model="movementForm" :rules="movementRules" ref="movementForm" label-width="150px">
        <el-form-item label="Movement Type" prop="movementType">
          <el-select v-model="movementForm.movementType" placeholder="Select Movement Type" style="width: 100%">
            <el-option label="Stock In" value="STOCK_IN"></el-option>
            <el-option label="Stock Out" value="STOCK_OUT"></el-option>
            <el-option label="Adjustment" value="ADJUSTMENT"></el-option>
            <el-option label="Return" value="RETURN"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Quantity Changed" prop="quantityChanged">
          <el-input-number 
            v-model="movementForm.quantityChanged" 
            :min="movementForm.movementType === 'STOCK_OUT' ? -999 : 1"
            :max="999"
            style="width: 100%">
          </el-input-number>
          <span class="form-help-text" v-if="movementForm.movementType === 'STOCK_OUT' || movementForm.movementType === 'ADJUSTMENT'">
            Use negative number for stock reduction
          </span>
        </el-form-item>
        <el-form-item label="Reference" prop="reference">
          <el-input v-model="movementForm.reference" placeholder="Order or Document Reference"></el-input>
        </el-form-item>
        <el-form-item label="Notes" prop="notes">
          <el-input type="textarea" v-model="movementForm.notes" :rows="3"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitMovement">Confirm</el-button>
      </span>
    </el-dialog>
    
    <!-- Movement details dialog -->
    <el-dialog
      title="Movement Details"
      :visible.sync="detailsVisible"
      width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="Movement ID">{{ movementDetails.movementId }}</el-descriptions-item>
        <el-descriptions-item label="Part ID">{{ movementDetails.partId }}</el-descriptions-item>
        <el-descriptions-item label="Part Number">{{ movementDetails.partNumber }}</el-descriptions-item>
        <el-descriptions-item label="Part Name">{{ movementDetails.partName }}</el-descriptions-item>
        <el-descriptions-item label="Movement Type">
          <el-tag 
            :type="movementDetails.movementType === 'STOCK_IN' ? 'success' : 
                  movementDetails.movementType === 'STOCK_OUT' ? 'danger' : 
                  movementDetails.movementType === 'RETURN' ? 'warning' : 'info'">
            {{ movementDetails.movementType }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Quantity Changed">
          <span :class="{'text-positive': movementDetails.quantityChanged > 0, 'text-negative': movementDetails.quantityChanged < 0}">
            {{ movementDetails.quantityChanged > 0 ? '+' + movementDetails.quantityChanged : movementDetails.quantityChanged }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="Previous Quantity">{{ movementDetails.previousQuantity }}</el-descriptions-item>
        <el-descriptions-item label="Current Quantity">{{ movementDetails.currentQuantity }}</el-descriptions-item>
        <el-descriptions-item label="Movement Date">{{ formatDate(movementDetails.movementDate) }}</el-descriptions-item>
        <el-descriptions-item label="Reference">{{ movementDetails.reference }}</el-descriptions-item>
        <el-descriptions-item label="Notes">{{ movementDetails.notes }}</el-descriptions-item>
        <el-descriptions-item label="Created By">{{ movementDetails.createdBy }}</el-descriptions-item>
        <el-descriptions-item label="Create Time">{{ formatDate(movementDetails.createTime) }}</el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailsVisible = false">Close</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import { isAuth } from '@/utils'
  export default {
    props: {
      partId: {
        type: [Number, String],
        required: true
      }
    },
    data () {
      return {
        dataForm: {
          movementType: ''
        },
        dateRange: [],
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dialogVisible: false,
        detailsVisible: false,
        movementForm: {
          partId: '',
          movementType: 'STOCK_IN',
          quantityChanged: 1,
          reference: '',
          notes: ''
        },
        movementDetails: {},
        movementRules: {
          movementType: [
            { required: true, message: 'Please select movement type', trigger: 'change' }
          ],
          quantityChanged: [
            { required: true, message: 'Please enter quantity', trigger: 'blur' }
          ]
        }
      }
    },
    created () {
      this.getDataList()
    },
    methods: {
      isAuth,
      // Get movement list
      getDataList () {
        this.dataListLoading = true
        let params = {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'partId': this.partId,
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
      // Format date
      formatDate (date) {
        if (!date) return ''
        let d = new Date(date)
        return d.toLocaleString()
      },
      // Add movement
      addMovementHandle () {
        this.movementForm = {
          partId: this.partId,
          movementType: 'STOCK_IN',
          quantityChanged: 1,
          reference: '',
          notes: ''
        }
        this.dialogVisible = true
        this.$nextTick(() => {
          this.$refs['movementForm'].resetFields()
        })
      },
      // Submit movement
      submitMovement () {
        this.$refs['movementForm'].validate((valid) => {
          if (valid) {
            // Ensure negative for stock out
            if (this.movementForm.movementType === 'STOCK_OUT' && this.movementForm.quantityChanged > 0) {
              this.movementForm.quantityChanged = -this.movementForm.quantityChanged
            }
            
            this.$http({
              url: this.$http.adornUrl('/autoparts/movement/save'),
              method: 'post',
              data: this.$http.adornData(this.movementForm)
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: 'Operation successful',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.dialogVisible = false
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
      // View details
      viewDetailsHandle (id) {
        this.$http({
          url: this.$http.adornUrl(`/autoparts/movement/info/${id}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.movementDetails = data.inventoryMovement
            this.detailsVisible = true
          }
        })
      },
      // Delete
      deleteHandle (id) {
        this.$confirm(`Are you sure you want to delete this movement record?`, 'Warning', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/autoparts/movement/delete'),
            method: 'post',
            data: this.$http.adornData([id], false)
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
.filter-container {
  margin-bottom: 20px;
}
.text-positive {
  color: #67c23a;
  font-weight: bold;
}
.text-negative {
  color: #f56c6c;
  font-weight: bold;
}
.form-help-text {
  color: #909399;
  font-size: 12px;
  line-height: 1;
  padding-top: 4px;
}
</style> 