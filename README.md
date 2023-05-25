# netty-learn

# 工作原理

1. Netty 抽象出两组线程池BossGroup 专门负责接收客户端的连接，
   WorkerGroup 专门负责网络的读写
2. BossGroup和WorkGroup类型都是NioEventLoopGroup
3. NioEventLoopGroup相当于一个事件循环，这个组中含有多个事件循环，每一个事件循环是NioEventLoop
4. NioEventLoop 表示一个不断循环的执行处理任务的线程，每个NioEventLoop都有一个selector 用于监听绑定在其上的socket的网络通讯
5. NioEventLoopGroup 可以有多个线程，即可以含有多个NioEventLoop
6. 每个BossNioEventLoop执行的步骤有3
    1) 轮询accept事件
    2) 处理accept事件，与client建立连接，生成NioSocketChannel, 并将其注册到某个workNioEventLoop上的selector
    3) 处理任务队列的任务， 即runAllTasks
7. 每个WorkNioEventLoop循环执行步骤
    1) 轮询read，write事件
    2) 处理i/o事件，即read，write事件，在对应NioSocketChannel处理
    3) 处理任务队列，即runAllTasks
8. 每个WorkNioEventLoop处理业务时，会使用pipeline（管道）,pipeline中包含channel 即通过pipeline可以获取对应

Netty高性能架构设计
线程模型：

1. 传统阻塞I/O服务模型
2. Reactor模式
   1). 单Reactor单线程
   2). 单Reactor多线程
   3). 主从Reactor多线程 （Netty基于该模型做改进）

# Netty组件：

### NIOEventLoopGroup(线程池组)： 在netty中抽象出 parentGroup(处理客户端连接)、childGroup(Handle NET write/read)

### NIOEventLoop: ``表示一个不断循环处理任务的线程, 包含一下组件``

* Selector: ``用于监听绑定在其上的Socket网络通道``
    * NioChannel(通道):``可以注册多个被一个Selecotr监听``
        * 只会绑定在唯一的NioEventLoop
        * 会绑定一个自己的ChannelPipeline
* taskQueue：``用户程序自定义普通任务队列``
* scheduledTaskQueue: ``用户自定义定时任务队列``

* Bootstrap(客户端启动类), ServerBootstrap(服务端启动类)


### 异步模型

``异步模型建立在future、callback``
``链式操作``

* Future:``异步执行结果``
    * ChannelFuture接口： 实现该接口可以添加自定义监听器，当监听的事件发生，就会通知监听器
*

# 面试可能会出现的问题

1. Netty中NioEventLoop 默认线程数是多少？ 答： 是服务器CPU核数的两倍
2. ChannelHandlerContext netty上下文 可以获取 channel、pipeline、eventLoop等信息