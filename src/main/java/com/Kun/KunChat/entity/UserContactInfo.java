package com.Kun.KunChat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @TableName user_contact_info
 */
@TableName(value = "user_contact_info")
@Data
public class UserContactInfo {
    public interface UpdateGroup {
    }

    /**
     * Mybatis Plus 不能使用联合主键，这个是凑数用的
     */
    @TableId(value = "id")
    private String id;

    /**
     * 所属用户ID
     */
    @TableField(value = "user_id")
    @Null(groups = UserContactInfo.UpdateGroup.class)
    private String userId;

    /**
     * 与之产生联系的用户ID或群组ID
     */
    @TableField(value = "contact_id")
    @NotEmpty(groups = UserContactInfo.UpdateGroup.class)
    private String contactId;

    /**
     * 0：代表好友关系；1：代表群组关系
     */
    @TableField(value = "type")
    @Null(groups = UserContactInfo.UpdateGroup.class)
    private Integer type;

    /**
     * 此ID对目标ID的状态；0：等待处理  1：产生联系（好友、群组）  2：删除  3：拉黑  4：被删除  5：被拉黑
     */
    @TableField(value = "status")
    @Null(groups = UserContactInfo.UpdateGroup.class)
    private Integer status;

    /**
     * 关系创建时间
     */
    @TableField(value = "create_time")
    @Null(groups = UserContactInfo.UpdateGroup.class)
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "last_update")
    @Null(groups = UserContactInfo.UpdateGroup.class)
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
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
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
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
        sb.append(", id=").append(id);
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