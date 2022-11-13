package com.geekbrains;


import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;

public class Network {
    private static Socket socket;

    private static ObjectDecoderInputStream in;

    private static ObjectEncoderOutputStream out;

    private static boolean opened;

    public static boolean isOpened() {
        return opened;
    }

    public static void setOpened(boolean opened) {
        Network.opened = opened;
    }

    static void start() {
        try {
            socket = new Socket("localhost", 8080);
            opened = true;
            in = new ObjectDecoderInputStream(socket.getInputStream(), 200 * 1024 * 1024);
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void stop() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean sendMsg(AbstractMsg msg) {
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    static AbstractMsg readAbstractMsg() {
        AbstractMsg msg = null;
        try {
            if (opened && !socket.isClosed()) {

                msg = (AbstractMsg) in.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;

    }


}

