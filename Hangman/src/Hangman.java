import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public final class Hangman extends JFrame implements ActionListener 
{

    private int condition = 0;
    private static String lettersGuessed = "";
    private static String[] words;
    private static char[] guesses;
    private static char[] randomWord;
    private static final String FILE_START = "Play";
    private static final String FILE_STOP = "Exit";
    private static final int APP_HEIGHT = 600;
    private static final int APP_WIDTH = 600;
    private static final int MAX_TRIES = 6;
    public Random rGen = new Random();
    public static JPanel mainPanel, leftPanel, rightPanel, bottomPanel, belowPanel;
    public static String word;
    public static final String FILE = "input/words.txt";
    public static final String SHOW_REPLAY = "Play Again?";
    public static int numBodyParts = 0;
    

    public Hangman() 
    {
        super("Hangman");
        setSize(APP_WIDTH, APP_HEIGHT);
        words = textFile();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 0));
        mainPanel.setBackground(Color.WHITE);
        rightPanel = new JPanel();
        leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        rightPanel.setBackground(Color.WHITE);
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(4, 4));
        bottomPanel.setBackground(Color.GRAY);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        belowPanel = new JPanel();
        belowPanel.setBackground(Color.GREEN);
        add(mainPanel);
        add(belowPanel, BorderLayout.AFTER_LAST_LINE);
        belowPanel.setVisible(false);

        createMenuBar();
        createButtons(bottomPanel); 
        replayButtons(belowPanel);
    }

    
    public void replayButtons(JPanel belowPanel) 
    {
        JButton playAgain;
        playAgain = new JButton(SHOW_REPLAY);
        playAgain.setSize(80, 80);
        playAgain.setActionCommand(SHOW_REPLAY);
        playAgain.addActionListener(this);
        JButton exit;
        exit = new JButton(FILE_STOP);
        exit.setActionCommand(FILE_STOP);
        exit.addActionListener(this);
        exit.setSize(80, 80);
        belowPanel.add(playAgain);
        belowPanel.add(exit);
    }

    public void createButtons(JPanel bottomPanel) 
    {

        JButton[] buttons = new JButton[26];
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

        for (int i = 0; i < buttons.length; i++) 
        {
            buttons[i] = new JButton(letters[i]);
            buttons[i].setSize(40, 40);
            buttons[i].setActionCommand(letters[i]);
            buttons[i].addActionListener(this);
            bottomPanel.add(buttons[i]);
        }
    }

   
    public void createMenuBar() 
    {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
   
        JMenu fileMenu;
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        createMenuItem(fileMenu, FILE_START);
        createMenuItem(fileMenu, FILE_STOP);
    }

   
    public void createMenuItem(JMenu menu, String itemName) 
    {
        JMenuItem menuItem = new JMenuItem(itemName);
        menuItem.addActionListener(this);
        menu.add(menuItem);
    }

    @Override
    public void paint(Graphics g) 
    {
        super.paint(g);
        Font font = new Font("Arial", Font.BOLD | Font.ITALIC, 24);
        g.setFont(font);
        g.setColor(Color.BLUE);
   
        if (condition == 1) 
        {
            gameMessages(g);
            String result = "";
            for (int i = 0; i < guesses.length; i++) 
            {
                result += guesses[i] + " ";
            }
            g.drawString(result, 250, 175);
            g.drawString("Letters tried", 250, 250);
            g.drawString(lettersGuessed, 250, 325);
            g.drawString("You have " + (MAX_TRIES - numBodyParts) + " tries left", 250, 375);
            System.out.println(randomWord);
            hangman(g);
        }
    }

    public void hangman(Graphics g) 
    {
        if (numBodyParts >= 1) 
        {
            g.setColor(Color.ORANGE);
            g.fillOval(35, 120, 70, 60);
            g.setColor(Color.BLUE);
            g.fillOval(55, 140, 10, 10);
            g.fillOval(75, 140, 10, 10);
            g.setColor(Color.BLACK);
            g.drawArc(50, 155, 40, 10, -10, -180);
            
            if (numBodyParts >= 2) 
            {
                g.setColor(Color.ORANGE);
                g.fillRect(60, 180, 20, 80);
            }

            if (numBodyParts >= 3) 
            {
                g.setColor(Color.ORANGE);
                g.fillRect(25, 200, 45, 15);
                g.setColor(Color.YELLOW);
                g.fillRect(15, 200, 10, 15);
            }

            if (numBodyParts >= 4) 
            {
                g.setColor(Color.ORANGE);
                g.fillRect(80, 200, 45, 15);
                g.setColor(Color.YELLOW);
                g.fillRect(120, 200, 10, 15);
            }

            if (numBodyParts >= 5) 
            {
                g.setColor(Color.ORANGE);
                g.fillRect(35, 260, 30, 15);
            }

            if (numBodyParts >= 6) 
            {
                g.setColor(Color.ORANGE);
                g.fillRect(70, 260, 30, 15);
            }
        }
    }

    public void gameMessages(Graphics g) 
    {
        int x = 25;
        int y = 80;
        
        if (!conqueror()&& numBodyParts==0) 
        {
           g.drawString("Let's Play Hangman!", x,y);
        } 
        
        if (conqueror() && numBodyParts < MAX_TRIES) 
        {
            g.drawString("You win! :-)", x,y);
            displayMessageTime();   
        } 
        
        if (numBodyParts == MAX_TRIES) 
        { 
            g.drawString("You lose! :-( ", x,y);
            displayMessageTime();
        }

    }

    public String getWord() 
    {
        words = textFile();
        int n = words.length;
        int r = rGen.nextInt(n);
        String guessWord;
        guessWord = words[r];
        return guessWord;
    }

    
    public boolean conqueror() 
    {
        return Arrays.equals(guesses, randomWord);
    }

    public final String[] textFile() 
    {
    
        BufferedReader reader;
        List<String> wordList = new ArrayList<>();
        try 
        {
            reader = new BufferedReader(new FileReader(FILE));
            String s;

            while ((s = reader.readLine()) != null) 
            {
                wordList.add(s);
                
            }
            reader.close();
        } 
        catch (IOException e) 
        {
            System.out.println(e.getMessage());
            System.exit(-1);
        } 
        
        return wordList.toArray(new String[wordList.size()]);

    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {

        String command = e.getActionCommand();

        if (command.equals(FILE_START)) 
        {
	    condition = 1;
            play();
            repaint();

        } 
        else if (command.length() == 1 && condition == 1) 
        {
	    letters(command);
        }
        
        else if (command.equals(SHOW_REPLAY)) 
        {
            numBodyParts = 0;
            lettersGuessed = "";
            bottomPanel.setVisible(true);
            belowPanel.setVisible(false);
            condition = 1;
            play();
            repaint();

        } 
        else if (command.equals(FILE_STOP)) 
        {
            condition = 2;
            System.exit(0);
        }
    }

    public void letters(String command) 
    {
        System.out.println(command);
        if (word.contains(command.toLowerCase())) 
        {
            for (int i = 0; i < randomWord.length; i++) 
            {
                if (command.toLowerCase().charAt(0) == randomWord[i]) 
                {
                    guesses[i] = command.toLowerCase().charAt(0);
                }
            }
        } 
        
        else if (!word.contains(command.toLowerCase())) 
        {
            numBodyParts++;
        }

        
        lettersGuessed += command;
        
        if (numBodyParts < MAX_TRIES && !conqueror()) 
        {
            lettersGuessed += ",";
        }
        repaint();
    }

    
    public void play() 
    {
        word = getWord(); 
        randomWord = word.toCharArray();
        guesses = new char[randomWord.length];
        for (int i = 0; i < guesses.length; i++) 
        {
            guesses[i] = '_';
        }

    }
    
    public static void pause(int seconds)
    {
        Date start = new Date();
        Date end = new Date();
        while(end.getTime() - start.getTime() < seconds * 1000)
        {
            end = new Date();
        }    
    }
    
    public void displayMessageTime()
    {
         pause(1);
         bottomPanel.setVisible(false);
         belowPanel.setVisible(true);
    }
    
    public static void main(String[] args) 
    {
        Hangman program = new Hangman();
        program.setVisible(true);
    }
}