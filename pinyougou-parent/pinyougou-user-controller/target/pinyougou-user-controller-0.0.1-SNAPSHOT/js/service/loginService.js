//服务层
app.service('loginService',function($http){
	
	//读取用户的登录名
	this.getName = function(){
		return $http.get("../login/getName.do");
	}
	
});