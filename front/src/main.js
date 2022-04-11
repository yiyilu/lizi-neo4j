// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'  /*加入vuex状态管理*/
import router from './router'  /*引入路由配置*/
import echarts from 'echarts'
import RightToolbar from "@/components/RightToolbar"
import stack from 'vue-tantan-stack'
import './assets/icons'
import {resetForm, selectDictLabel, selectDictLabels} from "@/utils/ruoyi"; // icon
import VueParticles from 'vue-particles'
Vue.use(ElementUI);
Vue.use(Vuex)
Vue.use(VueParticles)
Vue.use(stack)
Vue.component('RightToolbar', RightToolbar)
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.resetForm = resetForm

Vue.config.productionTip = false

Vue.prototype.$echarts = echarts

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

/* eslint-disable no-new */
new Vue({
  router, /*使用路由配置*/
  store,  /*使用vuex进行状态管理*/
  render: h => h(App),
}).$mount("#app")
