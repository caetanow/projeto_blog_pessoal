package com.generation.blogpessoal.controller;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.*;

import javax.crypto.spec.PSource;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("/postagem")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

    @Autowired
    public PostagemRepository postagemRepository;

    @Autowired
    public TemaRepository temaRepository;

    @RequestMapping
    public ResponseEntity<List<Postagem>> getAll(){
        return ResponseEntity.ok(postagemRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Postagem> getById(@PathVariable Long id){
        return postagemRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
        return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
    }

    @PostMapping
    public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
        if (temaRepository.existsById(postagem.getTema().getId()))
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(postagemRepository.save(postagem));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @PutMapping
    public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
        if(postagemRepository.existsById(postagem.getId())){
            if(temaRepository.existsById(postagem.getTema().getId()))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(postagemRepository.save(postagem));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostagem(@PathVariable Long id){
        return postagemRepository.findById(id)
                .map(resposta -> {
                    postagemRepository.deleteById(id);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
