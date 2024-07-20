package br.com.desafio.contas.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.desafio.contas.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta,Long>{

	@Transactional
	@Query("SELECT c FROM Conta c WHERE c.id = :id")
	Optional<Conta> findById(@Param("id") long id);
	
	<S extends Conta> S save(Conta conta);
	
	void delete(Conta conta);
	
    @Transactional
	@Query("SELECT c FROM Conta c WHERE c.dataVencimento = :dataVencimento AND c.descricao = :descricao")
    Optional<Conta> findByDataAndDescricao(@Param("dataVencimento") LocalDate dataVencimento, @Param("descricao") String descricao);
	
	@Modifying
    @Transactional
    @Query("UPDATE Conta c SET c.situacao = :situacao WHERE c.id = :id")
    int updateSituacaoById(@Param("situacao") boolean situacao, @Param("id") long id);
}
