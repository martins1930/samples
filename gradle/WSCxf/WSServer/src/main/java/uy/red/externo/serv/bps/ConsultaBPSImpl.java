/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.red.externo.serv.bps;

import javax.jws.WebService;
import javax.xml.soap.SOAPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uy.edu.fing.test.data.PersonaDTO;

@WebService(endpointInterface="uy.red.externo.serv.bps.ConsultaBPS")
public class ConsultaBPSImpl implements ConsultaBPS {

    private static final Logger log = LoggerFactory.getLogger(ConsultaBPSImpl.class);

    @Override
    public Integer consultarSaldo(PersonaDTO p) throws SOAPException {
        log.info("la persona obtenida es: {}", p);
        Integer ret_saldo = 0;
        if (p != null) {
            String nro_doc = p.getNro_doc();
            if (nro_doc != null && nro_doc.equals("1234567")) {
                ret_saldo = 5000;
            }
        }
        return ret_saldo;
    }
}
