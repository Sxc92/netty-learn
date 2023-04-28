package com.netty.learn.transports.java;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 史偕成
 * @date 2023/04/25 15:18
 **/
public class JavaNioServer {

    public void server(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket socket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        // 将服务器绑定到选取的端口
        socket.bind(address);
        // 打开 Selector 处理 channel
        Selector selector = Selector.open();
        // 将ServerStock注册到Selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        final ByteBuffer wrap = ByteBuffer.wrap("Hi\r\n".getBytes());

        for (; ; ) {
            try {
                // 等待需要的新事件；阻塞将一直持续到下一个事件传入
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        // 获取所有的Selection-key 实例
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            try {
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    // 接受客户端，并将它注册到选择器
                    client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, wrap.duplicate());
                    System.out.println("Accepted connection from" + client);
                }
                if (key.isWritable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    while (buffer.hasRemaining()) {
                        if (client.write(buffer) == 0) {
                            break;
                        }
                    }
                    client.close();
                }
            } catch (IOException e) {
                key.cancel();
                try {
                    key.channel().close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
}
