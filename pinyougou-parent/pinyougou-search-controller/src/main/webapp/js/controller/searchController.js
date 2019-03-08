app.controller('searchController',function($scope,searchService,$location){
	
	//定义搜索对象的结构
	$scope.searchMap = {'keywords':'','category':'','brand':'','spec':{},'price':'','pageNum':1,'pageSize':40,'sort':'','sortField':''};

	//搜索
	$scope.search=function(){
		//因为在文本框中读取的是String类型 在后端查找时强制转换成Integer类型会出现错误
		$scope.searchMap.pageNum = parseInt($scope.searchMap.pageNum);
		
		searchService.search($scope.searchMap).success(
			function(response){
				$scope.resultMap = response;
				buildPageLabel();
			}
		);		
	}
	
	buildPageLabel = function(){
		//构建分页栏
		$scope.pageLabel = [];
		//开始页码
		var firstPage = 1;
		//截止页码
		var lastPage = $scope.resultMap.totalPages;
		//前面有省略号
		$scope.firstDot = true;
		//后面有省略号
		$scope.lastDot = true;
		
		//如果总页码数量大于5
		if($scope.resultMap.totalPages > 5){
			//如果当前页码小于等于3  显示前5页
			if($scope.searchMap.pageNum <= 3){
				lastPage = 5;
				//前面没省略号
				$scope.firstDot = false;
			}else if($scope.searchMap.pageNum >= $scope.resultMap.totalPages - 2){
				//显示最后5页
				firstPage = $scope.resultMap.totalPages - 4;
				//后面没省略号
				$scope.lastDot = false;
			}else{
				//显示以当前页为中心的5页
				firstPage = $scope.searchMap.pageNum - 2;
				lastPage = $scope.searchMap.pageNum + 2;
			}
		}else{
			$scope.firstDot = false;
			$scope.lastDot = false;
		}
		
		
		for(var i = firstPage; i <= lastPage; i++){
			$scope.pageLabel.push(i);
		}
	}
	
	//添加搜索项 改变searchMap的值
	$scope.addSearchItem = function(key,value){
		
		//如果是品牌或者规格
		if(key=='category' || key=='brand' || key=='price'){
			$scope.searchMap[key] = value;
		}else{
			//用户点击的是规格
			$scope.searchMap.spec[key] = value;
		}
		//进行查询
		$scope.search();
	}
	
	//撤销搜索项
	$scope.removeSearchItem = function(key){
		if(key=='category' || key=='brand' || key=='price'){
			//如果用户点击的是商品分类或者商品品牌
			$scope.searchMap[key] = "";
		}else{
			//用户点击的是规格
			delete $scope.searchMap.spec[key];
		}
		//进行查询
		$scope.search();
	}
	
	//分页查询
	$scope.queryByPage = function(pageNum){
		if(pageNum < 1 || pageNum > $scope.resultMap.totalPages){
			return ;
		}
		$scope.searchMap.pageNum = pageNum;
		$scope.search();
	}
	
	//判断当前页是否是第一页
	$scope.isTopPage = function(){
		if($scope.searchMap.pageNum == 1){
			return true;
		}else{
			return false;
		}
	}
	
	//判断当前页是否为最后一页
	$scope.isEndPage = function(){
		if($scope.searchMap.pageNum == $scope.resultMap.totalPages){
			return true;
		}else{
			return false;
		}
	}
	
	//排序查询
	$scope.sortSearch = function(sortField,sort){
		$scope.searchMap.sort = sort;
		$scope.searchMap.sortField = sortField;
		
		$scope.search();
	}
	
	//判断关键字是否是品牌
	$scope.keywordsIsBrand = function(){
		for(var i=0; i<$scope.resultMap.brandList.length; i++){
			if($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text) >= 0){
				return true;
			}
		}
		return false;
	}
	
	//主页跳转到此搜索页面后进行自动搜索
	$scope.loadKeywords = function(){
		$scope.searchMap.keywords = $location.search()['keywords'];
		$scope.search();
	}
}); 