/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.chofer;

import ejb.FormularioEJBLocal;
import ejb.UsuarioEJBLocal;
import entity.EdicionFormulario;
import entity.Formulario;
import entity.FormularioEvidencia;
import entity.Traslado;
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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Aracelly
 */
@Named(value = "todoChoferMB")
@RequestScoped
@ManagedBean
public class TodoChoferMB {

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

    private String evidencia;

    private int contador = 1;

    private String cambia;

    private List<Traslado> intercalado;

    static final Logger logger = Logger.getLogger(TodoChoferMB.class.getName());

    public TodoChoferMB() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "TodoChoferMB");
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
        logger.exiting(this.getClass().getName(), "TodoChoferMB");
    }

    @PostConstruct
    public void cargarDatos() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "cargarDatosChofer");
        this.formulario = formularioEJB.findFormularioByNue(this.nue);
        this.usuarioSesion = usuarioEJB.findUsuarioSesionByCuenta(usuarioSis);

        this.trasladosList = formularioEJB.traslados(this.formulario);

        this.edicionesList = formularioEJB.listaEdiciones(nue);

        this.bloqueada = formulario.getBloqueado();
        this.editable = formularioEJB.esParticipanteCC(formulario, usuarioSesion);
        logger.log(Level.INFO, "editable {0}", editable);
        if (bloqueada) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Esta cadena de custodia se encuentra cerrada.", ""));
        }

        intercalado(trasladosList);

        logger.log(Level.INFO, "formulario ruc {0}", this.formulario.getRuc());
        logger.log(Level.FINEST, "todos cant traslados {0}", this.trasladosList.size());
        logger.exiting(this.getClass().getName(), "cargarDatosChofer");
    }

    public String salir() {
        logger.setLevel(Level.ALL);
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

    //envia a la pagina para realizar una edicion en este formulario.
    public String editar() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "editar");
        httpServletRequest.getSession().setAttribute("nueF", this.nue);
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        logger.exiting(this.getClass().getName(), "editar", "editarChoferET");
        return "editarChoferET.xhtml?faces-redirect=true";
    }

    public String nuevaCadena() {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "nuevaCadena");
        //Enviando usuario
        httpServletRequest1.getSession().setAttribute("cuentaUsuario", this.usuarioSis);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nueva Cadena", "Ir a nuevo formulario"));
        logger.exiting(this.getClass().getName(), "nuevaCadena", "choferFormulario");
        return "choferFormulario?faces-redirect=true";
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
        System.out.println(intercalado.toString());
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


    public String getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(String evidencia) {
        this.evidencia = evidencia;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
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
    
    
}
