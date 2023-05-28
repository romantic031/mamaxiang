<template>
	<view class="home">
		<u-search showAction="true" actionText="搜索" animation="true" 
				  placeholder="请输入关键字搜索" v-model="keyword" @search="getSearchStore" 
				  @custom="getSearchStore">
		</u-search>
		<view class="body_view">
			<view class="body_view_top">
				<text>历史搜索</text><button @click="cleanRecoding" plain="true">清空</button>
			</view>
			<text @click="byRecodingSearch(index)" class="body_view_bottom"
				  v-for="(recoding,index) in recoding">
					{{recoding}}
			</text>
			<view class="storeList_view">
				<view style="color: #9d9d9d; margin-left: 20rpx; line-height: 60rpx;">
					搜索结果：
				</view>
				<view class="storeList_innerView">
					<view class="storeList_li" v-for="(store,index) in stores" 
					:key="store.id" @click="popup_open00(index,store.name,store.detail)">
						<view class="storeList_content" 
							  style="color: black; font-size: 30rpx; margin-top: 20rpx; ">
								{{store.name}}
						</view>	<text>{{store.location}}</text>
						<view class="storeList_content">{{store.detail}}</view>
						<view v-if="store.businessHours!=null"  
							  class="storeList_content" style="margin-bottom: 20rpx;">
								营业时间:{{store.businessHours}}
						</view>
						<view v-else  class="storeList_content" 
						style="margin-bottom: 20rpx;">
								营业时间:暂无
						</view>
					</view>
				</view>
			</view>
			<u-popup :show="popup_show" @close="popup_close" 
					 @open="popup_open" :round="10" :closeable="true">
				<view class="popup_view">
					<view class="storeList_view">
						<view 
							style="margin-top: 40rpx; margin-bottom: 40rpx; font-size: 30rpx;">
							<span style="font-size: 45rpx;" class="iconfont icon-dianpu"></span>确认门店
						</view>
						<view 
							style="color: darkorange; margin-top: 40rpx; margin-bottom: 30rpx; font-size: 25rpx;">
								珍惜粮食，按需点餐
							</view>
						<view class="storeList_li" 
						v-for="store in stores" :key="store.id" 
						v-if="store.id==storesIndex+1" 
						style=" background-color:#FFFFFF; border:2px solid crimson; border-radius: 20px; margin-bottom: 80rpx;">
							<view class="storeList_content" style="color: black; font-size: 30rpx; margin-top: 20rpx; margin-left: 20rpx;">{{store.name}}</view><text style="margin-right: 20rpx;">{{store.location}}</text>
							<view class="storeList_content" style="margin-left: 20rpx;">{{store.detail}}</view>
							<view v-if="store.businessHours!=null" class="storeList_content" style="margin-bottom: 20rpx; margin-left: 15rpx;">营业时间:{{store.businessHours}}</view>
							<view v-else class="storeList_content" style="margin-bottom: 20rpx; margin-left: 15rpx;">营业时间:暂无</view>
						</view>
					</view>
						<view>
							<button class="popup_btn" @click="toDish01">店内就餐</button>
							<button class="popup_btn" @click="toDish02">打包带走</button>
						</view>
				</view>
			</u-popup>
		</view>
	</view>
</template>

<script>
	export default{
		data(){
			return{
				keyword:'',
				recoding:['步行街','星汇广场','星汇'],
				storesIndex:0,
				popup_show:false,
				stores:[],
				storesName:'',
				storesDetail:'',
				diningStyle0:'店内就餐',
				diningStyle1:'打包带走'
			}
		},
		onLoad(option) {
				this.keyword=option.keyword
		},
		methods:{
			cleanRecoding(){
				this.recoding=[]
			},
			byRecodingSearch(index){
				this.keyword=this.recoding[index]
			},
			popup_close() {
				this.popup_show=false
			},
			popup_open00(index,storename,storedetail) {
				this.popup_show=true
				this.storesIndex=index
				this.storesName=storename
				this.storesDetail=storedetail
			},
			popup_open(index){
				this.popup_show=true
				this.storesIndex=index
			},
			async getSearchStore(){
				const res = await this.$myRequest({
					url:'/api/store/showAllStore?name='+this.keyword
				})
				this.stores=res.data.data
				this.recoding.push(this.keyword)
			},
			toDish01(){
				uni.navigateTo({
					url:'/pages/dish/dish?diningStyle='+this.diningStyle0+'&storesName='+this.storesName+'&storesDetail='+this.storesDetail
				})
			},
			toDish02(){
				uni.navigateTo({
					url:'/pages/dish/dish?diningStyle='+this.diningStyle1+'&storesName='+this.storesName+'&storesDetail='+this.storesDetail
				})
			}
		}	
	}
</script>

<style>
	page{
		height: 100%;
		background-color: #e8e8e8;
	}
	.home{
		height: 100%;
	}
	.body_view{
		margin-top: 20px;
		height: 100%;
		width: 100%;
	}
	.body_view_top{
		height: 60rpx;
		width: 100%;
	}
	.body_view_top text{
		margin-left: 5px;
		font-size: 30rpx;
		font-weight: 700;
	}
	.body_view_top button{
		line-height: 60rpx;
		height: 60rpx;
		border: none;
		margin-right: 5px;
		font-size: 25rpx;
		background-color: #e8e8e8;
		color: #55aaff;
		float: right;
		top:-6px;
	}
	.body_view_bottom {
		padding: 8px;
		height: 50rpx;
		width: 100rpx;
		margin: 0 8px;
		background-color: #b1b1b1;
		border-radius: 10px;
		color: #535353;
		font-weight: 500;
		font-size: 20rpx;
	}
	.storeList_view{
		border-radius: 10px;
		width: 690rpx;
		background-color: #f4f4f4;
		margin-left: auto;
		margin-right: auto;
		margin-top: 40rpx;
	}
	.storeList_innerView{
		width: 650rpx;
		margin-left: auto;
		margin-right: auto;
	}	
	.storeList_li{
		border-bottom: #9d9d9d solid 1px; 
	}
	.storeList_content{
		color: #9d9d9d;
		font-size: 25rpx;
		line-height: 55rpx;
	}
	.storeList_li text{
		color: #9d9d9d;
		font-size: 25rpx;
		float: right;
	}
	.popup_view{
		background-color: #f4f4f4;
	}
	.popup_btn{
			width: 300rpx;
			border-radius: 40px;
			height: 80rpx;
			line-height: 80rpx;
			font-size: 30rpx;
			background-color: #aa1024;
			color: #ffffff;
			float: left;
			margin-right: 40rpx;
			left: 55rpx;
		}
</style>