package reader;

import java.util.List;

import entity.Account;
import entity.Transaction;

public interface Reader {

    List<Account> listAllAccounts();

    Account findAccount(final String name);

    Account findAccountById(final long id);

    List<Transaction> listAllTransactions();
}
