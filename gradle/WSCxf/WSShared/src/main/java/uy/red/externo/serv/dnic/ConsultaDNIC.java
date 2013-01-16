/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.red.externo.serv.dnic;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.soap.SOAPException;
import uy.edu.fing.test.data.DataDocumento;
import uy.edu.fing.test.data.DatosPersona;

@WebService
public interface ConsultaDNIC {

    @WebMethod(operationName = "consultarDatos", action = "act_consultarDatos")
    public DatosPersona consultarDatos(@WebParam(name = "documento") DataDocumento d) throws SOAPException;    
}
