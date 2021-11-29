// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import './plugins/axios.js'
import './plugins/element.js'
import store from './store'
import Video from 'video.js'
import 'video.js/dist/video-js.css'





Vue.config.productionTip = false
Vue.prototype.$video = Video


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
