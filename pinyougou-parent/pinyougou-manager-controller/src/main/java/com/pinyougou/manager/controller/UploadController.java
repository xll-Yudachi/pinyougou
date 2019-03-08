package com.pinyougou.manager.controller;

import javax.print.DocFlavor.STRING;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pinyougou.entity.Result;

import util.FastDFSClient;

/**
 * 
 * Title:UploadController.java
 * Description: 运行商广告上传Controller
 * @author xll
 * @date 2019年2月11日 下午5:33:23
 * @version 1.0
 *
 */
@RestController
public class UploadController {
	

	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;
	
	@RequestMapping("/upload")
	public Result upload(MultipartFile file) {
	
		
		//获取文件名
		String originalFilename = file.getOriginalFilename();
		//获取扩展名
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1 );
		
		try {
			FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
			String FILE_ID = client.uploadFile(file.getBytes(),extName);
			//图片完整路径
			String url = FILE_SERVER_URL + FILE_ID;
			
			return new Result(true, url);
			
		} catch (Exception e) {
			return new Result(false, "上传失败");
		}
	}
}
