<template>
  <el-form>
   <h2>Layout settings</h2>
<el-form-item label="Navigation bar type">
<el-radio-group v-model="navbarLayoutType">
<el-radio label="default" border>default</el-radio>
<el-radio label="inverse" border>inverse</el-radio>
</el-radio-group>
</el-form-item>
<el-form-item label="Sidebar skin">
      <el-radio-group v-model="sidebarLayoutSkin">
        <el-radio label="light" border>light</el-radio>
        <el-radio label="dark" border>dark</el-radio>
      </el-radio-group>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    computed: {
      navbarLayoutType: {
        get () { return this.$store.state.common.navbarLayoutType },
        set (val) { this.$store.commit('common/updateNavbarLayoutType', val) }
      },
      sidebarLayoutSkin: {
        get () { return this.$store.state.common.sidebarLayoutSkin },
        set (val) { this.$store.commit('common/updateSidebarLayoutSkin', val) }
      }
    }
  }
</script>

