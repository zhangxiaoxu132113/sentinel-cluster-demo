package com.water.neptune.usercenter.web.controller;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: zhangmiaojie
 * @date: 2020/5/6 14:42
 */
public class BaseController {

    /**
     * 获取授权令牌
     *
     * @param request HttpServletRequest
     * @return access_token
     */
    protected String getAccessToken(HttpServletRequest request) {
        return request.getParameter("token");
    }
}
