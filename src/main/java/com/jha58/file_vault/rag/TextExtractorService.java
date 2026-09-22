package com.jha58.file_vault.rag;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.ai.document.Document;

import java.io.IOException;
import java.util.List;


@Service
public class TextExtractorService {

    public String extractText(MultipartFile file) throws IOException {
        Resource resource = new InputStreamResource(file.getInputStream());   
        TikaDocumentReader reader = new TikaDocumentReader(resource);
        List<Document> documents = reader.get();
        
        StringBuilder text = new StringBuilder();
        for (Document doc : documents) {
            text.append(doc.getText());
        }
        return text.toString();
    }
    
}
