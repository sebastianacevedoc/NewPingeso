/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.digitador;

import ejb.FormularioDigitadorLocal;
import ejb.UsuarioEJBLocal;
import ejb.ValidacionVistasMensajesEJBLocal;
import entity.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
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
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Aracelly
 */
@Named(value = "crearFormularioMB")
@RequestScoped
@ManagedBean
public class CrearFormularioMB {

    @EJB
    private ValidacionVistasMensajesEJBLocal validacionVistasMensajesEJB;
    @EJB
    private FormularioDigitadorLocal formularioDigitador;

    @EJB
    private UsuarioEJBLocal usuarioEJB;

    static final Logger logger = Logger.getLogger(CrearFormularioMB.class.getName());

    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;

    private HttpServletRequest httpServletRequest1;
    private FacesContext facesContext1;

    private HttpServletRequest httpServletRequest2;
    private FacesContext facesContext2;

    private String usuarioSis;
    //Guardamos el usuario que inicia sesion
    private Usuario usuarioSesion;

    //Atributos del formulario
    private String ruc;
    private String rit;
    private int parte;
    private String parteS;
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

    private String motivo;

    //Usuario levantador
    private Usuario iniciaCadena;

    private List<String> usuarios;
    
    public CrearFormularioMB() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "CrearFormularioMB");

        this.usuarioSesion = new Usuario();
        this.iniciaCadena = new Usuario();
        this.usuarios = new ArrayList();

        this.facesContext2 = FacesContext.getCurrentInstance();
        this.httpServletRequest2 = (HttpServletRequest) facesContext2.getExternalContext().getRequest();

        this.facesContext = FacesContext.getCurrentInstance();
        this.httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();

        this.facesContext1 = FacesContext.getCurrentInstance();
        this.httpServletRequest1 = (HttpServletRequest) facesContext1.getExternalContext().getRequest();
        if (httpServletRequest1.getSession().getAttribute("cuentaUsuario") != null) {
            this.usuarioSis = (String) httpServletRequest1.getSession().getAttribute("cuentaUsuario");
            logger.log(Level.FINEST, "Usuario recibido {0}", this.usuarioSis);
        }

        logger.exiting(this.getClass().getName(), "CrearFormularioMB");
    }

    @PostConstruct
    public void cargarDatos() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "loadUsuario");
        
        boolean falla = false;

        if (usuarioSis != null) { //verifica si falla la carga de los datos que pasan por parámetro
            this.usuarioSesion = usuarioEJB.findUsuarioSesionByCuenta(this.usuarioSis);                  
        } else {
            falla = true;
        }

        //si es un usario no permitido, o si está deshabilitado
        if (usuarioSesion == null || !usuarioSesion.getCargoidCargo().getNombreCargo().equals("Digitador") || usuarioSesion.getEstadoUsuario() == false) {
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
        
        this.usuarios = usuarioEJB.findAllUserCrear();
        logger.exiting(this.getClass().getName(), "loadUsuario");
    }

    public String iniciarFormulario() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "iniciarFormulario");
        StringTokenizer st = new StringTokenizer(levantadaPor," ");
        int cont = st.countTokens();
        for (int i =0; i <cont; i++){
            String cosas = st.nextToken();
            if(i==0){
                rut = cosas;
                System.out.println("RUT ------> "+rut);
            }
            if(i==2){
                //30.08
                levantadaPor = cosas;
                System.out.println("NOMBRE-------> "+levantadaPor);
            }
        }
        

        String resultado = formularioDigitador.crearFormulario(rut, ruc, rit, nue, parte, delito, direccionSS, lugar, unidadPolicial, fecha, observacion, descripcion, usuarioSesion);

        if (resultado.equals("Exito")) {
            httpServletRequest.getSession().setAttribute("nueF", this.nue);
            httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
            httpServletRequest1.getSession().setAttribute("rutInicia", this.rut);

            logger.exiting(this.getClass().getName(), "iniciarFormulario", "forAddTHU11");
            return "forAddTHU11?faces-redirect=true";
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent uic = UIComponent.getCurrentComponent(fc);
        fc.addMessage(uic.getClientId(fc), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resultado));
        logger.exiting(this.getClass().getName(), "iniciarFormulario", "");
        return "";
    }

    //redirecciona a la pagina para iniciar cadena de custodia
    public String iniciarCadena() {
        logger.entering(this.getClass().getName(), "iniciarCadena");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.exiting(this.getClass().getName(), "iniciarCadena", "digitadorFormulario");
        return "digitadorFormularioHU11?faces-redirect=true";
    }

    public String salir() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "salirDigitador");
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        httpServletRequest1.removeAttribute("cuentaUsuario");
        logger.exiting(this.getClass().getName(), "salirDigitador", "/indexListo");
        return "/indexListo?faces-redirect=true";
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
    
      public void validarDLU(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String cadena = (String) value;
        if (!cadena.equals("")) {

            String mensaje = validacionVistasMensajesEJB.verificarInitFin(cadena);
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
    
    public void validarFecha(FacesContext context, UIComponent toValidate, Object value){
        context = FacesContext.getCurrentInstance();
        Date f = (Date) value;
        String mensaje = "";
        
        if(f != null){
            mensaje = validacionVistasMensajesEJB.checkFecha(f);
            if(!mensaje.equals("Exito")){
                ((UIInput) toValidate).setValid(false);
                context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
            }
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

    public int getParte() {
        return parte;
    }

    public void setParte(int parte) {
        this.parte = parte;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Usuario getIniciaCadena() {
        return iniciaCadena;
    }

    public void setIniciaCadena(Usuario iniciaCadena) {
        this.iniciaCadena = iniciaCadena;
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

    public List<String> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<String> usuarios) {
        this.usuarios = usuarios;
    }    
}
