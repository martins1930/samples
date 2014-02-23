/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.martins1930.gradle;

import com.github.martins1930.gradle.ejb.Echo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.text.WordUtils;

@WebServlet(name="dummys", urlPatterns={"/dummys"})
public class DummyServlet extends HttpServlet {
    
    @EJB
    private Echo sayecho; 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String capitalizedText = "hi this text is capitalized" ;
            capitalizedText = WordUtils.capitalize(capitalizedText);
            
            URL resourceText = Thread.currentThread().getContextClassLoader().getResource("dummyf.txt");
            
            String textContent = "";
            if (resourceText!=null) {
                File fileText = new File(resourceText.getPath());
                FileReader fileReader = new FileReader(fileText);
                BufferedReader buffReader = new BufferedReader(fileReader);

                String lineText = buffReader.readLine() ;
                while (lineText!=null) {                    
                    textContent += lineText  ;
                    lineText = buffReader.readLine();
                }
                
            }
            else {
                textContent = "emptyFile ";
            }
            
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Dummy</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Dummy1!! "+ textContent + capitalizedText + "</h1>");
            out.println("<h1>Servlet Dummy1!! "+ sayecho.echo(textContent) + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } catch (NamingException ex) {
            Logger.getLogger(DummyServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DummyServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {            
            out.close();
        }        
    }

    
    
}
