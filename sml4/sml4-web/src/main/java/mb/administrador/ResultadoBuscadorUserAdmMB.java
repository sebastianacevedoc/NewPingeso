/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.administrador;

import ejb.FormularioEJBLocal;
import ejb.UsuarioEJBLocal;
import entity.Usuario;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sebastian
 */
@Named(value = "resultadoBuscadorUserAdmMB")
@RequestScoped
@ManagedBean
public class ResultadoBuscadorUserAdmMB {

    @EJB
    private UsuarioEJBLocal usuarioEJB;

    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;

    private HttpServletRequest httpServletRequest1;
    private FacesContext facesContext1;

    private String userSesion;
    private Usuario usuarioSesion;

    private String rut;
    private Usuario usuarioBuscado;
    private String estadoUsuarioEspanol;

    private static final Logger logger = Logger.getLogger(ResultadoBuscadorUserAdmMB.class.getName());
    private boolean estadoH;
    private boolean estadoD;

    public ResultadoBuscadorUserAdmMB() {
        
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "ResultadoBuscadorUserAdmMB");

        facesContext1 = FacesContext.getCurrentInstance();
        httpServletRequest1 = (HttpServletRequest) facesContext1.getExternalContext().getRequest();

        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        if (httpServletRequest.getSession().getAttribute("rut") != null) {
            this.rut = (String) httpServletRequest.getSession().getAttribute("rut");
            logger.log(Level.FINEST, "rut recibido {0}", this.rut);
        }

        if (httpServletRequest1.getSession().getAttribute("cuentaUsuario") != null) {
            this.userSesion = (String) httpServletRequest1.getSession().getAttribute("cuentaUsuario");
            logger.log(Level.FINEST, "Usuario recibido {0}", this.userSesion);
        }
        logger.exiting(this.getClass().getName(), "ResultadoBuscadorUserAdmMB");

        
    }
    
     @PostConstruct
    public void cargarDatos() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "CargarDatosADM");
                
        boolean falla = false;

        if (userSesion != null && rut != null) { //verifica si falla la carga de los datos que pasan por parámetro
            this.usuarioSesion = usuarioEJB.findUsuarioSesionByCuenta(this.userSesion);
            this.usuarioBuscado = usuarioEJB.findUserByRut(this.rut);
        } else {
            falla = true;
        }

        //si es un usario no permitido, o si está deshabilitado
        if (usuarioSesion == null || !usuarioSesion.getCargoidCargo().getNombreCargo().equals("Administrativo") || usuarioSesion.getEstadoUsuario() == false) {
            falla = true;
        }

        if(usuarioBuscado == null){
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
                //System.out.println("POST CONSTRUCTOR FALLO");
            }
        }      
        
        latino();
        
        if(Objects.equals(usuarioSesion.getIdUsuario(), usuarioBuscado.getIdUsuario())){ //deshabilitar botones si es el mismo usuario
            estadoD=true;
            estadoH=true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Imposible modificar usuario actual", ""));
        }
        
        logger.log(Level.INFO, "Nombre usuario {0}", this.usuarioBuscado.getNombreUsuario());
        logger.log(Level.FINEST, "Rut usuario {0}", this.usuarioBuscado.getRutUsuario());
        logger.exiting(this.getClass().getName(), "CargarDatosADM");
    }

    private void latino(){
    boolean estado = this.usuarioBuscado.getEstadoUsuario();
        if(estado){
            estadoUsuarioEspanol = "Habilitado";
            this.estadoH=true;
            this.estadoD=false;
        }
        else{
            estadoUsuarioEspanol = "Deshabilitado";
            this.estadoD=true;
            this.estadoH=false;
        }
    }
    
    public String habilitarUsuario() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "habilitarUsuario");
        boolean response = usuarioEJB.edicionEstadoUsuario(this.rut, "Activo");

        if (response == true) {
            httpServletRequest.getSession().setAttribute("rut", this.rut);
            httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.userSesion);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario ha sido habilitado correctamente", ""));
            logger.exiting(this.getClass().getName(), "habilitarUsuario", "buscadorJefeAreaResultUsuario");
            latino();
            return "buscadorAdmResultUsuario.xhtml?faces-redirect=true";
            //return "";
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario ya se encuentra habilitado", ""));
        logger.info("Usuario ya se encuentra habilitado");
        logger.exiting(this.getClass().getName(), "habilitarUsuario", "habilitarUsuario");

        return "";
    }

    public String deshabilitarUsuario() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "deshabilitarUsuario");
        boolean response = usuarioEJB.edicionEstadoUsuario(this.rut, "Inactivo");
        if (response == true) {
            httpServletRequest.getSession().setAttribute("rut", this.rut);
            httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.userSesion);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario ha sido deshabilitado correctamente", ""));
            logger.exiting(this.getClass().getName(), "deshabilitarUsuario", "buscadorJefeAreaResultUsuario");
            return "buscadorAdmResultUsuario.xhtml?faces-redirect=true";
           // return "";
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario ya se encuentra deshabilitado", ""));
        logger.info("Usuario ya se encuentra deshabilitado");
        logger.exiting(this.getClass().getName(), "deshabilitarUsuario", "deshabilitarUsuario");
        return "";
    }
    //retorna a la vista para realizar busqueda
    public String buscador() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "buscadorADM");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.userSesion);
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        logger.exiting(this.getClass().getName(), "buscadorADM", "buscador");
        return "buscadorAdministrador?faces-redirect=true";
    }

    public String crearUsuario() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "crearUsuarioADM");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.userSesion);
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        logger.exiting(this.getClass().getName(), "crearUsuarioADM", "crearUsuario");
        return "crearUsuarioAdministrador.xhtml?faces-redirect=true";
    }
    
     public String salir() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "salirADM");
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        httpServletRequest1.removeAttribute("cuentaUsuario");
        logger.exiting(this.getClass().getName(), "salirADM", "/indexListo");
        return "/indexListo?faces-redirect=true";
    }

     
     
     
    public String getUserSesion() {
        return userSesion;
    }

    public void setUserSesion(String userSesion) {
        this.userSesion = userSesion;
    }

    public Usuario getUsuarioSesion() {
        return usuarioSesion;
    }

    public void setUsuarioSesion(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public Usuario getUsuarioBuscado() {
        return usuarioBuscado;
    }

    public void setUsuarioBuscado(Usuario usuarioBuscado) {
        this.usuarioBuscado = usuarioBuscado;
    }

    public String getEstadoUsuarioEspanol() {
        return estadoUsuarioEspanol;
    }

    public void setEstadoUsuarioEspanol(String estadoUsuarioEspanol) {
        this.estadoUsuarioEspanol = estadoUsuarioEspanol;
    }

    public boolean isEstadoH() {
        return estadoH;
    }

    public void setEstadoH(boolean estadoH) {
        this.estadoH = estadoH;
    }

    public boolean isEstadoD() {
        return estadoD;
    }

    public void setEstadoD(boolean estadoD) {
        this.estadoD = estadoD;
    }
    
    

}
