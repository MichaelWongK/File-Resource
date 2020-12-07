package com.micheal.file.mongo.web;

import com.micheal.file.mongo.common.constant.FileConstant;
import com.micheal.file.mongo.common.enums.FileTypeEnum;
import com.micheal.file.mongo.model.view.FileForm;
import com.micheal.file.mongo.service.FileService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Description mongo for FileController
 *
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/7/29 21:45
 */
@RestController
@RequestMapping("mongo")
public class FileController {

    @Autowired
    private FileService fileService;
    @Resource
    private MongoDatabaseFactory mongoDatabaseFactory;

    @GetMapping
    public ModelAndView index(FileForm form) {
        Map<String, Object> response = new HashMap<>(4);
        response.put("data", fileService.findBy(form));
        response.put("fileTypeEnums", FileTypeEnum.values());
        response.put("realName", form == null ? "" : form.getRealName());
        response.put("fileTypeEnum", form == null ? FileTypeEnum.AUDIO : form.getFileTypeEnum());

        return new ModelAndView("index", response);
    }



    /**
     * 文件上传
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("upload")
    public String upload(MultipartFile[] multipartFile) {
        return fileService.upload(multipartFile);
    }

    /**
     * 读取文件
     *
     * @param id       文件ID
     * @param response HttpServletResponse
     * @throws Exception 读取文件异常
     */
    @GetMapping(value = "read/{id}")
    public void readById(@PathVariable String id, HttpServletResponse response) throws Exception {
        GridFSFile gridFsFile = fileService.findById(id);
        Optional.ofNullable(gridFsFile.getMetadata())
                .ifPresent(document -> response.setContentType(document.getString(FileConstant.CONTENT_TYPE)));

        GridFSBucket bucket = GridFSBuckets.create(mongoDatabaseFactory.getMongoDatabase());
        GridFsResource resource = new GridFsResource(gridFsFile, bucket.openDownloadStream(gridFsFile.getObjectId()));

        OutputStream stream = response.getOutputStream();
        stream.write(IOUtils.toByteArray(resource.getInputStream()));
        stream.flush();
        stream.close();
    }

    /**
     * 删除文件
     *
     * @param id 文件ID
     * @return index页面
     */
    @DeleteMapping(value = "delete/{id}")
    public ModelAndView deleteById(@PathVariable String id) {
        fileService.deleteById(id);

        return index(null);
    }

}
