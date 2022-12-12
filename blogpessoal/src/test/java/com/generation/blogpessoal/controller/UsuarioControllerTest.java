package com.generation.blogpessoal.controller;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void start(){
        usuarioRepository.deleteAll();

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Root", "root@root.com.br", "rootroot", " "));
    }

    @Test
    @DisplayName("Cadastrar um Usuário")
    public void deveCriarUmUsuario(){

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L,
                "measure", "measure@measure.com.br", "measuremeasure", "www.measure.com.br/measure.jpeg"));

        ResponseEntity<Usuario> corpoResposta = testRestTemplate
                .exchange("/usuario/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
        assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());


    }



    @Test
    @DisplayName("Não deve permitir duplicidade de usuário")
    public void naoDeveDuplicarUsuario(){
        usuarioService.cadastrarUsuario(new Usuario(0L,
                "course", "course@course.com.br", "coursecourse", "www.course.com.br/course.jpg"));

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L,
                "course", "course@course.com.br", "coursecourse", "www.course.com.br/course.jpg"));

        ResponseEntity<Usuario> corpoResposta = testRestTemplate
                .exchange("/usuario/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Deve atualizar um Usuário")
    public void deveAtualizarUsuario(){
        Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L,
                "weapon", "weapon@weapon.com.br", "weaponweapon", "www.weapon.com.br/weapon.jpg"));

        Usuario usuaroUpdate = new Usuario(usuarioCadastrado.get().getId(),
                "camera", "camera@camera.com.br", "cameracamera", "www.camera.com.br/camera.jpg");

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuaroUpdate);

        ResponseEntity<Usuario> corpoResposta = testRestTemplate
                .withBasicAuth("root@root.com.br", "rootroot")
                .exchange("/usuario/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
        assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
    }

    @Test
    @DisplayName("Deve listar todos usuários")
    public void deveListarTodosUsuarios(){
        usuarioService.cadastrarUsuario(new Usuario(0L,
                "enough", "enough@enough.com.br", "enoughenough",
                "www.enough.com.br/enough.jpg"));

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "journey", "journey@journey.com.br", "journeyjourney",
                "www.journey.com.br/journey.jpg"));

        ResponseEntity<String> resposta = testRestTemplate
                .withBasicAuth("root@root.com.br", "rootroot")
                .exchange("/usuario/all", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
}
