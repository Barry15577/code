<template>
  <el-dialog
    :title="!dataForm.productId ? 'Add Insurance Product' : 'Edit Insurance Product'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="65%">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Product Code" prop="productCode">
            <el-input v-model="dataForm.productCode" placeholder="Product Code" :disabled="!!dataForm.productId"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Product Name" prop="productName">
            <el-input v-model="dataForm.productName" placeholder="Product Name"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Product Type" prop="productType">
            <el-select v-model="dataForm.productType" placeholder="Select Product Type" style="width: 100%">
              <el-option label="Life Insurance" value="Life"></el-option>
              <el-option label="Health Insurance" value="Health"></el-option>
              <el-option label="Property Insurance" value="Property"></el-option>
              <el-option label="Auto Insurance" value="Auto"></el-option>
              <el-option label="Travel Insurance" value="Travel"></el-option>
              <el-option label="Business Insurance" value="Business"></el-option>
              <el-option label="Other" value="Other"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Provider" prop="provider">
            <el-input v-model="dataForm.provider" placeholder="Insurance Provider"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Min Premium" prop="minPremium">
            <el-input-number v-model="dataForm.minPremium" :precision="2" :step="100" :min="0" placeholder="Minimum Premium" style="width: 100%"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Max Coverage" prop="maxCoverage">
            <el-input-number v-model="dataForm.maxCoverage" :precision="2" :step="10000" :min="0" placeholder="Maximum Coverage" style="width: 100%"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="Description" prop="description">
        <el-input type="textarea" v-model="dataForm.description" placeholder="Product Description" :rows="3"></el-input>
      </el-form-item>
      
      <el-form-item label="Coverage Details" prop="coverageDetails">
        <el-input type="textarea" v-model="dataForm.coverageDetails" placeholder="Coverage Details" :rows="5"></el-input>
      </el-form-item>
      
      <el-form-item label="Terms & Conditions" prop="terms">
        <el-input type="textarea" v-model="dataForm.terms" placeholder="Terms and Conditions" :rows="5"></el-input>
      </el-form-item>
      
      <el-form-item label="Status" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="1">Active</el-radio>
          <el-radio :label="0">Inactive</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">Cancel</el-button>
      <el-button type="primary" @click="dataFormSubmit()">Confirm</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  data () {
    // Custom validator to check if product code exists
    const validateProductCode = (rule, value, callback) => {
      if (!value) {
        callback(new Error('Please enter product code'))
        return
      }
      
      // Only check for duplicates when adding new product
      if (!this.dataForm.productId) {
        this.$http({
          url: this.$http.adornUrl(`/insurance/product/check-code/${value}`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            if (data.exists) {
              callback(new Error('Product code already exists'))
            } else {
              callback()
            }
          } else {
            callback()
          }
        })
      } else {
        callback()
      }
    }
    
    return {
      visible: false,
      dataForm: {
        productId: 0,
        productCode: '',
        productName: '',
        productType: '',
        provider: '',
        minPremium: '',
        maxCoverage: '',
        description: '',
        coverageDetails: '',
        terms: '',
        status: 1
      },
      dataRule: {
        productCode: [
          { required: true, message: 'Product code cannot be empty', trigger: 'blur' },
          { validator: validateProductCode, trigger: 'blur' }
        ],
        productName: [
          { required: true, message: 'Product name cannot be empty', trigger: 'blur' }
        ],
        productType: [
          { required: true, message: 'Please select product type', trigger: 'change' }
        ],
        provider: [
          { required: true, message: 'Provider cannot be empty', trigger: 'blur' }
        ],
        status: [
          { required: true, message: 'Please select status', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.dataForm.productId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.productId) {
          this.$http({
            url: this.$http.adornUrl(`/insurance/product/info/${this.dataForm.productId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm = {
                ...this.dataForm,
                ...data.product
              }
            }
          })
        }
      })
    },
    // Submit form
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/insurance/product/${!this.dataForm.productId ? 'save' : 'update'}`),
            method: !this.dataForm.productId ? 'post' : 'put',
            data: this.$http.adornData(this.dataForm)
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
.el-input-number.is-controls-right .el-input__inner {
  text-align: left;
}
</style> 