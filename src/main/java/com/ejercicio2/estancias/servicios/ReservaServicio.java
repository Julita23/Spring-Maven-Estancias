package com.ejercicio2.estancias.servicios;

import com.ejercicio2.estancias.entidades.Casa;
import com.ejercicio2.estancias.entidades.Cliente;
import com.ejercicio2.estancias.entidades.Reserva;
import com.ejercicio2.estancias.errores.ErrorServicio;
import com.ejercicio2.estancias.repositorios.ReservaRepositorio;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaServicio {

    @Autowired
    private ReservaRepositorio rr;

    @Autowired
    private ClienteServicio cs;

    @Autowired
    private CasaServicio casaS;

    @Transactional
    public Reserva crearReserva(String idCliente, String idCasa, Date fechaDesdeReserva, Date fechaHastaReserva) throws ErrorServicio {

        validarReserva(idCliente, idCasa, fechaDesdeReserva, fechaHastaReserva);

        Cliente cliente = cs.buscarPorId(idCliente);

        if (cliente == null) {

            throw new ErrorServicio("El cliente es nulo");
        }

        Casa casa = casaS.buscarPorId(idCasa);
        if (casa == null) {
            throw new ErrorServicio("La casa no existe");
        }

        Date fechaDesdeCasa = casa.getFechaDesde();
        Date fechaHastaCasa = casa.getFechaHasta();

        if (fechaDesdeReserva.before(fechaDesdeCasa)) {

            throw new ErrorServicio("La fecha de ingreso debe ser igual o posterior a la fecha de disponibilidad de la casa" + fechaDesdeCasa.toString());
        }

        if (fechaHastaReserva.after(fechaHastaCasa)) {

            throw new ErrorServicio("La fecha de salida debe ser igual o anterior a la fecha de disponibilidad de la casa" + fechaHastaCasa.toString());
        }

        LocalDate fechaDReserva = fechaDesdeReserva.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaHReserva = fechaHastaReserva.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Period p = Period.between(fechaDReserva, fechaHReserva);

        int diasReserva = (int) ChronoUnit.DAYS.between(fechaDReserva, fechaHReserva);
        Integer minDias = casa.getMinDias();
        Integer maxDias = casa.getMaxDias();

        if (diasReserva < minDias) {

            throw new ErrorServicio("La cantidad de dias de la reserva no puede ser menor al minimo de dias disponible" + minDias.toString());
        }

        if (diasReserva > maxDias) {

            throw new ErrorServicio("La cantidad de dias de la reserva no puede ser mayor al maximo de dias disponible" + maxDias.toString());
        }

        List<Reserva> reservas = rr.listarReservasOcupadas(idCasa, fechaDesdeReserva, fechaHastaReserva);

        if (!reservas.isEmpty()) {

            throw new ErrorServicio("La casa se encuentra ocupada en las fechas indicadas");
        }

        Reserva reserva = new Reserva();

        reserva.setFechaDesde(fechaDesdeReserva);
        reserva.setFechaHasta(fechaHastaReserva);
        reserva.setCliente(cliente);
        reserva.setCasa(casa);
        reserva.setAlta(Boolean.TRUE);
        return rr.save(reserva);

    }

    @Transactional
    public void darBajaReserva(String id) throws ErrorServicio {

        Reserva reserva = rr.getById(id);

        if (reserva != null) {

                reserva.setAlta(Boolean.FALSE);
                rr.save(reserva);
 
        } else {

            throw new ErrorServicio("La reserva indicada no existe");
        }
    }

    public void validarReserva(String idCliente, String idCasa, Date fechaDesdeReserva, Date fechaHastaReserva) throws ErrorServicio {

        if (fechaDesdeReserva == null) {

            throw new ErrorServicio("La fecha de ingreso no puede ser nula");
        }

        if (fechaHastaReserva == null) {

            throw new ErrorServicio("La fecha de salida no puede ser nula");
        }

        if (fechaHastaReserva.before(fechaDesdeReserva)) {

            throw new ErrorServicio("La fecha de salida debe estar despues de la fecha de ingreso");
        }

        if (idCliente == null || idCliente.trim().isEmpty()) {

            throw new ErrorServicio("El id del cliente es nulo o se encuentra vacio");
        }

        if (idCasa == null || idCasa.trim().isEmpty()) {

            throw new ErrorServicio("El id de la casa es nulo o se encuentra vacio");
        }

    }

}
