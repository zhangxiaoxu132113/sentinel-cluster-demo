package com.water.neptune.usercenter.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zhangmiaojie
 * @Date: 2020/4/30 16:48
 */
@Data
@TableName("t_account")
public class AccountPO implements Serializable {
    @TableId
    private Long id;
    private String userName;
    private String phone;
    private String password;
}
