package com.Kun.KunChat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Data;

/**
 * @TableName user_chat_info
 */
@TableName(value = "user_chat_info")
@Data
public class UserChatInfo {
    // 自定义 @Validated 注解分组
    public interface sendGroup {
    }

    public interface replyGroup {

    }

    /**
     * 聊天消息主键
     */
    @TableId(value = "id")
    @Null(groups = {sendGroup.class, replyGroup.class})
    private String id;

    /**
     * 发送方ID
     */
    @TableField(value = "sender_id")
    @NotEmpty(groups = {sendGroup.class, replyGroup.class})
    private String senderId;

    /**
     * 接收方ID
     */
    @TableField(value = "receiver_id")
    @NotEmpty(groups = {sendGroup.class, replyGroup.class})
    private String receiverId;

    /**
     * 回复的消息ID
     */
    @TableField(value = "reply_id")
    @NotEmpty(groups = replyGroup.class)
    private String replyId;

    /**
     * 0私聊 1群聊
     */
    @TableField(value = "chat_type")
    @NotEmpty(groups = replyGroup.class)
    private Integer chatType;

    /**
     * 消息类型：0文本消息 1图片消息 2视频消息 3文件消息 4回复消息
     */
    @TableField(value = "message_type")
    private Integer messageType;

    /**
     * 如果是文本消息就是text，如果是文字或图片消息就是一个src
     */
    @TableField(value = "content")
    @NotEmpty(groups = {sendGroup.class, replyGroup.class})
    private String content;

    /**
     * 0已过期 1正常 2撤回
     */
    @TableField(value = "status")
    @Null(groups = {sendGroup.class, replyGroup.class})
    private Integer status;

    /**
     * 消息创建时间的毫秒值
     */
    @TableField(value = "create_time")
    @Null(groups = {sendGroup.class, replyGroup.class})
    private Long createTime;

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
        UserChatInfo other = (UserChatInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSenderId() == null ? other.getSenderId() == null : this.getSenderId().equals(other.getSenderId()))
                && (this.getReceiverId() == null ? other.getReceiverId() == null : this.getReceiverId().equals(other.getReceiverId()))
                && (this.getReplyId() == null ? other.getReplyId() == null : this.getReplyId().equals(other.getReplyId()))
                && (this.getChatType() == null ? other.getChatType() == null : this.getChatType().equals(other.getChatType()))
                && (this.getMessageType() == null ? other.getMessageType() == null : this.getMessageType().equals(other.getMessageType()))
                && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSenderId() == null) ? 0 : getSenderId().hashCode());
        result = prime * result + ((getReceiverId() == null) ? 0 : getReceiverId().hashCode());
        result = prime * result + ((getReplyId() == null) ? 0 : getReplyId().hashCode());
        result = prime * result + ((getChatType() == null) ? 0 : getChatType().hashCode());
        result = prime * result + ((getMessageType() == null) ? 0 : getMessageType().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", senderId=").append(senderId);
        sb.append(", receiverId=").append(receiverId);
        sb.append(", replyId=").append(replyId);
        sb.append(", chatType=").append(chatType);
        sb.append(", messageType=").append(messageType);
        sb.append(", content=").append(content);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}