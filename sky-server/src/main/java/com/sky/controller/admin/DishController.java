package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishFlavorMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("正在新增菜品：{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        //精确清理添加到的种类(其实可以省略，因为默认新增菜品都是停售状态)
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询：{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除菜品：{}",ids);
        dishService.deleteBatch(ids);
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 根据id查询菜品和口味
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品和口味")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("当前查询的菜品id为：{}",id);
        DishVO dish = dishService.getByIdWithFlavor(id);
        return Result.success(dish);
    }


    /**
     * 修改菜品信息
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("当前修改的数据为：{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 停售和起售菜品
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("停售和起售菜品")
    public Result startOrStopSell(@PathVariable String status,Long id){
        log.info("正在更改商品售卖状态：{},{}",status,id);
        dishService.startOrStopSell(status,id);
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 清理缓存
     * @param pattern
     */
    //TODO 可以改成AOP
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
