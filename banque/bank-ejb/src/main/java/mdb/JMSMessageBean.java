package mdb;

import javax.annotation.security.RunAs;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import entity.Account;
import entity.Transaction;
import reader.LocalReader;
import writer.LocalWriter;

@MessageDriven(name = "EJBbankQueue", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/SampleQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "maxPoolSize", propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1")
})
public class JMSMessageBean implements MessageListener {

    @EJB
    private LocalWriter writer;

    @EJB
    private LocalReader reader;

    @Override
    public void onMessage(final Message message) {
        if (!(message instanceof TextMessage)) {
            return;
        }

        TextMessage textMessage = (TextMessage) message;
        String text;
        try {
            text = textMessage.getText();
        } catch (JMSException e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace(System.err);
            return;
        }

        processTransaction(text);
    }

    private void processTransaction(String text) {
        String[] parts = text.split("_");
        if (parts.length < 3) {
            System.err.println("Invalid message format");
            return;
        }

        try {
            long accountId = Long.parseLong(parts[0]);
            String transactionType = parts[1];
            float amount = Float.parseFloat(parts[2]);
            float amountUpdated = transactionType.equals("Retrait") ? -amount : amount;

            Account account = reader.findAccountById(accountId);
            if (account != null) {
                account.setBalance(account.getBalance() + amountUpdated);
                writer.updateAccount(account);
                Transaction transaction = new Transaction(transactionType, amount, account);
                writer.addTransaction(transaction);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing message: " + e.getMessage());
        }
    }
}
