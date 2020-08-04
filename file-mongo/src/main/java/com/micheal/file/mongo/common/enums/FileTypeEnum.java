package com.micheal.file.mongo.common.enums;

import com.micheal.file.mongo.common.util.FileExtensionUtils;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/7/29 23:01
 * @Description
 */
public enum FileTypeEnum {

    /***/
    VIDEO("视频"),
    AUDIO("音频"),
    IMAGE("图片"),
    DOCUMENT("文档"),
    OTHER("其他");

    private final String name;

    FileTypeEnum(String name) {
        this.name = name;
    }

    public static String getFileType(String fileName) {
        FileTypeEnum fileTypeEnum = OTHER;
        if (FileExtensionUtils.isVideo(fileName)) {
            fileTypeEnum = VIDEO;
        }else if (FileExtensionUtils.isAudio(fileName)) {
            fileTypeEnum = AUDIO;
        } else if (FileExtensionUtils.isImage(fileName)) {
            fileTypeEnum = IMAGE;
        } else if (FileExtensionUtils.isDocument(fileName)) {
            fileTypeEnum = DOCUMENT;
        }
        return fileTypeEnum.name();
    }

    public String getName() {
        return name;
    }
}
