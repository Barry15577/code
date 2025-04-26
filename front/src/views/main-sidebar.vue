<template>
  <aside class="site-sidebar" :class="'site-sidebar--' + sidebarLayoutSkin">
    <div class="site-sidebar__inner">
      <el-menu
        :default-active="menuActiveName || 'home'"
        :collapse="sidebarFold"
        :collapseTransition="false"
        class="site-sidebar__menu">
        <el-menu-item index="home" @click="$router.push({ name: 'home' })">
          <icon-svg name="shouye" class="site-sidebar__menu-icon"></icon-svg>
          <span slot="title">Dashboard</span>
        </el-menu-item>
      
        <sub-menu
          v-for="menu in menuList"
          :key="menu.menuId"
          :menu="menu"
          :dynamicMenuRoutes="dynamicMenuRoutes">
        </sub-menu>
      </el-menu>
    </div>
  </aside>
</template>

<script>
  import SubMenu from './main-sidebar-sub-menu'
  import { isURL } from '@/utils/validate'
  export default {
    data () {
      return {
        dynamicMenuRoutes: []
      }
    },
    components: {
      SubMenu
    },
    computed: {
      sidebarLayoutSkin: {
        get () { return this.$store.state.common.sidebarLayoutSkin }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold }
      },
      menuList: {
        get () { return this.$store.state.common.menuList },
        set (val) { this.$store.commit('common/updateMenuList', val) }
      },
      menuActiveName: {
        get () { return this.$store.state.common.menuActiveName },
        set (val) { this.$store.commit('common/updateMenuActiveName', val) }
      },
      mainTabs: {
        get () { return this.$store.state.common.mainTabs },
        set (val) { this.$store.commit('common/updateMainTabs', val) }
      },
      mainTabsActiveName: {
        get () { return this.$store.state.common.mainTabsActiveName },
        set (val) { this.$store.commit('common/updateMainTabsActiveName', val) }
      }
    },
    watch: {
      $route: 'routeHandle'
    },
    created () {
      this.menuList = JSON.parse(sessionStorage.getItem('menuList') || '[]')
      this.dynamicMenuRoutes = JSON.parse(sessionStorage.getItem('dynamicMenuRoutes') || '[]')
      this.routeHandle(this.$route)
    },
    methods: {
      // Route operation
      routeHandle (route) {
        if (route.meta.isTab) {
          // Select tab, add if it doesn't exist
          var tab = this.mainTabs.filter(item => item.name === route.name)[0]
          if (!tab) {
            if (route.meta.isDynamic) {
              route = this.dynamicMenuRoutes.filter(item => item.name === route.name)[0]
              if (!route) {
                return console.error('Could not find available tab!')
              }
            }
            tab = {
              menuId: route.meta.menuId || route.name,
              name: route.name,
              title: route.meta.title,
              type: isURL(route.meta.iframeUrl) ? 'iframe' : 'module',
              iframeUrl: route.meta.iframeUrl || '',
              params: route.params,
              query: route.query
            }
            this.mainTabs = this.mainTabs.concat(tab)
          }
          this.menuActiveName = tab.menuId + ''
          this.mainTabsActiveName = tab.name
        }
      }
    }
  }
</script>

<style lang="scss">
.site-sidebar {
  position: fixed;
  top: 60px;
  left: 0;
  bottom: 0;
  z-index: 1020;
  width: 230px;
  overflow: hidden;
  background-color: #2c3e50;
  transition: width 0.3s;

  &--dark {
    background-color: #2c3e50;

    .site-sidebar__menu {
      background-color: transparent;
      border-right: 0;

      .el-menu-item, .el-submenu__title {
        color: #fff;
        
        i {
          color: inherit;
        }

        &:hover, &:focus {
          color: #fff;
          background-color: rgba(255, 255, 255, 0.1);
        }
      }

      .el-menu-item.is-active, 
      .el-submenu.is-active .el-submenu__title {
        color: #3498db;
        background-color: rgba(52, 152, 219, 0.1);

        i {
          color: inherit;
        }
      }
    }

    .el-submenu .el-menu {
      background-color: rgba(0, 0, 0, 0.15);
    }
  }

  &--light {
    background-color: #fff;
    border-right: 1px solid #dcdfe6;

    .site-sidebar__menu {
      background-color: transparent;
      border-right: 0;

      .el-menu-item, .el-submenu__title {
        color: #2c3e50;
        
        i {
          color: inherit;
        }

        &:hover, &:focus {
          color: #3498db;
          background-color: #ecf5ff;
        }
      }

      .el-menu-item.is-active,
      .el-submenu.is-active .el-submenu__title {
        color: #3498db;
        background-color: #ecf5ff;

        i {
          color: inherit;
        }
      }
    }

    .el-submenu .el-menu {
      background-color: #f5f7fa;
    }
  }

  &__inner {
    position: relative;
    z-index: 1;
    width: 250px;
    height: 100%;
    padding-bottom: 30px;
    overflow-y: auto;
  }

  &__menu {
    width: 230px;
    border-right: 0;

    &:not(.el-menu--collapse) {
      width: 230px;
    }
  }

  &__menu-icon {
    width: 24px;
    margin-right: 5px;
    text-align: center;
    font-size: 16px;
    color: inherit;
  }
}

.site-sidebar--fold {
  .site-sidebar__menu {
    width: 64px;
  }

  .site-sidebar__menu-icon {
    margin-right: 0;
  }
}

.el-menu--vertical {
  .el-menu--popup {
    min-width: 200px;
    padding: 0;
    border: 0;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }

  .el-menu-item, .el-submenu__title {
    height: 40px;
    line-height: 40px;
    padding: 0 20px;
    font-size: 14px;
    color: #606266;

    &:hover, &:focus {
      color: #3498db;
      background-color: #ecf5ff;
    }

    i {
      margin-right: 8px;
      color: inherit;
    }
  }

  .el-menu-item.is-active {
    color: #3498db;
    background-color: #ecf5ff;

    i {
      color: inherit;
    }
  }
}
</style>
