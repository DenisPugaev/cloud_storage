package com.geekbrains;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.geekbrains.Server.log;

public class RegHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof RegistrationMsg) {
            RegistrationMsg registration = (RegistrationMsg) msg;
            String user = SqlAuthService.authUser(registration.login, registration.password);
            if (user != null) {
                RegistrationMsg registrationMsg = new RegistrationMsg("/notNullUser");
                ctx.writeAndFlush(registrationMsg);
            } else {
                SqlAuthService.registrationNewUser(registration.login, registration.password, registration.nickName);
                Files.createDirectory(Paths.get("ServerStorage-" + registration.nickName));
                RegistrationMsg registrationMsg = new RegistrationMsg("/regOK " + registration.nickName);
                ctx.writeAndFlush(registrationMsg);
            }
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
