<template>
	<view class='my'>
		<!--头部-->
		<view class='my-header'>
			<view class='header-main'>
				<!-- <view class='header-config'>
					<image class='config-img' src="/../../static/images/dog-01.jpg" mode=""></image>
			</view> -->
		<view class='header-logo'>
			<image  v-if="user.avatar" :src="getImage(user.avatar)" class="logo-img"></image>
			<!-- <image  v-if="user.avatar" :src="QRImg" class="logo-img"></image> -->
			<image v-if="!token" class='logo-img' src="/../../static/images/dog-01.jpg" mode=""></image>
					<view class='logo-name' style="padding-left: 40rpx;">{{user.nickName}}</view>
				</view>
			</view>
				
		</view>
		<view class="header-config">
			<view v-if="user.vip==1" class="header-msg" >
				您的会员{{userVip.end}}过期
			</view>
			<view v-if="user.vip==0" class="header-msg">你还没加入会员
			</view>
		</view>
		<view class="my-content-item" style="padding-left: 20rpx ;">
			<view style="color: bisque;">VIP套餐</view>
			<view></view>
		</view>
		<!--内容列表-->
		<view class="my-content">
				<view class="my-content-item" >
					<view>3个月仅45元【点此参加】</view>
					<view class="item-font" @click="becomeVip(90)">开通</view>
				</view>
				<view class="my-content-item">
					<view>1个月25元</view>
					<view class="item-font" @click="becomeVip(30)">开通</view>
				</view>
				<view class="my-content-item">
					<view>2个月48元</view>
					<view class="item-font" @click="becomeVip(60)">开通</view>
				</view>
				<view class="my-content-item">
					<view>6个月128元</view>
					<view class="item-font" @click="becomeVip(180)">开通</view>
				</view>
				<view class="my-content-item">
					<view>年费228元</view>
					<view class="item-font" @click="becomeVip(360)">开通</view>
				</view>
		</view>
	</view>
</template>
<script>
	export default {
		name:"vip",
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
			// if(this.user.avatar){
				
			// }
		},
		// onReady(){
		// 	this.getImg()
		// }
		methods:{
			refresh() {
			   uni.redirectTo({
			       url: ''
			   });
			},
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
			async becomeVip(days){
				const res = await this.$myRequest({
						url:'/api/userVip/becomeVip?day='+days,
						method:'PUT',
						// data:{
						// 	day:days
						// }
					})
					uni.showToast({
						title:res.data.msg,
						icon:"none"
					})
					if(res.data.code==200){
						// location.reload()
						this.refresh()
					}
					console.log(res)
				},
			 getImage(image){
				return `http://activate.navicat.com:8081/api/resources/image?name=${image}`
				// const res =  this.$myRequest({
				// 		url:'/api/resources/image?name='+image,
				// 		// responseType: "arraybuffer", //这是必要的一步,responseType必须设置为arraybuffer
				// 		// data:{
				// 		// 	name:this.user.avatar
				// 		// }
				// 		// header:uni.getStorageSync('token')
				// })
				// return res
			},
			// async getImg() {
			//         const res =await this.$myRequest({
			// 		  url:'/api/users/download',
			//           data: {
			//             name: this.user.avatar,
			//           },
			//           responseType: "arraybuffer", //这是必要的一步,responseType必须设置为arraybuffer
			//         })
			//         console.log(res, "kdkdkd")
			//         const arrayBuffer = res.data
			//         //将arrayBuffer数据转换成base64格式即可显示
			//         this.QRImg = `data:image/jpeg;base64,${uni.arrayBufferToBase64(arrayBuffer)}`
			//       },	
		}
	}
</script>

<style scoped>
.my-header{
	/* background-image: url("../../static/images/dog-01.jpg") no-repeat; */
	/* background-color: #CCCCCC; */
	background-image: linear-gradient(to top, #9890e3 0%, #b1f4cf 100%);
	width: 100%;
	height: 280rpx;
}
.header-main{
	position: absolute;
	/* width: 100%; */
	top: 50rpx;
}
.header-config{
	position: absolute;
	left: 40rpx;
}
.header-logo{
	/* position: absolute;
	left: 50%;
	margin-left:-60rpx;
	width: 120rpx; */
	/* padding-left: 300rpx; */
}
.config-img{
	width: 40rpx;
	height: 40rpx;
}
.logo-img{
	width: 120rpx;
	height: 120rpx;
	margin-left: 50rpx;
	border: 2rpx solid #CCCCCC;
	border-radius: 50%;
	background-color: #FFFFFF;
}
.order-title{
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 20rpx;
}
.order-list{
	padding: 20rpx;
	display: flex;
}
.order-itm{
	flex:1;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items:center;
}
.order-img{
	width: 66rpx;
	height: 66rpx;
}
.my-content{
	margin: 20rpx 0;
	padding: 0 20rpx;
}
.my-content-item{
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding:20rpx 0;
	border-bottom: 2px solid #CCCCCC;
	/* background-color:  bisque;"; */
}
.item-font{
	background-color: #CCCCCC;
	width: 80rpx;
	text-align: center;
	color: #FFFFFF;
	font-size: 25rpx;
	height: 40rpx;
	line-height: 40rpx;
}
.header-config{
		position: absolute;
		left: 50%;
		right: 5%;
		top: 7%;
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
</style>