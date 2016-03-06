package ejb;

import entity.Area;
import entity.Cargo;
import entity.EdicionFormulario;
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
import facade.EdicionFormularioFacadeLocal;
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
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Aracelly
 */
@Stateless
public class FormularioEJB implements FormularioEJBLocal {

    @EJB
    private FormularioEvidenciaFacadeLocal formularioEvidenciaFacade;

    @EJB
    private TipoEvidenciaFacadeLocal tipoEvidenciaFacade;

    @EJB
    private EvidenciaFacadeLocal evidenciaFacade;
    @EJB
    private SemaforoFacadeLocal semaforoFacade;
    @EJB
    private CargoFacadeLocal cargoFacade;
    @EJB
    private TipoUsuarioFacadeLocal tipoUsuarioFacade;
    @EJB
    private AreaFacadeLocal areaFacade;
    @EJB
    private TrasladoFacadeLocal trasladoFacade;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private TipoMotivoFacadeLocal tipoMotivoFacade;
    @EJB
    private FormularioFacadeLocal formularioFacade;
    @EJB
    private EdicionFormularioFacadeLocal edicionFormularioFacade;
    @EJB
    private ValidacionEJBLocal validacionEJB;

    static final Logger logger = Logger.getLogger(FormularioEJB.class.getName());

    @Override
    public Usuario obtenerPoseedorFormulario(Formulario formulario) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "obtenerPoseedorFormulario", formulario.getNue());
        //Busco todo slos traslados del formulario
        List<Traslado> trasladoList = traslados(formulario);
        Usuario usuarioPoseedor = formulario.getUsuarioidUsuarioInicia(); //usuario que inicia el formulario
        //Comparando fechas entre traslados
        if (!trasladoList.isEmpty()) {
            usuarioPoseedor = trasladoList.get(trasladoList.size() - 1).getUsuarioidUsuarioEntrega();
        }
        logger.exiting(this.getClass().getName(), "obtenerPoseedorFormulario", usuarioPoseedor.toString());
        return usuarioPoseedor;
    }

    //** trabajar en la consulta sql
    // por qué es necesario tener las ediciones de un solo usuario ?
    //@Override
    public List<EdicionFormulario> listaEdiciones(int nue, int idUser) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "listaEdiciones", nue + " " + idUser);
        List<EdicionFormulario> lista = new ArrayList();
        List<EdicionFormulario> response = new ArrayList();
        lista = edicionFormularioFacade.findAll();

        for (int i = 0; i < lista.size(); i++) {

            if (lista.get(i).getUsuarioidUsuario().getIdUsuario() == idUser && lista.get(i).getFormularioNUE().getNue() == nue) {
                response.add(lista.get(i));
            }
        }

        if (response.isEmpty()) {
            //response = null;
            logger.exiting(this.getClass().getName(), "listaEdiciones", "sin elementos");
            return response;
        }
        logger.exiting(this.getClass().getName(), "listaEdiciones", response.size());
        return response;
    }

    //observacion: se pueden reducir la cant de consultas usando como parametro de entrada un Formulario.
    @Override
    public List<EdicionFormulario> listaEdiciones(int nue) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "listaEdiciones");
        List<EdicionFormulario> retorno = new ArrayList<>();
        Formulario f = formularioFacade.findByNue(nue);
        if (f != null) {
            retorno = edicionFormularioFacade.listaEdiciones(f);
            if (retorno == null) {
                logger.severe("lista de ediciones es null");
                retorno = new ArrayList<>();
            }
        } else {
            logger.severe("formulario no encontrado");
        }
        logger.exiting(this.getClass().getName(), "listaEdiciones", retorno.size());
        return retorno;
    }

    @Override
    public Formulario findFormularioByNue(int nueAbuscar) {

        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "findFormularioByNue", nueAbuscar);

        Formulario formulario = formularioFacade.findByNue(nueAbuscar);
        if (formulario != null) {
            logger.exiting(this.getClass().getName(), "findFormularioByNue", formulario.toString());
            return formulario;
        }
        logger.exiting(this.getClass().getName(), "findFormularioByNue", "Error con formulario");
        return null;
    }

    //Acondicionado para la nueva regla de negocio
    @Override
    public String crearTraslado(Formulario formulario, Date fechaT, String observaciones, String motivoNext, Usuario uSesion, Usuario entrega) {
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

        //Busco todos los traslados del formulario
        List<Traslado> trasladoList = traslados(formulario);

        //Comparando fechas entre traslados
        if (!trasladoList.isEmpty() && trasladoList.size() >= 2 && !validacionEJB.compareFechas(fechaT, trasladoList.get(trasladoList.size() - 2).getFechaEntrega())) {
            logger.exiting(this.getClass().getName(), "crearTraslado", "Error con Fecha");
            return "Error, la fecha del nuevo traslado debe ser igual o posterior a la ultima fecha de traslado.";
        }

        //Comparando fecha entre traslado y formulario
        if (!validacionEJB.compareFechas(fechaT, formulario.getFechaOcurrido())) {
            logger.exiting(this.getClass().getName(), "crearTraslado", "Error con Fecha");
            return "Error, la fecha de traslado debe ser igual o posterior a la fecha del formulario.";
        }

        //verificando que usuario recibe sea distinto del usuario que entrega
        if (uSesion.getRutUsuario().equals(entrega.getRutUsuario())) { //si se trata del mismo usuario
            logger.exiting(this.getClass().getName(), "crearTraslado", "Usuario Entrega y Recibe son el mismo");
            return "El usuario que recibe la cadena de custodia debe ser distinto al usuario que la entrega.";
        }

        //Actualizando traslado
        Traslado ultimoTraslado = ultimoTraslado(formulario.getNue());
        ultimoTraslado.setFechaEntrega(fechaT);
        ultimoTraslado.setObservaciones(observaciones);
        ultimoTraslado.setUsuarioidUsuarioRecibe(uSesion);

        System.out.println("-------------------------------------------------------------------------------------------------------------");
        System.out.println("NUE ULTIMO TRASLADO: " + ultimoTraslado.getFormularioNUE().getNue());
        System.out.println("USUARIO ENTREGA ULTIMO TRASLADO: " + ultimoTraslado.getUsuarioidUsuarioEntrega().getNombreUsuario());
        System.out.println("MOTIVO ULTIMO TRASLADO: " + ultimoTraslado.getTipoMotivoidMotivo().getTipoMotivo());
        System.out.println("USUARIO RECIBE ULTIMO TRASLADO: " + ultimoTraslado.getUsuarioidUsuarioRecibe().getNombreUsuario());
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        //verificamos si el traslado se trata de un peritaje, lo cual pone al formulario en amarillo (y no se debe crear un siguiente traslado).
        //ultimoTraslado.getTipoMotivoidMotivo().getTipoMotivo()
        if (motivoNext.equals("Peritaje")) {
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            System.out.println("EL SIGUIENTE MOTIVO ES PERITAJE:" + motivoNext);
            System.out.println("SE PROCEDE A CAMBIAR EL SEMAFORO AMARILLO Y ACTUALIZAR EL FORMULARIO");
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            Semaforo semAmarillo = semaforoFacade.findByColor("Amarillo");
            if (semAmarillo == null) {
                logger.exiting(this.getClass().getName(), "crearTraslado", "No se encontro semaforo Amarillo");
                return "Error al modificar estado del semaforo.";
            }
            formulario.setSemaforoidSemaforo(semAmarillo);
            logger.info("se inicia la edición del formulario para dejarlo en amarillo");
            formularioFacade.edit(formulario);
            logger.info("se finaliza la edición del formulario para dejarlo en amarillo");

        }

        if (motivoNext.equals("ninguno")) { //indica que se esta recibiendo para peritaje. implica que se debe cerrar la cc y dejarla en verde.

            System.out.println("-------------------------------------------------------------------------------------------------------------");
            System.out.println("EL SIGUIENTE MOTIVO ES NINGUNO:" + motivoNext);
            System.out.println("SE PROCEDE A CAMBIAR EL SEMAFORO A VERDE Y ACTUALIZAR EL FORMULARIO");
            System.out.println("-------------------------------------------------------------------------------------------------------------");

            Semaforo semVerde = semaforoFacade.findByColor("Verde");
            if (semVerde == null) {
                logger.exiting(this.getClass().getName(), "crearTraslado", "No se encontro semaforo Verde");
                return "Error al modificar estado del semaforo.";
            }
            formulario.setSemaforoidSemaforo(semVerde);
            formulario.setBloqueado(true);
            logger.info("se inicia la edición del formulario para cambiar semaforo a verde");
            formularioFacade.edit(formulario);
            logger.info("se finaliza la edición del formulario para cambiar semaforo a verde");

        } else { //custodia o traslado, (no se  modifica semaforo, se debe crear un siguiente traslado)
            
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            System.out.println("EL SIGUIENTE MOTIVO:" + motivoNext);
            System.out.println("SE CREA UN NUEVO TRASLADO");
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            TipoMotivo motivoNextP = tipoMotivoFacade.findByTipoMotivo(motivoNext);
            if (motivoNextP == null) {
                logger.exiting(this.getClass().getName(), "crearTraslado", "No se encontro motivoNext");
                return "Error con el motivo de traslado.";
            }
            Traslado siguienteTraslado = new Traslado();
            siguienteTraslado.setUsuarioidUsuarioEntrega(uSesion);
            siguienteTraslado.setFormularioNUE(formulario);
            siguienteTraslado.setTipoMotivoidMotivo(motivoNextP);
            
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            System.out.println("USUARIO ENTREGA SIGUIENTE TRASLADO:" + siguienteTraslado.getUsuarioidUsuarioEntrega().getNombreUsuario());
            System.out.println("NUEVA DEL FORMULARIO SIGUIENTE TRASLADO: "+siguienteTraslado.getFormularioNUE().getNue());
            System.out.println("MOTIVO DEL SIGUIENTE TRASLADO: "+siguienteTraslado.getTipoMotivoidMotivo().getTipoMotivo());
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            
            logger.info("se inicia la creacion del siguiente traslado");
            trasladoFacade.create(siguienteTraslado);
            logger.info("se finaliza la creacion del siguiente traslado");
        }

        logger.info("se inicia actualizacion del traslado");
        trasladoFacade.edit(ultimoTraslado);
        logger.info("se finaliza actualizacion del traslado");

        logger.exiting(this.getClass().getName(), "crearTraslado", "Exito");
        return "Exito";

    }

    //** modificada para retornar una lista vacía si no encuentra resultados.
    @Override
    public List<Traslado> traslados(Formulario formulario) {
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

    //ZACK
    //Función que crea el formulario
    // el String de retorno se muentra como mensaje en la vista.
    @Override
    public String crearFormulario(String motivo, String ruc, String rit, int nue, int nParte, String delito, String direccionSS, String lugar, Date fecha, String observacion, String descripcion, String unidad, Usuario inicia) {

        //match con motivo
        TipoMotivo motivoP = tipoMotivoFacade.findByTipoMotivo(motivo);
        if (motivoP == null) {
            logger.exiting(this.getClass().getName(), "crearFormulario", "Error con motivo de entrega.");
            return "Error con motivo de entrega.";
        }

        Formulario nuevoFormulario = new Formulario();
        Semaforo estadoInicial;
        switch (motivo) {
            case "Peritaje":
                estadoInicial = semaforoFacade.findByColor("Amarillo");
                break;
            default:
                estadoInicial = semaforoFacade.findByColor("Rojo");
        }

        nuevoFormulario.setDireccionFotografia("C:");
        nuevoFormulario.setFechaIngreso(new Date(System.currentTimeMillis()));
        nuevoFormulario.setFechaOcurrido(fecha);
        nuevoFormulario.setUltimaEdicion(nuevoFormulario.getFechaIngreso());
        nuevoFormulario.setUsuarioidUsuario(inicia); // Usuario digitador
        nuevoFormulario.setUsuarioidUsuarioInicia(inicia); //Usuario inicia
        nuevoFormulario.setDescripcionEspecieFormulario(descripcion);
        nuevoFormulario.setObservaciones(observacion);
        nuevoFormulario.setDelitoRef(delito);
        nuevoFormulario.setNue(nue);
        nuevoFormulario.setNumeroParte(nParte);
        nuevoFormulario.setDelitoRef(delito);
        nuevoFormulario.setRuc(ruc);
        nuevoFormulario.setRit(rit);
        nuevoFormulario.setUnidadPolicial(unidad);
        nuevoFormulario.setLugarLevantamiento(lugar);
        nuevoFormulario.setDireccionSS(direccionSS);
        nuevoFormulario.setBloqueado(false);
        nuevoFormulario.setSemaforoidSemaforo(estadoInicial);

        logger.finest("se inicia la persistencia del nuevo formulario");
        formularioFacade.create(nuevoFormulario);
        logger.finest("se finaliza la persistencia del nuevo formulario");

        //creando primer traslado
        Traslado primerTraslado = new Traslado();
        primerTraslado.setFormularioNUE(formularioFacade.findByNue(nue));
        primerTraslado.setTipoMotivoidMotivo(motivoP);
        primerTraslado.setUsuarioidUsuarioEntrega(inicia);

        System.out.println("-------------------------------------------------------------------------------------------------------------");
        System.out.println("NUE PRIMER TRASLADO: " + primerTraslado.getFormularioNUE().getNue());
        System.out.println("USUARIO ENTREGA PRIMER  TRASLADO: " + primerTraslado.getUsuarioidUsuarioEntrega().getNombreUsuario());
        System.out.println("--------------------------------------------------------------------------------------------------------------");

        logger.finest("se inicia la persistencia del primer traslado");
        trasladoFacade.create(primerTraslado);
        logger.finest("se finaliza la persistencia del primer traslado");

        logger.exiting(this.getClass().getName(), "crearFormulario", true);
        return "Exito";

    }

    //se crea una nueva edicion para el formulario indicado.
    //modificado Ara
    @Override
    public String edicionFormulario(Formulario formulario, String obsEdicion, Usuario usuarioSesion, int parte, String ruc, String rit) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "edicionFormulario");
        System.out.println("EJB ruc " + ruc);
        System.out.println("EJB rit " + rit);
        System.out.println("EJB parte " + parte);
        System.out.println("EJB obs " + obsEdicion);

        if (!esParticipanteCC(formulario, usuarioSesion)) {
            logger.exiting(this.getClass().getName(), "edicionFormulario", "usuario no ha participado en cc");
            return "Usted no ha participado en esta cadena de custodia.";
        }

        if (obsEdicion.equals("") && parte == 0 && ruc == null && rit == null) { //si no viene ningún campo, al menos se necesita observacion
            logger.exiting(this.getClass().getName(), "edicionFormulario", "requiere observacion");
            return "Se requiere edición.";
        }

        if (!obsEdicion.equals("")) {
            //Creando el objeto edicion
            EdicionFormulario edFObs = new EdicionFormulario();

            edFObs.setFormularioNUE(formulario);
            edFObs.setUsuarioidUsuario(usuarioSesion);
            edFObs.setFechaEdicion(new Date(System.currentTimeMillis()));
            edFObs.setObservaciones(obsEdicion + "");
            //Actualizando ultima edicion formulario
            formulario.setUltimaEdicion(edFObs.getFechaEdicion());

            edFObs.setObservaciones(obsEdicion);
            edicionFormularioFacade.create(edFObs);
            formularioFacade.edit(formulario);
            logger.log(Level.INFO, "AAAAAAAAAAAAAA se inserto observacion {0}", obsEdicion);
        }
        if (parte > 0) {
            EdicionFormulario edFParte = new EdicionFormulario();

            edFParte.setFormularioNUE(formulario);
            edFParte.setUsuarioidUsuario(usuarioSesion);
            edFParte.setFechaEdicion(new Date(System.currentTimeMillis()));
            edFParte.setObservaciones(obsEdicion + "");
            //Actualizando ultima edicion formulario
            formulario.setUltimaEdicion(edFParte.getFechaEdicion());

            edFParte.setObservaciones("Se ingresa N° parte: " + parte);
            edicionFormularioFacade.create(edFParte);
            formulario.setNumeroParte(parte);
            formularioFacade.edit(formulario);
            logger.log(Level.INFO, "se ha insertado n Parte {0}", formulario.getNumeroParte());
        }
        if (rit != null && !rit.equals("") && validacionEJB.checkRucOrRit(rit)) {
            EdicionFormulario edFRit = new EdicionFormulario();

            edFRit.setFormularioNUE(formulario);
            edFRit.setUsuarioidUsuario(usuarioSesion);
            edFRit.setFechaEdicion(new Date(System.currentTimeMillis()));
            edFRit.setObservaciones(obsEdicion + "");
            //Actualizando ultima edicion formulario
            formulario.setUltimaEdicion(edFRit.getFechaEdicion());

            edFRit.setObservaciones("Se ingresa R.I.T.: " + rit);
            edicionFormularioFacade.create(edFRit);
            formulario.setRit(rit);
            formularioFacade.edit(formulario);
            logger.log(Level.INFO, "se ha insertado rit {0}", formulario.getRit());
        }
        if (ruc != null && !ruc.equals("") && validacionEJB.checkRucOrRit(ruc)) {
            EdicionFormulario edFRuc = new EdicionFormulario();

            edFRuc.setFormularioNUE(formulario);
            edFRuc.setUsuarioidUsuario(usuarioSesion);
            edFRuc.setFechaEdicion(new Date(System.currentTimeMillis()));
            edFRuc.setObservaciones(obsEdicion + "");
            //Actualizando ultima edicion formulario
            formulario.setUltimaEdicion(edFRuc.getFechaEdicion());

            edFRuc.setObservaciones("Se ingresa R.U.C.: " + ruc);
            edicionFormularioFacade.create(edFRuc);
            formulario.setRuc(ruc);
            formularioFacade.edit(formulario);
            logger.log(Level.INFO, "se ha insertado ruc {0}", formulario.getRuc());
        }

        logger.exiting(this.getClass().getName(), "edicionFormulario", "Exito");
        return "Exito";

    }

    //retorna true cuando el usuario si ha particiado en la cc.
    @Override
    public boolean esParticipanteCC(Formulario formulario, Usuario usuario) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "obtenerParticipantesCC");
        if (usuario.equals(formulario.getUsuarioidUsuarioInicia())) {
            logger.exiting(this.getClass().getName(), "obtenerParticipantesCC", true);
            return true;
        }
        List<Traslado> traslados = trasladoFacade.findByNue(formulario);
        if (traslados == null || traslados.isEmpty()) {
            logger.log(Level.INFO, "formulario ''{0}'' no registra traslados", formulario.getNue());
            logger.exiting(this.getClass().getName(), "obtenerParticipantesCC", false);
            return false;
        }
        for (int i = 0; i < traslados.size(); i++) {
            if (traslados.get(i).getUsuarioidUsuarioRecibe() != null && traslados.get(i).getUsuarioidUsuarioRecibe().equals(usuario)) {
                logger.exiting(this.getClass().getName(), "obtenerParticipantesCC", true);
                return true;
            }
            if (traslados.get(i).getUsuarioidUsuarioEntrega().equals(usuario)) {
                logger.exiting(this.getClass().getName(), "obtenerParticipantesCC", true);
                return true;
            }
        }

        logger.exiting(this.getClass().getName(), "obtenerParticipantesCC", false);
        return false;
    }

    @Override
    public List<Formulario> findByNParteRR(String input, String aBuscar) {

        List<Formulario> formulariosList = new ArrayList<>();

        if (input.equals("") || aBuscar.equals("")) {
            return formulariosList;
        }

        switch (aBuscar) {
            case "NumeroParte":
                if (validacionEJB.esNumero(input)) { //si no son solo numeros, retorna lista vacía.
                    formulariosList = formularioFacade.findByNParte(Integer.parseInt(input));
                }
                break;
            case "Ruc":
                if (validacionEJB.checkRucOrRit(input)) { //si no es un ruc valido, retorna lista vacia
                    formulariosList = formularioFacade.findByRuc(input);
                }
                break;
            default:
                //es rit
                if (validacionEJB.checkRucOrRit(input)) { //si no es un rit valido, retorna lista vacia
                    formulariosList = formularioFacade.findByRit(input);
                }
                break;
        }
        return formulariosList;
    }

    private Traslado ultimoTraslado(int nue) {
        logger.setLevel(Level.ALL);
        logger.entering(this.getClass().getName(), "ultimoTraslado", nue);
        Traslado traslado = null;
        Formulario f = formularioFacade.find(nue);
        if (f != null) {
            List<Traslado> trasladosList = trasladoFacade.findByNue(f);
            if (!trasladosList.isEmpty()) {
                traslado = trasladosList.get(trasladosList.size() - 1);
            }
        }
        logger.exiting(this.getClass().getName(), "ultimoTraslado", traslado.toString());
        return traslado;
    }

    @Override
    public List<Formulario> findAllFormularios() {

        List<Formulario> formularios = new ArrayList();

        formularios = formularioFacade.findAll();

        return formularios;
    }

    @Override
    public List<Cargo> findAllCargos() {

        List<Cargo> cargos = new ArrayList();
        cargos = cargoFacade.findAll();

        return cargos;
    }

    @Override
    public List<Area> findAllAreas() {
        List<Area> areas = new ArrayList();
        areas = areaFacade.findAll();

        return areas;
    }

    @Override
    public List<FormularioEvidencia> findEvidenciaFormularioByFormulario(Formulario formulario) {
        logger.entering(this.getClass().getName(), "findEvidenciaFormularioByFormulario", formulario.toString());
        List<FormularioEvidencia> result = formularioEvidenciaFacade.findByFormulario(formulario);
        if (result == null) {
            result = new ArrayList<>();
        }
        logger.exiting(this.getClass().getName(), "findEvidenciaFormularioByFormulario", formulario.toString());
        return result;
    }

}
