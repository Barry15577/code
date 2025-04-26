<template>
  <el-dialog
    title="Upload file"
    :close-on-click-modal="false"
    @close="closeHandle"
    :visible.sync="visible">
    <el-upload
      drag
      :action="url"
      :before-upload="beforeUploadHandle"
      :on-success="successHandle"
      multiple
      :file-list="fileList"
      style="text-align: center;">
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">Drag the file here, or <em>click to upload</em></div>
<div class="el-upload__tip" slot="tip">Only supports jpg, png, and gif format images! </div>
    </el-upload>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        url: '',
        num: 0,
        successNum: 0,
        fileList: []
      }
    },
    methods: {
      init (id) {
        this.url = this.$http.adornUrl(`/sys/oss/upload?token=${this.$cookie.get('token')}`)
        this.visible = true
      },
     // Before uploading
beforeUploadHandle (file) {
if (file.type !== 'image/jpg' && file.type !== 'image/jpeg' && file.type !== 'image/png' && file.type !== 'image/gif') {
this.$message.error('Only support jpg, png, gif format images!')
return false
        }
        this.num++
      },
      // Upload successful
successHandle (response, file, fileList) {
this.fileList = fileList
this.successNum++
if (response && response.code === 0) {
if (this.num === this.successNum) {
this.$confirm('Operation successful, do you want to continue?', 'Prompt', {
confirmButtonText: 'Confirm',
cancelButtonText: 'Cancel',
type: 'warning'
            }).catch(() => {
              this.visible = false
            })
          }
        } else {
          this.$message.error(response.msg)
        }
      },
     // When the popup window is closed
      closeHandle () {
        this.fileList = []
        this.$emit('refreshDataList')
      }
    }
  }
</script>
