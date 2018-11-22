package com.test.framework;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.beanutils.MethodUtils;

public class ProviderReflect {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void provider(final Object service, int port) throws Exception {
        final ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            final Socket socket = serverSocket.accept();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        try {
                            try {
                                String method = objectInputStream.readUTF();
                                Object[] params = (Object[]) objectInputStream.readObject();
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                try {
                                    Object result = MethodUtils.invokeExactMethod(service, method, params);
                                    objectOutputStream.writeObject(result);

                                } catch (Throwable t) {
                                    objectOutputStream.writeObject(t);
                                } finally {
                                    objectOutputStream.close();
                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            } finally {
                                objectInputStream.close();
                            }

                        }finally {
                            socket.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            });
        }

    }
}
