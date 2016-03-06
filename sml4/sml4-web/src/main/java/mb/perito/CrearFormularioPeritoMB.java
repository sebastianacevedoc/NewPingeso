/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.perito;

import ejb.FormularioEJBLocal;
import ejb.UsuarioEJBLocal;
import ejb.ValidacionVistasMensajesEJBLocal;
import entity.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sebastian
 */
@Named(value = "crearFormularioPeritoMB")
@RequestScoped
@ManagedBean
public class CrearFormularioPeritoMB {

    @EJB
    private ValidacionVistasMensajesEJBLocal validacionVistasMensajesEJB;
    @EJB
    private UsuarioEJBLocal usuarioEJB;
    @EJB
    private FormularioEJBLocal formularioEJB;

    static final Logger logger = Logger.getLogger(CrearFormularioPeritoMB.class.getName());

    //Guardamos el usuario que inicia sesion
    private Usuario usuarioSesion;

    //Atributos del formulario
    private String ruc;
    private String rit;
    private int nue;
    private String nueS;
    private String cargo;
    private String delito;
    private String direccionSS;
    private String lugar;
    private String unidadPolicial;
    private String levantadaPor;
    private String rut;
    private Date fecha;
    private String observacion;
    private String descripcion;
    private int parte;
    private String parteS;

    private String motivo;

    //Guardamos la cuenta del usuario que entrego la vista del login
    private String usuarioSis;

    //Captura al usuario proveniente del inicio de sesión
    private HttpServletRequest httpServletRequest1;
    private FacesContext facesContext1;

    //Envio del nue
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;

    public CrearFormularioPeritoMB() {
        //logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "CrearFormularioPeritoMB");
        this.usuarioSesion = new Usuario();
        this.facesContext1 = FacesContext.getCurrentInstance();
        this.httpServletRequest1 = (HttpServletRequest) facesContext1.getExternalContext().getRequest();
        this.facesContext = FacesContext.getCurrentInstance();
        this.httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        if (httpServletRequest1.getSession().getAttribute("cuentaUsuario") != null) {
            this.usuarioSis = (String) httpServletRequest1.getSession().getAttribute("cuentaUsuario");
            logger.log(Level.FINEST, "Usuario recibido {0}", this.usuarioSis);
        }

        logger.exiting(this.getClass().getName(), "CrearFormularioPeritoMB");
    }

    @PostConstruct
    public void cargarDatos() {
        //logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "cargarDatosPerito");

        boolean falla = false;

        if (usuarioSis != null) { //verifica si falla la carga de los datos que pasan por parámetro
            this.usuarioSesion = usuarioEJB.findUsuarioSesionByCuenta(this.usuarioSis);
        } else {
            falla = true;
        }

        //si es un usario no permitido, o si está deshabilitado
        if (usuarioSesion == null || !(usuarioSesion.getCargoidCargo().getNombreCargo().equals("Perito") || usuarioSesion.getCargoidCargo().getNombreCargo().equals("Tecnico")) || usuarioSesion.getEstadoUsuario() == false) {
            falla = true;
        }

        //en caso de falla, redireccionamos a la página de inicio de sesión
        if (falla == true) {
            try {
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext exc = fc.getExternalContext();
                String uri = exc.getRequestContextPath();
                exc.redirect(uri + "/faces/indexListo.xhtml");
            } catch (Exception e) {
                System.out.println("POST CONSTRUCTOR FALLO");
            }
        }

        this.cargo = this.usuarioSesion.getCargoidCargo().getNombreCargo();
        this.levantadaPor = this.usuarioSesion.getNombreUsuario() + " " + this.usuarioSesion.getApellidoUsuario();
        this.rut = this.usuarioSesion.getRutUsuario();

        GregorianCalendar c = new GregorianCalendar();
        this.fecha = c.getTime();

        logger.exiting(this.getClass().getName(), "cargarDatosPerito");
    }

    public String iniciarFormulario() {
        //logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "iniciarFormularioPerito");

        String resultado = formularioEJB.crearFormulario(motivo, ruc, rit, nue, parte, delito, direccionSS, lugar, fecha, observacion, descripcion, unidadPolicial, usuarioSesion);
        //Enviando nue
        httpServletRequest.getSession().setAttribute("nueF", this.nue);
        //Enviando usuario
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);

        if (resultado.equals("Exito")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, resultado, "Datos exitosos"));
            logger.exiting(this.getClass().getName(), "iniciarFormularioPerito", "formularioCreadoPerito");
            return "formularioCreadoPerito?faces-redirect=true";
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resultado, "Datos no válidos"));
        logger.exiting(this.getClass().getName(), "iniciarFormularioPerito", "");
        return "";
    }

    //redirecciona a la pagina para realizar una busqueda
    public String buscar() {
        //logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "buscar");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.exiting(this.getClass().getName(), "buscar", "buscadorPerito");
        return "buscadorPerito?faces-redirect=true";
    }

    //redirecciona a la pagina para iniciar cadena de custodia
    public String iniciarCadena() {
        //logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "iniciarCadena");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.exiting(this.getClass().getName(), "iniciarCadena", "peritoFormulario");
        return "peritoFormulario?faces-redirect=true";
    }

    public String salir() {
        //logger.setLevel(Level.ALL);
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "salirPerito");
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        httpServletRequest1.removeAttribute("cuentaUsuario");
        logger.exiting(this.getClass().getName(), "salirPerito", "/indexListo");
        return "/indexListo?faces-redirect=true";
    }

    public void validarDLU(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String cadena = (String) value;
        if (!cadena.equals("")) {

            String mensaje = validacionVistasMensajesEJB.verificarCaracteresInitFin(cadena);
            if (!mensaje.equals("Exito")) {
                ((UIInput) toValidate).setValid(false);
                context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
            }
        }
    }

    public void validarDelito(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String delito1 = (String) value;
        if (!delito1.equals("")) {

            String mensaje = validacionVistasMensajesEJB.verificarInitFinCarac(delito1);
            if (!mensaje.equals("Exito")) {
                ((UIInput) toValidate).setValid(false);
                context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
            }
        }
    }

    public void validarRuc(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        if (!texto.equals("")) {

            String mensaje = validacionVistasMensajesEJB.checkRucE(texto);
            if (!mensaje.equals("Exito")) {
                ((UIInput) toValidate).setValid(false);
                context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
            }
        }
    }

    public void validarRit(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        if (!texto.equals("")) {
            String mensaje = validacionVistasMensajesEJB.checkRitE(texto);
            if (!mensaje.equals("Exito")) {
                ((UIInput) toValidate).setValid(false);
                context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
            }
        }
    }

    public void validarNParte(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        String mensaje = "";

        if (texto.equals("")) {
            mensaje = "Exito";
        } else {

            try {
                parte = Integer.parseInt(texto);
                mensaje = validacionVistasMensajesEJB.checkParte(parte);
            } catch (NumberFormatException e) {

                if (!texto.equals("")) {
                    mensaje = "N° Parte erróneo";
                }
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
            }
        }

        if (!mensaje.equals("Exito")) {
            ((UIInput) toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
        }
    }

    public void validarNue(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        String mensaje = "";
        try {
            nue = Integer.parseInt(texto);
            if (nue > 0) {
                mensaje = validacionVistasMensajesEJB.existeNue(nue);
            } else {
                mensaje = "N.U.E. erróneo";
            }

        } catch (NumberFormatException e) {

            if (!texto.equals("")) {
                mensaje = "N.U.E. erróneo";
            } else {
                mensaje = "Debe ingresar N.U.E.";
            }
        }

        if (!mensaje.equals("Exito")) {
            ((UIInput) toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
        }
    }

    public void validarDelitoRef(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        if (!texto.equals("")) {
            String mensaje = validacionVistasMensajesEJB.validarDelitoRef(texto);
            if (!mensaje.equals("Exito")) {
                ((UIInput) toValidate).setValid(false);
                context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
            }
        }
    }

    public Usuario getUsuarioSesion() {
        return usuarioSesion;
    }

    public void setUsuarioSesion(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRit() {
        return rit;
    }

    public void setRit(String rit) {
        this.rit = rit;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDelito() {
        return delito;
    }

    public void setDelito(String delito) {
        this.delito = delito;
    }

    public String getDireccionSS() {
        return direccionSS;
    }

    public void setDireccionSS(String direccionSS) {
        this.direccionSS = direccionSS;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getUnidadPolicial() {
        return unidadPolicial;
    }

    public void setUnidadPolicial(String unidadPolicial) {
        this.unidadPolicial = unidadPolicial;
    }

    public String getLevantadaPor() {
        return levantadaPor;
    }

    public void setLevantadaPor(String levantadaPor) {
        this.levantadaPor = levantadaPor;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsuarioSis() {
        return usuarioSis;
    }

    public void setUsuarioSis(String usuarioSis) {
        this.usuarioSis = usuarioSis;
    }

    public int getNue() {
        return nue;
    }

    public void setNue(int nue) {
        this.nue = nue;
    }

    public int getParte() {
        return parte;
    }

    public void setParte(int parte) {
        this.parte = parte;
    }

    public String getNueS() {
        return nueS;
    }

    public void setNueS(String nueS) {
        this.nueS = nueS;
    }

    public String getParteS() {
        return parteS;
    }

    public void setParteS(String parteS) {
        this.parteS = parteS;
    }

}
