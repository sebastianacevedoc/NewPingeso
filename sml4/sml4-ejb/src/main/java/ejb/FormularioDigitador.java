/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import static ejb.FormularioEJB.logger;
import entity.Area;
import entity.Cargo;
import entity.Evidencia;
import entity.Formulario;
import entity.FormularioEvidencia;
import entity.Semaforo;
import entity.TipoEvidencia;
import entity.TipoMotivo;
import entity.TipoUsuario;
import entity.Traslado;
import entity.Usuario;
import facade.AreaFacadeLocal;
import facade.CargoFacadeLocal;
import facade.EvidenciaFacadeLocal;
import facade.FormularioEvidenciaFacadeLocal;
import facade.FormularioFacadeLocal;
import facade.SemaforoFacadeLocal;
import facade.TipoEvidenciaFacadeLocal;
import facade.TipoMotivoFacadeLocal;
import facade.TipoUsuarioFacadeLocal;
import facade.TrasladoFacadeLocal;
import facade.UsuarioFacadeLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author sebastian
 */
@Stateless
public class FormularioDigitador implements FormularioDigitadorLocal {

    @EJB
    private SemaforoFacadeLocal semaforoFacade;

    @EJB
    private FormularioEvidenciaFacadeLocal formularioEvidenciaFacade;
    @EJB
    private EvidenciaFacadeLocal evidenciaFacade;
    @EJB
    private TipoEvidenciaFacadeLocal tipoEvidenciaFacade;
    @EJB
    private FormularioFacadeLocal formularioFacade;
    @EJB
    private ValidacionEJBLocal validacionEJB;
    @EJB
    private TipoUsuarioFacadeLocal tipoUsuarioFacade;
    @EJB
    private AreaFacadeLocal areaFacade;
    @EJB
    private CargoFacadeLocal cargoFacade;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private TrasladoFacadeLocal trasladoFacade;
    @EJB
    private TipoMotivoFacadeLocal tipoMotivoFacade;

    //ZACK
    //Función que crea el formulario
    // el String de retorno se muentra como mensaje en la vista.
    @Override
    public String crearFormulario(String ruc, String rit, int nue, int nParte, String cargo, String delito, String direccionSS, String lugar, String unidadPolicial, String levantadoPor, String rut, Date fecha, String observacion, String descripcion, Usuario digitador) {

        //Verificando si existe un formulario con ese nue
        Formulario verificar = formularioFacade.findByNue(nue);
        if (verificar != null) {
            logger.exiting(this.getClass().getName(), "crearFormulario", "Error nue existente");
            return "Formulario existente";
        }
        //Verificando delito
        if (!validacionEJB.soloCaracteres(delito)) {
            logger.exiting(this.getClass().getName(), "crearFormulario", "Error con delito");
            return "Error con delito, debe ingresar solo caracteres";
        }
        //Verificando levantadoPor
        if (!validacionEJB.soloCaracteres(levantadoPor)) {
            logger.exiting(this.getClass().getName(), "crearFormulario", "Error con levantado por");
            return "Error con levantador por, debe ingresar solo caracteres.";
        }

        Date fechaActual = new Date();

        if (fecha.after(fechaActual)) {
            logger.exiting(this.getClass().getName(), "compareFechas", "error");
              return "Error con fecha ingresada, no puede ser superior a la fecha actual";
        }
        //ruc - rit- nparte - obs y descripcion no son obligatorios
        Usuario usuarioIngresar = null;
        //Verificando en la base de datos si existe el usuario con ese rut
        usuarioIngresar = usuarioFacade.findByRUN(rut);

        if (usuarioIngresar == null) {
            //En el caso que no exista, se crea 
            //Crea un externo, entregando el nombre, rut, y le entrega por defecto el area externo, cargo, otro y tipo de usuario externo.
            usuarioIngresar = crearExterno1(levantadoPor, rut);

            if (usuarioIngresar == null) {
                logger.exiting(this.getClass().getName(), "crearFormulario", "No se pudo crear el nuevo usuario");
                return "No se pudo crear el nuevo usuario";
            }
        }

        //match semaforo rojo         
        Semaforo semaforoP = semaforoFacade.findByColor("Rojo");
        if (semaforoP == null) {
            logger.exiting(this.getClass().getName(), "crearFormulario", "No se logro encontrar el semaforo de color rojo");
            return "Ocurrió un problema al guardar los datos.";
        }

        Formulario nuevoFormulario = new Formulario();

        nuevoFormulario.setDireccionFotografia("C:");
        nuevoFormulario.setFechaIngreso(new Date(System.currentTimeMillis()));
        nuevoFormulario.setFechaOcurrido(fecha);
        nuevoFormulario.setUltimaEdicion(nuevoFormulario.getFechaIngreso());
        nuevoFormulario.setUsuarioidUsuario(digitador); //Hace referencia a la persona que esta digitalizando el formulario
        nuevoFormulario.setUsuarioidUsuarioInicia(usuarioIngresar); //Hace referencia el usuario que inicia la cadena
        nuevoFormulario.setDescripcionEspecieFormulario(descripcion);
        nuevoFormulario.setObservaciones(observacion);
        nuevoFormulario.setDelitoRef(delito);
        nuevoFormulario.setNue(nue);
        nuevoFormulario.setNumeroParte(nParte);
        nuevoFormulario.setDelitoRef(delito);
        nuevoFormulario.setRuc(ruc);
        nuevoFormulario.setRit(rit);
        nuevoFormulario.setUnidadPolicial(unidadPolicial);
        nuevoFormulario.setLugarLevantamiento(lugar);
        nuevoFormulario.setDireccionSS(direccionSS);
        nuevoFormulario.setBloqueado(false);
        nuevoFormulario.setSemaforoidSemaforo(semaforoP);

        logger.finest("se inicia la persistencia del nuevo formulario");
        formularioFacade.create(nuevoFormulario);

        logger.finest("se finaliza la persistencia del nuevo formulario");
        logger.exiting(this.getClass().getName(), "crearFormulario", true);
        return "Exito";

    }
    //ZACK

    private Usuario crearExterno1(String levantadoPor, String rut) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "crearExterno1");

        Usuario nuevoExterno = new Usuario();
        //area se esta entregando Externo
        Area areaExterno = areaFacade.findByArea("Externo");
        if (areaExterno == null) {
            logger.exiting(this.getClass().getName(), "crearExterno1", "problema al buscar area externo");
            return null;
        }

        TipoUsuario tue = tipoUsuarioFacade.findByTipo("Externo");
        if (tue == null) {
            logger.exiting(this.getClass().getName(), "crearExterno1", "problema al buscar tipo usuario externo");
            return null;
        }
        //buscando cargo, en el caso que no exista se crea

        Cargo cargoExterno = cargoFacade.findByCargo("Otro");
        if (cargoExterno == null) {
            logger.exiting(this.getClass().getName(), "crearExterno1", "problema al buscar cargo otro");
            return null;
        }

        nuevoExterno.setNombreUsuario(levantadoPor);
        nuevoExterno.setRutUsuario(rut);
        nuevoExterno.setAreaidArea(areaExterno);
        nuevoExterno.setCargoidCargo(cargoExterno);
        nuevoExterno.setTipoUsuarioidTipoUsuario(tue);
        nuevoExterno.setEstadoUsuario(Boolean.TRUE);
        nuevoExterno.setMailUsuario("na");
        nuevoExterno.setPassUsuario("na");
        logger.finest("se inicia la persistencia del nuevo usuario externo");
        usuarioFacade.create(nuevoExterno);
        logger.finest("se finaliza la persistencia del nuevo usuario externo");

        Usuario newExterno = usuarioFacade.findByRUN(rut);;

        if (newExterno != null) {
            logger.exiting(this.getClass().getName(), "crearExterno1", "retornando nuevo usuario externo");
            return newExterno;
        }
        logger.exiting(this.getClass().getName(), "crearExterno1", "no se pudo retorna el usuario externo");
        return null;
    }

    @Override
    public String crearTraslado(Formulario formulario, Usuario usuarioInicia, String usuarioRecibe, String usuarioRecibeCargo, String usuarioRecibeRut, String usuarioRecibeUnidad, Date fechaT, String observaciones, String motivo, Usuario uSesion) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "crearTraslado");

        if (formulario == null) {
            logger.exiting(this.getClass().getName(), "crearTraslado", "Formulario nulo");
            return "Imposible agregar traslado, ocurrió un problema al cargar el formulario, por favor intente más tarde.";
        }

        //verificamos que el formulario no se encuentre bloqueado.
        if (formulario.getBloqueado()) {
            logger.exiting(this.getClass().getName(), "crearTraslado", "Formulario bloqueado");
            return "Imposible agregar traslado, esta cadena de custodia se encuentra cerrada.";
        }

        if (!validacionEJB.soloCaracteres(usuarioRecibe)) {
            logger.exiting(this.getClass().getName(), "crearTraslado", "Error verificacion datos usuario recibe");
            return "Error con nombre usuario Recibe, solo permite caracteres";
        }

        if (!validacionEJB.soloCaracteres(usuarioRecibeCargo)) {
            logger.exiting(this.getClass().getName(), "crearTraslado", "Error verificacion datos usuario recibe cargo");
            return "Error con el cargo ingresado, permite solo caracteres";
        }
        if (!validacionEJB.soloCaracteres(usuarioRecibeUnidad)) {
            logger.exiting(this.getClass().getName(), "crearTraslado", "Error verificacion datos usuario recibe unidad");
            return "Error con la unidad ingresada, permite solo caracteres";
        }
        //Busco todos los traslados del formulario
        List<Traslado> trasladoList = traslados(formulario);

        Date fechaActual = new Date();
        //valido que la fecha ingresada no sea mayor que la actual
        if (fechaT.after(fechaActual)) {
                logger.exiting(this.getClass().getName(), "compareFechas", true);
                return "Error con fecha ingresada, no puede ser superior a la fecha actual";
            }
        
        //Comparando fecha entre traslado y formulario
        if (!validacionEJB.compareFechas(fechaT, formulario.getFechaOcurrido())) {
            logger.exiting(this.getClass().getName(), "crearTraslado", "Error con Fecha");
            return "Error, la fecha de traslado debe ser igual o posterior a la fecha del formulario.";
        }

        //Comparando fechas entre traslados
        if (!trasladoList.isEmpty() && !validacionEJB.compareFechas(fechaT, trasladoList.get(trasladoList.size() - 1).getFechaEntrega())) {
            logger.exiting(this.getClass().getName(), "crearTraslado", "Error con Fecha");
            return "Error, la fecha del nuevo traslado debe ser igual o posterior a la ultima fecha de traslado.";
        }

        //traer usuarios, motivo
        TipoMotivo motivoP = tipoMotivoFacade.findByTipoMotivo(motivo);
        if (motivoP == null) {
            logger.exiting(this.getClass().getName(), "crearTraslado", "Error con Motivo de Traslado");
            return "Error, se requiere especificar Motivo del traslado.";
        }

        Usuario usuarioRecibeP = null;

        //Verificando usuario Recibe
        usuarioRecibeP = usuarioFacade.findByRUN(usuarioRecibeRut);
        if (usuarioRecibeP == null) {
            usuarioRecibeP = crearExterno1(usuarioRecibe, usuarioRecibeRut);
            if (usuarioRecibeP == null) {
                logger.exiting(this.getClass().getName(), "crearTraslado", "Error con creacion usuario Recibe");
                return "Error con datos de la persona que recibe.";
            }
        }

        //verificando que usuario recibe sea distinto del usuario que entrega
        if (usuarioInicia.getRutUsuario().equals(usuarioRecibeP.getRutUsuario())) { //si se trata del mismo usuario
            logger.exiting(this.getClass().getName(), "crearTraslado", "Usuario Entrega y Recibe son el mismo");
            return "El usuario que recibe la cadena de custodia debe ser distinto al usuario que la entrega.";
        }

        //Creando traslado
        Traslado nuevoTraslado = new Traslado();
        nuevoTraslado.setFechaEntrega(fechaT);
        nuevoTraslado.setFormularioNUE(formulario);
        nuevoTraslado.setObservaciones(observaciones);
        nuevoTraslado.setTipoMotivoidMotivo(motivoP);
        nuevoTraslado.setUsuarioidUsuarioRecibe(usuarioRecibeP);
        nuevoTraslado.setUsuarioidUsuarioEntrega(usuarioInicia);

        logger.info("se inicia insercion del nuevo traslado");
        trasladoFacade.create(nuevoTraslado);
        logger.info("se finaliza insercion del nuevo traslado");

        //verificamos si se se trata de un peritaje, lo cual finaliza la cc.
        if (nuevoTraslado.getTipoMotivoidMotivo().getTipoMotivo().equals("Peritaje")) {
            //quite la restriccion de que solo puede cerrar un tecnico o perito, porque dadas las circunstancias, no se puede asegurar.
            logger.info("se realiza peritaje, por tanto se finaliza la cc.");
            formulario.setBloqueado(true);
            logger.info("se inicia la edición del formulario para bloquearlo");
            formularioFacade.edit(formulario);
            logger.info("se finaliza la edición del formulario para bloquearlo");
        }

        logger.exiting(this.getClass().getName(), "crearTraslado", "Exito");
        return "Exito";

    }

    //** modificada para retornar una lista vacía si no encuentra resultados.
    private List<Traslado> traslados(Formulario formulario) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "traslados", formulario.toString());
        List<Traslado> retorno = trasladoFacade.findByNue(formulario);
        if (retorno == null) {
            retorno = new ArrayList<>();
            logger.exiting(this.getClass().getName(), "traslados", retorno.size());
            return retorno;
        } else {
            logger.exiting(this.getClass().getName(), "traslados", retorno.size());
            return retorno;
        }
    }

}
