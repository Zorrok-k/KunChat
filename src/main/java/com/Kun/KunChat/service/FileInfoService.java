package com.Kun.KunChat.service;

import com.Kun.KunChat.entity.FileInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Kun
 * @description 针对表【file_info】的数据库操作Service
 * @createDate 2025-05-04 18:18:38
 */
public interface FileInfoService extends IService<FileInfo> {

    FileInfo isExist(String md5);

    FileInfo addFile(String md5, MultipartFile file);

    FileInfo updateFile(String md5, int status);

}
