# coding=utf-8
import smtplib
from email.header import Header
from email.mime.text import MIMEText

from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
from thrift.transport import TSocket, TTransport

from message.api import MessageService, config


class MessageHandle(object):
    def sendMobileMessage(self, mobile, message):
        # 做一个假的发短信
        print('send mobile message, mobile:%s, message:%s' % (mobile, message))
        return True

    def sendEmailMessage(self, email, message):
        print('send email message, email:%s, message:%s' % (email, message))
        message_obj = MIMEText(message, 'plain', 'utf-8')
        message_obj['From'] = config.SENDER_EMAIL
        message_obj['To'] = email
        message_obj['Subject'] = Header(config.EMAIL_HEADER, 'utf-8')
        try:
            smtp_obj = smtplib.SMTP('smtp.163.com')
            smtp_obj.login(config.SENDER_EMAIL, config.AUTH_CODE)
            smtp_obj.sendmail(config.SENDER_EMAIL, [email], message_obj.as_string())
            print('mail send success')
            return True
        except smtplib.SMTPException as e:
            print(e)
            print('mail send failed')
            return False


if __name__ == '__main__':
    handle = MessageHandle()
    processor = MessageService.Processor(handle)
    # 监听9090端口
    transport = TSocket.TServerSocket(None, 9090)
    # 传输方式 帧传输,报文一帧一帧传输
    tfactory = TTransport.TFramedTransportFactory()
    # 传输协议,使用二进制的传输协议
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)
    print('python thrift server start')
    server.serve()
    print('python thrift server exit')
