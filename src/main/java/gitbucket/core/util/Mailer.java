/*******************************************************************************
 * Copyright (c) 2018-11-08 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package gitbucket.core.util;

import gitbucket.core.model.Account;
import gitbucket.core.service.SystemSettingsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

import java.util.List;

/**
 * Mailer
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-08
 * auto generate by qdp.
 */
public class Mailer {

    private SystemSettingsService.SystemSettings settings;

    public Mailer(SystemSettingsService.SystemSettings settings) {
        this.settings = settings;
    }

    public void send(String to, String subject, String textMsg, String htmlMsg, Account loginAccount) {
        HtmlEmail email = createMail(subject, textMsg, htmlMsg, loginAccount);
        if (email == null) {
            return;
        }
        try {
            email.addTo(to).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendBcc(List<String> bcc, String subject, String textMsg, String htmlMsg, Account loginAccount) {
        HtmlEmail email = createMail(subject, textMsg, htmlMsg, loginAccount);
        if (email == null) {
            return;
        }
        try {
            for (String address : bcc) {
                email.addBcc(address);
            }
            email.send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public HtmlEmail createMail(String subject, String textMsg, String htmlMsg, Account loginAccount) {
        if (!settings.notification()) {
            return null;
        }
        SystemSettingsService.Smtp smtp = settings.smtp().get();

        HtmlEmail email = null;
        try {
            email = new HtmlEmail();
            String host = smtp.host();
            Integer port = (Integer) smtp.port().get();
            Boolean ssl = (Boolean) smtp.ssl().get();
            Boolean starttls = (Boolean) smtp.starttls().get();
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setAuthenticator(new DefaultAuthenticator(smtp.user().get(), StringUtils.defaultString(smtp.password().get())));
            email.setSSLOnConnect(ssl);
            if (ssl == true) {
                email.setSslSmtpPort(port.toString());
            }
            email.setStartTLSEnabled(starttls);
            email.setStartTLSRequired(starttls);

            String fromAddress = smtp.fromAddress().get();
            String userName = loginAccount == null ? "" : StringUtils.defaultIfBlank(loginAccount.userName(), "GitBucket");
            String fromName = smtp.fromName().get();
            fromName = StringUtils.defaultIfBlank(fromName, userName);
            fromAddress = StringUtils.defaultIfBlank(fromAddress, "notifications@gitbucket.com");
            email.setFrom(fromAddress, userName);
            email.setCharset("UTF-8");
            email.setSubject(subject);
            if (textMsg != null) {
                email.setTextMsg(textMsg);
            }
            if (htmlMsg != null) {
                email.setHtmlMsg(htmlMsg);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return email;
    }
}
