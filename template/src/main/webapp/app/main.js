import Vue from 'vue'

import WebSocket from './websocket'
Vue.use(WebSocket, process.env.BASE_API + '/websocket/tracker')

import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import i18n from './i18n' // 国际化
import App from './App'
import router from './router'
import store from './store'
import * as filters from './filters' // 全局filter
import './icons' // icon
import './errorLog'// error log
import './permission' // 权限
import './mock' // 该项目所有请求使用mockjs模拟

// 使用lodash
import _ from 'lodash'
Vue.prototype._ = _

Vue.use(Element, {
  i18n: (key, value) => i18n.t(key, value)
})

// register global utility filters.
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

import Morphling from 'morphling'
Vue.use(Morphling)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  template: '<App/>',
  components: { App }
})
