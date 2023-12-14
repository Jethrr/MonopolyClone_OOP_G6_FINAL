//Final Nani Na Code sa Amoa Ilhanan
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class MonopolyGame extends JFrame {
    private JPanel mainPanel;
    private JLabel backgroundImageLabel;
    private JTextField displayText;
    private JTextField displayProp;
    private JButton btnBuy;
    private JButton btnPayRent;
    private JButton btnRoll;
    private JButton btnP1;
    private JButton btnP2;
    private JTextField displayMove;
    private JButton btnSellProperty;
    private Dice dice;
    private Jail jail;
    private FreeParking freeParking;
    private int currentPlayerIndex;  // Declare as a class field
    private Player player1;  // Declare as a class field
    private Player player2;  // Declare as a class field
    private Player currentPlayer;
    private StreetProperty mediterraneanAve;
    private StreetProperty balticAve;
    private StreetProperty orientalAve;
    private StreetProperty vermontAve;
    private StreetProperty connecticutAve;
    private StreetProperty STcharlesPlace;
    private StreetProperty statesAve;
    private StreetProperty virginiaAve;
    private StreetProperty STjamesPlace;
    private StreetProperty tennesseeAve;
    private StreetProperty newyorkAve;
    private StreetProperty kentuckyAve;
    private StreetProperty indianaAve;
    private StreetProperty illinoisAve;
    private StreetProperty atlanticAve;
    private StreetProperty ventnorAve;
    private StreetProperty marvinGardens;
    private StreetProperty pacificAve;
    private StreetProperty northcarolinaAve;
    private StreetProperty pennsylvaniaAve;
    private StreetProperty parkPlace;
    private StreetProperty boardWalk;

    private StreetProperty reading;

    private StreetProperty Pvania;

    private StreetProperty BnO;

    private StreetProperty SL;
    private boolean gameIsRunning;


    public void updateDisplayText(String text) {
        displayText.setText(text);
    }

    public static void main(String[] args) {
        MonopolyGame frame = new MonopolyGame();
        // Set up the main frame
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setContentPane(frame.mainPanel);
        frame.setSize(1280, 900);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Initialize game variables and start the game
        new Thread(() -> frame.startGame()).start();
        deleteTransactionHistoryFile();
    }

    public MonopolyGame() {
        // Load the background image
        ImageIcon backgroundImageIcon = new ImageIcon("monopoly_original.jpg");
        Image backgroundImage = backgroundImageIcon.getImage().getScaledInstance(1280, 900, Image.SCALE_DEFAULT);
        backgroundImageIcon = new ImageIcon(backgroundImage);

        currentPlayer = player1;
        gameIsRunning = true;
        // Set the preferred size of the main panel
        mainPanel.setPreferredSize(new Dimension(1280, 900));

        // Set the preferred size of the displayText field
        displayText.setPreferredSize(new Dimension(100, 150));          
        displayProp.setPreferredSize(new Dimension(100, 300));
        displayMove.setPreferredSize(new Dimension(100, 150));

        dice = new Dice();

        btnRoll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameIsRunning) {
                    btnRoll.setEnabled(false);
                    new Thread(() -> {
                        int diceRoll = dice.roll();
                        updateDisplayText("Dice rolled: " + diceRoll);
                        handleDiceRoll(diceRoll);
                        btnRoll.setEnabled(true);
                    }).start();
                }
            }
        });

        btnP1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameIsRunning) {
                    showPlayerProperties(player1);
                }
            }
        });

        btnP2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameIsRunning) {
                    showPlayerProperties(player2);
                }
            }
        });
        btnSellProperty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTransactionHistory();
            }
        });
    }

    private void showPlayerProperties(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("Properties owned by ").append(player.getName()).append(":\n");

        for (Property property : player.getOwnedProperties()) {
            message.append(property.getName()).append("\n");
        }

        JOptionPane.showMessageDialog(this, message.toString(), "Player Properties", JOptionPane.INFORMATION_MESSAGE);
    }

    private void switchToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
        currentPlayer = (currentPlayerIndex == 0) ? player1 : player2;
        displayText.setText(currentPlayer.getName() + "'s Turn");
    }

   /* private void handleBuyButtonClick(Player currentPlayer, Property currentProperty) {
        // Implement the logic for handling the buy button click
        // For example:
        // Call the onPurchase method to handle the purchase logic
        // Add your specific logic for buying the property here
        if (currentPlayer.canAfford(currentProperty.getPrice())) {

            currentProperty.setOwner(currentPlayer);
            displayProp.setText(currentPlayer.getName() + " bought " + currentProperty.getName() + " for $" + currentProperty.getPrice());
            currentPlayer.pay(currentProperty.getPrice());
            currentPlayer.addProperty(currentProperty);
            System.out.println(currentPlayer.getMoney());

        } else {
            displayProp.setText(currentPlayer.getName() + " cannot afford " + currentProperty.getName());
            // Implement additional logic, e.g., bankrupt the player
        }
    }*/

    private void handleBuyButtonClick(Player currentPlayer, Property currentProperty) {
        Player owner = currentProperty.getOwner();

        if (currentPlayer.canAfford(currentProperty.getPrice()) && owner == null) {
            // Property is unowned, allow the current player to buy it
            currentProperty.setOwner(currentPlayer);
            displayProp.setText(currentPlayer.getName() + " bought " + currentProperty.getName() + " for $" + currentProperty.getPrice());
            currentPlayer.pay(currentProperty.getPrice());
            currentPlayer.addProperty(currentProperty);
            System.out.println("After purchase - " + currentPlayer.getName() + " money: " + currentPlayer.getMoney());
            logTransaction(currentPlayer.getName() + " bought " + currentProperty.getName() + " for $" + currentProperty.getPrice());
        } else if (owner != null && owner != currentPlayer) {
            // Property is owned by another player, charge rent to the current player
            int rent = currentProperty.getRent();
            currentPlayer.pay(rent);
            owner.receiveRent(rent);
            displayProp.setText(currentPlayer.getName() + " paid $" + rent + " as rent to " + owner.getName() + " for " + currentProperty.getName());
            System.out.println("After paying rent - " + currentPlayer.getName() + " money: " + currentPlayer.getMoney());
            logTransaction(currentPlayer.getName() + " paid $" + rent + " as rent to " + owner.getName() + " for " + currentProperty.getName());
        } else {
            displayProp.setText(currentPlayer.getName() + " cannot buy " + currentProperty.getName());
            // Implement additional logic if needed
        }
    }



    private void logTransaction(String transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.txt", true))) {
            writer.write(transaction);
            writer.newLine(); // Add a new line for each transaction
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception if necessary
        }
    }

    private void displayTransactionHistory() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea transactionTextArea = new JTextArea(20, 40);
        transactionTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(transactionTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Set transaction history in the JTextArea
        transactionTextArea.setText(readTransactionHistory());

        // Show transaction history in a dialog (Replace this with your display logic)
        JOptionPane.showMessageDialog(this, panel, "Transaction History", JOptionPane.PLAIN_MESSAGE);
    }

    private String readTransactionHistory() {
        StringBuilder transactionHistory = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactionHistory.append(line).append("\n"); // Append each line to the transaction history
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception if necessary
        }
        return transactionHistory.toString();
    }

    private static void deleteTransactionHistoryFile() {
        File file = new File("transactions.txt");
        if (file.exists()) {
            file.delete(); // Delete the transactions file
        }
    }




    private void handleDiceRoll(int diceRoll) {

        displayMove.setText(currentPlayer.move(diceRoll) + " - $" +  currentPlayer.getMoney());

        if (currentPlayer.getPosition() % 40 == 1 && currentPlayer.getMoney() != 1500) {
            int rotations = currentPlayer.getPosition() / 40;
            int salary = 200 * rotations;
            displayProp.setText(currentPlayer.addToMoney(salary));
            displayText.setText(currentPlayer.getName() + " passed Go and collected $" + salary);
        }

        switch (currentPlayer.getPosition() % 40) {
            case 1:
                currentPlayer.setPosition(1);
                switchToNextPlayer();
                break;
            case 2: // Mediterranean Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, mediterraneanAve));
                    currentPlayer.setPosition(2);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !mediterraneanAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, mediterraneanAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    displayProp.setText("Exception occurred on Mediterranean Avenue: " + e.getMessage());
                }
                break;
            case 3: // Community Chest
                CommunityChestCard communityChestCard = new CommunityChestCard(); // add desc here
                currentPlayer.setPosition(3);
                displayProp.setText(communityChestCard.executeAction(currentPlayer));
                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                switchToNextPlayer();
                break;
            case 4: // Baltic Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, balticAve));
                    currentPlayer.setPosition(4);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !balticAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, balticAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Baltic Avenue: " + e.getMessage());
                }
                break;
            case 5: // Income Tax
                IncomeTax incomeTax = new IncomeTax("Income Tax", 200);
                currentPlayer.setPosition(5);
                displayProp.setText(incomeTax.collectIncomeTax(currentPlayer));
                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                switchToNextPlayer();
                break;
            case 6: // Reading Railroad

                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, reading));
                    currentPlayer.setPosition(6);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !reading.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, reading);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println("Exception occurred on Reading Railroad: " + e.getMessage());
                }
                break;
            case 7: // Oriental Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, orientalAve));
                    currentPlayer.setPosition(7);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }
                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !orientalAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, orientalAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Oriental Avenue: " + e.getMessage());
                }
                break;
            case 8: // Chance
                ChanceCard chanceCard = new ChanceCard();
                currentPlayer.setPosition(8);
                chanceCard.executeAction(currentPlayer);
                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                switchToNextPlayer();
                break;
            case 37: // Chance
                ChanceCard chanceCard1 = new ChanceCard();
                currentPlayer.setPosition(37);
                chanceCard1.executeAction(currentPlayer);
                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                switchToNextPlayer();
                break;
            case 23: // Chance
                ChanceCard chanceCard2 = new ChanceCard();
                currentPlayer.setPosition(23);
                chanceCard2.executeAction(currentPlayer);
                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                switchToNextPlayer();
                break;
            case 9: // Vermont Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, vermontAve));
                    currentPlayer.setPosition(9);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !vermontAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, vermontAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Vermont Avenue: " + e.getMessage());
                }
                break;
            case 10: // Connecticut Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, connecticutAve));
                    currentPlayer.setPosition(10);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !connecticutAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, connecticutAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Connecticut Avenue: " + e.getMessage());
                }
                break;
            case 11: // Just Visiting/In Jail
                currentPlayer.setPosition(11);
                if (!jail.isPlayerInJail(currentPlayer)) {
                    currentPlayer.pay(100); // Payment for visiting Jail
                    System.out.println("After visiting jail - " + currentPlayer.getName() + " money: " + currentPlayer.getMoney());
                    displayProp.setText(currentPlayer.getName() + " is just visiting Jail. Paid $100.");
                } else {
                    currentPlayer.pay(300); // Payment for being in Jail
                    currentPlayer.setInJail(false); // Assuming payment allows the player to get out
                    System.out.println("After visiting jail - " + currentPlayer.getName() + " money: " + currentPlayer.getMoney());
                    displayProp.setText(currentPlayer.getName() + " is in Jail. Paid $300 and got out of Jail!");
                }
                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                switchToNextPlayer();
                break;

            case 12: // St. Charles Place
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, STcharlesPlace));
                    currentPlayer.setPosition(12);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !STcharlesPlace.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, STcharlesPlace);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on St Charles Place: " + e.getMessage());
                }
                break;
            case 13: // Electric Company
                try {
                    UtilityProperty.ElectricCompany ec = new UtilityProperty.ElectricCompany();
                    displayProp.setText(Handle.handleUtilityProperty(currentPlayer, ec));
                    currentPlayer.setPosition(13);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }
                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            handleBuyButtonClick(currentPlayer, ec);
                            displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                            switchToNextPlayer();
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Electric Company: " + e.getMessage());
                }
                break;
            case 14: // States Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, statesAve));
                    currentPlayer.setPosition(14);
                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !statesAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, statesAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on States Avenue: " + e.getMessage());
                }
                break;
            case 15: // Virginia Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, virginiaAve));
                    currentPlayer.setPosition(15);
                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }
                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !virginiaAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, virginiaAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Virginia Avenue: " + e.getMessage());
                }
                break;
            case 16: // Pennsylvania Railroad

                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, Pvania));
                    currentPlayer.setPosition(16);
                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }
                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !Pvania.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, Pvania);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println("Exception occurred on Pennsylvania Railroad: " + e.getMessage());
                }
                break;

            case 17: // St. James Place
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, STjamesPlace));
                    currentPlayer.setPosition(17);
                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }
                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !STjamesPlace.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, STjamesPlace);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on St. James Place: " + e.getMessage());
                }
                break;
            case 18: // Community Chest
                CommunityChestCard communityChestCard2 = new CommunityChestCard();
                displayProp.setText(communityChestCard2.executeAction(currentPlayer));
                currentPlayer.setPosition(18);
                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                switchToNextPlayer();
                break;
            case 34: // Community Chest
                CommunityChestCard communityChestCard3 = new CommunityChestCard();
                displayProp.setText(communityChestCard3.executeAction(currentPlayer));
                currentPlayer.setPosition(34);
                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                switchToNextPlayer();
                break;
            case 19: // Tennessee Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, tennesseeAve));
                    currentPlayer.setPosition(19);
                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }
                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !tennesseeAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, tennesseeAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Tennessee Avenue: " + e.getMessage());
                }
                break;
            case 20: // New York Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, newyorkAve));
                    currentPlayer.setPosition(20);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !newyorkAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, newyorkAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on New York Avenue : " + e.getMessage());
                }
                break;
            case 21: // Free Parking
                currentPlayer.setPosition(21);
                displayProp.setText(freeParking.handleFreeParking(currentPlayer));
                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                switchToNextPlayer();
                break;
            case 22: // Kentucky Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, kentuckyAve));
                    currentPlayer.setPosition(22);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !kentuckyAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, kentuckyAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Kentucky Avenue : " + e.getMessage());
                }
                break;
            case 24: // Indiana Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, indianaAve));
                    currentPlayer.setPosition(24);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !indianaAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, indianaAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Indiana Avenue : " + e.getMessage());
                }
                break;
            case 25: // Illinois Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, illinoisAve));
                    currentPlayer.setPosition(25);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !illinoisAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, illinoisAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Illinois Avenue : " + e.getMessage());
                }
                break;
            case 26: // B. & O. Railroad

                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, BnO));
                    currentPlayer.setPosition(26);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !BnO.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, BnO);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println("Exception occurred on B. & O. Railroad: " + e.getMessage());
                }

                break;
            case 27: // Atlantic Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, atlanticAve));
                    currentPlayer.setPosition(27);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !atlanticAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, atlanticAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Atlantic Avenue : " + e.getMessage());
                }
                break;
            case 28: // Ventnor Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, ventnorAve));
                    currentPlayer.setPosition(28);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !ventnorAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, ventnorAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Ventnor Avenue : " + e.getMessage());
                }
                break;
            case 29: // Water Works
                try {
                    UtilityProperty.WaterWorks ww = new UtilityProperty.WaterWorks();
                    currentPlayer.setPosition(29);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    displayProp.setText(Handle.handleUtilityProperty(currentPlayer, ww));

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            handleBuyButtonClick(currentPlayer,ww);
                            displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                            switchToNextPlayer();
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Water Works: " + e.getMessage());
                }
                break;

            case 30: // Marvin Gardens
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, marvinGardens));
                    currentPlayer.setPosition(30);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !marvinGardens.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, marvinGardens);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Marvin Gardens: " + e.getMessage());
                }
                break;

            case 31: // Go to Jail
                displayProp.setText(jail.sendToJail(currentPlayer));
                currentPlayer.setPosition(31);
                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                switchToNextPlayer();
                break;

            case 32: // Pacific Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, pacificAve));
                    currentPlayer.setPosition(32);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !pacificAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, pacificAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Pacific Avenue : " + e.getMessage());
                }
                break;
            case 33: // North Carolina Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, northcarolinaAve));
                    currentPlayer.setPosition(33);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !northcarolinaAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, northcarolinaAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on North Carolina Avenue : " + e.getMessage());
                }
                break;
            case 35: // Pennsylvania Avenue
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, pennsylvaniaAve));
                    currentPlayer.setPosition(35);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !pennsylvaniaAve.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, pennsylvaniaAve);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    System.out.println("Exception occurred on Pennsylvania Avenue : " + e.getMessage());
                }
                break;
            case 36: // Short Line

                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, SL));
                    currentPlayer.setPosition(36);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !SL.isOwned()) {
                                handleBuyButtonClick(currentPlayer,SL);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println("Exception occurred on Short Line: " + e.getMessage());
                }
                break;
            case 38: // Park Place
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, parkPlace));
                    currentPlayer.setPosition(38);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !parkPlace.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, parkPlace);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    displayProp.setText("Exception occurred on Mediterranean Avenue: " + e.getMessage());
                }
                break;
            case 39: // Luxury Tax
                LuxuryTax luxuryTax = new LuxuryTax("Luxury Tax", 100);
                displayProp.setText(luxuryTax.collectLuxuryTax(currentPlayer));
                currentPlayer.setPosition(39);
                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                switchToNextPlayer();
                break;
            case 40: // Boardwalk //
                try {
                    displayProp.setText(Handle.handleStreetProperty(currentPlayer, boardWalk));
                    currentPlayer.setPosition(40);

                    for (ActionListener al : btnBuy.getActionListeners()) {
                        btnBuy.removeActionListener(al);
                    }

                    btnBuy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (gameIsRunning && !boardWalk.isOwnedBy(currentPlayer)) {
                                handleBuyButtonClick(currentPlayer, boardWalk);
                                displayMove.setText(currentPlayer.getName() + "'s Balance $" +  currentPlayer.getMoney());
                                switchToNextPlayer();
                            }
                        }
                    });

                } catch (Exception e) {
                    displayProp.setText("Exception occurred on Mediterranean Avenue: " + e.getMessage());
                }
                break;

            default:
                // throw new IllegalStateException("Unexpected value: " + currentPlayer.getPosition() % 40);
                System.err.println("Unexpected value: " + currentPlayer.getPosition() % 41);
                break;
        }

        if (currentPlayer.getMoney() <= 0) {
            updateDisplayText(currentPlayer.getName() + " is bankrupt. Game over!");
            deleteTransactionHistoryFile();
            gameIsRunning = false;
        }

        if (!gameIsRunning) {
            // Display a dialog to end the game
            JOptionPane.showMessageDialog(this, "Game over!");
        }
    }

    private void startGame() {
        // Create players and board spaces
        String playerName1 = JOptionPane.showInputDialog("Enter Player 1's Name:");
        String playerName2 = JOptionPane.showInputDialog("Enter Player 2's Name:");


        player1 = new Player(playerName1);
        player2 = new Player(playerName2);

        mediterraneanAve = new StreetProperty("Mediterranean Avenue", 60, 40,"Brown");
        balticAve = new StreetProperty("Baltic Avenue", 60, 40,"Brown");
        orientalAve = new StreetProperty("Oriental Avenue", 100,60, "LightBlue");
        vermontAve = new StreetProperty("Vermont Avenue", 100, 80, "LightBlue");
        connecticutAve = new StreetProperty("Connecticut Avenue", 100, 80, "LightBlue");
        STcharlesPlace = new StreetProperty("St. Charles Place", 140, 120, "Pink");
        statesAve = new StreetProperty("States Avenue", 140, 120, "Pink");
        virginiaAve = new StreetProperty("Virginia Avenue", 160,140, "Pink");
        STjamesPlace = new StreetProperty("St. James Place", 140, 120, "Orange");
        tennesseeAve = new StreetProperty("Tennessee Avenue", 180, 160, "Orange");
        newyorkAve = new StreetProperty("New York Avenue", 200, 180, "Orange");
        kentuckyAve = new StreetProperty("Kentucky Avenue", 220, 200, "Red");
        indianaAve = new StreetProperty("Indiana Avenue", 220, 200, "Red");
        illinoisAve = new StreetProperty("Illinois Avenue", 240, 220, "Red");
        atlanticAve = new StreetProperty("Atlantic Avenue", 260, 240, "Yellow");
        ventnorAve = new StreetProperty("Ventnor Avenue", 260, 240, "Yellow");
        marvinGardens = new StreetProperty("Marvin Gardens", 280, 260, "Yellow");
        pacificAve = new StreetProperty("Pacific Avenue", 300, 280, "Green");
        northcarolinaAve = new StreetProperty("North Carolina Avenue", 300, 280, "Green");
        pennsylvaniaAve = new StreetProperty("Pennsylvania Avenue", 320, 300, "Green");
        parkPlace = new StreetProperty("Park Place", 350, 330, "Blue");
        boardWalk = new StreetProperty("Board Walk", 400, 380, "Blue");

        reading = new StreetProperty("Reading Railroads", 200, 180, "NoColor");
        Pvania = new StreetProperty("Pennsylvania Railroads", 200, 180, "NoColor");
        BnO = new StreetProperty("B. & O. Railroads", 200, 180, "NoColor");
        SL = new StreetProperty("Short Line", 200, 180, "NoColor");

        jail = new Jail();
        freeParking = new FreeParking();

        // Initialize game variables


        Scanner scanner = new Scanner(System.in);

        while (gameIsRunning) {

            while (gameIsRunning) {
                currentPlayer = (currentPlayerIndex == 0) ? player1 : player2;

                // Simulate dice roll


                // Check for passing Go and collect salary


                // Handle board spaces based on player position


                // Check if a player is bankrupt


                // Switch to the next player


                // Ask for user input to continue the game
//            String gameInfo = "Press Enter to continue or type 'quit' to end the game:";
//            System.out.println(gameInfo);
//            updateDisplayText(gameInfo);

            }
            // Close the scanner

            scanner.close();

        }
    }
}