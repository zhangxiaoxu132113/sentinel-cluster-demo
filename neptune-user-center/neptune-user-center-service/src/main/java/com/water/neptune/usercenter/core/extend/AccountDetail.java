package com.water.neptune.usercenter.core.extend;

import com.water.neptune.usercenter.model.po.AccountPO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

/**
 * @Author: zhangmiaojie
 * @Date: 2020/4/30 17:00
 */
public class AccountDetail implements UserDetails {
    protected static final String ROLE_PREFIX = "ROLE_";
    protected static final GrantedAuthority DEFAULT_ACCOUNT_ROLE = new SimpleGrantedAuthority(ROLE_PREFIX + "account");

    protected AccountPO account;

    public AccountDetail(AccountPO account){
        this.account=account;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Arrays.asList(DEFAULT_ACCOUNT_ROLE, new SimpleGrantedAuthority(ROLE_PREFIX + "UNITY"), new SimpleGrantedAuthority(ROLE_PREFIX + "MOBILE"));
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public AccountPO getAccount() {
        return account;
    }

    public void setAccount(AccountPO account) {
        this.account = account;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{account=").append(account);
        sb.append('}');
        return sb.toString();
    }
}
