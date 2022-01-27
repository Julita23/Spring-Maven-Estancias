package com.ejercicio2.estancias.servicios;

import com.ejercicio2.estancias.entidades.Casa;
import com.ejercicio2.estancias.entidades.Familia;
import com.ejercicio2.estancias.entidades.Foto;
import com.ejercicio2.estancias.errores.ErrorServicio;
import com.ejercicio2.estancias.repositorios.CasaRepositorio;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CasaServicio {

    @Autowired
    private CasaRepositorio cr;

    @Autowired
    private FamiliaServicio fs;

    @Autowired
    private FotoServicio fotoS;

    @Transactional
    public Casa crearCasa(String idFamilia, String calle, Integer numero, String codPostal, String ciudad, String pais, Date fechaDesde, Date fechaHasta, Integer minDias, Integer maxDias, Double precio, String tipoVivienda, String descripcion, MultipartFile archivo) throws ErrorServicio {

        validarCasa(calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);
        validarDescripcion(descripcion);
        
        Familia familia = fs.buscarPorId(idFamilia);

        if (familia != null) {

            Casa casa = new Casa();

            casa.setCalle(calle);
            casa.setNumero(numero);
            casa.setCodPostal(codPostal);
            casa.setCiudad(ciudad);
            casa.setPais(pais);
            casa.setFechaDesde(fechaDesde);
            casa.setFechaHasta(fechaHasta);
            casa.setMinDias(minDias);
            casa.setMaxDias(maxDias);
            casa.setPrecio(precio);
            casa.setTipoVivienda(tipoVivienda);
            casa.setDescripcion(descripcion);
            Foto foto = fotoS.guardar(archivo);
            casa.setFoto(foto);
            casa.setAlta(true);
            familia.setCasa(casa);

            return cr.save(casa);

        } else {

            throw new ErrorServicio("La familia solicitada no fue encontrada");

        }

    }

    @Transactional
    public Casa modificarCasa(String idCasa, String calle, Integer numero, String codPostal, String ciudad, String pais, Date fechaDesde, Date fechaHasta, Integer minDias, Integer maxDias, Double precio, String tipoVivienda, String descripcion, MultipartFile archivo) throws ErrorServicio {

        validarCasa(calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);
        validarDescripcion(descripcion);
        
        Casa casa = cr.getById(idCasa);

        if (casa != null) {

            casa.setCalle(calle);
            casa.setNumero(numero);
            casa.setCodPostal(codPostal);
            casa.setCiudad(ciudad);
            casa.setPais(pais);
            casa.setFechaDesde(fechaDesde);
            casa.setFechaHasta(fechaHasta);
            casa.setMinDias(minDias);
            casa.setMaxDias(maxDias);
            casa.setPrecio(precio);
            casa.setTipoVivienda(tipoVivienda);
            casa.setDescripcion(descripcion);

            String idFoto = null;

            if (casa.getFoto() != null) {
                idFoto = casa.getFoto().getId();
            }

            Foto foto = fotoS.actualizar(idFoto, archivo);
            casa.setFoto(foto);
            return cr.save(casa);

        } else {

            throw new ErrorServicio("No se encontro la casa solicitada");
        }

    }

    @Transactional(readOnly = true)
    public Casa buscarPorId(String id) {

        return cr.getById(id);

    }

    @Transactional
    public void altaCasa(String id) throws ErrorServicio {

        Casa casa = cr.getById(id);

        if (casa != null) {

            casa.setAlta(true);
            cr.save(casa);
        } else {

            throw new ErrorServicio("La casa solicitada no se encuentra disponible");
        }

    }

    @Transactional
    public void bajaCasa(String id) throws ErrorServicio {

        Casa casa = cr.getById(id);

        if (casa != null) {

            casa.setAlta(false);
            cr.save(casa);
        } else {

            throw new ErrorServicio("La casa solicitada no se encuentra disponible");
        }

    }
    
    @Transactional(readOnly = true)
    public List<Casa> buscarCasasPorFechaDisponible(Date fechaDesde, Date fechaHasta){
        
        return cr.buscarCasasPorFechaDisponible(fechaDesde, fechaHasta);
        
    }

    public void validarCasa(String calle, Integer numero, String codPostal, String ciudad, String pais, Date fechaDesde, Date fechaHasta, Integer minDias, Integer maxDias, Double precio, String tipoVivienda) throws ErrorServicio {

        if (calle == null || calle.trim().isEmpty()) {

            throw new ErrorServicio("La calle no puede ser nula");
        }

        if (numero == null || numero < 0) {

            throw new ErrorServicio("El numero no puede ser nulo o menor a 0");
        }

        if (codPostal == null || codPostal.trim().isEmpty()) {

            throw new ErrorServicio("El codigo postal no puede ser nulo");
        }

        if (ciudad == null || ciudad.trim().isEmpty()) {

            throw new ErrorServicio("La ciudad no puede ser nula");
        }

        if (pais == null || pais.trim().isEmpty()) {

            throw new ErrorServicio("El pais no puede ser nulo");
        }

        if (fechaDesde == null) {

            throw new ErrorServicio("La fecha no puede ser nula");
        }

        if (fechaHasta == null) {

            throw new ErrorServicio("La fecha no puede ser nula");
        }

        if (fechaHasta.before(fechaDesde)) {

            throw new ErrorServicio("La fecha hasta ingresada, no puede ser antes de la fecha desde");
        }

        if (fechaHasta.equals(fechaDesde)) {

            throw new ErrorServicio("El alquiler de la casa no puede ser menor a dos dias");
        }

        LocalDate fechaD = fechaDesde.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaH = fechaHasta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Period p = Period.between(fechaD, fechaH);

        int dias = (int) ChronoUnit.DAYS.between(fechaD, fechaH);
        System.out.println(dias);

        if (minDias == null) {

            throw new ErrorServicio("El minimo de dias no puede ser nulo");
        }

        if (dias < minDias) {

            throw new ErrorServicio("La cantidad de dias no puede ser menor al minimo de dias disponibles");
        }

        if (maxDias == null) {

            throw new ErrorServicio("El maximo de dias no puede ser nulo");
        }

        if (dias < maxDias) {

            throw new ErrorServicio("La cantidad de dias no puede superar al maximo de dias disponibles");
        }

        if (minDias > maxDias) {

            throw new ErrorServicio("El minimo de dias no puede ser mayor al maximo de dias");
        }

        if (precio == null || precio < 0) {

            throw new ErrorServicio("El precio no puede ser nulo o menor a cero");
        }

        if (tipoVivienda == null || tipoVivienda.trim().isEmpty()) {

            throw new ErrorServicio("El tipo de vivienda no puede ser nula");
        }
    }
    
    public void validarDescripcion(String descripcion) throws ErrorServicio{
        
        if(descripcion.length()>1499){
            
            throw new ErrorServicio("La cantidad de caracteres excede los 1500 caracteres");
        }
    }
}
