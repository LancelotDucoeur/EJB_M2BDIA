package reader;

import static entity.Account.QN.FIND_ACCOUNT;
import static entity.Account.QN.FIND_ACCOUNT_BY_ID;
import static entity.Account.QN.ALL_ACCOUNT;
import static entity.Transaction.QN.ALL_TRANSACTIONS;


import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entity.Account;
import entity.Transaction;

@Stateless
@Local(LocalReader.class)
@Remote(RemoteReader.class)
public class ReaderBean implements LocalReader, RemoteReader {

    @PersistenceContext
    private EntityManager entityManager = null;

    @SuppressWarnings("unchecked")
    public Account findAccount(final String name) {
        Query query = entityManager.createNamedQuery(FIND_ACCOUNT);
        query.setParameter("NAME", name);
        List<Account> accounts = query.getResultList();
        if (accounts != null && accounts.size() > 0) {
            return accounts.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Account findAccountById(final long id) {
        Query query = entityManager.createNamedQuery(FIND_ACCOUNT_BY_ID);
        query.setParameter("ID", id);
        List<Account> account = query.getResultList();
        if (account != null && account.size() > 0) {
            return account.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Account> listAllAccounts() {
        return entityManager.createNamedQuery(ALL_ACCOUNT).getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Transaction> listAllTransactions() {
        return entityManager.createNamedQuery(ALL_TRANSACTIONS).getResultList();
    }

}
