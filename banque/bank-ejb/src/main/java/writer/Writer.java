package writer;

import java.lang.management.OperatingSystemMXBean;

import entity.Account;
import entity.Transaction;


/**
 * Interface distante pour le bean Writer.
 */
public interface Writer {

    void addAccount(final Account account);

    void removeAccount(final Account account);

    public void updateAccount(final Account account);

    void addTransaction(final Transaction transaction);

    void removeTransaction(final Transaction transaction);

    public void updateTransaction(final Transaction transaction);
}
