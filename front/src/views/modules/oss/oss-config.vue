<template>
  <el-dialog
    title="Cloud Storage Configuration"
:close-on-click-modal="false"
:visible.sync="visible">
<el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
<el-form-item size="mini" label="Storage Type">
<el-radio-group v-model="dataForm.type">
<el-radio :label="1">Qiniu</el-radio>
<el-radio :label="2">Alibaba Cloud</el-radio>
<el-radio :label="3">Tencent Cloud</el-radio>
        </el-radio-group>
      </el-form-item>
      <template v-if="dataForm.type === 1">
        <el-form-item size="mini">
          <a href="http://www.renren.io/open/qiniu.html" target="_blank"></a>
        </el-form-item>
        <el-form-item label="domain name"> 
<el-input v-model="dataForm.qiniuDomain" placeholder=""></el-input> 
</el-form-item> 
<el-form-item label="path prefix"> 
<el-input v-model="dataForm.qiniuPrefix" placeholder="If not set, the default is empty"></el-input> 
</el-form-item> 
<el-form-item label="AccessKey"> 
<el-input v-model="dataForm.qiniuAccessKey" placeholder="AccessKey"></el-input> 
</el-form-item> 
<el-form-item label="SecretKey"> 
<el-input v-model="dataForm.qiniuSecretKey" placeholder="SecretKey"></el-input> 
</el-form-item> 
<el-form-item label="space name"> 
<el-input v-model="dataForm.qiniuBucketName" placeholder="storage space name"></el-input>
</el-form-item>
</template>
      <template v-else-if="dataForm.type === 2">
<el-form-item label="Domain name">
<el-input v-model="dataForm.aliyunDomain" placeholder="Domain name bound to Alibaba Cloud"></el-input>
</el-form-item>
<el-form-item label="Path prefix">
<el-input v-model="dataForm.aliyunPrefix" placeholder="Default is empty if not set"></el-input>
</el-form-item>
<el-form-item label="EndPoint">
<el-input v-model="dataForm.aliyunEndPoint" placeholder="Alibaba Cloud EndPoint"></el-input>
</el-form-item>
<el-form-item label="AccessKeyId">
<el-input v-model="dataForm.aliyunAccessKeyId" placeholder="Alibaba Cloud AccessKeyId"></el-input>
</el-form-item> 
<el-form-item label="AccessKeySecret"> 
<el-input v-model="dataForm.aliyunAccessKeySecret" placeholder="Alibaba Cloud AccessKeySecret"></el-input> 
</el-form-item> 
<el-form-item label="BucketName"> 
<el-input v-model="dataForm.aliyunBucketName" placeholder="Alibaba Cloud BucketName"></el-input> 
</el-form-item> 
</template>
     <template v-else-if="dataForm.type === 3">
<el-form-item label="Domain name">
<el-input v-model="dataForm.qcloudDomain" placeholder="Domain name bound to Tencent Cloud"></el-input>
</el-form-item>
<el-form-item label="Path prefix">
<el-input v-model="dataForm.qcloudPrefix" placeholder="Default is empty if not set"></el-input>
</el-form-item>
<el-form-item label="AppId">
<el-input v-model="dataForm.qcloudAppId" placeholder="Tencent Cloud AppId"></el-input>
</el-form-item>
<el-form-item label="SecretId">
<el-input v-model="dataForm.qcloudSecretId" placeholder="Tencent Cloud SecretId"></el-input>
</el-form-item>
<el-form-item label="SecretKey">
<el-input v-model="dataForm.qcloudSecretKey" placeholder="Tencent Cloud SecretKey"></el-input>
</el-form-item>
<el-form-item label="BucketName">
<el-input v-model="dataForm.qcloudBucketName" placeholder="Tencent CloudBucketName"></el-input>
</el-form-item>
<el-form-item label="Bucket Region">
<el-input v-model="dataForm.qcloudRegion" placeholder="such as: sh (optional value, South China: gz North China: tj East China: sh)"></el-input>
</el-form-item>
</template>
    </el-form> 
<span slot="footer" class="dialog-footer"> 
<el-button @click="visible = false">Cancel</el-button> 
<el-button type="primary" @click="dataFormSubmit()">OK</el-button> 
</span> 
</el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {},
        dataRule: {}
      }
    },
    methods: {
      init (id) {
        this.visible = true
        this.$http({
          url: this.$http.adornUrl('/sys/oss/config'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          this.dataForm = data && data.code === 0 ? data.config : []
        })
      },
     // form submission 
dataFormSubmit () { 
this.$refs['dataForm'].validate((valid) => { 
if (valid) { 
this.$http({ 
url: this.$http.adornUrl('/sys/oss/saveConfig'), 
method: 'post', 
data: this.$http.adornData(this.dataForm) 
}).then(({data}) => { 
if (data && data.code === 0) { 
this.$message({ 
message: 'Operation successful',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
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

