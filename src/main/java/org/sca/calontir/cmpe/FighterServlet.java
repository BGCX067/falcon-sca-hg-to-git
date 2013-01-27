package org.sca.calontir.cmpe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.sca.calontir.cmpe.db.FighterDAO;
import org.sca.calontir.cmpe.db.PropertiesDao;
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
            if (mode.startsWith("save")) {
                if (mode.equals("saveAuthorizations")) {
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
				PropertiesDao propDao = new PropertiesDao();
				String start = propDao.getProperty("calontir.validStart");
				String end = propDao.getProperty("calontir.validEnd");
				DateTime startDt = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime(start);
				DateTime endDt = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime(end);
                try {
                    cardMaker.build(baosPDF, flist, startDt, endDt);
                } catch (Exception ex) {
                    Logger.getLogger(FighterServlet.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException("Error building the cards", ex);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("attachment; filename=");
                sb.append("FighterCard_");
                sb.append(f.getScaName());
				sb.append("_");
				sb.append(String.format("%tF", new Date()));
                sb.append(".pdf");
				String fn = sb.toString().replaceAll(" ", "_");
				fn = fn.replaceAll("-", "_");
                response.setHeader("Content-disposition", fn);
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
