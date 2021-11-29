import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userId:'',
    userName:'',
    adminId:'',
    adminName:'',
  },
  mutations: {
    setUserId(state,userId){
      state.userId = userId
    },
    setUserName(state,userName){
      state.userName = userName
    },
    setAdminId(state,adminId){
      state.adminId=adminId
    },
    setAdminName(state,adminName){
      state.adminName=adminName
    }
  },
  getters:{
    getUserId:state => state.userId,
    getUserName:state => state.userName,
    getAdminId:state => state.adminId,
    getAdminName:state => state.adminName,
  },
  actions: {
  },
  modules: {
  },
})
