package org.sca.calontir.cmpe;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jdo.Transaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.db.AuthTypeDAO;
import org.sca.calontir.cmpe.db.FighterDAO;
import org.sca.calontir.cmpe.db.PMF;
import org.sca.calontir.cmpe.db.ScaGroupDAO;
import org.sca.calontir.cmpe.utils.FighterUpdater;

/**
 *
 * @author rik
 */
public class FighterServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

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
                    fighter = FighterUpdater.authFromRequest(request, fighter);
                } else {
                    fighter = FighterUpdater.infoFromRequest(request, fighter);
                }
                try {
                    Long key = dao.saveFighter(fighter, false);
                    fighter.setFighterId(key);
                } catch (ValidationException ex) {
                }
                mode = "view";
            }
            request.setAttribute("mode", mode);
        } else {
            fighter = new Fighter();

            fighter = FighterUpdater.fromRequest(request, fighter);
            try {
                dao.saveFighter(fighter);
                request.setAttribute("mode", "view");
            } catch (ValidationException ex) {
                Logger.getLogger(FighterServlet.class.getName()).log(Level.SEVERE, null, ex);
                String mode = request.getParameter("mode");
                request.setAttribute("mode", "add");
                request.setAttribute("error", ex.getMessage());
            }
           
        }
        request.setAttribute("fighter", fighter);
        //response.sendRedirect("/fighter.jsp");
        this.getServletContext().getRequestDispatcher("/fighter.jsp").
                include(request, response);

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
