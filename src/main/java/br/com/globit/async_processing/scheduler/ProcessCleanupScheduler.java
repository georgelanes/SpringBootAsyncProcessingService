package br.com.globit.async_processing.scheduler;

import br.com.globit.async_processing.repository.ProcessTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@EnableScheduling
@Slf4j
public class ProcessCleanupScheduler {

    @Autowired
    private ProcessTracker processTracker;
    
    @Value("${async.cleanup.retention-days:7}")
    private int retentionDays;

    @Scheduled(
        fixedRateString = "${async.cleanup.interval-ms:60000}",
        initialDelayString = "${async.cleanup.initial-delay-ms:60000}"
    )
    public void cleanOldProcessings() {
        log.info("Iniciando limpeza agendada de processamentos antigos...");
        try {
            processTracker.removeProcessingsOlderThan(
                LocalDateTime.now().minus(retentionDays, ChronoUnit.DAYS)
            );
            log.info("Limpeza agendada concluída com sucesso");
        } catch (Exception e) {
            log.error("Erro durante a limpeza agendada", e);
        }
    }
}