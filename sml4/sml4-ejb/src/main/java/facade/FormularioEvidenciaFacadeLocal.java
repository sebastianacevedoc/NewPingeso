/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Formulario;
import entity.FormularioEvidencia;
import entity.TipoEvidencia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Alan
 */
@Local
public interface FormularioEvidenciaFacadeLocal {

    void create(FormularioEvidencia formularioEvidencia);

    void edit(FormularioEvidencia formularioEvidencia);

    void remove(FormularioEvidencia formularioEvidencia);

    FormularioEvidencia find(Object id);

    List<FormularioEvidencia> findAll();

    List<FormularioEvidencia> findRange(int[] range);

    int count();
    
    List<FormularioEvidencia> findByFormulario(Formulario formulario); 
    
    List<FormularioEvidencia> findByTE(TipoEvidencia te);
}