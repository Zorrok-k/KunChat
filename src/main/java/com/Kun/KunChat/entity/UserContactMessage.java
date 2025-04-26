package com.Kun.KunChat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName user_contact_message
 */
@TableName(value = "user_contact_message")
@Data
public class UserContactMessage {
    /**
     * 消息自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 申请者ID
     */
    @TableField(value = "applicant_id")
    private String applicantId;

    /**
     * 受理者ID
     */
    @TableField(value = "acceptor_id")
    private String acceptorId;

    /**
     * 申请类型：0 好友  1 群组
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 申请建立联系的ID，用户或群组
     */
    @TableField(value = "contact_id")
    private String contactId;

    /**
     * 该消息状态：0 待处理  1 已同意  2 已拒绝  3 已拉黑  4 已忽略
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 申请信息
     */
    @TableField(value = "apply_info")
    private String applyInfo;

    /**
     * 最后申请时间
     */
    @TableField(value = "last_apply_time")
    private Long lastApplyTime;

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
        UserContactMessage other = (UserContactMessage) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getApplicantId() == null ? other.getApplicantId() == null : this.getApplicantId().equals(other.getApplicantId()))
                && (this.getAcceptorId() == null ? other.getAcceptorId() == null : this.getAcceptorId().equals(other.getAcceptorId()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getContactId() == null ? other.getContactId() == null : this.getContactId().equals(other.getContactId()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getApplyInfo() == null ? other.getApplyInfo() == null : this.getApplyInfo().equals(other.getApplyInfo()))
                && (this.getLastApplyTime() == null ? other.getLastApplyTime() == null : this.getLastApplyTime().equals(other.getLastApplyTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getApplicantId() == null) ? 0 : getApplicantId().hashCode());
        result = prime * result + ((getAcceptorId() == null) ? 0 : getAcceptorId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getContactId() == null) ? 0 : getContactId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getApplyInfo() == null) ? 0 : getApplyInfo().hashCode());
        result = prime * result + ((getLastApplyTime() == null) ? 0 : getLastApplyTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", applicantId=").append(applicantId);
        sb.append(", acceptorId=").append(acceptorId);
        sb.append(", type=").append(type);
        sb.append(", contactId=").append(contactId);
        sb.append(", status=").append(status);
        sb.append(", applyInfo=").append(applyInfo);
        sb.append(", lastApplyTime=").append(lastApplyTime);
        sb.append("]");
        return sb.toString();
    }
}