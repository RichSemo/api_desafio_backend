package br.com.desafio.contas.infrastructure.util;

import java.math.BigDecimal;

import br.com.desafio.contas.domain.model.Conta;

public class MockContaHelper {
	
	public static Conta getConta() {
		
		Conta conta = new Conta();
		conta.setId(1);
		conta.setDataVencimento(Util.getLocalDateValue("2024-08-01"));
		conta.setDataPagamento(Util.getLocalDateValue("2024-07-22"));
		conta.setValor(new BigDecimal("100.00"));
		conta.setDescricao("Pagamento de teste");
		conta.setSituacao(true);
		return conta;
	}
	

}
