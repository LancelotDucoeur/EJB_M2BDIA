package org.jboss.as.quickstarts.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ejb.EJB;


import entity.Account;
import entity.Transaction;
import reader.LocalReader;
import init.Initializer;


@JMSDestinationDefinitions(
    value = {
        @JMSDestinationDefinition(
            name = "java:/queue/SampleQueue",
            interfaceName = "javax.jms.Queue",
            destinationName = "EJBbankQueue"
        )
    }
)

@WebServlet("/MdbServlet")
public class MdbServlet extends HttpServlet {

    private static final long serialVersionUID = -8314035702649252239L;

    private static final int ITERATION_NUMBER = 10;

    @EJB
    private Initializer initializerBean;

    @EJB
    private LocalReader readerBean;

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:/queue/SampleQueue")
    private Queue queue;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
    
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <link type=\"text/css\" href=\"MdbServlet.css\" rel=\"stylesheet\" id=\"stylesheet\" />");
        out.println("<title>Ajout des Opérations avec JMS</title>");
        out.println("</head>");
        out.println("<body>");
    
        out.println("<header>");
        out.println("<form action='\\bank-web' method='get'>");
        out.println("<input type='submit' class='home-button' value='Accueil'/>");
        out.println("</form>");
        out.println("</header>");
    
        out.println("<main>");
        out.println("<h1>Ajout des opérations avec JMS</h1>");
    
        initAccount(out);
    
        List<Account> accounts = null;
        try {
            accounts = readerBean.listAllAccounts();
        } catch (Exception e) {
            displayException(out, "Impossible d'appeler listAllAccount sur le bean", e);
            return;
        }
    
        try {
            final Destination destination = queue;
            out.write("<h3>Les opérations suivantes vont être envoyées à la file :</h3>");

            Random rand = new Random();

            for (int i = 0; i < ITERATION_NUMBER; i++) {
                int nombreAleatoire = rand.nextInt(accounts.size());
                Account compteAleatoire = accounts.get(nombreAleatoire);
                long idCompteAleatoire = compteAleatoire.getId();

                String typeTransaction = rand.nextInt(2) == 0 ? "Dépôt" : "Retrait";

                int montantAleatoire = rand.nextInt(100) + 1;

                String message = idCompteAleatoire + "_" + typeTransaction + "_" + montantAleatoire;

                context.createProducer().send(destination, message);
                out.write("Message (" + i + "): " + typeTransaction + " de " + montantAleatoire + " euros sur le compte numéro " + idCompteAleatoire + "</br>");
            }

            out.println("<br/><br/>");
            out.println("<form action=\"\\bank-web\\display\" method=\"get\">");
            out.println("<div><input class='validate-button' type=\"submit\" value=\"Voir le résultat\"/></div>");
            out.println("</form>");

        } finally {
            if (out != null) {
                out.close();
            }
        }
        out.println("</main>");
        out.println("</body>");
        out.println("</html>");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void initAccount(final PrintWriter out) {
        try {
            initializerBean.initializeEntities();
        } catch (Exception e) {
            displayException(out, "Impossible d'initialiser la liste des comptes", e);
            return;
        }
    }

    private void displayException(final PrintWriter out, final String errMsg, final Exception e) {
        out.println("<div class='exception'>");
        out.println("<h2>Erreur</h2>");
        out.println("<p>" + errMsg + "</p>");
        out.println("<pre>");
        e.printStackTrace(out);
        out.println("</pre>");
        out.println("</div>");
    }
}
