<template>
  <el-dialog
    :title="!dataForm.id ? 'Add New Notification' : 'Edit Notification'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="700px">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
      <el-form-item label="Title" prop="title">
        <el-input v-model="dataForm.title" placeholder="Notification Title"></el-input>
      </el-form-item>
      <el-form-item label="Type" prop="type">
        <el-select v-model="dataForm.type" placeholder="Type" style="width: 100%">
          <el-option label="Info" value="INFO"></el-option>
          <el-option label="Warning" value="WARNING"></el-option>
          <el-option label="Error" value="ERROR"></el-option>
          <el-option label="Success" value="SUCCESS"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Priority" prop="priority">
        <el-select v-model="dataForm.priority" placeholder="Priority" style="width: 100%">
          <el-option label="Low" value="LOW"></el-option>
          <el-option label="Medium" value="MEDIUM"></el-option>
          <el-option label="High" value="HIGH"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Target Audience" prop="target">
        <el-select v-model="dataForm.target" placeholder="Target Audience" style="width: 100%">
          <el-option label="All Users" value="ALL"></el-option>
          <el-option label="Admins Only" value="ADMIN"></el-option>
          <el-option label="Regular Users" value="USER"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Content" prop="content">
        <el-input type="textarea" v-model="dataForm.content" :rows="5" placeholder="Notification Content"></el-input>
      </el-form-item>
      <el-form-item label="Status" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="1">Enabled</el-radio>
          <el-radio :label="0">Disabled</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="Expiration Date" prop="expirationTime">
        <el-date-picker
          v-model="dataForm.expirationTime"
          type="datetime"
          placeholder="Select Expiration Date"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%">
        </el-date-picker>
        <div class="form-tip">Leave blank for notifications that never expire</div>
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
        dataForm: {
          id: 0,
          title: '',
          content: '',
          type: 'INFO',
          priority: 'MEDIUM',
          target: 'ALL',
          status: 0,
          expirationTime: ''
        },
        dataRule: {
          title: [
            { required: true, message: 'Title cannot be empty', trigger: 'blur' }
          ],
          content: [
            { required: true, message: 'Content cannot be empty', trigger: 'blur' }
          ],
          type: [
            { required: true, message: 'Please select a type', trigger: 'change' }
          ],
          priority: [
            { required: true, message: 'Please select a priority', trigger: 'change' }
          ],
          target: [
            { required: true, message: 'Please select a target audience', trigger: 'change' }
          ],
          status: [
            { required: true, message: 'Please select a status', trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/sys/notification/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm = {
                  ...this.dataForm,
                  ...data.notification
                }
              }
            })
          }
        })
      },
      // Form submit
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/sys/notification/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'title': this.dataForm.title,
                'content': this.dataForm.content,
                'type': this.dataForm.type,
                'priority': this.dataForm.priority,
                'target': this.dataForm.target,
                'status': this.dataForm.status,
                'expirationTime': this.dataForm.expirationTime
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
.form-tip {
  font-size: 12px;
  color: #909399;
  line-height: 1.2;
  padding-top: 4px;
}
</style> 