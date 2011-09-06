package org.sca.calontir.cmpe;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.db.FighterDAO;

/**
 * Servlet called from the fighter search box.
 *
 * @author rik
 */
public class FighterSearchServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Fighter fighter = null;
        FighterDAO dao = new FighterDAO();
        String mode = request.getParameter("mode");
        if (mode != null && mode.equals("add")) {
            fighter = new Fighter();
        } else if(mode != null && mode.equals("lookup")) {
            String idStr = request.getParameter("fid");
            Long id = Long.valueOf(idStr);
            fighter = dao.getFighter(id);
        } else {
            String search = request.getParameter("search");

            List<Fighter> ret = null;
            
            if(!StringUtils.isBlank(search))
                ret = dao.queryFightersByScaName(search);

            if (ret !=null && ret.isEmpty()) {
                fighter = null;
            } else {
                if (ret !=null && ret.size() == 1) {
                    fighter = ret.get(0);
                } else {
                    if(ret == null)
                        ret = dao.getFighters();
                    request.setAttribute("fighters", ret);
                    this.getServletContext().getRequestDispatcher("/fighterList.jsp").
                            include(request, response);
                    return;
                }
            }
        }
        if (fighter == null) {
            request.setAttribute("error", "No fighters were found, please retry your search");
            this.getServletContext().getRequestDispatcher("/index.jsp").
                    include(request, response);
        } else {
            request.setAttribute("mode", mode);
            request.setAttribute("fighter", fighter);
            //response.sendRedirect("/fighter.jsp");
            this.getServletContext().getRequestDispatcher("/fighter.jsp").
                    include(request, response);
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
