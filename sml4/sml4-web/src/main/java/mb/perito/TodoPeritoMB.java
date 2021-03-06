/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.perito;

import ejb.FormularioEJBLocal;
import ejb.UsuarioEJBLocal;
import entity.EdicionFormulario;
import entity.Formulario;
import entity.Traslado;
import entity.Usuario;
import java.util.ArrayList;
import java.util.List;
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
@Named(value = "todoPeritoMB")
@RequestScoped
@ManagedBean
public class TodoPeritoMB {

    @EJB
    private UsuarioEJBLocal usuarioEJB;

    @EJB
    private FormularioEJBLocal formularioEJB;

    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;

    private HttpServletRequest httpServletRequest1;
    private FacesContext facesContext1;

    private int nue;
    private String usuarioSis;
    private Usuario usuarioSesion;

    private Formulario formulario;

    private List<Traslado> trasladosList;
    private List<EdicionFormulario> edicionesList;

    private boolean bloqueada;
    private boolean editable;
    private boolean deshabBtnRecibir;

    private int contador = 1;

    private String cambia;

    private List<Traslado> intercalado;

    private String numeroParte;
    
    static final Logger logger = Logger.getLogger(TodoPeritoMB.class.getName());

    public TodoPeritoMB() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "TodoPeritoMB");
        this.trasladosList = new ArrayList<>();
        this.edicionesList = new ArrayList<>();
        this.intercalado = new ArrayList<>();
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        if (httpServletRequest.getSession().getAttribute("nueF") != null) {
            this.nue = (int) httpServletRequest.getSession().getAttribute("nueF");
            logger.log(Level.FINEST, "todo nue recibido {0}", this.nue);
        }
        this.facesContext1 = FacesContext.getCurrentInstance();
        this.httpServletRequest1 = (HttpServletRequest) facesContext1.getExternalContext().getRequest();
        if (httpServletRequest1.getSession().getAttribute("cuentaUsuario") != null) {
            this.usuarioSis = (String) httpServletRequest1.getSession().getAttribute("cuentaUsuario");
            logger.log(Level.FINEST, "Usuario recibido {0}", this.usuarioSis);
        }
        logger.exiting(this.getClass().getName(), "TodoPeritoMB");
    }

    @PostConstruct
    public void cargarDatos() {
        // logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "cargarDatosPerito");

        boolean falla = false;

        if (usuarioSis != null && nue != 0) { //verifica si falla la carga de los datos que pasan por parámetro
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
                //System.out.println("POST CONSTRUCTOR FALLO");
            }
        }

        this.formulario = formularioEJB.findFormularioByNue(this.nue);
        if (formulario.getNumeroParte() == 0) {
            this.numeroParte = "";
        } else {
            this.numeroParte = "" + formulario.getNumeroParte();
        }

        this.usuarioSesion = usuarioEJB.findUsuarioSesionByCuenta(usuarioSis);

        this.trasladosList = formularioEJB.traslados(this.formulario);

        this.edicionesList = formularioEJB.listaEdiciones(nue);

        this.bloqueada = formulario.getBloqueado();

        this.editable = formularioEJB.esParticipanteCC(formulario, usuarioSesion);
        logger.log(Level.INFO, "editable {0}", editable);
        if (bloqueada) {
            this.deshabBtnRecibir = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Esta cadena de custodia se encuentra cerrada.", ""));
        }

        if (bloqueada || Objects.equals(usuarioSesion.getIdUsuario(), trasladosList.get(trasladosList.size() - 1).getUsuarioidUsuarioEntrega().getIdUsuario())) {
            this.deshabBtnRecibir = true;
        }

        intercalado(trasladosList);

        logger.log(Level.INFO, "formulario ruc {0}", this.formulario.getRuc());
        logger.log(Level.FINEST, "todos cant traslados {0}", this.trasladosList.size());
        logger.exiting(this.getClass().getName(), "cargarDatosPerito");
    }

    //redirecciona a la pagina para realizar una busqueda
    public String buscar() {
        //logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "buscar");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.exiting(this.getClass().getName(), "buscar", "buscadorPerito");
        return "buscadorPerito.xhtml?faces-redirect=true";
    }

    //redirecciona a la pagina para iniciar cadena de custodia
    public String iniciarCadena() {
        //logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "iniciarCadena");
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.exiting(this.getClass().getName(), "iniciarCadena", "peritoFormulario");
        return "peritoFormulario.xhtml?faces-redirect=true";
    }

    public String salir() {
        //logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "salirPerito");
        logger.log(Level.FINEST, "usuario saliente {0}", this.usuarioSesion.getNombreUsuario());
        httpServletRequest1.removeAttribute("cuentaUsuario");
        logger.exiting(this.getClass().getName(), "salirPerito", "/indexListo");
        return "/indexListo.xhtml?faces-redirect=true";
    }

    //envia a la pagina para realizar una edicion en este formulario.
    public String editar() {
        //logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "editar");
        httpServletRequest.getSession().setAttribute("nueF", this.nue);
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.exiting(this.getClass().getName(), "editar", "editarPeritoET");
        return "editarPeritoET.xhtml?faces-redirect=true";
    }

    public String nuevaCadena() {
        //logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "nuevaCadena");
        //Enviando usuario
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nueva Cadena", "Ir a nuevo formulario"));
        logger.exiting(this.getClass().getName(), "nuevaCadena", "peritoFormulario");
        return "peritoFormulario.xhtml?faces-redirect=true";
    }

    //envía a la página para recibir la cadena
    public String recibirCadena() {
        //logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "recibirCadena");
        httpServletRequest.getSession().setAttribute("nueF", this.nue);
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);

        //si el siguiente traslado es para peritaje, enviamos a la vista para recibir para peritaje 
        if (trasladosList.get(trasladosList.size() - 1).getTipoMotivoidMotivo().getTipoMotivo().equals("Peritaje")) {
            logger.exiting(this.getClass().getName(), "recibirCadena", "recibirPeritoETP");
            return "recibirPeritoETP?faces-redirect=true";
        }
        //como no el siguiente traslado no era para peritaje, enviamos a la vista donde se indica el motivo del siguiente traslado.
        logger.exiting(this.getClass().getName(), "recibirCadena", "recibirPeritoET");
        return "recibirPeritoET?faces-redirect=true";
    }

    public String cambio() {

        if (contador == 1) {
            cambia = "Entrega";
            contador++;
        } else if (contador == 2) {
            cambia = "Recibe";
            contador++;
        } else {
            contador = 2;
            cambia = "Entrega";
        }

        return cambia;
    }

    private void intercalado(List<Traslado> traslados) {

        for (int i = 0; i < traslados.size(); i++) {

            for (int j = 0; j < 2; j++) {
                Traslado tras = new Traslado();
                tras.setFechaEntrega(traslados.get(i).getFechaEntrega());
                tras.setFormularioNUE(traslados.get(i).getFormularioNUE());
                tras.setObservaciones(traslados.get(i).getObservaciones());
                tras.setTipoMotivoidMotivo(traslados.get(i).getTipoMotivoidMotivo());

                if (j == 0) {
                    tras.setUsuarioidUsuarioEntrega(traslados.get(i).getUsuarioidUsuarioEntrega());

                } else {
                    tras.setUsuarioidUsuarioEntrega(traslados.get(i).getUsuarioidUsuarioRecibe());

                }
                intercalado.add(tras);

            }

        }
        //System.out.println(intercalado.toString());
    }

    public String getCambia() {
        return cambia;
    }

    public void setCambia(String cambia) {
        this.cambia = cambia;
    }

    public List<Traslado> getIntercalado() {
        return intercalado;
    }

    public void setIntercalado(List<Traslado> intercalado) {
        this.intercalado = intercalado;
    }

    public List<EdicionFormulario> getEdicionesList() {
        return edicionesList;
    }

    public void setEdicionesList(List<EdicionFormulario> edicionesList) {
        this.edicionesList = edicionesList;
    }

    public List<Traslado> getTrasladosList() {
        return trasladosList;
    }

    public void setTrasladosList(List<Traslado> trasladosList) {
        this.trasladosList = trasladosList;
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

    public boolean isBloqueada() {
        return bloqueada;
    }

    public void setBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public boolean isDeshabBtnRecibir() {
        return deshabBtnRecibir;
    }

    public void setDeshabBtnRecibir(boolean deshabBtnRecibir) {
        this.deshabBtnRecibir = deshabBtnRecibir;
    }

    public String getNumeroParte() {
        return numeroParte;
    }

    public void setNumeroParte(String numeroParte) {
        this.numeroParte = numeroParte;
    }
    
    

}
