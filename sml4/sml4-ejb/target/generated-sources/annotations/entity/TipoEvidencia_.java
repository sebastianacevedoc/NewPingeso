package entity;

import entity.Area;
import entity.Evidencia;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-24T16:30:58")
@StaticMetamodel(TipoEvidencia.class)
public class TipoEvidencia_ { 

    public static volatile SingularAttribute<TipoEvidencia, Integer> idTipoEvidencia;
    public static volatile SingularAttribute<TipoEvidencia, String> nombreTipoEvidencia;
    public static volatile SingularAttribute<TipoEvidencia, Area> areaidArea;
    public static volatile ListAttribute<TipoEvidencia, Evidencia> evidenciaList;

}