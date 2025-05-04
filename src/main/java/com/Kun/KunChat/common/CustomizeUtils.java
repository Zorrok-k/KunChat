package com.Kun.KunChat.common;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.Kun.KunChat.StaticVariable.StaticVariable.*;

/**
 * Author: Beta
 * Date: 2025/4/23 9:57
 * Param:
 * Return:
 * Description: 一个自定义工具类
 **/

@Component
public class CustomizeUtils {

    // 用于文件类型识别
    public static final int UNKNOWN = 0;
    public static final int IMAGE = 1;
    public static final int VIDEO = 2;
    public static final int AUDIO = 3;
    public static final int DOCUMENT = 4;
    public static final int ARCHIVE = 5;

    // 获取一个UUID默认16位
    public String getUUID(int size) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, size);
    }

    public String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    // 上传文件 专门处理用户和群组这两种图片的
    public void upLoad(MultipartFile file, String id, int type) throws IOException {
        File targetFileFolder = switch (type) {
            // 传用户头像
            case 0 -> new File(USER_PATH + id + "/picture", USER_AVATAR_NAME);
            // 传用户信息背景图
            case 1 -> new File(USER_PATH + id + "/picture", USERINFO_COVER_NAME);
            // 传群组头像
            case 2 -> new File(GROUP_PATH + id + "/picture", GROUP_AVATAR_NAME);
            // 传群组信息背景图
            case 3 -> new File(GROUP_PATH + id + "/picture", GROUP_COVER_NAME);
            default -> null;
        };
        // 目标路径不存在就要创建
        assert targetFileFolder != null;
        if (!targetFileFolder.exists()) {
            targetFileFolder.mkdirs();
        }
        file.transferTo(targetFileFolder);
    }

    // 文件类型识别
    public int getFileType(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return UNKNOWN;
        }

        byte[] buffer = new byte[8]; // 读取前几个字节用于判断魔数
        try (InputStream is = file.getInputStream()) {
            int bytesRead = is.read(buffer);
            if (bytesRead < 4) {
                return UNKNOWN;
            }

            // JPEG 图片
            if ((buffer[0] & 0xFF) == 0xFF && (buffer[1] & 0xFF) == 0xD8) {
                return IMAGE;
            }
            // PNG 图片
            else if (buffer[0] == -119 && buffer[1] == 'P' && buffer[2] == 'N' && buffer[3] == 'G') {
                return IMAGE;
            }
            // GIF 图片
            else if (buffer[0] == 'G' && buffer[1] == 'I' && buffer[2] == 'F' && buffer[3] == '8') {
                return IMAGE;
            }
            // PDF 文档
            else if (buffer[0] == '%' && buffer[1] == 'P' && buffer[2] == 'D' && buffer[3] == 'F') {
                return DOCUMENT;
            }
            // ZIP 压缩包
            else if (buffer[0] == 'P' && buffer[1] == 'K' && buffer[2] == 3 && buffer[3] == 4) {
                return ARCHIVE;
            }
            // RAR 压缩包
            else if (buffer[0] == 'R' && buffer[1] == 'a' && buffer[2] == 'r' && buffer[3] == '!') {
                return ARCHIVE;
            }

            // MP4 视频 (ftyp 或 moov 开头)
            else if (buffer[0] == 'f' && buffer[1] == 't' && buffer[2] == 'y' && buffer[3] == 'p') {
                return VIDEO;
            }
            // AVI 视频
            else if (buffer[0] == 'R' && buffer[1] == 'I' && buffer[2] == 'F' && buffer[3] == 'F') {
                return VIDEO;
            }
            // MOV / QuickTime 视频
            else if ((buffer[0] & 0xFF) == 0x00 && (buffer[1] & 0xFF) == 0x00 &&
                    (buffer[2] & 0xFF) == 0x00 && (buffer[3] & 0xFF) == 0x14 &&
                    (buffer[4] & 0xFF) == 0x66 && (buffer[5] & 0xFF) == 0x72 &&
                    (buffer[6] & 0xFF) == 0x65 && (buffer[7] & 0xFF) == 0x65) {
                return VIDEO;
            }

            // MP3 音频（ID3v2 tag header）
            else if (buffer[0] == 'I' && buffer[1] == 'D' && buffer[2] == '3') {
                return AUDIO;
            }
            // WAV 音频
            else if (buffer[0] == 'f' && buffer[1] == 'L' && buffer[2] == 'a' && buffer[3] == 'C') {
                return AUDIO;
            }
            // OGG 音频
            else if (buffer[0] == 'O' && buffer[1] == 'g' && buffer[2] == 'g' && buffer[3] == 'S') {
                return AUDIO;
            }
        }

        return UNKNOWN; // 默认为未知类型
    }
}
