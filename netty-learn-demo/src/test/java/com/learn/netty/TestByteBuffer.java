package com.learn.netty;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 史偕成
 * @date 2023/05/16 07:08
 **/
public class TestByteBuffer {

    public static void main(String[] args) {

        try {
            FileChannel channel = new FileInputStream("/Users/sxc/Documents/java/netty/netty-in-action/netty-learn-demo/data.txt").getChannel();
            //准备缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                int len = channel.read(buffer);
                if (len == -1) break;
                // 打印buffer
                // 切换到读模式
                buffer.flip();
// buffer.hasRemaining() 检查是否还有剩余的未读数据
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    System.out.println((char) b);
                }

                // 切换成写模式
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
