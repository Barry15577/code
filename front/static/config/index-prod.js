/**
* Production environment
*/
;(function () {
window.SITE_CONFIG = {};

// api interface request address
window.SITE_CONFIG['baseUrl'] = 'http://demo.open.renren.io/renren-fast-server';

// cdn address = domain name + version number
window.SITE_CONFIG['domain'] = './'; // domain name
window.SITE_CONFIG['version'] = ''; // version number (year, month, day, hour, minute)
window.SITE_CONFIG['cdnUrl'] = window.SITE_CONFIG.domain + window.SITE_CONFIG.version;
})();
