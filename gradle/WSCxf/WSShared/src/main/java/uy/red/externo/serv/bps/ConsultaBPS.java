/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.red.externo.serv.bps;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.soap.SOAPException;
import uy.edu.fing.test.data.PersonaDTO;

@WebService
public interface ConsultaBPS {

    @WebMethod(operationName = "consultarSaldo", action = "act_consultarSaldo")
    public Integer consultarSaldo(@WebParam(name = "persdto") PersonaDTO p) throws SOAPException;
}
