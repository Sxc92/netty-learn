package com.netty.learn.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author 史偕成
 * @date 2023/05/16 07:49
 **/
public class Client {

    public static void main(String[] args) throws IOException {
        SocketChannel open = SocketChannel.open();
        open.connect(new InetSocketAddress("localhost", 8188));

        System.out.println("waiting.....");
    }
}
