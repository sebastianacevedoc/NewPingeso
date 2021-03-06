/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.Formulario;
import entity.Usuario;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author sebastian
 */
@Local
public interface FormularioDigitadorLocal {

    public String crearFormulario(String rut, String ruc, String rit, int nue, int nParte, String delito, String direccionSS, String lugar, String unidadPolicial, Date fecha, String observacion, String descripcion, Usuario digitador);

    public String crearTraslado(Formulario formulario, Usuario usuarioInicia, String usuarioRecibeRut, Date fechaT, String observaciones, String motivo);
}
