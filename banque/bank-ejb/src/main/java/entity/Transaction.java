package entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name=Transaction.QN.ALL_TRANSACTIONS, query="SELECT o FROM Transaction o")
})
public class Transaction implements Serializable {

    public static class QN {
        public static final String ALL_TRANSACTIONS = "Transaction.allTransaction";
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="Account_id")
    private Account account;

    private String typeTransaction;
    private float amount;

    public Transaction() {
    }

    public Transaction(String typeTransaction, float amount, Account account) {
        this.typeTransaction = typeTransaction;
        this.amount = amount;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d, typeTransaction=%s, amount=%.2f]", 
                             this.getClass().getSimpleName(), id, typeTransaction, amount);
    }
}
