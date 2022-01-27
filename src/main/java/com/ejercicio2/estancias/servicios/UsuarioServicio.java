package com.ejercicio2.estancias.servicios;

import com.ejercicio2.estancias.entidades.Cliente;
import com.ejercicio2.estancias.entidades.Familia;
import com.ejercicio2.estancias.entidades.Usuario;
import com.ejercicio2.estancias.enumueraciones.Rol;
import com.ejercicio2.estancias.errores.ErrorServicio;
import com.ejercicio2.estancias.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio ur;

    @Autowired
    private FamiliaServicio fs;

    @Autowired
    private ClienteServicio cs;

    @Transactional
    public Usuario crearUsuario(String alias, String email, String clave, Rol rol, String nombre, Integer edadMin, Integer edadMax, Integer numHijos, String nombreC, String calle, Integer numero, String codPostal, String ciudad, String pais) throws ErrorServicio {

        validarUsuario(alias, email);
        validarClave(clave);

        Usuario usuario = ur.buscarUsuarioPorMail(email);

        if (usuario != null) {

            throw new ErrorServicio("El cliente ya existe");
        }

        if (rol == Rol.FAMILIA) {

            Familia familia = fs.crearFamilia(nombre, edadMin, edadMax, numHijos);
            familia.setAlias(alias);
            familia.setEmail(email);
            String encriptado = new BCryptPasswordEncoder().encode(clave);
            familia.setClave(encriptado);
            familia.setFechaAlta(new Date());
            familia.setRol(rol);
            familia.setAlta(Boolean.TRUE);

            return ur.save(familia);
        }

        if (rol == Rol.CLIENTE) {

            Cliente c = cs.crearCliente(nombreC, calle, numero, codPostal, ciudad, pais);

            c.setAlias(alias);
            c.setEmail(email);
            c.setAlta(Boolean.TRUE);
            String encriptado = new BCryptPasswordEncoder().encode(clave);
            c.setClave(encriptado);
            c.setRol(rol);
            c.setFechaAlta(new Date());

            return ur.save(c);

        } else {

            throw new ErrorServicio("Ha ocurrido un problema con los roles");
        }
    }

    @Transactional
    public Usuario modificarUsuario(String id, String alias, String email, Rol rol, String nombre, Integer edadMin, Integer edadMax, Integer numHijos, String nombreC, String calle, Integer numero, String codPostal, String ciudad, String pais) throws ErrorServicio {

        validarUsuario(alias, email);

        Usuario usuario = ur.getById(id);
        
        if (!usuario.getEmail().equals(email)) {
            
            Usuario usuario1 = ur.buscarUsuarioPorMail(email);
            
            if (usuario1 != null) {
                
                throw new ErrorServicio("El usuario ya existe");
            }
        }

        if (rol == Rol.FAMILIA) {

            Familia familia = fs.modificarFamilia(id, nombre, edadMin, edadMax, numHijos);

            familia.setAlias(alias);
            familia.setEmail(email);
   
            return ur.save(familia);

        }

        if (rol == Rol.CLIENTE) {

            Cliente cliente = cs.modificarCliente(id, nombreC, calle, numero, codPostal, ciudad, pais);

            cliente.setAlias(alias);
            cliente.setEmail(email);

            return ur.save(cliente);

        } else {

            throw new ErrorServicio("El rol no fue encontrado");
        }
    }
    
    @Transactional
    public Usuario cambiarClave(String id, String clave1, String clave2) throws ErrorServicio{
        
        validarClave(clave1);
        validarClave(clave2);

        Usuario usuario = ur.getById(id);

        boolean matches = new BCryptPasswordEncoder().matches(clave1, usuario.getClave());
        
        if(matches == true){
            
            String encriptado = new BCryptPasswordEncoder().encode(clave2);
            usuario.setClave(encriptado);
            return ur.save(usuario);
            
        }else{
            
            throw new ErrorServicio("La contrase;a anterior no coincide con la original");
        }
    }

    @Transactional
    public void darBajaUsuario(String id) throws ErrorServicio {

        Usuario usuario = ur.getById(id);

        if (usuario != null) {

            usuario.setFechaBaja(new Date());
            usuario.setAlta(Boolean.FALSE);
        } else {
            throw new ErrorServicio("El usuario no fue encontrado");
        }
    }

    @Transactional
    public void darAltaUsuario(String id) throws ErrorServicio {

        Usuario usuario = ur.getById(id);

        if (usuario != null) {

            usuario.setFechaBaja(null);
            usuario.setAlta(Boolean.TRUE);
            ur.save(usuario);

        } else {
            throw new ErrorServicio("El usuario no fue encontrado");
        }
    }

    @Transactional(readOnly = true)
    public List listarUsuarios() {
        return ur.findAll();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosActivos() {

        return ur.listarUsuariosActivos();

    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosInactivos() {

        return ur.listarUsuariosInactivos();

    }
    
    @Transactional(readOnly = true)
    public Usuario buscarPorId(String id){
        
        return ur.getById(id);
    }

    public void validarUsuario(String alias, String email) throws ErrorServicio {

        if (alias == null || alias.trim().isEmpty()) {

            throw new ErrorServicio("El alias no puede ser nulo");
        }

        if (email == null || email.trim().isEmpty()) {

            throw new ErrorServicio("El email no puede ser nulo");
        }

    }
    
    public void validarClave(String clave) throws ErrorServicio{
        
        if (clave == null || clave.trim().isEmpty()) {

            throw new ErrorServicio("El clave no puede ser nulo");
        }

        if (clave.length() > 6 || clave.length() < 4) {

            throw new ErrorServicio("La clave no puede tener menos de 4 digitos ni mas de 6 digitos");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = ur.buscarUsuarioPorMail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);

            return user;

        } else {
            return null;
        }
    }

}
