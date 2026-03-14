package quiz;

import java.util.Random;

import main.GamePanel;

public class QuizManager {

    GamePanel gp;

    String question;
    String playerAnswer;

    public int quizState = 0;

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

        quizState = 3;

        GradeResult result = ChatGPTGrader.grade(question, answer);

        int score = result.score;
        String reason = result.reason;
        

        int reward = score / 2;

        gp.money += reward;

        gp.wallet.addTransaction("Quiz Reward", reward);

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
                reason,
                "You earned $" + reward + "!"
            });
}

        quizState = 0;
    }
}