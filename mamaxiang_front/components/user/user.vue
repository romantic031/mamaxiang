<template>
	<view class="login">
		<swiper vertical="true" style="height: 100vh;">
			<swiper-item>
				<scroll-view>
					<view class="login-tel">
						<view class="tel-main"  @tap="goBack">
							<uni-icons type="close" size="30" class="close"></uni-icons>
						</view>
						<uni-icons type="contact-filled" size="100" color="#AFAFAF" class="avatar"></uni-icons>
						<view class="tel" @tap="goRegisterAccount">账号注册</view>
						<LoginOther></LoginOther>
						<view class="login-go">
							<view>已有账号, 去登录</view>
							<uni-icons type="arrowdown" size="30" ></uni-icons>
						</view>
					</view>
				</scroll-view>
			</swiper-item>
			<swiper-item>
				<scroll-view>
						<view class="login-tel">
							<view class="tel-main">
								<view class="close close-center">
									<view class="tel-main" @tap="goBack">
										<uni-icons type="close" size="30" class="close"></uni-icons>
									</view>
									<view class="login-go">
										<uni-icons type="arrowup" size="30" ></uni-icons>
										<view>没有账号, 去注册</view>
									</view>
									<view></view>
								</view>
								<view class="login-form">
									<view class="login-user">
										<text class="user-text">账号</text>
										<input type="text" v-model="account" value="" placeholder="请输入账号">
									</view>
									<view class="login-user" style="padding-top: 20rpx;">
										<text class="user-text">密码</text>
										<input type="password" v-model="userPwd" value="" placeholder="请输入6-16位字符">
									</view>
								</view>
								<view class="login-quick">
									<!-- <view>忘记密码</view> -->
									<view @tap="goPhoneLogin">免密登录</view>
								</view>
								<view class="tel" @tap="submit()">登录</view>
								<view class="reminder">温馨提示：您可以选择免密登录，更加方便</view>
								<LoginOther></LoginOther>
							</view>
						</view>
				</scroll-view>
			</swiper-item>
		</swiper>
		<!-- <button type="primary" class="btn-login">一键登录</button>	 -->
	</view>
</template>

<script>
	import LoginOther from '@/components/user/login-other.vue'
	export default {
		name:"user",
		data() {
			return {
				//账号
				account:"",
				//密码
				userPwd:"",
				//验证的正则
				rules:{
					account:{
						rule:/\S/,
						msg:"账号不能为空"
					},
					userPwd:{
						rule:/[0-9a-zA-Z]{6,16}/,
						msg:"密码应为6-16字符"
					}
				},
				
			};
		},
		components: {
			LoginOther
		},
		methods: {
			//关闭当前页面，返回上一页
			goBack(){
				uni.navigateBack({
					delta:1
				})
			},
			//点击登录
			async submit(){
				// if( this.rules.account.rule.test(account) ) return;
				if( !this.validate("account")) 	return;
				if( !this.validate("userPwd")) 	return;
				const res=await this.$myRequest({
					url:'/api/users/login',
					method:'POST',
					data:{account:this.account,password:this.userPwd}
				})
				// console.log(res);
				uni.showToast({
					title:res.data.msg,
					icon:"none"
				})
				if(res.data.code==200){
					uni.setStorageSync('token', res.data.data);
					uni.showLoading({
						title:"登录中..."
					})
					setTimeout(()=>{
						// this.
						uni.hideLoading();
						uni.navigateTo({
							url:"/pages/index/index"
						})
						// url
					},2000)
				}	
			},
			//判断验证是否符合要求
			validate(key){
				let bool=true;
				if( !this.rules[key].rule.test(this[key]) ){
					uni.showToast({
						title:this.rules[key].msg,
						icon:"none"
					})
					bool=false;
					return false;
				}
				return bool;
			},
			goRegisterAccount(){
				uni.navigateTo({
					url:"../user/register-account"
				})
			},
			goPhoneLogin(){
				uni.navigateTo({
					url:"../user/login-phone"
				})
			}
		}
	}
</script>

<style lang="scss" scoped> 
.login-tel{
	width: 100vw;
	height: 100vh;
}
.tel-main{
	padding: 0 20rpx;
}
// .close{
// 	padding: 120rpx 0;
// }
.tel{
	width: 100%;
	height: 80rpx;
	line-height: 80rpx;
	margin-top: 80px;
	color: #FFFFFF;
	background-color: #49BDFB;
	border-radius: 40rpx;
	text-align: center;
}
.avatar{
	// text-align: center;
	padding-left: 280rpx;
	
	// padding: 0 auto;
}
.login-go{
		//调成flex，好居中
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		// position: fixed;
		// padding-top: 20rpx;
		// bottom: 0rpx;
}
/*第二*/
.close-center > view{
	flex: 1;
}
.login-form{
	padding-top: 150rpx;
}
.login-user{
	font-size: 32rpx;
	padding: 10rpx 0;
	display: flex;
	align-items: center;
	border-bottom: 2rpx solid #f7f7f7;
}
.user-text{
	padding-right: 10rpx;
}
.login-quick{
	display: flex;
	//左右
	justify-content: space-between;
	padding: 20rpx 0;
	text-align: center;
	font-size: 25rpx;
}
.reminder{
	text-align: center;
	font-size: 25rpx;
	color: #49BDFB;
	padding-top: 10rpx;
}
</style>