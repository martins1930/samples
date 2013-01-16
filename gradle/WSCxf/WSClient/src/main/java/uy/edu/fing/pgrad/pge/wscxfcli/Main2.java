/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.edu.fing.pgrad.pge.wscxfcli;

import javax.xml.soap.SOAPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uy.edu.fing.pgrad.pge.wscxf.Hello;
import uy.edu.fing.test.data.DataDocumento;
import uy.edu.fing.test.data.DatosPersona;
import uy.edu.fing.test.data.PersonaDTO;
import uy.red.externo.serv.bps.ConsultaBPS;
import uy.red.externo.serv.dnic.ConsultaDNIC;

public class Main2 {

    private static final Logger log = LoggerFactory.getLogger(Main2.class);
    
    public static void main(String[] args) throws SOAPException {
        log.info("test de dnic y bps");
        
        ApplicationContext ctx = new ClassPathXmlApplicationContext("client-bean.xml");
        
        //----------------------------
        // DNIC
        ConsultaDNIC dnicS = (ConsultaDNIC)  ctx.getBean("consultaDnicClient");
        DataDocumento d = new DataDocumento();
        d.setNro_documento("1234567");
        d.setPais_documento("UY");
        d.setTipo_documento("DO");
        DatosPersona datP = dnicS.consultarDatos(d);
        log.info("El resultado desde el ws es: {}",datP);
        
        //------------------------
        // BPS
        ConsultaBPS bpsS = (ConsultaBPS) ctx.getBean("consultaBpsClient");
        
        PersonaDTO pdto = new PersonaDTO();
        pdto.setFec_nac(datP.getFec_nac());
        pdto.setNro_doc(datP.getCi());
        pdto.setPais_doc(d.getPais_documento());
        pdto.setTipo_doc(d.getTipo_documento());
        
        Integer saldoBPS = bpsS.consultarSaldo(pdto);
        log.info("el saldo es: {}",saldoBPS);
    }
    
}
