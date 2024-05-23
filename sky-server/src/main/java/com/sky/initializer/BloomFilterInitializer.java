package com.sky.initializer;

import com.sky.entity.Dish;
import com.sky.service.DishService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

//@Component
public class BloomFilterInitializer implements CommandLineRunner {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void run(String... args) throws Exception {
        //创建菜品布隆过滤器
        RBloomFilter<Long> dishnameBloomFilter =redissonClient.getBloomFilter("BLOOM_DISH_DISHID");
        //触发addToBloomFilter方法
        List<Dish> dishList = dishService.listAll();
        if (dishList.size() > 0) {
            dishnameBloomFilter.tryInit(dishList.size(),0.05);//误判率控制在5%以内
            dishList.forEach(e -> dishnameBloomFilter.add(e.getId()));
        }
    }
}
