package com.Kun.KunChat.controller;

import com.Kun.KunChat.StaticVariable.Status;
import com.Kun.KunChat.annotation.GlobalInterceptor;
import com.Kun.KunChat.common.BaseController;
import com.Kun.KunChat.common.BusinessException;
import com.Kun.KunChat.common.ResponseGlobal;
import com.Kun.KunChat.entity.GroupInfo;
import com.Kun.KunChat.service.GroupInfoService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Author: Beta
 * Date: 2025/4/24 15:57
 * Description:
 **/

@RestController("groupController")
@RequestMapping("/group")
@Validated
public class GroupController extends BaseController {

    @Autowired
    private GroupInfoService groupInfoService;

    @PostMapping("/create")
    @GlobalInterceptor
    public ResponseGlobal<Object> groupCreate(@RequestParam @NotEmpty String groupName,
                                              @RequestParam @NotNull Integer joinType) {
        try {
            String[] result = (String[]) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
            assert result != null;
            // 创建群组
            return getSuccessResponse(groupInfoService.createGroup(result[1], groupName, joinType));
        } finally {
        }

    }

    @PostMapping("/update")
    @GlobalInterceptor
    public ResponseGlobal<Object> groupUpdate(@RequestBody @Validated(GroupInfo.UpdateGroup.class) GroupInfo groupInfo) {
        try {
            String[] result = (String[]) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
            assert result != null;
            GroupInfo groupDataBase = groupInfoService.getGroup(groupInfo.getGroupId());
            if (!groupDataBase.getOwnerId().equalsIgnoreCase(result[1])) {
                throw new BusinessException(Status.ERROR_ACTION);
            }
            if (groupInfoService.getGroup(groupInfo.getGroupId()).getStatus() == 0) {
                throw new BusinessException(Status.ERROR_GROUPDEL);
            }
            return getSuccessResponse(groupInfoService.updateGroup(groupInfo));
        } finally {
        }
    }

    @RequestMapping("/delete")
    @GlobalInterceptor
    public ResponseGlobal<Object> deleteGroup(@RequestParam @NotEmpty String groupId) {
        try {
            String[] result = (String[]) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
            assert result != null;
            GroupInfo groupDataBase = groupInfoService.getGroup(groupId);
            if (!groupDataBase.getOwnerId().equalsIgnoreCase(result[1])) {
                throw new BusinessException(Status.ERROR_ACTION);
            }
            return getSuccessResponse(groupInfoService.deleteGroup(groupId));
        } finally {
        }
    }


    @RequestMapping("/search")
    public ResponseGlobal<Object> serach(@RequestParam(required = false) String groupId,
                                         @RequestParam(required = false) String groupName,
                                         @RequestParam(required = false, defaultValue = "1") Integer page) {
        try {
            if (!groupId.isEmpty()) {
                GroupInfo groupInfo = groupInfoService.getGroup(groupId);
                if (groupInfo == null) {
                    throw new BusinessException(Status.ERROR_SERACH);
                }
                return getSuccessResponse(groupInfo);
            } else {
                return getSuccessResponse(groupInfoService.getGroup(groupName, page));
            }
        } finally {
        }
    }

    @RequestMapping("/test")
    @GlobalInterceptor
    public ResponseGlobal<Object> tttset() {
        return getSuccessResponse();
    }

}
