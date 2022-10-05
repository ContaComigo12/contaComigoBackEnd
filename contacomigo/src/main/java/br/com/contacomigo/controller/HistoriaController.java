package br.com.contacomigo.controller;

import br.com.contacomigo.model.HistoriaModel;
import br.com.contacomigo.repository.HistoriaRepository;
import br.com.contacomigo.service.HistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping({"/historia"})
@CrossOrigin({"*"})
public class HistoriaController {

    @Autowired
    private HistoriaService service;

    @GetMapping
    public ResponseEntity<List<HistoriaModel>> getAllHistorias(){
        return ResponseEntity.ok().body(service.getAllHistorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriaModel> getHistoriaById(@PathVariable long id){
        return ResponseEntity.ok().body(service.getHistoriaById(id));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<HistoriaModel>> getHistoriaByCategoria(@PathVariable String categoria){
        return ResponseEntity.ok().body(service.getHistoriasByCategoria(categoria));
    }

    @GetMapping("/subcategoria/{subCategoria}")
    public ResponseEntity<HistoriaModel> getHistoriaBySubCategoriaRamdom(@PathVariable String subCategoria){
        return ResponseEntity.ok().body(service.getHistoriaBySubCategoriaRandom(subCategoria));
    }

    @PostMapping
    public ResponseEntity<HistoriaModel> salvarHistoria(@RequestParam String titulo, @RequestParam String categoria, @RequestParam String subCategoria, @RequestParam MultipartFile[] imagens){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarHistoria(titulo, categoria, subCategoria, imagens));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<HistoriaModel> deletarHistoria(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.deletarHistoria(id));
    }
}
