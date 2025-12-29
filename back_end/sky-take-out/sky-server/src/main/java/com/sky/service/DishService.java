package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * @version 1.0
 * @ProjectName: sky-take-out
 * @Package: com.sky.service
 * @Description:
 * @Author: Simon
 * @CreateDate: 2025/12/29
 */

public interface DishService {

    void saveWithFlavor(DishDTO dishDTO);
}
