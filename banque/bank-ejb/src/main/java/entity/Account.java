package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name=Account.QN.ALL_ACCOUNT, query="SELECT c FROM Account c"),
    @NamedQuery(name=Account.QN.FIND_ACCOUNT, query="SELECT c FROM Account c WHERE c.name = :NAME"),
    @NamedQuery(name=Account.QN.FIND_ACCOUNT_BY_ID, query="SELECT c FROM Account c WHERE c.id = :ID")
})
public class Account implements Serializable {

    public static class QN {
        public static final String ALL_ACCOUNT = "Account.allAccount";
        public static final String FIND_ACCOUNT = "Account.findAccount";
        public static final String FIND_ACCOUNT_BY_ID = "Account.findAccountById";
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private String name;
    private float balance;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private Collection<Transaction> transactions = new ArrayList<>();

    public Account() {
    }

    public Account(String name, float balance) {
        this.name = name;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(String typeTransaction, float amount) {
        Transaction transaction = new Transaction(typeTransaction, amount, this);
        transactions.add(transaction);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d, name=%s, balance=%.2f euros]", 
                             this.getClass().getSimpleName(), id, name, balance);
    }
}
