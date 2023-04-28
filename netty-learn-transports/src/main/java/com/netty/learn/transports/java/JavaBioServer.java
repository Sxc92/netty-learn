package com.netty.learn.transports.java;

import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO Block IO (Oio old IO)
 * @author 史偕成
 * @date 2023/04/25 15:08
 **/
public class JavaBioServer {

    public void server(int port) throws IOException {
        // 将服务器绑定到指定端口
        final ServerSocket serverSocket = new ServerSocket(port);
        try {
            for (; ; ) {
                // 接受连接
                final Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connect from " + clientSocket);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream output;
                        try {
                            output = clientSocket.getOutputStream();
                            // 将消息写给客户端
                            output.write("Hi\r\n".getBytes(CharsetUtil.UTF_8));
                            output.flush();
                            // 关闭连接
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
