package com.netty.learn.demo.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 说明： 自定义一个handler 需要继承netty规定好的某个HandlerAdapter
 * 这时我们自定义一个handler 才能称为Handler
 *
 * @author 史偕成
 * @date 2023/05/23 17:50
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据
     *
     * @param ctx 上下文对象，含有pipeline（管道）、channel（通道）
     * @param msg 客户端发送的数据， 默认是对象 Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        /**
         * 用户自定义普通任务
         */
        ctx.channel().eventLoop().execute(new Runnable() {

            @Override
            public void run() {

            }
        });
        // 打印服务端线程
//        System.out.println("server thread Info: " + Thread.currentThread().getName());
        System.out.println("server ctx = " + ctx);
        // 将msg转成一个ByteBuffer
        ByteBuf buffer = (ByteBuf) msg;
        System.out.println("客户端发送消息是： " + buffer.toString(StandardCharsets.UTF_8));
        System.out.println("客户端地址： " + ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕 返回消息
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入缓冲，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, Client", StandardCharsets.UTF_8));
    }

    /**
     * 发送异常需要关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
