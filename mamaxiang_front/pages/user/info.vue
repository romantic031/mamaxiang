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
			<view style="color: bisque;">个人信息</view>
		</view>
		<!-- 模拟弹出层 -->
		<!-- 修改昵称 -->
		<u-modal :show="nickNameshow" :title="title.titleNickName" width="300px"
				 content='' 
				 showCancelButton='true'
				 :closeOnClickOverlay="true"  @close="close" @cancel="cancel"
				 @confirm="submitNickName()">
				 <textarea class="popup_textarea" placeholder='输入要修改的信息' style="border: 1px solid #CCCCCC; width: 630rpx; height: 120rpx;" v-model="update.infoNickName">
				 						</textarea>
		</u-modal>
		<!-- 修改账号 -->
		<u-modal :show="accountshow" :title="title.titleAccount" width="300px"
				 content='' 
				 showCancelButton='true'
				 :closeOnClickOverlay="true"  @close="close" @cancel="cancel"
				 @confirm="submitAccount()">
				 <textarea class="popup_textarea" placeholder='输入要修改的信息' style="border: 1px solid #CCCCCC; width: 630rpx; height: 120rpx;" v-model="update.infoAccount">
				 						</textarea>
		</u-modal>
		<!-- 修改手机号 -->
		<u-modal :show="phoneshow" :title="title.titlePhone" width="300px"
				 content='' 
				 showCancelButton='true'
				 :closeOnClickOverlay="true"  @close="close" @cancel="cancel"
				 @confirm="submitPhone()">
				 <textarea class="popup_textarea" placeholder='输入要修改的信息' style="border: 1px solid #CCCCCC; width: 630rpx; height: 120rpx;" v-model="update.infoPhone">
				 						</textarea>
		</u-modal>
		<!--内容列表-->
		<view class="my-content">
				<view class="my-content-item" @click="editNickName()">
					<view class="left-name">昵称</view>
					<view class="item-font" >{{user.nickName}}<span class="symbol">></span></view>
				</view>
				<view class="my-content-item" @click="editAccount()">
					<view class="left-name">账号</view>
					<view class="item-font" >{{user.account}}<span class="symbol">></span></view>
				</view>
				<view class="my-content-item" @click="editPhone()">
					<view class="left-name">手机号</view>
					<view class="item-font" >{{user.phone}}<span class="symbol">></span></view>
				</view>
				<view class="my-content-item">
					<view class="left-name">是否是会员</view>
					<view class="item-font" v-if="user.vip==1"><span class="symbol">是</span></view>
					<view class="item-font" v-if="user.vip==0"><span class="symbol">否</span></view>
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
					account:"",
					phone:"",
					vip:""
				},
				userVip:{
					start:"",
					end:""
				},
				update:{
					infoNickName:"",
					infoAccount:"",
					infoPhone:""
				},
				//弹窗是否显示 默认值
				nickNameshow:false,
				// modelshow:false,
				accountshow:false,
				phoneshow:false,
				title:{
					titleNickName:'编辑昵称',
					titleAccount:'编辑账号',
					titlePhone:'编辑手机号'
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
					this.user.account=res.data.data.account,
					this.user.vip=res.data.data.isVip,
					this.user.phone=res.data.data.phone
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
			 // 单机打开编辑弹窗  开始
			 edit(){
				 this.modelshow=true
			 },
			 editNickName(){
				 this.nickNameshow=true
			 },
			 editAccount(){
				 this.accountshow=true
			 },
			 editPhone(){
				this.phoneshow=true
			 },
			 // 单机打开编辑弹窗  结束
			 // 取消回调
			 cancel() {
				// this.modelshow = false
				this.nickNameshow=false
				this.accountshow=false
			},
			 close() {
				// this.modelshow = false
				this.nickNameshow=false
				this.accountshow=false
				this.phoneshow=false
			},
			 // 确认回调
			 submitNickName(){
				 const res =this.$myRequest({
				 		url:'/api/users/updateNickName',
				 		method:'PUT',
						data:{nickname:this.update.infoNickName}
				 	})
					// console.log(res)
					this.close()
					this.getUserInfo(); 	
			 },
			 submitAccount(){
				 const res =this.$myRequest({
						url:'/api/users/updateAccount',
						method:'PUT',
						data:{account:this.update.infoAccount}
					})
					// console.log(res)
					this.close()
					this.getUserInfo(); 	
			 },
			 submitPhone(){
				 if(this.update.infoPhone.length!=11){
					 uni.showToast({
						icon:'none',
					 	title: '手机号不符合规则'
					 });
				 }else{
				 const res =this.$myRequest({
						url:'/api/users/phone',
						method:'PUT',
						data:{phone:this.update.infoPhone}
					})
					// console.log(res)
					this.close()
					this.getUserInfo(); 	
					}
			 },
			 getImage(image){
				return `http://activate.navicat.com:8081/api/resources/image?name=${image}`
			},
			
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
.left-name{
	color: #CCCCCC;
}
.item-font{
	/* background-color: #CCCCCC; */
	/* width: 80rpx; */
	text-align: center;
	/* color: #FFFFFF; */
	font-size: 30rpx;
	height: 50rpx;
	line-height: 40rpx;
}
.symbol{
	color: #CCCCCC;
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
.btn{
	font-size: 25rpx;
}
</style>