<template>
  <div
    class="site-wrapper"
    :class="{ 'site-sidebar--fold': sidebarFold }"
    v-loading.fullscreen.lock="loading"
    element-loading-text="Loading...">
    <template v-if="!loading">
      <main-navbar />
      <main-sidebar />
      <div class="site-content__wrapper" :style="{ 'min-height': documentClientHeight + 'px' }">
        <main-content v-if="!$store.state.common.contentIsNeedRefresh" />
      </div>
    </template>
  </div>
</template>

<script>
  import MainNavbar from './main-navbar'
  import MainSidebar from './main-sidebar'
  import MainContent from './main-content'
  export default {
    provide () {
      return {
        // refresh
        refresh () {
          this.$store.commit('common/updateContentIsNeedRefresh', true)
          this.$nextTick(() => {
            this.$store.commit('common/updateContentIsNeedRefresh', false)
          })
        }
      }
    },
    data () {
      return {
        loading: true
      }
    },
    components: {
      MainNavbar,
      MainSidebar,
      MainContent
    },
    computed: {
      documentClientHeight: {
        get () { return this.$store.state.common.documentClientHeight },
        set (val) { this.$store.commit('common/updateDocumentClientHeight', val) }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold }
      },
      userId: {
        get () { return this.$store.state.user.id },
        set (val) { this.$store.commit('user/updateId', val) }
      },
      userName: {
        get () { return this.$store.state.user.name },
        set (val) { this.$store.commit('user/updateName', val) }
      }
    },
    created () {
      this.getUserInfo()
    },
    mounted () {
      this.resetDocumentClientHeight()
    },
    methods: {
     // Reset window visible height
resetDocumentClientHeight () {
this.documentClientHeight = document.documentElement['clientHeight']
window.onresize = () => {
this.documentClientHeight = document.documentElement['clientHeight']
}
},
// Get current administrator information
      getUserInfo () {
        this.$http({
          url: this.$http.adornUrl('/sys/user/info'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.loading = false
            this.userId = data.user.userId
            this.userName = data.user.username
          }
        })
      }
    }
  }
</script>

<style lang="scss">
.site-wrapper {
  position: relative;
  min-height: 100vh;
  background-color: #f5f7fa;

  &.site-sidebar--fold {
    .site-navbar__header {
      width: 64px;
      text-align: center;

      .site-navbar__brand-lg {
        display: none;
      }
      .site-navbar__brand-mini {
        display: inline-block;
      }
    }

    .site-navbar__body {
      margin-left: 64px;
    }

    .site-sidebar {
      width: 64px;

      .site-sidebar__inner {
        width: 84px;
      }

      .el-menu--collapse {
        width: 64px;
      }
    }

    .site-content__wrapper {
      margin-left: 64px;
    }
  }
}

.site-content {
  position: relative;
  padding: 15px;

  &__wrapper {
    position: relative;
    padding-top: 60px;
    margin-left: 230px;
    min-height: 100%;
    background-color: #f5f7fa;
  }
}

// Scrollbar styles
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
  background-color: #f5f7fa;
}

::-webkit-scrollbar-thumb {
  background-color: #dcdfe6;
  border-radius: 4px;

  &:hover {
    background-color: #c0c4cc;
  }
}

// Card styles
.el-card {
  border: none;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
  }

  .el-card__header {
    padding: 15px 20px;
    border-bottom: 1px solid #ebeef5;
    background-color: #fff;
  }

  .el-card__body {
    padding: 20px;
  }
}

// Table styles
.el-table {
  border-radius: 8px;
  overflow: hidden;

  th {
    background-color: #f5f7fa;
    color: #606266;
    font-weight: 600;
  }

  td, th {
    padding: 12px 0;
  }

  .el-button--text {
    padding: 0 8px;
  }
}

// Form styles
.el-form {
  .el-form-item__label {
    font-weight: 500;
  }

  .el-input__inner,
  .el-textarea__inner {
    border-radius: 4px;
    
    &:hover {
      border-color: #3498db;
    }
    
    &:focus {
      border-color: #3498db;
    }
  }
}

// Button styles
.el-button {
  border-radius: 4px;
  font-weight: 500;
  transition: all 0.3s;

  &--primary {
    background-color: #3498db;
    border-color: #3498db;

    &:hover, &:focus {
      background-color: #2980b9;
      border-color: #2980b9;
    }
  }
}
</style>
