<template>
	<view class="login">
							<view class="login-tel">
								<view class="tel-main">
									<view class="login-form">									
										<view class="login-user">
											<text class="user-text" v-model="phone">手机号</text>
											<!-- <input type="number" v-model="phone" focus="true"  placeholder="请输入11位手机号"> -->
											<input type="number" v-model="phone" focus="true"  placeholder="请输入11位手机号">
											<button style="color: blue; width: 230rpx; height: 70rpx; font-size: 20rpx; line-height: 70rpx;" type="" :disabled="disabled" @tap="sendCode()">{{codeMsg}}</button>
										</view>
										<view class="login-user" style="padding-top: 20rpx;">
											<text class="user-text" v-model="userCode">验证码</text>
											<input type="text" v-model="userCode" value="" placeholder="请输入6位验证码">
										</view>
									</view>
									<view class="login-quick">	
										<view @tap="goMy">账号密码登录</view>
									</view>
									<view class="tel" @tap="submit()">登录</view>
									<view class="reminder">温馨提示：如果手机号对应账号不存在，则自动注册</view>
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
				//倒计时
				codeNum:60,
				codeMsg:"发送验证码",
				disabled:false,
				//手机号
				phone:"13288806028",
				//用户输入的验证码
				userCode:"",
				//验证的正则
				rules:{
					userCode:{
						rule:/^\d{6}$/,
						msg:"验证码错误"
					},
					phone:{
						// rule:/(^1\d{10}$)|(^[0-9]\d{7}$)/,
						rule:/^1[3456789]\d{9}$/,
						msg:"手机号输入错误"
					}
				},
				
			};
		},
		components: {
			LoginOther
		},
		methods: {
			//点击验证码发送
			async sendCode(){
				// async getAllStore(){
					if( !this.validate("phone")) 	return;
					const res = await this.$myRequest({
							url:'/api/users/sendCode',
							// params:"phone":this.phone
							data:{phone:this.phone}
						})
						uni.showToast({
							title:res.data.msg,
							icon:"none"
						})
					console.log(res)
				// },
			
				this.disabled=true;
				
				//倒计时减时间
				let timer=setInterval(()=>{
					--this.codeNum;
					this.codeMsg='重新发送（'+this.codeNum+'）';
				},1000);
				setTimeout(()=>{
					clearInterval(timer);
					this.codeNum=60;
					this.disabled=false;
					this.codeMsg='重新发送('+this.codeNum+')';
				},60000)
			},
			//关闭当前页面，返回上一页
			goBack(){
				uni.navigateBack({
					delta:1
				})
			},
			//点击登录
			async submit(){
				// if( this.rules.account.rule.test(account) ) return;
				if( !this.validate("userCode")) 	return;
				const res=await this.$myRequest({
					url:'/api/users/login',
					method:'POST',
					data:{phone:this.phone,code:this.userCode}
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
			goMy(){
				uni.navigateTo({
					url:"../user/my"
				})
			}
		},
		}
</script>

<style>
	.tel-main{
		padding: 0 20rpx;
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
	.avatar{
		// text-align: center;
		padding-left: 280rpx;
		
		// padding: 0 auto;
	}
	.login-form{
		margin-top:  250rpx;
	}
	.login-user{
		font-size: 32rpx;
		padding: 10rpx 0;
		display: flex;
		align-items: center;
		border-bottom: 2rpx solid #f7f7f7;
	}
	.login-quick{
		display: flex;
		//左右
		justify-content: space-around;
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
	.user-text{
		padding-right: 10rpx;
	}
</style>