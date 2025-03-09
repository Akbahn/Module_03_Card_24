package org.main.module_03_card_24;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenAIHelper {
    private static final String API_KEY = "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";

    public static String chatGPT(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        String model = "gpt-3.5-turbo";

        try {
            // Create HTTP connection
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + API_KEY);
            con.setRequestProperty("Content-Type", "application/json");

            // Build request body
            String body = "{"
                    + "\"model\": \"" + model + "\","
                    + "\"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}],"
                    + "\"max_tokens\": 50"
                    + "}";

            // Send request
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = body.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read response
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // Parse JSON response
            return extractChatResponse(response.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return "Error retrieving response from OpenAI.";
        }
    }

    private static String extractChatResponse(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);
        JSONArray choices = json.getJSONArray("choices");
        if (choices.length() > 0) {
            return choices.getJSONObject(0).getJSONObject("message").getString("content").trim();
        }
        return "No response from AI.";
    }
}
