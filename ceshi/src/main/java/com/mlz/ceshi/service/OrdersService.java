package com.mlz.ceshi.service;


/*
 * @创建人: MaLingZhao
 * @创建时间: 2019/10/11
 * @描述：
 */


import com.mlz.ceshi.dao.OrdersDao;
import com.mlz.ceshi.domain.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OrdersService {
    @Autowired
    private OrdersDao ordersDao;

    public boolean save(String itemId)
    {
        try{
            Orders orders=new Orders();

            orders.setId(UUID.randomUUID().toString());
            ordersDao.save(orders);
            log.info("订单创建成功");
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
