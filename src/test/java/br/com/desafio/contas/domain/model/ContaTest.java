package br.com.desafio.contas.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.desafio.contas.infrastructure.util.MockContaHelper;

@RunWith(MockitoJUnitRunner.class)
public class ContaTest {

	private Conta conta;
	
	@Before
	public void setUp() throws Exception {
		this.conta = MockContaHelper.getConta();
	}
	
	@Test
	public void deveRetornarConta() {
		assertNotNull(conta.getId());
		assertNotNull(conta.getDataVencimento());
		assertNotNull(conta.getDataPagamento());
		assertNotNull(conta.getDescricao());
		assertNotNull(conta.getValor());
		assertNotNull(conta.isSituacao());
	}
 }
