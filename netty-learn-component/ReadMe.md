# Netty 组件分析
* Channel -- Socket
* EventLoop -- 控制流、多线程处理、并发
* ChannelFuture -- 异步通知

## 1. Channel接口
 ### 基本IO操作（bind、connect、read、write）Netty提供了多个API简化操作
1.1 EmbeddedChannel

1.2 LocalServerChannel

1.3 NioDatagramChannel

1.4 NioSocketChannel

## 2. EventLoop接口
 ### 定义了Netty的核心抽象，用于处理连接的生命周期中所发生的事情