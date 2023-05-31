package com.netty.learn.demo.netty.heart;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author 史偕成
 * @date 2023/05/25 22:26
 **/
public class HeartServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 将event向下转型
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE -> eventType = "读空闲";
                case WRITER_IDLE -> eventType = "写空闲";
                case ALL_IDLE -> eventType = "读写空闲";
                default -> throw new IllegalStateException("Unexpected : ");
            }
            System.out.println(ctx.channel().remoteAddress() + "---超时---" + eventType);

            System.out.println("server do something....");
            // 如果发生空闲，关闭通道
//            ctx.channel().close();
        }
    }
}
