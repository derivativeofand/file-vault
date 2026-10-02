package com.jha58.file_vault.rag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/chat")
public class RagController {
    
    @Autowired 
    private RagService ragService;

    @PostMapping("/{fileId}")
    public String ask(@RequestBody String question, @PathVariable Long fileId) {
        return ragService.answerQuestion(fileId, question);
    }
    
}
