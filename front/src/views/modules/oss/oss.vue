<template>
  <div class="mod-oss">
    <el-form :inline="true" :model="dataForm">
      <el-form-item>
       <el-button type="primary" @click="configHandle()">Cloud storage configuration</el-button>
<el-button type="primary" @click="uploadHandle()">Upload file</el-button>
<el-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">Batch Delete</el-button>
      </el-form-item>
    </el-form>
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
        prop="id"
        header-align="center"
        align="center"
        width="80"
        label="ID">
      </el-table-column>
      <el-table-column
        prop="url"
        header-align="center"
        align="center"
       label="URL address">
      </el-table-column>
      <el-table-column
        prop="createDate"
        header-align="center"
        align="center"
        width="180"
        label="Creation time">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="Action">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">Delete</el-button>
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
   <!-- Pop-up window, cloud storage configuration -->
<config v-if="configVisible" ref="config"></config>
<!-- Pop-up window, upload file -->
    <upload v-if="uploadVisible" ref="upload" @refreshDataList="getDataList"></upload>
  </div>
</template>

<script>
  import Config from './oss-config'
  import Upload from './oss-upload'
  export default {
    data () {
      return {
        dataForm: {},
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        configVisible: false,
        uploadVisible: false
      }
    },
    components: {
      Config,
      Upload
    },
    activated () {
      this.getDataList()
    },
    methods: {
     // Get the data list
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/sys/oss/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize
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
    // Pages per page
sizeChangeHandle (val) {
this.pageSize = val
this.pageIndex = 1
this.getDataList()
},
// Current page
currentChangeHandle (val) {
this.pageIndex = val
this.getDataList()
},
// Multiple selections
selectionChangeHandle (val) {
this.dataListSelections = val
},
// Cloud storage configuration
configHandle () {
this.configVisible = true
this.$nextTick(() => {
this.$refs.config.init()
})
},
// Upload files
uploadHandle () {
this.uploadVisible = true
this.$nextTick(() => {
this.$refs.upload.init()
})
      },
      // Delete
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`Are you sure you want to perform the [${id ? 'Delete' : 'Batch Delete'}] operation on [id=${ids.join(',')}]?`, 'Prompt', {
confirmButtonText: 'Confirm',
cancelButtonText: 'Cancel',
type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/sys/oss/delete'),
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
