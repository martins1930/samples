/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.edu.fing.test.data;

public class DatosPersona {

    private Integer suceso;
    private String ci;
    private String nombre;
    private Integer edad;
    private String fec_nac;
    private String lugar_nac;

    public DatosPersona() {
        suceso = 1;
    }

    public Integer getSuceso() {
        return suceso;
    }

    public void setSuceso(Integer suceso) {
        this.suceso = suceso;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getFec_nac() {
        return fec_nac;
    }

    public void setFec_nac(String fec_nac) {
        this.fec_nac = fec_nac;
    }

    public String getLugar_nac() {
        return lugar_nac;
    }

    public void setLugar_nac(String lugar_nac) {
        this.lugar_nac = lugar_nac;
    }

    @Override
    public String toString() {
        return "DatosPersona{" + "suceso=" + suceso + ", ci=" + ci + ", nombre=" + nombre + ", edad=" + edad + ", fec_nac=" + fec_nac + ", lugar_nac=" + lugar_nac + '}';
    }
    
}
