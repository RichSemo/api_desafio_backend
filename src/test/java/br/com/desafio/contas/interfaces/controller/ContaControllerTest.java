package br.com.desafio.contas.interfaces.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.desafio.contas.domain.model.Conta;
import br.com.desafio.contas.domain.service.ContaService;
import br.com.desafio.contas.infrastructure.util.MockContaHelper;
import br.com.desafio.contas.infrastructure.util.Util;

@WebMvcTest(ContaController.class)
public class ContaControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContaService contaService;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    private Conta conta;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        conta = MockContaHelper.getConta();
    }
    
    @Test
    public void testUploadCsv() throws Exception {
    	
    	MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "conteúdo,csv".getBytes());

        doNothing().when(contaService).importarArquivoCSV(any());

        mockMvc.perform(multipart("/api/desafio/contas/uploadCSV")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Importação bem-sucedida!"));
    }
    
    @Test
    public void testUploadCsv_Failure() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "conteúdo,csv".getBytes());

        doThrow(new IOException("Erro de leitura")).when(contaService).importarArquivoCSV(any());

        mockMvc.perform(multipart("/api/desafio/contas/uploadCSV")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Erro ao importar o arquivo CSV: Erro de leitura"));
    }
    
    @Test
    public void testCadastrarConta() throws JsonProcessingException, Exception {
    	
    	doNothing().when(contaService).salvarConta(any(Conta.class));

    	mockMvc.perform(post("/api/desafio/contas/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isCreated())
                .andExpect(content().string(Util.asJsonString(conta)));
    }
    
    @Test
    public void testConsultarConta() throws Exception {
    	
    	when(contaService.obterContaById(conta.getId())).thenReturn(conta);

        mockMvc.perform(get("/api/desafio/contas/consultar/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(Util.asJsonString(conta)));
    }
    
    @Test
    public void testObterListaPorDataDescricao() throws Exception {

    	Pageable pageable = PageRequest.of(0, 10);
        Page<Conta> page = new PageImpl<>(Arrays.asList(conta), pageable, 1);

        when(contaService.obterListaDeContas(any(Conta.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(post("/api/desafio/contas/obterListaPorDataDescricao")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
                .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].descricao").value(conta.getDescricao()));
    }
    
    @Test
    public void testAtualizarConta() throws Exception {

    	doNothing().when(contaService).atualizarConta(any(Conta.class));

        mockMvc.perform(post("/api/desafio/contas/atualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isOk())
                .andExpect(content().string(Util.asJsonString(conta)));
    }
    
    @Test
    public void testAlterarSituacaoConta() throws Exception {
    	
    	doNothing().when(contaService).atualizarSituacao(any(Conta.class));

        mockMvc.perform(post("/api/desafio/contas/alterarSituacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isOk())
                .andExpect(content().string(Util.asJsonString(conta)));
    }
    
    @Test
    public void testDeletarConta() throws Exception {
    	
    	doNothing().when(contaService).deleteConta(any(Conta.class));

        mockMvc.perform(post("/api/desafio/contas/deletar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conta)))
                .andExpect(status().isOk())
                .andExpect(content().string(Util.asJsonString(conta)));
    }
    
    @Test
    public void testObterTotalPagoPeriodo() throws Exception {

    	LocalDate startDate = Util.getLocalDateValue("2024-07-01");
        LocalDate endDate = Util.getLocalDateValue("2024-07-31");
    	
        String valorTotalPago = Util.formatBigDecimalToBrazil(new BigDecimal("1000.00"));
        when(contaService.obterTotalPagoPorPeriodo(startDate, endDate)).thenReturn(valorTotalPago);

        mockMvc.perform(get("/totalPagoPeriodo")
        		.param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
        		.andExpect(status().isOk())
                .andExpect(content().string(valorTotalPago));
    }
}
