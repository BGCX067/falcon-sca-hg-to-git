/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.sca.calontir.cmpe.data.AuthType;
import org.sca.calontir.cmpe.data.Authorization;
import org.sca.calontir.cmpe.data.Fighter;
import org.sca.calontir.cmpe.db.AuthTypeDAO;
import org.sca.calontir.cmpe.db.FighterDAO;

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
        response.setContentType("text/html;charset=UTF-8");
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        FighterDAO dao = new FighterDAO();
        AuthTypeDAO atDao = new AuthTypeDAO();
        
        Fighter fighter = new Fighter();
        
        fighter.setScaName(request.getParameter("scaName"));
        fighter.setScaMemberNo(request.getParameter("scaMemberNo"));
        List<Authorization> authorizations = new LinkedList<Authorization>();
        String[] authIds = request.getParameterValues("authorization");
        for(String authId : authIds) {
            int aid = Integer.parseInt(authId);
            AuthType at = atDao.getAuthType(aid);
            Authorization a = new Authorization();
            a.setAuthType(at.getAuthTypeId());
            a.setDate(new Date());
            authorizations.add(a);
        }
        fighter.setAuthorization(authorizations);
        dao.saveFighter(fighter);
        request.setAttribute("mode", "view");
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
