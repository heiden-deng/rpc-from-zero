package com.test.invoke;

import com.test.framework.ConsumerProxy;

import com.test.service.HelloService;


public class RpcConsumerMain {
    public static void main( String[] args )
    {
        try{
            HelloService helloService = ConsumerProxy.consumer(HelloService.class,"127.0.0.1",8999);
            String result = helloService.sayHello("dengjq");
            System.out.println("call result=" + result);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
