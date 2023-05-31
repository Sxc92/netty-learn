package com.netty.learn.demo.netty.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author 史偕成
 * @date 2023/05/26 15:53
 **/
public class SocketServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 添加Http解码器、编码器
                            pipeline.addLast(new HttpServerCodec());
                            // 添加Chunked 支持异步发送大的码流， 但不占用过多的内存
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 参数的最大长度, 支持分段将多个消息转换成单一的FullHttpRequest或者是FullHttpResponse
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /**
                             * 1. 对应webSocket 它的数据是以帧（Frame）的形式传递
                             * 2. 可以看到WebSocketFrame 下面六个子类
                             * 3. 请求地址 ws://localhost:7001/hello
                             * 4.WebSocketServerProtocolHandler 将http升级为ws 协议， 保持长连接
                             * 5. 是通过一个状态码 101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello2"));

                            //
                            pipeline.addLast(new CustomWebSocketFrameHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(7001).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
