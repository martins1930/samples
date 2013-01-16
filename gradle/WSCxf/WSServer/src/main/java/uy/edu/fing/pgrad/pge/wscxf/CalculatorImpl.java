/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.edu.fing.pgrad.pge.wscxf;

import javax.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebService(endpointInterface="uy.edu.fing.pgrad.pge.wscxf.Calculator")
public class CalculatorImpl implements Calculator {

    private static final Logger log = LoggerFactory.getLogger(CalculatorImpl.class);
    
    @Override
    public Integer sum(Integer a, Integer b) {
        log.info("los parametros recibidos son: {}, {}",a,b);
        return a+b;
    }
    
}
