package org.main.module_03_card_24;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LayoutController {

    @FXML
    private ImageView card01;

    @FXML
    private ImageView card02;

    @FXML
    private ImageView card03;

    @FXML
    private ImageView card04;

    @FXML
    private Button hintBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private TextField answerTxtF;

    @FXML
    private Button verifyBtn;

    private List<String> cardNames;
    private final List<String> selectedCards = new ArrayList<>();
    private static final String API_KEY = System.getenv("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");

    public void initialize() {
        cardNames = new ArrayList<>();
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        String[] ranks = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};

        for (String rank : ranks) {
            for (String suit : suits) {
                cardNames.add(rank + "_of_" + suit + ".png");
            }
        }

        refreshCards(null);
    }

    @FXML
    void refreshCards(ActionEvent event) {
        List<String> uniqueCards = getUniqueRandomCards(4);
        setCardImage(card01, uniqueCards.get(0));
        setCardImage(card02, uniqueCards.get(1));
        setCardImage(card03, uniqueCards.get(2));
        setCardImage(card04, uniqueCards.get(3));

        selectedCards.clear();
        selectedCards.addAll(uniqueCards);
    }

    private List<String> getUniqueRandomCards(int count) {
        List<String> uniqueCards = new ArrayList<>();
        List<String> availableRanks = Arrays.asList("ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king");
        Random random = new Random();

        while (uniqueCards.size() < count) {
            String rank = availableRanks.get(random.nextInt(availableRanks.size()));
            String suit = getRandomSuit();
            String card = rank + "_of_" + suit + ".png";
            if (!uniqueCards.contains(card)) {
                uniqueCards.add(card);
            }
        }
        return uniqueCards;
    }

    private String getRandomSuit() {
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        Random random = new Random();
        return suits[random.nextInt(suits.length)];
    }

    private void setCardImage(ImageView imageView, String cardName) {
        String imagePath = "/org/main/module_03_card_24/png/" + cardName;
        try (InputStream inputStream = getClass().getResourceAsStream(imagePath)) {
            if (inputStream == null) {
                System.err.println("Cannot find image: " + imagePath);
                return;
            }
            Image image = new Image(inputStream);
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
        }
    }

    @FXML
    void showHint(ActionEvent event) {
        List<Integer> cardValues = getCardValues();
        String prompt = "Given these numbers: " + cardValues +
                ", provide a hint on how to form 24 using +, -, *, /, and parentheses.";

        String hint = OpenAIHelper.chatGPT(prompt);
        showAlert("AI Hint", hint);

    }



    @FXML
    void verifyExpression(ActionEvent event) {
        String expression = answerTxtF.getText().trim();
        if (expression.isEmpty()) {
            showAlert("Error", "Please enter an expression.");
            return;
        }
        try {
            expression = validateExpression(expression);
            List<Integer> exprValues = extractValues(expression);
            List<Integer> cardValues = getCardValues();
            if (!multisetEquals(exprValues, cardValues)) {
                showAlert("Error", "Expression must use all four card values exactly once.");
                return;
            }

            double result = evaluateExpression(expression);
            if (Math.abs(result - 24) < 1e-6) {
                showAlert("Success", "Correct! The expression evaluates to 24.");
            } else {
                showAlert("Error", "Incorrect. The expression evaluates to " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Invalid expression. Please try again.");
        }
    }

    private String validateExpression(String expression) {
        String sanitized = expression.replaceAll("[^0-9+\\-*/().]", "");
        int balance = 0;
        for (char c : sanitized.toCharArray()) {
            if (c == '(') balance++;
            else if (c == ')') balance--;
            if (balance < 0)
                throw new IllegalArgumentException("Unbalanced parentheses.");
        }
        if (balance != 0)
            throw new IllegalArgumentException("Unbalanced parentheses.");
        return sanitized;
    }

    private List<Integer> extractValues(String expression) {
        List<Integer> values = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            values.add(Integer.parseInt(matcher.group()));
        }
        return values;
    }

    private double evaluateExpression(String expression) {
        try {
            Expression exp = new ExpressionBuilder(expression).build();
            return exp.evaluate();
        } catch (IllegalArgumentException | ArithmeticException e) {
            System.err.println("Error evaluating expression: " + expression);
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid expression.");
        }
    }

    private List<Integer> getCardValues() {
        List<Integer> values = new ArrayList<>();
        for (String card : selectedCards) {
            String rank = card.split("_")[0];
            int value = switch (rank) {
                case "ace" -> 1;
                case "jack" -> 11;
                case "queen" -> 12;
                case "king" -> 13;
                default -> Integer.parseInt(rank);
            };
            values.add(value);
        }
        Collections.sort(values);
        return values;
    }

    private boolean multisetEquals(List<Integer> list1, List<Integer> list2) {
        if (list1.size() != list2.size()) return false;
        List<Integer> copy1 = new ArrayList<>(list1);
        List<Integer> copy2 = new ArrayList<>(list2);
        Collections.sort(copy1);
        Collections.sort(copy2);
        return copy1.equals(copy2);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

