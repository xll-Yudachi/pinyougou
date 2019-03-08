//服务层
app.service('cartService',function($http){
	
	//获得购物车列表
	this.findCartList = function(){
		return $http.get('cart/findCartList.do');
	}

	//添加商品到购物车
	this.addGoodsToCartList = function(itemId,num){
		return $http.get('cart/addGoodsToCartList.do?itemId=' + itemId + '&num=' + num);
	}
	
	//求合计
	this.sum = function(cartList){
		
		var totalValue = {totalNum:0,totalMoney:0};
		
		for(var i=0; i<cartList.length; i++){
			//购物车对象
			var cart = cartList[i];
			for(var j=0; j<cart.orderItemList.length; j++){
				//购物车明细
				var orderItem = cart.orderItemList[j];
				totalValue.totalNum += orderItem.num;
				totalValue.totalMoney += orderItem.totalFee;
			}
		}
		
		return totalValue;
	}
	
	//获取当前登录账号的收货地址
	this.findAddressList = function(){
		return $http.get('address/findListByLoginUser.do');
	}
	
	//添加收货地址
	this.addAddress = function(entity){
		return $http.post('address/addAddress.do',entity);
	}
	
	//添加订单
	this.submitOrder = function(order){
		return $http.post('order/add.do',order);
	}
});
