package br.com.desafio.contas.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import br.com.desafio.contas.domain.model.Conta;
import br.com.desafio.contas.infrastructure.repository.ContaRepository;
import br.com.desafio.contas.infrastructure.util.MockContaHelper;
import br.com.desafio.contas.infrastructure.util.Util;

public class ContaServiceTest {

	@InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testObterContaById() {
    	Conta conta = MockContaHelper.getConta();
    	Optional<Conta> optConta = Optional.of(conta);
    	when(contaRepository.findById(conta.getId())).thenReturn(optConta);   	
    	assertNotNull(contaService.obterContaById(conta.getId()));
    }
    
    @Test
    public void testObterListaDeContas() {
    	Conta conta = MockContaHelper.getConta();
    	List<Conta> contas = new ArrayList<Conta>();
    	contas.add(conta);
    	Page<Conta> pageConta = new PageImpl<>(contas);
    	when(contaRepository.findByDataAndDescricao(conta.getDataVencimento(), conta.getDescricao(), (Pageable) any(Pageable.class))).thenReturn(pageConta);   	
    	assertNotNull(contaService.obterListaDeContas(conta, (Pageable) any(Pageable.class)));
    }
    
    @Test
    public void testSaveOrUpdateConta() {
        Conta conta = MockContaHelper.getConta();
        contaService.saveOrUpdateConta(conta);
        verify(contaRepository, times(1)).save(conta);
    }
    
    @Test
    public void testAtualizarSituacao() {
    	Conta conta = MockContaHelper.getConta();
        conta.setSituacao(false);
        contaService.atualizarSituacao(conta);
        verify(contaRepository, times(1)).updateSituacaoById(conta.isSituacao(), conta.getId());
    }
    
    @Test
    public void testDeleteConta() {
    	Conta conta = MockContaHelper.getConta();
        contaService.deleteConta(conta);
        verify(contaRepository, times(1)).delete(conta);
    }

    @Test
    public void testImportarArquivoCSV() throws IOException {
    	
    	String csvContent = "id;data_vencimento;data_pagamento;valor;descricao;situacao\n" +
                "1;2024-08-01;2024-07-22;100.00;Pagamento de teste;true\n";
    	
    	MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));    	
    	contaService.importarArquivoCSV(file);
        verify(contaRepository, times(2)).save(any(Conta.class));
    }
    
    @Test
    public void testObterTotalPagoPorPeriodo() {

        LocalDate startDate = Util.getLocalDateValue("2024-07-01");
        LocalDate endDate = Util.getLocalDateValue("2024-07-31");
        BigDecimal valorTotalPago = new BigDecimal("1000.00");

        when(contaRepository.findTotalPaidValueBetweenDates(startDate, endDate)).thenReturn(valorTotalPago);

        String resultado = contaService.obterTotalPagoPorPeriodo(startDate, endDate);
        String objetivo = Util.formatBigDecimalToBrazil(valorTotalPago);
        assertEquals(objetivo, resultado);

        verify(contaRepository).findTotalPaidValueBetweenDates(startDate, endDate);
    }
}