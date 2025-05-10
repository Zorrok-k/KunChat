package com.Kun.KunChat.controller;

import com.Kun.KunChat.annotation.GlobalInterceptor;
import com.Kun.KunChat.common.BaseController;
import com.Kun.KunChat.common.BusinessException;
import com.Kun.KunChat.common.ResponseGlobal;
import com.Kun.KunChat.entity.GroupInfo;
import com.Kun.KunChat.entity.UserContactMessage;
import com.Kun.KunChat.entity.UserInfo;
import com.Kun.KunChat.service.GroupInfoService;
import com.Kun.KunChat.service.UserContactInfoService;
import com.Kun.KunChat.service.UserContactMessageService;
import com.Kun.KunChat.service.UserInfoService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static com.Kun.KunChat.StaticVariable.Status.*;

/**
 * Author: Beta
 * Date: 2025/4/26 10:33
 * Description:
 **/

@RestController("contactController")
@RequestMapping("/contact")
@Validated
public class ContactController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserContactInfoService userContactInfoService;

    @Autowired
    private UserContactMessageService userContactMessageService;

    @Autowired
    private GroupInfoService groupInfoService;

    @PostMapping("/build")
    @GlobalInterceptor
    @Transactional
    public ResponseGlobal<Object> buildContacts(@RequestBody @Validated(UserContactMessage.UpdateGroup.class) UserContactMessage userContactMessage) {
        try {
            // 解密token获取登录信息
            String[] result = (String[]) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
            assert result != null;
            String userId = result[1];
            // 防止用户更改，以token的信息为准
            userContactMessage.setApplicantId(userId);
            // 如果用户或群组不需要同意就能添加
            if (userContactMessage.getType() == 0) {
                UserInfo userInfo = userInfoService.getUser(userContactMessage.getContactId());
                if (userInfo.getJoinType() == 0) {
                    // 如果曾经不是好友 则要建立关系记录
                    if (userContactInfoService.getUserContactInfo(userId, userInfo.getUserId()) == null) {
                        userContactInfoService.buildContact(userId, userInfo.getUserId(), 0, 1);
                    } else {
                        // 曾经是好友 则更新关系记录
                        userContactInfoService.updateStatus(userId, userInfo.getUserId(), 1);
                        userContactInfoService.updateStatus(userInfo.getUserId(), userId, 1);
                    }
                    return getSuccessResponse();
                }
            } else {
                GroupInfo groupInfo = groupInfoService.getGroup(userContactMessage.getContactId());
                if (groupInfo.getJoinType() == 0) {
                    if (userContactInfoService.getUserContactInfo(userId, groupInfo.getGroupId()) == null) {
                        userContactInfoService.buildContact(userId, groupInfo.getGroupId(), 1, 1);
                    } else {
                        userContactInfoService.updateStatus(userId, groupInfo.getGroupId(), 1);
                        userContactInfoService.updateStatus(groupInfo.getGroupId(), userId, 1);
                    }
                }
            }
            // 获取当前关系请求消息是否存在
            UserContactMessage message = userContactMessageService.getMessage(userId, userContactMessage.getContactId());
            // 不存在则创建请求消息
            if (message == null) {
                // 请求类型是好友
                if (userContactMessage.getType() == 0) {
                    // 设置处理对象id为请求对象id
                    userContactMessage.setAcceptorId(userContactMessage.getContactId());
                } else {
                    // 请求类型是群组 设置处理对象为群组id查出来的群组信息中的群主id
                    GroupInfo groupInfo = groupInfoService.getGroup(userContactMessage.getContactId());
                    userContactMessage.setAcceptorId(groupInfo.getOwnerId());
                }
                // 设置该请求的请求时间
                userContactMessage.setLastApplyTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
                // // 在关系表中添加一条以当前请求用户为视角的关系记录，默认status为待处理
                // userContactInfoService.buildContact(userId, userContactMessage.getContactId(), userContactMessage.getType());
                // 返回这条请求消息
                return getSuccessResponse(userContactMessageService.addMessage(userContactMessage));
            } else {
                // 消息存存在 则查看状态码
                // 被拉黑抛出异常，不做任何处理
                if (message.getStatus() == 3) {
                    throw new BusinessException(ERROR_BLACK);
                }
                // 如果已经同意该请求，抛重复请求关系异常
                if (message.getStatus() == 1) {
                    throw new BusinessException(ERROR_CONTACTREPEAT);
                }
                // 走到这只能是被拒绝或尚未处理
                message.setStatus(0);
                // 更新请求时间
                message.setLastApplyTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
                // 返回该条请求消息
                return getSuccessResponse(userContactMessageService.updateMessage(message));
            }
        } finally {
        }
    }

    @RequestMapping("/get")
    @GlobalInterceptor
    public ResponseGlobal<Object> getContacts(@NotNull @PositiveOrZero Integer type) {
        try {
            String[] result = (String[]) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
            assert result != null;
            String userId = result[1];
            // 获取id好友id列表
            List<String> friendIdList = userContactInfoService.getUserContactInfo(userId, type);
            // 创建一个空的朋友信息列表  这么做是为了吃上缓存😎
            // List friendInfoList = null;
            if (type == 0) {
                List<UserInfo> contactInfoList = new ArrayList<>();
                for (String s : friendIdList) {
                    if (userInfoService.getUser(s).getStatus() != 2 && userInfoService.getUser(s).getStatus() == 3) {
                        contactInfoList.add(userInfoService.getUser(s));
                    }
                }
                return getSuccessResponse(contactInfoList);
            } else {
                List<GroupInfo> contactInfoList = new ArrayList<>();
                for (String s : friendIdList) {
                    if (groupInfoService.getGroup(s).getStatus() != 4 && groupInfoService.getGroup(s).getStatus() == 5) {
                        contactInfoList.add(groupInfoService.getGroup(s));
                    }
                }
                return getSuccessResponse(contactInfoList);
            }
        } finally {
        }
    }

    // 消息接收方对消息进行处理 相应关系表的status做出更新
    @PostMapping("/approval")
    @GlobalInterceptor
    public ResponseGlobal<Object> uapprovalContacts(@NotNull @PositiveOrZero Integer messageId, @NotNull @PositiveOrZero Integer status) {
        try {
            String[] result = (String[]) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
            assert result != null;
            String userId = result[1];
            // 先查询这个消息id的内容
            UserContactMessage message = userContactMessageService.getMessage(messageId);
            // 如果这个处理id不是目前登录的id 就抛没有权限的异常
            if (!message.getAcceptorId().equalsIgnoreCase(userId)) {
                throw new BusinessException(ERROR_ACTION);
            }
            // 如果同意请求
            if (status == 1) {
                // 如果是好友请求
                if (message.getType() == 0) {
                    if (userContactInfoService.getUserContactInfo(userId, message.getApplicantId()) == null) {
                        // 建立关系记录 一式两份
                        userContactInfoService.buildContact(userId, message.getApplicantId(), 0, 1);
                    } else {
                        userContactInfoService.updateStatus(userId, message.getApplicantId(), 1);
                        userContactInfoService.updateStatus(message.getApplicantId(), userId, 1);
                    }

                } else {
                    if (userContactInfoService.getUserContactInfo(message.getContactId(), message.getApplicantId()) == null) {
                        // 建立关系记录 一式两份
                        userContactInfoService.buildContact(message.getContactId(), message.getApplicantId(), 1, 1);
                    } else {
                        userContactInfoService.updateStatus(message.getContactId(), message.getApplicantId(), 1);
                        userContactInfoService.updateStatus(message.getApplicantId(), message.getContactId(), 1);
                    }
                }
            }
            // 更新这条请求消息
            message = new UserContactMessage();
            message.setId(messageId);
            message.setStatus(status);
            // 返回这条请求消息的最新内容
            return getSuccessResponse(userContactMessageService.updateMessage(message));
        } finally {
        }
    }

    @PostMapping("/update")
    @GlobalInterceptor
    public ResponseGlobal<Object> updateContacts(@NotEmpty String contactId, @NotNull @PositiveOrZero Integer status) {
        try {
            String[] result = (String[]) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
            assert result != null;
            String userId = result[1];
            if (status == 2) {
                // 删除和被删除
                userContactInfoService.updateStatus(userId, contactId, 2);
                userContactInfoService.updateStatus(contactId, userId, 4);
            } else if (status == 3) {
                // 拉黑和被拉黑
                userContactInfoService.updateStatus(userId, contactId, 3);
                userContactInfoService.updateStatus(contactId, userId, 5);
            }
            return getSuccessResponse();
        } finally {
        }
    }

}
