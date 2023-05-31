package com.netty.learn.demo.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author 史偕成
 * @date 2023/05/25 21:22
 **/
public class ChatServer {
    /**
     * 监听端口
     */
    private int port;

    public ChatServer(int port) {
        this.port = port;
    }

    /**
     * 编写run方法， 处理客户端请求
     */
    public void run() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //
                            ch.pipeline()
                                    // 加入一个解码器
                                    .addLast("decoder", new StringDecoder())
                                    // 加入一个编码器
                                    .addLast("encoder", new StringEncoder())
                                    // 自定义业务handler
                                    .addLast("customer", new ChatServerHandler());
                        }
                    });
            System.out.println("chat server is start successfully");
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ChatServer(17000).run();
    }
}
