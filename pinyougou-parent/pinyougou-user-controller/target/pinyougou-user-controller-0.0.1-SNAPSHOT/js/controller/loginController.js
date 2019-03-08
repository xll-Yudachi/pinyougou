//登录控制器
app.controller('loginController',function($scope,loginService){
	
	$scope.getName = function(){
		loginService.getName().success(
			function(response){
				$scope.userName = response.userName;
			}
		);
	}
});