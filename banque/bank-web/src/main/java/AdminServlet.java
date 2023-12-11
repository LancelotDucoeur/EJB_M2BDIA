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

public class AdminServlet extends HttpServlet {

    private static final long serialVersionUID = 7724116000656853982L;

    @EJB
    private LocalWriter writerBean;

    @EJB
    private LocalReader readerBean;

    @EJB
    private Initializer initializerBean;

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String title = "Ajout d'un compte";

        printHTMLHeader(out, title);
        
        out.println("<div class=\"contenneurAjoutEtAffich\">");

        out.println("  <div class=\"contennueAjout\">");

        initAccount(out);

        out.println("<br />");

        printAddAccount(out);

        out.println("  </div>");

        
        out.println("  <div class=\"contennueAffich\">");

        addAccount(out, request.getParameter("name"), request.getParameter("balance"));

        printAccounts(out);
        out.println("  </div>");
        out.println("</div>");

        out.close();
    }

    private void printHTMLHeader(final PrintWriter out, final String title) {
        out.println("<!DOCTYPE html>");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");
        out.println("    <link type=\"text/css\" href=\"../AdminServlet.css\" rel=\"stylesheet\" id=\"stylesheet\" />");
        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<header>");
        out.println("<form action=\"\\bank-web\" method=\"get\">");
        out.println("<input type=\"submit\" class=\"home-button\" value=\"Accueil\"/>");
        out.println("</form>");
        out.println("<h1>" + title + "</h1>");
        out.println("</header>");
    }

    private void initAccount(final PrintWriter out) {
        out.println("<section class='init-account'>");
        out.println("<p>Initialisation des comptes...</p>");
        try {
            initializerBean.initializeEntities();
            out.println("<p>Initialisation réussie.</p>");
        } catch (Exception e) {
            printException(out, "Impossible d'initialiser la liste des comptes", e);
            return;
        }
        out.println("</section>");
    }

    private void printAccounts(final PrintWriter out) {
        out.println("<section class='account-list'>");
        out.println("<h2>Liste des Accounts</h2>");
    
        List<Account> accounts = null;
        try {
            accounts = readerBean.listAllAccounts();
        } catch (Exception e) {
            printException(out, "Impossible d'appeler listAllAccount sur le bean", e);
            return;
        }
    
        if (accounts != null && !accounts.isEmpty()) {
            out.println("<ul>");
            for (Account account : accounts) {
                out.println("<li><strong>" + account.getName() + " a " + account.getBalance() + " euros.</strong></li>");
            }
            out.println("</ul>");
        } else {
            out.println("<p>Aucun account trouvé.</p>");
        }
    
        out.println("</section>");
    }

    private void printAddAccount(final PrintWriter out) {
        out.println("<section class='add-account'>");
        out.println("<h2>Ajouter un compte</h2>");
        out.println("<form action=\"add-account\" method=\"get\">");
        out.println("<div>");
        out.println("<input name=\"name\" type=\"text\" placeholder=\"Nom du compte\"/>");
        out.println("<br/><br/>");
        out.println("<input name=\"balance\" type=\"number\" placeholder=\"Solde du compte\"/>");
        out.println("<br/><br/>");
        out.println("<input type=\"submit\" value=\"Ajouter\"/>");
        out.println("</div>");
        out.println("</form>");
        out.println("</section>");
    }
    

    private void addAccount(final PrintWriter out, final String name, final String balance) {
        if (name != null && balance != null) {
            try {
                float balanceFloat = Float.parseFloat(balance);
                Account account = new Account(name, balanceFloat);
                writerBean.addAccount(account);
                out.println("<p>Compte ajouté avec succès.</p>");
            } catch (NumberFormatException e) {
                printException(out, "Le solde doit être un nombre valide", e);
            } catch (Exception e) {
                printException(out, "Impossible d'ajouter un nouveau compte", e);
            }
        }
    }
    
    private void printException(final PrintWriter out, final String errMsg, final Exception e) {
        out.println("<section class='exception'>");
        out.println("<h2>Erreur</h2>");
        out.println("<p>" + errMsg + "</p>");
        out.println("<pre>");
        e.printStackTrace(out);
        out.println("</pre>");
        out.println("</section>");
    }
    

}
