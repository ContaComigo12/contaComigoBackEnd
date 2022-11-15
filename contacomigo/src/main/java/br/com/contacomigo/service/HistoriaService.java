package br.com.contacomigo.service;

import br.com.contacomigo.exceptions.SalvamentoImagemException;
import br.com.contacomigo.model.HistoriaModel;
import br.com.contacomigo.repository.HistoriaRepository;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public  static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        multipart.transferTo(convFile);
        return convFile;
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

    public HistoriaModel salvarHistoria(String titulo, String categoria, String subCategoria, MultipartFile[] imagens) throws Exception {
        HistoriaModel historiaModel = new HistoriaModel();
        historiaModel.setTitulo(titulo);
        historiaModel.setCategoria(categoria);
        historiaModel.setSubCategoria(subCategoria);
        try{
            for (int i = 0; i < imagens.length; i++ ){
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image",imagens[i].getOriginalFilename(),
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        new File(multipartToFile(imagens[i], "temporario").toURI())))
                        .build();
                Request request = new Request.Builder()
                        .url("https://api.imgur.com/3/upload")
                        .method("POST", body)
                        .addHeader("Authorization", "Bearer df254b79d2e0e37f6de79b4c7e268e5b4e34e808")
                        .build();
                Response response = client.newCall(request).execute();
                System.out.println(response);
                JSONObject imgur = new JSONObject(response.body().string());
                JSONObject data = imgur.getJSONObject("data");
                String imgLink = (String) data.getString("link");

                historiaModel.setImgsHistoria(imgLink, i);
            }
            return this.repository.save(historiaModel);

        }catch (Exception e){
            System.out.println(e);
            throw new SalvamentoImagemException("Não foi possível Salvar", 503);
        }
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

    public HistoriaModel updateHistoria(long id, String titulo, String categoria, String subCategoria, MultipartFile[] imagens) throws Exception {
        HistoriaModel historiaModel = new HistoriaModel();
        historiaModel.setId(id);
        historiaModel.setTitulo(titulo);
        historiaModel.setCategoria(categoria);
        historiaModel.setSubCategoria(subCategoria);
        try{
            for (int i = 0; i < imagens.length; i++ ){
                System.out.println(imagens[i]);
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image",imagens[i].getOriginalFilename(),
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        new File(multipartToFile(imagens[i], "temporario").toURI())))
                        .build();
                Request request = new Request.Builder()
                        .url("https://api.imgur.com/3/upload")
                        .method("POST", body)
                        .addHeader("Authorization", "Bearer df254b79d2e0e37f6de79b4c7e268e5b4e34e808")
                        .build();
                Response response = client.newCall(request).execute();
                System.out.println(response);
                JSONObject imgur = new JSONObject(response.body().string());
                JSONObject data = imgur.getJSONObject("data");
                String imgLink = (String) data.getString("link");

                historiaModel.setImgsHistoria(imgLink, i);
            }
            return this.repository.save(historiaModel);

        }catch (Exception e){
            System.out.println(e);
            throw new SalvamentoImagemException("Não foi possível Salvar", 503);
        }
    }
}
