package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @version 1.0
 * @ProjectName: sky-take-out
 * @Package: com.sky.service
 * @Description:
 * @Author: Simon
 * @CreateDate: 2026/1/6
 */

public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     * */
    User wxLogin(UserLoginDTO userLoginDTO);
}
