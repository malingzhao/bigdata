package com.mlz.ceshi.service;


/*
 * @创建人: MaLingZhao
 * @创建时间: 2019/10/11
 * @描述：
 */

import com.mlz.ceshi.dao.ItemsDao;
import com.mlz.ceshi.domain.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/*service层需要加
* */
@Service
public class ItemService {

    @Autowired
    private ItemsDao itemsDao;
    /*
    * 开发的方法
    * */

    public Items getItem(String itemId){
        return  itemsDao.findOne(itemId);
    }



    public void save(Items items)
    {
        items.setId(UUID.randomUUID().toString());
        itemsDao.save(items);
    }

    /*
    * 根据itemId获取库存量
    * */

    public  int getItemCounts(String itemId)
    {
        return  itemsDao.findOne(itemId).getCounts() ;

    }

    public  void reduceCount(String itemId,int count)
    {
        Items items = getItem(itemId);
        items.setCounts(items.getCounts()-count);
        itemsDao.save(items);

    }
}
