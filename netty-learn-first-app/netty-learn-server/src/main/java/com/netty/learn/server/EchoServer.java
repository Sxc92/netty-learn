package com.netty.learn.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 服务端启动类
 *
 * @author 史偕成
 * @date 2023/04/24 15:14
 **/
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
//        if (args.length != 1) {
//            System.out.println("Usage:" + EchoServer.class.getSimpleName() + " ");
//        }
//        int port = Integer.parseInt(args[0]);
        new EchoServer(8888).start();
    }

    /**
     * 流程
     * 1. NioEventLoopGroup 实例来接收和处理新的连接, 并且将Channel的类型指定位NioServer-SocketChannel
     * 2. 将本地地址设置位一个具有选定端口的InetSocket-Address 服务器将绑定到这个地址以监听新的连接请求
     * 3. ChannelInitializer 作用： 当一个新的连接被接受时 一个新的子Channel将会被创建，而ChannelInitializer 将会把一个你的handler的实例添加到Channel的ChannelPipeline中
     * 4. 绑定服务器，并等待完成（sync() 方法的调用将导致当前Thread 阻塞 直到绑定操作完成为止）
     * 5. 该应用程序将会阻塞等待直到服务器的Channel关闭（因为你在Channel的close Future上调用了sync() 方法） 并释放所有的资源 包括所有被创建的线程
     * @throws InterruptedException
     */
    public void start() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        // 创建event-loopGroup
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {

            ServerBootstrap b = new ServerBootstrap();
            // 指定所使用的NIO传输Channel
            b.group(eventExecutors)
                    .channel(NioServerSocketChannel.class)
                    // 指定的端口设置套字节地址
                    .localAddress(new InetSocketAddress(port))
                    // 添加一个EchoServer-Handler 到子Channel 的channelPipeline
                    .childHandler(new ChannelInitializer() {
                        //
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(serverHandler);
                        }
                    });
            // 异步绑定服务器，调用sync() 方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            // 获取Channel的CloseFuture 并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup 释放所有的资源
            eventExecutors.shutdownGracefully().sync();
        }
    }
}
