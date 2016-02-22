/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.digitador;

import ejb.FormularioDigitadorLocal;
import ejb.FormularioEJBLocal;
import ejb.UsuarioEJBLocal;
import ejb.ValidacionVistasMensajesEJBLocal;
import entity.Formulario;
import entity.FormularioEvidencia;
import entity.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Aracelly
 */
@Named(value = "forTrasladoMB")
@RequestScoped
@ManagedBean
public class ForTrasladoMB {

    @EJB
    private ValidacionVistasMensajesEJBLocal validacionVistasMensajesEJB;

    @EJB
    private FormularioDigitadorLocal formularioDigitador;

    @EJB
    private UsuarioEJBLocal usuarioEJB;

    @EJB
    private FormularioEJBLocal formularioEJB;

    static final Logger logger = Logger.getLogger(ForTrasladoMB.class.getName());

    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;

    private HttpServletRequest httpServletRequest1;
    private FacesContext facesContext1;

    private HttpServletRequest httpServletRequest2;
    private FacesContext facesContext2;
    
    private HttpServletRequest httpServletRequest3;
    private FacesContext facesContext3;

    private String usuarioRecibe;
    private String usuarioRecibeUnidad;
    private String usuarioRecibeCargo;
    private String usuarioRecibeRut;
    private String motivo;
    private String observacionesT;
    private Date fechaT;

    private String usuarioSis;
    private Usuario usuarioSesion;

    private int nue;
    private String rutInicia;
    private Usuario usuarioInicia;
    private Formulario formulario;

    public ForTrasladoMB() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "ForTrasladoMB");
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        if (httpServletRequest.getSession().getAttribute("nueF") != null) {
            this.nue = (int) httpServletRequest.getSession().getAttribute("nueF");
            logger.log(Level.FINEST, "nue recibido {0}", this.nue);
        }
        this.facesContext1 = FacesContext.getCurrentInstance();
        this.httpServletRequest1 = (HttpServletRequest) facesContext1.getExternalContext().getRequest();
        if (httpServletRequest1.getSession().getAttribute("cuentaUsuario") != null) {
            this.usuarioSis = (String) httpServletRequest1.getSession().getAttribute("cuentaUsuario");
            logger.log(Level.FINEST, "Usuario recibido {0}", this.usuarioSis);
        }

        facesContext2 = FacesContext.getCurrentInstance();
        httpServletRequest2 = (HttpServletRequest) facesContext2.getExternalContext().getRequest();
        
        facesContext3 = FacesContext.getCurrentInstance();
        httpServletRequest3 = (HttpServletRequest) facesContext3.getExternalContext().getRequest();


        if (httpServletRequest2.getSession().getAttribute("rutInicia") != null) {
            this.rutInicia = (String) httpServletRequest1.getSession().getAttribute("rutInicia");
            logger.log(Level.FINEST, "Rut recibido {0}", this.rutInicia);
        }
        this.usuarioSesion = new Usuario();
        this.usuarioInicia = new Usuario();
        this.formulario = new Formulario();

        logger.exiting(this.getClass().getName(), "ForTrasladoMB");
    }

    @PostConstruct
    public void cargarDatos() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "cargarDatos");
        this.formulario = formularioEJB.findFormularioByNue(this.nue);
        this.usuarioSesion = usuarioEJB.findUsuarioSesionByCuenta(usuarioSis);
        this.usuarioInicia = usuarioEJB.findUserByRut(this.rutInicia);
        this.fechaT = formulario.getFechaOcurrido();
        logger.exiting(this.getClass().getName(), "cargarDatos");
    }

    public String agregarTraslado() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "agregarTrasladoDigitador");
        //logger.log(Level.FINEST, "rut usuario entrega {0}", this.usuarioEntrega);
        logger.log(Level.FINEST, " usuario recibe {0}", this.usuarioRecibe);
        logger.log(Level.FINEST, "motivo {0}", this.motivo);

        if (usuarioRecibeRut == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar un R.U.T. válido", " "));

            logger.exiting(this.getClass().getName(), "agregarTrasladoDigitador", "Debe ingresar el rut");
            return "";

        } else {
            String mensaje = validacionVistasMensajesEJB.checkRut(usuarioRecibeRut);
            if (!mensaje.equals("Exito")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, " "));
                logger.exiting(this.getClass().getName(), "agregarTrasladoDigitador", "Rut ingresado inválido");
                return "";

            }
        }
        if (usuarioRecibe == null || usuarioRecibe.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar un nombre válido", " "));
            logger.exiting(this.getClass().getName(), "agregarTrasladoDigitador", "Debe ingresar un nombre valido");
            return "";

        }

        if ("".equals(usuarioRecibeCargo) || "".equals(usuarioRecibeUnidad) || usuarioRecibeCargo == null || usuarioRecibeUnidad == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar el cargo y unidad", " "));
            logger.exiting(this.getClass().getName(), "agregarTrasladoDigitador", "debbe seleccionar cargo o unidad");
            return "";
        }

        if (motivo == null || motivo.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe especificar el motivo del traslado", " "));
            logger.exiting(this.getClass().getName(), "agregarTrasladoDigitador", "Debe especificar el motivo del traslado");
            return "";
        }

        String resultado = formularioDigitador.crearTraslado(formulario, usuarioInicia, usuarioRecibe, usuarioRecibeCargo, usuarioRecibeRut, usuarioRecibeUnidad, fechaT, observacionesT, motivo, usuarioSesion);
        if (resultado.equals("Exito")) {
            httpServletRequest.getSession().setAttribute("nueF", this.nue);
            httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
            httpServletRequest3.getSession().setAttribute("rutInicia1", this.usuarioRecibeRut);
            logger.exiting(this.getClass().getName(), "agregarTrasladoDigitador", "todoHU11?faces-redirect=true");
            return "todoHU11?faces-redirect=true";
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resultado, "Uno o más datos inválidos"));
        logger.exiting(this.getClass().getName(), "agregarTrasladoDigitador", "traslado creado con éxito");
        return "";
    }

    public String nuevaCadena() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "nuevaCadena");
        httpServletRequest.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.exiting(this.getClass().getName(), "nuevaCadena", "/digitadorFormularioHU11");
        return "/digitadorFormularioHU11.xhtml?faces-redirect=true";
    }

    public String salir() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "salirDigitador");
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        httpServletRequest1.removeAttribute("cuentaUsuario");
        logger.exiting(this.getClass().getName(), "salirDigitador", "/indexListo");
        return "/indexListo?faces-redirect=true";
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }

    public int getNue() {
        return nue;
    }

    public void setNue(int nue) {
        this.nue = nue;
    }

    public String getUsuarioRecibe() {
        return usuarioRecibe;
    }

    public void setUsuarioRecibe(String usuarioRecibe) {
        this.usuarioRecibe = usuarioRecibe;
    }

    public String getUsuarioRecibeUnidad() {
        return usuarioRecibeUnidad;
    }

    public void setUsuarioRecibeUnidad(String usuarioRecibeUnidad) {
        this.usuarioRecibeUnidad = usuarioRecibeUnidad;
    }

    public String getUsuarioRecibeCargo() {
        return usuarioRecibeCargo;
    }

    public void setUsuarioRecibeCargo(String usuarioRecibeCargo) {
        this.usuarioRecibeCargo = usuarioRecibeCargo;
    }

    public String getUsuarioRecibeRut() {
        return usuarioRecibeRut;
    }

    public void setUsuarioRecibeRut(String usuarioRecibeRut) {
        this.usuarioRecibeRut = usuarioRecibeRut;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservacionesT() {
        return observacionesT;
    }

    public void setObservacionesT(String observacionesT) {
        this.observacionesT = observacionesT;
    }

    public Date getFechaT() {
        return fechaT;
    }

    public void setFechaT(Date fechaT) {
        this.fechaT = fechaT;
    }

    public Usuario getUsuarioSesion() {
        return usuarioSesion;
    }

    public void setUsuarioSesion(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
    }

    public String getUsuarioSis() {
        return usuarioSis;
    }

    public void setUsuarioSis(String usuarioSis) {
        this.usuarioSis = usuarioSis;
    }

    public String getRutInicia() {
        return rutInicia;
    }

    public void setRutInicia(String rutInicia) {
        this.rutInicia = rutInicia;
    }

    public Usuario getUsuarioInicia() {
        return usuarioInicia;
    }

    public void setUsuarioInicia(Usuario usuarioInicia) {
        this.usuarioInicia = usuarioInicia;
    }

}
