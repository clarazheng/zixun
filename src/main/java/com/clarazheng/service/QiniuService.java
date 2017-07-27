package com.clarazheng.service;

import com.alibaba.fastjson.JSONObject;
import com.clarazheng.util.ZixunUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by clara on 2017/5/10.
 */
@Service
public class QiniuService
{
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //构造一个带指定Zone对象的配置类
    Configuration cfg = new Configuration(Zone.zone0());
    //...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);
    //...生成上传凭证，然后准备上传
    //...生成上传凭证，然后准备上传
    String accessKey = "your access key";
    String secretKey = "your secret key";
    String bucket = "your bucket name";

    private static String QINIU_IMAGE_DOMAIN ="http://opj52kf1i.bkt.clouddn.com/";
    Auth auth = Auth.create(accessKey, secretKey);
    String upToken = auth.uploadToken(bucket);
    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos=file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String ext=file.getOriginalFilename().substring(dotPos+1).toLowerCase();
            if(!ZixunUtil.isFileAllowed(ext)){
                return null;
            }
            String fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+ext;
            Response response = uploadManager.put(file.getBytes(),fileName, upToken);
            if (response.isOK() && response.isJson()) {
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(response.bodyString()).get("key");
            } else {
                logger.error("七牛异常:" + response.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            logger.error("七牛异常:" + e.getMessage());
            return null;
        }
    }
}
