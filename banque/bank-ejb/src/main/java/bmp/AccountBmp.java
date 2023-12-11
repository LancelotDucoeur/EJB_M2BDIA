import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AccountBmp {

  private long id;
  private String name;
  private float balance;

  public void delete() {
    try {
      InitialContext ctx = new InitialContext();
      DataSource ds = (DataSource) ctx.lookup("java:/OracleDS");

      try (Connection conn = ds.getConnection();
           PreparedStatement stmt = conn.prepareStatement("DELETE FROM account WHERE id = ?")) {
        stmt.setLong(1, this.id);
        stmt.executeUpdate();
      }
    } catch (NamingException e) {
      System.err.println("Erreur JNDI : " + e);
    } catch (SQLException e) {
      System.err.println("Erreur connexion BD: " + e);
    }
  }
}
