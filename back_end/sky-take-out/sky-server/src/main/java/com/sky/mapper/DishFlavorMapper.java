package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @ProjectName: sky-take-out
 * @Package: com.sky.mapper
 * @Description:
 * @Author: Simon
 * @CreateDate: 2025/12/29
 */
@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     * @param flavors
     * */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id来删除口味数据
     * @param dishIds
     * */
    void deleteByDishIds(List<Long> dishIds);

    /**
     * 根据菜品id查询口味数据
     * @param id
     * @return 菜品口味数据
     * */
    List<DishFlavor> getByDishId(Long id);
}
