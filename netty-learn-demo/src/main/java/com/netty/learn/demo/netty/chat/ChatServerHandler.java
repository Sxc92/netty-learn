package com.netty.learn.demo.netty.chat;

import com.google.errorprone.annotations.Var;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 史偕成
 * @date 2023/05/25 21:31
 **/
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 定义一个channel组 管理所有的channel
     * GlobalEventExecutor.INSTANCE 全局事件执行器（单例）
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 日期格式化
     */
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 第一个调用
     * 表示链接建立，一但链接，第一个被执行
     * 将当前channel加入到channelGroup
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将客户端加入群聊的信息推送给其它在线客户
        channelGroup.writeAndFlush("时间：" + simpleDateFormat.format(new Date()) + ", " + "【client】:" + ctx.channel().remoteAddress() + " Join the group chat\n");
        channelGroup.add(channel);
    }

    /**
     * 表示channel 处于活动状态， 提示xxx上线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("时间：" + simpleDateFormat.format(new Date()) + ", " + ctx.channel().remoteAddress() + "Online");
    }

    /**
     * 表示channel 处于非活动状态， 提示xxx离线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Time：" + simpleDateFormat.format(new Date()) + ", " + ctx.channel().remoteAddress() + "Offline");
    }

    /**
     * 断开链接，触发handler
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将客户端离开群聊的信息推送给其它在线客户
        channelGroup.writeAndFlush("Time：" + simpleDateFormat.format(new Date()) + ", " + "【client】:" + ctx.channel().remoteAddress() + " Leave the group chat\n");
        channelGroup.remove(channel);
        System.out.println("channelGroup size: " + channelGroup.size());
    }

    /**
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取到当前channel
        Channel channel = ctx.channel();
        // 遍历channelGroup, 根据不同的情况， 回送不同的消息
        channelGroup.forEach(item -> {
            item.writeAndFlush("Time：" + simpleDateFormat.format(new Date()) + ", " + "【client】:" + channel.remoteAddress() + "send msg: " + msg + "\n");
        });
    }

    /**
     * 异常
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
