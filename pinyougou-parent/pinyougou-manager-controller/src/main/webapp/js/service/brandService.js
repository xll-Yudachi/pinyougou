//服务层
app.service('brandService',function($http){
	    	
	//查询所有商品品牌
	this.findAll=function(){
		return $http.get('../brand/findAll.do');		
	}
	//分页查询商品品牌
	this.findPage=function(page,rows){
		return $http.get('../brand/findPage.do?page='+page+'&rows='+rows);
	}
	//查询指定的商品品牌
	this.findOne=function(id){
		return $http.get('../brand/findBrand.do?id='+id);
	}
	//增加商品品牌
	this.add=function(entity){
		return  $http.post('../brand/add.do',entity );
	}
	//修改商品品牌
	this.update=function(entity){
		return  $http.post('../brand/update.do',entity );
	}
	//删除商品品牌
	this.dele=function(ids){
		return $http.get('../brand/delete.do?ids='+ids);
	}
	//搜索商品品牌
	this.search=function(page,rows,searchEntity){
		return $http.post('../brand/search.do?page='+page+"&size="+rows, searchEntity);
	}    
	//下拉列表数据
	this.selectOptionList=function(){
		return $http.get('../brand/selectOptionList.do');
	}
	
});
