package com.project.daily.model.ai;

import lombok.Data;
import java.util.List;

@Data
public class LlamaResponse {
    private List<LlamaChoice> choices;
}
