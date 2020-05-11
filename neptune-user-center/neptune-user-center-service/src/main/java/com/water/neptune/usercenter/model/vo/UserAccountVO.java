package com.water.neptune.usercenter.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : zhangmiaojie
 * @date : 2020/5/6 14:51
 */
@Data
public class UserAccountVO implements Serializable {
    private String token;
    private String userName;
    private String phone;
}
