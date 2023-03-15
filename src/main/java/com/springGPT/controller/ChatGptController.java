package com.springGPT.controller;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
@RequestMapping("/chatgpt")
public class ChatGptController {

    @PostMapping
    public String getAnswer(@RequestBody String question) throws IOException, JSONException {
        // Make ChatGPT Request
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        
        JSONObject requestBody = new JSONObject();
        
        requestBody.put("prompt", question);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 100);
        requestBody.put("top_p", 1);
        
        /////////////////////////////////////////////////////////////////////////////////////////////////
        
        okhttp3.RequestBody body = okhttp3.RequestBody.create(requestBody.toString(), mediaType);
        Request chatGptRequest = new Request.Builder()
               .url("https://api.openai.com/v1/engine/davinci-completions")
               .post((okhttp3.RequestBody) body)
               .addHeader("Content-Type", "application/json")
               .addHeader("Authorization", "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")
               .build();
        
        /////////////////////////////////////////////////////////////////////////////////////////////////

//        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, requestBody.toString());
        
//        Request chatGptRequest = new Request.Builder()
//          .url("https://api.openai.com/v1/engine/davinci-completions")
//          .post(body)
//          .addHeader("Content-Type", "application/json")
//          .addHeader("Authorization", "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")
//          .build();

        // Handle ChatGPT Response
        Response chatGptResponse = client.newCall(chatGptRequest).execute();
        JSONObject responseObj = new JSONObject(chatGptResponse.body().string());
        String answer = responseObj.getJSONArray(" choices").getJSONObject(0).getString("text");

        // Save Question and Answer to CSV File
        FileWriter csvWriter = new FileWriter("qa_pairs.csv", true);
        csvWriter.append(question).append(",").append(answer).append("\n");
        csvWriter.flush();
        csvWriter.close();

        // Return Answer to User
        return answer;
    }
}

