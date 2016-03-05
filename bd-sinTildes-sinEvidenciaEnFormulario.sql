CREATE DATABASE  IF NOT EXISTS `mydb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mydb`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	5.7.9-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `area`
--

DROP TABLE IF EXISTS `area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `area` (
  `idArea` int(11) NOT NULL AUTO_INCREMENT,
  `nombreArea` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idArea`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area`
--

LOCK TABLES `area` WRITE;
/*!40000 ALTER TABLE `area` DISABLE KEYS */;
INSERT INTO `area` VALUES (1,'Laboratorio SML'),(2,'LACRIM'),(3,'LABOCAR'),(4,'Tanatología SML'),(5,'Otro'),(6,'Clínica SML');
/*!40000 ALTER TABLE `area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cargo`
--

DROP TABLE IF EXISTS `cargo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cargo` (
  `idCargo` int(11) NOT NULL AUTO_INCREMENT,
  `nombreCargo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idCargo`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cargo`
--

LOCK TABLES `cargo` WRITE;
/*!40000 ALTER TABLE `cargo` DISABLE KEYS */;
INSERT INTO `cargo` VALUES (1,'Digitador'),(2,'Administrativo'),(3,'Perito'),(4,'Tecnico'),(5,'Jefe de area'),(6,'Chofer'),(7,'Externo');
/*!40000 ALTER TABLE `cargo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `edicion_formulario`
--

DROP TABLE IF EXISTS `edicion_formulario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `edicion_formulario` (
  `idEdicion` int(11) NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` int(11) NOT NULL,
  `Formulario_NUE` int(11) NOT NULL,
  `fechaEdicion` datetime DEFAULT NULL,
  `observaciones` varchar(400) DEFAULT NULL,
  PRIMARY KEY (`idEdicion`),
  KEY `fk_Edicion_Formulario_Usuario1_idx` (`Usuario_idUsuario`),
  KEY `fk_Edicion_Formulario_Formulario1_idx` (`Formulario_NUE`),
  CONSTRAINT `fk_Edicion_Formulario_Formulario1` FOREIGN KEY (`Formulario_NUE`) REFERENCES `formulario` (`NUE`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Edicion_Formulario_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `edicion_formulario`
--

LOCK TABLES `edicion_formulario` WRITE;
/*!40000 ALTER TABLE `edicion_formulario` DISABLE KEYS */;
INSERT INTO `edicion_formulario` VALUES (1,7,1313,'2016-03-05 02:54:58',' '),(2,7,1313,'2016-03-05 03:03:28','Se ingresa N° parte: 12341234'),(3,3,1717,'2016-03-05 03:18:04','Se ingresa R.I.T: 1313-13'),(4,3,1717,'2016-03-05 03:18:19','Se ingresa R.U.C.: 1313-13'),(5,3,1717,'2016-03-05 03:19:04','Se ingresa N° parte: 5465446');
/*!40000 ALTER TABLE `edicion_formulario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evidencia`
--

DROP TABLE IF EXISTS `evidencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evidencia` (
  `idEvidencia` int(11) NOT NULL,
  `nombreEvidencia` varchar(45) DEFAULT NULL,
  `Tipo_Evidencia_idTipoEvidencia` int(11) NOT NULL,
  PRIMARY KEY (`idEvidencia`),
  KEY `fk_Evidencia_Tipo_Evidencia1_idx` (`Tipo_Evidencia_idTipoEvidencia`),
  CONSTRAINT `fk_Evidencia_Tipo_Evidencia1` FOREIGN KEY (`Tipo_Evidencia_idTipoEvidencia`) REFERENCES `tipo_evidencia` (`idTipoEvidencia`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evidencia`
--

LOCK TABLES `evidencia` WRITE;
/*!40000 ALTER TABLE `evidencia` DISABLE KEYS */;
INSERT INTO `evidencia` VALUES (1,'Contenido bucal',2),(2,'Contenido vaginal',2),(3,'Contenido rectal',2),(4,'Lecho ungeal',2),(5,'Secreciones',2),(6,'Sangre',2),(7,'Orina',2),(8,'Otros',2),(9,'Vestido',4),(10,'Blusa',4),(11,'Camisa',4),(12,'Pantalón',4),(13,'Polera',4),(14,'Chaqueta',4),(15,'Chaleco',4),(16,'Calzado',4),(17,'Otros',4),(18,'Protector',6),(19,'Toalla higiénica',6),(20,'Otros',6),(21,'Contenido bucal',1),(22,'Contenido vaginal',1),(23,'Contenido rectal',1),(24,'Lecho ungeal',1),(25,'Secreciones',1),(26,'Sangre',1),(27,'Orina',1),(28,'Tejido cerebro',1),(29,'Tejido pulmón',1),(30,'Tejido corazón',1),(31,'Tejido hígado',1),(32,'Tejido baso',1),(33,'Tejido diafragma',1),(34,'Tejido intestino',1),(35,'Tejido piel ',1),(36,'Tejido otros',1),(37,'Vestido',3),(38,'Blusa',3),(39,'Camisa',3),(40,'Patalón',3),(41,'Polera',3),(42,'Chaqueta',3),(43,'Chaleco',3),(44,'Calzado',3),(45,'Otros',3),(46,'Protector',5),(47,'Toalla higiénica',5),(48,'Arma blanca',5),(49,'Cuchillo',5),(50,'Sable',5),(51,'Otros',5),(52,'Bala',8),(53,'Otros',8),(54,'Otros',7),(55,'Otros',9);
/*!40000 ALTER TABLE `evidencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formulario`
--

DROP TABLE IF EXISTS `formulario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formulario` (
  `NUE` int(11) NOT NULL,
  `fechaIngreso` datetime DEFAULT NULL,
  `RUC` varchar(45) DEFAULT NULL,
  `RIT` varchar(45) DEFAULT NULL,
  `direccionFotografia` varchar(60) DEFAULT NULL,
  `fechaOcurrido` datetime DEFAULT NULL,
  `lugarLevantamiento` varchar(50) DEFAULT NULL,
  `numeroParte` int(11) DEFAULT NULL,
  `observaciones` varchar(300) DEFAULT NULL,
  `direccionSS` varchar(50) DEFAULT NULL,
  `delitoRef` varchar(45) DEFAULT NULL,
  `descripcionEspecieFormulario` varchar(300) DEFAULT NULL,
  `ultimaEdicion` datetime DEFAULT NULL,
  `bloqueado` tinyint(1) DEFAULT NULL,
  `unidadPolicial` varchar(45) DEFAULT NULL,
  `Usuario_idUsuario` int(11) NOT NULL,
  `Usuario_idUsuarioInicia` int(11) NOT NULL,
  `Semaforo_idSemaforo` int(11) NOT NULL,
  PRIMARY KEY (`NUE`),
  KEY `fk_Formulario_Usuario1_idx` (`Usuario_idUsuario`),
  KEY `fk_Formulario_Usuario2_idx` (`Usuario_idUsuarioInicia`),
  KEY `fk_Formulario_Semaforo1_idx` (`Semaforo_idSemaforo`),
  CONSTRAINT `fk_Formulario_Semaforo1` FOREIGN KEY (`Semaforo_idSemaforo`) REFERENCES `semaforo` (`idSemaforo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Formulario_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Formulario_Usuario2` FOREIGN KEY (`Usuario_idUsuarioInicia`) REFERENCES `usuario` (`idUsuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formulario`
--

LOCK TABLES `formulario` WRITE;
/*!40000 ALTER TABLE `formulario` DISABLE KEYS */;
INSERT INTO `formulario` VALUES (11,'2016-03-05 18:57:39','','','C:','2016-03-05 18:57:39','sdfasd',0,'','sdfa','sdfas','','2016-03-05 18:57:39',1,'11',2,2,2),(12,'2016-03-05 18:59:16','','','C:','2016-03-05 18:59:16','asd',0,'dasf','asfd','adsfa','sdfa','2016-03-05 18:59:16',0,'asdf',3,3,3),(1313,'2016-03-05 02:51:43','','13-13','C:','2016-03-05 02:51:43','1313',12341234,'313','1313','trece trece','1313','2016-03-05 03:03:28',0,'1313',7,7,1),(1717,'2016-03-05 03:17:37','1313-13','1313-13','C:','2016-03-05 03:17:37','adsf',5465446,'asdfa','asdf','asdfa','sd','2016-03-05 03:19:04',0,'asdf',3,3,1),(123123,'2016-03-05 19:09:44','','','C:','2016-03-05 19:09:44','kjh',0,'kjh','kjh','kjh','','2016-03-05 19:09:44',0,'kjh',2,2,3);
/*!40000 ALTER TABLE `formulario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formulario_evidencia`
--

DROP TABLE IF EXISTS `formulario_evidencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formulario_evidencia` (
  `idFormularioEvidencia` int(11) NOT NULL AUTO_INCREMENT,
  `Formulario_NUE` int(11) NOT NULL,
  `Evidencia_idEvidencia` int(11) NOT NULL,
  PRIMARY KEY (`idFormularioEvidencia`),
  KEY `fk_Formulario_has_Evidencia_Evidencia1_idx` (`Evidencia_idEvidencia`),
  KEY `fk_Formulario_has_Evidencia_Formulario1_idx` (`Formulario_NUE`),
  CONSTRAINT `fk_Formulario_has_Evidencia_Evidencia1` FOREIGN KEY (`Evidencia_idEvidencia`) REFERENCES `evidencia` (`idEvidencia`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Formulario_has_Evidencia_Formulario1` FOREIGN KEY (`Formulario_NUE`) REFERENCES `formulario` (`NUE`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formulario_evidencia`
--

LOCK TABLES `formulario_evidencia` WRITE;
/*!40000 ALTER TABLE `formulario_evidencia` DISABLE KEYS */;
/*!40000 ALTER TABLE `formulario_evidencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `semaforo`
--

DROP TABLE IF EXISTS `semaforo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `semaforo` (
  `idSemaforo` int(11) NOT NULL,
  `semaforo` varchar(45) DEFAULT NULL,
  `descripcionSemaforo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idSemaforo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `semaforo`
--

LOCK TABLES `semaforo` WRITE;
/*!40000 ALTER TABLE `semaforo` DISABLE KEYS */;
INSERT INTO `semaforo` VALUES (1,'Rojo','.../imagenes/rojo.jpg'),(2,'Verde','.../imagenes/verde.jpg'),(3,'Amarillo','.../imagenes/amarillo.jpg');
/*!40000 ALTER TABLE `semaforo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_evidencia`
--

DROP TABLE IF EXISTS `tipo_evidencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_evidencia` (
  `idTipoEvidencia` int(11) NOT NULL,
  `nombreTipoEvidencia` varchar(45) DEFAULT NULL,
  `Area_idArea` int(11) NOT NULL,
  PRIMARY KEY (`idTipoEvidencia`),
  KEY `fk_Tipo_Evidencia_Area1_idx` (`Area_idArea`),
  CONSTRAINT `fk_Tipo_Evidencia_Area1` FOREIGN KEY (`Area_idArea`) REFERENCES `area` (`idArea`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_evidencia`
--

LOCK TABLES `tipo_evidencia` WRITE;
/*!40000 ALTER TABLE `tipo_evidencia` DISABLE KEYS */;
INSERT INTO `tipo_evidencia` VALUES (1,'Biológica',4),(2,'Biológica',6),(3,'Vestuario',4),(4,'Vestuario',6),(5,'Artefacto',4),(6,'Artefacto',6),(7,'Otros',6),(8,'Elemento balístico',4),(9,'Otros',4);
/*!40000 ALTER TABLE `tipo_evidencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_motivo`
--

DROP TABLE IF EXISTS `tipo_motivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_motivo` (
  `idMotivo` int(11) NOT NULL AUTO_INCREMENT,
  `tipoMotivo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idMotivo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_motivo`
--

LOCK TABLES `tipo_motivo` WRITE;
/*!40000 ALTER TABLE `tipo_motivo` DISABLE KEYS */;
INSERT INTO `tipo_motivo` VALUES (1,'Peritaje'),(2,'Custodia'),(3,'Traslado');
/*!40000 ALTER TABLE `tipo_motivo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_usuario`
--

DROP TABLE IF EXISTS `tipo_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_usuario` (
  `idTipoUsuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombreTipo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idTipoUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_usuario`
--

LOCK TABLES `tipo_usuario` WRITE;
/*!40000 ALTER TABLE `tipo_usuario` DISABLE KEYS */;
INSERT INTO `tipo_usuario` VALUES (1,'SML'),(2,'Externo');
/*!40000 ALTER TABLE `tipo_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `traslado`
--

DROP TABLE IF EXISTS `traslado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `traslado` (
  `idTraslado` int(11) NOT NULL AUTO_INCREMENT,
  `fechaEntrega` datetime DEFAULT NULL,
  `observaciones` varchar(300) DEFAULT NULL,
  `Tipo_Motivo_idMotivo` int(11) NOT NULL,
  `Usuario_idUsuarioEntrega` int(11) NOT NULL,
  `Usuario_idUsuarioRecibe` int(11) DEFAULT NULL,
  `Formulario_NUE` int(11) NOT NULL,
  PRIMARY KEY (`idTraslado`),
  KEY `fk_Involucrado_Tipo_Motivo1_idx` (`Tipo_Motivo_idMotivo`),
  KEY `fk_Involucrado_Usuario2_idx` (`Usuario_idUsuarioEntrega`),
  KEY `fk_Involucrado_Formulario1_idx` (`Formulario_NUE`),
  KEY `fk_Traslado_Usuario1_idx` (`Usuario_idUsuarioRecibe`),
  CONSTRAINT `fk_Involucrado_Formulario1` FOREIGN KEY (`Formulario_NUE`) REFERENCES `formulario` (`NUE`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Involucrado_Tipo_Motivo1` FOREIGN KEY (`Tipo_Motivo_idMotivo`) REFERENCES `tipo_motivo` (`idMotivo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Involucrado_Usuario2` FOREIGN KEY (`Usuario_idUsuarioEntrega`) REFERENCES `usuario` (`idUsuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Traslado_Usuario1` FOREIGN KEY (`Usuario_idUsuarioRecibe`) REFERENCES `usuario` (`idUsuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `traslado`
--

LOCK TABLES `traslado` WRITE;
/*!40000 ALTER TABLE `traslado` DISABLE KEYS */;
INSERT INTO `traslado` VALUES (1,NULL,NULL,3,7,NULL,1313),(2,NULL,NULL,3,3,NULL,1717),(3,'2016-03-05 19:00:10','',3,2,3,11),(4,NULL,NULL,1,3,NULL,12),(5,'2016-03-05 19:03:07','recibo para peritaje',1,3,7,11),(6,'2016-03-05 19:10:24','',3,2,3,123123),(7,'2016-03-05 19:11:27','',1,3,2,123123);
/*!40000 ALTER TABLE `traslado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `idUsuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombreUsuario` varchar(45) DEFAULT NULL,
  `apellidoUsuario` varchar(45) DEFAULT NULL,
  `rutUsuario` varchar(30) DEFAULT NULL,
  `passUsuario` varchar(45) DEFAULT NULL,
  `mailUsuario` varchar(45) DEFAULT NULL,
  `cuentaUsuario` varchar(30) DEFAULT NULL,
  `estadoUsuario` tinyint(1) DEFAULT NULL,
  `Cargo_idCargo` int(11) NOT NULL,
  `Area_idArea` int(11) NOT NULL,
  `Tipo_Usuario_idTipoUsuario` int(11) NOT NULL,
  PRIMARY KEY (`idUsuario`),
  KEY `fk_Usuario_Cargo1_idx` (`Cargo_idCargo`),
  KEY `fk_Usuario_Area1_idx` (`Area_idArea`),
  KEY `fk_Usuario_Tipo_Usuario1_idx` (`Tipo_Usuario_idTipoUsuario`),
  CONSTRAINT `fk_Usuario_Area1` FOREIGN KEY (`Area_idArea`) REFERENCES `area` (`idArea`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Usuario_Cargo1` FOREIGN KEY (`Cargo_idCargo`) REFERENCES `cargo` (`idCargo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Usuario_Tipo_Usuario1` FOREIGN KEY (`Tipo_Usuario_idTipoUsuario`) REFERENCES `tipo_usuario` (`idTipoUsuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Aracelly','Altamirano','174212074','conManjar','ara@correo.com','aaltamirano',1,1,1,1),(2,'Nicolas','Rozas','182953202','nikoazul1','nico@correo.com','nrozas',1,4,1,1),(3,'Sebastian','Acevedo','18486956k','sapito','zack@correo.com','sacevedo',1,3,1,1),(4,'Patricia','Riquelme','188444407','patita','paty@correo.com','priquelme',1,5,1,1),(5,'Armando','Casas','82843973','codo','armando@correo.com','acasas',1,6,1,1),(6,'Zulema','Sanchez','131457502','sol','zul@correo.com','zsanchez',1,2,1,1),(7,'Seba Alta','Altamirano','210145796','superman','seba@correo.com','saltamirano',1,3,1,1),(8,'Marthina','Cornejo','97880085','martillo','marthi@correo.com','mcornejo',1,4,1,1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-05 19:29:53
