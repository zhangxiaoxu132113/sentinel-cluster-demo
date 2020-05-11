package com.water.neptune.usercenter.web.controller;

import com.water.neptune.usercenter.model.vo.UserAccountVO;
import com.water.neptune.usercenter.service.BizUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zhangmiaojie
 * @Date: 2020/4/29 17:35
 */
@RequestMapping(value = "/user")
@RestController
public class UserController extends BaseController {

    @Autowired
    private BizUserService bizUserService;
    /**
     * 获取用户详情
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public String getById(@PathVariable Long userId) {
        return "neptune-user-center-" + userId;
    }

    /**
     * 根据手机号码获取用户详情
     *
     * @return
     */
    @GetMapping(value = "/getByPhone")
    public UserAccountVO getByPhone(@RequestParam("phone") String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new RuntimeException("手机号码不能为空!");
        }
        return bizUserService.getUserByPhone(phone);
    }

    /**
     * 使用手机号码注册
     *
     * @param phone 用户手机号码
     */
    @PostMapping("/registerByPhone")
    public void registerByPhone(String phone, String pwd) {

    }

    /**
     * 绑定手机号码
     *
     * @param uid
     * @param phone
     */
    @PostMapping("/bindByPhone")
    public void bindByPhone(@RequestParam("uid") Long uid,
                            @RequestParam("phone") String phone) {
        bizUserService.bindByPhone(uid, phone);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        bizUserService.logout(super.getAccessToken(request));
    }


}
