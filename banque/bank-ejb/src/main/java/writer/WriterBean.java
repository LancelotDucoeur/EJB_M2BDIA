package writer;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entity.Account;
import entity.Transaction;

@Stateless
@Remote(RemoteWriter.class)
@Local(LocalWriter.class)
public class WriterBean implements LocalWriter, RemoteWriter {

    @PersistenceContext
    private EntityManager entityManager;

    public void addAccount(Account account) {
        entityManager.persist(account);
    }

    public void removeAccount(Account account) {
        Account mergedAccount = entityManager.merge(account);
        entityManager.remove(mergedAccount);
    }

    public void updateAccount(Account account) {
        entityManager.merge(account);
    }
    
    public void addTransaction(Transaction transaction) {
        entityManager.persist(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        Transaction mergedTransaction = entityManager.merge(transaction);
        entityManager.remove(mergedTransaction);
    }

    public void updateTransaction(Transaction transaction) {
        entityManager.merge(transaction);
    }
}
