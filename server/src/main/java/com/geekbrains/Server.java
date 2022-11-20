package com.geekbrains;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.log4j.Logger;


public class Server {
    public static Logger log = Logger.getLogger("stdout");

    public void run() {
        EventLoopGroup auth = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(auth, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder(1024 * 1024 * 500, ClassResolvers.cacheDisabled(null)),
                                    new RegHandler(),
                                    new AuthHandler()
                                    //serverHandler
                            );
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture cf = sb.bind(8080).sync();

            log.info("СТАРТ СЕРВЕРА...");
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("Ошибка на сервере!");
            e.printStackTrace();
        } finally {
            auth.shutdownGracefully();
            worker.shutdownGracefully();

        }
    }

    public static void main(String[] args) {
        new Server().run();
    }
}


