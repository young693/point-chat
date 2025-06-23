package com.point.chat.pointcommon.config;

import lombok.Data;

/**
 * 文件上传配置
 */
@Data
public class UploadFileConfig {

    /**
     * 根路径
     */
    private String rootContext;

    /**
     * 服务器存储地址
     */
    private String rootPath;

    /**
     * 类型
     */
    private String type;

    /**
     * 扩展名
     */
    private String extStr = "png,jpg";

    /**
     * 文件大小上限
     */
    private int size = 2;

    /**
     * 是否压缩
     */
    private boolean compress;

}