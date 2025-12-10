package com.project.daily.model.ai;


import lombok.Data;
import java.util.List;

@Data
public class LlamaRequest {
    private List<LlamaMessage> messages;
}
