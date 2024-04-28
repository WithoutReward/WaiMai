package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类
 */

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    //每分钟触发一次
    @Scheduled(cron = "0 * * * * ? ")
    public void processTimeoutOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());

        //处理
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-15);

        List<Orders> list = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, localDateTime);

        if (list != null && list.size() > 0) {
            for (Orders order : list) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时，自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }


    /**
     * 处理一直派送中的订单
     */
    //每天凌晨一点触发一次
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理处于派送中的订单:{}",LocalDateTime.now());

        LocalDateTime localDateTime = LocalDateTime.now().plusHours(-1);

        List<Orders> list = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS,localDateTime);

        if (list != null && list.size() > 0) {
            for (Orders order : list) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }

    }
}
