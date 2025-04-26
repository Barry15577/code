<template>
  <el-dialog
    title="Add Inventory Movement"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="650px">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="160px">
      <el-form-item label="Part" prop="partId">
        <el-select 
          v-model="dataForm.partId" 
          filterable 
          remote 
          reserve-keyword 
          placeholder="Search Part by Number or Name" 
          :remote-method="searchParts" 
          :loading="partLoading"
          style="width: 100%"
          @change="handlePartChange">
          <el-option 
            v-for="part in partOptions" 
            :key="part.partId" 
            :label="`${part.partNumber} - ${part.partName}`" 
            :value="part.partId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Part Information" v-if="selectedPart.partId">
        <el-descriptions :column="1" size="small" border>
          <el-descriptions-item label="Current Stock">
            <span :class="{'text-danger': selectedPart.stockQuantity <= selectedPart.lowStockThreshold}">
              {{ selectedPart.stockQuantity }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="Threshold">{{ selectedPart.lowStockThreshold }}</el-descriptions-item>
        </el-descriptions>
      </el-form-item>
      <el-form-item label="Movement Type" prop="movementType">
        <el-select v-model="dataForm.movementType" placeholder="Select Movement Type" style="width: 100%">
          <el-option label="Stock In" value="STOCK_IN"></el-option>
          <el-option label="Stock Out" value="STOCK_OUT"></el-option>
          <el-option label="Adjustment" value="ADJUSTMENT"></el-option>
          <el-option label="Return" value="RETURN"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Quantity Changed" prop="quantityChanged">
        <el-input-number 
          v-model="dataForm.quantityChanged" 
          :min="dataForm.movementType === 'STOCK_OUT' ? -999 : 1"
          :max="999"
          style="width: 100%">
        </el-input-number>
        <span class="form-help-text" v-if="dataForm.movementType === 'STOCK_OUT' || dataForm.movementType === 'ADJUSTMENT'">
          Use negative number for stock reduction
        </span>
      </el-form-item>
      <el-form-item label="Movement Date" prop="movementDate">
        <el-date-picker
          v-model="dataForm.movementDate"
          type="datetime"
          placeholder="Select date and time"
          style="width: 100%">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="Reference" prop="reference">
        <el-input v-model="dataForm.reference" placeholder="Order or Document Reference"></el-input>
      </el-form-item>
      <el-form-item label="Notes" prop="notes">
        <el-input type="textarea" v-model="dataForm.notes" :rows="3"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">Cancel</el-button>
      <el-button type="primary" @click="dataFormSubmit()">Confirm</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        partLoading: false,
        partOptions: [],
        selectedPart: {},
        dataForm: {
          movementId: 0,
          partId: '',
          movementType: 'STOCK_IN',
          quantityChanged: 1,
          movementDate: new Date(),
          reference: '',
          notes: ''
        },
        dataRule: {
          partId: [
            { required: true, message: 'Please select a part', trigger: 'change' }
          ],
          movementType: [
            { required: true, message: 'Please select movement type', trigger: 'change' }
          ],
          quantityChanged: [
            { required: true, message: 'Please enter quantity', trigger: 'blur' }
          ],
          movementDate: [
            { required: true, message: 'Please select date', trigger: 'change' }
          ]
        }
      }
    },
    watch: {
      'dataForm.movementType': function (val) {
        // Reset quantity when movement type changes
        if (val === 'STOCK_IN' || val === 'RETURN') {
          this.dataForm.quantityChanged = Math.abs(this.dataForm.quantityChanged)
        } else if (val === 'STOCK_OUT') {
          this.dataForm.quantityChanged = -Math.abs(this.dataForm.quantityChanged)
        }
      }
    },
    methods: {
      init () {
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          this.dataForm.movementDate = new Date()
          this.partOptions = []
          this.selectedPart = {}
        })
      },
      // Search parts by number or name
      searchParts (query) {
        if (query) {
          this.partLoading = true
          this.$http({
            url: this.$http.adornUrl('/autoparts/part/search'),
            method: 'get',
            params: this.$http.adornParams({
              'query': query
            })
          }).then(({data}) => {
            this.partLoading = false
            if (data && data.code === 0) {
              this.partOptions = data.list
            }
          })
        } else {
          this.partOptions = []
        }
      },
      // Handle part selection change
      handlePartChange (partId) {
        if (partId) {
          // Find selected part details
          const part = this.partOptions.find(p => p.partId === partId)
          if (part) {
            this.selectedPart = part
          } else {
            // If not found in options, fetch from server
            this.$http({
              url: this.$http.adornUrl(`/autoparts/part/info/${partId}`),
              method: 'get'
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.selectedPart = data.part
              }
            })
          }
        } else {
          this.selectedPart = {}
        }
      },
      // Submit form
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            // Ensure negative for stock out
            if (this.dataForm.movementType === 'STOCK_OUT' && this.dataForm.quantityChanged > 0) {
              this.dataForm.quantityChanged = -this.dataForm.quantityChanged
            }
            
            this.$http({
              url: this.$http.adornUrl('/autoparts/movement/save'),
              method: 'post',
              data: this.$http.adornData({
                'partId': this.dataForm.partId,
                'movementType': this.dataForm.movementType,
                'quantityChanged': this.dataForm.quantityChanged,
                'movementDate': this.dataForm.movementDate,
                'reference': this.dataForm.reference,
                'notes': this.dataForm.notes
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: 'Operation successful',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>

<style scoped>
.text-danger {
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