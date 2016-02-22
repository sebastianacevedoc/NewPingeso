/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Formulario;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;

/**
 *
 * @author sebastian
 */
@Stateless
public class FormularioFacade extends AbstractFacade<Formulario> implements FormularioFacadeLocal {

    @PersistenceContext(unitName = "com.pingeso_sml4-ejb_ejb_3.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FormularioFacade() {
        super(Formulario.class);
    }

    //@NamedQuery(name = "Formulario.findByNue", query = "SELECT f FROM Formulario f WHERE f.nue = :nue")
    @Override
    public Formulario findByNue(int nue) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "findByNue", nue);
        Formulario retorno = null;
        try {
            Query q = em.createNamedQuery("Formulario.findByNue", Formulario.class).setParameter("nue", nue);
            //retorno = new Usuario();
            retorno = (Formulario) q.getSingleResult();
            logger.log(Level.INFO, "buscar formulario by nue -> {0}", nue);
        } catch (IllegalArgumentException e) {
            logger.severe("FormularioFacade: el nombre o el parametro de la Query no existe -> " + e);
            retorno = null;
        } catch (NoResultException e) {
            logger.severe("FormularioFacade: No hay resultados -> " + e);
            retorno = null;
        } catch (NonUniqueResultException e) {
            logger.severe("FormularioFacade: hay mas de un resulado -> " + e);
            retorno = null;
        } catch (IllegalStateException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (QueryTimeoutException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (TransactionRequiredException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (PessimisticLockException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (LockTimeoutException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (PersistenceException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        }
        if (retorno == null) {
            logger.exiting(this.getClass().getName(), "findByNue", null);
            return null;
        } else {
            logger.exiting(this.getClass().getName(), "findByNue", retorno.toString());
            return retorno;
        }
    }

    @Override
    public List<Formulario> findByNParte(int nParte) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "findByNParte", nParte);
        List<Formulario> retorno = null;
        try {
            Query q = em.createNamedQuery("Formulario.findByNumeroParte", Formulario.class).setParameter("numeroParte", nParte);
            retorno = q.getResultList();
            logger.log(Level.INFO, "buscar formulario by numero de parte -> {0}", nParte);
        } catch (IllegalArgumentException e) {
            logger.severe("FormularioFacade: el nombre o el parametro de la Query no existe -> " + e);
            retorno = null;
        } catch (NoResultException e) {
            logger.severe("FormularioFacade: No hay resultados -> " + e);
            retorno = null;
        } catch (NonUniqueResultException e) {
            logger.severe("FormularioFacade: hay mas de un resulado -> " + e);
            retorno = null;
        } catch (IllegalStateException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (QueryTimeoutException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (TransactionRequiredException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (PessimisticLockException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (LockTimeoutException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (PersistenceException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        }
        if (retorno == null) {
            logger.exiting(this.getClass().getName(), "findByNParte", null);
            return null;
        } else {
            logger.exiting(this.getClass().getName(), "findByNParte", retorno.toString());
            return retorno;
        }
    }

    @Override
    public List<Formulario> findByRuc(String ruc) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "findByRuc", ruc);
        List<Formulario> retorno = null;
        try {
            Query q = em.createNamedQuery("Formulario.findByRuc", Formulario.class).setParameter("ruc", ruc);
            retorno = q.getResultList();
            logger.log(Level.INFO, "buscar formulario by ruc -> {0}", ruc);
        } catch (IllegalArgumentException e) {
            logger.severe("FormularioFacade: el nombre o el parametro de la Query no existe -> " + e);
            retorno = null;
        } catch (NoResultException e) {
            logger.severe("FormularioFacade: No hay resultados -> " + e);
            retorno = null;
        } catch (NonUniqueResultException e) {
            logger.severe("FormularioFacade: hay mas de un resulado -> " + e);
            retorno = null;
        } catch (IllegalStateException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (QueryTimeoutException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (TransactionRequiredException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (PessimisticLockException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (LockTimeoutException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (PersistenceException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        }
        if (retorno == null) {
            logger.exiting(this.getClass().getName(), "findByRuc", null);
            return null;
        } else {
            logger.exiting(this.getClass().getName(), "findByRuc", retorno.toString());
            return retorno;
        }
    }

    @Override
    public List<Formulario> findByRit(String rit) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "findByRit", rit);
        List<Formulario> retorno = null;
        try {
            Query q = em.createNamedQuery("Formulario.findByRit", Formulario.class).setParameter("rit", rit);
            retorno = q.getResultList();
            logger.log(Level.INFO, "buscar formulario by rit -> {0}", rit);
        } catch (IllegalArgumentException e) {
            logger.severe("FormularioFacade: el nombre o el parametro de la Query no existe -> " + e);
            retorno = null;
        } catch (NoResultException e) {
            logger.severe("FormularioFacade: No hay resultados -> " + e);
            retorno = null;
        } catch (NonUniqueResultException e) {
            logger.severe("FormularioFacade: hay mas de un resulado -> " + e);
            retorno = null;
        } catch (IllegalStateException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (QueryTimeoutException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (TransactionRequiredException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (PessimisticLockException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (LockTimeoutException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        } catch (PersistenceException e) {
            logger.severe("FormularioFacade: ocurrio un problema con la consulta -> " + e);
            retorno = null;
        }
        if (retorno == null) {
            logger.exiting(this.getClass().getName(), "findByRit", null);
            return null;
        } else {
            logger.exiting(this.getClass().getName(), "findByRit", retorno.toString());
            return retorno;
        }
    }


}
