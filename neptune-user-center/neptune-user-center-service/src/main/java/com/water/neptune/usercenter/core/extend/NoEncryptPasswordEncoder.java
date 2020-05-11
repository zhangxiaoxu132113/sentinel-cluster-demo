package com.water.neptune.usercenter.core.extend;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author: zhangmiaojie
 * @Date: 2020/4/30 15:44
 */
public class NoEncryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return (String) charSequence;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals((String) charSequence);
    }
}
