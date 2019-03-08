//服务层
app.service('sellerService',function($http){
	    	
	//增加 
	this.add=function(entity){
		return  $http.post('../seller/add.do',entity);
	}
	
	//获取登录用户的账号名
	this.getLoginName = function(){
		return $http.get('../seller/getLoginName.do');
	}
	
});
