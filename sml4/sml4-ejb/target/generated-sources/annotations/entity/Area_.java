package entity;

import entity.TipoEvidencia;
import entity.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-24T16:30:58")
@StaticMetamodel(Area.class)
public class Area_ { 

    public static volatile ListAttribute<Area, TipoEvidencia> tipoEvidenciaList;
    public static volatile SingularAttribute<Area, Integer> idArea;
    public static volatile SingularAttribute<Area, String> nombreArea;
    public static volatile ListAttribute<Area, Usuario> usuarioList;

}