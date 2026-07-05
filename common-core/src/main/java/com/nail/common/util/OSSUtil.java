package com.nail.common.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传OSS工具类
 * 用于头像、美甲作品图等文件上传
 *
 * @author nail-platform
 */
@Slf4j
@Component
public class OSSUtil {

    /**
     * OSS地域节点
     */
    @Value("${aliyun.oss.endpoint:}")
    private String endpoint;

    /**
     * OSS AccessKey ID
     */
    @Value("${aliyun.oss.accessKeyId:}")
    private String accessKeyId;

    /**
     * OSS AccessKey Secret
     */
    @Value("${aliyun.oss.accessKeySecret:}")
    private String accessKeySecret;

    /**
     * OSS存储桶名称
     */
    @Value("${aliyun.oss.bucketName:}")
    private String bucketName;

    /**
     * 访问域名（可选，用于自定义域名）
     */
    @Value("${aliyun.oss.domain:}")
    private String domain;

    /**
     * 上传文件
     *
     * @param file 文件
     * @param directory 目录（如：avatar, artwork等）
     * @return 文件访问URL
     */
    public String upload(MultipartFile file, String directory) {
        // 参数校验
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        try {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String fileName = generateFileName(extension);

            // 构建对象键
            String objectKey = buildObjectKey(directory, fileName);

            // 上传文件
            uploadFile(file.getInputStream(), objectKey, file.getContentType(), file.getSize());

            // 返回访问URL
            return getFileUrl(objectKey);

        } catch (IOException e) {
            log.error("文件上传失败: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 上传文件（使用输入流）
     *
     * @param inputStream 文件流
     * @param directory  目录
     * @param contentType 内容类型
     * @param fileSize   文件大小
     * @return 文件访问URL
     */
    public String upload(InputStream inputStream, String directory, String contentType, long fileSize) {
        try {
            // 生成唯一文件名
            String extension = getExtensionFromContentType(contentType);
            String fileName = generateFileName(extension);

            // 构建对象键
            String objectKey = buildObjectKey(directory, fileName);

            // 上传文件
            uploadFile(inputStream, objectKey, contentType, fileSize);

            // 返回访问URL
            return getFileUrl(objectKey);

        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL
     * @return 是否删除成功
     */
    public boolean delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        try {
            // 从URL中提取对象键
            String objectKey = extractObjectKeyFromUrl(fileUrl);
            if (objectKey == null) {
                log.warn("无法从URL中提取对象键: {}", fileUrl);
                return false;
            }

            // 删除文件
            getOSSClient().deleteObject(bucketName, objectKey);
            log.info("文件删除成功: {}", objectKey);
            return true;

        } catch (Exception e) {
            log.error("文件删除失败: {}", fileUrl, e);
            return false;
        }
    }

    /**
     * 获取文件临时访问URL（带签名）
     *
     * @param fileUrl 文件URL
     * @param expiration 过期时间（分钟）
     * @return 临时访问URL
     */
    public String getTemporaryUrl(String fileUrl, int expiration) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }

        try {
            // 从URL中提取对象键
            String objectKey = extractObjectKeyFromUrl(fileUrl);
            if (objectKey == null) {
                return fileUrl;
            }

            // 设置过期时间
            Date expirationDate = new Date(System.currentTimeMillis() + expiration * 60 * 1000L);

            // 生成带签名的URL
            URL url = getOSSClient().generatePresignedUrl(bucketName, objectKey, expirationDate);
            return url.toString();

        } catch (Exception e) {
            log.error("生成临时URL失败: {}", fileUrl, e);
            return fileUrl;
        }
    }

    /**
     * 上传文件核心方法
     */
    private void uploadFile(InputStream inputStream, String objectKey, String contentType, long fileSize) {
        // 创建OSSClient
        OSS ossClient = getOSSClient();

        try {
            // 设置元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileSize);
            metadata.setContentType(contentType);

            // 创建上传请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, inputStream, metadata);

            // 上传文件
            ossClient.putObject(putObjectRequest);
            log.info("文件上传成功: {}", objectKey);

        } finally {
            // 关闭OSSClient
            ossClient.shutdown();
        }
    }

    /**
     * 获取OSSClient实例
     */
    private OSS getOSSClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 构建对象键
     */
    private String buildObjectKey(String directory, String fileName) {
        if (directory == null || directory.isEmpty()) {
            return fileName;
        }
        // 统一使用斜杠分隔
        return directory.replaceAll("^/|/$", "") + "/" + fileName;
    }

    /**
     * 生成唯一文件名
     */
    private String generateFileName(String extension) {
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf(".");
        return lastDotIndex > 0 ? filename.substring(lastDotIndex) : "";
    }

    /**
     * 根据ContentType获取扩展名
     */
    private String getExtensionFromContentType(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            return "";
        }

        switch (contentType.toLowerCase()) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "image/webp":
                return ".webp";
            default:
                return "";
        }
    }

    /**
     * 获取文件访问URL
     */
    private String getFileUrl(String objectKey) {
        // 如果配置了自定义域名，使用自定义域名
        if (domain != null && !domain.isEmpty()) {
            return domain.replaceAll("/$", "") + "/" + objectKey;
        }
        // 否则使用默认的OSS访问路径
        return "https://" + bucketName + "." + endpoint.replaceAll("^https?://", "") + "/" + objectKey;
    }

    /**
     * 从URL中提取对象键
     */
    private String extractObjectKeyFromUrl(String fileUrl) {
        try {
            // 如果使用自定义域名
            if (domain != null && !domain.isEmpty() && fileUrl.startsWith(domain)) {
                return fileUrl.substring(domain.replaceAll("/$", "").length() + 1);
            }
            // 如果使用默认OSS域名
            String ossDomain = "https://" + bucketName + "." + endpoint.replaceAll("^https?://", "") + "/";
            if (fileUrl.startsWith(ossDomain)) {
                return fileUrl.substring(ossDomain.length());
            }
            return null;
        } catch (Exception e) {
            log.error("提取对象键失败: {}", fileUrl, e);
            return null;
        }
    }
}
