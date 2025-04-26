<template>
  <div class="site-wrapper site-page--login">
    <div class="site-content__wrapper">
      <div class="site-content">
        <div class="brand-info">
          <h2 class="brand-info__text">Customer Relationship Management</h2>
          <p class="brand-info__intro">Professional CRM platform for business growth</p>
          <div class="brand-info__features">
           
            <!--  -->
            <!-- <div class="feature-item">
              <i class="el-icon-document"></i>
              <span>Sales Reporting</span>
            </div> -->
          </div>
        </div>
        <div class="login-main">
          <div class="login-header">
            <div class="login-logo">
              <i class="el-icon-s-custom"></i>
            </div>
            <h3 class="login-title">{{ isLogin ? 'Welcome Back' : 'Create New Account' }}</h3>
          </div>
          
          <!-- Login Form -->
          <el-form v-if="isLogin" :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
            <el-form-item prop="userName">
              <el-input v-model="dataForm.userName" placeholder="Username">
                <template slot="prefix">
                  <i class="el-icon-user"></i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="dataForm.password" type="password" placeholder="Password">
                <template slot="prefix">
                  <i class="el-icon-lock"></i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="captcha">
              <el-row :gutter="20">
                <el-col :span="14">
                  <el-input v-model="dataForm.captcha" placeholder="Verification Code">
                    <template slot="prefix">
                      <i class="el-icon-key"></i>
                    </template>
                  </el-input>
                </el-col>
                <el-col :span="10" class="login-captcha">
                  <img :src="captchaPath" @click="getCaptcha()" alt="">
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-button class="login-btn-submit" type="primary" @click="dataFormSubmit()">
                <i class="el-icon-right"></i> Sign In
              </el-button>
            </el-form-item>
            <div class="form-footer">
              <span>Don't have an account?</span>
              <a href="javascript:;" @click="switchForm(false)" class="switch-form">Register Now</a>
            </div>
          </el-form>
          
          <!-- Registration Form -->
          <el-form v-else :model="registerForm" :rules="registerRules" ref="registerForm" @keyup.enter.native="registerFormSubmit()" status-icon>
            <el-form-item prop="userName">
              <el-input v-model="registerForm.userName" placeholder="Username">
                <template slot="prefix">
                  <i class="el-icon-user"></i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="registerForm.password" type="password" placeholder="Password">
                <template slot="prefix">
                  <i class="el-icon-lock"></i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input v-model="registerForm.confirmPassword" type="password" placeholder="Confirm Password">
                <template slot="prefix">
                  <i class="el-icon-lock"></i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="email">
              <el-input v-model="registerForm.email" placeholder="Email">
                <template slot="prefix">
                  <i class="el-icon-message"></i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button class="login-btn-submit" type="primary" @click="registerFormSubmit()">
                <i class="el-icon-check"></i> Register
              </el-button>
            </el-form-item>
            <div class="form-footer">
              <span>Already have an account?</span>
              <a href="javascript:;" @click="switchForm(true)" class="switch-form">Sign In</a>
            </div>
          </el-form>
          
          <div class="login-footer">
            <p>© 2025 Customer Relationship Management System</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getUUID } from '@/utils'
export default {
  data () {
    // Password confirmation validation
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('Passwords do not match'))
      } else {
        callback()
      }
    }
    return {
      isLogin: true, // Controls whether to display login or registration form
      dataForm: {
        userName: '',
        password: '',
        uuid: '',
        captcha: ''
      },
      dataRule: {
        userName: [
          { required: true, message: 'Username cannot be empty', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Password cannot be empty', trigger: 'blur' }
        ],
        captcha: [
          { required: true, message: 'Verification code cannot be empty', trigger: 'blur' }
        ]
      },
      registerForm: {
        userName: '',
        password: '',
        confirmPassword: '',
        email: '',
        uuid: '',
        captcha: '',
        role: 'user' // Default registration as regular user
      },
      registerRules: {
        userName: [
          { required: true, message: 'Username cannot be empty', trigger: 'blur' },
          { min: 4, max: 20, message: 'Username must be 4-20 characters', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Password cannot be empty', trigger: 'blur' },
          { min: 6, max: 20, message: 'Password must be 6-20 characters', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: 'Please confirm your password', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ],
        email: [
          { required: true, message: 'Email cannot be empty', trigger: 'blur' },
          { type: 'email', message: 'Please enter a valid email address', trigger: 'blur' }
        ]
      },
      captchaPath: ''
    }
  },
  created () {
    this.getCaptcha()
  },
  methods: {
    // Switch between login/registration forms
    switchForm (isLogin) {
      this.isLogin = isLogin
      this.getCaptcha()
      // Reset form when switching
      if (isLogin) {
        this.$nextTick(() => {
          this.$refs['dataForm'] && this.$refs['dataForm'].resetFields()
        })
      } else {
        this.$nextTick(() => {
          this.$refs['registerForm'] && this.$refs['registerForm'].resetFields()
        })
      }
    },
    // Get verification code
    getCaptcha () {
      const uuid = getUUID()
      if (this.isLogin) {
        this.dataForm.uuid = uuid
      } else {
        this.registerForm.uuid = uuid
      }
      this.captchaPath = this.$http.adornUrl(`/captcha.jpg?uuid=${uuid}`)
    },
    // Submit login form
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl('/sys/login'),
            method: 'post',
            data: this.$http.adornData({
              'username': this.dataForm.userName,
              'password': this.dataForm.password,
              'uuid': this.dataForm.uuid,
              'captcha': this.dataForm.captcha
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$cookie.set('token', data.token)
              this.$router.replace({ name: 'home' })
            } else {
              this.getCaptcha()
              this.$message.error(data.msg)
            }
          })
        }
      })
    },
    // Submit registration form
    registerFormSubmit () {
      this.$refs['registerForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl('/sys/register'),
            method: 'post',
            data: this.$http.adornData({
              'username': this.registerForm.userName,
              'password': this.registerForm.password,
              'email': this.registerForm.email,
              'uuid': this.registerForm.uuid,
              'captcha': this.registerForm.captcha,
              'role': this.registerForm.role
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: 'Registration successful, please sign in',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.switchForm(true)
                }
              })
            } else {
              this.getCaptcha()
              this.$message.error(data.msg || 'Registration failed, please try again')
            }
          })
        }
      })
    }
  }
}
</script>

<style lang="scss">
.site-wrapper.site-page--login {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background: linear-gradient(135deg, #17B3A3 0%, #3498db 100%);
  overflow: hidden;
  
  .site-content__wrapper {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    padding: 0;
    margin: 0;
    overflow-x: hidden;
    overflow-y: auto;
    background-color: transparent;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .site-content {
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 40px;
    
    @media screen and (max-width: 992px) {
      flex-direction: column;
      padding: 40px 20px;
    }
  }
  
  .brand-info {
    flex: 1;
    max-width: 600px;
    margin: 0;
    padding-right: 100px;
    
    @media screen and (max-width: 992px) {
      max-width: 100%;
      padding-right: 0;
      margin-bottom: 60px;
      text-align: center;
    }
  }
  
  .brand-info__text {
    margin: 0 0 20px 0;
    font-size: 48px;
    font-weight: 600;
    color: #fff;
    line-height: 1.2;
    letter-spacing: 2px;
    text-shadow: 0 2px 10px rgba(0,0,0,0.2);
    animation: fadeInUp 0.8s ease;
    
    @media screen and (max-width: 768px) {
      font-size: 36px;
    }
  }
  
  .brand-info__intro {
    font-size: 20px;
    line-height: 1.6;
    color: rgba(255,255,255,0.9);
    margin: 0 0 30px 0;
    animation: fadeInUp 0.8s ease 0.2s both;
    
    @media screen and (max-width: 768px) {
      font-size: 16px;
    }
  }
  
  .brand-info__features {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
    margin-top: 30px;
    animation: fadeInUp 0.8s ease 0.4s both;
    
    @media screen and (max-width: 992px) {
      justify-content: center;
    }
    
    .feature-item {
      display: flex;
      align-items: center;
      background: rgba(255, 255, 255, 0.15);
      padding: 12px 24px;
      border-radius: 50px;
      backdrop-filter: blur(5px);
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-3px);
        background: rgba(255, 255, 255, 0.2);
        box-shadow: 0 6px 15px rgba(0, 0, 0, 0.15);
      }
      
      i {
        font-size: 20px;
        color: #fff;
        margin-right: 8px;
      }
      
      span {
        color: #fff;
        font-size: 16px;
        font-weight: 500;
      }
    }
  }
  
  .login-main {
    position: relative;
    width: 420px;
    padding: 40px;
    background: rgba(255,255,255,0.95);
    border-radius: 20px;
    box-shadow: 0 20px 60px rgba(0,0,0,0.15);
    animation: fadeInRight 0.8s ease;
    
    @media screen and (max-width: 576px) {
      width: 100%;
      padding: 30px 20px;
    }
  }
  
  .login-header {
    text-align: center;
    margin-bottom: 40px;
  }
  
  .login-logo {
    margin-bottom: 20px;
    
    i {
      font-size: 48px;
      color: #17B3A3;
      animation: bounceIn 0.8s ease;
    }
  }
  
  .login-title {
    font-size: 24px;
    color: #2c3e50;
    margin: 0;
    font-weight: 600;
  }
  
  .el-form-item {
    margin-bottom: 25px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .el-input {
    .el-input__inner {
      height: 46px;
      line-height: 46px;
      border-radius: 8px;
      padding-left: 45px;
      border: 1px solid #dcdfe6;
      background-color: #f5f7fa;
      transition: all 0.3s;
      
      &:hover {
        border-color: #17B3A3;
      }
      
      &:focus {
        border-color: #17B3A3;
        background-color: #fff;
        box-shadow: 0 0 0 2px rgba(23, 179, 163, 0.1);
      }
    }
    
    .el-input__prefix {
      left: 15px;
      
      i {
        font-size: 18px;
        color: #909399;
        line-height: 46px;
        transition: all 0.3s;
      }
    }
    
    &.is-active .el-input__prefix i {
      color: #17B3A3;
    }
  }
  
  .login-captcha {
    img {
      width: 100%;
      height: 46px;
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        opacity: 0.8;
      }
    }
  }
  
  .login-btn-submit {
    width: 100%;
    height: 50px;
    margin-top: 10px;
    font-size: 16px;
    font-weight: 500;
    background: linear-gradient(to right, #17B3A3, #3498db);
    border: none;
    border-radius: 12px;
    transition: all 0.3s;
    
    i {
      margin-right: 8px;
    }
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 20px rgba(23, 179, 163, 0.3);
    }
    
    &:active {
      transform: translateY(0);
    }
  }
  
  .login-footer {
    margin-top: 30px;
    text-align: center;
    
    p {
      font-size: 13px;
      color: #999;
    }
  }

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  @keyframes fadeInRight {
    from {
      opacity: 0;
      transform: translateX(20px);
    }
    to {
      opacity: 1;
      transform: translateX(0);
    }
  }

  @keyframes bounceIn {
    from {
      opacity: 0;
      transform: scale3d(0.3, 0.3, 0.3);
    }
    50% {
      opacity: 1;
      transform: scale3d(1.1, 1.1, 1.1);
    }
    to {
      transform: scale3d(1, 1, 1);
    }
  }

  .form-footer {
    text-align: center;
    margin-top: 20px;
    font-size: 14px;
    color: #606266;
    
    .switch-form {
      color: #17B3A3;
      margin-left: 5px;
      font-weight: 500;
      text-decoration: none;
      transition: all 0.3s;
      
      &:hover {
        color: #3498db;
        text-decoration: underline;
      }
    }
  }
}
</style>
