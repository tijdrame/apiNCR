package com.boa.api.response;

import java.util.Map;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InwardResponse extends GenericResponse{
    private Map<String, Object> dataInward;
}
