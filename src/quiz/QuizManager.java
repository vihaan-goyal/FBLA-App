package quiz;

import java.util.Random;
import javax.swing.SwingUtilities;
import main.GamePanel;

public class QuizManager {

    GamePanel gp;
    String question;
    String playerAnswer;

    public int quizState = 0;
    public boolean loading = false;   // add this

    public QuizManager(GamePanel gp){
        this.gp = gp;
    }

    public void startQuiz(){
        gp.ui.speaker = "Quizzard";
        if(gp.ui.typingMode) return;

        String[] questions = {
            "Why is exercise important for pets?",
            "Name two signs a pet may be sick.",
            "Why should pets visit the vet?",
            "Name one thing dogs shouldn't eat."
        };

        question = questions[new Random().nextInt(questions.length)];

        quizState = 1; 

        gp.ui.startDialogue(new String[]{
            "Test your pet care knowledge!",
            "If I like your answer, you'll get a reward!",
            question,
            "Type your answer."
        });

    }

    public void beginTyping(){
        gp.ui.typingMode = true;
        quizState = 2;
    }

    public void submitAnswer(String answer){

        // capture values for the background thread
        final String q = question;
        final String a = answer;

        loading = true;
        quizState = 3; // grading
        gp.ui.speaker = "Quizzard";

        Thread graderThread = new Thread(() -> {

            long start = System.currentTimeMillis();

            // runs OFF the UI thread, so the game keeps rendering
            GradeResult result = ChatGPTGrader.grade(q, a);

            // enforce a minimum 3 second spinner so the animation always shows
            long elapsed = System.currentTimeMillis() - start;
            if(elapsed < 3000){
                try {
                    Thread.sleep(3000 - elapsed);
                } catch(InterruptedException e){
                    // ignore, just proceed
                }
            }

            // apply the result back on the UI thread
            SwingUtilities.invokeLater(() -> {

                int score = result.score;
                String reason = result.reason;

                int reward = score / 2;

                gp.wallet.earn("Quiz Reward", reward);

                if(reason != null){
                    gp.ui.startDialogue(new String[]{
                        "Hmm...",
                        "Your answer score: " + score + "/100",
                        reason,
                        "You earned $" + reward + "!"
                    });
                }else{
                    gp.ui.startDialogue(new String[]{
                        "Hmm...",
                        "Your answer score: " + score + "/100",
                        "You earned $" + reward + "!"
                    });
                }

                loading = false;
                quizState = 0;
            });
        });

        graderThread.setDaemon(true);
        graderThread.start();
    }   
}