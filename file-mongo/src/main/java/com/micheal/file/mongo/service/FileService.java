package com.micheal.file.mongo.service;

import com.micheal.file.mongo.model.view.FileForm;
import com.micheal.file.mongo.model.vo.FileVO;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/7/30 16:27
 * @Description
 */
public interface FileService {


    /**
     * 根据名称获取文件集合
     * @param form
     * @return
     */
    List<FileVO> findBy(FileForm form);

    /**
     * 文件上传
     * @param files
     */
    void upload(MultipartFile[] files);

    /**
     * 根据文件ID获取唯一文件
     *
     * @param id 文件ID
     * @return file
     */
    GridFSFile findById(String id);

    /**
     * 根据名字获取唯一文件
     *
     * @param filename 文件名字
     * @return file
     */
    GridFSFile findByFilename(String filename);


    /**
     * 删除
     *
     * @param id 文件ID
     */
    void deleteById(String id);

}
