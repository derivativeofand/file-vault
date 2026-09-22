package com.jha58.file_vault.rag;

import java.io.IOException;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jha58.file_vault.file.FileMetaData;
import com.jha58.file_vault.rag.TextExtractorService;

@Service 
public class RagService {    
    @Autowired 
    private TextExtractorService textExtractorService;

    @Autowired
    private TextChunker textChunker;

    @Autowired 
    private VectorStore vectorStore;

    public void processFile(FileMetaData file, MultipartFile rawFile) throws IOException {
        String text = textExtractorService.extractText(rawFile);    
        List<Document> chunks = textChunker.chunkText(text);

        for (Document chunk : chunks) {
            chunk.getMetadata().put("file_id", file.getId().toString());
        }

        vectorStore.add(chunks);
    }
}
