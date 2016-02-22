/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Formulario;
import java.sql.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author sebastian
 */
@Local
public interface FormularioFacadeLocal {

    void create(Formulario formulario);

    void edit(Formulario formulario);

    void remove(Formulario formulario);

    Formulario find(Object id);

    List<Formulario> findAll();

    List<Formulario> findRange(int[] range);

    int count();

    Formulario findByNue(int nue);
    
    List<Formulario> findByNParte(int nParte);
    
    List<Formulario> findByRuc(String ruc);
    
    List<Formulario> findByRit(String rit);
    
}