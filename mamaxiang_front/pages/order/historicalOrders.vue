<template>
	<view class="home">
		<view class="topTitle">历史订单</view>
		<view class="orderView"  v-for="(orders,index) in orders" :key="index">
			<view class="innerView">
				<view class="innerViewTitle">自取</view>
				<view class="innerViewStatus">已完成</view>
			</view>
			<view class="innerViewTime">时间:{{orders.orderTime}}</view>
				<view class="orderItem" v-for="(order,index) in orders.orderDetails"
					  :key="index">
					<image :src="setIcon(order.image)" class="itemImg"></image>
					<view class="itemName">{{order.name}}</view>
					<view class="itemSpace"></view>
					<view class="itemuPrice">
						<view class="itemuPrice_top">￥{{order.amount}}</view>
						<view class="itemuPrice_bottom">×{{order.number}}</view> 	
					</view>	
				</view>
				合计:
				<View class="orderTotal">
					<view class="totalAmount">￥{{orders.amount}}</view>
				</View>
		</view>
	</view>
</template>
<script>
	import { getImage } from '../../utils/resources'
	export default{
		data(){
			return{
				orders:[],
				orderAmount:[],
				orderDetails:[]
			}
		},
		onLoad() {
			this.getOrderdata()
		},
		computed:{
		},
		methods:{
			setIcon(icon){
				if(icon){
					const images = icon.split(',')
					return getImage(images[0])
				}
			},
			async getOrderdata(){
				const res= await this.$myRequest({
					url:'/api/order/userPage?page=1&pageSize=10'
				})
				if(res.data.code === 200){
					this.orders = res.data.data.records
					this.ordersAmount=res.data.data.records.amount
				}
			},
		}
	}
</script>

<style>
	@import url('../../style/historicalOrders.css');
</style>