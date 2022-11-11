package com.geekbrains;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import static com.geekbrains.Server.log;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    private boolean authOk = false;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
        try {
            if (authOk) {
                ctx.fireChannelRead(message);
                return;
            }
            if (message instanceof AuthMsg) {
                AuthMsg auth = (AuthMsg) message;
                String user = SqlAuthService.authUser(auth.login, auth.password);
                if (user != null) {
                    authOk = true;
                    ctx.pipeline().addLast(new ServerHandler(user));
                    ctx.writeAndFlush(new AuthMsg("/authOk " + user));
                } else if (auth.message.equals("/connectionClose")) {
                    authOk = false;
                    ctx.writeAndFlush(new AuthMsg("/nullUser"));
                } else {
                    ctx.writeAndFlush(new AuthMsg("/nullUser"));
                }
            }
        } finally {
            ReferenceCountUtil.release(message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
