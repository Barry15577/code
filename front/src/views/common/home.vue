<template>
  <div class="mod-home">
    <!-- Hero Banner -->
    <div class="hero-banner">
      <div class="banner-content">
        <h1>Protect Your Future with Our Insurance Plans</h1>
        <p>We offer a wide range of insurance products to meet your needs</p>
        <el-button type="primary" size="large" @click="scrollToProducts">View Plans</el-button>
      </div>
    </div>
    
    <!-- Product Types Filter -->
    <div class="product-types-section">
      <div class="section-title">
        <h2>Browse by Insurance Type</h2>
      </div>
      <div class="type-filters">
        <el-button 
          v-for="type in productTypes" 
          :key="type" 
          :class="['type-button', selectedType === type ? 'active' : '']"
          @click="filterByType(type)">
          {{ type }} Insurance
        </el-button>
        <el-button class="type-button" @click="filterByType('')">All Types</el-button>
      </div>
    </div>
    
    <!-- Featured Products -->
    <div class="products-section" id="products-section" ref="productsSection">
      <div class="section-title">
        <h2>{{ selectedType ? selectedType + ' Insurance Plans' : 'Featured Insurance Plans' }}</h2>
      </div>
      
      <div v-loading="productsLoading" class="product-list">
        <el-empty v-if="!productsLoading && products.length === 0" description="No products found"></el-empty>
        <el-card 
          v-for="product in products" 
          :key="product.productId" 
          class="product-card"
          shadow="hover">
          <div class="product-tag" :class="getProductTagClass(product.productType)">
            {{ product.productType }}
          </div>
          <h3 class="product-title">{{ product.productName }}</h3>
          <div class="product-provider">{{ product.provider }}</div>
          <div class="product-price" v-if="product.minPremium">
            <span class="price-label">From</span>
            <span class="price-amount">${{ product.minPremium }}</span>
          </div>
          <div class="product-coverage" v-if="product.maxCoverage">
            <span class="coverage-label">Coverage up to</span>
            <span class="coverage-amount">${{ product.maxCoverage }}</span>
          </div>
          <div class="product-description">{{ truncateText(product.description, 100) }}</div>
          <div class="product-actions">
            <el-button type="primary" @click="viewProductDetails(product)">View Details</el-button>
            <el-button type="success" @click="openOrderForm(product)">Get a Quote</el-button>
          </div>
        </el-card>
      </div>
      
      <!-- Load More Button -->
      <div class="load-more" v-if="products.length > 0 && !allProductsLoaded">
        <el-button @click="loadMoreProducts" :loading="loadingMore">Load More</el-button>
      </div>
    </div>
    
    <!-- Product Details Dialog -->
    <el-dialog
      title="Insurance Product Details"
      :visible.sync="productDetailsVisible"
      width="70%"
      class="product-details-dialog">
      <div v-if="selectedProduct" v-loading="detailsLoading">
        <div class="details-header">
          <h2>{{ selectedProduct.productName }}</h2>
          <div class="details-tag" :class="getProductTagClass(selectedProduct.productType)">
            {{ selectedProduct.productType }}
          </div>
          <div class="details-provider">Provider: {{ selectedProduct.provider }}</div>
        </div>
        
        <el-divider content-position="left">Coverage Information</el-divider>
        
        <el-row :gutter="20" class="details-section">
          <el-col :span="12">
            <div class="details-item">
              <div class="details-label">Minimum Premium</div>
              <div class="details-value price">{{ selectedProduct.minPremium ? '$' + selectedProduct.minPremium : 'Contact for pricing' }}</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="details-item">
              <div class="details-label">Maximum Coverage</div>
              <div class="details-value coverage">{{ selectedProduct.maxCoverage ? '$' + selectedProduct.maxCoverage : 'Contact for details' }}</div>
            </div>
          </el-col>
        </el-row>
        
        <el-divider content-position="left">Description</el-divider>
        
        <div class="details-description">
          {{ selectedProduct.description || 'No description available' }}
        </div>
        
        <el-tabs type="border-card" class="details-tabs">
          <el-tab-pane label="Coverage Details">
            <div v-if="selectedProduct.coverageDetails" class="details-content" v-html="formatText(selectedProduct.coverageDetails)"></div>
            <div v-else class="no-content">No coverage details available</div>
          </el-tab-pane>
          <el-tab-pane label="Terms & Conditions">
            <div v-if="selectedProduct.terms" class="details-content" v-html="formatText(selectedProduct.terms)"></div>
            <div v-else class="no-content">No terms and conditions available</div>
          </el-tab-pane>
        </el-tabs>
        
        <div class="details-actions">
          <el-button type="success" @click="openOrderForm(selectedProduct)">Schedule an Appointment</el-button>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="productDetailsVisible = false">Close</el-button>
      </span>
    </el-dialog>
    
    <!-- Order Form Dialog -->
    <el-dialog
      title="Schedule an Insurance Consultation"
      :visible.sync="orderFormVisible"
      width="60%"
      class="order-form-dialog">
      <el-form 
        v-if="selectedProduct"
        :model="orderForm"
        :rules="orderRules"
        ref="orderForm"
        label-width="140px"
        status-icon>
        <el-alert
          title="Get a personalized insurance quote and consultation"
          type="info"
          description="Complete this form to schedule a consultation with our insurance expert."
          show-icon
          :closable="false"
          style="margin-bottom: 20px;">
        </el-alert>
        
        <el-form-item label="Product" prop="productId">
          <div class="selected-product">
            <span class="selected-product-name">{{ selectedProduct.productName }}</span>
            <span class="selected-product-type">{{ selectedProduct.productType }}</span>
          </div>
        </el-form-item>
        
        <el-form-item label="Your Name" prop="customerName">
          <el-input v-model="orderForm.customerName" placeholder="Enter your full name"></el-input>
        </el-form-item>
        
        <el-form-item label="Phone Number" prop="contactPhone">
          <el-input v-model="orderForm.contactPhone" placeholder="Enter your phone number"></el-input>
        </el-form-item>
        
        <el-form-item label="Email" prop="contactEmail">
          <el-input v-model="orderForm.contactEmail" placeholder="Enter your email address"></el-input>
        </el-form-item>
        
        <el-form-item label="Preferred Date" prop="appointmentDate">
          <el-date-picker
            v-model="orderForm.appointmentDate"
            type="datetime"
            placeholder="Select preferred consultation date"
            :picker-options="datePickerOptions"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        
        <el-form-item label="Comments" prop="comments">
          <el-input 
            type="textarea" 
            v-model="orderForm.comments" 
            placeholder="Enter any additional information or questions"
            :rows="4">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="orderFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitOrder" :loading="submitting">Submit</el-button>
      </div>
    </el-dialog>
    
    <!-- Success Message Dialog -->
    <el-dialog
      title="Appointment Scheduled Successfully"
      :visible.sync="successDialogVisible"
      width="40%"
      center>
      <div class="success-message">
        <i class="el-icon-success success-icon"></i>
        <h3>Thank you for your interest!</h3>
        <p>Your consultation request has been submitted successfully.</p>
        <p>One of our insurance experts will contact you shortly to confirm your appointment.</p>
        <p class="reference-number">Reference Number: <span>{{ orderReferenceNumber }}</span></p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="successDialogVisible = false">OK</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { formatDate } from '@/utils/date'

export default {
  data() {
    // Validate phone number
    const validatePhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('Please enter your phone number'))
      } else if (!/^[0-9]{10,15}$/.test(value.replace(/[^0-9]/g, ''))) {
        callback(new Error('Please enter a valid phone number'))
      } else {
        callback()
      }
    }
    
    // Validate email
    const validateEmail = (rule, value, callback) => {
      if (!value) {
        callback()
      } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
        callback(new Error('Please enter a valid email address'))
      } else {
        callback()
      }
    }
    
    // Get today's date for date picker min date
    const today = new Date()
    
    return {
      productTypes: [],
      selectedType: '',
      products: [],
      productsLoading: false,
      detailsLoading: false,
      currentPage: 1,
      pageSize: 4,
      allProductsLoaded: false,
      loadingMore: false,
      
      productDetailsVisible: false,
      selectedProduct: null,
      
      orderFormVisible: false,
      submitting: false,
      
      successDialogVisible: false,
      orderReferenceNumber: '',
      
      orderForm: {
        productId: null,
        productName: '',
        customerName: '',
        contactPhone: '',
        contactEmail: '',
        appointmentDate: '',
        comments: ''
      },
      
      orderRules: {
        customerName: [
          { required: true, message: 'Please enter your name', trigger: 'blur' },
          { min: 2, max: 50, message: 'Name must be between 2 and 50 characters', trigger: 'blur' }
        ],
        contactPhone: [
          { required: true, trigger: 'blur', validator: validatePhone }
        ],
        contactEmail: [
          { validator: validateEmail, trigger: 'blur' }
        ],
        appointmentDate: [
          { required: true, message: 'Please select a preferred date', trigger: 'change' }
        ]
      },
      
      datePickerOptions: {
        disabledDate(date) {
          return date < today
        }
      }
    }
  },
  
  created() {
    this.fetchProductTypes()
    this.fetchProducts()
  },
  
  methods: {
    // Fetch product types
    fetchProductTypes() {
      this.$http({
        url: this.$http.adornUrl('/api/insurance/product-types'),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.productTypes = data.productTypes || []
        }
      })
    },
    
    // Fetch products with optional type filter
    fetchProducts(loadMore = false) {
      if (loadMore) {
        this.currentPage++
        this.loadingMore = true
      } else {
        this.currentPage = 1
        this.products = []
        this.productsLoading = true
        this.allProductsLoaded = false
      }
      
      const params = {
        page: this.currentPage,
        limit: this.pageSize
      }
      
      if (this.selectedType) {
        params.productType = this.selectedType
      }
      
      this.$http({
        url: this.$http.adornUrl('/api/insurance/products'),
        method: 'get',
        params: this.$http.adornParams(params)
      }).then(({data}) => {
        this.productsLoading = false
        this.loadingMore = false
        
        if (data && data.code === 0) {
          const newProducts = data.page.list || []
          
          if (loadMore) {
            this.products = [...this.products, ...newProducts]
          } else {
            this.products = newProducts
          }
          
          // Check if all products are loaded
          this.allProductsLoaded = this.products.length >= data.page.totalCount
        }
      }).catch(() => {
        this.productsLoading = false
        this.loadingMore = false
      })
    },
    
    // Load more products
    loadMoreProducts() {
      this.fetchProducts(true)
    },
    
    // Filter products by type
    filterByType(type) {
      this.selectedType = type
      this.fetchProducts()
    },
    
    // Get product tag class based on type
    getProductTagClass(type) {
      const typeMap = {
        'Life': 'life',
        'Health': 'health',
        'Property': 'property',
        'Auto': 'auto',
        'Travel': 'travel',
        'Business': 'business',
        'Other': 'other'
      }
      return typeMap[type] || 'other'
    },
    
    // Truncate text
    truncateText(text, maxLength) {
      if (!text) return 'No description available'
      return text.length > maxLength ? text.slice(0, maxLength) + '...' : text
    },
    
    // Format text with line breaks
    formatText(text) {
      if (!text) return 'Not available'
      return text.replace(/\n/g, '<br>')
    },
    
    // View product details
    viewProductDetails(product) {
      this.selectedProduct = product
      this.productDetailsVisible = true
      this.detailsLoading = true
      
      // Fetch complete product details
      this.$http({
        url: this.$http.adornUrl(`/api/insurance/product/${product.productId}`),
        method: 'get'
      }).then(({data}) => {
        this.detailsLoading = false
        if (data && data.code === 0) {
          this.selectedProduct = data.product
        }
      }).catch(() => {
        this.detailsLoading = false
      })
    },
    
    // Open order form
    openOrderForm(product) {
      this.selectedProduct = product
      this.orderForm = {
        productId: product.productId,
        productName: product.productName,
        customerName: '',
        contactPhone: '',
        contactEmail: '',
        appointmentDate: '',
        comments: ''
      }
      this.orderFormVisible = true
      
      // Close product details dialog if open
      if (this.productDetailsVisible) {
        this.productDetailsVisible = false
      }
    },
    
    // Submit order
    submitOrder() {
      this.$refs.orderForm.validate((valid) => {
        if (valid) {
          this.submitting = true
          
          this.$http({
            url: this.$http.adornUrl('/api/insurance/submit-order'),
            method: 'post',
            data: this.$http.adornData(this.orderForm)
          }).then(({data}) => {
            this.submitting = false
            
            if (data && data.code === 0) {
              this.orderReferenceNumber = data.orderId
              this.orderFormVisible = false
              this.successDialogVisible = true
              this.$refs.orderForm.resetFields()
            } else {
              this.$message.error(data.msg || 'Failed to submit order')
            }
          }).catch(() => {
            this.submitting = false
            this.$message.error('System error, please try again later')
          })
        }
      })
    },
    
    // Scroll to products section
    scrollToProducts() {
      const element = this.$refs.productsSection
      if (element) {
        element.scrollIntoView({ behavior: 'smooth' })
      }
    }
  }
}
</script>

<style scoped>
.mod-home {
  line-height: 1.5;
  padding-bottom: 50px;
}

/* Hero Banner */
.hero-banner {
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('https://images.unsplash.com/photo-1560520031-3a4dc4e9de0c?ixlib=rb-1.2.1&auto=format&fit=crop&w=1920&q=80');
  background-size: cover;
  background-position: center;
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: white;
  margin-bottom: 40px;
}

.banner-content {
  max-width: 800px;
  padding: 0 20px;
}

.banner-content h1 {
  font-size: 36px;
  margin-bottom: 20px;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.6);
}

.banner-content p {
  font-size: 18px;
  margin-bottom: 30px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.6);
}

/* Product Types Section */
.product-types-section {
  margin-bottom: 30px;
  padding: 0 20px;
}

.section-title {
  text-align: center;
  margin-bottom: 25px;
}

.section-title h2 {
  font-size: 28px;
  color: #303133;
  position: relative;
  display: inline-block;
  margin: 0;
}

.section-title h2:after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  width: 60px;
  height: 3px;
  background: #409EFF;
  transform: translateX(-50%);
}

.type-filters {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
}

.type-button {
  min-width: 120px;
  border-radius: 20px;
}

.type-button.active {
  background-color: #409EFF;
  color: white;
  border-color: #409EFF;
}

/* Products Section */
.products-section {
  padding: 0 20px;
  margin-top: 40px;
  margin-bottom: 40px;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 25px;
  margin-bottom: 30px;
}

.product-card {
  position: relative;
  transition: transform 0.3s, box-shadow 0.3s;
  height: 100%;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.product-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 2px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
  color: white;
}

.product-tag.life {
  background-color: #409EFF;
}

.product-tag.health {
  background-color: #67C23A;
}

.product-tag.property {
  background-color: #E6A23C;
}

.product-tag.auto {
  background-color: #F56C6C;
}

.product-tag.travel {
  background-color: #909399;
}

.product-tag.business {
  background-color: #6b7ddf;
}

.product-tag.other {
  background-color: #909399;
}

.product-title {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 18px;
  color: #303133;
}

.product-provider {
  font-size: 14px;
  color: #606266;
  margin-bottom: 15px;
}

.product-price {
  margin-bottom: 5px;
}

.price-label {
  font-size: 13px;
  color: #606266;
}

.price-amount {
  font-size: 18px;
  font-weight: bold;
  color: #409EFF;
  margin-left: 5px;
}

.product-coverage {
  margin-bottom: 15px;
}

.coverage-label {
  font-size: 13px;
  color: #606266;
}

.coverage-amount {
  font-size: 15px;
  font-weight: bold;
  color: #67C23A;
  margin-left: 5px;
}

.product-description {
  font-size: 14px;
  color: #606266;
  margin-bottom: 20px;
  min-height: 60px;
}

.product-actions {
  display: flex;
  justify-content: space-between;
  margin-top: auto;
}

.load-more {
  text-align: center;
  margin-top: 20px;
}

/* Product Details Dialog */
.details-header {
  margin-bottom: 20px;
}

.details-header h2 {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 22px;
  color: #303133;
}

.details-tag {
  display: inline-block;
  padding: 3px 12px;
  border-radius: 15px;
  font-size: 13px;
  font-weight: bold;
  color: white;
  margin-bottom: 10px;
}

.details-provider {
  font-size: 14px;
  color: #606266;
}

.details-section {
  margin-bottom: 20px;
}

.details-item {
  margin-bottom: 15px;
}

.details-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.details-value {
  font-size: 16px;
  color: #303133;
}

.details-value.price {
  color: #409EFF;
  font-weight: bold;
}

.details-value.coverage {
  color: #67C23A;
  font-weight: bold;
}

.details-description {
  background: #f8f8f8;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
  line-height: 1.6;
}

.details-tabs {
  margin-bottom: 20px;
}

.details-content {
  line-height: 1.6;
  padding: 10px;
  max-height: 300px;
  overflow-y: auto;
}

.no-content {
  color: #909399;
  font-style: italic;
  padding: 15px;
  text-align: center;
}

.details-actions {
  margin-top: 20px;
  text-align: center;
}

/* Order Form */
.selected-product {
  padding: 10px 15px;
  background: #f5f7fa;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.selected-product-name {
  font-weight: bold;
  color: #303133;
}

.selected-product-type {
  margin-left: 10px;
  color: #606266;
  font-size: 13px;
}

/* Success Message */
.success-message {
  text-align: center;
  padding: 20px 0;
}

.success-icon {
  font-size: 60px;
  color: #67C23A;
  margin-bottom: 20px;
}

.reference-number {
  margin-top: 20px;
  color: #606266;
}

.reference-number span {
  font-weight: bold;
  color: #409EFF;
}

@media (max-width: 768px) {
  .product-list {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  }
  
  .banner-content h1 {
    font-size: 28px;
  }
  
  .banner-content p {
    font-size: 16px;
  }
}
</style>

