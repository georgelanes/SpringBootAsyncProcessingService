package br.com.globit.async_processing.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProcessResult {
    private String id;
    private String status;
    private Object result;
    private LocalDateTime lastUpdated;
}