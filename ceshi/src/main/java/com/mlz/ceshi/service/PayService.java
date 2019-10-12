package com.mlz.ceshi.service;


/*
 * @创建人: MaLingZhao
 * @创建时间: 2019/10/11
 * @描述：
 */


import com.mlz.ceshi.utils.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private DistributedLock distributedLock;

    public boolean buy1(String itemId) {
        //每次购买9个
        //step1 判断库存
        int count = itemService.getItemCounts(itemId);
        int buyCount = 9;
        if (count < buyCount) {


            log.error("库存不足 ,下单失败。购买数{}件,库存只有{}件", buyCount, count);
            return false;
        }
        //step2 订单
        boolean flag = ordersService.save(itemId);
        //TODO ...  模拟高并发场景


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //step3 扣库存

        if (flag) {
            itemService.reduceCount(itemId, buyCount);

        } else {
            log.error("订单创建失败");
            return false;
        }
        return true;


    }

    public boolean buy2(String itemId) {
        distributedLock.getLock();
        //每次购买9个
        //step1 判断库存
        int count = itemService.getItemCounts(itemId);
        int buyCount = 9;
        if (count < buyCount) {
            log.error("库存不足 ,下单失败。购买数{}件,库存只有{}件", buyCount, count);
            distributedLock.releaseLock();
            return false;
        }
        //step2 订单
        boolean flag = ordersService.save(itemId);
        //TODO ...  模拟高并发场景


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            distributedLock.releaseLock();
            e.printStackTrace();
        }

        //step3 扣库存

        if (flag) {
            itemService.reduceCount(itemId, buyCount);

        } else {
            log.error("订单创建失败");
            distributedLock.releaseLock();
            return false;
        }
        return true;


    }

}
