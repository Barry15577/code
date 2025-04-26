<template>
  <div class="mod-notification">
    <el-card shadow="never" class="aui-card--fill">
      <div class="mod-notification__top">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
          <el-form-item>
            <el-input v-model="dataForm.title" placeholder="Title" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="dataForm.type" placeholder="Type" clearable>
              <el-option label="Info" value="INFO"></el-option>
              <el-option label="Warning" value="WARNING"></el-option>
              <el-option label="Error" value="ERROR"></el-option>
              <el-option label="Success" value="SUCCESS"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="dataForm.priority" placeholder="Priority" clearable>
              <el-option label="Low" value="LOW"></el-option>
              <el-option label="Medium" value="MEDIUM"></el-option>
              <el-option label="High" value="HIGH"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="dataForm.target" placeholder="Target Audience" clearable>
              <el-option label="All Users" value="ALL"></el-option>
              <el-option label="Admins Only" value="ADMIN"></el-option>
              <el-option label="Regular Users" value="USER"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="dataForm.status" placeholder="Status" clearable>
              <el-option label="Enabled" :value="1"></el-option>
              <el-option label="Disabled" :value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="getDataList()">Search</el-button>
            <el-button v-if="isAuth('sys:notification:save')" type="primary" @click="addOrUpdateHandle()">Add</el-button>
            <el-button v-if="isAuth('sys:notification:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">Delete</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="dataList"
        border
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;">
        <el-table-column
          type="selection"
          header-align="center"
          align="center"
          width="50">
        </el-table-column>
        <el-table-column
          prop="title"
          header-align="center"
          align="center"
          label="Title">
        </el-table-column>
        <el-table-column
          prop="type"
          header-align="center"
          align="center"
          width="110"
          label="Type">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 'INFO'" type="info">Info</el-tag>
            <el-tag v-else-if="scope.row.type === 'WARNING'" type="warning">Warning</el-tag>
            <el-tag v-else-if="scope.row.type === 'ERROR'" type="danger">Error</el-tag>
            <el-tag v-else-if="scope.row.type === 'SUCCESS'" type="success">Success</el-tag>
            <span v-else>{{ scope.row.type }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="priority"
          header-align="center"
          align="center"
          width="110"
          label="Priority">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.priority === 'LOW'" type="info" size="small">Low</el-tag>
            <el-tag v-else-if="scope.row.priority === 'MEDIUM'" type="warning" size="small">Medium</el-tag>
            <el-tag v-else-if="scope.row.priority === 'HIGH'" type="danger" size="small">High</el-tag>
            <span v-else>{{ scope.row.priority }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="target"
          header-align="center"
          align="center"
          width="120"
          label="Target">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.target === 'ALL'" type="success" size="small">All Users</el-tag>
            <el-tag v-else-if="scope.row.target === 'ADMIN'" type="danger" size="small">Admins Only</el-tag>
            <el-tag v-else-if="scope.row.target === 'USER'" type="primary" size="small">Regular Users</el-tag>
            <span v-else>{{ scope.row.target }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          header-align="center"
          align="center"
          width="80"
          label="Status">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="statusChangeHandle(scope.row)"
              :disabled="!isAuth('sys:notification:update')">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
          prop="publishTime"
          header-align="center"
          align="center"
          width="170"
          label="Published">
          <template slot-scope="scope">
            {{ scope.row.publishTime ? formatDate(scope.row.publishTime) : 'Not Published' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="expirationTime"
          header-align="center"
          align="center"
          width="170"
          label="Expires">
          <template slot-scope="scope">
            {{ scope.row.expirationTime ? formatDate(scope.row.expirationTime) : 'Never' }}
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          header-align="center"
          align="center"
          width="170"
          label="Operations">
          <template slot-scope="scope">
            <el-button v-if="isAuth('sys:notification:info')" type="text" size="small" @click="previewHandle(scope.row)">Preview</el-button>
            <el-button v-if="isAuth('sys:notification:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">Edit</el-button>
            <el-button v-if="isAuth('sys:notification:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">Delete</el-button>
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

      <!-- Preview dialog -->
      <el-dialog
        title="Notification Preview"
        :visible.sync="previewVisible"
        width="500px">
        <div class="notification-preview">
          <div :class="['notification-header', previewData.type ? previewData.type.toLowerCase() : 'info']">
            <i class="notification-icon" :class="getIconClass()"></i>
            <h3 class="notification-title">{{ previewData.title }}</h3>
          </div>
          <div class="notification-content" v-html="previewData.content"></div>
          <div class="notification-meta">
            <div v-if="previewData.publishTime">Published: {{ formatDate(previewData.publishTime) }}</div>
            <div v-if="previewData.expirationTime">Expires: {{ formatDate(previewData.expirationTime) }}</div>
          </div>
        </div>
      </el-dialog>

      <!-- Add or Update -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </el-card>
  </div>
</template>

<script>
  import AddOrUpdate from './notification-add-or-update'
  import { isAuth } from '@/utils'
  
  export default {
    data () {
      return {
        dataForm: {
          title: '',
          type: '',
          priority: '',
          target: '',
          status: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        previewVisible: false,
        previewData: {}
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getDataList()
    },
    methods: {
      isAuth,
      // Format date
      formatDate (date) {
        if (!date) return ''
        let d = new Date(date)
        return d.toLocaleString()
      },
      // Get icon class based on notification type
      getIconClass () {
        switch (this.previewData.type) {
          case 'INFO': return 'el-icon-info'
          case 'WARNING': return 'el-icon-warning'
          case 'ERROR': return 'el-icon-error'
          case 'SUCCESS': return 'el-icon-success'
          default: return 'el-icon-info'
        }
      },
      // Get data list
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/sys/notification/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'title': this.dataForm.title,
            'type': this.dataForm.type,
            'priority': this.dataForm.priority,
            'target': this.dataForm.target,
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
      // Status change
      statusChangeHandle (row) {
        let url = '/sys/notification/'
        if (row.status === 1) {
          url += 'publish/' + row.id
        } else {
          url += 'unpublish/' + row.id
        }
        
        this.$http({
          url: this.$http.adornUrl(url),
          method: 'post'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: 'Operation successful',
              type: 'success',
              duration: 1500
            })
            this.getDataList()
          } else {
            this.$message.error(data.msg)
            row.status = row.status === 1 ? 0 : 1 // Revert the switch state
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
      // Preview notification
      previewHandle (row) {
        this.previewData = {...row}
        this.previewVisible = true
      },
      // Delete
      deleteHandle (id) {
        let ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`Are you sure you want to ${ids.length > 1 ? 'batch delete' : 'delete'}?`, 'Warning', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/sys/notification/delete'),
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
        }).catch(() => {})
      }
    }
  }
</script>

<style scoped>
.mod-notification {
  position: relative;
}
.notification-preview {
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
}
.notification-header {
  padding: 12px 16px;
  display: flex;
  align-items: center;
  background-color: #f0f9eb;
  border-bottom: 1px solid #e1f3d8;
}
.notification-header.info {
  background-color: #f4f4f5;
  border-bottom-color: #e9e9eb;
}
.notification-header.warning {
  background-color: #fdf6ec;
  border-bottom-color: #faecd8;
}
.notification-header.error {
  background-color: #fef0f0;
  border-bottom-color: #fde2e2;
}
.notification-header.success {
  background-color: #f0f9eb;
  border-bottom-color: #e1f3d8;
}
.notification-icon {
  font-size: 20px;
  margin-right: 10px;
}
.notification-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}
.notification-content {
  padding: 16px;
  min-height: 80px;
  background-color: #fff;
}
.notification-meta {
  padding: 8px 16px;
  background-color: #f9f9f9;
  color: #909399;
  font-size: 12px;
  display: flex;
  justify-content: space-between;
}
</style> 