package entity;

import entity.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-02-29T14:54:22")
@StaticMetamodel(Cargo.class)
public class Cargo_ { 

    public static volatile SingularAttribute<Cargo, String> nombreCargo;
    public static volatile SingularAttribute<Cargo, Integer> idCargo;
    public static volatile ListAttribute<Cargo, Usuario> usuarioList;

}