<template>
	<view class="home">
		<!-- 头部门店信息及用餐方式 -->
		<view class="top_view">
			<view style="display: flex; flex-direction: column; flex:3">
				<view style="flex:2;">
					<text :bind='storesName'
						style="margin-left: 5px; font-size: 35rpx; font-weight: 600; ">{{storesName}}</text>
				</view>
				<view style="padding: 10rpx; flex:1;">
					<text :bind='diningStyle'
						style="padding-right: 10rpx; font-size: 25rpx; border-right:3rpx solid #303030; color: #4d4d4d;">{{diningStyle}}</text>
					<text :bind='storesDetail'
						style="padding-left: 10rpx; font-size: 25rpx; color:  #4d4d4d; ">{{storesDetail}}</text>
				</view>
			</view>
			<span style="font-size: 35px; flex:1; text-align: center; line-height: 60px; justify-content: flex-end;"
				class="iconfont icon-kefu"></span>
		</view>
		<!-- 轮播图 -->
		<u-swiper :list="imgList" indicator indicatorMode="dot"></u-swiper>
		<!-- 菜单和菜品展示 -->
		<view style="display: flex;">
			<!-- 左侧分类列表 -->
			<scroll-view class="left" scroll-y>
				<view class="left_view" :class="active===index?'.left_view_active':''"
					v-for="(category,index) in categories" :key="category.id"
					@click="LeftGetDish(index,category.id,category.name,category.type)">
					{{category.name}}
				</view>
			</scroll-view>
			<!-- 右测菜品列表 -->
			<scroll-view class="right" scroll-y>
				<view style="margin-bottom: 20rpx;">
					<text style="color: #9d9d9d; font-size: 25rpx;">{{cates}}</text>
				</view>
				<view :key="dish.id" class="right_view" v-for="(dish,index) in dishes">
					<view>
						<image :src="setIcon(dish.image)"></image>
						<view class="right_view_info">
							<view>{{dish.name}}</view>
							<view>￥<text style="color:#aa1024; font-size:35rpx;">
									{{dish.price}}
								</text>
							</view>
							<view style="display: flex; height: 91px; width:265rpx; align-items:flex-end; align-content: flex-end;">
								<button class="LeftSubtract" v-if="dish.number > 0" style="flex: 1; width: 20px;"
									@click.prevent.stop="subtractCart(dish)">-</button>
								<view class="centerNum" style="flex: 1; text-align: center;">{{dish.number}}</view>
								<button @click.prevent.stop="chooseFlavor(dish)" :class="isFlavor?'.right_view_info_btn':''"
									v-if="dish.flavors.length > 0 && !dish.number">选择规格</button>
								<button v-else class="rightAdd" @click.prevent.stop="addCart(dish)"
										style="flex: 1; width: 20px;"
										>+</button>
							</view>
						</view>
					</view>
					<!-- 模拟层菜品详情 -->
					<view>
						<u-modal :show="dialogflavors.show" :title="title" width="300px" content='content'
							:closeOnClickOverlay="true" :dish='dish' @close="modal_close" confirmColor="#aa1024;"
							confirmText="加入购物车" @confirm="addCart(dialogflavors)">
							<view>
								<image :src="dialogflavors.image" style="width:250px;"></image>
							</view>
							<view v-for="flavor in dialogflavors.flavors">
								<view>{{flavor.name}}:</view>
								<view style="display: flex; margin-top: 10px;">
									<view>
										<u-checkbox-group placement="row" @change="checkboxChange()">
											<u-checkbox size="11" labelSize="11px"
												v-for="value in flavorValueCut(flavor.value)" :key="index"
												:label="value" :name="value">
											</u-checkbox>
										</u-checkbox-group>
									</view>
								</view>
							</view>
						</u-modal>
					</view>
				</view>
				<!-- 套餐列表展示 -->
				<view :key="setmeal.id" class="right_view" v-for="setmeal in setmeals">
					<view>
						<image :src="setIcon(setmeal.image)"></image>
						<view class="right_view_info">
							<view>{{setmeal.name}}</view>
							<view>￥<text style="color:#aa1024; font-size:35rpx;">
									{{setmeal.price}}
								</text>
							</view>
							<view style="display: flex; height: 91px; width:265rpx; align-items:flex-end; align-content: flex-end;">
								<button class="LeftSubtract" v-if="setmeal.number > 0" style="flex: 1; width: 20px;"
									@click.prevent.stop="subtractCart(setmeal)">-</button>
								<view class="centerNum" style="flex: 1; text-align: center;">{{setmeal.number}}</view>
								<button @click.prevent.stop="chooseFlavor(setmeal)" :class="isFlavor?'.right_view_info_btn':''"
									v-if="setmeal.flavors.length > 0 && !setmeal.number">选择规格</button>
								<button v-else class="rightAdd" @click.prevent.stop="addCart(setmeal)"
										style="flex: 1; width: 20px;"
										>+</button>
							</view>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
		<!-- 底部固定购物车，下订单按钮 -->
		<view class="anniu">
			<button style="padding: 0;" formType="submit" :disabled="disabled" class="btn-normal btbdstk"
				@click="openCart()">
				<view class="sqdzk">
					<view style="flex: 2; text-align: right;">
						<span style="font-size: 45rpx;" class="iconfont icon-canyin"></span>
					</view>
					<view style="flex: 6; text-align: left; font-size: 35rpx;">￥{{goodsPrice}}</view>
					<view style="flex: 3; font-size: 30rpx; text-align: left;">选好了</view>
				</view>
			</button>
		</view>
		<!-- 购物车弹出层 -->
		<u-popup :show="cartDialogShow" @close="closeCart" :round="50" @open="openCart">
			<view class="cartBox">
				<!-- 顶部标题 -->
				<view class="cartTopView">
					<view class="cartTopTitle">我的购物车({{goodsNum}})</view>
					<button class="cartTopBtn" @click="cleanCart" plain="true">
						<span class="iconfont icon-changyonggoupiaorenshanchu"></span>
						清空购物车
					</button>
				</view>
				<!-- 现在选择的商品 -->
				<view class="cartContentView">
					<view class="cartContentItem" v-for="(item,index) in cartData" :key="item.id">
						<image :src="setIcon(item.image)"></image>
						<view class="itemCenterView">
							<view class="itemName">{{item.name}}</view>
							<view class="itemPrice">￥{{item.amount}}</view>
						</view>
						<view class="itemRightView">
							<button class="LeftSubtract" @click="cartNumberSubtract(item)">-</button>
							<text class="centerNum" style="flex: 1;">{{item.number}}</text>
							<button class="rightAdd" @click="cartNumAdd(item)">+</button>
						</view>
					</view>
				</view>
				<!-- 底部合计，下订单按钮 -->
				<view class="cartBottomView">
					<view class="anniu">
						<button style="padding: 0;" formType="submit" :disabled="disabled" class="btn-normal btbdstk"
							@click="toOrder()">
							<view class="sqdzk">
								<view style="flex: 2; text-align: right;">
									<span style="font-size: 45rpx;" class="iconfont icon-canyin"></span>
								</view>
								<view style="flex: 6; text-align: left; font-size: 35rpx;">￥{{goodsPrice}}</view>
								<view style="flex: 3; font-size: 30rpx; text-align: left;">选好了</view>
							</view>
						</button>
					</view>
				</view>
			</view>
		</u-popup>
	</view>
</template>
<script>
import { getImage } from '../../utils/resources'
	export default {
		data() {
			return {
				categories: [],
				cates: "",
				cateId: undefined,
				imgList: [
					'/../../static/images/backiee-01.jpg',
					'/../../static/images/backiee-02.jpg',
					'/../../static/images/backiee-03.jpg'
				],
				dishes: [],
				dishId: '',
				setmeals: [],
				setmealId: '',
				storesId: undefined,
				storesName: '',
				storesDetail: '',
				active: 0,
				title: '规格详情',
				content: '',
				diningStyle: '',
				dialogflavors: {

					show: false,
					flavors: [],
					selectFlavors: [],
					name: '',
					price: undefined,
					image: ''
				},
				isFlavor: true,
				categoryId: undefined,
				cartData: [],
				cartDialogShow: false
			}
		},
		computed: {
			goodsNum() {
				let num = 0
				if (this.cartData != null) {
					this.cartData.forEach(item => {
						num += item.number
					})
					if (num < 99) {
						return num
					} else {
						return '99+'
					}
				}
			},
			goodsPrice() {
				let price = 0
				if (this.cartData != null) {
					this.cartData.forEach(item => {
						price += (item.number * item.amount)
					})
				}
				return price
			}
		},
		onLoad(option) {
			this.diningStyle = option.diningStyle
			this.storesName = option.storesName
			this.storesDetail = option.storesDetail
			this.LeftGetDish(0, 1, '主食点心', 1)
			this.getCategories()
			this.getCartList()
			this.getSearchStore()
		},
		methods: {
			//获取图片
			setIcon(icon){
				if(icon){
					const images = icon.split(',')
					return getImage(images[0])
				}
			},
			modal_close() {
				this.dialogflavors.show = false
			},
			async getCategories() {
				const res = await this.$myRequest({
					url: '/api/category/list'
				})
				this.categories = res.data.data
			},
			async getCartList() {
				const res = await this.$myRequest({
					url: '/api/shoppingCart/list'
				})
				this.cartData = res.data.data
			},
			async getSearchStore(){
				const res = await this.$myRequest({
					url:'/api/store/showAllStore?name='+this.storesName
				})
				this.storesId=res.data.data[0].id
			},
			async LeftGetDish(index, id, name, type) {
				this.cateId = id
				this.active = index
				this.cates = name
				if (type === 1) {
					const res = await this.$myRequest({
						url: '/api/dish/listByCategory',
						data: {
							categoryId: id
						}
					})
					if (res.code === 200) {
						let dishList = res.data.data
						const cartData = this.cartData
						if (dishList.length > 0 && cartData.length > 0) {
							dishList.forEach(dish => {
								cartData.forEach(cart => {
									if (dish.id === cart.dishId) {
										dish.number = cart.number
									}
								})
							})
						}
						this.dishes = dishList
					}
					this.dishes = res.data.data
					console.log(res.data.data[0].image)
					this.setmeals = []
				} else {
					const res = await this.$myRequest({
						url: '/api/setmeal/listByCategory/' + id
					})
					this.setmeals = res.data.data
					this.dishes = []
				}
			},
			flavorValueCut(flavor) {
				let value = flavor.split(",")
				return value
			},
			checkboxChange(n) {
				this.dialogflavors.selectFlavors = Array({
					n
				})
			},
			chooseFlavor(item) {
				 
				this.dishId = item.id
				this.dialogflavors = {
					name: item.name,
					flavors: item.flavors,
					price: item.price,
					show: true,
					image: item.image,
				}
			},
			addShopping00(id, flavors) {
				this.setmealId = id
				this.dialogflavors.show = true
			},
			openCart() {
				this.getCartList()
				if (this.cartData.length > 0) {
					this.cartDialogShow = true
				}
			},
			closeCart() {
				this.cartDialogShow = false
			},
			//菜单中添加商品数量
			async addCart(item) {
				let params = {
					name: item.name,
					amount: item.price,
					// dishFlavor:this.dialogflavors.selectFlavors,
					dishId: '', //菜品id
					setmealId: '', //套餐id
					image: item.image,
				}
				if (Array.isArray(item.flavors)) { //表示是菜品
					params.dishId = this.dishId
				} else { //表示套餐 套餐没有口味
					params.setmealId = item.id
				}
				const res = await this.$myRequest({
					url: '/api/shoppingCart/add',
					method: 'POST',
					data: params
				})
				if (res.data.code === 200) {	
					this.dishes.forEach(dish=>{
					 if(dish.id === this.dishId){
					  dish.number = res.data.data.number
					 }
					})
					this.modal_close()
					this.getCartList()
				} else {
					this.$notify({
						type: 'warning',
						message: res.msg
					});
				}
			},
			//菜单中减少选中的商品
			async subtractCart(item) {
				let params = {
					dishId: item.id
				}
				if (!Array.isArray(item.flavors)) {
					params = {
						setmealId: item.id
					}
				}
				const res = await this.$myRequest({
					url: '/api/shoppingCart/sub',
					method: 'POST',
					data:params
					
				})
				if (res.data.code === 200) {
					this.dishes.forEach(dish => {
						if (dish.id === item.id) {
							dish.number = (res.data.data.number === 0 ? undefined : res.data.data.number)
						}
					})
					this.getCartList()
				} else {
					this.$notify({
						type: 'warning',
						message: res.msg
					});
				}
			},
			async cartNumAdd(item) {
				this.dishId = item.dishId
				let params = {
					name: item.name,
					amount: item.price,
					// dishFlavor: this.dialogflavors.selectFlavors,
					dishId: item.dishId, //菜品id
					setmealId: item.setmealId, //套餐id
					image: item.image
				}
				const res = await this.$myRequest({
				 url: '/api/shoppingCart/add',
					method: 'POST',
					data:params	
				})
				if (res.data.code === 200) {
					this.dishes.forEach(dish => {
						if (dish.id === (item.dishId || item.setmealId)) {
							dish.number = res.data.data.number
						}
					})
					this.getCartList()
				} else {
					this.$notify({
						type: 'warning',
						message: res.msg
					});
				}
			},
			async cartNumberSubtract(item) {
				let params = {dishId: item.dishId}
				if (item.setmealId) {
					params = {
						setmealId: item.setmealId
					}
				}
				const res = await this.$myRequest({
					url: '/api/shoppingCart/sub',
					method: 'POST',
					data:params
			 })
				if (res.data.code === 200) {
					this.dishes.forEach(dish => {
						if (dish.id === (item.dishId || item.setmealId)) {
							dish.number = (res.data.data.number === 0 ? undefined : res.data.data.number)
						}
					})
					this.getCartList()
				} else {
			  this.$notify({
						type: 'warning',
						message: res.msg
					});
				}
			},
			async cleanCart() {
				const res = await this.$myRequest({
					url: '/api/shoppingCart/clean',
					method: 'DELETE'
				})
				if (res.data.code === 200) {
					for (let ele of this.dishes) {
				  ele.number = undefined
					}
					this.cartData = []
					this.cartDialogShow = false
				}
			},
			//生成订单方法并且跳转
			async toOrder() {
				let params = {
					storeId:this.storesId
				}
				const res = await this.$myRequest({
					url:'/api/order/placeOrder',
					method:'POST',
					data:params
				})
				if(res.data.code === 200){
					if (this.cartData.length > 0) {
						uni.navigateTo({
							url: '/pages/order/addOrder?storesName=' + this.storesName+'&storesDetail='+this.storesDetail
						})
					}
				}else {
					this.$notify({
						type: 'warning',
						message: res.msg
					});
				}
			},
		}
	}
</script>

<style lang="scss">
	@import url("../../style/dish.css");
</style>
