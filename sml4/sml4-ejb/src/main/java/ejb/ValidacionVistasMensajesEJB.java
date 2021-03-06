/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Aracelly
 */
@Stateless
public class ValidacionVistasMensajesEJB implements ValidacionVistasMensajesEJBLocal {

    @EJB
    private ValidacionEJBLocal validacionEJB;

    @Override
    public String checkFecha(Date fecha) {
        Date fechaActual = new Date();

        if (fecha.after(fechaActual)) {

            return "No puede ser superior a la fecha actual";
        }
      
        return "Exito";
    }
    
    

    @Override
    public String checkRucE(String ruc) {
        if (!validacionEJB.checkRucOrRit(ruc)) {
            //System.out.println("Error ruc ------> " + ruc);
            return "R.U.C. erróneo";
        }
        if (!validacionEJB.cantCaract(ruc)) {
            return "No puede exceder los 45 caracteres";
        }

        return "Exito";
    }

    @Override
    public String checkRitE(String rit) {
        if (rit == null || rit.equals("") || !validacionEJB.checkRucOrRit(rit)) {
            return "R.I.T. erróneo";
        }
        if (!validacionEJB.cantCaract(rit)) {
            return "No puede exceder los 45 caracteres";
        }
        return "Exito";
    }

    @Override
    public String checkParte(int numeroParte) {

        if (numeroParte < 1) {
            return "N° Parte erróneo";
        }
        return "Exito";

    }

    @Override
    public String checkRut(String rut) {
        if (!validacionEJB.val(rut)) {
            return "R.U.N. erróneo";
        }
        return "Exito";
    }

    @Override
    public String soloCaracteres(String dato) {
        if (dato == null || dato.equals("") || validacionEJB.soloCaracteres(dato) == false) {
            return "Debe ingresar solo caracteres.";
        }
        return "Exito";
    }

    @Override
    public String validarCuentaUsuario(String cuenta) {

        int largoCuenta = cuenta.length();
        char espacio = 32;

        if (!validacionEJB.cantCaract2(cuenta)) {
            return "No puede exceder los 30 caracteres";

        }
        
        if (largoCuenta < 0) {
            return "Debe ingresar cuenta de usuario";
        }

        for (int i = 0; i < largoCuenta; i++) {
            if (cuenta.charAt(i) == espacio) {
                return "No puede contener espacios";
            }
        }
        if (validacionEJB.validarCuentaUsuario(cuenta) == false) {
            return "Cuenta ya registrada";
        }
        
        return "Exito";
    }

    @Override
    public String checkCorreo(String correo) {

        int largoCuenta = correo.length();
        char espacio = 32;

        if (largoCuenta < 0) {
            return "Debe ingresar un correo";
        }

        for (int i = 0; i < largoCuenta; i++) {
            if (correo.charAt(i) == espacio) {
                return "No puede contener espacios";
            }
        }

        if (validacionEJB.validarEmail(correo) == false) {
            return "Correo erróneo";
        }
        return "Exito";
    }

    @Override
    public String validarCorreo(String correo) {

        int largoCuenta = correo.length();
        char espacio = 32;

        if (!validacionEJB.cantCaract(correo)) {
            return "No puede exceder los 45 caracteres";
        }
        
        if (largoCuenta < 0) {
            return "Debe ingresar un correo";
        }

        for (int i = 0; i < largoCuenta; i++) {
            if (correo.charAt(i) == espacio) {
                return "No puede contener espacios";
            }
        }

        if (validacionEJB.correoExiste(correo) == true) {
            return "Correo ya registrado";
        }
        return "Exito";
    }

    @Override
    public String validarRut(String rut) {

        int largoCuenta = rut.length();
        char espacio = 32;

        if (largoCuenta < 0) {
            return "Debe ingresar un R.U.N.";
        }

        if (!validacionEJB.val(rut)) {
            return "R.U.N. erróneo";
        }

        if (validacionEJB.rutExiste(rut) == true) {
            return "R.U.N. ya registrado";
        }
        return "Exito";
    }

    @Override
    public String validarDelitoRef(String delito) {
        if (validacionEJB.soloCaracteres(delito) == false) {
            return "Debe ingresar solo caracteres";
        }
        return "Exito";
    }

    @Override
    public String existeNue(int nue) {
        if (validacionEJB.nueExiste(nue) == true) {
            return "N.U.E. ya registrado";
        }
        return "Exito";
    }

    @Override
    public String verificarInitFinSoloCaracteres(String texto) {
        int largoCadena = texto.length();

        if (!validacionEJB.cantCaract(texto)) {
            return "No puede exceder los 45 caracteres";
        }

        if (texto.charAt(0) == 32) {
            return "No puede contener espacio al comienzo";
        }

        if (texto.charAt(largoCadena - 1) == 32) {
            return "No puede contener espacio al final";
        }

        if (validacionEJB.soloCaracteres(texto) == false) {
            return "Debe ingresar solo caracteres";
        }
        return "Exito";
    }

    @Override
    public String verificarInitFin(String texto) {

        int largoCadena = texto.length();

        if (texto.charAt(0) == 32) {
            return "No puede contener espacio al comienzo";
        }

        if (texto.charAt(largoCadena - 1) == 32) {
            return "No puede contener espacio al final";
        }
        if (!validacionEJB.cantCaract(texto)) {
            return "No puede exceder los 45 caracteres";
        }
        return "Exito";

    }

    @Override
    public String verificarObservacion(String texto) {
        if (!validacionEJB.cantCaract3(texto)) {
            return "No puede exceder los 300 caracteres";
        }
        return "Exito";
    }
    
    @Override
    public String verificarEdicion(String texto){
        if(!validacionEJB.cantCaract4(texto)){
            return "No puede exceder los 400 caracteres";
        }        
        return "Exito";
    }    

    @Override
    public String checkFechaTraslado(Date fecha, Date traslado) {
        
        //fecha actual no debe ser anterior al ultimo traslado
        if (traslado.after(fecha)) {

            return "No puede ser anterior al último traslado";
        }
      
        return "Exito";
    }
    
}
