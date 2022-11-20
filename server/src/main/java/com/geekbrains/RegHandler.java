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
            String user = SqlAuthService.authUser(registration.getLogin(), registration.getPassword());
            if (user != null) {
                RegistrationMsg registrationMsg = new RegistrationMsg("/notNullUser");
                ctx.writeAndFlush(registrationMsg);
            } else {
                SqlAuthService.registrationNewUser(registration.getLogin(), registration.getPassword(), registration.getNickName());
                Files.createDirectory(Paths.get("ServerStorage-" + registration.getNickName()));
                RegistrationMsg registrationMsg = new RegistrationMsg("/regOK " + registration.getNickName());
                log.info(registrationMsg);
                ctx.writeAndFlush(registrationMsg);
            }
            return;
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
