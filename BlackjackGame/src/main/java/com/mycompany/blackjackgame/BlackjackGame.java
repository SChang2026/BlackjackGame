/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.blackjackgame;

/**
 *
 * @author SChang2026
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class BlackjackGame extends JFrame {
    private final JLabel messageLabel;
    private final JButton hitButton;
    private final JButton standButton;
    private final JButton newGameButton;
    private final JTextArea dealerArea;
    private final JTextArea playerArea;
    private final JLabel balanceLabel;
    private final JTextField betField;

    private final Deck deck;
    private final Hand dealerHand;
    private final Hand playerHand;

    private boolean dealerCardHidden;
    private int balance;
    private int currentBet;

public BlackjackGame() {
    setTitle("Blackjack Game");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(1920, 1080);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setUndecorated(true);
    setVisible(true);
    setLayout(new BorderLayout());

    getContentPane().setBackground(new Color(0, 128, 34));

    balance = 500000000;

    // Message label
    messageLabel = new JLabel("Welcome to Blackjack!", SwingConstants.CENTER);
    messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
    messageLabel.setForeground(Color.WHITE);
    add(messageLabel, BorderLayout.NORTH);
    
    dealerArea = new JTextArea();
    dealerArea.setEditable(false);

    // Create and customize the border for "Dealer Hand"
    var dealerBorder = BorderFactory.createTitledBorder("Dealer Hand");
    dealerBorder.setTitleColor(Color.WHITE); // Set title color to white
    dealerArea.setBorder(dealerBorder);
    dealerArea.setFont(new Font("Serif", Font.PLAIN, 45));
    dealerArea.setBackground(new Color(0, 128, 34));
    dealerArea.setForeground(Color.WHITE);

    playerArea = new JTextArea();
    playerArea.setEditable(false);

    // Create and customize the border for "Your Hand"
    var playerBorder = BorderFactory.createTitledBorder("Your Hand");
    playerBorder.setTitleColor(Color.WHITE); // Set title color to white
    playerArea.setBorder(playerBorder);
    playerArea.setFont(new Font("Serif", Font.PLAIN, 45));
    playerArea.setBackground(new Color(0, 128, 34));
    playerArea.setForeground(Color.WHITE);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(new JScrollPane(dealerArea));
        centerPanel.add(new JScrollPane(playerArea));
        centerPanel.setOpaque(false);
        add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        hitButton = new JButton("Hit");
        hitButton.setBackground(Color.YELLOW);
        hitButton.setOpaque(true);
        hitButton.setBorderPainted(false);

        standButton = new JButton("Stand");
        standButton.setBackground(Color.YELLOW);
        standButton.setOpaque(true);
        standButton.setBorderPainted(false);

        newGameButton = new JButton("New Game");
        newGameButton.setBackground(Color.YELLOW);
        newGameButton.setOpaque(true);
        newGameButton.setBorderPainted(false);

        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(newGameButton);
        buttonPanel.setOpaque(false);
        add(buttonPanel, BorderLayout.SOUTH);

        // Top panel with balance and betting
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        balanceLabel = new JLabel("Balance: $" + balance, SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        balanceLabel.setForeground(Color.WHITE);

        JPanel betPanel = new JPanel();
        JLabel betLabel = new JLabel("Enter Bet: ");
        betLabel.setForeground(Color.WHITE);
        betField = new JTextField(10);

        betPanel.add(betLabel);
        betPanel.add(betField);
        betPanel.setOpaque(false);
        topPanel.add(balanceLabel);
        topPanel.add(betPanel);
        topPanel.setOpaque(false);
        add(topPanel, BorderLayout.NORTH);

       // Instructions Box
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        JTextArea instructionsArea = new JTextArea();
        instructionsArea.setEditable(false);
        instructionsArea.setText("Blackjack Rules:\n" +
                "1. The goal is to beat the dealer's hand without going over 21.\n" +
                "2. Face cards are worth 10.\n" +
                "3. Aces are worth 1 or 11, whichever makes a better hand.\n" +
                "4. Each player starts with two cards; one of the dealer's cards is hidden.\n" +
                "5. Players can 'Hit' to take another card or 'Stand' to hold their total.\n" +
                "6. If you go over 21, you bust and lose the bet.\n" +
                "\n" +
                "Mouse Rules:\n" +
                "Use it to hit, stand, and press new game.");
        instructionsArea.setFont(new Font("Serif", Font.PLAIN, 16));
        instructionsArea.setBackground(new Color(0, 128, 34));
        instructionsArea.setForeground(Color.WHITE);

        JButton closeInstructionsButton = new JButton("Close Rules");
        closeInstructionsButton.setBackground(Color.RED);
        closeInstructionsButton.setForeground(Color.WHITE);
        closeInstructionsButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeInstructionsButton.addActionListener(e -> instructionsPanel.setVisible(false));

        instructionsPanel.add(new JScrollPane(instructionsArea), BorderLayout.CENTER);
        instructionsPanel.add(closeInstructionsButton, BorderLayout.SOUTH);
        instructionsPanel.setOpaque(false);
        add(instructionsPanel, BorderLayout.WEST);


        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.addActionListener(e -> System.exit(0));

        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitPanel.setOpaque(false);
        exitPanel.add(exitButton);
        add(exitPanel, BorderLayout.EAST);

        // Action Listeners
        hitButton.addActionListener(new HitAction());
        standButton.addActionListener(new StandAction());
        newGameButton.addActionListener(new NewGameAction());

        deck = new Deck();
        dealerHand = new Hand();
        playerHand = new Hand();

        startNewGame();
    }

   



    private void startNewGame() {
        try {
            currentBet = Integer.parseInt(betField.getText());
            if (currentBet <= 0 || currentBet > balance) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid bet! Please enter a valid amount.");
            return;
        }

        balance -= currentBet;
        updateBalance();

        deck.shuffle();
        dealerHand.clear();
        playerHand.clear();
        dealerCardHidden = true;

        dealerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        playerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());

        updateDisplay();

        messageLabel.setText("Your move: Hit or Stand?");
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
    }

    private void updateDisplay() {
        if (dealerCardHidden) {
            dealerArea.setText("[Hidden], " + dealerHand.getVisibleCards() + " (Value: Hidden)");
        } else {
            dealerArea.setText(dealerHand.toString());
        }
        playerArea.setText(playerHand.toString());
    }

    private void checkGameStatus() {
        if (playerHand.getValue() > 21) {
            messageLabel.setText("You busted! Dealer wins.");
            endGame();
        } else if (dealerHand.getValue() > 21) {
            messageLabel.setText("Dealer busted! You win.");
            balance += currentBet * 2;
            updateBalance();
            endGame();
        } else if (playerHand.getValue() == 21) {
            messageLabel.setText("Blackjack! You win!");
            balance += currentBet * 2;
            updateBalance();
            endGame();
        }
    }

    private void dealerPlay() {
        dealerCardHidden = false;
        updateDisplay();
        while (dealerHand.getValue() < 17) {
            dealerHand.addCard(deck.drawCard());
        }
        updateDisplay();

        if (dealerHand.getValue() > 21 || playerHand.getValue() > dealerHand.getValue()) {
            messageLabel.setText("You win!");
            balance += currentBet * 2;
        } else if (dealerHand.getValue() == playerHand.getValue()) {
            messageLabel.setText("It's a tie! Your bet is returned.");
            balance += currentBet;
        } else {
            messageLabel.setText("Dealer wins!");
        }
        updateBalance();
        endGame();
    }

    private void endGame() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
    }

    private void updateBalance() {
        balanceLabel.setText("Balance: $" + balance);
    }

    private class HitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            playerHand.addCard(deck.drawCard());
            updateDisplay();
            checkGameStatus();
        }
    }

    private class StandAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            dealerPlay();
        }
    }

    private class NewGameAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (balance <= 0) {
                messageLabel.setText("Game over! You're out of money.");
            } else {
                startNewGame();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BlackjackGame game = new BlackjackGame();
            game.setVisible(true);
        });
    }
}

class Deck {
    private final ArrayList<String> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(rank + " of " + suit);
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public String drawCard() {
        return cards.remove(cards.size() - 1);
    }
}

class Hand {
    private final ArrayList<String> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(String card) {
        cards.add(card);
    }

    public void clear() {
        cards.clear();
    }

    public int getValue() {
        int value = 0;
        int aces = 0;
        for (String card : cards) {
            String rank = card.split(" ")[0];
            switch (rank) {
                case "2": case "3": case "4": case "5": case "6":
                case "7": case "8": case "9": case "10":
                    value += Integer.parseInt(rank);
                    break;
                case "Jack": case "Queen": case "King":
                    value += 10;
                    break;
                case "Ace":

                    aces++;
                    value += 11;
                    break;
            }
        }
        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }
        return value;
    }

    public String getVisibleCards() {
        if (cards.size() > 1) {
            return cards.get(1);
        }
        return "";
    }

    @Override
    public String toString() {
        return String.join(", ", cards) + " (Value: " + getValue() + ")";
    }
}
