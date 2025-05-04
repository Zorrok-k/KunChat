package com.Kun.KunChat.service.impl;

import com.Kun.KunChat.common.CustomizeUtils;
import com.Kun.KunChat.entity.FileInfo;
import com.Kun.KunChat.mapper.FileInfoMapper;
import com.Kun.KunChat.service.FileInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static com.Kun.KunChat.StaticVariable.StaticVariable.CHATFILES_PATH;
import static com.Kun.KunChat.StaticVariable.StaticVariable.CHATFILES_SRC;

/**
 * @author Kun
 * @description 针对表【file_info】的数据库操作Service实现
 * @createDate 2025-05-04 18:18:38
 */
@Slf4j
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo>
        implements FileInfoService {

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private CustomizeUtils customizeUtils;

    @Override
    public FileInfo isExist(String md5) {
        return fileInfoMapper.selectById(md5);
    }

    @Override
    @Transactional
    public FileInfo addFile(String md5, MultipartFile file) {
        // 生成唯一标识
        String fileId = customizeUtils.getUUID();
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        // 提取文件后缀
        String prefix = "";
        String suffix = "";
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            prefix = originalFilename.substring(0, lastDotIndex);
            suffix = originalFilename.substring(lastDotIndex); // 包括点号 ".jpg"
        } else {
            prefix = originalFilename;
        }

        // 构建新文件名（原文件名 + 自定义后缀 + 后缀扩展名）
        String newFileName = prefix + '_' + fileId + suffix;

        // 添加至数据库
        FileInfo fileInfo = new FileInfo();
        fileInfo.setMd5(md5);
        fileInfo.setName(newFileName);
        fileInfo.setSrc(CHATFILES_SRC + fileId + suffix);
        try {
            fileInfo.setType(customizeUtils.getFileType(file));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        fileInfo.setStatus(1);
        fileInfoMapper.insert(fileInfo);

        // 写入文件
        File targetFileFolder = new File(CHATFILES_PATH, fileId + suffix);

        // 目标路径不存在就要创建
        if (!targetFileFolder.exists()) {
            targetFileFolder.mkdirs();
        }
        try {
            file.transferTo(targetFileFolder);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return fileInfoMapper.selectById(md5);
    }

    @Override
    public FileInfo updateFile(String md5, int status) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setMd5(md5);
        fileInfo.setStatus(status);
        fileInfoMapper.updateById(fileInfo);
        return fileInfoMapper.selectById(md5);
    }
}




