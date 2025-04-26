/**
* Dynamically load initial resources
*/
;(function() {
var resList = {
icon: window.SITE_CONFIG.cdnUrl + '/static/img/favicon.ico',
css: [
window.SITE_CONFIG.cdnUrl + '/static/css/app.css',
],
js: [
// Plug-in, load before placing the business, so as to avoid errors when the business needs depend on the plug-in and it has not been loaded
// Plug-in - echarts
window.SITE_CONFIG.cdnUrl + '/static/plugins/echarts-3.8.5/echarts.common.min.js',
// Plug-in - ueditor
window.SITE_CONFIG.cdnUrl + '/static/plugins/ueditor-1.4.3.3/ueditor.config.js', 
window.SITE_CONFIG.cdnUrl + '/static/plugins/ueditor-1.4.3.3/ueditor.all.min.js', 
window.SITE_CONFIG.cdnUrl + '/static/plugins/ueditor-1.4.3.3/lang/zh-cn/zh-cn.js', 
// business 
window.SITE_CONFIG.cdnUrl + '/static/js/manifest.js', 
window.SITE_CONFIG.cdnUrl + '/static/js/vendor.js', 
window.SITE_CONFIG.cdnUrl + '/static/js/app.js' 
] 
};

 // icon 
(function () { 
var _icon = document.createElement('link'); 
_icon.setAttribute('rel', 'shortcut icon'); 
_icon.setAttribute('type', 'image/x-icon'); 
_icon.setAttribute('href', resList.icon); 
document.getElementsByTagName('head')[0].appendChild(_icon); 
})(); 

// style 
(function () { 
document.getElementsByTagName('html')[0].style.opacity = 0; 
var i = 0; 
var _style = null; 
var createStyles = function () { 
if (i >= resList.css.length) { 
document.getElementsByTagName('html')[0].style.opacity = 1; 
return; 
} 
_style = document.createElement('link'); 
_style.href = resList.css[i]; 
_style.setAttribute('rel', 'stylesheet'); 
_style.onload = function () { 
i++; 
createStyles(); 
} 
document.getElementsByTagName('head')[0].appendChild(_style); 
} 
createStyles(); 
})(); 

// script
  document.onreadystatechange = function () {
    if (document.readyState === 'interactive') {
      var i = 0;
      var _script = null;
      var createScripts = function () {
        if (i >= resList.js.length) {
          return;
        }
        _script = document.createElement('script');
        _script.src = resList.js[i];
        _script.onload = function () {
          i++;
          createScripts();
        }
        document.getElementsByTagName('body')[0].appendChild(_script);
      }
      createScripts();
    }
  };
})();
