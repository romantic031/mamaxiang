import Vue from 'vue'
import	Vuex from 'vuex'
Vue.use(Vuex)
//用户
import user from './modules/user.js'
const store = new Vuex.Store({
    state: {
		//公共的变量，这里的变量不能随便修改，只能通过触发mutations的方法才能改变
		//登录状态
		loginStatus:false,
		token:null,
		//用户信息(昵称/头像)
		userInfo:{}
	},
    mutations: {
		//相当于同步的操作
	},
    actions: {
		//相当于异步的操作,不能直接改变state的值，只能通过触发mutations的方法才能改变
	}
})
export default store
