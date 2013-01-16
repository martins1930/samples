/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.edu.fing.pgrad.pge.wscxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface Calculator {
    
    @WebMethod(operationName="sum")
    public Integer sum(@WebParam(name="a") Integer a, @WebParam(name="b")Integer b);
}
