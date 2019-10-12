package com.mlz.ceshi.utils;


/*
 * @创建人: MaLingZhao
 * @创建时间: 2019/10/11
 * @描述：
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

/*
分布式锁
* */
@Configuration
@Slf4j
public class DistributedLock {

    //zk的客户端
    private CuratorFramework client = null;

    private static final String ZK_LOCK = "pk-zk-locks";
    private static final String DISTRIBTED_LOCK = "pk-distributed-locks";

    private static CountDownLatch countDownLatch=new CountDownLatch(1);


    public DistributedLock() {
        client = CuratorFrameworkFactory.builder().connectString("192.168.2.101:2181")
                .sessionTimeoutMs(1000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .namespace("zk-namespace")
                .build();

        client.start();
    }

    @Bean
    public  CuratorFramework getClient() throws Exception {
        client=client.usingNamespace("zk-namespace");

        try {
         if(client.checkExists().forPath("/"+ZK_LOCK)==null)
         {
             client.create()
                     .creatingParentsIfNeeded()
                     .withMode(CreateMode.PERSISTENT)
                     .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE);
         }
        } catch (Exception e) {
            e.printStackTrace();
            addWatch("/"+ZK_LOCK);
        }

        

        return  client;
    }

    private void addWatch(String path) throws Exception {


        PathChildrenCache cache=new PathChildrenCache(client,path,true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {

                if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                    String path=event.getData().getPath();
                    if(path.contains(DISTRIBTED_LOCK))
                    {
                       countDownLatch.countDown();
                    }
                }
            }
        });


    }

    /*
     * 获得分布式锁*/
    public void getLock() {


        while (true)
        {
            try {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/"+ZK_LOCK+"/"+DISTRIBTED_LOCK );
                log.info("分布式锁获得锁成功..................");
            } catch (Exception e) {



                e.printStackTrace();

                if(countDownLatch.getCount()<=0)
                {
                    countDownLatch=new CountDownLatch(1);

                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException el) {
                    el.printStackTrace();
                }
            };
        }
    }

    /*
    释放分布式锁，订单创建成功或者异常的时候释放锁
    * */
    public boolean releaseLock() {

        try {
            if( (client.checkExists().forPath("/"+ZK_LOCK+"/"+DISTRIBTED_LOCK))!=null){
                client.delete().forPath("/"+ZK_LOCK+"/"+DISTRIBTED_LOCK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        log.info("分布式锁释放成功");

        return true;
    }


}
