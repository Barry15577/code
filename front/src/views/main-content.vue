<template>
  <main class="site-content" :class="{ 'site-content--tabs': $route.meta.isTab }">
    <!-- Main entry tab s -->
    <el-tabs
      v-if="$route.meta.isTab"
      v-model="mainTabsActiveName"
      :closable="true"
      @tab-click="selectedTabHandle"
      @tab-remove="removeTabHandle">
      <el-dropdown class="site-tabs__tools" :show-timeout="0">
        <i class="el-icon-arrow-down el-icon--right"></i>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="tabsCloseCurrentHandle">Close the current tab</el-dropdown-item>
<el-dropdown-item @click.native="tabsCloseOtherHandle">Close other tabs</el-dropdown-item>
<el-dropdown-item @click.native="tabsCloseAllHandle">Close all tabs</el-dropdown-item>
<el-dropdown-item @click.native="refresh()">Refresh the current tab</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <el-tab-pane
        v-for="item in mainTabs"
        :key="item.name"
        :label="item.title"
        :name="item.name">
        <el-card :body-style="siteContentViewHeight">
          <iframe
            v-if="item.type === 'iframe'"
            :src="item.iframeUrl"
            width="100%" height="100%" frameborder="0" scrolling="yes">
          </iframe>
          <keep-alive v-else>
            <router-view v-if="item.name === mainTabsActiveName" />
          </keep-alive>
        </el-card>
      </el-tab-pane>
    </el-tabs>
   <!-- Main entry tab e -->
    <el-card v-else :body-style="siteContentViewHeight">
      <keep-alive>
        <router-view />
      </keep-alive>
    </el-card>
  </main>
</template>

<script>
  import { isURL } from '@/utils/validate'
  export default {
    inject: ['refresh'],
    data () {
      return {
      }
    },
    computed: {
      documentClientHeight: {
        get () { return this.$store.state.common.documentClientHeight }
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
      },
      siteContentViewHeight () {
        var height = this.documentClientHeight - 50 - 30 - 2
        if (this.$route.meta.isTab) {
          height -= 40
          return isURL(this.$route.meta.iframeUrl) ? { height: height + 'px' } : { minHeight: height + 'px' }
        }
        return { minHeight: height + 'px' }
      }
    },
    methods: {
     // tabs, select the tab
      selectedTabHandle (tab) {
        tab = this.mainTabs.filter(item => item.name === tab.name)
        if (tab.length >= 1) {
          this.$router.push({ name: tab[0].name, query: tab[0].query, params: tab[0].params })
        }
      },
      // tabs, Deletetab
      removeTabHandle (tabName) {
        this.mainTabs = this.mainTabs.filter(item => item.name !== tabName)
        if (this.mainTabs.length >= 1) {
          // The currently selected tab is deleted
          if (tabName === this.mainTabsActiveName) {
            var tab = this.mainTabs[this.mainTabs.length - 1]
            this.$router.push({ name: tab.name, query: tab.query, params: tab.params }, () => {
              this.mainTabsActiveName = this.$route.name
            })
          }
        } else {
          this.menuActiveName = ''
          this.$router.push({ name: 'home' })
        }
      },
      // tabs, close current
tabsCloseCurrentHandle () {
this.removeTabHandle(this.mainTabsActiveName)
},
// tabs, close others
tabsCloseOtherHandle () {
this.mainTabs = this.mainTabs.filter(item => item.name === this.mainTabsActiveName)
},
// tabs, close all
tabsCloseAllHandle () {
this.mainTabs = []
this.menuActiveName = ''
this.$router.push({ name: 'home' })
},
// tabs, refresh current
      tabsRefreshCurrentHandle () {
        var tab = this.$route
        this.removeTabHandle(tab.name)
        this.$nextTick(() => {
          this.$router.push({ name: tab.name, query: tab.query, params: tab.params })
        })
      }
    }
  }
</script>

