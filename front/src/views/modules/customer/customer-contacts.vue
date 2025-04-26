<template>
  <el-dialog
    :title="`Contact Records - ${customerName}`"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="70%">
    
    <div class="contacts-toolbar">
      <el-button v-if="isAuth('customer:contact:save')" type="primary" @click="addContactRecord()">
        <i class="el-icon-plus"></i> Add Contact Record
      </el-button>
    </div>
    
    <!-- Data Table -->
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      style="width: 100%">
      <el-table-column
        prop="contactId"
        header-align="center"
        align="center"
        width="80"
        label="ID">
      </el-table-column>
      <el-table-column
        prop="contactType"
        header-align="center"
        align="center"
        width="120"
        label="Contact Type">
        <template slot-scope="scope">
          <el-tag :type="getContactTypeTag(scope.row.contactType)">{{ scope.row.contactType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="contactDate"
        header-align="center"
        align="center"
        width="160"
        label="Contact Date">
      </el-table-column>
      <el-table-column
        prop="contactPerson"
        header-align="center"
        align="center"
        width="140"
        label="Contact Person">
      </el-table-column>
      <el-table-column
        prop="summary"
        header-align="center"
        align="left"
        label="Summary">
      </el-table-column>
      <el-table-column
        prop="nextFollowUp"
        header-align="center"
        align="center"
        width="160"
        label="Next Follow-up">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="Operations">
        <template slot-scope="scope">
          <el-button v-if="isAuth('customer:contact:update')" type="text" size="small" @click="editContactRecord(scope.row)">Edit</el-button>
          <el-button v-if="isAuth('customer:contact:delete')" type="text" size="small" class="delete-btn" @click="deleteContactRecord(scope.row.contactId)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- Pagination -->
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    
    <!-- Add/Edit Contact Record Dialog -->
    <el-dialog
      :title="!contactForm.contactId ? 'Add Contact Record' : 'Edit Contact Record'"
      :visible.sync="contactDialogVisible"
      width="50%"
      append-to-body>
      <el-form :model="contactForm" :rules="contactRules" ref="contactForm" label-width="140px">
        <el-form-item label="Contact Type" prop="contactType">
          <el-select v-model="contactForm.contactType" placeholder="Contact Type">
            <el-option label="Call" value="Call"></el-option>
            <el-option label="Email" value="Email"></el-option>
            <el-option label="Meeting" value="Meeting"></el-option>
            <el-option label="Site Visit" value="Site Visit"></el-option>
            <el-option label="Other" value="Other"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Contact Date" prop="contactDate">
          <el-date-picker
            v-model="contactForm.contactDate"
            type="datetime"
            placeholder="Select date and time"
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="Contact Person" prop="contactPerson">
          <el-input v-model="contactForm.contactPerson" placeholder="Contact Person"></el-input>
        </el-form-item>
        <el-form-item label="Summary" prop="summary">
          <el-input type="textarea" v-model="contactForm.summary" placeholder="Summary" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="Next Follow-up" prop="nextFollowUp">
          <el-date-picker
            v-model="contactForm.nextFollowUp"
            type="datetime"
            placeholder="Select date and time"
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="contactDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitContactForm()">Confirm</el-button>
      </span>
    </el-dialog>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">Close</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { isAuth } from '@/utils'
  export default {
    data () {
      return {
        visible: false,
        customerId: null,
        customerName: '',
        dataListLoading: false,
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        
        // Contact record form dialog
        contactDialogVisible: false,
        contactForm: {
          contactId: 0,
          customerId: 0,
          contactType: '',
          contactDate: '',
          contactPerson: '',
          summary: '',
          nextFollowUp: ''
        },
        contactRules: {
          contactType: [
            { required: true, message: 'Please select contact type', trigger: 'change' }
          ],
          contactDate: [
            { required: true, message: 'Please select contact date', trigger: 'change' }
          ],
          contactPerson: [
            { required: true, message: 'Contact person cannot be empty', trigger: 'blur' }
          ],
          summary: [
            { required: true, message: 'Summary cannot be empty', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      // Check permission for buttons
      isAuth,
      
      // Initialize the component with customer ID and name
      init (customerId, customerName) {
        this.customerId = customerId
        this.customerName = customerName || 'Customer'
        this.visible = true
        this.getDataList()
      },
      
      // Get contact records for the customer
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/customer/contact/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'customerId': this.customerId
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
      
      // Get appropriate tag type for contact type
      getContactTypeTag (type) {
        const tagMap = {
          'Call': 'success',
          'Email': 'info',
          'Meeting': 'primary',
          'Site Visit': 'warning',
          'Other': ''
        }
        return tagMap[type] || ''
      },
      
      // Add new contact record
      addContactRecord () {
        this.contactForm = {
          contactId: 0,
          customerId: this.customerId,
          contactType: '',
          contactDate: new Date().toISOString().slice(0, 19).replace('T', ' '),
          contactPerson: '',
          summary: '',
          nextFollowUp: ''
        }
        this.contactDialogVisible = true
        this.$nextTick(() => {
          this.$refs['contactForm'] && this.$refs['contactForm'].resetFields()
        })
      },
      
      // Edit existing contact record
      editContactRecord (record) {
        this.contactForm = {
          contactId: record.contactId,
          customerId: this.customerId,
          contactType: record.contactType,
          contactDate: record.contactDate,
          contactPerson: record.contactPerson,
          summary: record.summary,
          nextFollowUp: record.nextFollowUp
        }
        this.contactDialogVisible = true
        this.$nextTick(() => {
          this.$refs['contactForm'] && this.$refs['contactForm'].resetFields()
        })
      },
      
      // Delete contact record
      deleteContactRecord (contactId) {
        this.$confirm('Are you sure you want to delete this contact record?', 'Warning', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`/customer/contact/delete/${contactId}`),
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
      
      // Submit contact form (add or update)
      submitContactForm () {
        this.$refs['contactForm'].validate((valid) => {
          if (valid) {
            // Determine URL based on add or update operation
            const url = this.contactForm.contactId 
              ? this.$http.adornUrl('/customer/contact/update') 
              : this.$http.adornUrl('/customer/contact/save')
            
            // Determine HTTP method
            const method = this.contactForm.contactId ? 'put' : 'post'
            
            this.$http({
              url: url,
              method: method,
              data: this.$http.adornData(this.contactForm)
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: 'Operation successful',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.contactDialogVisible = false
                    this.getDataList()
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

<style lang="scss">
  .contacts-toolbar {
    margin-bottom: 20px;
  }
  
  .delete-btn {
    color: #f56c6c;
  }
</style> 