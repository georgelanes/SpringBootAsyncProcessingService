package br.com.globit.async_processing.model;

import java.util.Map;

import lombok.Data;

@Data
public class ProcessRequest {
    private String id;
    private Map<String, Object> data;
}