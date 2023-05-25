package com.learn.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 史偕成
 * @date 2023/05/19 20:44
 **/
public class NIOFileChannelRead {

    public static void main(String[] args) throws IOException {

        File file = new File("/Users/sxc/Documents/java/netty/netty-in-action/nio-learn/file/file.txt");
//        String str = "hello 尚硅谷";
        FileInputStream fileInputStream = new FileInputStream(file);

        // fileChannel 真实类型： FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        // 创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());

        fileChannel.read(buffer);

        // 将缓冲区的字节转成字符串
        System.out.println(new String(buffer.array()));

        fileInputStream.close();
    }
}
