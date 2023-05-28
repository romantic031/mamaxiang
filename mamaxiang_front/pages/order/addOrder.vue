<template>
	<view class="home">
		<view class="top_stepsView">
			<u-steps current="1">
					<u-steps-item title="已下单" desc="10:30">
					</u-steps-item>
					<u-steps-item title="正在制作" desc="10:35" ></u-steps-item>
					<u-steps-item title="已完成" desc="11:40"></u-steps-item>
			</u-steps>
			<view class="top_title">嘛嘛香大厨正在制作您的餐品，请耐心等待</view>
			<view class="top_btnBox">
				<view class="btn" @click="generateCode">取餐码:{{code}}</view>
			</view>
		</view>
		<view class="storeView">
			<view class="storeView_name">{{storesName}}</view>
			<view class="storeView_detail">{{storesDetail}}</view>
		</view>
		<view class="bottom_orderView">
			<view class="orderCode">自提订单: {{ordersId}}</view>
			<view class="orderItem" v-for="(order,index) in orders[0].orderDetails"
				  :key="order.id">
				<image :src="setIcon(order.image)" class="itemImg"></image>
				<view class="itemName">{{order.name}}</view>
				<view class="itemSpace"></view>
				<view class="itemuPrice">
					<view class="itemuPrice_top">￥{{order.amount}}</view>
					<view class="itemuPrice_bottom">×{{order.number}}</view> 	
				</view>	
			</view>
			<view class="orderTotal">
				<view class="totalText">合计:</view>
				<view class="totaldetail">
					<view class="detailNum">共{{goodsNum}}两件商品</view>
					<view class="detailamount">	实付:￥
					<text class="amounttext">{{ordersAmount}}</text></view>
				</View>
			</view>
		</view>
		<view class="bottomBtn">
			<button class="btn00" @click="toDish">返回点餐</button>
			<button class="btn00" @click="resurePay">确认支付</button>
		</view>
	</view>
</template>

<script>
	import { getImage } from '../../utils/resources'
	export default{
		data(){
			return{
				storesName:'',
				storesDetail:'',
				code:undefined,
				orders:[],
				ordersId:undefined,
				ordersAmount:undefined
			}
		},
		onLoad(option) {
			this.storesName=option.storesName
			this.storesDetail=option.storesDetail
			this.getOrderdata()
			this.generateCode()
		},
		computed:{
			goodsNum(){
				let num = 0
				if(this.orders[0].orderDetails!=null){
					this.orders[0].orderDetails.forEach( item=> {
						num += item.number
					})
					if (num < 99) {
								return num
						} else {
								return '99+'
						}
				}
			}
		},
		methods:{
			//获取图片
			setIcon(icon){
				if(icon){
					const images = icon.split(',')
					return getImage(images[0])
				}
			},
			generateCode(){
				let min=10000
				let max=11000
				this.code = Math.floor(Math.random() * (max - min) ) + min
			},
			async getOrderdata(){
				const res= await this.$myRequest({
					url:'/api/order/userPage?page=1&pageSize=1'
				})
				if(res.data.code === 200){
					this.orders = res.data.data.records
					this.ordersId = res.data.data.records[0].id
					this.ordersAmount=res.data.data.records[0].amount
				}
			},
			toDish(){
				uni.navigateTo({
					url:'/pages/dish/dish'
				})
			},
			async resurePay(){
				const res = await this.$myRequest({
					url: '/api/shoppingCart/clean',
					method: 'DELETE'
				})
				if (res.data.code === 200) {
					uni.navigateTo({
						url:'/pages/index/index'
					})
				}
			}	
		}	
	}
</script>

<style>
	@import url("../../style/addOrder.css");
</style>