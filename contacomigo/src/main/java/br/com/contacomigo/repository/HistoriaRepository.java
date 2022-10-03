package br.com.contacomigo.repository;

import br.com.contacomigo.model.HistoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriaRepository extends JpaRepository<HistoriaModel, Long> {
    public List<HistoriaModel> findAllByCategoria(String categoria);

    public List<HistoriaModel> findAllBySubCategoria(String subCategoria);
}
