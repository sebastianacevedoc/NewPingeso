package mb.jefeArea;

import ejb.FormularioEJBLocal;
import ejb.UsuarioEJBLocal;
import ejb.ValidacionVistasMensajesEJBLocal;
import entity.Formulario;
import entity.Usuario;
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
@Named(value = "buscadorJefeAreaMB")
@RequestScoped
@ManagedBean
public class BuscadorJefeAreaMB {

    @EJB
    private ValidacionVistasMensajesEJBLocal validacionVistasMensajesEJB;

    @EJB
    private UsuarioEJBLocal usuarioEJB;
    @EJB
    private FormularioEJBLocal formularioEJB;

    static final Logger logger = Logger.getLogger(BuscadorJefeAreaMB.class.getName());

    private Usuario usuarioSesion;

    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;

    private HttpServletRequest httpServletRequest1;
    private FacesContext facesContext1;

    private HttpServletRequest httpServletRequest2;
    private FacesContext facesContext2;

    private String usuarioSis;
    //para busqueda de nue
    private int nue;
    private String nueS;

    //busqueda ruc ric y nparte
    private String buscar;
    private String input;

    //busqueda usuario
    private String rut;

    public BuscadorJefeAreaMB() {
     //   logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "BusquedaJefeAreaMB");
        /**/
        this.facesContext = FacesContext.getCurrentInstance();
        this.httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();

        this.facesContext2 = FacesContext.getCurrentInstance();
        this.httpServletRequest2 = (HttpServletRequest) facesContext2.getExternalContext().getRequest();

        this.facesContext1 = FacesContext.getCurrentInstance();
        this.httpServletRequest1 = (HttpServletRequest) facesContext1.getExternalContext().getRequest();
        if (httpServletRequest1.getSession().getAttribute("cuentaUsuario") != null) {
            this.usuarioSis = (String) httpServletRequest1.getSession().getAttribute("cuentaUsuario");
            logger.log(Level.FINEST, "Usuario recibido {0}", this.usuarioSis);
        }

//        this.facesContext3 = FacesContext.getCurrentInstance();
//        this.httpServletRequest3 = (HttpServletRequest) facesContext3.getExternalContext().getRequest();
//        if (httpServletRequest3.getSession().getAttribute("buscar") != null) {
//            this.buscar = (String) httpServletRequest3.getSession().getAttribute("buscar");
//
//        }
//        //System.out.println("LOAD busqueda -> " + buscar);

        logger.exiting(this.getClass().getName(), "BusquedaJefeAreaMB");
    }

    @PostConstruct
    public void loadUsuario() {
     //   logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "loadUsuarioJefeArea");
        
        boolean falla = false;

        if (usuarioSis != null ) { //verifica si falla la carga de los datos que pasan por parámetro
            this.usuarioSesion = usuarioEJB.findUsuarioSesionByCuenta(this.usuarioSis);                  
        } else {
            falla = true;
        }

        //si es un usario no permitido, o si está deshabilitado
        if (usuarioSesion == null || !usuarioSesion.getCargoidCargo().getNombreCargo().equals("Jefe de area") || usuarioSesion.getEstadoUsuario() == false) {
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
        
        logger.exiting(this.getClass().getName(), "loadUsuarioJefeArea");
    }

    public String buscarFormulario() {
    //    logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "buscarFormularioJefeArea");
        logger.log(Level.INFO, "NUE CAPTURADO:{0}", this.nue);

//        if (nue <= 0) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar un N.U.E. válido", " "));
//            return "";
//        }
        Formulario formulario = formularioEJB.findFormularioByNue(this.nue);

        if (formulario != null) {
            httpServletRequest.getSession().setAttribute("nueF", this.nue);
            httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
            logger.exiting(this.getClass().getName(), "buscarFormularioJefeArea", "buscadorJefeAreaResultTE");
            return "buscadorJefeAreaResultTE.xhtml?faces-redirect=true";
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent uic = UIComponent.getCurrentComponent(fc);
        fc.addMessage(uic.getClientId(fc), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "N.U.E. no existe"));
       // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "N.U.E. no existe", ""));
        logger.info("formulario no encontrado");
        logger.exiting(this.getClass().getName(), "buscarFormularioJefeArea", "buscadorJefeArea");
        return "";
    }

    public String buscarFormularioRRP() {
    //    logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "buscarFormularioRRPJefeArea");
        logger.log(Level.INFO, "INPUT CAPTURADO:{0}", this.input);

        String formNotFound = "";
        String mensaje = "";

        if (buscar != null && !buscar.equals("")) {

            switch (buscar) {
                case "Ruc":
//                    if (input.equals("")) {
//                        mensaje = "Debe ingresasr R.U.C.";
//                    } else {
//                        mensaje = validacionVistasMensajesEJB.checkRucE(input);
//                    }
                    formNotFound = "R.U.C. no existe";
                    break;
                case "Rit":
//                    if (input.equals("")) {
//                        mensaje = "Debe ingresasr R.I.T";
//                    } else {
//                        mensaje = validacionVistasMensajesEJB.checkRitE(input);
//                    }
                    formNotFound = "R.I.T. no existe";
                    break;
                case "NumeroParte":
//                    int parte;
//                    try {
//                        parte = Integer.parseInt(input);
//                    } catch (NumberFormatException e) {
//                        String msg = "";
//                        if (!input.equals("")) {
//                            msg = "N° Parte erróneo";
//                        } else {
//                            msg = "Debe N° Parte";
//                        }
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
//                        return "";
//                    }
//                    mensaje = validacionVistasMensajesEJB.checkParte(parte);

                    formNotFound = "N° Parte no existe";
                    break;
            }
//            if (!mensaje.equals("Exito")) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, ""));
//                return "";
//            }
        }

        List<Formulario> formulario = formularioEJB.findByNParteRR(input, buscar);

        if (!formulario.isEmpty()) {
            httpServletRequest.getSession().setAttribute("input", this.input);
            httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
            httpServletRequest2.getSession().setAttribute("buscar", this.buscar);
            logger.exiting(this.getClass().getName(), "buscarFormularioRRPJefeArea", "buscadorJefeAreaResultRRP");
            return "buscadorJefeAreaResultRRP.xhtml?faces-redirect=true";
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent uic = UIComponent.getCurrentComponent(fc);
        fc.addMessage(uic.getClientId(fc), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", formNotFound));
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, formNotFound, ""));
        logger.info("formulario no encontrado");
        logger.exiting(this.getClass().getName(), "buscarFormularioRRPJefeArea", "buscarFormularioRRP");
        return "";
    }

    public String buscarUsuario() {
     //   logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "buscarUsuarioJefeArea");
        logger.log(Level.INFO, "RUT CAPTURADO:{0}", this.rut);

//        if (rut != null && !rut.equals("")) {
//            String mensaje = validacionVistasMensajesEJB.checkRut(rut);
//            if (!mensaje.equals("Exito")) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar R.U.T. válido", " "));
//                return "";
//            }
//        }
        Usuario user = usuarioEJB.findUserByRut(rut);

        if (user != null) {
            httpServletRequest.getSession().setAttribute("rut", this.rut);
            httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
            logger.exiting(this.getClass().getName(), "buscarUsuarioJefeArea", "buscadorJefeAreaResultUsuario");
            return "buscadorJefeAreaResultUsuario.xhtml?faces-redirect=true";
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent uic = UIComponent.getCurrentComponent(fc);
        fc.addMessage(uic.getClientId(fc), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "R.U.T. no existe"));
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "R.U.T. no existe", ""));
        logger.info("formulario no encontrado");
        logger.exiting(this.getClass().getName(), "buscarUsuarioJefeArea", "buscarUsuario");
        return "";

    }

    //retorna a la vista para realizar busqueda
    public String buscador() {
     //   logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "buscadorJefeArea");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        logger.exiting(this.getClass().getName(), "buscadorJefeArea", "buscadorJefeArea");
        return "buscadorJefeArea?faces-redirect=true";
    }

    public String crearUsuario() {
    //    logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "buscadorJefeArea");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        logger.exiting(this.getClass().getName(), "buscadorJefeArea", "crearUsuario");
        return "crearUsuarioJefeArea.xhtml?faces-redirect=true";
    }

    public String semaforo() {
      //  logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "buscadorJefeArea");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        logger.exiting(this.getClass().getName(), "buscadorJefeArea", "semaforo");
        return "semaforoJefeArea.xhtml?faces-redirect=true";
    }

    public String salir() {
    //    logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "salirJefeArea");
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        httpServletRequest1.removeAttribute("cuentaUsuario");
        logger.exiting(this.getClass().getName(), "salirJefeArea", "/indexListo");
        return "/indexListo?faces-redirect=true";
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

    public void validarRut(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        String mensaje = validacionVistasMensajesEJB.checkRut(texto);
        if (!mensaje.equals("Exito")) {
            ((UIInput) toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensaje));
        }
    }


    public void validarInput(FacesContext context, UIComponent toValidate, Object value) {
        context = FacesContext.getCurrentInstance();
        String texto = (String) value;
        String tipo = (String) toValidate.getAttributes().get("check");
        String mensaje = "";
        
        //System.out.println("INPUT "+texto+" TIPO "+tipo);
        
        if (tipo != null && !tipo.equals("")) {
            switch (tipo) {
                case "Ruc":
                    mensaje = validacionVistasMensajesEJB.checkRucE(texto);
                    break;
                case "Rit":
                    mensaje = validacionVistasMensajesEJB.checkRitE(texto);
                    break;
                case "NumeroParte":
                    try {
                        int parteIngresado = Integer.parseInt(texto);
                        mensaje = validacionVistasMensajesEJB.checkParte(parteIngresado);
                    } catch (NumberFormatException nfe) {
                        mensaje = "N° Parte erróneo";
                    }
                    break;
            }

            if (!mensaje.equals("Exito")) {
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

    public int getNue() {
        return nue;
    }

    public void setNue(int nue) {
        this.nue = nue;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNueS() {
        return nueS;
    }

    public void setNueS(String nueS) {
        this.nueS = nueS;
    }

}
