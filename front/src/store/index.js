import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token : null,
    user : {
      id : "",
      auth : ""
    }
  },
  mutations: {
    userInfo(state, payload) {
      state.user.id = payload.id;
      state.user.auth = payload.auth;
    }
  },
  actions: {
  },
  modules: {
  }
})
