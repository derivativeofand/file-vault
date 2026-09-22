package com.jha58.file_vault.rag;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

@Component
public class TextChunker {

    private final TokenTextSplitter textSplitter;

    public TextChunker() {
        this.textSplitter = TokenTextSplitter.builder()
                    .withChunkSize(800)
                    .withMinChunkSizeChars(100)
                    .withMinChunkLengthToEmbed(5)
                    .withMaxNumChunks(10000)
                    .withKeepSeparator(true)
                    .build();
    }

    public List<Document> chunkText(String text) {
        Document document = new Document(text);
        return textSplitter.split(document);
    }
}