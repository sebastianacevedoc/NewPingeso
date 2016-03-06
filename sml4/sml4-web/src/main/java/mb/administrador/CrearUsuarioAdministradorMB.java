/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.administrador;

import ejb.FormularioEJBLocal;
import ejb.UsuarioEJBLocal;
import ejb.ValidacionVistasMensajesEJBLocal;
import entity.Area;
import entity.Cargo;
import entity.Usuario;
import java.util.ArrayList;
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
import mb.jefeArea.CrearUsuarioJefeAreaMB;

/**
 *
 * @author sebastian
 */
@Named(value = "crearUsuarioAdministradorMB")
@RequestScoped
@ManagedBean
public class CrearUsuarioAdministradorMB {

    @EJB
    private ValidacionVistasMensajesEJBLocal validacionVistasMensajesEJB;

    @EJB
    private UsuarioEJBLocal usuarioEJB;

    @EJB
    private FormularioEJBLocal formularioEJB;

    static final Logger logger = Logger.getLogger(CrearUsuarioJefeAreaMB.class.getName());

    private Usuario usuarioSesion;

    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;

    private HttpServletRequest httpServletRequest1;
    private FacesContext facesContext1;

    private String usuarioSis;

    private String rut;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String pass;
    private String pass2;
    private String mail;
    private String cuentaUsuario;
    private String cargo;
    private String area;

    private List<String> cargos;

    private List<String> areas;

    public CrearUsuarioAdministradorMB() {

        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "CrearUsuarioADM");

        this.facesContext = FacesContext.getCurrentInstance();
        this.httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();

        this.cargos = new ArrayList();
        this.areas = new ArrayList();
        this.facesContext1 = FacesContext.getCurrentInstance();
        this.httpServletRequest1 = (HttpServletRequest) facesContext1.getExternalContext().getRequest();
        if (httpServletRequest1.getSession().getAttribute("cuentaUsuario") != null) {
            this.usuarioSis = (String) httpServletRequest1.getSession().getAttribute("cuentaUsuario");
            logger.log(Level.FINEST, "Usuario recibido {0}", this.usuarioSis);
        }

        logger.exiting(this.getClass().getName(), "CrearUsuarioADM");
    }

    @PostConstruct
    public void loadDatos() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "loadDatosJefeArea");

        boolean falla = false;

        if (usuarioSis != null) { //si falla la carga de los datos que pasan por parámetro
            this.usuarioSesion = usuarioEJB.findUsuarioSesionByCuenta(usuarioSis);
        } else {
            falla = true;
        }

        //si es un usario no permitido, o si está deshabilitado
        if (usuarioSesion == null || !usuarioSesion.getCargoidCargo().getNombreCargo().equals("Administrativo") || usuarioSesion.getEstadoUsuario() == false) {
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

        //Cargando cargos
        List<Cargo> cargos1 = formularioEJB.findAllCargos();

        for (Cargo cargo : cargos1) {
            this.cargos.add(cargo.getNombreCargo());
        }

        //Cargando areas
        List<Area> areas1 = formularioEJB.findAllAreas();

        for (Area area : areas1) {
            this.areas.add(area.getNombreArea());
        }

        logger.exiting(this.getClass().getName(), "loadDatosJefeArea");
    }

    public String crearUsuario() {

        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "CrearUsuarioADM");

//        System.out.println("-------------->>>>>>>>>>>>>UIComponent "+uic.getClientId());
//        System.out.println("-------------->>>>>>>>>>>>>UIComponent CONTEX "+ uic.getClientId(fc));        
        String response = usuarioEJB.crearUsuario(nombreUsuario, apellidoUsuario, rut, pass, mail, cuentaUsuario, cargo, area);

        if (response.equals("Exito")) {
            httpServletRequest.getSession().setAttribute("rut", this.rut);
            httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
            logger.exiting(this.getClass().getName(), "crearUsuarioADM", "crearUsuarioResultAdm");
            return "crearUsuarioResultAdm?faces-redirect=true";
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent uic = UIComponent.getCurrentComponent(fc);
        fc.addMessage(uic.getClientId(fc), new FacesMessage(FacesMessage.SEVERITY_ERROR, response, ""));
        logger.info("No se pudo crear el usuario");
        logger.exiting(this.getClass().getName(), "CrearUsuarioADM");
        return "";
    }

    //retorna a la vista para realizar busqueda
    public String buscador() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "buscadorADM");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        logger.exiting(this.getClass().getName(), "buscadorADM", "buscador");
        return "buscadorAdministrador?faces-redirect=true";
    }

    public String crearUsuario1() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "crearUADM");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        logger.exiting(this.getClass().getName(), "crearUADM", "crearUsuario");
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

    public void validarRut(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        String mensaje = validacionVistasMensajesEJB.checkRut(texto);
        if (!mensaje.equals("Exito")) { //caso rut invalido
            ((UIInput) toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
        } else { //rut valido, verificamos si la se encuentra registrado
            mensaje = validacionVistasMensajesEJB.validarRut(texto);
            if (!mensaje.equals("Exito")) {
                ((UIInput) toValidate).setValid(false);
                context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
            }
        }
    }

    public void validarCorreo(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        String mensaje = validacionVistasMensajesEJB.checkCorreo(texto);
        if(mensaje.equals("Exito")){//si el correo tiene el formato correcto, entonces validamos que este no exista.
            mensaje = validacionVistasMensajesEJB.validarCorreo(texto);
        } 
        if (!mensaje.equals("Exito")) { //correo invalido
            ((UIInput) toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
        }
    }

    public void validarCuenta(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        String mensaje = validacionVistasMensajesEJB.validarCuentaUsuario(texto);
        if (!mensaje.equals("Exito")) { //cuenta invalido
            ((UIInput) toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
        }
    }

    public void validarApellido(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        String mensaje = validacionVistasMensajesEJB.verificarInitFinSoloCaracteres(texto);
        if (!mensaje.equals("Exito")) { //apellido invalido
            ((UIInput) toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
        }
    }

    public void validarNombre(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        String mensaje = validacionVistasMensajesEJB.verificarInitFinSoloCaracteres(texto);
        if (!mensaje.equals("Exito")) { //nombre invalido
            ((UIInput) toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
        }
    }

    public void validarPass(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;

        if (texto.length() < 8) {
            ((UIInput) toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Contraseña es inferior a 8 caracteres"));
        }
    }

    public void validarPass2(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        String pass1 = (String) toValidate.getAttributes().get("Pass");

        if (!texto.equals(pass1)) {
            ((UIInput) toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Contraseñas deben coincidir"));
        }
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

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPass2() {
        return pass2;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCuentaUsuario() {
        return cuentaUsuario;
    }

    public void setCuentaUsuario(String cuentaUsuario) {
        this.cuentaUsuario = cuentaUsuario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<String> getAreas() {
        return areas;
    }

    public void setAreas(List<String> areas) {
        this.areas = areas;
    }

    public List<String> getCargos() {
        return cargos;
    }

    public void setCargos(List<String> cargos) {
        this.cargos = cargos;
    }

}
