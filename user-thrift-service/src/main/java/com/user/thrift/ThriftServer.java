/**
 * @Project: micro-service
 * @Package com.user.thrift
 * @author : zzc
 * @date Date : 2019年08月04日 上午10:38
 * @version V1.0
 */


package com.user.thrift;

import com.thrift.user.UserService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ThriftServer {
    @Autowired
    private UserService.Iface userService;

    @Value("${server.port}")
    private int serverPort;

    @PostConstruct
    public void startThriftServer() {
        // 注册到thrift容器中,以下代码类似于message-thrift-python-service
        TProcessor processor = new UserService.Processor<>(userService);
        TNonblockingServerSocket socket = null;
        // 使用NIO
        try {
            socket = new TNonblockingServerSocket(serverPort);
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        TNonblockingServer.Args args = new TNonblockingServer.Args(socket);
        args.processor(processor);
        // 传输方式
        args.transportFactory(new TFastFramedTransport.Factory());
        // 传输协议
        args.protocolFactory(new TBinaryProtocol.Factory());
        TServer server = new TNonblockingServer(args);
        server.serve();
    }
}
