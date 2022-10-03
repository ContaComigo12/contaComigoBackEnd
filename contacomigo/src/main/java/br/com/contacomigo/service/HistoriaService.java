package br.com.contacomigo.service;

import br.com.contacomigo.model.HistoriaModel;
import br.com.contacomigo.repository.HistoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class HistoriaService {

    @Autowired private HistoriaRepository repository;

    private static String caminhoImagens = "C:/Users/Davi/Desktop/contacomigo-backend/imagens/";

    private String extrairExtensao(String nomeImagem){
        int i = nomeImagem.lastIndexOf(".");
        return nomeImagem.substring(i+1);
    }

    public List<HistoriaModel> getAllHistorias(){
        return repository.findAll();
    }

    public HistoriaModel getHistoriaById(long id){
        HistoriaModel historiaModel = null;
        Optional<HistoriaModel> historiaModelOptional = repository.findById(id);
        if(historiaModelOptional.isPresent()){
            historiaModel = historiaModelOptional.get();
        }
        return historiaModel;
    }

    public List<HistoriaModel> getHistoriasByCategoria(String categoria){
        return this.repository.findAllByCategoria(categoria);
    }

    public HistoriaModel getHistoriaBySubCategoriaRandom(String subCategoria){
        Random random = new Random();
        List<HistoriaModel> historiaModelList = this.repository.findAllBySubCategoria(subCategoria);
        HistoriaModel historiaModel = historiaModelList.get(random.nextInt(historiaModelList.size()));
        return historiaModel;
    }

    public HistoriaModel salvarHistoria(String titulo, String categoria, String subCategoria, MultipartFile[] imagens){
        HistoriaModel historiaModel = new HistoriaModel();
        historiaModel.setTitulo(titulo);
        historiaModel.setCategoria(categoria);
        historiaModel.setSubCategoria(subCategoria);
        try{
            for (int i = 0; i < imagens.length; i++ ){
                var novoNome = UUID.randomUUID() + "." + extrairExtensao(imagens[i].getOriginalFilename());
                var caminhoFinal = caminhoImagens + novoNome;
                Files.copy(imagens[i].getInputStream(), Path.of(caminhoFinal), StandardCopyOption.REPLACE_EXISTING);
                historiaModel.setImgsHistoria(novoNome, i);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return this.repository.save(historiaModel);
    }

    public HistoriaModel deletarHistoria(long id){
        HistoriaModel historiaModel = null;
        Optional<HistoriaModel> optionalHistoriaModel = this.repository.findById(id);
        if (optionalHistoriaModel.isPresent()){
            historiaModel = optionalHistoriaModel.get();
            this.repository.deleteById(id);
        }
        return historiaModel;
    }
}
