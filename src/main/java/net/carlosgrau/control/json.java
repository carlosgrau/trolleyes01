/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.carlosgrau.control;

import com.google.gson.Gson;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author a021792876p
 */
public class json extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/jso;charset=UTF-8");
        //response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            Gson gson = new Gson();
            String json;
            String status = "";
            String msg = "";
            try {

                HttpSession oSession = request.getSession();
                String strOp = request.getParameter("op");
                String strPass = "";
                String strUser = "";

                if (strOp != null) {

                    if (strOp.equalsIgnoreCase("conectar")) {
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                        } catch (Exception e) {
                            response.setStatus(500);
                            status = "500";
                            msg = "Error at library";
                            json = "";
                            //json = "{\"status\":" + status + ",\"msg\":\"" + msg + "\"}";
                            //out.print(json);
                        }
                        //C3P0 CONNECTION FUNCIONA 100%
                        ComboPooledDataSource cpds = new ComboPooledDataSource();
                        cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver            
                        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/trolleyes");
                        cpds.setUser("root");
                        cpds.setPassword("bitnami");
                        cpds.setMaxPoolSize(50);
                        //HIKARI CONNECTION FUNCIONA 100%
//                        HikariConfig config = new HikariConfig();
//                        config.setJdbcUrl("jdbc:mysql://localhost:3306/trolleyes");
//                        config.setUsername("root");
//                        config.setPassword("bitnami");
//                        config.addDataSourceProperty("cachePrepStmts", "true");
//                        config.addDataSourceProperty("prepStmtCacheSize", "250");
//                        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

                        try {
                            //HikariDataSource oConnectioPool = new HikariDataSource(config);
                            Connection oConnection = (Connection) cpds.getConnection();
                            oConnection.close();
                            response.setStatus(200);
                            status = "200";
                            msg = "Connection OK";
                            json = "";
                            //json = "{\"status\":" + status + ",\"msg\":\"" + msg + "\"}";
                            //out.print(json);

                        } catch (SQLException e) {
                            response.setStatus(500);
                            status = "500";
                            msg = "Connection refused";
                            json = "";
                            //json = "{\"status\":" + status + ",\"msg\":\"" + msg + "\"}";
                            //out.print(json);
                        }

                    }
                    strPass = request.getParameter("pass");
                    strUser = request.getParameter("user");
                    if (strOp.equalsIgnoreCase("login")) {
                        if (strUser.equalsIgnoreCase("carlos") && strPass.equalsIgnoreCase("carlos")) {
                            oSession.setAttribute("sesion", strUser);
                            response.setStatus(200);
                            status = "200";
                            msg = "Validación correcta ->" + strUser;

                        } else {
                            response.setStatus(401);
                            status = "401";
                            msg = "Validación incorrecta";
                        }
                    }

                    if (strOp.equalsIgnoreCase("logout")) {
                        oSession.invalidate();
                        response.setStatus(200);
                        status = "200";
                        msg = "LOGOUT correcto";

                    }
                    if (strOp.equalsIgnoreCase("check")) {
                        String usuariolog = (String) oSession.getAttribute("sesion");
                        if (usuariolog != null) {
                            response.setStatus(200);
                            status = "200";
                            msg = "Estas comprobando y todavia estas logeado";

                        }

                    }
                    if (strOp.equalsIgnoreCase("secreto")) {
                        response.setStatus(200);
                        status = "200";
                        msg = "El secreto es: SECRETO ESCONDIDO";
                    }
                    json = "{\"status\":" + status + ",\"msg\":\"" + msg + "\"}";
                    out.print(json);
                } else {
                    response.setStatus(500);
                    status = "500";
                    msg = "El operador es Null y no puede serlo";
                    json = "{\"status\":" + status + ",\"msg\":\"" + msg + "\"}";
                    out.print(json);

                }
            } catch (Exception e) {
                response.setStatus(401);
                status = "401";
                msg = "La operación que ha introducido no es correcta.";
                json = "{\"status\":" + status + ",\"msg\":\"" + msg + "\"}";
                out.print(json);

            }
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
