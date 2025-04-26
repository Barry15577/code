<template>
  <div class="customer-list-container">
    <el-card shadow="never" class="aui-card--fill">
      <div slot="header" class="clearfix">
        <span>Customer Management</span>
        <el-button v-if="isAuth('customer:customer:save')" type="primary" size="small" class="add-button" @click="addOrUpdateHandle()">
          <i class="el-icon-plus"></i> Add Customer
        </el-button>
      </div>
      
      <!-- Search Area -->
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()" class="search-form">
        <el-form-item>
          <el-input v-model="dataForm.customerName" placeholder="Customer Name" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="dataForm.email" placeholder="Email" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="dataForm.status" placeholder="Status" clearable>
            <el-option label="Active" value="1"></el-option>
            <el-option label="Inactive" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getDataList()">Search</el-button>
          <el-button @click="resetSearch()">Reset</el-button>
        </el-form-item>
      </el-form>
      
      <!-- Data Table -->
      <el-table
        :data="dataList"
        border
        v-loading="dataListLoading"
        style="width: 100%">
        <el-table-column
          prop="customerId"
          header-align="center"
          align="center"
          width="100"
          label="ID">
        </el-table-column>
        <el-table-column
          prop="customerName"
          header-align="center"
          align="center"
          label="Customer Name">
        </el-table-column>
        <el-table-column
          prop="email"
          header-align="center"
          align="center"
          label="Email">
        </el-table-column>
        <el-table-column
          prop="phone"
          header-align="center"
          align="center"
          label="Phone">
        </el-table-column>
        <el-table-column
          prop="company"
          header-align="center"
          align="center"
          label="Company">
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
          prop="createTime"
          header-align="center"
          align="center"
          width="180"
          label="Created At">
        </el-table-column>
        <el-table-column
          fixed="right"
          header-align="center"
          align="center"
          width="230"
          label="Operations">
          <template slot-scope="scope">
            <el-button v-if="isAuth('customer:customer:view')" type="text" size="small" @click="viewHandle(scope.row.customerId)">View</el-button>
            <el-button v-if="isAuth('customer:customer:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.customerId)">Edit</el-button>
            <el-button v-if="isAuth('customer:customer:delete')" type="text" size="small" class="delete-btn" @click="deleteHandle(scope.row.customerId)">Delete</el-button>
            <el-button v-if="isAuth('customer:contact:list')" type="text" size="small" @click="contactsHandle(scope.row.customerId, scope.row.customerName)">Contacts</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- Pagination -->
      <el-pagination
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
        :current-page="pageIndex"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="totalPage"
        layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>
      
      <!-- Add/Edit Form Dialog -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
      
      <!-- Customer Detail Dialog -->
      <customer-detail v-if="customerDetailVisible" ref="customerDetail"></customer-detail>
      
      <!-- Customer Contacts Dialog -->
      <customer-contacts v-if="customerContactsVisible" ref="customerContacts"></customer-contacts>
    </el-card>
  </div>
</template>

<script>
  import AddOrUpdate from './customer-add-or-update'
  import CustomerDetail from './customer-detail'
  import CustomerContacts from './customer-contacts'
  import { isAuth } from '@/utils'
  export default {
    data () {
      return {
        dataForm: {
          customerName: '',
          email: '',
          status: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        addOrUpdateVisible: false,
        customerDetailVisible: false,
        customerContactsVisible: false
      }
    },
    components: {
      AddOrUpdate,
      CustomerDetail,
      CustomerContacts
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // Check permission for buttons
      isAuth,
      
      // Get customer list data
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/customer/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'customerName': this.dataForm.customerName,
            'email': this.dataForm.email,
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
      
      // Reset search form
      resetSearch () {
        this.dataForm = {
          customerName: '',
          email: '',
          status: ''
        }
        this.getDataList()
      },
      
      // Handle page size change
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      
      // Handle current page change
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      
      // View customer details
      viewHandle (id) {
        this.customerDetailVisible = true
        this.$nextTick(() => {
          this.$refs.customerDetail.init(id)
        })
      },
      
      // Handle add or update operation
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      
      // Handle delete operation
      deleteHandle (id) {
        this.$confirm(`Are you sure you want to delete this customer?`, 'Warning', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`/customer/delete/${id}`),
            method: 'delete'
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
      },
      
      // Handle customer contacts management
      contactsHandle (customerId, customerName) {
        this.customerContactsVisible = true
        this.$nextTick(() => {
          this.$refs.customerContacts.init(customerId, customerName)
        })
      }
    }
  }
</script>

<style lang="scss">
  .customer-list-container {
    .add-button {
      float: right;
    }
    
    .search-form {
      margin-bottom: 20px;
    }
    
    .delete-btn {
      color: #f56c6c;
    }
  }
</style> 