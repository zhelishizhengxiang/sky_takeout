package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version 1.0
 * @ProjectName: sky-take-out
 * @Package: com.sky.service.impl
 * @Description:
 * @Author: Simon
 * @CreateDate: 2025/12/29
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        //1.向菜品表插入一条菜品
        Dish dish = new Dish();
        //进行属性拷贝
        BeanUtils.copyProperties(dishDTO,dish);
        //公共字段已经使用切面填充
        dishMapper.insert(dish);

        //2.向口味表插入若干条数据
        //获取菜品的id
        Long dishId = dish.getId();

        //口味可选先判空
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0){
            //遍历对dishId赋值
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            dishFlavorMapper.insertBatch(flavors);
        }
    }


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     * */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //根据PageHelper来接话分页查询sql
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        //使用DishVO来接受查询结果
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Transactional
    public void deleteBatch(List<Long> ids) {
        //1.起售中和与套餐关联的菜品都不能进行删除
        for (long i :ids) {
            Dish dish=dishMapper.getById(i);
            if (dish.getStatus() == StatusConstant.ENABLE){
                log.error("起售中的菜品不能删除");
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        if (setmealDishMapper.getByDishIds(ids)!=null && setmealDishMapper.getByDishIds(ids).size()>0){
            log.error("与套餐关联的菜品不能删除");
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品数据
        dishMapper.deleteBatch(ids);

        //3.该菜品关联的口味也需要相继删除
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 根据id查询菜品和对应的口味
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id) {
        //1.先查询菜品表信息
        Dish dish=dishMapper.getById(id);
        //2.查询关联的口味
        List<DishFlavor> flavors=dishFlavorMapper.getByDishId(id);

        //封装数据
        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        //集合不能试用BeanUtils
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * 修改菜品(包含口味)
     * @param dishDTO
     * @return
     * */
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        //1.修改菜品表
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //2.修改关联口味：可能有增删改得情况。可以换种思路将之前得删掉，重新插入
        dishFlavorMapper.deleteByDishIds(Arrays.asList(dishDTO.getId()));
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0){
            //遍历对dishId赋值
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishDTO.getId());
            }
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        //List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}


