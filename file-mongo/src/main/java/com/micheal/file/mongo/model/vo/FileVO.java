package com.micheal.file.mongo.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/7/30 16:19
 * @Description
 */
@Data
@Accessors(chain = true)
public class FileVO {

    private String id;
    private String realName;
    private String filename;
    private String fileType;
    private String contentType;
    private String uploadDate;
}
