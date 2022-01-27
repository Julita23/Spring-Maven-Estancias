package com.ejercicio2.estancias.servicios;

import com.ejercicio2.estancias.entidades.Familia;
import com.ejercicio2.estancias.entidades.Usuario;
import com.ejercicio2.estancias.errores.ErrorServicio;
import com.ejercicio2.estancias.repositorios.FamiliaRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FamiliaServicio extends Usuario {

    @Autowired
    private FamiliaRepositorio fr;

    public Familia crearFamilia(String nombre, Integer edadMin, Integer edadMax, Integer numHijos) throws ErrorServicio {

        validarFamilia(nombre, edadMin, edadMax, numHijos);
        Familia familia = new Familia();
        familia.setNombre(nombre);
        familia.setEdadMin(edadMin);
        familia.setEdadMax(edadMax);
        familia.setNumHijos(numHijos);

        return familia;
    }

    @Transactional
    public Familia modificarFamilia(String id, String nombre, Integer edadMin, Integer edadMax, Integer numHijos) throws ErrorServicio {

        validarFamilia(nombre, edadMin, edadMax, numHijos);

        if (id == null || id.trim().isEmpty()) {

            throw new ErrorServicio("El id no puede ser nulo");
        }

        Familia familia = fr.getById(id);

        if (familia != null) {

            familia.setNombre(nombre);
            familia.setEdadMin(edadMin);
            familia.setEdadMax(edadMax);
            familia.setNumHijos(numHijos);
            return fr.save(familia);

        } else {

            throw new ErrorServicio("La familia no se encuentra setteada");
        }
    }

    @Transactional(readOnly = true)
    public Familia buscarPorId(String id) {
        
        return fr.getById(id);
    }

    @Transactional(readOnly = true)
    public List<Familia> listarFamilias() {

        return fr.findAll();

    }

    public void validarFamilia(String nombre, Integer edadMin, Integer edadMax, Integer numHijos) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {

            throw new ErrorServicio("El nombre no puede ser nulo");

        }

        if (edadMin == null || edadMin < 0) {

            throw new ErrorServicio("la edad no puede ser nula o menor que 0");

        }

        if (edadMax == null || edadMax < 0 || edadMax < edadMin) {

            throw new ErrorServicio("la edad no puede ser nula o menor que 0, ni menor que la minima");

        }

        if (numHijos == null || numHijos < 0) {

            throw new ErrorServicio("El numero de hijos no puede ser nulo");

        }
    }

}
