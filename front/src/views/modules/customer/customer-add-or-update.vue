<template>
  <el-dialog
    :title="!dataForm.customerId ? 'Add Customer' : 'Edit Customer'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="50%">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
      <el-form-item label="Customer Name" prop="customerName">
        <el-input v-model="dataForm.customerName" placeholder="Customer Name"></el-input>
      </el-form-item>
      <el-form-item label="Email" prop="email">
        <el-input v-model="dataForm.email" placeholder="Email"></el-input>
      </el-form-item>
      <el-form-item label="Phone" prop="phone">
        <el-input v-model="dataForm.phone" placeholder="Phone"></el-input>
      </el-form-item>
      <el-form-item label="Company" prop="company">
        <el-input v-model="dataForm.company" placeholder="Company"></el-input>
      </el-form-item>
      <el-form-item label="Address" prop="address">
        <el-input type="textarea" v-model="dataForm.address" placeholder="Address"></el-input>
      </el-form-item>
      <el-form-item label="Status" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="1">Active</el-radio>
          <el-radio :label="0">Inactive</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="Industry" prop="industry">
        <el-select v-model="dataForm.industry" placeholder="Industry">
          <el-option label="Technology" value="Technology"></el-option>
          <el-option label="Finance" value="Finance"></el-option>
          <el-option label="Healthcare" value="Healthcare"></el-option>
          <el-option label="Education" value="Education"></el-option>
          <el-option label="Manufacturing" value="Manufacturing"></el-option>
          <el-option label="Retail" value="Retail"></el-option>
          <el-option label="Others" value="Others"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Customer Source" prop="source">
        <el-select v-model="dataForm.source" placeholder="Customer Source">
          <el-option label="Website" value="Website"></el-option>
          <el-option label="Referral" value="Referral"></el-option>
          <el-option label="Email Campaign" value="Email Campaign"></el-option>
          <el-option label="Social Media" value="Social Media"></el-option>
          <el-option label="Event" value="Event"></el-option>
          <el-option label="Other" value="Other"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Notes" prop="notes">
        <el-input type="textarea" v-model="dataForm.notes" placeholder="Notes" :rows="3"></el-input>
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
      // Custom validator for phone number
      const validatePhone = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('Phone number cannot be empty'))
        } else {
          const phoneRegex = /^[+]?[(]?[0-9]{3}[)]?[-\s.]?[0-9]{3}[-\s.]?[0-9]{4,6}$/
          if (!phoneRegex.test(value)) {
            callback(new Error('Please enter a valid phone number'))
          } else {
            callback()
          }
        }
      }
      
      // Custom validator for email
      const validateEmail = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('Email cannot be empty'))
        } else {
          const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
          if (!emailRegex.test(value)) {
            callback(new Error('Please enter a valid email address'))
          } else {
            callback()
          }
        }
      }
      
      return {
        visible: false,
        dataForm: {
          customerId: 0,
          customerName: '',
          email: '',
          phone: '',
          company: '',
          address: '',
          status: 1,
          industry: '',
          source: '',
          notes: ''
        },
        dataRule: {
          customerName: [
            { required: true, message: 'Customer name cannot be empty', trigger: 'blur' }
          ],
          email: [
            { required: true, message: 'Email cannot be empty', trigger: 'blur' },
            { validator: validateEmail, trigger: 'blur' }
          ],
          phone: [
            { required: true, message: 'Phone number cannot be empty', trigger: 'blur' },
            { validator: validatePhone, trigger: 'blur' }
          ],
          company: [
            { required: false, message: 'Company name cannot be empty', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.customerId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.customerId) {
            // If editing, load customer data
            this.$http({
              url: this.$http.adornUrl(`/customer/info/${this.dataForm.customerId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.customerName = data.customer.customerName
                this.dataForm.email = data.customer.email
                this.dataForm.phone = data.customer.phone
                this.dataForm.company = data.customer.company
                this.dataForm.address = data.customer.address
                this.dataForm.status = data.customer.status
                this.dataForm.industry = data.customer.industry
                this.dataForm.source = data.customer.source
                this.dataForm.notes = data.customer.notes
              }
            })
          }
        })
      },
      // Submit form data
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            // Determine URL based on add or update operation
            const url = this.dataForm.customerId 
              ? this.$http.adornUrl(`/customer/update`) 
              : this.$http.adornUrl(`/customer/save`)
            
            // Determine HTTP method
            const method = this.dataForm.customerId ? 'put' : 'post'
            
            this.$http({
              url: url,
              method: method,
              data: this.$http.adornData({
                'customerId': this.dataForm.customerId || undefined,
                'customerName': this.dataForm.customerName,
                'email': this.dataForm.email,
                'phone': this.dataForm.phone,
                'company': this.dataForm.company,
                'address': this.dataForm.address,
                'status': this.dataForm.status,
                'industry': this.dataForm.industry,
                'source': this.dataForm.source,
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