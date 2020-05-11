package com.water.neptune.usercenter.service;

import com.water.neptune.usercenter.model.vo.UserAccountVO;

/**
 * @author : zhangmiaojie
 * @date : 2020/5/6 14:47
 */
public interface BizUserService {

    /**
     * 退出登录
     *
     * @param accessToken 授权令牌
     * @return 是否登录成功
     */
    boolean logout(String accessToken);

    /**
     * 根据手机号码获取用户详情
     *
     * @param phone 手机号码
     * @return AccountVO
     */
    UserAccountVO getUserByPhone(String phone);

    /**
     * 绑定手机号码
     *
     * @param uid
     * @param phone
     */
    void bindByPhone(Long uid, String phone);
}
