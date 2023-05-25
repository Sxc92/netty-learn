package com.netty.learn.demo.nio;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * @author 史偕成
 * @date 2023/05/16 07:24
 **/
public class Server {
    public static void main(String[] args) throws IOException {
        // 1.利用nio 来理解阻塞模式
        ByteBuffer buffer = ByteBuffer.allocateDirect(16);
        // 1。创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();

        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8188));

        // 建立一个连接集合
        List<SocketChannel> channels = Lists.newArrayList();
        // 为了建立多次连接
        while (true) {
            // 3. accept 建立客户端连接, SocketChannel 用来与客户端之间通信

            SocketChannel accept = ssc.accept();
            channels.add(accept);

            // 遍历channel 接收客户端发送数据
            for (SocketChannel channel : channels) {
                int read = channel.read(buffer);
                buffer.flip();
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    System.out.println((char) b);
                }

                // 切换成写模式
                buffer.clear();
            }
        }

    }
}
