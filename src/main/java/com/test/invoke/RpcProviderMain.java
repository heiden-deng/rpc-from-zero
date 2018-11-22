package com.test.invoke;

import com.test.framework.ProviderReflect;
import com.test.service.HelloService;
import com.test.service.HelloServiceImpl;

public class RpcProviderMain {
    public static void main( String[] args )
    {
        HelloService helloService = new HelloServiceImpl();
        try{
            ProviderReflect.provider(helloService,8999);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
