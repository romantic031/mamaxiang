<template>
	<view>
		<user v-if="!token"></user>
		<userinfo v-if="token"></userinfo>
	</view>
</template>
	
<script>
	// import badgeMix from '@/mixins/tabbar-badge.js'
	// 1. 从 vuex 中按需导入 mapState 辅助函数
	
	// import { mapState, mapGetters,mapActions, mapMutations } from 'vuex'
	
	export default {
	  // mixins: [badgeMix],
	  
	 //  computed:  mapState({
		//     // 从state中拿到数据 箭头函数可使代码更简练
		//     token: state => state.token
		// }), 
	  data() {
	    return {
			token:uni.getStorageSync('token')
		}
	  },
			
	}
</script>

<style>
</style>