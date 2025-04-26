export default {
  namespaced: true,
state: {
// The visible height of the page document (changes with the window size)
documentClientHeight: 0,
// Navigation bar, layout style, default (default) / inverse (reverse)
navbarLayoutType: 'default',
// Sidebar, layout skin, light (light color) / dark (black)
sidebarLayoutSkin: 'dark',
// Sidebar, folded state
sidebarFold: false,
// Sidebar, menu
menuList: [],
menuActiveName: '',
// Content, whether to refresh
contentIsNeedRefresh: false,
// Main entry tab page
mainTabs: [],
mainTabsActiveName: ''
},
  mutations: {
    updateDocumentClientHeight (state, height) {
      state.documentClientHeight = height
    },
    updateNavbarLayoutType (state, type) {
      state.navbarLayoutType = type
    },
    updateSidebarLayoutSkin (state, skin) {
      state.sidebarLayoutSkin = skin
    },
    updateSidebarFold (state, fold) {
      state.sidebarFold = fold
    },
    updateMenuList (state, list) {
      state.menuList = list
    },
    updateMenuActiveName (state, name) {
      state.menuActiveName = name
    },
    updateContentIsNeedRefresh (state, status) {
      state.contentIsNeedRefresh = status
    },
    updateMainTabs (state, tabs) {
      state.mainTabs = tabs
    },
    updateMainTabsActiveName (state, name) {
      state.mainTabsActiveName = name
    }
  }
}
