<template>
	<view class="home">
		<u-swiper 
			      :list="imgList"
				  indicator
				  indicatorMode="dot"	  
		></u-swiper>
		<view class="nav"> 
		<view class="nav_view01" >
			<text v-if="!token">下午好,请登录------></text>
			<text v-if="token">下午好,{{user.nickName}}</text>
			<!-- <v-if="token"  ></v-if> -->
			<button v-if="!token" size="mini" plain="true" @tap="goMy">登录</button>
			<button v-if="token" size="mini" plain="true" @tap="goMy">我的</button>
		</view>
		<view class="nav_view02"><button @click="toStore">
		<span style="font-size: 40px;" class="iconfont icon-shiiocn02"></span><text>开始点餐Order Now</text></button>
		</view>
		<navigator class="navgtorbar" url="/pages/vip/vip" style="margin-left: 140rpx;">
			<span style="font-size: 35px;" class="iconfont icon-baobei"></span><div>会员</div>
		</navigator>
		<navigator class="navgtorbar" url="/pages/order/order" style="margin-left: 200rpx;" @click="toHistoricalOrder">
			<span style="font-size: 35px; margin-left: 25rpx;" class="iconfont icon-wenzhang" ></span><div>我的订单</div>
		</navigator>
		</view>
		<view>
			<image style="width: 355rpx; height: 500rpx;" src="/../../static/images/dog-01.jpg"></image>
			<image style="width: 355rpx; margin-left: 10rpx; height: 500rpx;" src="/../../static/images/dog-02.jpg"></image>
		</view>
		
	</view>
</template>
<script>
	export default {
		data() {
			return {
				token:uni.getStorageSync('token'),
				imgList:[
					'/../../static/images/backiee-01.jpg',
					'/../../static/images/backiee-02.jpg',
					'/../../static/images/backiee-03.jpg'
				],
				username:"",
				user:{
					nickName:""
				}
			}
		},
		onReady() {
			if(this.token){
				this.getUserInfo()	
			}
		},
		methods: {
			toStore(){
				uni.navigateTo({
					url:"/pages/store/store"
				})
			},
			goMy(){
				uni.navigateTo({
					url:"/pages/user/my"
				})
			},
			toHistoricalOrder(){
				uni.navigateTo({
					url:"/pages/order/historicalOrders"
				})
			},
			// if(token){
			// 	this.getUserInfo()
			// },
			async getUserInfo(){
				const res = await this.$myRequest({
						url:'/api/users/userInfo',
						// header:uni.getStorageSync('token')
				})
				console.log(res)
				if(res.data.code==200){
					this.user.nickName=res.data.data.nickname
				}
			},	
		}
	}
	
</script>

<style>
	@import url("../../style/index.css");
</style>
