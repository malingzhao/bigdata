package com.mlz.ceshi.domain;


/*
 * @创建人: MaLingZhao
 * @创建时间: 2019/10/11
 * @描述：
 */

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Orders {

    private String id;
    private String itemId;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
