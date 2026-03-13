package quiz;

import java.util.Random;

import main.GamePanel;

public class QuizManager {

    GamePanel gp;

    String question;
    String playerAnswer;

    public int quizState = 0;

    long lastQuizTime = 0;
    long quizCooldown = 60000; // 60 seconds (for testing)

    public QuizManager(GamePanel gp){
        this.gp = gp;
    }

    public void startQuiz(){
        if(gp.ui.typingMode) return;

        long now = System.currentTimeMillis();

        if(now - lastQuizTime < quizCooldown){

            long remaining = (quizCooldown - (now - lastQuizTime)) / 1000;

            gp.ui.startDialogue(new String[]{
                "You've already taken a quiz.",
                "Come back in " + remaining + " seconds."
            });

            return;
        }

        lastQuizTime = now;

        String[] questions = {
            "Why is exercise important for pets?",
            "Name two signs a pet may be sick.",
            "Why should pets visit the vet?"
        };

        question = questions[new Random().nextInt(questions.length)];

        quizState = 1; 

        gp.ui.startDialogue(new String[]{
            "Pet Care Quiz!",
            question,
            "Type your answer."
        });

    }

    public void beginTyping(){
        gp.ui.typingMode = true;
        quizState = 2;
    }

    public void submitAnswer(String answer){

        quizState = 3;

        int score = ChatGPTGrader.grade(question, answer);

        int reward = score / 2;

        gp.money += reward;

        gp.wallet.addTransaction("Quiz Reward", reward);

        gp.ui.startDialogue(new String[]{
            "Hmm...",
            "Your answer score: " + score + "/100",
            "You earned $" + reward + "!"
        });

        quizState = 0;
    }
}