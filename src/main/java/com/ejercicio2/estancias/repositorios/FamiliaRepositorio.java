
package com.ejercicio2.estancias.repositorios;

import com.ejercicio2.estancias.entidades.Familia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamiliaRepositorio extends JpaRepository<Familia, String>{
    
    
    
}
