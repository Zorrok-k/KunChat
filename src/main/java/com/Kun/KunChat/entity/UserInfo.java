package com.Kun.KunChat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息
 *
 * @TableName user_info
 */
@TableName(value = "user_info")
@Data
public class UserInfo {

    // 自定义 @Validated 注解分组
    public interface UpdateGroup {
    }

    /**
     * 用户ID
     */
    @TableId(value = "user_id")
    @Size(max = 16)
    private String userId;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    @Size(max = 16, groups = UserInfo.UpdateGroup.class)
    private String nickName;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @Email(groups = UserInfo.UpdateGroup.class)
    private String email;

    /**
     * 密码
     */
    @TableField(value = "password")
    @Size(max = 32, groups = UserInfo.UpdateGroup.class)
    private String password;

    /**
     * 个性签名
     */
    @TableField(value = "signature")
    @Size(max = 50, groups = UserInfo.UpdateGroup.class)
    private String signature;

    /**
     * 0：女；1：男
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 0：不需要同意；1：需要同意
     */
    @TableField(value = "join_type")
    private Integer joinType;

    /**
     * 0：封禁；1：正常使用
     */
    @TableField(value = "status")
    @Null(groups = UserInfo.UpdateGroup.class)
    private Integer status;

    /**
     * 用户创建时间
     */
    @TableField(value = "create_time")
    @Null(groups = UserInfo.UpdateGroup.class)
    private LocalDateTime createTime;

    /**
     * 最后登录时间
     */
    @TableField(value = "last_login_time")
    @Null(groups = UserInfo.UpdateGroup.class)
    private LocalDateTime lastLoginTime;

    /**
     * 最后下线时间；需要记录到毫秒级
     */
    @TableField(value = "last_off_time")
    @Null(groups = UserInfo.UpdateGroup.class)
    private Long lastOffTime;

    /**
     * 地区名
     */
    @TableField(value = "area_name")
    private String areaName;

    /**
     * 地区编号
     */
    @TableField(value = "area_code")
    private String areaCode;

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
        UserInfo other = (UserInfo) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId())) && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName())) && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail())) && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword())) && (this.getSignature() == null ? other.getSignature() == null : this.getSignature().equals(other.getSignature())) && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender())) && (this.getJoinType() == null ? other.getJoinType() == null : this.getJoinType().equals(other.getJoinType())) && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus())) && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime())) && (this.getLastLoginTime() == null ? other.getLastLoginTime() == null : this.getLastLoginTime().equals(other.getLastLoginTime())) && (this.getLastOffTime() == null ? other.getLastOffTime() == null : this.getLastOffTime().equals(other.getLastOffTime())) && (this.getAreaName() == null ? other.getAreaName() == null : this.getAreaName().equals(other.getAreaName())) && (this.getAreaCode() == null ? other.getAreaCode() == null : this.getAreaCode().equals(other.getAreaCode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getSignature() == null) ? 0 : getSignature().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getJoinType() == null) ? 0 : getJoinType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getLastLoginTime() == null) ? 0 : getLastLoginTime().hashCode());
        result = prime * result + ((getLastOffTime() == null) ? 0 : getLastOffTime().hashCode());
        result = prime * result + ((getAreaName() == null) ? 0 : getAreaName().hashCode());
        result = prime * result + ((getAreaCode() == null) ? 0 : getAreaCode().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", nickName=").append(nickName);
        sb.append(", email=").append(email);
        sb.append(", password=").append(password);
        sb.append(", signature=").append(signature);
        sb.append(", gender=").append(gender);
        sb.append(", joinType=").append(joinType);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", lastOffTime=").append(lastOffTime);
        sb.append(", areaName=").append(areaName);
        sb.append(", areaCode=").append(areaCode);
        sb.append("]");
        return sb.toString();
    }
}