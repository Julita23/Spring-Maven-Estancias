package com.ejercicio2.estancias.repositorios;

import com.ejercicio2.estancias.entidades.Reserva;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, String> {

    @Query(value = "SELECT * FROM reserva r "
            + "WHERE r.casa_id=?1 "
            + "AND ((r.fecha_desde BETWEEN ?2 AND ?3) "
            + "OR (r.fecha_hasta BETWEEN ?2 AND ?3) "
            + "OR ((?2 BETWEEN r.fecha_desde AND r.fecha_hasta) AND (?3 BETWEEN r.fecha_desde AND r.fecha_hasta)));", nativeQuery = true)
    public List<Reserva> listarReservasOcupadas(String idCasa, Date fechaDesdeReserva, Date fechaHastaReserva);
    
    

}
