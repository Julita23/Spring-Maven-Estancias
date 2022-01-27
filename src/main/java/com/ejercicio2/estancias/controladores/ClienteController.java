package com.ejercicio2.estancias.controladores;

import com.ejercicio2.estancias.entidades.Cliente;
import com.ejercicio2.estancias.servicios.ClienteServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/cliente")
public class ClienteController {
    
    @Autowired
    private ClienteServicio cs;
    
    @GetMapping
    public String cliente(){
        
        return "cliente.html";
    }
    
    @GetMapping("/listarClientes")
    public String listarClientes(ModelMap modelo){
        
        List<Cliente> clientesLista = cs.listarClientes();
        modelo.addAttribute("clientes", clientesLista);
        return "listarClientes";
    }

}
