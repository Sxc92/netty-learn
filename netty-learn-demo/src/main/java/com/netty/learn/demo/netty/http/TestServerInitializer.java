package com.netty.learn.demo.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author 史偕成
 * @date 2023/05/25 16:59
 **/
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 1.向管道添加处理器
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 2. 加入netty 提供的httpSevercodec
        // netty提供处理Http的编码解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());

        // 增加一个自己的Http编码Handler
        pipeline.addLast("MyHttpServerHandler", new TestHttpServerHandler());

        System.out.println("ok~~~");
    }
}
