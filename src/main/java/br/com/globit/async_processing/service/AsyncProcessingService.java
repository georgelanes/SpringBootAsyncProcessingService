package br.com.globit.async_processing.service;

import br.com.globit.async_processing.exception.AsyncProcessingException;
import br.com.globit.async_processing.model.ProcessRequest;
import br.com.globit.async_processing.model.ProcessResult;
import br.com.globit.async_processing.model.ProcessStatus;
import br.com.globit.async_processing.repository.ProcessTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AsyncProcessingService {
    
    @Autowired
    private ProcessTracker processTracker;
    
    @Async
    public CompletableFuture<ProcessResult> processDataAsync(ProcessRequest request) {
        log.info("Iniciando processamento assíncrono para request: {}", request.getId());
        
        // Registra início do processamento
        processTracker.trackProcess(request.getId(), ProcessStatus.PROCESSING, null);
        
        try {
            // Simulando processamento longo
            Thread.sleep(5000);
            
            // Simulando resultado do processamento
            Map<String, Object> processedData = new HashMap<>();
            processedData.put("processedAt", LocalDateTime.now());
            processedData.put("originalData", request.getData());
            
            // Registra sucesso
            ProcessResult result = new ProcessResult();
            result.setId(request.getId());
            result.setStatus(ProcessStatus.COMPLETED.name());
            result.setResult(processedData);
            
            processTracker.trackProcess(request.getId(), ProcessStatus.COMPLETED, processedData);
            
            log.info("Processamento concluído para request: {}", request.getId());
            return CompletableFuture.completedFuture(result);
            
        } catch (Exception e) {
            log.error("Erro no processamento assíncrono", e);
            // Registra falha
            processTracker.trackProcess(request.getId(), ProcessStatus.FAILED, e.getMessage());
            throw new AsyncProcessingException("Erro no processamento", e);
        }
    }
    
    public Optional<ProcessResult> getProcessStatus(String id) {
        return processTracker.getProcessStatus(id);
    }
}