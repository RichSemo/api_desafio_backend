package br.com.desafio.contas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.desafio.contas.model.Conta;
import br.com.desafio.contas.service.ContaService;

@RestController
@RequestMapping("/api/desafio/contas")
public class ContasController {

	@Autowired
	private ContaService contaService;
	
	@PostMapping("/upload-csv")
    public ResponseEntity<List<Conta>> uploadCsv(@RequestParam("file") MultipartFile file) {
        List<Conta> contas = contaService.conversorCSVparaConta(file);
        return new ResponseEntity<>(contas, HttpStatus.OK);
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
	
	@PostMapping("/obter-lista-por-filtro")
	public ResponseEntity<List<Conta>> obterListaContaPorFiltro(@RequestBody Conta conta) {
		return new ResponseEntity<>(contaService.obterListaDeContas(conta), HttpStatus.OK);	
	}
	
	@PostMapping("/atualizar")
	public ResponseEntity<Conta> atualizarConta(@RequestBody Conta conta) {
		contaService.saveOrUpdateConta(conta);
		return new ResponseEntity<>(conta, HttpStatus.OK);		
	}
	
	@PostMapping("/alterar-situacao")
	public ResponseEntity<Conta> alterarSituacaoConta(@RequestBody Conta conta) {
		contaService.atualizarSituacao(conta);
		return new ResponseEntity<>(conta, HttpStatus.OK);
	}
	
	@PostMapping("/deletar")
	public ResponseEntity<Conta> deletarConta(@RequestBody Conta conta) {
		contaService.deleteConta(conta);
		return new ResponseEntity<>(conta, HttpStatus.OK);		
	}
}