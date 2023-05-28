<template>
	<view class="login-tel">
		<view class="tel-main">
			<view class="close close-center">
				<view></view>
			</view>
			<view class="login-form">
				<view class="login-user">
					<text class="user-text">账号</text>
					<input type="text" v-model="account" value="" placeholder="请输入账号">
				</view>
				<view class="login-user">
					<text class="user-text">密码</text>
					<input type="password" v-model="userPwd" value="" placeholder="请输入6-16位字符">
				</view>
				<view class="login-user">
					<text class="user-text">手机号</text>
					<!-- <input type="number" v-model="phone" focus="true"  placeholder="请输入11位手机号"> -->
					<input type="number" v-model="phone" focus="true"  placeholder="请输入11位手机号">
				</view>
			</view>
			<view class="tel" @tap="submit()">注册</view>
			<LoginOther></LoginOther>
		</view>
	</view>
</template>

<script>
	export default{
		name:"register-account",
		data() {
			return {
				//账号
				account:"",
				//密码
				userPwd:"",
				//手机号
				phone:"",
				//验证的正则
				rules:{
					account:{
						rule:/\S/,
						msg:"账号不能为空"
					},
					userPwd:{
						rule:/[0-9a-zA-Z]{6,16}/,
						msg:"密码应为6-16字符"
					},
					phone:{
						// rule:/(^1\d{10}$)|(^[0-9]\d{7}$)/,
						rule:/^1[3456789]\d{9}$/,
						msg:"手机号输入错误"
					}
				},

			};
		},
		methods: {
			//关闭当前页面，返回上一页
			goBack(){
				uni.navigateBack({
					delta:1
				})
			},
			//点击注册
			async submit(){
				// if( this.rules.account.rule.test(account) ) return;
				if( !this.validate("account") ) return;
				if( !this.validate("userPwd")) 	return;
				if( !this.validate("phone")) 	return;
				const res = await this.$myRequest({
						url:'/api/users/register',
						method:'POST',
						// params:"phone":this.phone
						data:{account:this.account,phone:this.phone,password:this.userPwd}
					})
					uni.showToast({
						title:res.data.msg,
						icon:"none"
					})
					if(res.data.code==200){
						uni.showLoading({
							title:"注册中..."
						})
						setTimeout(()=>{
							uni.hideLoading();
							uni.navigateBack({
								delta:1
							})
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
			}
		
		}
		
	}
</script>

<style>
.login-tel{
	width: 100vw;
	height: 100vh;
}
.tel-main{
	padding: 0 20rpx;
}

.login-form{
	margin-top: 150rpx;
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
</style>