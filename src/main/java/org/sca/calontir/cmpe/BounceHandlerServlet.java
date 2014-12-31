package org.sca.calontir.cmpe;

import com.google.appengine.api.mail.BounceNotification;
import com.google.appengine.api.mail.BounceNotificationParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rikscarborough
 */
public class BounceHandlerServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(BounceHandlerServlet.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String line = new String();
        try {
            BounceNotification bounce = BounceNotificationParser.parse(request);
            line += "Original Message: \n";
            line += "From: " + bounce.getOriginal().getFrom() + "\n";
            line += "To: " + bounce.getOriginal().getTo() + "\n";
            line += "Subject: " + bounce.getOriginal().getSubject() + "\n";
            line += "Text: " + bounce.getOriginal().getText() + "\n";
            line += "Notification Message: \n";
            line += "From: " + bounce.getNotification().getFrom() + "\n";
            line += "To: " + bounce.getNotification().getTo() + "\n";
            line += "Subject: " + bounce.getNotification().getSubject() + "\n";
            line += "Text: " + bounce.getNotification().getText() + "\n";
            log.log(Level.INFO, line);
        } catch (MessagingException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        line = line.replaceAll("\n", "<br>");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BounceHandlerServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + line + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
