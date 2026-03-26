import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizApp extends JFrame {
    
    // UI Components
    private JLabel headerLabel, timeLabel, questionNumberLabel;
    private JTextArea questionTextArea;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionsGroup;
    private JButton nextButton, prevButton, submitTestButton;
    private JPanel rightGridPanel;
    private JButton[] gridButtons;
    private Timer timer;

    // State Variables
    private int currentSectionIndex = 0; 
    private int currentQuestionIndex = 0;
    private int totalTimeLeft = 5400; // 90 minutes (5400 seconds) for the entire CET
    
    // Test Configurations (MAH MCA CET Pattern)
    private final String[] sectionNames = {"Mathematics", "Logical Reasoning", "Computer Awareness", "General English"};
    private final int[] positiveMarks = {2, 2, 2, 2}; // CET: +2 for correct
    private final int[] negativeMarks = {0, 0, 0, 0}; // CET: 0 negative marking
    
    // Data Storage
    private List<List<QuestionBank.Question>> allQuestions;
    private int[][] userAnswers; // Stores the index of selected options. -1 means unattempted.
    private boolean[][] visited; // Tracks if a question has been seen

    public QuizApp() {
        setTitle("MAH MCA CET Mock Simulator");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Initialize Data
        initializeData();

        // 2. Setup Top Panel (Headers & Timer)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(41, 128, 185));
        
        headerLabel = new JLabel("Section: ", JLabel.LEFT);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        timeLabel = new JLabel("Total Time: 90:00", JLabel.RIGHT);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        topPanel.add(headerLabel, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 3. Setup Center Panel (Question & Options)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        questionNumberLabel = new JLabel("Question 1:");
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        questionTextArea = new JTextArea(4, 50);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        questionTextArea.setBackground(getBackground()); // Match window color

        JPanel questionTextPanel = new JPanel(new BorderLayout(0, 10));
        questionTextPanel.add(questionNumberLabel, BorderLayout.NORTH);
        questionTextPanel.add(new JScrollPane(questionTextArea), BorderLayout.CENTER);
        centerPanel.add(questionTextPanel, BorderLayout.NORTH);

        // Options Panel
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        optionButtons = new JRadioButton[4];
        optionsGroup = new ButtonGroup();
        
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton("Option " + (i + 1));
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            
            // Save answer immediately when a radio button is clicked
            final int optionIndex = i;
            optionButtons[i].addActionListener(e -> {
                userAnswers[currentSectionIndex][currentQuestionIndex] = optionIndex;
                updateGridColors(); // Turn button green immediately
            });
            
            optionsGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        centerPanel.add(optionsPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // 4. Setup Right Panel (Question Palette)
        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setBorder(BorderFactory.createTitledBorder("Question Palette"));
        rightWrapper.setPreferredSize(new Dimension(280, 0));
        
        rightGridPanel = new JPanel();
        JPanel gridContainer = new JPanel(new BorderLayout());
        gridContainer.add(rightGridPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(gridContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        scrollPane.setBorder(null);
        rightWrapper.add(scrollPane, BorderLayout.CENTER);
        add(rightWrapper, BorderLayout.EAST);

        // 5. Setup Bottom Panel (Navigation & Sections)
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        
        // Row 1: Question Navigation
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        prevButton = new JButton("<< Previous");
        JButton clearButton = new JButton("Clear Response");
        nextButton = new JButton("Next >>");
        navPanel.add(prevButton);
        navPanel.add(clearButton);
        navPanel.add(nextButton);
        
        // Row 2: Section Tabs & Submit
        JPanel sectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        for (int i = 0; i < 4; i++) {
            final int secIdx = i;
            JButton secBtn = new JButton(sectionNames[i]);
            secBtn.addActionListener(e -> loadSection(secIdx));
            sectionPanel.add(secBtn);
        }
        submitTestButton = new JButton("Submit Full Test");
        submitTestButton.setBackground(new Color(231, 76, 60));
        submitTestButton.setForeground(Color.WHITE);
        sectionPanel.add(submitTestButton);
        
        bottomPanel.add(navPanel);
        bottomPanel.add(sectionPanel);
        add(bottomPanel, BorderLayout.SOUTH);

        // 6. Button Action Listeners
        prevButton.addActionListener(e -> navigateQuestion(-1));
        nextButton.addActionListener(e -> navigateQuestion(1));
        clearButton.addActionListener(e -> {
            userAnswers[currentSectionIndex][currentQuestionIndex] = -1;
            optionsGroup.clearSelection();
            updateGridColors();
        });
        submitTestButton.addActionListener(e -> submitTest());

        // 7. Start the App!
        startGlobalTimer();
        loadSection(0);
    }

    private void initializeData() {
        allQuestions = new ArrayList<>();
        allQuestions.add(QuestionBank.getMathsQuestions());
        allQuestions.add(QuestionBank.getLrQuestions());
        allQuestions.add(QuestionBank.getComputerQuestions());
        allQuestions.add(QuestionBank.getEnglishQuestions());

        userAnswers = new int[4][];
        visited = new boolean[4][];
        for (int i = 0; i < 4; i++) {
            userAnswers[i] = new int[allQuestions.get(i).size()];
            visited[i] = new boolean[allQuestions.get(i).size()];
            Arrays.fill(userAnswers[i], -1); // Unattempted
            Arrays.fill(visited[i], false);  // Unvisited
        }
    }
    
    private void startGlobalTimer() {
        if (timer != null) timer.stop();
        timer = new Timer(1000, e -> {
            totalTimeLeft--;
            updateTimerLabel();
            if (totalTimeLeft <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Time is up! Auto-submitting the test.");
                calculateAndShowResults();
            }
        });
        timer.start();
    }

    private void loadSection(int sectionIndex) {
        currentSectionIndex = sectionIndex;
        currentQuestionIndex = 0;
        
        // Update Headers
        headerLabel.setText(sectionNames[sectionIndex] + " (+" + positiveMarks[sectionIndex] + " / -" + negativeMarks[sectionIndex] + ")");
        
        // Rebuild Right-Side Question Palette
        rightGridPanel.removeAll();
        int qCount = allQuestions.get(currentSectionIndex).size();
        
        rightGridPanel.setLayout(new GridLayout(0, 5, 5, 5)); 
        gridButtons = new JButton[qCount];
        
        for (int i = 0; i < qCount; i++) {
            JButton btn = new JButton(String.valueOf(i + 1));
            btn.setPreferredSize(new Dimension(42, 42)); 
            btn.setMargin(new Insets(1, 1, 1, 1));
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
            btn.setFocusPainted(false);
            
            final int qIndex = i;
            btn.addActionListener(e -> {
                currentQuestionIndex = qIndex;
                displayQuestion(currentQuestionIndex);
            });
            gridButtons[i] = btn;
            rightGridPanel.add(btn);
        }
        
        displayQuestion(0);
        rightGridPanel.revalidate();
        rightGridPanel.repaint();
    }

    private void displayQuestion(int index) {
        List<QuestionBank.Question> currentSectionQuestions = allQuestions.get(currentSectionIndex);
        QuestionBank.Question q = currentSectionQuestions.get(index);

        questionNumberLabel.setText("Question " + (index + 1) + " of " + currentSectionQuestions.size() + ":");
        questionTextArea.setText(q.text);

        optionsGroup.clearSelection(); // Clear previous UI selection
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(q.options[i]);
        }

        // If user already answered this previously, re-select their choice
        int previousAnswer = userAnswers[currentSectionIndex][index];
        if (previousAnswer != -1) {
            optionButtons[previousAnswer].setSelected(true);
        }

        // Disable Prev button if on first question, disable Next if on last
        prevButton.setEnabled(index > 0);
        nextButton.setEnabled(index < currentSectionQuestions.size() - 1);
        
        // Mark as visited and update colors
        visited[currentSectionIndex][index] = true;
        updateGridColors();
    }

    private void navigateQuestion(int direction) {
        currentQuestionIndex += direction;
        displayQuestion(currentQuestionIndex);
    }

    private void updateGridColors() {
        for (int i = 0; i < gridButtons.length; i++) {
            if (userAnswers[currentSectionIndex][i] != -1) {
                gridButtons[i].setBackground(new Color(46, 204, 113)); // Bright Green for answered
                gridButtons[i].setForeground(Color.WHITE);
            } else if (visited[currentSectionIndex][i]) {
                gridButtons[i].setBackground(new Color(189, 195, 199)); // Gray for visited
                gridButtons[i].setForeground(Color.BLACK);
            } else {
                gridButtons[i].setBackground(UIManager.getColor("Button.background")); // Default
                gridButtons[i].setForeground(Color.BLACK);
            }
            
            if (i == currentQuestionIndex) {
                gridButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
            } else {
                gridButtons[i].setBorder(UIManager.getBorder("Button.border"));
            }
        }
    }

    private void updateTimerLabel() {
        int minutes = totalTimeLeft / 60;
        int seconds = totalTimeLeft % 60;
        timeLabel.setText(String.format("Total Time: %02d:%02d", minutes, seconds));
    }

    private void submitTest() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to submit the ENTIRE test?", 
            "Confirm Submission", JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            if(timer != null) timer.stop();
            calculateAndShowResults();
        }
    }

    // ==========================================
    // Detailed Results & Evaluation UI
    // ==========================================
    private void calculateAndShowResults() {
        int grandTotal = 0;

        // --- Data Builders for the Downloadable Print Report ---
        StringBuilder reportSummaryBuilder = new StringBuilder();
        StringBuilder reportCorrectBuilder = new StringBuilder("\n==========================================\n");
        reportCorrectBuilder.append("=== ALL CORRECT QUESTIONS (SECTION-WISE) ===\n");
        reportCorrectBuilder.append("==========================================\n\n");
        
        StringBuilder reportWrongBuilder = new StringBuilder("\n==========================================\n");
        reportWrongBuilder.append("=== ALL WRONG QUESTIONS (SECTION-WISE) ===\n");
        reportWrongBuilder.append("==========================================\n\n");

        // Container Panels for Tabs (UI)
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel correctPanel = new JPanel(new GridBagLayout());
        JPanel wrongPanel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);

        for (int i = 0; i < 4; i++) {
            int sectionScore = 0;
            int correctCount = 0;
            int wrongCount = 0;

            List<QuestionBank.Question> secQs = allQuestions.get(i);
            
            // --- Report building for current section headers ---
            reportCorrectBuilder.append("--- SECTION: ").append(sectionNames[i]).append(" ---\n");
            reportWrongBuilder.append("--- SECTION: ").append(sectionNames[i]).append(" ---\n");
            
            // Generate Section Headers for Tabs (UI)
            JLabel correctSecHeader = new JLabel("=== " + sectionNames[i] + " ===");
            correctSecHeader.setFont(new Font("Arial", Font.BOLD, 18));
            correctSecHeader.setForeground(new Color(41, 128, 185));
            correctPanel.add(correctSecHeader, gbc);

            JLabel wrongSecHeader = new JLabel("=== " + sectionNames[i] + " ===");
            wrongSecHeader.setFont(new Font("Arial", Font.BOLD, 18));
            wrongSecHeader.setForeground(new Color(231, 76, 60));
            wrongPanel.add(wrongSecHeader, gbc);

            boolean hasCorrect = false;
            boolean hasWrong = false;

            for (int qIndex = 0; qIndex < secQs.size(); qIndex++) {
                int userAns = userAnswers[i][qIndex];
                QuestionBank.Question q = secQs.get(qIndex);
                
                if (userAns == -1) {
                    continue; // Skip unattempted in detailed tabs (could optionally add an Unattempted tab)
                } 
                
                if (userAns == q.correctOption) {
                    correctCount++;
                    sectionScore += positiveMarks[i];
                    hasCorrect = true;
                    
                    // Add Card to Correct Panel (UI)
                    JPanel qCard = createResultCard("Q" + (qIndex + 1), q.text, q.options[userAns], null, new Color(230, 255, 230));
                    correctPanel.add(qCard, gbc);
                    
                    // Add to print report string
                    reportCorrectBuilder.append("Q").append(qIndex + 1).append(": ").append(q.text).append("\n");
                    reportCorrectBuilder.append("Your Answer: ").append(q.options[userAns]).append("\n\n");
                    
                } else {
                    wrongCount++;
                    sectionScore -= negativeMarks[i]; // Deduct negative marks
                    hasWrong = true;
                    
                    // Add Card to Wrong Panel (UI)
                    JPanel qCard = createResultCard("Q" + (qIndex + 1), q.text, q.options[userAns], q.options[q.correctOption], new Color(255, 235, 235));
                    wrongPanel.add(qCard, gbc);
                    
                    // Add to print report string
                    reportWrongBuilder.append("Q").append(qIndex + 1).append(": ").append(q.text).append("\n");
                    reportWrongBuilder.append("Your Answer: ").append(q.options[userAns]).append("\n");
                    reportWrongBuilder.append("Correct Answer: ").append(q.options[q.correctOption]).append("\n\n");
                }
            }
            
            // Handle missing values for report
            if (!hasCorrect) reportCorrectBuilder.append("No correct answers in this section.\n\n");
            if (!hasWrong) reportWrongBuilder.append("No wrong answers in this section.\n\n");
            
            // Handle missing values for UI
            if (!hasCorrect) correctPanel.add(new JLabel("No correct answers in this section."), gbc);
            if (!hasWrong) wrongPanel.add(new JLabel("No wrong answers in this section."), gbc);

            grandTotal += sectionScore;
            
            // Build Summary Panel Data
            String secSummaryStr = String.format("• %s -> Attempted: %d | Correct: %d | Wrong: %d | Score: %d", 
                sectionNames[i], (correctCount + wrongCount), correctCount, wrongCount, sectionScore);
            
            reportSummaryBuilder.append(secSummaryStr).append("\n"); // Append to report
            
            JLabel secSummary = new JLabel(secSummaryStr);
            secSummary.setFont(new Font("Arial", Font.PLAIN, 18));
            summaryPanel.add(secSummary);
            summaryPanel.add(Box.createVerticalStrut(15));
        }

        // Finalize Print Report string format
        StringBuilder finalReportFileContent = new StringBuilder();
        finalReportFileContent.append("=== MAH MCA CET MOCK EXAM REPORT ===\n\n");
        finalReportFileContent.append("==========================================\n");
        finalReportFileContent.append("GRAND TOTAL SCORE: ").append(grandTotal).append("\n");
        finalReportFileContent.append("==========================================\n\n");
        finalReportFileContent.append("=== SECTION-WISE SUMMARY ===\n");
        finalReportFileContent.append(reportSummaryBuilder.toString());
        finalReportFileContent.append(reportCorrectBuilder.toString());
        finalReportFileContent.append(reportWrongBuilder.toString());

        // Push layout up
        gbc.weighty = 1.0;
        correctPanel.add(Box.createGlue(), gbc);
        wrongPanel.add(Box.createGlue(), gbc);

        summaryPanel.add(new JLabel("==========================================="));
        JLabel finalScoreLabel = new JLabel("GRAND TOTAL SCORE: " + grandTotal);
        finalScoreLabel.setFont(new Font("Arial", Font.BOLD, 22));
        finalScoreLabel.setForeground(new Color(46, 204, 113));
        summaryPanel.add(Box.createVerticalStrut(20));
        summaryPanel.add(finalScoreLabel);

        // Package everything in a JTabbedPane
        JTabbedPane resultTabs = new JTabbedPane();
        resultTabs.setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollSummary = new JScrollPane(summaryPanel);
        JScrollPane scrollCorrect = new JScrollPane(correctPanel);
        scrollCorrect.getVerticalScrollBar().setUnitIncrement(16);
        JScrollPane scrollWrong = new JScrollPane(wrongPanel);
        scrollWrong.getVerticalScrollBar().setUnitIncrement(16);

        resultTabs.addTab("Summary & Scorecard", scrollSummary);
        resultTabs.addTab("Correct Answers (Section-wise)", scrollCorrect);
        resultTabs.addTab("Wrong Answers (Section-wise)", scrollWrong);

        // Display Dialog overlaying main frame
        JDialog resultDialog = new JDialog(this, "Detailed Test Results", true);
        resultDialog.setSize(850, 600);
        resultDialog.setLocationRelativeTo(this);
        resultDialog.setLayout(new BorderLayout());
        
        resultDialog.add(resultTabs, BorderLayout.CENTER);
        
        // --- ADD PRINT / SAVE AS PDF BUTTON ---
        JPanel dialogBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dialogBottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton printBtn = new JButton("Print / Save as PDF");
        printBtn.setFont(new Font("Arial", Font.BOLD, 14));
        printBtn.setBackground(new Color(39, 174, 96));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFocusPainted(false);
        
        printBtn.addActionListener(e -> {
            // Create an invisible text area just to hold the formatted text for printing
            JTextArea printArea = new JTextArea(finalReportFileContent.toString());
            printArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            printArea.setWrapStyleWord(true);
            printArea.setLineWrap(true);
            
            try {
                // This utilizes Java's built in printing capability. 
                // The user can choose "Save as PDF" or "Microsoft Print to PDF" from the dialog.
                boolean complete = printArea.print(
                    new MessageFormat("MAH MCA CET Exam Report"),
                    new MessageFormat("- Page {0} -")
                );
                
                if (complete) {
                    JOptionPane.showMessageDialog(resultDialog, "Report printed/saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (PrinterException pe) {
                JOptionPane.showMessageDialog(resultDialog, "Printing failed: " + pe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialogBottomPanel.add(printBtn);
        resultDialog.add(dialogBottomPanel, BorderLayout.SOUTH);
        // ---------------------------
        
        resultDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        
        resultDialog.setVisible(true);
        System.exit(0);
    }

    // Helper method to format Questions neatly in UI blocks
    private JPanel createResultCard(String qNum, String questionText, String userAnswer, String correctAnswer, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(bgColor);

        JLabel qLabel = new JLabel(qNum + ": ");
        qLabel.setFont(new Font("Arial", Font.BOLD, 14));
        qLabel.setVerticalAlignment(SwingConstants.TOP);
        
        JTextArea qTextArea = new JTextArea(questionText);
        qTextArea.setEditable(false);
        qTextArea.setLineWrap(true);
        qTextArea.setWrapStyleWord(true);
        qTextArea.setBackground(bgColor);
        qTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(bgColor);
        textPanel.add(qLabel, BorderLayout.WEST);
        textPanel.add(qTextArea, BorderLayout.CENTER);

        JPanel ansPanel = new JPanel();
        ansPanel.setLayout(new BoxLayout(ansPanel, BoxLayout.Y_AXIS));
        ansPanel.setBackground(bgColor);
        ansPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel uAnsLabel = new JLabel("Your Answer: " + userAnswer);
        uAnsLabel.setFont(new Font("Arial", Font.BOLD, 13));
        uAnsLabel.setForeground(Color.DARK_GRAY);
        ansPanel.add(uAnsLabel);
        
        if (correctAnswer != null) {
            JLabel cAnsLabel = new JLabel("Correct Answer: " + correctAnswer);
            cAnsLabel.setFont(new Font("Arial", Font.BOLD, 13));
            cAnsLabel.setForeground(new Color(39, 174, 96)); // Dark Green
            ansPanel.add(Box.createVerticalStrut(5));
            ansPanel.add(cAnsLabel);
        }

        card.add(textPanel, BorderLayout.CENTER);
        card.add(ansPanel, BorderLayout.SOUTH);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizApp().setVisible(true);
        });
    }
}