package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 批量插入套餐和菜品的关联关系数据
     * @param setmealDishes
     * @return
     * */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 批量删除套餐和菜品的关联关系
     * @param setmealIds
     */
    void deleteBySetmealIds(List<Long> setmealIds);

    /**
     * 根据套餐id查询套餐和菜品的关联关系
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);
}
