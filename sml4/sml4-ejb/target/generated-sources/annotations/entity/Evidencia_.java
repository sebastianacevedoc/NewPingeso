package entity;

import entity.FormularioEvidencia;
import entity.TipoEvidencia;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-19T02:51:13")
@StaticMetamodel(Evidencia.class)
public class Evidencia_ { 

    public static volatile SingularAttribute<Evidencia, Integer> idEvidencia;
    public static volatile ListAttribute<Evidencia, FormularioEvidencia> formularioEvidenciaList;
    public static volatile SingularAttribute<Evidencia, TipoEvidencia> tipoEvidenciaidTipoEvidencia;
    public static volatile SingularAttribute<Evidencia, String> nombreEvidencia;

}