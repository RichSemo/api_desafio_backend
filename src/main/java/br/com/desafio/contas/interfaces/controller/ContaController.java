package br.com.desafio.contas.interfaces.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.desafio.contas.domain.model.Conta;
import br.com.desafio.contas.domain.service.ContaService;

@RestController
@RequestMapping("/api/desafio/contas")
public class ContaController {

	@Autowired
	private ContaService contaService;
	
	@PostMapping("/uploadCSV")
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
		try {
			contaService.importarArquivoCSV(file);
            return "Importação bem-sucedida!";
        } catch (IOException e) {
            return "Erro ao importar o arquivo CSV: " + e.getMessage();
        }
    }
	
	@PostMapping("/cadastrar")
	public ResponseEntity<Conta> cadastrarConta(@RequestBody Conta conta) {
		contaService.saveOrUpdateConta(conta);
		return new ResponseEntity<>(conta, HttpStatus.CREATED);
	}
	
	@GetMapping("/consultar/{id}")
	public ResponseEntity<Conta> consultarConta(@PathVariable long id) {
		return new ResponseEntity<>(contaService.obterContaById(id), HttpStatus.OK);	
	}
	
	@PostMapping("/obterListaPorDataDescricao")
	public ResponseEntity<Page<Conta>> obterListaPorDataDescricao(@RequestBody Conta conta, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {		
		Pageable pageable = PageRequest.of(page, size);
        Page<Conta> listaPorFiltro = contaService.obterListaDeContas(conta, pageable);
		return new ResponseEntity<>(listaPorFiltro, HttpStatus.OK);	
	}
	
	@PostMapping("/atualizar")
	public ResponseEntity<Conta> atualizarConta(@RequestBody Conta conta) {
		contaService.saveOrUpdateConta(conta);
		return new ResponseEntity<>(conta, HttpStatus.OK);		
	}
	
	@PostMapping("/alterarSituacao")
	public ResponseEntity<Conta> alterarSituacaoConta(@RequestBody Conta conta) {
		contaService.atualizarSituacao(conta);
		return new ResponseEntity<>(conta, HttpStatus.OK);
	}
	
	@PostMapping("/deletar")
	public ResponseEntity<Conta> deletarConta(@RequestBody Conta conta) {
		contaService.deleteConta(conta);
		return new ResponseEntity<>(conta, HttpStatus.OK);		
	}
	
	@GetMapping("/totalPagoPeriodo")
	public String obterTotalPagoPeriodo(
			@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,			
	        @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate) {
		
		return "R$ " + contaService.obterTotalPagoPorPeriodo(startDate, endDate);	
	}
}