package com.learn.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 利用FileChannel 复制数据
 *
 * @author 史偕成
 * @date 2023/05/20 15:04
 **/
public class NIOFileChannelCopyFile {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream =
                new FileInputStream("/Users/sxc/Documents/java/netty/netty-in-action/nio-learn/file/file2.txt");
        FileChannel inputChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream =
                new FileOutputStream("/Users/sxc/Documents/java/netty/netty-in-action/nio-learn/file/file3.txt");
        FileChannel outputChannel = fileOutputStream.getChannel();


        ByteBuffer buffer = ByteBuffer.allocate(5);

        while (true) {
            // 缓冲区一定要复位 清空缓冲区数据
            buffer.clear();
            // 循环读取 返回值 是读取数据
            int read = inputChannel.read(buffer);
            System.out.println("read =" + read);
            // 表示读完了
            if (read == -1) {
                break;
            }
            buffer.flip();
            outputChannel.write(buffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }
}
