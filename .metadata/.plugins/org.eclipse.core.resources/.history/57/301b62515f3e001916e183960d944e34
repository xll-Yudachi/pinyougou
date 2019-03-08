//控制层 
app.controller('userController' ,function($scope,$controller,userService){	
	
	
	
	//用户注册
	$scope.reg = function(){
		
		//判断两次密码输入是否一致
		if($scope.entity.password != $scope.checkPassword){
			alert("两次密码输入不一致，请重新输入");
			$scope.entity.password="";
			$scope.checkPassword="";
			return ;
		}
		
		//用户注册
		userService.add($scope.entity,$scope.smscode).success(
			function(response){
				if(response.success){
					alert(response.message);
				}else{
					alert(response.message);
				}
			}
		);
	}
	
	//发送验证码
	$scope.sendCode = function() {
		
		if($scope.entity.phone == null || $scope.entity.phone == ""){
			alert("手机号不能为空")
			return ;
		}
		
		userService.sendCode($scope.entity.phone).success(
			function(response){
				if(response.success){
					alert(response.message);
				}else{
					alert(response.message);
				}
			}
		);
	}
	
});	
