package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @ProjectName: sky-take-out
 * @Package: com.sky.mapper
 * @Description:
 * @Author: Simon
 * @CreateDate: 2025/12/30
 */
@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询对映体得套餐id
     * @param dishIds
     * @return 套餐id集合
     * */
    List<SetmealDish> getByDishIds(List<Long> dishIds);
}
