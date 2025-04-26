<template>
  <el-dialog
    :title="!dataForm.supplierId ? 'Add Supplier' : 'Edit Supplier'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="70%">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="150px" class="form-container">
      <el-tabs type="card">
        <!-- Basic Information -->
        <el-tab-pane label="Basic Information">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Supplier Code" prop="supplierCode">
                <el-input v-model="dataForm.supplierCode" placeholder="Supplier Code" :disabled="!!dataForm.supplierId"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Supplier Name" prop="supplierName">
                <el-input v-model="dataForm.supplierName" placeholder="Supplier Name"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Supplier Type" prop="supplierType">
                <el-select v-model="dataForm.supplierType" placeholder="Select Supplier Type" style="width: 100%">
                  <el-option
                    v-for="type in supplierTypes"
                    :key="type"
                    :label="type"
                    :value="type">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Industry" prop="industry">
                <el-input v-model="dataForm.industry" placeholder="Industry"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Status" prop="status">
                <el-radio-group v-model="dataForm.status">
                  <el-radio :label="1">Active</el-radio>
                  <el-radio :label="0">Inactive</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Rating" prop="rating">
                <el-rate 
                  v-model="dataForm.rating" 
                  show-score 
                  text-color="#ff9900"
                  :max="5"
                  score-template="{value}">
                </el-rate>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="Address" prop="address">
                <el-input v-model="dataForm.address" placeholder="Address"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        
        <!-- Contact Information -->
        <el-tab-pane label="Contact Information">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Contact Person" prop="contactPerson">
                <el-input v-model="dataForm.contactPerson" placeholder="Contact Person"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Contact Phone" prop="contactPhone">
                <el-input v-model="dataForm.contactPhone" placeholder="Contact Phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Contact Email" prop="contactEmail">
                <el-input v-model="dataForm.contactEmail" placeholder="Contact Email"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Website" prop="website">
                <el-input v-model="dataForm.website" placeholder="Website URL"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        
        <!-- Business Information -->
        <el-tab-pane label="Business Information">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Registration #" prop="registrationNumber">
                <el-input v-model="dataForm.registrationNumber" placeholder="Registration Number"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Established Year" prop="establishedYear">
                <el-input-number v-model="dataForm.establishedYear" :min="1900" :max="new Date().getFullYear()" style="width: 100%"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Employee Count" prop="employeeCount">
                <el-input-number v-model="dataForm.employeeCount" :min="0" style="width: 100%"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Annual Revenue" prop="annualRevenue">
                <el-input-number 
                  v-model="dataForm.annualRevenue" 
                  :min="0" 
                  :precision="2" 
                  :step="1000" 
                  style="width: 100%">
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="Payment Terms" prop="paymentTerms">
                <el-input v-model="dataForm.paymentTerms" placeholder="Payment Terms"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="Main Products/Services" prop="mainProducts">
                <el-input
                  type="textarea"
                  :rows="4"
                  placeholder="Main products or services offered"
                  v-model="dataForm.mainProducts">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        
        <!-- Additional Information -->
        <el-tab-pane label="Additional Information">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="Description" prop="description">
                <el-input
                  type="textarea"
                  :rows="8"
                  placeholder="Description or additional notes"
                  v-model="dataForm.description">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">Cancel</el-button>
      <el-button type="primary" @click="dataFormSubmit()">Submit</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    // Validate supplier code
    const validateSupplierCode = (rule, value, callback) => {
      if (this.dataForm.supplierId) {
        callback()
        return
      }
      if (!value) {
        callback(new Error('Please enter supplier code'))
        return
      }
      this.$http({
        url: this.$http.adornUrl(`/supplier/check-code/${value}`),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          if (data.exists) {
            callback(new Error('Supplier code already exists'))
          } else {
            callback()
          }
        } else {
          callback()
        }
      })
    }
    
    return {
      visible: false,
      supplierTypes: ['Product', 'Service', 'Technology', 'Consulting', 'Manufacturing', 'Distribution', 'Other'],
      dataForm: {
        supplierId: 0,
        supplierCode: '',
        supplierName: '',
        supplierType: '',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        address: '',
        website: '',
        registrationNumber: '',
        establishedYear: '',
        annualRevenue: '',
        employeeCount: '',
        industry: '',
        mainProducts: '',
        paymentTerms: '',
        description: '',
        rating: 3,
        status: 1
      },
      dataRule: {
        supplierCode: [
          { required: true, message: 'Supplier code cannot be empty', trigger: 'blur' },
          { validator: validateSupplierCode, trigger: 'blur' }
        ],
        supplierName: [
          { required: true, message: 'Supplier name cannot be empty', trigger: 'blur' }
        ],
        supplierType: [
          { required: true, message: 'Supplier type cannot be empty', trigger: 'change' }
        ],
        contactPhone: [
          { pattern: /^[0-9+\-\s()]*$/, message: 'Please enter a valid phone number', trigger: 'blur' }
        ],
        contactEmail: [
          { pattern: /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/, message: 'Please enter a valid email address', trigger: 'blur' }
        ],
        website: [
          { pattern: /^(https?:\/\/)?([a-zA-Z0-9]([a-zA-Z0-9\-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,6}(\/.*)?$/, message: 'Please enter a valid website URL', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.dataForm.supplierId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.supplierId) {
          this.$http({
            url: this.$http.adornUrl(`/supplier/supplier/info/${this.dataForm.supplierId}`),
            method: 'get'
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = {...this.dataForm, ...data.supplier}
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
            url: this.$http.adornUrl(`/supplier/supplier/${!this.dataForm.supplierId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'supplierId': this.dataForm.supplierId || undefined,
              'supplierCode': this.dataForm.supplierCode,
              'supplierName': this.dataForm.supplierName,
              'supplierType': this.dataForm.supplierType,
              'contactPerson': this.dataForm.contactPerson,
              'contactPhone': this.dataForm.contactPhone,
              'contactEmail': this.dataForm.contactEmail,
              'address': this.dataForm.address,
              'website': this.dataForm.website,
              'registrationNumber': this.dataForm.registrationNumber,
              'establishedYear': this.dataForm.establishedYear,
              'annualRevenue': this.dataForm.annualRevenue,
              'employeeCount': this.dataForm.employeeCount,
              'industry': this.dataForm.industry,
              'mainProducts': this.dataForm.mainProducts,
              'paymentTerms': this.dataForm.paymentTerms,
              'description': this.dataForm.description,
              'rating': this.dataForm.rating,
              'status': this.dataForm.status
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
              this.$message.error(data.msg || 'Operation failed')
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.form-container {
  padding: 20px 10px;
}

.el-input-number {
  width: 100% !important;
}
</style> 