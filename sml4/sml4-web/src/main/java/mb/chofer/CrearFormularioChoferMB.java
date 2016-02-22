/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.chofer;

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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sebastian
 */
@Named(value = "crearFormularioChoferMB")
@RequestScoped
@ManagedBean
public class CrearFormularioChoferMB {
    @EJB
    private ValidacionVistasMensajesEJBLocal validacionVistasMensajesEJB;
    @EJB
    private UsuarioEJBLocal usuarioEJB;
    @EJB
    private FormularioEJBLocal formularioEJB;

    static final Logger logger = Logger.getLogger(CrearFormularioChoferMB.class.getName());

    //Guardamos el usuario que inicia sesion
    private Usuario usuarioS;

    //Atributos del formulario
    private String ruc;
    private String rit;
    private int nue;
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

    private String motivo;

    //Guardamos la cuenta del usuario que entrego la vista del login
    private String usuarioSis;

    //Captura al usuario proveniente del inicio de sesión
    private HttpServletRequest httpServletRequest1;
    private FacesContext facesContext1;

    //Envio del nue
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;


    public CrearFormularioChoferMB() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "CrearFormularioChoferMB");
        this.usuarioS = new Usuario();

        this.facesContext = FacesContext.getCurrentInstance();
        this.httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();

        this.facesContext1 = FacesContext.getCurrentInstance();
        this.httpServletRequest1 = (HttpServletRequest) facesContext1.getExternalContext().getRequest();
        if (httpServletRequest1.getSession().getAttribute("cuentaUsuario") != null) {
            this.usuarioSis = (String) httpServletRequest1.getSession().getAttribute("cuentaUsuario");
            logger.log(Level.FINEST, "Usuario recibido {0}", this.usuarioSis);
        }
        logger.exiting(this.getClass().getName(), "CrearFormularioChoferMB");
    }

    @PostConstruct
    public void cargarDatos() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "cargarDatosChofer");
        this.usuarioS = (Usuario) usuarioEJB.findUsuarioSesionByCuenta(usuarioSis);

        this.cargo = usuarioS.getCargoidCargo().getNombreCargo();
        this.levantadaPor = usuarioS.getNombreUsuario();
        this.rut = usuarioS.getRutUsuario();

        GregorianCalendar c = new GregorianCalendar();
        this.fecha = c.getTime();

        logger.exiting(this.getClass().getName(), "cargarDatosChofer");
    }

    public String iniciarFormulario() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "iniciarFormularioChofer");
        int nParte =0;
        boolean datosIncorrectos = false;
        if (parte != 0) {
            String mensaje = validacionVistasMensajesEJB.checkParte(parte);
            if (!mensaje.equals("Exito")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, " "));
                datosIncorrectos = true;
                 httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
                return "";
            }
        }
        if (ruc != null) {
            String mensaje = validacionVistasMensajesEJB.checkRuc(ruc);
            if (!mensaje.equals("Exito")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, " "));
                datosIncorrectos = true;
                 httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
                return "";
            }
        }
        if (rit != null) {
            String mensaje = validacionVistasMensajesEJB.checkRit(rit);
            if (!mensaje.equals("Exito")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, " "));
                datosIncorrectos = true;
                 httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
                return "";
            }
        }
         if (nue <=0) {
            
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar un N.U.E válido", " "));
                datosIncorrectos = true;
                 httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
                return "";
            
        }
        
        if(datosIncorrectos){
            httpServletRequest.getSession().setAttribute("nueF", this.nue);
            httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
            logger.exiting(this.getClass().getName(), "editarFormularioPerito", "");
            return "";
        }    
        
        
        String resultado = formularioEJB.crearFormulario(motivo, ruc, rit, nue, nParte, cargo, delito, direccionSS, lugar, unidadPolicial, levantadaPor, rut, fecha, observacion, descripcion, usuarioS);

        if (resultado.equals("Exito")) {
            //Enviando nue
            httpServletRequest.getSession().setAttribute("nueF", this.nue);
            //Enviando usuario
            httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, resultado, "Datos exitosos"));
            logger.exiting(this.getClass().getName(), "iniciarFormularioChofer", "formularioCreadoChofer");
            return "formularioCreadoChofer.xhtml?faces-redirect=true";
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, resultado, "Datos no válidos"));
        logger.exiting(this.getClass().getName(), "iniciarFormularioChofer", "");
        return "";
    }

    public String salir() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "salirChofer");
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioS.getNombreUsuario());
        httpServletRequest1.removeAttribute("cuentaUsuario");
        logger.exiting(this.getClass().getName(), "salirChofer", "/indexListo");
        return "/indexListo?faces-redirect=true";
    }

    //redirecciona a la pagina para realizar una busqueda
    public String buscar() {
        logger.entering(this.getClass().getName(), "buscar");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.exiting(this.getClass().getName(), "buscar", "buscadorChofer");
        return "buscadorChofer?faces-redirect=true";
    }

    //redirecciona a la pagina para iniciar cadena de custodia
    public String iniciarCadena() {
        logger.entering(this.getClass().getName(), "iniciarCadena");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.exiting(this.getClass().getName(), "iniciarCadena", "choferFormulario");
        return "choferFormulario?faces-redirect=true";
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Usuario getUsuarioS() {
        return usuarioS;
    }

    public void setUsuarioS(Usuario usuarioS) {
        this.usuarioS = usuarioS;
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

    public int getNue() {
        return nue;
    }

    public void setNue(int nue) {
        this.nue = nue;
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

    public int getParte() {
        return parte;
    }

    public void setParte(int parte) {
        this.parte = parte;
    }

  



}
