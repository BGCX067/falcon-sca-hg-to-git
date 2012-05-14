package org.sca.calontir.cmpe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.sca.calontir.cmpe.db.FighterDAO;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.print.CardMaker;
import org.sca.calontir.cmpe.user.Security;
import org.sca.calontir.cmpe.user.SecurityFactory;
import org.sca.calontir.cmpe.utils.FighterUpdater;

/**
 *
 * @author rik
 */
public class FighterServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Security security = SecurityFactory.getSecurity();

        FighterDAO dao = new FighterDAO();

        String fighterIdStr = request.getParameter("fighterId");
        int fighterId = (fighterIdStr == null || fighterIdStr.equalsIgnoreCase("null")) ? 0 : Integer.parseInt(fighterIdStr);
        Fighter fighter;
        if (fighterId > 0) {
            String mode = request.getParameter("mode");
            fighter = dao.getFighter(fighterId);
            System.out.println("Got fighter " + fighter.getFighterId() + ": " + fighter.getScaName());
            if (mode.startsWith("save")) {
                if (mode.equals("saveAuthorizations")) {
                    System.out.println("Saving auths");
                    fighter = FighterUpdater.authFromRequest(request, fighter);
                } else {
                    fighter = FighterUpdater.infoFromRequest(request, fighter);
                }
                try {
                    Long key = dao.saveFighter(fighter, security.getUser().getFighterId(), false);
                    fighter.setFighterId(key);
                } catch (ValidationException ex) {
                }
                mode = "view";
                request.setAttribute("mode", mode);
                request.setAttribute("fighter", fighter);
                request.setAttribute("uimessage", fighter.getScaName() + " saved");
//                this.getServletContext().getRequestDispatcher("/fighter.jsp").
//                        include(request, response);
            } else if (mode.equals("deleteFighter")) {
                System.out.println("Delete was called for " + fighter.getFighterId() + ": " + fighter.getScaName());
                dao.deleteFighter(fighter.getFighterId());
                request.setAttribute("uimessage", fighter.getScaName() + " deleted");
//                this.getServletContext().getRequestDispatcher("/index.jsp").
//                        include(request, response);
            } else if (mode.equals("printFighter")) {
                Fighter f = dao.getFighter(fighter.getFighterId());
                ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
                CardMaker cardMaker = new CardMaker();
                List<Fighter> flist = new ArrayList<Fighter>();
                flist.add(f);
                try {
                    cardMaker.build(baosPDF, flist, new DateTime(2012, 6, 1, 0, 0, 0, 0), new DateTime(2012, 8, 31, 0, 0, 0, 0));
                } catch (Exception ex) {
                    Logger.getLogger(FighterServlet.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException("Error building the cards", ex);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("attachment; filename=");
                sb.append("FighterCard ");
                sb.append(f.getScaName());
                sb.append(".pdf");
                response.setHeader("Content-disposition", sb.toString());
                response.setContentType("application/pdf");
                response.setContentLength(baosPDF.size());
                ServletOutputStream sos = response.getOutputStream();
                baosPDF.writeTo(sos);
                sos.flush();
                return;
            } else {
                request.setAttribute("mode", mode);
                request.setAttribute("fighter", fighter);
//                this.getServletContext().getRequestDispatcher("/fighter.jsp").
//                        include(request, response);
            }
        } else {
            fighter = FighterUpdater.fromRequest(request, new Fighter());
            boolean success = false;
            try {
                dao.saveFighter(fighter, security.getUser().getFighterId());
                request.setAttribute("mode", "view");
                success = true;
            } catch (ValidationException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                request.setAttribute("mode", "add");
                request.setAttribute("error", ex.getMessage());
                request.setAttribute("fighter", fighter);
//                this.getServletContext().getRequestDispatcher("/fighter.jsp").
//                        include(request, response);
            }
            if (success) {
                request.setAttribute("uimessage", fighter.getScaName() + " added");
//                this.getServletContext().getRequestDispatcher("/index.jsp").
//                        include(request, response);
            }
        }
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
           // /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Fighter Servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");

        } finally { 
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
