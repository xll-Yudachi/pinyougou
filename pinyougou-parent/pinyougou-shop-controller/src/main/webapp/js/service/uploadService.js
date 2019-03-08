/**
 * 文件上传服务前端service
 */
app.service('uploadService',function($http){
	
	 //上传文件
	 this.uploadFile = function() {
		//FormData是html5中将上传文件包装成二进制的类
		var formdata = new FormData();
		//file是文件上传框的name file.files[0]表示第一个文件上传框
		formdata.append('file',file.files[0]);
		
		//文件上传时只能用以下格式的$http
		return $http({
			url:'../upload.do',
			method:'post',
			data:formdata,
			//默认为json格式 设置为undefined则为multipart格式
			headers:{'Content-Type':undefined},
			transformRequest:angular.identity
		});
		
	 }
});