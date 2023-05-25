package com.netty.learn.demo.netty.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author 史偕成
 * @date 2023/05/23 17:37
 **/
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        // 创建BossGroup WorkerGroup
        // 创建两个线程组 ： bossGroup、workerGroup
        // 2. bossGroup: 只是处理连接请求， workerGroup： 处理客户端的业务
        // 3. 两个都是无限循环
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建服务器端的启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置两个线程组
            serverBootstrap.group(bossGroup, workerGroup)
                    // 设置通道类型实现 NioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列得到的连接数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置 保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 给我们的workerGroup的EventLoop对应的管道设置处理器
                    .childHandler(
                            // 创建一个通道测试对象
                            new ChannelInitializer<SocketChannel>() {
                                /**
                                 * 向pipeline设置处理器
                                 * @param socketChannel
                                 * @throws Exception
                                 */
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    socketChannel.pipeline().addLast(new NettyServerHandler());
                                }
                            });
            System.out.println("server is ready.....");
            // 绑定端口，并且同步处理
            // 启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();
            // 注册监听器监听 自定义事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("监听端口成功");
                    } else {
                        System.out.println("监听端口失败");
                    }
                }
            });
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
