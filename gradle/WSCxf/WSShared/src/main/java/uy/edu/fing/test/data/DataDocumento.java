/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.edu.fing.test.data;

public class DataDocumento {

    private String tipo_documento;
    private String pais_documento;
    private String nro_documento;

    public DataDocumento() {
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getPais_documento() {
        return pais_documento;
    }

    public void setPais_documento(String pais_documento) {
        this.pais_documento = pais_documento;
    }

    public String getNro_documento() {
        return nro_documento;
    }

    public void setNro_documento(String nro_documento) {
        this.nro_documento = nro_documento;
    }
    
    @Override
    public String toString() {
        return "DataDocumento{" + "tipo_documento=" + tipo_documento + ", pais_documento=" + pais_documento + ", nro_documento=" + nro_documento + '}';
    }
}
