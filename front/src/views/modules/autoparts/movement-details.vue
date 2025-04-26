<template>
  <el-dialog
    title="Inventory Movement Details"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="600px">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="Movement ID">{{ movementDetails.movementId }}</el-descriptions-item>
      <el-descriptions-item label="Part Information">
        <span v-if="movementDetails.partId">
          <el-link 
            type="primary" 
            :underline="false"
            @click="viewPartDetails(movementDetails.partId)">
            {{ movementDetails.partNumber }} - {{ movementDetails.partName }}
          </el-link>
        </span>
      </el-descriptions-item>
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
      <el-button @click="visible = false">Close</el-button>
      <el-button v-if="isAuth('autoparts:movement:delete')" type="danger" @click="deleteHandle(movementDetails.movementId)">Delete</el-button>
    </span>
    
    <!-- Part details dialog -->
    <part-details v-if="partDetailsVisible" ref="partDetails"></part-details>
  </el-dialog>
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
        visible: false,
        partDetailsVisible: false,
        movementDetails: {
          movementId: '',
          partId: '',
          partNumber: '',
          partName: '',
          movementType: '',
          quantityChanged: 0,
          previousQuantity: 0,
          currentQuantity: 0,
          movementDate: '',
          reference: '',
          notes: '',
          createdBy: '',
          createTime: ''
        }
      }
    },
    methods: {
      isAuth,
      init (id) {
        this.visible = true
        this.$nextTick(() => {
          this.getMovementDetails(id)
        })
      },
      // Get movement details
      getMovementDetails (id) {
        this.$http({
          url: this.$http.adornUrl(`/autoparts/movement/info/${id}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.movementDetails = data.inventoryMovement
          }
        })
      },
      // Format date
      formatDate (date) {
        if (!date) return ''
        let d = new Date(date)
        return d.toLocaleString()
      },
      // View part details
      viewPartDetails (partId) {
        this.partDetailsVisible = true
        this.$nextTick(() => {
          this.$refs.partDetails.init(partId)
        })
      },
      // Delete movement
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
                  this.visible = false
                  this.$emit('refreshDataList')
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
.text-positive {
  color: #67c23a;
  font-weight: bold;
}
.text-negative {
  color: #f56c6c;
  font-weight: bold;
}
</style> 