package quiz;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatGPTGrader {

    static String API_KEY = "YOUR-API-KEY-HERE";
    public static GradeResult grade(String question, String answer){

        try{

            URL url = new URL("https://api.openai.com/v1/chat/completions");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);

            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);

            String prompt =
                """
                Grade this pet care answer from 0 to 100.

                Expect a one line answer.

                Return a very short one line reason (12 words or less).

                Return EXACTLY in this format:
                score:<number>
                reason:<short explanation>

                Question: %s
                Answer: %s
                """.formatted(question, answer);

            String safePrompt = prompt
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n");

            String body = """
            {
              "model": "gpt-4o-mini",
              "messages": [
                {"role": "system", "content": "You grade pet care answers."},
                {"role": "user", "content": "%s"}
              ],
              "max_tokens": 60
            }
            """.formatted(safePrompt);

            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.close();

            InputStream stream = (conn.getResponseCode() >= 400)
                    ? conn.getErrorStream()
                    : conn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            String line;
            String response = "";

            while((line = br.readLine()) != null){
                response += line;
            }

            br.close();

            String content = extractContent(response);
            System.out.println("AI content:\n" + content.replace("\\n", "\n").trim());

            int score = extractScore(content);
            String reason = extractReason(content);

            return new GradeResult(score, reason);

        }catch(Exception e){
            System.out.println("API Failed! using fallback");
            System.out.println(e.getMessage());

            int fallbackScore = (int)(Math.random() * 60 + 30);
            return new GradeResult(fallbackScore, null);
        }
    }

    static int extractScore(String text){

        Pattern p = Pattern.compile("score:\\s*(\\d+)");
        Matcher m = p.matcher(text);

        if(m.find()){
            return Integer.parseInt(m.group(1));
        }

        return 50;
    }

    static String extractReason(String text){

        Pattern p = Pattern.compile("reason:\\s*(.*)");
        Matcher m = p.matcher(text);

        if(m.find()){
            return m.group(1).trim();
        }

        return null;
    }

    static String extractContent(String json){

        Pattern p = Pattern.compile("\"content\"\\s*:\\s*\"(.*?)\"");
        Matcher m = p.matcher(json);

        if(m.find()){
            return m.group(1);
        }

        return "";
    }
}