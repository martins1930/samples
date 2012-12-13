/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.foop.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.lang3.text.WordUtils;


public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        DataSource ds =null ;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null; 
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("jdbc/Atfsff");
            connection = ds.getConnection();
            ps = connection.prepareStatement("select id from posible");
            rs = ps.executeQuery();
            long aLong = 0L ;
            if (rs.next()) {
                aLong = rs.getLong(1);
            }
            String capital = WordUtils.capitalize("hoy capital123456");
            out.write("<h1>Hola Servlet222222_SCAN2345678901!!: "+capital+"   nombClass: "+aLong+"</h1>");
        } catch (SQLException ex) {
            Logger.getLogger(HelloServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(HelloServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(HelloServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    

}