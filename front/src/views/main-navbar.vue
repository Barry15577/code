<template>
  <nav class="site-navbar" :class="'site-navbar--' + navbarLayoutType">
    <div class="site-navbar__header">
      <h1 class="site-navbar__brand" @click="$router.push({ name: 'home' })">
        <a class="site-navbar__brand-lg" href="javascript:;">CRM System</a>
        <a class="site-navbar__brand-mini" href="javascript:;">CRM</a>
      </h1>
    </div>
    <div class="site-navbar__body clearfix">
      <el-menu
        class="site-navbar__menu"
        mode="horizontal">
        <el-menu-item class="site-navbar__switch" index="0" @click="sidebarFold = !sidebarFold">
          <icon-svg name="zhedie"></icon-svg>
        </el-menu-item>
      </el-menu>
      <el-menu
        class="site-navbar__menu site-navbar__menu--right"
        mode="horizontal">
        <el-menu-item index="1" @click="$router.push({ name: 'theme' })">
          <template slot="title">
            <el-badge value="new">
              <icon-svg name="shezhi" class="el-icon-setting"></icon-svg>
            </el-badge>
          </template>
        </el-menu-item>
        
        <el-menu-item index="2">
          <notification></notification>
        </el-menu-item>
      
        <el-menu-item class="site-navbar__avatar" index="3">
          <el-dropdown :show-timeout="0" placement="bottom">
            <span class="el-dropdown-link">
              <img src="~@/assets/img/avatar.png" :alt="userName">{{ userName }}
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="updatePasswordHandle()">Change Password</el-dropdown-item>
              <el-dropdown-item @click.native="logoutHandle()">Logout</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
      </el-menu>
    </div>
    <!-- Dialog: Change Password -->
    <update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
  </nav>
</template>

<script>
  import UpdatePassword from './main-navbar-update-password'
  import Notification from '@/components/notification'
  import { clearLoginInfo } from '@/utils'
  export default {
    data () {
      return {
        updatePassowrdVisible: false
      }
    },
    components: {
      UpdatePassword,
      Notification
    },
    computed: {
      navbarLayoutType: {
        get () { return this.$store.state.common.navbarLayoutType }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold },
        set (val) { this.$store.commit('common/updateSidebarFold', val) }
      },
      mainTabs: {
        get () { return this.$store.state.common.mainTabs },
        set (val) { this.$store.commit('common/updateMainTabs', val) }
      },
      userName: {
        get () { return this.$store.state.user.name }
      }
    },
    methods: {
      // Change password
      updatePasswordHandle () {
        this.updatePassowrdVisible = true
        this.$nextTick(() => {
          this.$refs.updatePassowrd.init()
        })
      },
      // Logout
      logoutHandle () {
        this.$confirm(`Are you sure you want to logout?`, 'Confirmation', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/sys/logout'),
            method: 'post',
            data: this.$http.adornData()
          }).then(({data}) => {
            if (data && data.code === 0) {
              clearLoginInfo()
              this.$router.push({ name: 'login' })
            }
          })
        }).catch(() => {})
      }
    }
  }
</script>

<style lang="scss">
.site-navbar {
  position: fixed;
  top: 0;
  right: 0;
  left: 0;
  z-index: 1030;
  height: 60px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
  background: linear-gradient(to right, #2c3e50, #3498db);

  &--inverse {
    background: linear-gradient(to right, #2c3e50, #34495e);
  }

  &__header {
    position: relative;
    float: left;
    width: 230px;
    height: 60px;
    background: rgba(0, 0, 0, 0.1);
  }

  &__brand {
    display: flex;
    align-items: center;
    height: 60px;
    margin: 0;
    padding: 0 20px;
    font-size: 20px;
    font-weight: bold;
    text-decoration: none;
    color: #fff;
    transition: all 0.3s;

    &:hover {
      color: #f1f1f1;
      background: rgba(0, 0, 0, 0.1);
    }

    &-lg {
      font-size: 20px;
      color: #fff;
      text-decoration: none;
    }

    &-mini {
      display: none;
      font-size: 14px;
      color: #fff;
      text-decoration: none;
    }
  }

  &__body {
    position: relative;
    margin-left: 230px;
    padding-right: 15px;
    background-color: transparent;
  }

  &__menu {
    float: left;
    background-color: transparent;
    border-bottom: 0;

    &--right {
      float: right;
    }

    .el-menu-item {
      height: 60px;
      line-height: 60px;
      color: #fff;
      border-bottom: 0;

      &:hover, &:focus {
        color: #fff;
        background-color: rgba(255, 255, 255, 0.1);
      }

      i {
        color: inherit;
      }
    }
  }

  &__avatar {
    margin: 10px 0 10px 10px;
    padding: 0 !important;

    .el-dropdown-link {
      display: flex;
      align-items: center;
      color: #fff;
      
      img {
        width: 36px;
        height: 36px;
        margin-right: 8px;
        border-radius: 100%;
        vertical-align: middle;
      }
    }
  }
}

.el-menu--horizontal {
  .el-menu-item:not(.is-disabled):hover {
    background-color: rgba(255, 255, 255, 0.1);
  }
}

.el-dropdown-menu {
  margin: 0;
  padding: 10px 0;
  border: 0;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .el-dropdown-menu__item {
    padding: 8px 20px;
    font-size: 14px;
    line-height: 1.5;
    color: #606266;

    &:hover {
      background-color: #ecf5ff;
      color: #3498db;
    }

    i {
      margin-right: 8px;
    }
  }
}
</style>
