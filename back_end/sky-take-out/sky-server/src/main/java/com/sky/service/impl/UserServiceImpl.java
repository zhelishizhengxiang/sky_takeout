package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @ProjectName: sky-take-out
 * @Package: com.sky.service.impl
 * @Description:
 * @Author: Simon
 * @CreateDate: 2026/1/6
 */
@Service
public class UserServiceImpl implements UserService {

    //获取openid的微信服务接口地址
    private static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return 用户信息
     * */
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //1.访问微信接口服务，获取openid
       String openid=getOpenId(userLoginDTO.getCode());

        //2.判断openid是否存在，如果为空则登陆失败，抛出业务异常
        if (openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //3.判断openid是否在数据库中，即是否是新用户
        User user = userMapper.getByOpenid(openid);
        //4.不在，则为新用户注册
        if (user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        //5.返回结果
        return user;
    }

    /**
     * 调用微信接口服务，获取openid
     * @param code 微信授权码
     * @return openid
     * */
    public String getOpenId(String code) {
        Map<String, String> paramMap=new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", code);
        paramMap.put("grant_type", "authorization_code");
        //访问该服务后返回的是json字符串，其中包含openid
        String entity = HttpClientUtil.doGet(WX_LOGIN, paramMap);

        //解析json字符串
        JSONObject jsonObject = JSONObject.parseObject(entity);
        return jsonObject.getString("openid");
    }
}
