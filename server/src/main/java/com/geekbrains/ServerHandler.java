package com.geekbrains;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import static com.geekbrains.Server.log;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final String nickName;

    ServerHandler(String user) {
        this.nickName = user;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ctx.writeAndFlush(new AuthMsg());

            if (msg instanceof DownloadMsg) {
                DownloadMsg downloadRequest = (DownloadMsg) msg;
                if (Files.exists(Paths.get("ServerStorage-" + nickName + "/" + downloadRequest.getFileName()))) {
                    FileMsg fileMsg = new FileMsg((Paths.get("ServerStorage-" + nickName + "/" + downloadRequest.getFileName())));
                    ctx.writeAndFlush(fileMsg);
                }
            }
            if (msg instanceof DeleteMsg) {
                DeleteMsg deleteMsg = (DeleteMsg) msg;
                if (deleteMsg.getFileName() == null) {
                    ctx.writeAndFlush(new DeleteMsg("/deleteError"));
                } else {
                    Files.delete(Paths.get("ServerStorage-" + nickName + "/" + deleteMsg.getFileName()));
                    ctx.writeAndFlush(new DeleteMsg("/deleteOK"));
                    updateServerFileList(ctx);
                }
            }
            if (msg instanceof FileMsg) {
                FileMsg fileMessage = (FileMsg) msg;
                Files.write(Paths.get("ServerStorage-" + nickName + "/" + fileMessage.getFileName()),
                        fileMessage.getData(), StandardOpenOption.CREATE);
                updateServerFileList(ctx);
            }
            if (msg instanceof UpdateFileListMsg) {
                updateServerFileList(ctx);
            }
            if (msg instanceof AuthMsg) {
                AuthMsg authMsg = (AuthMsg) msg;
                if ("/closeConnection".equals(authMsg.message)) {
                    ctx.close();
                }
            }
            if (msg instanceof RenameMsg) {
                RenameMsg renameMsg = (RenameMsg) msg;
                String renameTextServer = renameMsg.getNewFileName().trim();
                if (renameTextServer.isEmpty()) {
                    ctx.writeAndFlush(new RenameMsg("/renameEmpty"));
                    return;
                }
                if (Files.list(Paths.get("ServerStorage-" + nickName)).map(Path::getFileName)
                        .anyMatch(f -> f.toString().equals(renameTextServer))) {
                    ctx.writeAndFlush(new RenameMsg("/renameBusy"));
                } else {
                    Files.move(Paths.get("ServerStorage-" + nickName + "/" + renameMsg.getOldFileName()),
                            (Paths.get("ServerStorage-" + nickName + "/" + renameMsg.getOldFileName())).resolveSibling(renameTextServer));
                    ctx.writeAndFlush(new RenameMsg("/renameOK"));
                    updateServerFileList(ctx);
                }
            }


        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void updateServerFileList(ChannelHandlerContext ctx) {
        try {
            ArrayList<String> serverFileList = new ArrayList<>();
            Files.list(Paths.get("ServerStorage-" + nickName + "/")).map(p -> p.getFileName().toString()).forEach(serverFileList::add);
            ctx.writeAndFlush(new UpdateFileListMsg(serverFileList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info(nickName + " подключился");
    }
}

