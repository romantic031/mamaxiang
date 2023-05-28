<template>
	<view class="login-other">
		<view class="other-text">
			<view>或者用以下方式进行登录</view>
		</view>
		<view class="other">
			<view class="other-item">
				<uni-icons type="phone" size="30" class="close"></uni-icons>
				<view>手机号登录</view>
			</view>
			<view class="other-item">
				<uni-icons type="email" size="30" class="close"></uni-icons>
				<view>邮箱登录</view>
			</view>
			<view class="other-item">
				<uni-icons type="qq" size="30" class="close"></uni-icons>
				<view>QQ登录</view>
			</view>
			<view class="other-item">
				<uni-icons type="weixin" size="30" class="close"></uni-icons>
				<view>微信登录</view>
			</view>
		</view>
	</view>
</template>

<script>
</script>

<style scoped>
	/*	其他登录方式	*/
	.login-other{
		padding: 100rpx 0;
	}
	.other-text{
		display: flex;
		padding: 50rpx 0;
	}
	.other-text view{
		line-height: 0rpx;
		padding: 0 10rpx;
	}
	.other-text::after{
		flex: 1;
		content: '';
		height: 2rpx;
		background-color: #CCCCCC;
	}
	.other-text::before{
		flex: 1;
		content: '';
		height: 2rpx;
		background-color: #CCCCCC;
	}
	.other{
		display: flex;
		// 往中间堆
		justify-content: space-around;
	}
	.other-item{
		font-size: 20rpx;
		text-align: center;
	}
	
</style>