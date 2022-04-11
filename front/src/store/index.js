import Vue from 'vue'
import Vuex from 'vuex'
// import app from './modules/app'
// import tagsView from './modules/tagsView'
// import permission from './modules/permission'
// import settings from './modules/settings'
import getters from './getters'
import user from './module/user'  /*引入user module*/

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user
  }
  //getters
})

export default store
