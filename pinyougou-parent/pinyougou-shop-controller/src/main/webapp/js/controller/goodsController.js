 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location,goodsService,uploadService,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
	$scope.entity = {goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(){				
		//获取页面传递的参数($location.search()方法会返回一个参数列表数组)
		//参数的获取方式：$location.search()['参数名']
		//页面上参数传递的格式为#?  例如#?id=11 否则AngularJS不能识别
		var id = $location.search()['id'];
		if(id == null){
			return ;
		}
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;	
				//商品介绍的回显
				editor.html($scope.entity.goodsDesc.introduction);
				//商品图片的回显(数据库中读取的是JSON格式字符串  转化为Json对象)
				$scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);
				//扩展属性
				$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
				//规格选项
				$scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
				//转换SKU列表中的规格对象
				for(var i=0; i<$scope.entity.itemList.length; i++){
					$scope.entity.itemList[i].spec=  JSON.parse($scope.entity.itemList[i].spec);	
				}
			}
		);				
	}

	//增加商品
	$scope.save=function(){	
		
		//获取富文本编辑器中的商品描述内容
		$scope.entity.goodsDesc.introduction = editor.html();
		//服务层对象
		var serviceObject;
		if($scope.entity.goods.id!=null){
			//修改
			serviceObject = goodsService.update($scope.entity);
		}else{
			//添加
			serviceObject = goodsService.add($scope.entity);
		}
		serviceObject.success(
			function(response){
				if(response.success){
					alert(response.message);
					/*//清空内容
					$scope.entity = {goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};
					//清空富文本编辑器
					editor.html("");*/
					location.href='goods.html';
				}else{
					alert(response.message);
				}
			}
		);	
	}
		
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
	//上传图片
	$scope.uploadFile = function(){
		uploadService.uploadFile().success(
				function(response){
					if(response.success){
						$scope.image_entity.url = response.message;
					}else{
						alert(response.message);
					}
				}
		);
	}

	//将当前上传的图片实体存入图片列表
	$scope.add_image_entity = function(){
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);
	}
	
	//移除图片
	$scope.remove_image_entity = function(index){
		$scope.entity.goodsDesc.itemImages.splice(index,1);
	}
	
	//查询一级商品分类列表
	$scope.selectItemCat_1_List = function(){
		itemCatService.findByParentId(0).success(
				function(response){
					$scope.itemCat_1_List = response;
				}
		);
	}
	
	//利用变量监控 实现查询二级商品分类列表
	$scope.$watch('entity.goods.category1Id',function(newValue,oldValue){
		itemCatService.findByParentId(newValue).success(
			function(response){
				$scope.itemCat_2_List = response;
			}	
		);
	});
	
	//利用变量监控 实现查询三级商品分类列表
	$scope.$watch('entity.goods.category2Id',function(newValue,oldValue){
		itemCatService.findByParentId(newValue).success(
			function(response){
				$scope.itemCat_3_List = response;
			}	
		);
	});
	
	//读取模板ID
	$scope.$watch('entity.goods.category3Id',function(newValue,oldValue){
		itemCatService.findOne(newValue).success(
			function(response){
				$scope.entity.goods.typeTemplateId = response.typeId;
			}
		);
	});
	
	//读取模板ID后,读取品牌列表
	$scope.$watch('entity.goods.typeTemplateId',function(newValue,oldValue){
		typeTemplateService.findOne(newValue).success(
			function(response){
				//模板对象
				$scope.typeTemplate = response;
				//品牌列表类型转换
				$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
				//扩展属性
				//如果为增加
				if($location.search()['id'] == null){
					$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
				}
			}
		);
		
		typeTemplateService.findSpecList(newValue).success(
				function(response){
					$scope.specList = response;
				}
		);
	});
	
	//更新规格列表属性
	$scope.updateSpecAttribute = function($event,name,value){
		
		var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,'attributeName',name);
	
		if(object!=null){
			//被勾选了
			if($event.target.checked){
				object.attributeValue.push(value);
			}else{
				//取消勾选 移除选项
				object.attributeValue.splice(object.attributeValue.indexOf(value),1);
				//如果选项都取消了 将此条记录移除
				if(object.attributeValue.length==0){
					$scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object),1);
				}
			}
			
		}else{
			$scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
		}
	}
	
	//创建SKU列表
	$scope.createItemList = function(){
		//列表初始化 方便进行深克隆
		$scope.entity.itemList = [{spec:{},price:0,num:9999,status:'0',isDefault:'0'}];
		
		var items = $scope.entity.goodsDesc.specificationItems;
		
		for(var i=0; i<items.length; i++){
			$scope.entity.itemList = addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
		}
	}
	
	addColumn = function(list,columnName,columnValues){
		var newList = [];
		for(var i=0;i<list.length;i++){
			var oldRow = list[i];
			for(var j=0; j<columnValues.length; j++){
				//深克隆的技巧
				var newRow = JSON.parse(JSON.stringify(oldRow));
				newRow.spec[columnName] = columnValues[j];
				newList.push(newRow);
			}
		}
		return newList;
	}
	
	//商品状态
	$scope.status = ['未审核','已审核','审核未通过','已关闭'];
	
	//商品分类列表
	$scope.itemCatList = [];

	$scope.findItemCatList = function(){
		itemCatService.findAll().success(
			function(response){
				for(var i=0; i<response.length; i++){
					$scope.itemCatList[response[i].id] = response[i].name;
				}
			}
		);
	}
	
	//判断规格与规格选项是否被勾选
	$scope.checkAttributeValue = function(specName,optionName){
		
		var items = $scope.entity.goodsDesc.specificationItems;
		
		var object = $scope.searchObjectByKey(items, 'attributeName', specName);
		
		if(object != null){
			if(object.attributeValue.indexOf(optionName) >= 0){
				//如果能查询到规格选项
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	$scope.updateIsMarketable = function(id,isMarketable){
		goodsService.updateIsMarketable(id,isMarketable).success(
			function(response){
				if(response.success){
					alert(response.message);
					$scope.reloadList();
				}else{
					alert(response.message);
				}
			}
		);
	}
});	
