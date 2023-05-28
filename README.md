## 关于 Lint RPC Framework
>  LintRPC是一个轻量级Java RPC 框架, 底层采用Netty实现, 模拟Dubbo运行模式(闲来无事 练习一下)

>  本质上就是代替完成网络请求，让程序调用接口如调用本地的一个Interface一样简单

## 地址
- 作者博客: <a href="https://www.bedebug.com" target="_blank">https://www.bedebug.com</a>
- 交流Q群: 724850675 (1群)

## 相关技术栈
| 名称             | 版本号 |
| ---------------- |---|
| Netty版本          | 4.1.90.Final |

## 启动顺序
1. lint-rpc-demo-provide2 (提供 drink结果 和 drinkListAll)
2. lint-rpc-demo-provide1 (提供 eat结果 ，同时调用provide2，进行组合结果)
3. lint-rpc-demo-consumer (分别调用 provide1、provide2)

## 技术介绍
1. lint-rpc-demo 为使用 lint-rpc框架做的demo，包含一个 consumer 两个provide
   其中 consumer 开启20个线程 去定时请求 2个 provide，其中一个provide调用了另外一个provide
2. lint-rpc-spi 利用SPI技术 固定服务列表扩展模式
   先支持本地服务列表，后序可扩展成 nacos、zk等服务列表
3. lint-rpc-support 为整个框架的核心包
   其工作的原理便是，使用jdk自身的proxy去动态代理 rpc接口，
   底层使用netty作为网络通信（懒得使用原生的nio网络模型了，太麻烦了 还需要处理一大堆东西）
   在netty channel 的 handler中，定义了 protocol 自定义协议，用于规定网络传输格式和为后序jdk序列化做足准备
   当provide收到请求后，通过序列化的结果，从本地spi库中查询本地服务，并通过反射进行invoke执行

**相关技术点（读源码，你可以学习到）**
```text
1. 可插拔式扩展接口（类似与springboot的autoconfigure）
   SPI

2. 相关设计模式
   动态代理、策略模式、工厂模式、单例模式
   
3. 池化思想

4. Java反射

5. 自定义消息协议 与 网络请求粘包拆包

6. 多线程与高并发与线程锁
```   


## RPC需要什么
### 想要实现一个基本的RPC框架，其实需要什么？
1. 网络IO，BIO\NIO\AIO，Socket编程，HTTP通信，一个就行。
2. 序列化，JDK序列化，JSON、Hessian、Kryo、ProtoBuffer、ProtoStuff、Fst知道一个就行。
3. 反射，JDK或者Cglib的动态代理。
   
### 那一个优秀的RPC框架，还需要考虑什么问题?
1. 一个服务可能有多个实例，你在调用时，要如何获取这些实例的地址？
2. 服务注册中心多个实例，选哪个调用好？
3. 负载均衡服务注册中心每次都查？
4. 缓存相关客户端每次要等服务器返回结果？
5. 异步调用服务是要升级的？
6. 版本控制多个服务依赖，某个有问题？
7. 熔断器某个服务出了问题怎么办？监控 ..

## 支持
> 谢谢您愿意支持开源
<div align="center">
<img width="200" src="https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/vx.png"/>
<img width="200" src="https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/zfb.png"/>
</div>
