package entity;

import entity.Formulario;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-07T12:23:58")
@StaticMetamodel(Semaforo.class)
public class Semaforo_ { 

    public static volatile SingularAttribute<Semaforo, String> descripcionSemaforo;
    public static volatile ListAttribute<Semaforo, Formulario> formularioList;
    public static volatile SingularAttribute<Semaforo, Integer> idSemaforo;
    public static volatile SingularAttribute<Semaforo, String> semaforo;

}