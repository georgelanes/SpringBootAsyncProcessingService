package br.com.globit.async_processing.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.globit.async_processing.model.ProcessRequest;
import br.com.globit.async_processing.model.ProcessResult;
import br.com.globit.async_processing.model.ProcessStatus;
import br.com.globit.async_processing.service.AsyncProcessingService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/process")
@Slf4j
public class ProcessController {
    
    @Autowired
    private AsyncProcessingService asyncService;
    
    @PostMapping
    public ResponseEntity<ProcessResult> startProcess(@RequestBody ProcessRequest request) {
        log.info("Recebida nova requisição de processamento: {}", request.getId());
        
        // Inicia o processamento assíncrono
        asyncService.processDataAsync(request);
        
        // Retorna status inicial
        ProcessResult initialStatus = new ProcessResult();
        initialStatus.setId(request.getId());
        initialStatus.setStatus(ProcessStatus.PENDING.name());
        initialStatus.setLastUpdated(LocalDateTime.now());
        
        return ResponseEntity.accepted().body(initialStatus);
    }
    
    @GetMapping("/status/{id}")
    public ResponseEntity<ProcessResult> getStatus(@PathVariable String id) {
        return asyncService.getProcessStatus(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}