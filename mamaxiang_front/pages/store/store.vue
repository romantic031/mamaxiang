<template>
	<view class="home">
		<u-search showAction="true" actionText="搜索" animation="true" placeholder="请输入关键字搜索" v-model="keyword" @search="toSearch" @custom="toSearch"></u-search>		
			<view class="title_view"><text>常规餐厅</text></view>
			<view class="storeList_view">
				<view style="color: #9d9d9d; margin-left: 20rpx; line-height: 60rpx;">附近</view>
				<view class="storeList_innerView">
					<view v-if="index<=2" class="storeList_li" v-for="(store,index) in stores" :key="store.id" @click="popup_open00(index,store.name,store.detail)">
						<view class="storeList_content" style="color: black; font-size: 30rpx; margin-top: 20rpx; ">{{store.name}}</view><text>{{store.location}}</text>
						<view class="storeList_content">{{store.detail}}</view>
						<view v-if="store.businessHours!=null"  class="storeList_content" style="margin-bottom: 20rpx;">营业时间:{{store.businessHours}}</view>
						<view v-else  class="storeList_content" style="margin-bottom: 20rpx;">营业时间:暂无</view>
					</view>
					<u-collapse
						:border="false"
						accordion
						@change="collapse_change"
					    @close="collapse_close"
					    @open="collapse_open">
						<u-collapse-item title="查看更多"  align="center" >
							<view v-if="index>2"  class="storeList_li" v-for="(store,index) in stores" :key="store.id" @click="popup_open01(index,store.name,store.detail)">
								<view class="storeList_content" style="color: black; font-size: 30rpx; margin-top: 20rpx; ">{{store.name}}</view><text>{{store.location}}</text>
								<view class="storeList_content">{{store.detail}}</view>
								<view v-if="store.businessHours!=null"  class="storeList_content" style="margin-bottom: 20rpx;">营业时间:{{store.businessHours}}</view>
								<view v-else  class="storeList_content" style="margin-bottom: 20rpx;">营业时间:暂无</view>
							</view>
						</u-collapse-item>
					</u-collapse>
				</view>
			</view>
			<view class="title_view01"><text>常去&收藏</text></view>
			<view class="collection_view">
				<view class="collection_info">
					<image src="../../static/images/无收藏.png"></image><br>
					<text>暂无收藏信息</text>
				</view>
			</view>
				<u-popup :show="popup_show" @close="popup_close" @open="popup_open" :round="10" :closeable="true">
					<view class="popup_view">
						<view class="storeList_view">
							<view style="margin-top: 40rpx; margin-bottom: 40rpx; font-size: 30rpx;"><span style="font-size: 45rpx;" class="iconfont icon-dianpu"></span>确认门店</view>
							<view style="color: darkorange; margin-top: 40rpx; margin-bottom: 30rpx; font-size: 25rpx;">珍惜粮食，按需点餐</view>
							<view class="storeList_li" v-for="store in stores" :key="store.id" v-if="store.id==storesIndex+1" style=" background-color:#FFFFFF; border:2px solid crimson; border-radius: 20px; margin-bottom: 80rpx;">
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
</template>

<script>
	export default{
		data(){
			return{
				keyword:'',
				stores:[],
				storesName:'',
				storesDetail:'',
				storesIndex:"",
				popup_show:false,
				empty_show:true,
				diningStyle0:'店内就餐',
				diningStyle1:'打包带走'
			}
		},
		onLoad() {
			this.getAllStore()
		},
		methods:{
			popup_close() {
				this.popup_show=false
			},
			popup_open(index){
				this.popup_show=true
				this.storesIndex=index
			},
			popup_open00(index,storename,storedetail) {
				this.popup_show=true
				this.storesIndex=index
				this.storesName=storename
				this.storesDetail=storedetail
			},
			popup_open01(index,storename,storedetail) {
				this.popup_show=true
				this.storesIndex=index
				this.storesName=storename
				this.storesDetail=storedetail
			},
			collapse_open(){
			},
			collapse_close(){		
			},
			collapse_change(){		
			},
			async getAllStore(){
				const res = await this.$myRequest({
						url:'/api/store/showAllStore'
					})
					this.stores=res.data.data
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
			},
			toSearch(){
				uni.navigateTo({
					url:'/pages/search/search?keyword='+this.keyword
				})
			}
		}
	}
</script>

<style>
	@import url("../../style/store.css");
</style>
