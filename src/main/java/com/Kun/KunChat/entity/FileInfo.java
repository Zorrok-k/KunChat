package com.Kun.KunChat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName file_info
 */
@TableName(value ="file_info")
@Data
public class FileInfo {
    /**
     * 文件的校验值，一般是32位的十六进制编码，全网唯一
     */
    @TableId(value = "md5")
    private String md5;

    /**
     * 文件名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 文件路径
     */
    @TableField(value = "src")
    private String src;

    /**
     * 0未知类型 1图片 2视频 3音频 4文档 5压缩文件
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 0禁止 1启用
     */
    @TableField(value = "status")
    private Integer status;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FileInfo other = (FileInfo) that;
        return (this.getMd5() == null ? other.getMd5() == null : this.getMd5().equals(other.getMd5()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getSrc() == null ? other.getSrc() == null : this.getSrc().equals(other.getSrc()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMd5() == null) ? 0 : getMd5().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getSrc() == null) ? 0 : getSrc().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", md5=").append(md5);
        sb.append(", name=").append(name);
        sb.append(", src=").append(src);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}