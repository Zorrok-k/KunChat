package com.Kun.KunChat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @TableName group_info
 */
@TableName(value = "group_info")
@Data
public class GroupInfo {
    // 自定义 @Validated 注解分组
    public interface UpdateGroup {
    }

    /**
     * 群组ID
     */
    @TableId(value = "group_id")
    @Size(max = 16, groups = GroupInfo.UpdateGroup.class)
    @NotEmpty(groups = GroupInfo.UpdateGroup.class)
    private String groupId;

    /**
     * 群组名
     */
    @TableField(value = "group_name")
    @Size(max = 20, groups = GroupInfo.UpdateGroup.class)
    private String groupName;

    /**
     * 群主ID
     */
    @TableField(value = "owner_id")
    @Size(max = 16, groups = GroupInfo.UpdateGroup.class)
    private String ownerId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @Null(groups = GroupInfo.UpdateGroup.class)
    private LocalDateTime createTime;

    /**
     * 群公告
     */
    @TableField(value = "notice")
    @Size(max = 500, groups = GroupInfo.UpdateGroup.class)
    private String notice;

    /**
     * 0：直接加入；1：管理员同意后加入
     */
    @TableField(value = "join_type")
    private Integer joinType;

    /**
     * 群组状态 1：正常；0：封禁
     */
    @TableField(value = "status")
    @Null(groups = GroupInfo.UpdateGroup.class)
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
        GroupInfo other = (GroupInfo) that;
        return (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
                && (this.getGroupName() == null ? other.getGroupName() == null : this.getGroupName().equals(other.getGroupName()))
                && (this.getOwnerId() == null ? other.getOwnerId() == null : this.getOwnerId().equals(other.getOwnerId()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getNotice() == null ? other.getNotice() == null : this.getNotice().equals(other.getNotice()))
                && (this.getJoinType() == null ? other.getJoinType() == null : this.getJoinType().equals(other.getJoinType()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getOwnerId() == null) ? 0 : getOwnerId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getNotice() == null) ? 0 : getNotice().hashCode());
        result = prime * result + ((getJoinType() == null) ? 0 : getJoinType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", groupId=").append(groupId);
        sb.append(", groupName=").append(groupName);
        sb.append(", ownerId=").append(ownerId);
        sb.append(", createTime=").append(createTime);
        sb.append(", notice=").append(notice);
        sb.append(", joinType=").append(joinType);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}