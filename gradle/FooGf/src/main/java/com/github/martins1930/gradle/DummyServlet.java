/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.martins1930.gradle;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.text.WordUtils;

@WebServlet(name="dummys", urlPatterns={"/dummys"})
public class DummyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String capitalizedText = "hi this text is capitalized" ;
            capitalizedText = WordUtils.capitalize(capitalizedText);
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Dummy</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Dummy!! " + capitalizedText + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }        
    }

    
    
}
