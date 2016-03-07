/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author Aracelly
 */
@Local
public interface ValidacionVistasMensajesEJBLocal {

    public String verificarEdicion(String texto);

    //public String checkRuc(String ruc);

   // public String checkRit(String rit);

    public String checkRucE(String ruc);

    public String checkRitE(String rit);

    public String checkFecha(Date fecha);
    
    public String checkFechaTraslado(Date fecha, Date traslado);

    public String checkCorreo(String correo);

    public String checkParte(int numeroParte);

    public String checkRut(String rut);

    public String soloCaracteres(String dato);

    public String validarCuentaUsuario(String cuenta);

    public String validarCorreo(String correo);

    public String validarRut(String rut);

    public String verificarInitFinSoloCaracteres(String direccion);

    public String verificarInitFin(String direccion);

    public String validarDelitoRef(String delito);

    public String existeNue(int nue);

    public String verificarObservacion(String texto);
}
