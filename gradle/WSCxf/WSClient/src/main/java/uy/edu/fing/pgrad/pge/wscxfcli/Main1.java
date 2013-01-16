/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.edu.fing.pgrad.pge.wscxfcli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uy.edu.fing.pgrad.pge.wscxf.Calculator;
import uy.edu.fing.pgrad.pge.wscxf.Hello;

public class Main1 {

    private static final Logger log = LoggerFactory.getLogger(Main1.class);
    
    public static void main(String[] args) {
        ApplicationContext acx = new ClassPathXmlApplicationContext("client-bean.xml");
        Hello server = (Hello)  acx.getBean("helloClient");
        String retSer = server.sayHi("Test1");
        log.info("El resultado desde el ws es: {}",retSer);
        
        // -------------------- test para la suma con firma
//        Calculator serverCalc = (Calculator)  acx.getBean("calcClient");
//        Integer result = serverCalc.sum(100, 50);
//        log.info("El resultado desde el ws     Calculator es: {}",result);
          
    }
}
