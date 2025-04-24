package com.Kun.KunChat.controller;

import com.Kun.KunChat.StaticVariable.Status;
import com.Kun.KunChat.common.BaseController;
import com.Kun.KunChat.common.BusinessException;
import com.Kun.KunChat.common.ResponseGlobal;
import com.Kun.KunChat.entity.GroupInfo;
import com.Kun.KunChat.service.GroupInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/serach")
    public ResponseGlobal<Object> serach(@RequestParam(required = false) String groupId,
                                         @RequestParam(required = false) String groupName,
                                         @RequestParam(required = false, defaultValue = "1") Integer page) {
        try {
            if (!groupId.isEmpty()) {
                GroupInfo groupInfo = groupInfoService.getGroupInfo(groupId);
                if (groupInfo == null) {
                    throw new BusinessException(Status.ERROR_SERACH);
                }
                return getSuccessResponse(groupInfo);
            } else {
                return getSuccessResponse(groupInfoService.getGroupInfo(groupName, page));
            }
        } finally {
        }
    }

}
