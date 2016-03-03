/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import static ejb.FormularioEJB.logger;
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

            return "Error con fecha ingresada, no puede ser superior a la fecha actual";
        }
        return "Exito";
    }

    @Override
    public String checkRuc(String ruc) {

        if (ruc.equals("")) {
            return "Debe ingresar el RUC para realizar la búsqueda";
        }
        if (ruc == null || !validacionEJB.checkRucOrRit(ruc)) {
            System.out.println("Error ruc ------> " + ruc);
            return "El RUC ingresado es erróneo ";
        }
        System.out.println("RUC ------> " + ruc);
        return "Exito";
    }

    @Override
    public String checkRit(String rit) {

        if (rit.equals("")) {
            return "El RIT ingresado es erróneo";
        }
        if (rit == null || !validacionEJB.checkRucOrRit(rit)) {
            return "Debe ingresar un R.I.T. válido";
        }
        return "Exito";
    }

    @Override
    public String checkRucE(String ruc) {
        if (!validacionEJB.checkRucOrRit(ruc)) {
            System.out.println("Error ruc ------> " + ruc);
            return "R.U.C. erróneo";
        }

        return "Exito";
    }

    @Override
    public String checkRitE(String rit) {
        if (rit == null || rit.equals("") || !validacionEJB.checkRucOrRit(rit)) {
            return "R.I.T. erróneo";
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
            return "Debe ingresar un R.U.N";
        }

        for (int i = 0; i < largoCuenta; i++) {
            if (rut.charAt(i) == espacio) {
                return "No puede contener espacios";
            }
        }

        if (validacionEJB.rutExiste(rut) == true) {
            return "R.U.N. ya registrado";
        }
        return "Exito";
    }

    @Override
    public String validarFechaDigitador(Date fecha) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
