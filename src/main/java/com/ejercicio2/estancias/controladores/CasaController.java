package com.ejercicio2.estancias.controladores;

import com.ejercicio2.estancias.entidades.Casa;
import com.ejercicio2.estancias.servicios.CasaServicio;
import com.ejercicio2.estancias.servicios.FamiliaServicio;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/casa")
public class CasaController {

    @Autowired
    private CasaServicio cs;

    @Autowired
    private FamiliaServicio fs;

    @GetMapping("/guardarCasa/{id}")
    public String guardarCasa(@PathVariable String id, ModelMap modelo) {

        modelo.put("id", id);
        return "guardarCasa.html";
    }

    @GetMapping("/mostrarCasa/{id}")
    public String listarCasa(@PathVariable String id, ModelMap modelo) {

        Casa casa = cs.buscarPorId(id);
        modelo.put("casa", casa);
        return "mostrarCasa.html";

    }

    @PostMapping("/guardarCasa/{id}")
    public String guardarCasa(@PathVariable String id, @RequestParam String calle,
            @RequestParam Integer numero, @RequestParam String codPostal,
            @RequestParam String ciudad, @RequestParam String pais,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta,
            @RequestParam Integer minDias, @RequestParam Integer maxDias,
            @RequestParam Double precio, @RequestParam String tipoVivienda,
            @RequestParam String descripcion, @RequestParam MultipartFile archivo,
            RedirectAttributes r) {

        try {

            Casa casa = cs.crearCasa(id, calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda, descripcion, archivo);
            r.addFlashAttribute("exito", "Registro Exitoso");
            id = casa.getId();
            return "redirect:/casa/mostrarCasa/" + id;

        } catch (Exception e) {

            r.addFlashAttribute("error", e.getMessage());
            return "redirect:/casa/guardarCasa/{id}";

        }
    }

    @GetMapping("/modificarCasa/{id}")
    public String modificarCasa(@PathVariable String id, ModelMap modelo) {

        Casa casa = cs.buscarPorId(id);
        modelo.put("casa", casa);
        return "modificarCasa.html";
    }

    @PostMapping("/modificarCasa/{id}")
    public String modificarCasa(@PathVariable String id, @RequestParam String calle,
            @RequestParam Integer numero, @RequestParam String codPostal,
            @RequestParam String ciudad, @RequestParam String pais,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta,
            @RequestParam Integer minDias, @RequestParam Integer maxDias,
            @RequestParam Double precio, @RequestParam String tipoVivienda,
            @RequestParam String descripcion, @RequestParam MultipartFile archivo,
            RedirectAttributes r) {

        try {

            cs.modificarCasa(id, calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda, descripcion, archivo);
            r.addFlashAttribute("exito", "Modificacion Exitosa");
            return "redirect:/casa/mostrarCasa/{id}";

        } catch (Exception e) {

            r.addFlashAttribute("error", e.getMessage());
            return "redirect:/casa/modificarCasa/{id}";

        }
    }

    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id, RedirectAttributes r) {

        try {

            cs.bajaCasa(id);

        } catch (Exception e) {

            r.addFlashAttribute("error", e.getMessage());

        }
        return "redirect:/casa/mostrarCasa/{id}";
    }

    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id, RedirectAttributes r) {

        try {

            cs.altaCasa(id);

        } catch (Exception e) {

            r.addFlashAttribute("error", e.getMessage());

        }
        return "redirect:/casa/mostrarCasa/{id}";
    }

    @GetMapping("/filtrarCasas/{idCliente}")
    public String filtrarCasas(@PathVariable String idCliente, ModelMap modelo) {

        modelo.put("id", idCliente);
        return "filtrarCasas.html";

    }

    @PostMapping("/filtrarCasas/{idCliente}")
    public String filtrarCasas(@PathVariable String idCliente, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta, RedirectAttributes r) {
        
        List<Casa> casas = cs.buscarCasasPorFechaDisponible(fechaDesde, fechaHasta);
        
        r.addFlashAttribute("casas", casas);
        r.addFlashAttribute("fechaDesde", fechaDesde);
        r.addFlashAttribute("fechaHasta", fechaHasta);
        return "redirect:/casa/filtrarCasas/{idCliente}";
    }
    
   

}
