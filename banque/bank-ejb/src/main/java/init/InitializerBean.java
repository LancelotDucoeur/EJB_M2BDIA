package init;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import entity.Account;
import entity.Transaction;
import reader.LocalReader;
import writer.LocalWriter;

@Stateless(mappedName="myInitializerBean")
@Remote(Initializer.class)
public class InitializerBean implements Initializer {

    @EJB
    private LocalWriter writer;

    @EJB
    private LocalReader reader;

    @Override
    public void initializeEntities() {
        createAccountIfNotExists("Lancelot", 10000);
        createAccountIfNotExists("Thibault", 500);
    }

    private void createAccountIfNotExists(String accountName, float initialBalance) {
        if (reader.findAccount(accountName) == null) {
            Account account = new Account(accountName, initialBalance);
            createAndAddTransaction(account, "Retrait", 15);
            createAndAddTransaction(account, "Ajout", 50);
            writer.addAccount(account);
        }
    }

    private void createAndAddTransaction(Account account, String transactionType, float amount) {
        Transaction transaction = new Transaction(transactionType, amount, account);
        float newBalance = transactionType.equals("Retrait") ? account.getBalance() - amount : account.getBalance() + amount;
        account.setBalance(newBalance);
        account.getTransactions().add(transaction);
    }
}
