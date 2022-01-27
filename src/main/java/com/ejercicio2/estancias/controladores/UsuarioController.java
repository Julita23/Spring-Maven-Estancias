package com.ejercicio2.estancias.controladores;

import com.ejercicio2.estancias.enumueraciones.Rol;
import com.ejercicio2.estancias.errores.ErrorServicio;
import com.ejercicio2.estancias.servicios.ClienteServicio;
import com.ejercicio2.estancias.servicios.FamiliaServicio;
import com.ejercicio2.estancias.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioServicio us;

    @Autowired
    private ClienteServicio cs;

    @Autowired
    private FamiliaServicio fs;

    @GetMapping("/guardarCliente")
    public String guardarCliente(ModelMap modelo) {

        modelo.put("rol", Rol.CLIENTE);
        return "guardarCliente.html";
    }

    @PostMapping("/guardarUsuario")
    public String guardarUsuario(ModelMap modelo, @RequestParam(required = false) String alias,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String clave, @RequestParam Rol rol,
            @RequestParam(required = false) String nombre, @RequestParam(required = false) Integer edadMin,
            @RequestParam(required = false) Integer edadMax, @RequestParam(required = false) Integer numHijos,
            @RequestParam(required = false) String nombreC, @RequestParam(required = false) String calle,
            @RequestParam(required = false) Integer numero, @RequestParam(required = false) String codPostal,
            @RequestParam(required = false) String ciudad, @RequestParam(required = false) String pais,
            RedirectAttributes r) {

        try {

            us.crearUsuario(alias, email, clave, rol, nombre, edadMin, edadMax, numHijos, nombreC, calle, numero, codPostal, ciudad, pais);
            r.addFlashAttribute("exito", "Registro Exitoso");

        } catch (ErrorServicio e) {

            r.addFlashAttribute("error", e.getMessage());
        }

        if (rol.equals(Rol.CLIENTE)) {

            return "redirect:/usuario/guardarCliente";
        }

        if (rol.equals(Rol.FAMILIA)) {

            return "redirect:/usuario/guardarFamilia";

        } else {
            return "index.html";
        }
    }

    @GetMapping("/guardarFamilia")
    public String guardarFamilia(ModelMap modelo) {

        modelo.put("rol", Rol.FAMILIA);
        return "guardarFamilia.html";
    }

    @GetMapping("/alta/{id}/{rol}")
    public String alta(@PathVariable String id, @PathVariable Rol rol, RedirectAttributes r) {

        try {
            
            if (rol.equals(rol.CLIENTE)) {
                
                us.darAltaUsuario(id);
                return "redirect:/cliente/listarClientes";
            }

            if (rol.equals(rol.FAMILIA)) {
                
                us.darAltaUsuario(id);
                return "redirect:/familia/listarFamilias";
                
            } else {
                throw new Exception("Rol invalido");
            }

        } catch (Exception e) {

            r.addFlashAttribute("error", e.getMessage());
            if (rol.equals(rol.CLIENTE)) {

                return "redirect:/cliente/listarClientes";
            }

            if (rol.equals(rol.FAMILIA)) {

                return "redirect:/familia/listarFamilias";
            }

            return "index.html";
        }
    }

    @GetMapping("/baja/{id}/{rol}")
    public String baja(@PathVariable String id, @PathVariable Rol rol, RedirectAttributes r) {

        try {
           if (rol.equals(rol.CLIENTE)) {
                
                us.darBajaUsuario(id);
                return "redirect:/cliente/listarClientes";
            }

            if (rol.equals(rol.FAMILIA)) {
                
                us.darBajaUsuario(id);
                return "redirect:/familia/listarFamilias";
                
            } else {
                throw new Exception("Rol invalido");
            }

        } catch (Exception e) {

            r.addFlashAttribute("error", e.getMessage());
            if (rol.equals(rol.CLIENTE)) {

                return "redirect:/cliente/listarClientes";
            }

            if (rol.equals(rol.FAMILIA)) {

                return "redirect:/familia/listarFamilias";
            }

            return "index.html";
        }
    }

    @GetMapping("/modificarCliente/{id}")
    public String modificarCliente(@PathVariable String id, ModelMap modelo) {

        modelo.put("cliente", cs.buscarPorId(id));
        return "modificarCliente.html";

    }
    
    @GetMapping("/modificarFamilia/{id}")
    public String modificarFamilia(@PathVariable String id, ModelMap modelo) {

        modelo.put("familia", fs.buscarPorId(id));
        return "modificarFamilia.html";

    }

    @PostMapping("/modificarUsuario/{id}")
    public String modificarCliente(@PathVariable String id,
            ModelMap modelo, @RequestParam(required = false) String alias,
            @RequestParam(required = false) String email,
            //            @RequestParam(required = false) String clave, 
            @RequestParam Rol rol,
            @RequestParam(required = false) String nombre, @RequestParam(required = false) Integer edadMin,
            @RequestParam(required = false) Integer edadMax, @RequestParam(required = false) Integer numHijos,
            @RequestParam(required = false) String nombreC, @RequestParam(required = false) String calle,
            @RequestParam(required = false) Integer numero, @RequestParam(required = false) String codPostal,
            @RequestParam(required = false) String ciudad, @RequestParam(required = false) String pais,
            RedirectAttributes r) {

        try {

            if (rol.equals(rol.CLIENTE)) {

                us.modificarUsuario(id, alias, email, rol, nombre, edadMin, edadMax, numHijos, nombreC, calle, numero, codPostal, ciudad, pais);
                r.addFlashAttribute("exito", "Modificacion Exitosa");

                return "redirect:/usuario/modificarCliente/{id}";

            }
            if (rol.equals(rol.FAMILIA)) {
                us.modificarUsuario(id, alias, email, rol, nombre, edadMin, edadMax, numHijos, nombreC, calle, numero, codPostal, ciudad, pais);
                r.addFlashAttribute("exito", "Modificacion Exitosa");

                return "redirect:/usuario/modificarFamilia/{id}";

            } else {

                throw new Exception("El rol es invalido");

            }

        } catch (Exception e) {

            r.addFlashAttribute("error", e.getMessage());

            if (rol.equals(rol.CLIENTE)) {

                return "redirect:/usuario/modificarCliente/{id}";
            }

            if (rol.equals(rol.FAMILIA)) {

                return "redirect:/usuario/modificarFamilia/{id}";

            } else {

                return "index.html";
            }

        }

    }

    @GetMapping("/claveUsuario/{id}")
    public String cambiarClave(@PathVariable String id, ModelMap modelo) {

        modelo.put("usuario", us.buscarPorId(id));
        return "claveUsuario.html";
    }

    @PostMapping("/claveUsuario/{id}")
    public String cambiarClave(@PathVariable String id, ModelMap modelo, @RequestParam Rol rol, @RequestParam String clave1, @RequestParam String clave2, RedirectAttributes r) {

        try {

            if (rol.equals(Rol.CLIENTE)) {

                us.cambiarClave(id, clave1, clave2);
                r.addFlashAttribute("exito", "Modificacion Exitosa");

                return "redirect:/cliente/listarClientes";
            }

            if (rol.equals(Rol.FAMILIA)) {

                us.cambiarClave(id, clave1, clave2);
                r.addFlashAttribute("exito", "Modificacion Exitosa");

                return "redirect:/familia/listarFamilias";

            } else {

                throw new Exception("Rol invalido");
            }

        } catch (Exception e) {

            r.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuario/claveUsuario/{id}";
        }

    }

}
