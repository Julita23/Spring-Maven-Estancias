package com.ejercicio2.estancias.repositorios;

import com.ejercicio2.estancias.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String> {
    
    
}
