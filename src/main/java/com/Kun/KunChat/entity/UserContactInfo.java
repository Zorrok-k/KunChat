package com.Kun.KunChat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName user_contact_info
 */
@TableName(value ="user_contact_info")
@Data
public class UserContactInfo {
    /**
     * 所属用户ID
     */
    @TableId(value = "user_id")
    private String userId;

    /**
     * 与之产生联系的用户ID或群组ID
     */
    @TableId(value = "contact_id")
    private String contactId;

    /**
     * 0：代表好友关系；1：代表群组关系
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 此ID对目标ID的状态；0：好友  1：删除  2：拉黑  3：被删除  4：被拉黑
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 关系创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "last_update")
    private LocalDateTime lastUpdate;

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
        UserContactInfo other = (UserContactInfo) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getContactId() == null ? other.getContactId() == null : this.getContactId().equals(other.getContactId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getLastUpdate() == null ? other.getLastUpdate() == null : this.getLastUpdate().equals(other.getLastUpdate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getContactId() == null) ? 0 : getContactId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getLastUpdate() == null) ? 0 : getLastUpdate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", contactId=").append(contactId);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", lastUpdate=").append(lastUpdate);
        sb.append("]");
        return sb.toString();
    }
}