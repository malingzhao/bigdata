package com.mlz.ceshi.dao;


/*
 * @创建人: MaLingZhao
 * @创建时间: 2019/10/11
 * @描述：
 */


import com.mlz.ceshi.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersDao extends JpaRepository<Orders,String> {


}
