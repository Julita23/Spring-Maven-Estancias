package com.ejercicio2.estancias.controladores;

import com.ejercicio2.estancias.entidades.Familia;
import com.ejercicio2.estancias.servicios.FamiliaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/familia")
public class FamiliaController {

    @Autowired
    private FamiliaServicio fs;

    @GetMapping
    public String familia() {

        return "familia.html";
    }

    @GetMapping("/listarFamilias")
    public String listarFamilias(ModelMap modelo) {

        List<Familia> familiasLista = fs.listarFamilias();
        modelo.put("familias", familiasLista);

        return "listarFamilias.html";

    }

}
