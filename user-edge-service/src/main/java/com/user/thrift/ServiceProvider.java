/**
 * @Project: micro-service
 * @Package com.user.thrift
 * @author : zzc
 * @date Date : 2019年08月04日 上午11:13
 * @version V1.0
 */


package com.user.thrift;

import com.thrift.message.MessageService;
import com.thrift.user.UserService;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceProvider {
    /**
    * 提供一个thrift客户端来连接服务端
    */
    @Value("${thrift.user.ip}")
    private String userServiceIp;

    @Value("${thrift.user.port}")
    private int userServicePort;

    @Value("${thrift.message.ip}")
    private String messageServiceIp;

    @Value("${thrift.message.port}")
    private int messageServicePort;

    private enum ServiceType {
        USER,
        MESSAGE
    }

    public UserService.Client getUserService() {
        return this.getService(userServiceIp, userServicePort, ServiceType.USER);
    }

    public MessageService.Client getMessageService() {
        return this.getService(messageServiceIp, messageServicePort, ServiceType.MESSAGE);
    }

    public <T> T getService(String ip, int port, ServiceType serviceType) {
        TSocket socket = new TSocket(ip, port, 10000);
        TTransport transport = new TFramedTransport(socket);
        try {
            transport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        TProtocol protocol = new TBinaryProtocol(transport);
        TServiceClient result = null;
        switch (serviceType) {
            case USER:
                result = new UserService.Client(protocol);
                break;
            case MESSAGE:
                result = new MessageService.Client(protocol);
        }
        return (T) result;
    }
}
