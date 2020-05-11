package com.water.neptune.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.water.neptune.usercenter.mapper.AccountMapper;
import com.water.neptune.usercenter.model.po.AccountPO;
import com.water.neptune.usercenter.model.vo.UserAccountVO;
import com.water.neptune.usercenter.service.BizUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : zhangmiaojie
 * @date : 2020/5/6 14:59
 */
@Service
public class BizUserServiceImpl implements BizUserService {

    @Resource
    private TokenStore tokenStore;
    @Resource
    private DefaultTokenServices tokenServices;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public boolean logout(String accessToken) {
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
        if (oAuth2AccessToken == null) {
            return false;
        }
        return tokenServices.revokeToken(accessToken);
    }

    @Override
    public UserAccountVO getUserByPhone(String phone) {
        AccountPO account = accountMapper.selectOne(new LambdaQueryWrapper<AccountPO>().eq(AccountPO::getPhone, phone));
        if (account == null) {
            return null;
        }
        UserAccountVO userAccountVO = new UserAccountVO();
        BeanUtils.copyProperties(account, userAccountVO);
        return userAccountVO;
    }

    @Override
    public void bindByPhone(Long uid, String phone) {

    }
}
