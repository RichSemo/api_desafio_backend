package br.com.desafio.contas.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.desafio.contas.model.Conta;
import br.com.desafio.contas.repository.ContaRepository;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRep;
	
	public Conta obterContaById(long id) {
		Optional<Conta> optConta = contaRep.findById(id);
		
		if(optConta.isEmpty()) {
			return null;
		} else return optConta.get();
	}
	
	public List<Conta> obterListaDeContas(Conta conta){
		Optional<Conta> optConta = contaRep.findByDataAndDescricao(conta.getDataVencimento(), conta.getDescricao());
		return convertOptionalToList(optConta);
	}
	
	public void saveOrUpdateConta(Conta conta) {
		contaRep.save(conta);
	}
	
	public void atualizarSituacao(Conta conta) {
		contaRep.updateSituacaoById(conta.isSituacao(), conta.getId());
	}
	
	public void deleteConta(Conta conta) {
		contaRep.delete(conta);
	}
	
	public List<Conta> conversorCSVparaConta(MultipartFile file) {
		
        List<Conta> contas = new ArrayList<>();

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
        	
            //CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("id", "data_vencimento", "data_pagamento", "valor", "descricao", "situacao").withSkipHeaderRecord());
            
            for (CSVRecord csvRecord : csvParser) {
            	
            	long id = Long.parseLong(csvRecord.get("id"));
                String dataVencimento = csvRecord.get("data_vencimento");
                String dataPagamento = csvRecord.get("data_pagamento");
                BigDecimal valor = new BigDecimal(csvRecord.get("valor"));
                String descricao = csvRecord.get("descricao");
                boolean situacao = Boolean.parseBoolean(csvRecord.get("situacao"));
            	
                Conta conta = new Conta();
                conta.setId(id);
                conta.setDataVencimento(LocalDate.parse(dataVencimento));
                conta.setDataPagamento(LocalDate.parse(dataPagamento));
                conta.setValor(valor);
                conta.setDescricao(descricao);
                conta.setSituacao(situacao);
                contas.add(conta);
            }
            
            csvParser.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return contas;
    }
	
    private List<Conta> convertOptionalToList(Optional<Conta> optionalConta) {
        List<Conta> contas = new ArrayList<>();
        optionalConta.ifPresent(contas::add);
        return contas;
    }
}
