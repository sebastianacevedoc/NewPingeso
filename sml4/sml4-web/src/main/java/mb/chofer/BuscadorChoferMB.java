/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.chofer;

import ejb.FormularioEJBLocal;
import ejb.UsuarioEJBLocal;
import entity.Formulario;
import entity.Usuario;
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
@Named(value = "buscadorChofer")
@RequestScoped
@ManagedBean
public class BuscadorChoferMB {

    @EJB
    private UsuarioEJBLocal usuarioEJB;
    @EJB
    private FormularioEJBLocal formularioEJB;

    static final Logger logger = Logger.getLogger(BuscadorChoferMB.class.getName());

    private Usuario usuarioSesion;

    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;

    private HttpServletRequest httpServletRequest1;
    private FacesContext facesContext1;

    private String usuarioSis;
    private int nue;
    private String nueS;

    public BuscadorChoferMB() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "BuscadorChoferMB");
        /**/
        this.facesContext = FacesContext.getCurrentInstance();
        this.httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();

        this.facesContext1 = FacesContext.getCurrentInstance();
        this.httpServletRequest1 = (HttpServletRequest) facesContext1.getExternalContext().getRequest();
        if (httpServletRequest1.getSession().getAttribute("cuentaUsuario") != null) {
            this.usuarioSis = (String) httpServletRequest1.getSession().getAttribute("cuentaUsuario");
            logger.log(Level.FINEST, "Usuario recibido {0}", this.usuarioSis);
        }
        logger.exiting(this.getClass().getName(), "BuscadorChoferMB");
    }

    @PostConstruct
    public void loadUsuario() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "loadUsuarioChofer");
        
        boolean falla = false;

        if (usuarioSis != null) { //verifica si falla la carga de los datos que pasan por parámetro
            this.usuarioSesion = usuarioEJB.findUsuarioSesionByCuenta(this.usuarioSis);
        } else {
            falla = true;
        }

        //si es un usario no permitido, o si está deshabilitado
        if (usuarioSesion == null || !usuarioSesion.getCargoidCargo().getNombreCargo().equals("Chofer") || usuarioSesion.getEstadoUsuario() == false) {
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
        
        logger.exiting(this.getClass().getName(), "loadUsuarioChofer");
    }

    public String buscarFormulario() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "buscarFormularioChofer");
        logger.log(Level.INFO, "NUE CAPTURADO:{0}", this.nue);
        Formulario formulario = formularioEJB.findFormularioByNue(this.nue);

        if (formulario != null) {
            httpServletRequest.getSession().setAttribute("nueF", this.nue);
            httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
            
            logger.exiting(this.getClass().getName(), "buscarFormulario", "editarChoferET");
            return "editarChoferET.xhtml?faces-redirect=true";
        }
         FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent uic = UIComponent.getCurrentComponent(fc);
        fc.addMessage(uic.getClientId(fc), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "N.U.E. no existe"));
        logger.info("formulario no encontrado");
        logger.exiting(this.getClass().getName(), "buscarFormularioChofer", "");
        return "";
    }

    public String salir() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "salirChofer");
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        httpServletRequest1.removeAttribute("cuentaUsuario");
        logger.exiting(this.getClass().getName(), "salirChofer", "/indexListo");
        return "/indexListo?faces-redirect=true";
    }
    
    //redirecciona a la pagina para realizar una busqueda
    public String buscar(){
        logger.entering(this.getClass().getName(), "buscar");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);        
        logger.exiting(this.getClass().getName(), "buscar", "buscadorChofer");
        return "buscadorChofer?faces-redirect=true";
    }

    //redirecciona a la pagina para iniciar cadena de custodia
    public String iniciarCadena(){
        logger.entering(this.getClass().getName(), "iniciarCadena");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);        
        logger.exiting(this.getClass().getName(), "iniciarCadena", "choferFormulario");
        return "choferFormulario?faces-redirect=true";
    }
    
    public void validarNue(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        String mensaje = "";
        try {
            nue = Integer.parseInt(texto);
            if (nue > 0) {
                mensaje = "Exito";
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
    
    public String getUsuarioSis() {
        return usuarioSis;
    }

    public void setUsuarioSis(String usuarioSis) {
        this.usuarioSis = usuarioSis;
    }

    public Usuario getUsuarioSesion() {
        return usuarioSesion;
    }

    public void setUsuarioSesion(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
    }

    public int getNue() {
        return nue;
    }

    public void setNue(int nue) {
        this.nue = nue;
    }
    
    public String getNueS() {
        return nueS;
    }

    public void setNueS(String nueS) {
        this.nueS = nueS;
    }
}
