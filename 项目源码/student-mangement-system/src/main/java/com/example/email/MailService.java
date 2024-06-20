package com.example.email;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Properties;

/**
 */
@Component
public class MailService {

    /**
     * 邮件属性动态配置
     */
    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.password}")
    private String passwd;

    @Value("${spring.mail.host}")
    private String smtpHost;

    public Session getSession() {
        // 定义会话属性
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", smtpHost);
        props.setProperty("mail.smtp.auth", "true");
        Session session = Session.getInstance(props);
        session.setDebug(true);

        return session;
    }

    /**
     * 创建邮件信息
     * @param session 邮件会话
     * @param receiveMail 收取方
     * @param content  邮件内容
     * @return 信息实例
     * @throws Exception 创建邮件异常
     */
    public MimeMessage createMimeMessage(Session session,String receiveMail, String content) throws Exception {
        // 配置 message: From, To, Subject, Content, Time
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender, "SCU student system support", "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "", "UTF-8"));
        message.setSubject("验证码", "UTF-8");
        message.setContent(content, "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        // 应用配置
        message.saveChanges();
        return message;
    }

    /**
     * 发送邮件信息
     * @return 发送状态
     * @throws MessagingException 发送邮件异常
     */
    public Boolean sendMessage(Session session, MimeMessage message) throws MessagingException {
        // 建立传输通道发送
        Transport transport = session.getTransport();
        transport.connect(sender, passwd);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

        return true;
    }


}
