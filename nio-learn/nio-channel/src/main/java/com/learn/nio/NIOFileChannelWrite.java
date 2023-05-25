package com.learn.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 史偕成
 * @date 2023/05/19 20:44
 **/
public class NIOFileChannelWrite {

    public static void main(String[] args) throws IOException {
        String str = "hello 尚硅谷";
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/sxc/Documents/java/netty/netty-in-action/nio-learn/file/file.txt");

        // filechannel 真实类型： FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(str.getBytes());
        buffer.flip();
        fileChannel.write(buffer);
        fileOutputStream.close();
    }
}
