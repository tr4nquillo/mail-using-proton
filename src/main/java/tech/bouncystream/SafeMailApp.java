package tech.bouncystream;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.Properties;

public class SafeMailApp {

    private static final int MAX_RETRIES = 3;
    private static final int DELAY_SECONDS = 5;

    private static Properties properties() {
        final var properties = new Properties();

        try {
            final var propertiesFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("mail.properties");
            properties.load(propertiesFile);
        } catch (IOException ioException) {
            System.err.println("Error loading properties file:" + ioException.getMessage());
        }
        return properties;
    }

    public static void main(String[] args) throws InterruptedException {

        var props = SafeMailApp.properties();

        var username = System.getenv("MAIL_USER_NAME");
        var password = System.getenv("MAIL_PWD"); // from Bridge, not your Proton password

        var session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            final var message = SafeMailApp.message(session, username);
            send(message);
        } catch (InterruptedException interruptedEx) {
            // don't need to do anything now, because it's a single threaded app
        } catch (MessagingException messagingException) {
            System.err.println("Failed while sending the message: " + messagingException);
        }
    }

    private static void send (MimeMessage message) throws InterruptedException, MessagingException {
        var delaySeconds = DELAY_SECONDS;

        // for example an exponential backoff and then retry
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                Transport.send(message);
                System.out.println("Mail sent!");
                break;
            } catch (MessagingException messagingEx) {
                if (i == MAX_RETRIES - 1) {
                    throw messagingEx;
                }
                Thread.sleep(DELAY_SECONDS * 1000L);
                delaySeconds*=2;
            }
        }
    }

    private static MimeMessage message(Session session, String username) throws MessagingException {
            var message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("fanmail@example.com"));
            message.setSubject("Fun Mail");
            message.setText("Fun Mail with jakarta mail.");
            return message;
    }

}

