package com.micheal.file.mongo.common.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import java.util.Optional;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/7/29 22:16
 * @Description 获取文件类型工具类
 */
public class FileExtensionUtils {

    private static final String[] VIDEO_ARRAY = new String[]{"mp4", "avi", "wma", "rmvb", "rm", "flash", "mid", "3gp", "wmv"};
    private static final String[] IMAGE_ARRAY = new String[]{"jpg", "jpeg", "png", "gif", "ico", "bmp"};
    private static final String[] AUDIO_ARRAY = new String[]{"mp3", "wav", "ogg", "m4a"};
    private static final String[] DOCUMENT_ARRAY = new String[]{"doc", "docx", "xls", "xlsx", "ppt", "pptx", "md", "pdf", "txt"};

    /**
     * 是否是视频文件
     * @Description FilenameUtils.getExtension(fileName) 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static Boolean isVideo(String fileName) {
        String extension = FilenameUtils.getExtension(fileName).toLowerCase();
        return ArrayUtils.contains(VIDEO_ARRAY, extension);
    }

    /**
     * 是否是图片
     * @param filename
     * @return
     */
    public static Boolean isImage(String filename){
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return ArrayUtils.contains(IMAGE_ARRAY, extension);
    }

    /**
     * 是否是音频文件
     * @param filename
     * @return
     */
    public static boolean isAudio(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return ArrayUtils.contains(AUDIO_ARRAY, extension);
    }

    /**
     * 是否是DOCUMENT文件
     * @param filename
     * @return
     */
    public static Boolean isDocument(String filename){
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        return ArrayUtils.contains(DOCUMENT_ARRAY, extension);
    }

    /**
     * 修复不支持的文件格式
     *
     * @param extension 文件后缀名
     * @return MediaType
     */
    public static Optional<String> fixContentType(String extension) {
        if (StringUtils.isBlank(extension)) {
            return Optional.empty();
        }

        // 文件名后缀转小写
        extension = extension.toLowerCase();

        String contentType = null;

        // 兼容 markdown 文件类型
        if ("md".equals(extension)) {
            contentType = MediaType.TEXT_MARKDOWN.toString();
        }

        return Optional.ofNullable(contentType);
    }

}
