//服务层
app.service('userService',function($http){
	
	//用户注册
	this.add = function(entity,smscode){
		return $http.post('../user/add.do?smscode=' + smscode,entity);
	}
	
	//发送验证码
	this.sendCode = function(phone) {
		return $http.get('../user/sendCode.do?phone=' + phone);
	}
	
});
