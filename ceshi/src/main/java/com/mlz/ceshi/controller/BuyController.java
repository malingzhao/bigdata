package com.mlz.ceshi.controller;


/*
 * @创建人: MaLingZhao
 * @创建时间: 2019/10/11
 * @描述：
 */


import com.mlz.ceshi.service.PayService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class BuyController {

    @Autowired
    private PayService payService;

    @GetMapping("/buy1")
    @ResponseBody
    public String buy1(String itemId) {
        if (StringUtils.isNoneBlank(itemId)) {
            if (payService.buy1(itemId)) {
                return "订单创建成功";
            } else {
                return "订单创建失败";
            }

        } else {
            return "条目ID不能为空";
        }


    }

    @GetMapping("/buy2")
    @ResponseBody
    public String buy2(String itemId) {
        if (StringUtils.isNoneBlank(itemId)) {
            if (payService.buy2(itemId)) {
                return "订单创建成功";
            } else {
                return "订单创建失败";
            }

        } else {
            return "条目ID不能为空";
        }


    }

}
