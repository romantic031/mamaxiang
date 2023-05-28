<template>
	<view class="">
		
		<view class="my-header">
			<view class="header-main">
				<view class="header-ava">
					<!-- <image src="/static/images/dog-02.jpg" class="ava-img"></image> -->
					<image  v-if="user.avatar" :src="getImage(user.avatar)" class="logo-img"></image>
					<view class="ava-name" >{{user.nickName}}</view>
				</view>
				<!-- 判断会员 -->
				<view class="header-config">
					<view v-if="user.vip==1" class="header-msg" >
						您的会员{{userVip.end}}过期
						<view style="font-size: 30rpx; margin-top: 30rpx; border: 3rpx solid black;" @click="toVip">
							点击去续费会员
						</view>
					</view>
					<view v-if="user.vip==0" class="header-msg">你还没加入会员
					<view style="font-size: 30rpx; margin-top: 30rpx; border: 3rpx solid black;" @click="toVip">
						点击去加入会员
					</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="main">
			<view class="main-item" @click="toOrders">
				<view class="item-header">
					<view class="img1"></view>
					<view>我的订单</view>
				</view>
				<view>></view>
			</view>
			<view class="main-item" @click="toHistoricalOrder">
				<view class="item-header">
					<view class="img2"></view>
					<view>历史订单</view>
				</view>
				<view>></view>
			</view>
			<view class="main-item" @click="toInfo()">
				<view class="item-header">
					<view class="img3"></view>
					<view >我的信息</view>
				</view>
				<view  >></view>
			</view>
			<view class="main-item" @click="logout">
				<view class="item-header">
					<view class="img4"></view>
					<view style="font-weight: blod;color: black;">退出登录</view>
				</view>
				<view>></view>
			</view>
		</view>
		
			<!-- <view class="my-content">
					<view class="my-content-item" >
						<view class="item-singular" >我的订单</view>
					</view>
					<view class="my-content-item">
						<view class="item-even">历史订单</view>
					</view>
					<view class="my-content-item">
						<view class="item-singular">我的信息</view>
					</view>
					
			</view> -->
		<!-- <button @click="logout" class="logout" >退出登录</button>	 -->
	</view>
</template>

<script>
	export default {
		name:"userinfo",
		data() {
			return {
				token:uni.getStorageSync('token'),
				user:{
					nickName:"",
					avatar:"",
					vip:""
				},
				userVip:{
					start:"",
					end:""
				}
			};
		},
		onReady() {
			if(this.token){
				this.getUserInfo()	
			}
		},
		methods:{
			async getUserInfo(){
				const res = await this.$myRequest({
						url:'/api/users/userInfo',
						// header:uni.getStorageSync('token')
				})
				console.log(res)
				if(res.data.code==200){
					this.user.nickName=res.data.data.nickname,
					this.user.avatar=res.data.data.avatar,
					this.user.vip=res.data.data.isVip
					if(res.data.data.isVip==1){
						const res = await this.$myRequest({
								url:'/api/userVip/getVipInfo',
								// header:uni.getStorageSync('token')
						})
						console.log(res)
						this.userVip.start=res.data.data.startTime,
						this.userVip.end=res.data.data.endTime
					}
				}
			},
			// async getImage(){
			// 	const res = await this.$myRequest({
			// 			url:'/api/users/download',
			// 			data:{
			// 				avatar:this.user.avatar
			// 			}
			// 			// header:uni.getStorageSync('token')
			// 	})
			// 	// console.log(this.avatar)
				
			// },
			getImage(image){
							return `http://activate.navicat.com:8081/api/resources/image?name=${image}`
							},
			logout(){
					uni.removeStorageSync('token')
						uni.navigateTo({
							url:"/pages/index/index"
						})

			},
			toHistoricalOrder(){
				uni.navigateTo({
					url:"/pages/order/historicalOrders"
				})
			},
			toVip(){
				uni.navigateTo({
					url:"/pages/vip/vip"
				})
			},
			toInfo(){
				uni.navigateTo({
					url:"/pages/user/info"
				})
			},
			toOrders(){
				uni.navigateTo({
					url:"/pages/order/addOrder"
				})
			}
		}
	}
</script>

<style lang="scss">
	.my-header{
		background-image: linear-gradient(to top, #9890e3 0%, #b1f4cf 100%);
		width: 100%;
		height: 300rpx;
		position: relative;
	}
	.header-main{
		position: relative;
		top: 60rpx;
		height: 100%;
	}
	.header-config{
		position: absolute;
		left: 50%;
		right: 5%;
		top: 10%;
	}
	.header-msg{
		background-color: black;
		border: 2px solid gainsboro;
		width: 320rpx;
		font-size: 20rpx;
		height: 60rpx;
		color: white;
		padding-right: 100rpx;
		text-align: center;
		line-height: 60rpx;
		padding-left: 20rpx;
	}
	.header-ava{
		position: absolute;
		width:350rpx; 
		left: 10%;
		margin-left: -60rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	.ava-img{
		width: 120rpx;
		height: 120rpx;
		border: 2px solid #FFFFFF;
		border-radius: 50%;
		background-color: gray;
	}
	.ava-name{
		color: #FFFFFF;
		font-size: 40rpx;
		font-weight: bold;
		width: 200rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	.main{
		padding: 20rpx 10rpx;
	}
	.main-item{
		display: flex;
		justify-content: space-between;
		align-items: center;
		height: 120rpx;
		padding: 20rpx 20rpx;
		margin:20rpx 0;
		border: 2rpx solid black;
		border-radius: 24rpx;
		box-shadow: 1px 2px 5px 0 rgba(0, 0, 0, .5);
	}
	.item-header{
		display: flex;
		align-items: center;
		justify-content: center;
		font-weight: bold;
		color: gray;
	}
	.img1{
		width: 80rpx;
		height: 80rpx;
		background-color: limegreen;
		border-radius: 20rpx;
		margin-right: 15rpx;
	}
	.img2{
		width: 80rpx;
		height: 80rpx;
		background-color: skyblue;
		border-radius: 20rpx;
		margin-right: 15rpx;
	}
	.img3{
		width: 80rpx;
		height: 80rpx;
		background-color: lightyellow;
		border-radius: 20rpx;
		margin-right: 15rpx;
	}
	.img4{
		width: 80rpx;
		height: 80rpx;
		background-color: crimson;
		border-radius: 20rpx;
		margin-right: 15rpx;
	}
	.logo-img{
		width: 120rpx;
		height: 120rpx;
		margin-left: 20rpx;
		border: 2rpx solid #CCCCCC;
		border-radius: 50%;
		background-color: #FFFFFF;
	}
	
</style>