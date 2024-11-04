package br.com.globit.async_processing.repository;

import br.com.globit.async_processing.model.ProcessResult;
import br.com.globit.async_processing.model.ProcessStatus;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ProcessTracker {
    private final ConcurrentHashMap<String, ProcessResult> processings = new ConcurrentHashMap<>();
    
    public void trackProcess(String id, ProcessStatus status, Object result) {
        ProcessResult processResult = new ProcessResult();
        processResult.setId(id);
        processResult.setStatus(status.name());
        processResult.setResult(result);
        processResult.setLastUpdated(LocalDateTime.now());
        
        processings.put(id, processResult);
        log.debug("Status atualizado para processo {}: {}", id, status);
    }
    
    public Optional<ProcessResult> getProcessStatus(String id) {
        return Optional.ofNullable(processings.get(id));
    }

    @Scheduled(fixedRate = 86400000) // 24 horas em milissegundos
    public void cleanOldProcessings() {
        log.info("Iniciando limpeza de processamentos antigos...");
        
        LocalDateTime cutoffTime = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        int initialSize = processings.size();
        
        processings.entrySet().removeIf(entry -> 
            entry.getValue().getLastUpdated().isBefore(cutoffTime));
        
        int removedCount = initialSize - processings.size();
        log.info("Limpeza concluída. {} processamentos removidos.", removedCount);
    }

    public void removeProcessingsOlderThan(LocalDateTime cutoffTime) {
        log.debug("Removendo processamentos anteriores a {}", cutoffTime);
        
        int initialSize = processings.size();
        processings.entrySet().removeIf(entry -> 
            entry.getValue().getLastUpdated().isBefore(cutoffTime));
        
        int removedCount = initialSize - processings.size();
        if (removedCount > 0) {
            log.info("Removidos {} processamentos antigos", removedCount);
        }
    }
}