package com.springGPT.springGPTDemo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;


public class ChatGPTMicroservice {

  private static final String CHAT_GPT_API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";
  private static final String API_KEY = "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";

//  private static final OkHttpClient client = new OkHttpClient();
  private static final Gson gson = new Gson();
  
  static OkHttpClient client = new OkHttpClient.Builder()
		  .connectTimeout(30, TimeUnit.SECONDS) //30 seconds connect timeout
		  .writeTimeout(30, TimeUnit.SECONDS)
		  .readTimeout(30, TimeUnit.SECONDS) //30 seconds read timeout
		  .build();

  public static String answerQuestion(String question) throws IOException {
    String requestBodyJson = generateChatGPTRequestBodyJson(question);
    RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestBodyJson);
    Request request =
        new Request.Builder()
            .header("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + API_KEY)
            .url(CHAT_GPT_API_URL)
            .post(requestBody)
            .build();

    
    System.out.println(requestBodyJson);
    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//      String responseBodyString = response.body().string();
//      ChatGPTResponse chatGPTResponse = gson.fromJson(responseBodyString, ChatGPTResponse.class);
//      String answer = chatGPTResponse.getChoices()[0].getText().trim();
      
      String responseJson = response.body().string();
      JSONObject jsonResponse = new JSONObject(responseJson);
      System.out.println(jsonResponse);
      JSONArray choices = jsonResponse.getJSONArray("choices");
      String answer = choices.getJSONObject(0).getString("text");
      
      String answerBeforeNewLine = "";
      
      int nlIndex = answer.indexOf("Q:");
      if (nlIndex != -1) { // Make sure there is at least one newline character
          answerBeforeNewLine = answer.substring(0, nlIndex);
      } else {
          answerBeforeNewLine = answer;
      }      
      

      // Append question-answer pair to CSV file
      appendToCSVFile(question, answerBeforeNewLine);
      
      // Return the answer to the caller
      return answerBeforeNewLine;
    }
  }

  static String generateChatGPTRequestBodyJson(String question) {
//    return "{\"prompt\":\"Q: " + question.trim() + "\\nA:\",\"temperature\":0.7,\"max_tokens\":1024}";
	  JSONObject requestBody = new JSONObject();
	  requestBody.put("prompt", "Q: " + question.trim() + "\nA:");
	  requestBody.put("temperature", 0.7);
	  requestBody.put("max_tokens", 1024);

	  return requestBody.toString();
  
  }

  static void appendToCSVFile(String question, String answer) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("qa.csv", true))) {
      writer.write("\"" + question + "\",\"" + answer + "\"\n");
    }
  }
}

class ChatGPTResponse {
  private Choice[] choices;

  public Choice[] getChoices() {
    return choices;
  }

  public void setChoices(Choice[] choices) {
    this.choices = choices;
  }
}

class Choice {
  private String text;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}

