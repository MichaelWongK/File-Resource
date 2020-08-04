package com.micheal.file.mongo.model.view;

import com.micheal.file.mongo.common.enums.FileTypeEnum;
import lombok.Data;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/7/30 16:19
 * @Description
 */
@Data
public class FileForm {

    /**
     * 文件名字
     */
    private String realName;
    /**
     * 文件类型
     */
    private FileTypeEnum fileTypeEnum;
}
