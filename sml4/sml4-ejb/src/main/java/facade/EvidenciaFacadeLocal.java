/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Evidencia;
import entity.TipoEvidencia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author sebastian
 */
@Local
public interface EvidenciaFacadeLocal {

    void create(Evidencia evidencia);

    void edit(Evidencia evidencia);

    void remove(Evidencia evidencia);

    Evidencia find(Object id);

    List<Evidencia> findAll();

    List<Evidencia> findRange(int[] range);

    int count();

     Evidencia findByNombreAndTipoEvidencia(String evidencia, TipoEvidencia tipoEvid);

     List<Evidencia> evidenciasT(TipoEvidencia tipoEvid);

}
