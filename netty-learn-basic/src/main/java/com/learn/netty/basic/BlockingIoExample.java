package com.learn.netty.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 阻塞函数 展示调用服务器代码的普通示例
 *
 * @author 史偕成
 * @Date 2023/04/24 10:52
 **/
public class BlockingIoExample {

    public void serve(int portNumber) {
        try {
            // 创建一个新的ServerSocket 用以监听指定端口
            ServerSocket serverSocket = new ServerSocket(portNumber);
            // 将会一直阻塞到一个连接建立，随后返回一个新的Socket用于客户端和服务端之间的通信， 改ServerSocket将继续监听传入的连接
            Socket clientSocket = serverSocket.accept();
            // 都衍生自Socket的输入输出流
            // 从一个字符输入流中读取文本
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            // 打印对象的格式化的表示到文本输出流
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String request, response;
            while ((request = in.readLine()) != null) {
                if ("Done".equals(request)) {
                    break;
                }
                response = processRequest(request);
                out.println(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String processRequest(String request) {
        return "Processed";
    }
}
