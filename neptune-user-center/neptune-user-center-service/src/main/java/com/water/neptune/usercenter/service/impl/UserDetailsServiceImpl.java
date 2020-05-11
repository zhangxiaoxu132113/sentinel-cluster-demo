package com.water.neptune.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.water.neptune.usercenter.core.extend.AccountDetail;
import com.water.neptune.usercenter.mapper.AccountMapper;
import com.water.neptune.usercenter.model.po.AccountPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author : zhangmiaojie
 * @date : 2020/5/6 16:36
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AccountPO account = accountMapper.selectOne(new LambdaQueryWrapper<AccountPO>().eq(AccountPO::getUserName, s));
        return new AccountDetail(account);
    }
}
