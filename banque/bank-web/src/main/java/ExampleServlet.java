package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Account;
import entity.Transaction;
import init.Initializer;
import reader.LocalReader;
import writer.LocalWriter;

public class ExampleServlet extends HttpServlet {

    private static final long serialVersionUID = -3172627111841538912L;

    @EJB
    private LocalReader readerBean;

    @EJB
    private LocalWriter writerBean;

    @EJB
    private Initializer initializerBean;

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
        out.println("  <head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("    <link type=\"text/css\" href=\"ExampleServlet.css\" rel=\"stylesheet\" id=\"stylesheet\" />");
        out.println("    <title>Affichage des comptes</title>");
        out.println("  </head>");
        out.println("<body>");

        out.println("<header>");
        out.println("  <form action=\"\\bank-web\" method=\"get\">");
        out.println("    <input type=\"submit\" value=\"Accueil\" class=\"home-button\"/>");
        out.println("  </form>");
        out.println("</header>");

        out.println("<main>");
        out.println("  <h1>Affichage des comptes</h1>");
        out.println("  <section class=\"account-display\">");

        initAccount(out);
        displayAccount(out);

        out.println("  </section>");
        out.println("  <section class=\"additional-links\">");
        out.println("    <form action=\"secured/Admin\" method=\"get\">");
        out.println("      <input type=\"submit\" value=\"Ajouter un account\" class=\"admin-button\"/>");
        out.println("    </form>");
        out.println("  </section>");
        out.println("</main>");

        out.println("</body>");
        out.println("</html>");
        out.close();

    }

    private void initAccount(final PrintWriter out) {
        out.println("Initialisation des comptes<br/>");

        try {
            initializerBean.initializeEntities();
        } catch (Exception e) {
            displayException(out, "Impossible d'initialiser la liste des comptes", e);
            return;
        }
    }

    private void displayAccount(final PrintWriter out) {
        out.println("<div class='account-container'>");

        List<Account> accounts = null;
        try {
            accounts = readerBean.listAllAccounts();
        } catch (Exception e) {
            displayException(out, "Impossible d'appeler listAllAccount sur le bean", e);
            return;
        }
    
        if (accounts != null && !accounts.isEmpty()) {
            out.println("<h2>Liste des comptes avec leurs soldes</h2>");
            out.println("<table>");
            out.println("<thead><tr><th>Nom du compte</th><th>Solde</th><th>Opérations</th></tr></thead>");
            out.println("<tbody>");
            for (Account account : accounts) {
                out.println("<tr>");
                out.println("<td>" + account.getName() + "</td>");
                out.println("<td>" + account.getBalance() + " euros</td>");
                out.println("<td>");
                Collection<Transaction> lesTransactionsDuAccount = account.getTransactions();
                if (lesTransactionsDuAccount != null && !lesTransactionsDuAccount.isEmpty()) {
                    out.println("<ul>");
                    for (Transaction transaction : lesTransactionsDuAccount) {
                        out.println("<li>" + transaction.getTypeTransaction() + " de " + transaction.getAmount() + " euros</li>");
                    }
                    out.println("</ul>");
                } else {
                    out.println("Aucune opération");
                }
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");
        } else {
            out.println("<p>Aucun account trouvé !</p>");
        }
    
        out.println("</div>");
    }
    
    private void displayException(final PrintWriter out, final String errMsg, final Exception e) {
        out.println("<p>Exception : " + errMsg);
        out.println("<pre>");
        e.printStackTrace(out);
        out.println("</pre></p>");
    }

}
