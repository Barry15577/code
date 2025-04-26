<template>
  <el-dialog
    :title="!dataForm.partId ? 'Add New Auto Part' : 'Edit Auto Part'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="70%">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="180px">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="Basic Information" name="basic">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Part Number" prop="partNumber">
                <el-input v-model="dataForm.partNumber" placeholder="Part Number"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Part Name" prop="partName">
                <el-input v-model="dataForm.partName" placeholder="Part Name"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Category" prop="category">
                <el-select v-model="dataForm.category" placeholder="Select Category" style="width: 100%">
                  <el-option v-for="category in categoryOptions" :key="category" :label="category" :value="category"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Subcategory" prop="subcategory">
                <el-input v-model="dataForm.subcategory" placeholder="Subcategory"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Manufacturer" prop="manufacturer">
                <el-input v-model="dataForm.manufacturer" placeholder="Manufacturer"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="OEM Number" prop="oemNumber">
                <el-input v-model="dataForm.oemNumber" placeholder="OEM Number"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Purchase Price" prop="purchasePrice">
                <el-input-number v-model="dataForm.purchasePrice" :precision="2" :step="0.01" :min="0" placeholder="Purchase Price" style="width: 100%"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Selling Price" prop="sellingPrice">
                <el-input-number v-model="dataForm.sellingPrice" :precision="2" :step="0.01" :min="0" placeholder="Selling Price" style="width: 100%"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Stock Quantity" prop="stockQuantity">
                <el-input-number v-model="dataForm.stockQuantity" :min="0" placeholder="Stock Quantity" style="width: 100%"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Low Stock Threshold" prop="lowStockThreshold">
                <el-input-number v-model="dataForm.lowStockThreshold" :min="1" placeholder="Low Stock Threshold" style="width: 100%"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Condition" prop="condition">
                <el-select v-model="dataForm.condition" placeholder="Select Condition" style="width: 100%">
                  <el-option label="New" value="New"></el-option>
                  <el-option label="Used" value="Used"></el-option>
                  <el-option label="Refurbished" value="Refurbished"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Status" prop="status">
                <el-radio-group v-model="dataForm.status">
                  <el-radio :label="1">Active</el-radio>
                  <el-radio :label="0">Inactive</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="Specification Details" name="specs">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Compatible Makes" prop="compatibleMakes">
                <el-input v-model="dataForm.compatibleMakes" placeholder="Compatible Makes (comma separated)"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Compatible Models" prop="compatibleModels">
                <el-input v-model="dataForm.compatibleModels" placeholder="Compatible Models (comma separated)"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Year Range" prop="yearRange">
                <el-input v-model="dataForm.yearRange" placeholder="Year Range (e.g. 2015-2020)"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Dimensions" prop="dimensions">
                <el-input v-model="dataForm.dimensions" placeholder="Dimensions (e.g. 10x5x2 cm)"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Weight" prop="weight">
                <el-input-number v-model="dataForm.weight" :precision="2" :step="0.1" :min="0" placeholder="Weight" style="width: 70%"></el-input-number>
                <el-select v-model="dataForm.weightUnit" placeholder="Unit" style="width: 28%; margin-left: 2%">
                  <el-option label="kg" value="kg"></el-option>
                  <el-option label="g" value="g"></el-option>
                  <el-option label="lb" value="lb"></el-option>
                  <el-option label="oz" value="oz"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Location" prop="location">
                <el-input v-model="dataForm.location" placeholder="Location in warehouse"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="Description" prop="description">
            <el-input type="textarea" v-model="dataForm.description" :rows="4" placeholder="Description"></el-input>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="Supplier Information" name="supplier">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Supplier" prop="supplierId">
                <el-select v-model="dataForm.supplierId" placeholder="Select Supplier" style="width: 100%" @change="handleSupplierChange">
                  <el-option v-for="supplier in supplierOptions" :key="supplier.supplierId" :label="supplier.supplierName" :value="supplier.supplierId"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Supplier Name" prop="supplierName">
                <el-input v-model="dataForm.supplierName" placeholder="Supplier Name" disabled></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Restock Date" prop="restockDate">
                <el-date-picker
                  v-model="dataForm.restockDate"
                  type="datetime"
                  placeholder="Select restock date"
                  style="width: 100%">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Image URL" prop="imageUrl">
                <el-input v-model="dataForm.imageUrl" placeholder="Image URL"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
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
        activeTab: 'basic',
        categoryOptions: [],
        supplierOptions: [],
        dataForm: {
          partId: 0,
          partNumber: '',
          partName: '',
          category: '',
          subcategory: '',
          manufacturer: '',
          compatibleMakes: '',
          compatibleModels: '',
          yearRange: '',
          stockQuantity: 0,
          lowStockThreshold: 5,
          purchasePrice: '',
          sellingPrice: '',
          supplierId: '',
          supplierName: '',
          location: '',
          dimensions: '',
          weight: '',
          weightUnit: 'kg',
          condition: 'New',
          oemNumber: '',
          description: '',
          imageUrl: '',
          status: 1,
          restockDate: ''
        },
        dataRule: {
          partNumber: [
            { required: true, message: 'Part number cannot be empty', trigger: 'blur' }
          ],
          partName: [
            { required: true, message: 'Part name cannot be empty', trigger: 'blur' }
          ],
          category: [
            { required: true, message: 'Please select a category', trigger: 'change' }
          ],
          stockQuantity: [
            { required: true, message: 'Stock quantity cannot be empty', trigger: 'blur' }
          ],
          status: [
            { required: true, message: 'Status cannot be empty', trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.partId = id || 0
        this.visible = true
        this.getCategories()
        this.getSuppliers()
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.partId) {
            this.$http({
              url: this.$http.adornUrl(`/autoparts/part/info/${this.dataForm.partId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm = {
                  ...this.dataForm,
                  ...data.part
                }
              }
            })
          }
        })
      },
      // Get categories for dropdown
      getCategories () {
        this.$http({
          url: this.$http.adornUrl('/autoparts/part/categories'),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.categoryOptions = data.categories
          }
        })
      },
      // Get suppliers for dropdown
      getSuppliers () {
        this.$http({
          url: this.$http.adornUrl('/supplier/supplier/list/all'),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.supplierOptions = data.list
          }
        }).catch(error => {
          console.error('Error fetching suppliers:', error)
          this.$message.error('Failed to load suppliers. Please try again later.')
          this.supplierOptions = [] // Set empty array in case of error
        })
      },
      // Handle supplier change
      handleSupplierChange (supplierId) {
        const supplier = this.supplierOptions.find(item => item.supplierId === supplierId)
        if (supplier) {
          this.dataForm.supplierName = supplier.supplierName
        }
      },
      // Submit form
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/autoparts/part/${!this.dataForm.partId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'partId': this.dataForm.partId || undefined,
                'partNumber': this.dataForm.partNumber,
                'partName': this.dataForm.partName,
                'category': this.dataForm.category,
                'subcategory': this.dataForm.subcategory,
                'manufacturer': this.dataForm.manufacturer,
                'compatibleMakes': this.dataForm.compatibleMakes,
                'compatibleModels': this.dataForm.compatibleModels,
                'yearRange': this.dataForm.yearRange,
                'stockQuantity': this.dataForm.stockQuantity,
                'lowStockThreshold': this.dataForm.lowStockThreshold,
                'purchasePrice': this.dataForm.purchasePrice,
                'sellingPrice': this.dataForm.sellingPrice,
                'supplierId': this.dataForm.supplierId,
                'supplierName': this.dataForm.supplierName,
                'location': this.dataForm.location,
                'dimensions': this.dataForm.dimensions,
                'weight': this.dataForm.weight,
                'weightUnit': this.dataForm.weightUnit,
                'condition': this.dataForm.condition,
                'oemNumber': this.dataForm.oemNumber,
                'description': this.dataForm.description,
                'imageUrl': this.dataForm.imageUrl,
                'status': this.dataForm.status,
                'restockDate': this.dataForm.restockDate
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
</style>
