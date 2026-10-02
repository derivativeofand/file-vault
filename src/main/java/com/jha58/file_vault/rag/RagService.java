package com.jha58.file_vault.rag;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.jha58.file_vault.file.FileMetaData;

@Service 
public class RagService {    
    @Autowired 
    private TextExtractorService textExtractorService;

    @Autowired
    private TextChunker textChunker;

    @Autowired 
    private VectorStore vectorStore;

    @Autowired 
    private ChatModel chatModel;
    
    public void processFile(FileMetaData file, MultipartFile rawFile) throws IOException {
        String text = textExtractorService.extractText(rawFile);    
        List<Document> chunks = textChunker.chunkText(text);

        for (Document chunk : chunks) {
            chunk.getMetadata().put("file_id", file.getId().toString());
            chunk.getMetadata().put("user_id", file.getOwner().getId().toString());
        }

        vectorStore.add(chunks);
    }

    public void deleteVectorsByUserId(Long userId) {
        FilterExpressionBuilder builder = new FilterExpressionBuilder();
        Filter.Expression filter = builder.eq("user_id", userId.toString()).build();
        vectorStore.delete(filter);
    }

    public void deleteVectorsByFileId(Long fileId) {
        FilterExpressionBuilder builder = new FilterExpressionBuilder();
        Filter.Expression filter = builder.eq("file_id", fileId.toString()).build();
        vectorStore.delete(filter);
    }

    public String answerQuestion(Long fileId, String question) {
        // Search for relevant chunks in the vector store based on the question and file ID
        FilterExpressionBuilder builder = new FilterExpressionBuilder();
        Filter.Expression filter = builder.eq("file_id", fileId.toString()).build();

        List<Document> relevantChunks = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(question)
                .filterExpression("file_id == '" + fileId + "'")
                .topK(5)
                .build()
        );

        // Combine the content of the relevant chunks to form a context for answering the question
        StringBuilder contextBuilder = new StringBuilder();

        System.out.println(relevantChunks.size());
        for (int i = 0; i < relevantChunks.size(); i++) {
            Document chunk = relevantChunks.get(i);
            contextBuilder.append(chunk.getText());
        
            if (i < relevantChunks.size() - 1) {
                contextBuilder.append("\n\n");
            }
        }

        String context = contextBuilder.toString();
        // Prompt Engineering 
        String prompt = """
                        Answer the user's question based on only on the following context.
                        If the answer isn't in the context, say you don't know. Do not make up an answer.

                        Context:
                        %s
                        Question: %s
                        """. formatted(context, question);
        System.out.println("RETRIEVED CONTEXT: \n" + context);
        return chatModel.call(prompt);
    }
}
