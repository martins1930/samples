/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.red.externo.serv.dnic;

import java.util.HashMap;
import java.util.Map;
import javax.jws.WebService;
import javax.xml.soap.SOAPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uy.edu.fing.test.data.DataDocumento;
import uy.edu.fing.test.data.DatosPersona;

@WebService(endpointInterface="uy.red.externo.serv.dnic.ConsultaDNIC")
public class ConsultaDNICImpl implements ConsultaDNIC {

    private static final Logger log = LoggerFactory.getLogger(ConsultaDNICImpl.class);

    private Map<String, DatosPersona> per_sistema;

    @Override
    public DatosPersona consultarDatos(DataDocumento d) throws SOAPException {
        log.info("el documento obtenido es: {}", d);
        DatosPersona dataPersona = per_sistema.get(d.getNro_documento());
        if (dataPersona == null) {
            dataPersona = new DatosPersona();
            dataPersona.setSuceso(0);
        }
        return dataPersona;
    }
    

    public ConsultaDNICImpl() {
        per_sistema = new HashMap<String, DatosPersona>();
        DatosPersona dp1 = new DatosPersona();
        dp1.setCi("1234567");
        dp1.setNombre("ALCIDES EDGARDO");
        dp1.setEdad(80);
        dp1.setLugar_nac("LA PAZ");
        //ANIOS
        dp1.setFec_nac("1932-12-10");
        per_sistema.put(dp1.getCi(), dp1);


        // OTRA -------------------------------------------------------------
        DatosPersona dp2 = new DatosPersona();
        dp2.setCi("2111222");
        dp2.setNombre("CACHO CRUZ");
        dp2.setEdad(60);
        dp2.setLugar_nac("MONTEVIDEO");

        //ANIOS
        dp2.setFec_nac("1950-12-10");
        per_sistema.put(dp2.getCi(), dp2);

    }
}
