package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理
 */

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result add(@RequestBody SetmealDTO setmealDTO){
        log.info("正在新增套餐：{}",setmealDTO);
        setMealService.add(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询：{}",setmealPageQueryDTO);
        PageResult pageResult = setMealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除套餐：{}",ids);
        setMealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询套餐和套餐所含菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐和套餐所含菜品")
    public Result<SetmealVO> getById(@PathVariable Long id){
        log.info("当前查询的套餐id为：{}",id);
        SetmealVO setmeal =setMealService.getByIdWithDish(id);
        return Result.success(setmeal);
    }


    /**
     * 修改套餐信息
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改套餐信息")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("当前修改的套餐为：{}",setmealDTO);
        setMealService.updateWithDishes(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐的起售与停售
     *
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐的起售与停售")
    public Result startOrStopSell(@PathVariable String status,Long id) {
        log.info("正在起售/停售套餐：{},{}",id,status);
        setMealService.startOrStopSell(status,id);
        return Result.success();
    }
}
