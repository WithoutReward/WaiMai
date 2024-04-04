package com.sky.service;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @return
     */
    List<ShoppingCart> list();

    /**
     * 从购物车中删除一件商品
     * @param shoppingCartDTO
     */
    void deleteOne(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     */
    void deleteAll();
}
