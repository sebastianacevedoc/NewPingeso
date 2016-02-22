/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alan
 */
@Entity
@Table(name = "formulario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Formulario.findAll", query = "SELECT f FROM Formulario f"),
    @NamedQuery(name = "Formulario.findByNue", query = "SELECT f FROM Formulario f WHERE f.nue = :nue"),
    @NamedQuery(name = "Formulario.findByFechaIngreso", query = "SELECT f FROM Formulario f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "Formulario.findByRuc", query = "SELECT f FROM Formulario f WHERE f.ruc = :ruc"),
    @NamedQuery(name = "Formulario.findByRit", query = "SELECT f FROM Formulario f WHERE f.rit = :rit"),
    @NamedQuery(name = "Formulario.findByDireccionFotografia", query = "SELECT f FROM Formulario f WHERE f.direccionFotografia = :direccionFotografia"),
    @NamedQuery(name = "Formulario.findByFechaOcurrido", query = "SELECT f FROM Formulario f WHERE f.fechaOcurrido = :fechaOcurrido"),
    @NamedQuery(name = "Formulario.findByLugarLevantamiento", query = "SELECT f FROM Formulario f WHERE f.lugarLevantamiento = :lugarLevantamiento"),
    @NamedQuery(name = "Formulario.findByNumeroParte", query = "SELECT f FROM Formulario f WHERE f.numeroParte = :numeroParte"),
    @NamedQuery(name = "Formulario.findByObservaciones", query = "SELECT f FROM Formulario f WHERE f.observaciones = :observaciones"),
    @NamedQuery(name = "Formulario.findByDireccionSS", query = "SELECT f FROM Formulario f WHERE f.direccionSS = :direccionSS"),
    @NamedQuery(name = "Formulario.findByDelitoRef", query = "SELECT f FROM Formulario f WHERE f.delitoRef = :delitoRef"),
    @NamedQuery(name = "Formulario.findByDescripcionEspecieFormulario", query = "SELECT f FROM Formulario f WHERE f.descripcionEspecieFormulario = :descripcionEspecieFormulario"),
    @NamedQuery(name = "Formulario.findByUltimaEdicion", query = "SELECT f FROM Formulario f WHERE f.ultimaEdicion = :ultimaEdicion"),
    @NamedQuery(name = "Formulario.findByBloqueado", query = "SELECT f FROM Formulario f WHERE f.bloqueado = :bloqueado"),
    @NamedQuery(name = "Formulario.findByUnidadPolicial", query = "SELECT f FROM Formulario f WHERE f.unidadPolicial = :unidadPolicial")})
public class Formulario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUE")
    private Integer nue;
    @Column(name = "fechaIngreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 45)
    @Column(name = "RUC")
    private String ruc;
    @Size(max = 45)
    @Column(name = "RIT")
    private String rit;
    @Size(max = 60)
    @Column(name = "direccionFotografia")
    private String direccionFotografia;
    @Column(name = "fechaOcurrido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaOcurrido;
    @Size(max = 50)
    @Column(name = "lugarLevantamiento")
    private String lugarLevantamiento;
    @Column(name = "numeroParte")
    private Integer numeroParte;
    @Size(max = 300)
    @Column(name = "observaciones")
    private String observaciones;
    @Size(max = 50)
    @Column(name = "direccionSS")
    private String direccionSS;
    @Size(max = 45)
    @Column(name = "delitoRef")
    private String delitoRef;
    @Size(max = 300)
    @Column(name = "descripcionEspecieFormulario")
    private String descripcionEspecieFormulario;
    @Column(name = "ultimaEdicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaEdicion;
    @Column(name = "bloqueado")
    private Boolean bloqueado;
    @Size(max = 45)
    @Column(name = "unidadPolicial")
    private String unidadPolicial;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formularioNUE", fetch = FetchType.EAGER)
    private List<FormularioEvidencia> formularioEvidenciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formularioNUE", fetch = FetchType.EAGER)
    private List<Traslado> trasladoList;
    @JoinColumn(name = "Usuario_idUsuarioInicia", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Usuario usuarioidUsuarioInicia;
    @JoinColumn(name = "Usuario_idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Usuario usuarioidUsuario;
    @JoinColumn(name = "Semaforo_idSemaforo", referencedColumnName = "idSemaforo")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Semaforo semaforoidSemaforo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formularioNUE", fetch = FetchType.EAGER)
    private List<EdicionFormulario> edicionFormularioList;

    public Formulario() {
    }

    public Formulario(Integer nue) {
        this.nue = nue;
    }

    public Integer getNue() {
        return nue;
    }

    public void setNue(Integer nue) {
        this.nue = nue;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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

    public String getDireccionFotografia() {
        return direccionFotografia;
    }

    public void setDireccionFotografia(String direccionFotografia) {
        this.direccionFotografia = direccionFotografia;
    }

    public Date getFechaOcurrido() {
        return fechaOcurrido;
    }

    public void setFechaOcurrido(Date fechaOcurrido) {
        this.fechaOcurrido = fechaOcurrido;
    }

    public String getLugarLevantamiento() {
        return lugarLevantamiento;
    }

    public void setLugarLevantamiento(String lugarLevantamiento) {
        this.lugarLevantamiento = lugarLevantamiento;
    }

    public Integer getNumeroParte() {
        return numeroParte;
    }

    public void setNumeroParte(Integer numeroParte) {
        this.numeroParte = numeroParte;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getDireccionSS() {
        return direccionSS;
    }

    public void setDireccionSS(String direccionSS) {
        this.direccionSS = direccionSS;
    }

    public String getDelitoRef() {
        return delitoRef;
    }

    public void setDelitoRef(String delitoRef) {
        this.delitoRef = delitoRef;
    }

    public String getDescripcionEspecieFormulario() {
        return descripcionEspecieFormulario;
    }

    public void setDescripcionEspecieFormulario(String descripcionEspecieFormulario) {
        this.descripcionEspecieFormulario = descripcionEspecieFormulario;
    }

    public Date getUltimaEdicion() {
        return ultimaEdicion;
    }

    public void setUltimaEdicion(Date ultimaEdicion) {
        this.ultimaEdicion = ultimaEdicion;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getUnidadPolicial() {
        return unidadPolicial;
    }

    public void setUnidadPolicial(String unidadPolicial) {
        this.unidadPolicial = unidadPolicial;
    }

    @XmlTransient
    public List<FormularioEvidencia> getFormularioEvidenciaList() {
        return formularioEvidenciaList;
    }

    public void setFormularioEvidenciaList(List<FormularioEvidencia> formularioEvidenciaList) {
        this.formularioEvidenciaList = formularioEvidenciaList;
    }

    @XmlTransient
    public List<Traslado> getTrasladoList() {
        return trasladoList;
    }

    public void setTrasladoList(List<Traslado> trasladoList) {
        this.trasladoList = trasladoList;
    }

    public Usuario getUsuarioidUsuarioInicia() {
        return usuarioidUsuarioInicia;
    }

    public void setUsuarioidUsuarioInicia(Usuario usuarioidUsuarioInicia) {
        this.usuarioidUsuarioInicia = usuarioidUsuarioInicia;
    }

    public Usuario getUsuarioidUsuario() {
        return usuarioidUsuario;
    }

    public void setUsuarioidUsuario(Usuario usuarioidUsuario) {
        this.usuarioidUsuario = usuarioidUsuario;
    }

    public Semaforo getSemaforoidSemaforo() {
        return semaforoidSemaforo;
    }

    public void setSemaforoidSemaforo(Semaforo semaforoidSemaforo) {
        this.semaforoidSemaforo = semaforoidSemaforo;
    }

    @XmlTransient
    public List<EdicionFormulario> getEdicionFormularioList() {
        return edicionFormularioList;
    }

    public void setEdicionFormularioList(List<EdicionFormulario> edicionFormularioList) {
        this.edicionFormularioList = edicionFormularioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nue != null ? nue.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Formulario)) {
            return false;
        }
        Formulario other = (Formulario) object;
        if ((this.nue == null && other.nue != null) || (this.nue != null && !this.nue.equals(other.nue))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Formulario[ nue=" + nue + " ]";
    }
    
}
