package com.pinyougou.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

/**
 * 文件上传
 */
@RestController
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String file_server_url;//文件服务器地址

    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
    //取文件的扩展名
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);

        try {
            //创建一个FastDFS的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            //执行上传处理
            String path = fastDFSClient.uploadFile(file.getBytes(), extName);
            //拼接返回的url和ip地址  拼装成完整的url
            String url = file_server_url + path ;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }

}
