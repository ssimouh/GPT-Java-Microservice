package com.springGPT.springGPTDemo;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class ChatGPTMicroserviceTest {
    private final String API_KEY = "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";
    private final String CHAT_GPT_API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";

    @Test
    void testGenerateChatGPTRequestBodyJson() {
        String actual = ChatGPTMicroservice.generateChatGPTRequestBodyJson("Question");
        String expected = "{\"max_tokens\":1024,\"prompt\":\"Q: Question\\nA:\",\"temperature\":0.7}";
        assertEquals(expected, actual);
    }

    @Test
    void testAnswerQuestion() throws Exception {
        String question = "What is the capital of France?";
        String expectedAnswer = "The capital of France is Paris.";
        String actualAnswer = ChatGPTMicroservice.answerQuestion(question);
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void testAnswerQuestionWithInvalidAPIKey() {
        final String question = "What is the capital of France?";
        try {
            // Update invalid API key and run this test case
            String invalidApiKey = "anInvalidApiKey";
            System.setProperty("api_key", invalidApiKey);

            assertThrows(Exception.class, new Executable() {
                @Override
                public void execute() throws Throwable {
                    ChatGPTMicroservice.answerQuestion(question);
                }
            });
        } finally {
            System.clearProperty("api_key");
        }
    }

    @Test
    void testAnswerQuestionWithNullInput() {
        assertThrows(NullPointerException.class, () -> ChatGPTMicroservice.answerQuestion(null));
    }

    @Test
    void testAppendToCSVFile() throws IOException {
        String question = "What is the capital of Italy?";
        String answer = "The capital of Italy is Rome.";
        ChatGPTMicroservice.appendToCSVFile(question, answer);
    }

    @Test
    void testAppendToCSVFileWithNullInput() {
        assertThrows(NullPointerException.class, () -> ChatGPTMicroservice.appendToCSVFile(null, null));
    }
}
