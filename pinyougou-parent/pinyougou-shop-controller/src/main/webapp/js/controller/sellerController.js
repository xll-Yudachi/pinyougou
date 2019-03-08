 //控制层 
app.controller('sellerController' ,function($scope,$controller,sellerService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //商品入驻添加
	$scope.add = function(){
		sellerService.add($scope.entity).success(
			function(response){
				if(response.success){
					//跳转到登录界面(实际上跳转到通知等待审核通过的界面)
					location.href='shoplogin.html';
				}else{
					alert(response.message);
				}
			}	
		);
	}
	
	//获取登录用户的账号名
	$scope.getLoginName = function(){
		sellerService.getLoginName().success(
				function(response){
					$scope.loginName = response.loginName; 
				}
		);
	}
    
});	
