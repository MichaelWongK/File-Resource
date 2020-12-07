package com.micheal.file.mongo.service.impl;

import com.micheal.file.mongo.common.constant.FileConstant;
import com.micheal.file.mongo.common.enums.FileTypeEnum;
import com.micheal.file.mongo.common.util.FileExtensionUtils;
import com.micheal.file.mongo.model.view.FileForm;
import com.micheal.file.mongo.model.vo.FileVO;
import com.micheal.file.mongo.service.FileService;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/7/30 16:27
 * @Description
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private GridFsTemplate gridFsTemplate;
    private static final ThreadLocal<DateFormat> YEAR_2_SECOND_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    @Override
    public List<FileVO> findBy(FileForm form) {
        Query query =new Query();
        query.with(Sort.by(Sort.Order.desc("uploadDate")));
        if (form != null) {
            if (StringUtils.isNoneBlank(form.getRealName())) {
                query.addCriteria(GridFsCriteria.whereMetaData(FileConstant.FILENAME).regex(".*?" + form.getRealName() + ".*"));
            }
            if (form.getFileTypeEnum() != null) {
                query.addCriteria(GridFsCriteria.whereContentType().is(form.getFileTypeEnum().name()));
            }
        }

        List<FileVO> list = new ArrayList<>();

        MongoCursor<GridFSFile> iterator = gridFsTemplate.find(query).iterator();
        iterator.forEachRemaining(gridFSFile -> {
            // 基础信息
            FileVO fileVO = new FileVO()
                    .setId(gridFSFile.getObjectId().toString())
                    .setFilename(gridFSFile.getFilename().toLowerCase())
                    .setUploadDate(YEAR_2_SECOND_FORMAT.get().format(gridFSFile.getUploadDate()));

            // 附加信息
            Optional.ofNullable(gridFSFile.getMetadata()).ifPresent(document -> {
                String filename = document.getString(FileConstant.FILENAME);
                String contentType = document.getString(FileConstant.FILE_TYPE);
                String fileType =FileTypeEnum.valueOf(document.getString(FileConstant.FILE_TYPE)).getName();

                fileVO.setRealName(filename).setContentType(contentType).setFileType(fileType);
            });

            list.add(fileVO);

        });

        return list;
    }

    @Override
    public String upload(MultipartFile[] files) {
        if (files.length == 0) {
            return null;
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            // 文件原始名称
            String originalFilename = file.getOriginalFilename();
            // 文件后缀
            String extension = FilenameUtils.getExtension(originalFilename);
            // 文件新名称
            String filename = System.currentTimeMillis() + "." + extension;
            // 文件类型
            AtomicReference<String> contentType = new AtomicReference<>(file.getContentType());
            FileExtensionUtils.fixContentType(extension).ifPresent(contentType::set);

            Document document = new Document()
                    .append(FileConstant.FILENAME, originalFilename)
                    .append(FileConstant.CONTENT_TYPE, contentType.get())
                    .append(FileConstant.FILE_TYPE, FileTypeEnum.getFileType(originalFilename));
            try {
                ObjectId objectId = gridFsTemplate.store(file.getInputStream(), filename, contentType.get(), document);
                return objectId.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public GridFSFile findById(String id) {
        return gridFsTemplate.findOne(query(where("_id").is(id)));
    }

    @Override
    public GridFSFile findByFilename(String filename) {
        return gridFsTemplate.findOne(query(GridFsCriteria.whereFilename().is(filename)));
    }

    @Override
    public void deleteById(String id) {
        gridFsTemplate.delete(query(where("_id").is(id)));
    }
}
