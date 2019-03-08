app.service('searchService' ,function($http){
	
	//搜索商品
	this.search=function(searchMap){
		return $http.post('itemsearch/search.do',searchMap);
	}
	
});