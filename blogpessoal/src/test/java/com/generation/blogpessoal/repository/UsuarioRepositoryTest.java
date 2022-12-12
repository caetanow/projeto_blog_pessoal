package com.generation.blogpessoal.repository;

import com.generation.blogpessoal.model.Usuario;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS  )
public class UsuarioRepositoryTest {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void start(){
        usuarioRepository.deleteAll();
        usuarioRepository.save(new Usuario(0L,
                "director", "director@director.com.br", "directordirector",
                "www.director.com.br/director.jpg"));
        usuarioRepository.save(new Usuario(0L,
                    "confuse silva", "confuse@confuse.com.br", "confuseconfuse",
                "www.confuse.com.br/confuse.jpg"));
        usuarioRepository.save(new Usuario(0L,
                "uncle silva", "uncle@uncle.com.br", "uncleuncle",
                "www.uncle.com.br/uncle.jpg"));
        usuarioRepository.save(new Usuario(0L,
                "likely silva", "likely@likely.com.br", "likelylikely",
                "www.likely.com.br/likely.jpg"));
    }

    @Test
    @DisplayName("Retorna 1 usuario")
    public void deveRetornarUmUsuario(){
        Optional<Usuario> usuario = usuarioRepository.findByUsuario("confuse@confuse.com.br");
        assertTrue(usuario.get().getUsuario().equals("confuse@confuse.com.br"));
    }

    @Test
    @DisplayName("Deve retornar 3 usuários")
    public void deveREtornarTresUsuarios(){
        List<Usuario> listaUsuario = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");
        assertEquals(3, listaUsuario.size());
        assertTrue(listaUsuario.get(0).getNome().equals("confuse silva"));
        assertTrue(listaUsuario.get(1).getNome().equals("uncle silva"));
        assertTrue(listaUsuario.get(2).getNome().equals("likely silva"));
    }

    @AfterAll
    public void end(){
        usuarioRepository.deleteAll();
    }
}
