package com.netty.learn.demo.netty.tcp;

import io.netty.util.NettyRuntime;

/**
 * @author 史偕成
 * @date 2023/05/24 20:04
 **/
public class NettyFunctionMain {

    public static void main(String[] args) {
        // 获取当前服务器的CUP核数
        System.out.println(NettyRuntime.availableProcessors());
    }
}
