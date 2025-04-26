<template>
  <div class="mod-demo-ueditor">
    <el-alert
      title="Hint："
      type="warning"
      :closable="false">
    </el-alert>

    <script :id="ueId" class="ueditor-box" type="text/plain" style="width: 100%; height: 260px;">hello world!</script>
    <!-- Get content -->
<p><el-button @click="getContent()">Get content</el-button></p>
<el-dialog
title="Content"
      :visible.sync="dialogVisible"
      :append-to-body="true">
      {{ ueContent }}
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import ueditor from 'ueditor'
  export default {
    data () {
      return {
        ue: null,
        ueId: `J_ueditorBox_${new Date().getTime()}`,
        ueContent: '',
        dialogVisible: false
      }
    },
    mounted () {
      this.ue = ueditor.getEditor(this.ueId, {
        // serverUrl: '', //Server unified request interface path
        zIndex: 3000
      })
    },
    methods: {
      getContent () {
        this.dialogVisible = true
        this.ue.ready(() => {
          this.ueContent = this.ue.getContent()
        })
      }
    }
  }
</script>

<style lang="scss">
  .mod-demo-ueditor {
    position: relative;
    z-index: 510;
    > .el-alert {
      margin-bottom: 10px;
    }
  }
</style>
