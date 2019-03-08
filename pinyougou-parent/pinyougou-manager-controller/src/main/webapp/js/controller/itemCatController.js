 //控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    $scope.findByParentId = function(parentId){
    	itemCatService.findByParentId(parentId).success(
    			function(response){
    				$scope.list = response;
    				$scope.parentId = parentId;
    			}
    	);
    }
    
    $scope.grade = 1; //当前级别
    $scope.parentId = 0; //记录当前上级分类ID,默认为0
    
    $scope.setGrade = function(value){
    	$scope.grade = value;
    }
    
    $scope.selectList = function(temp_entity){
    	
    	if($scope.grade == 1){
    		$scope.entity_1 = null;
    		$scope.entity_2 = null;
    	}
    	if($scope.grade == 2){
    		$scope.entity_1 = temp_entity;
    		$scope.entity_2 = null;
    	}
    	if($scope.grade == 3){
    		$scope.entity_2 = temp_entity;
    	}
    	
    	$scope.findByParentId(temp_entity.id);
    
    }
    

	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			//设置其上级ID
			$scope.entity.parentId = $scope.parentId;
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.findByParentId($scope.parentId);//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	$scope.typeTemplateList={data:[]};//类型模板列表
	
	//读取类型模板列表
	$scope.findTypeTemplateList = function(){
		typeTemplateService.selectOptionList().success(
			function(response){
				$scope.typeTemplateList={data:response};
			}
		);		
	}
	
	//查询商品分类实例
	$scope.findOne = function(id){
		itemCatService.findOne(id).success(
				function(response){
					$scope.entity = response;
				}
		);
	}
	
	//批量删除
	$scope.dele = function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.findByParentId($scope.parentId);//刷新列表
					$scope.selectIds=[];
				}else{
					$scope.selectIds=[];
					alert(response.message);
				}
			}		
		);				
	}
	
});	

	
	 

	



