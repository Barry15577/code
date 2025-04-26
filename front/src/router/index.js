rt Vue from 'vue'
import Router from 'vue-router'
import http from '@/utils/httpRequest'
import { isURL } from '@/utils/validate'
import { clearLoginInfo } from '@/utils'

Vue.use(Router)


const _import = require('./import-' + process.env.NODE_ENV)


const globalRoutes = [
  { path: '/404', component: _import('common/404'), name: '404', meta: { title: '404未找到' } },
  { path: '/login', component: _import('common/login'), name: 'login', meta: { title: '登录' } }
]


const mainRoutes = {
  path: '/',
  component: _import('main'),
  name: 'main',
  redirect: { name: 'home' },
  meta: { title: 'Main entrance overall layout' },
  children: [
// Set the route display mode through the meta object
// 1. isTab: whether to display content through tab, true: yes, false: no
// 2. iframeUrl: whether to display content through iframe nesting, 'starting with http[s]://': yes, '': no
// Tip: If you need to display content through iframe nesting, but not open it through tab, please create a component yourself to use iframe processing!
    { path: '/home', component: _import('common/home'), name: 'home', meta: { title: '首页' } },
    { path: '/theme', component: _import('common/theme'), name: 'theme', meta: { title: '主题' } },
    
  ],
  beforeEnter (to, from, next) {
    let token = Vue.cookie.get('token')
    if (!token || !/\S/.test(token)) {
      clearLoginInfo()
      next({ name: 'login' })
    }
    next()
  }
}

const router = new Router({
mode: 'hash',
scrollBehavior: () => ({ y: 0 }),
isAddDynamicMenuRoutes: false, // Has the dynamic (menu) route been added?
routes: globalRoutes.concat(mainRoutes)
})

router.beforeEach((to, from, next) => {
// Add dynamic (menu) routes
// 1. Already added or global route, direct access
// 2. Get the menu list, add and save local storage
if (router.options.isAddDynamicMenuRoutes || fnCurrentRouteType(to, globalRoutes) === 'global') {
next()
  } else {
    http({
      url: http.adornUrl('/sys/menu/nav'),
      method: 'get',
      params: http.adornParams()
    }).then(({data}) => {
      if (data && data.code === 0) {
        fnAddDynamicMenuRoutes(data.menuList)
        router.options.isAddDynamicMenuRoutes = true
        sessionStorage.setItem('menuList', JSON.stringify(data.menuList || '[]'))
        sessionStorage.setItem('permissions', JSON.stringify(data.permissions || '[]'))
        next({ ...to, replace: true })
      } else {
        sessionStorage.setItem('menuList', '[]')
        sessionStorage.setItem('permissions', '[]')
        next()
      }
    }).catch((e) => {
console.log(`%c${e} Failed to request menu list and permissions, jump to login page!!`, 'color:blue')
router.push({ name: 'login' })
    })
  }
})

/**
* Determine the current route type, global: global route, main: main entry route
* @param {*} route current route
*/
function fnCurrentRouteType (route, globalRoutes = []) {
var temp = []
for (var i = 0; i < globalRoutes.length; i++) {
if (route.path === globalRoutes[i].path) {
return 'global'
} else if (globalRoutes[i].children && globalRoutes[i].children.length >= 1) {
temp = temp.concat(globalRoutes[i].children)
}
}
return temp.length >= 1 ? fnCurrentRouteType(route, temp) : 'main'
}

/**
* Add dynamic (menu) routes
* @param {*} menuList menu list
* @param {*} routes Recursively created dynamic (menu) routes
*/
function fnAddDynamicMenuRoutes (menuList = [], routes = []) {
  var temp = []
  for (var i = 0; i < menuList.length; i++) {
    if (menuList[i].list && menuList[i].list.length >= 1) {
      temp = temp.concat(menuList[i].list)
    } else if (menuList[i].url && /\S/.test(menuList[i].url)) {
      menuList[i].url = menuList[i].url.replace(/^\//, '')
      var route = {
        path: menuList[i].url.replace('/', '-'),
        component: null,
        name: menuList[i].url.replace('/', '-'),
        meta: {
          menuId: menuList[i].menuId,
          title: menuList[i].name,
          isDynamic: true,
          isTab: true,
          iframeUrl: ''
        }
      }
   // The url starts with http[s]:// and is displayed through iframe 
if (isURL(menuList[i].url)) { 
route['path'] = `i-${menuList[i].menuId}` 
route['name'] = `i-${menuList[i].menuId}` 
route['meta']['iframeUrl'] = menuList[i].url 
} else { 
try { 
route['component'] = _import(`modules/${menuList[i].url}`) || null 
} catch (e) {} 
} 
routes.push(route) 
} 
} 
if (temp. length >= 1) { 
fnAddDynamicMenuRoutes(temp, routes) 
} else { 
mainRoutes.name = 'main-dynamic' 
mainRoutes.children = routes 
router.addRoutes([ 
mainRoutes, { path: '*', redirect: { name: '404' } }
])
sessionStorage.setItem('dynamicMenuRoutes', JSON.stringify(mainRoutes.children || '[]'))
console.log('\n')
console.log('%c!<-------------------- Dynamic (menu) route s -------------------->', 'color:blue')
console.log(mainRoutes.children)
console.log('%c!<-------------------- Dynamic (menu) route e -------------------->', 'color:blue')
}
}
export default router
