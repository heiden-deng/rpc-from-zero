package com.test.framework;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class ConsumerProxy {
    public static <T> T consumer(final Class<T> interfacesClass,final String host, final int port) throws Exception{
        return (T) Proxy.newProxyInstance(interfacesClass.getClassLoader(), new Class<?>[]{interfacesClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket(host,port);

                try{
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    try{
                        objectOutputStream.writeUTF(method.getName());
                        objectOutputStream.writeObject(args);

                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        try{
                            Object result = objectInputStream.readObject();
                            if (result instanceof Throwable){
                                throw (Throwable)result;
                            }
                            return result;
                        }finally {
                            objectInputStream.close();
                        }
                    }finally {
                        objectOutputStream.close();
                    }

                }finally {
                    socket.close();
                }
            }
        });
    }
}
