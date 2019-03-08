//服务层
app.service('itemCatService',function($http){
	    	
	//根据父级id查询商品分类列表
	this.findByParentId = function(parentId){
		return  $http.get('../itemCat/findByParentId.do?parentId=' + parentId);
	}
	
	//添加商品类型
	this.add = function(entity){
		return $http.post('../itemCat/add.do',entity);
	}
	
	//查询商品类型实例
	this.findOne = function(id){
		return $http.get('../itemCat/findOne.do?id=' + id);
	}
	
	//修改商品分类信息
	this.update = function(entity){
		return $http.post('../itemCat/update.do',entity);
	}
	
	//批量删除商品分类
	this.dele = function(ids){
		return $http.get('../itemCat/delete.do?ids=' + ids);
	}
	
	//查询全部商品
	this.findAll = function(){
		return $http.get('../itemCat/findAll.do');
	}
});
