package com.ejercicio2.estancias.servicios;

import com.ejercicio2.estancias.entidades.Cliente;
import com.ejercicio2.estancias.errores.ErrorServicio;
import com.ejercicio2.estancias.repositorios.ClienteRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio cr;
    

    @Transactional
    public Cliente crearCliente(String nombre, String calle, Integer numero, String codPostal, String ciudad, String pais) throws ErrorServicio {

        validarCliente(nombre, calle, numero, codPostal, ciudad, pais);
        
        Cliente cliente = new Cliente();
        
        cliente.setNombre(nombre);
        cliente.setCalle(calle);
        cliente.setNumero(numero);
        cliente.setCodPostal(codPostal);
        cliente.setCiudad(ciudad);
        cliente.setPais(pais);
        
        return cliente;
    }
    
    @Transactional
    public Cliente modificarCliente(String id, String nombre, String calle, Integer numero, String codPostal, String ciudad, String pais) throws ErrorServicio{
        
        validarCliente(nombre, calle, numero, codPostal, ciudad, pais);
        
         if(id == null || id.trim().isEmpty()){
            
            throw new ErrorServicio("El id no puede ser nulo");
        }
        
         Cliente cliente = cr.getById(id);
         
         if(cliente != null){
             
             cliente.setNombre(nombre);
             cliente.setCalle(calle);
             cliente.setNumero(numero);
             cliente.setCodPostal(codPostal);
             cliente.setCiudad(ciudad);
             cliente.setPais(pais);
             return cr.save(cliente);
             
         }else{
             
             throw new ErrorServicio("El cliente buscado no existe");
         }
    }
    
    @Transactional(readOnly = true)
    public Cliente buscarPorId(String id){
        
        return cr.getById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> listarClientes(){
        
        return cr.findAll();
    }

    public void validarCliente(String nombre, String calle, Integer numero, String codPostal, String ciudad, String pais) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {

            throw new ErrorServicio("El nombre no puede ser nulo");

        }

        if (calle == null || calle.trim().isEmpty()) {

            throw new ErrorServicio("La calle no puede ser nula");

        }

        if (numero == null || numero < 0) {

            throw new ErrorServicio("El numero no puede ser nulo ni menor a 0");

        }

        if (codPostal == null || codPostal.trim().isEmpty()) {

            throw new ErrorServicio("La calle no puede ser nula");

        }
        
        if (ciudad == null || ciudad.trim().isEmpty()) {

            throw new ErrorServicio("La ciudad no puede ser nula");

        }
        
        if (pais == null || pais.trim().isEmpty()) {

            throw new ErrorServicio("El pais no puede ser nulo");

        }

    }

}
