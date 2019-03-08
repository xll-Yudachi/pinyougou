app.controller('seckillGoodsController', function($scope,seckillGoodsService,$location,$interval){
	
	 // 读取列表数据绑定到表单中
	 $scope.findList = function(){
		 seckillGoodsService.findList().success(
			   function(response){
				   $scope.list = response;
			   }
		 );
	 }
	 
	 $scope.findOne = function(){
		 // 接收参数
		 var id = $location.search()['id'];
		 seckillGoodsService.findOne(id).success(
			 function(response){
				 $scope.entity = response;
				 // 倒计时开始
				 // 获取从结束时间到当前时间的秒数
				 totalseconds = Math.floor((new Date($scope.entity.endTime).getTime() - new Date().getTime())/1000);
				 
				 time = $interval(function(){
					$scope.timeString = convertTimeString(totalseconds);
					totalseconds = totalseconds-1;
					if(totalseconds <= 0){
							$interval.cancel(time);
						}
					},1000);
			 }
		 );
	 }
	 
	 // 将秒数转换为 天小时分钟秒格式 xxx天 xx:xx:xx
	 convertTimeString = function(totalseconds){
		 var days = Math.floor(totalseconds/(60*60*24));
		 var hours = Math.floor((totalseconds - days*60*60*24)/(60*60));
		 var minutes = Math.floor((totalseconds - days*60*60*24 - hours*60*60)/60);
		 var seconds = totalseconds - days*60*60*24 - hours*60*60 - minutes*60;
		 var dayString = "";
		 var hoursString = "";
		 var minutesString = "";
		 var secondsString = "";
		 if(days > 0){
			 dayString += days + "天";
		 }
		 if(hours < 10){
			 hoursString += "0" + hours; 
		 }else{
			 hoursString += hours;
		 }
		 if(minutes < 10){
			 minutesString += "0" + minutes; 
		 }else{
			 minutesString += minutes;
		 }
		 if(seconds < 10){
			 secondsString += "0" + seconds; 
		 }else{
			 secondsString += seconds;
		 }
		 return dayString + hoursString + ":" + minutesString + ":" + secondsString;
	 }
	
	 $scope.submitOrder = function(){
		 seckillGoodsService.submitOrder($scope.entity.id).success(
				function(response){
					if(response.success){
						alert("抢购成功,请在5分钟之内完成支付");
						location.href="pay.html";
					}else{
						alert(response.message);
					}
				}
		 );
	 }
});