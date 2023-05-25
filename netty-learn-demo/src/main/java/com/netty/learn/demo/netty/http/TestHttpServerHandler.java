package com.netty.learn.demo.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * SimpleChannelInboundHandler 是 channelInboundHandlerAdapter
 * HttpObject 客户端和服务端相互通讯的数据被封装成HttpObject
 *
 * @author 史偕成
 * @date 2023/05/25 16:59
 **/
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端数据
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        System.out.println("当前ctx的handler" + ctx.handler());

        // 判断msg 是不是httprequest 请求
        if (msg instanceof HttpRequest) {

            String uriStr = ((HttpRequest) msg).uri();
            URI uri = new URI(uriStr);
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico 资源");
                return;
            }
            System.out.println("msg类型 = " + msg.getClass());
            System.out.println("客户端的地址" + ctx.channel().remoteAddress());

            // 回复信息给浏览器 【Http协议】
            ByteBuf content = Unpooled.copiedBuffer("hello,I'm server", StandardCharsets.UTF_8);

            // 构造一个http相应 即httpresponse
            // 参数1 ：Http协议版本
            // 参数2 返回状态码
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将构建好的response 返回
            ctx.writeAndFlush(response);
        }
    }
}
