/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.edu.fing.test.data;

public class PersonaDTO {

    private String tipo_doc;
    private String pais_doc;
    private String nro_doc;
    private String fec_nac;

    public PersonaDTO() {
    }

    public String getTipo_doc() {
        return tipo_doc;
    }

    public void setTipo_doc(String tipo_doc) {
        this.tipo_doc = tipo_doc;
    }

    public String getPais_doc() {
        return pais_doc;
    }

    public void setPais_doc(String pais_doc) {
        this.pais_doc = pais_doc;
    }

    public String getNro_doc() {
        return nro_doc;
    }

    public void setNro_doc(String nro_doc) {
        this.nro_doc = nro_doc;
    }

    public String getFec_nac() {
        return fec_nac;
    }

    public void setFec_nac(String fec_nac) {
        this.fec_nac = fec_nac;
    }

    @Override
    public String toString() {
        return "PersonaDTO{" + "tipo_doc=" + tipo_doc + ", pais_doc=" + pais_doc + ", nro_doc=" + nro_doc + ", fec_nac=" + fec_nac + '}';
    }
    
    
}
