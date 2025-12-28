package com.sky.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String projectPath;

    /**
     * 文件上传
     *
     * @param bytes 文件的字节数组形式
     * @param objectName 上传的原文件名
     * @return
     */
    public String upload(byte[] bytes, String objectName) {

        DefaultCredentialProvider credentialProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);

        //创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //指定文件名称：项目名称/年/月/UUID.suffix
        String dirName=projectPath + "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        String newFileName = UUID.randomUUID().toString()+objectName.substring(objectName.lastIndexOf("."));
        objectName = dirName + "/" + newFileName;

        try {
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            log.error("OSS服务异常，请求已发送但被拒绝: Error Code: {}, Error Message: {}, Request ID: {}, Host ID: {}", 
                     oe.getErrorCode(), oe.getErrorMessage(), oe.getRequestId(), oe.getHostId());
            throw new RuntimeException("OSS服务异常: " + oe.getErrorMessage(), oe);
        } catch (ClientException ce) {
            log.error("客户端异常，可能无法访问网络: Error Message: {}", ce.getMessage());
            throw new RuntimeException("客户端异常: " + ce.getMessage(), ce);
        } finally {
            if (ossClient != null) {
                try {
                    ossClient.shutdown();
                } catch (Exception e) {
                    log.warn("关闭OSS客户端时发生异常", e);
                }
            }
        }

        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);

        log.info("文件上传到:{}", stringBuilder.toString());

        return stringBuilder.toString();
    }
}