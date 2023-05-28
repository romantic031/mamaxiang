const Base_URL = "http://activate.navicat.com:8081"
export const myRequest = (options)=>{
	return new Promise((resolve,reject)=>{
		let token = uni.getStorageSync('token')
		// header: {'content-type':'application/json'},
		uni.request({
			url:Base_URL+options.url,
			responseType:options.responseType,
			method:options.method || 'GET',
			header: options.header || {
				authorization:token
			},
			params:options.params,
			data:options.data || {},
			success: (res)=>{
				// if(res.data.code !== 200){
				// 	return uni.showToast({
				// 		// title:'返回数据失败'
				// 	})
				// }
				resolve(res)
			},
		fail: (err)=> {
				uni.showToast({
						title:'请求接口失败'
					})
				reject(err)
			   }
		})
	})
}