/**Cathy Zhang, Stanley Zhang
 * Mr.Anandarajan
 * ICS4U 
 * Program run a Codebreakers game with AI. It allows the user to guess the code that is randomized by AI or for the user to pick the code
 * and the AI will try their best to guess it. The program runs exactly like the codebreaker game in which each player has 10 turns to guess 
 * the secret code. Hints are given in Black and White to signify right position and colour or right colour wrong position. The program has a 
 * few extra features from file handling(saving results on file), timer, sound, and difficulty (in terms of 4,6, or 8 colours).
 */

import java.io.*; //Imports
import javax.swing.border.Border;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.sound.sampled.*;
import java.io.File;

public class Codebreaker {

//Var
static String valid_chars = "";
static final int SIZE = 4;
static final int TRIES = 10;

static JFrame frame = new JFrame();
static JPanel startPanel,gamePanel,hintPanel,enterPanel, titlePanel, nullPanel, nullPanel2;
static JLabel Title = new JLabel("CODEBREAKERS"), nullLabel, nullLabel2, nullLabel3, nullLabel4, nullLabel5, instructions, test3, endLabel;
static Border bordergrid = BorderFactory.createLineBorder(Color.black,2);
static JButton playerGuess, AIGuess,exit, submit, howToPlay, backToMain, difficultly, playAgain, mainMenuButton, hintBlack, hintWhite;
static JButton[] buttons = new JButton[4];
static JLabel[][] answers = new JLabel[10][4];
static JLabel [][] hint = new JLabel[10][4];
static JLabel[] nullarrayLabel = new JLabel[4];
static int count1=-1,count2=-1,count3=-1,count4=-1, numofColours = 4, colourCount = 1, lives= 0, rowCount = 0, bCount=0, wCount=0, gameCount = 0;
static int[] input;
static ArrayList<Integer> colours = new ArrayList<Integer>();
static String intructionsString =   "<html><p> THE GAME OF CODE BREAKER HAS TWO ROLES: THE CREATER AND THE BREAKER. "+ //How to play instructions
"THE CREATER CREATES A HIDDEN COLOR COMBINATION OR CODE - "+
"CONSISTING OF 4-6 COLOR PEGS CHOSEN FROM 4-8 COLORS OF RED,ORANGE,YELLOW, GREEN, BLUE, PINK, CYAN, MAGENTA"+
"WITH REPEATS ALLOWED. E.G. ROGG - RED, ORANGE, GREEN, GREEN. THE BREAKER'S GOAL IS TO FIGURE OUT THE HIDDEN CODE BY GUESSING."+
" THE CREATER WILL BE GIVING FEEDBACK ON THE CLOSENESS OF GUESSES BY GIVING HINTS THAT SHOW.THE NUMBER OF CORRECT POSITION COLOR"+
" PEGS ARE IN THE GUESS, AND THE INCORRECTLY POSITIONED BUT CORRECT COLORED PEGS IN THE GUESS. BLACK PEGS REPRESENTS CORRECT"+
" POSITION AND COLOR. WHITE PEGS REPRESENTS WRONG POSITION BUT RIGHT COLOR. THE BREAKER HAS A TOTAL OF 10 ATTEMPTS TO GUESS "+
"THE CODE. IF THE BREAKER GUESSES WITHIN 10 TRIES, THEY WIN, IF THEY DON'T, THE CREATE WINS THE GAME. </p></html> ";
static String [] code = new String[4], hintTotal;
static String guess = "RROO", hintStr = "", pHint = "", ans;
static boolean win = false, loop = false;
static ArrayList<Integer> comboList;
static int numTries = 0;
static String time;
static double startTime, finishTime;
static Clip music;
static AudioInputStream musicAudio;
static PrintWriter output;

public static void main(String[] args) {
frame.setSize(1000,1000); //Frame initialization 
frame.setLayout(new BorderLayout(200,100));
frame.getContentPane().setBackground( Color.DARK_GRAY );
mainMenu();
frame.setVisible(true);
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

try { //Plays background music
    File audioMusic = new File("C:/Users/zhang/Java 11/Java 11/Java 11/Merry Go Round of Life - Howl's Moving Castle (Joe Hisaishi).wav");
    musicAudio = AudioSystem.getAudioInputStream(audioMusic); //Copy path to audioMusic
    music = AudioSystem.getClip();
    music.open(musicAudio);
} catch (Exception ex){} 
music.start(); //starts music
music.loop(Clip.LOOP_CONTINUOUSLY);

}

public static void mainMenu(){ //Creating Start menu
startPanel = new JPanel();
nullPanel = new JPanel();
nullPanel2 = new JPanel();
titlePanel = new JPanel();
nullLabel = new JLabel();
nullLabel2 = new JLabel();
nullLabel3 = new JLabel();
nullLabel4 = new JLabel();
nullLabel5 = new JLabel();
playerGuess = new JButton("PLAYER GUESS");
AIGuess = new JButton("AI GUESS");
howToPlay = new JButton("How to play");
exit = new JButton("EXIT");
startPanel.setLayout(new GridLayout(10, 0));
JLabel title = new JLabel("CODEBREAKERS");
title.setFont(new Font("Courier New",Font.ITALIC, 40));
title.setForeground(Color.WHITE);
titlePanel.add(title);
difficultly = new JButton("(Click to Change) # of Colours: 4");
difficultly.setFont(new Font("Courier New",Font.ITALIC, 18));
startPanel.add(difficultly);
difficultly.addActionListener(new toggleColours());
startPanel.add(nullLabel5);
startPanel.add(playerGuess);
playerGuess.addActionListener(new gamePlayerGuess());
startPanel.add(nullLabel);
startPanel.add(AIGuess);
AIGuess.addActionListener(new gameAIGuess());
startPanel.add(nullLabel2);
startPanel.add(howToPlay);
howToPlay.addActionListener(new Instructions());
startPanel.add(nullLabel3); //Null panels for formatting
startPanel.add(exit);
exit.addActionListener(new Exit());
startPanel.add(nullLabel4);
nullPanel.setBackground(Color.DARK_GRAY);
nullPanel2.setBackground(Color.DARK_GRAY);
startPanel.setBackground(Color.DARK_GRAY);
titlePanel.setBackground(Color.DARK_GRAY);
frame.add(nullPanel,BorderLayout.EAST);
frame.add(nullPanel2,BorderLayout.WEST);
frame.add(startPanel,BorderLayout.CENTER);
frame.add(titlePanel,BorderLayout.NORTH);
}

static class gamePlayerGuess implements ActionListener{
    public void actionPerformed(ActionEvent event){
        for(int i=0;i<numofColours;i++){ //Checks for # of colours being used
            colours.add(i+1);
        }
        if(numofColours == 4){
            valid_chars = "ROYG";
        }
        else if(numofColours == 6){
            valid_chars = "ROYGBP";
        }
        else{
            valid_chars = "ROYGBPCM";
        }
        clearStart();
        gamePagePlayerGuess();
        frame.repaint();
        frame.revalidate();
    }
}

static class gameAIGuess implements ActionListener{
    public void actionPerformed(ActionEvent event){
        for(int i=0;i<numofColours;i++){
            colours.add(i+1);
        }
        if(numofColours == 4){
            valid_chars = "ROYG";
        }
        else if(numofColours == 6){
            valid_chars = "ROYGBP";
        }
        else{
            valid_chars = "ROYGBPCM";
        }
        clearStart();
        comboList = generateCombinations(1, valid_chars.length(), SIZE);
        gamePageAIGuess();
        frame.repaint();
        frame.revalidate();
    }
}

public static void clearStart(){ //Deletes all components on startpanel
    frame.remove(startPanel);
    frame.remove(nullPanel);
    frame.remove(nullPanel2);
    frame.remove(titlePanel);

}

static class Exit implements ActionListener{
    public void actionPerformed(ActionEvent event){
      frame.dispose();
    }
}
public static void writeFile (){  //Writes playerGuesses games into file (code, num of tries, and time)
    gameCount ++;
    File myFile = new java.io.File("Game #" + gameCount);
    try {
        output = new PrintWriter(myFile);
        output.println ("code:" + ans);
        output.println ("# of tries " + lives);
        output.println ("Time:" + time);
        output.close();

        }
        catch (IOException e) {

        }
  
    
}

public static void gamePageAIGuess(){  //Creates the panels and components for AI guessing
    startTime = System.currentTimeMillis();
    code = createCode();
    frame.setLayout(new BorderLayout(10,50));
    gamePanel = new JPanel();
    gamePanel.setBackground(new Color(105, 75, 13));
    enterPanel = new JPanel();
    hintPanel = new JPanel();
    hintPanel.setBackground(new Color(130, 75, 13));
    enterPanel.setBackground(Color.GRAY);
    enterPanel.setLayout(new FlowLayout());
    hintPanel.setLayout(new GridLayout(11, 4));
    gamePanel.setLayout(new GridLayout(11, 4));


        buttons[0] = new JButton("Click Me"); //Buttons to help the user memorize their code
        buttons[0].setBorder(bordergrid);
        buttons[0].addActionListener(new colorChange());
        buttons[1] = new JButton("Click Me");
        buttons[1].setBorder(bordergrid);
        buttons[1].addActionListener(new colorChange2());
        buttons[2] = new JButton("Click Me");
        buttons[2].setBorder(bordergrid);
        buttons[2].addActionListener(new colorChange3());
        buttons[3] = new JButton("Click Me");
        buttons[3].setBorder(bordergrid);
        buttons[3].addActionListener(new colorChange4());
        gamePanel.add(buttons[0]);
        gamePanel.add(buttons[1]);
        gamePanel.add(buttons[2]);
        gamePanel.add(buttons[3]);

        answers[0][0] = new JLabel(); //First AI GUESS default first guess is RROO (red red orange orange)
        answers[0][0].setBorder(bordergrid);
        answers[0][0].setOpaque(true);
        answers[0][0].setBackground(Color.RED);
        gamePanel.add(answers[0][0]);
       
        answers[0][1] = new JLabel(); //First AI GUESS
        answers[0][1].setBorder(bordergrid);
        answers[0][1].setOpaque(true);
        answers[0][1].setBackground(Color.RED);
        gamePanel.add(answers[0][1]);
       
        answers[0][2] = new JLabel(); //First AI GUESS
        answers[0][2].setBorder(bordergrid);
        answers[0][2].setOpaque(true);
        answers[0][2].setBackground(Color.ORANGE);
        gamePanel.add(answers[0][2]);
       
        answers[0][3] = new JLabel(); //First AI GUESS
        answers[0][3].setBorder(bordergrid);
        answers[0][3].setOpaque(true);
        answers[0][3].setBackground(Color.ORANGE);
        gamePanel.add(answers[0][3]);
             

    for (int row = 0; row < 9; row++){ //creates buttons for board
        for(int col = 0; col < 4; col++){
            answers[row][col] = new JLabel();
            answers[row][col].setBorder(bordergrid);
            answers[row][col].setOpaque(true);
            answers[row][col].setBackground(Color.GRAY);
            gamePanel.add(answers[row][col]);

        }
    }

test3 = new JLabel();
playAgain = new JButton("Play Again"); //Submit, Play again, and menu buttons
mainMenuButton = new JButton("MainMenu");
submit = new JButton("Submit Hints");
frame.add(gamePanel,BorderLayout.CENTER);

hintBlack = new JButton("Black Hints: " + bCount);
hintBlack.addActionListener(new hintCountBlack());
hintWhite  = new JButton("White Hints: " + wCount);
hintWhite.addActionListener(new hintCountWhite());
hintPanel.add(hintBlack);
hintPanel.add(hintWhite);

for(int col = 0; col < 2; col++){ //Formatting
    nullarrayLabel[col] = new JLabel("_______");
    hintPanel.add(nullarrayLabel[col]);
}

for (int row = 0; row < 10; row++){ //creates hints
    for(int col = 0; col < 4; col++){
        hint[row][col] = new JLabel();
        hint[row][col].setBorder(bordergrid);
        hint[row][col].setOpaque(true);
        hint[row][col].setBackground(Color.GRAY);
        hintPanel.add(hint[row][col]);
    }
}

test3.setBackground(Color.DARK_GRAY);
enterPanel.add(submit);
submit.addActionListener(new hintSubmit());
enterPanel.add(playAgain);
playAgain.addActionListener(new restart2());
enterPanel.add(mainMenuButton);
mainMenuButton.addActionListener(new menu());
frame.add(hintPanel,BorderLayout.EAST);
frame.add(enterPanel,BorderLayout.NORTH);
frame.add(test3,BorderLayout.SOUTH); //Formatting for Panels
}

   
    public static void gamePagePlayerGuess(){ //Creates components for PlayerGuessing game
        startTime = System.currentTimeMillis();     //starts timer 
        code = createCode(); //creates random code 
        input = new int[4];
        frame.setLayout(new BorderLayout(10,50));
        gamePanel = new JPanel();
        gamePanel.setBackground(new Color(105, 75, 13));
        enterPanel = new JPanel();
        hintPanel = new JPanel();
        hintPanel.setBackground(new Color(130, 75, 13));
        enterPanel.setBackground(Color.GRAY);
        enterPanel.setLayout(new FlowLayout());
        hintPanel.setLayout(new GridLayout(11, 4));
        gamePanel.setLayout(new GridLayout(11, 4));


            buttons[0] = new JButton("Click Me"); //Creates input code buttons
            buttons[0].setBorder(bordergrid);
            buttons[0].addActionListener(new colorChange());
            buttons[1] = new JButton("Click Me");
            buttons[1].setBorder(bordergrid);
            buttons[1].addActionListener(new colorChange2());
            buttons[2] = new JButton("Click Me");
            buttons[2].setBorder(bordergrid);
            buttons[2].addActionListener(new colorChange3());
            buttons[3] = new JButton("Click Me");
            buttons[3].setBorder(bordergrid);
            buttons[3].addActionListener(new colorChange4());
            gamePanel.add(buttons[0]);
            gamePanel.add(buttons[1]);
            gamePanel.add(buttons[2]);
            gamePanel.add(buttons[3]);


        for (int row = 0; row < 10; row++){ //creates label for board (displaying colours)
            for(int col = 0; col < 4; col++){
                answers[row][col] = new JLabel();
                answers[row][col].setBorder(bordergrid);
                answers[row][col].setOpaque(true);
                answers[row][col].setBackground(Color.GRAY);
                gamePanel.add(answers[row][col]);

            }
        }

    test3 = new JLabel();
    playAgain = new JButton("Play Again");
    mainMenuButton = new JButton("MainMenu");
    submit = new JButton("Guess");
    frame.add(gamePanel,BorderLayout.CENTER);

    for(int col = 0; col < 4; col++){
        nullarrayLabel[col] = new JLabel("__________"); //Formatting
        hintPanel.add(nullarrayLabel[col]);
    }

    for (int row = 0; row < 10; row++){ //creates hints
        for(int col = 0; col < 4; col++){
            hint[row][col] = new JLabel();
            hint[row][col].setBorder(bordergrid);
            hint[row][col].setOpaque(true);
            hint[row][col].setBackground(Color.GRAY);
            hintPanel.add(hint[row][col]);
        }
    }

    test3.setBackground(Color.DARK_GRAY);
    enterPanel.add(submit);
    submit.addActionListener(new guessColours());
    enterPanel.add(playAgain);
    playAgain.addActionListener(new restart());
    enterPanel.add(mainMenuButton);
    mainMenuButton.addActionListener(new menu());
    frame.add(hintPanel,BorderLayout.EAST);
    frame.add(enterPanel,BorderLayout.NORTH);
    frame.add(test3,BorderLayout.SOUTH);
}
static class guessColours implements ActionListener{ //Actionlistener for button press
    public void actionPerformed(ActionEvent event){
        String codeStr = "";
        for (int i=0; i<4;i++){
            codeStr += code[i];
        }
        ans = codeStr;
        System.out.println(ans);
        rowCount += 1;
        input[0] = count1; //Find the button value which corresponds to a colour 
        input[1] = count2;
        input[2] = count3;
        input[3] = count4;
        for (int col = 0; col<4;col++){
            answers[lives][col].setBackground(buttons[col].getBackground()); //set the Background after each guess
        }

        playerGuess(code);

        for(int col = 0; col<hintStr.length();col++){ //Set the background of the hint labels after each guess
            if(hintStr.charAt(col) == 'b'){
                hint[lives-1][col].setBackground(Color.BLACK);
            }
            else if (hintStr.charAt(col) == 'w'){
                hint[lives-1][col].setBackground(Color.WHITE);
            }
        frame.repaint();
        frame.revalidate();
    }
    if(win){ //check for win
        finishTime = System.currentTimeMillis(); //if win end timer
        time = displayTime(startTime, finishTime); //calculate time
        endLabel = new JLabel("YOU GOT THE CODE RIGHT!" + " TIME: " + time); //outputs results
        for(int col = 0; col<4;col++){
            hint[lives][col].setBackground(Color.BLACK);
        }
        writeFile(); //add the results to file
        enterPanel.add(endLabel);
        enterPanel.remove(submit);
        frame.repaint();
        frame.revalidate();
    }

    if(lives == 10){
            finishTime = System.currentTimeMillis(); //if past 10 tries stop timer
            time = displayTime(startTime, finishTime); //calcuate times
            writeFile(); //add results to file
            endLabel = new JLabel("SORRY, YOU LOST! THE CODE WAS: " + codeStr+ " TIME: " + time);
            enterPanel.add(endLabel);
            enterPanel.remove(submit);
            frame.repaint();
            frame.revalidate();
        }
    }
}


static class colorChange implements ActionListener{ //Action listener to chagne colour of button 1 when clicked
    public void actionPerformed(ActionEvent event){
        count1 +=1;
        if(count1 == numofColours){
            count1 = 0;
        }
            if(colours.get(count1) == 1 ){
                buttons[0].setBackground(Color.RED);
            }
            else if(colours.get(count1) == 2){
                buttons[0].setBackground(Color.ORANGE);
            }
            else if(colours.get(count1) == 3){
                buttons[0].setBackground(Color.YELLOW);
            }
            else if(colours.get(count1) == 4){
                buttons[0].setBackground(Color.GREEN);
            }
            else if(colours.get(count1) == 5){
                buttons[0].setBackground(Color.BLUE);
            }
            else if(colours.get(count1) == 6){
                buttons[0].setBackground(Color.PINK);
            }
            else if(colours.get(count1) == 7){
                buttons[0].setBackground(Color.CYAN);
            }
            else {
                buttons[0].setBackground(Color.MAGENTA);
            }    
    }
}

static class colorChange2 implements ActionListener{//Action listener to chagne colour of button 2 when clicked
    public void actionPerformed(ActionEvent event){
        count2 +=1;
        if(count2 == numofColours){
            count2 = 0;
        }
        if(colours.get(count2) == 1 ){
            buttons[1].setBackground(Color.RED);
        }
        else if(colours.get(count2) == 2){
            buttons[1].setBackground(Color.ORANGE);
        }
        else if(colours.get(count2) == 3){
            buttons[1].setBackground(Color.YELLOW);
        }
        else if(colours.get(count2) == 4){
            buttons[1].setBackground(Color.GREEN);
        }
        else if(colours.get(count2) == 5){
            buttons[1].setBackground(Color.BLUE);
        }
        else if(colours.get(count2) == 6){
            buttons[1].setBackground(Color.PINK);
        }
        else if(colours.get(count2) == 7){
            buttons[1].setBackground(Color.CYAN);
        }
        else {
            buttons[1].setBackground(Color.MAGENTA);
        }   
    }
}

static class colorChange3 implements ActionListener{//Action listener to chagne colour of button 3 when clicked
    public void actionPerformed(ActionEvent event){
        count3 +=1;
        if(count3 == numofColours){
            count3 = 0;
        }    
        if(colours.get(count3) == 1 ){
            buttons[2].setBackground(Color.RED);
        }
        else if(colours.get(count3) == 2){
            buttons[2].setBackground(Color.ORANGE);
        }
        else if(colours.get(count3) == 3){
            buttons[2].setBackground(Color.YELLOW);
        }
        else if(colours.get(count3) == 4){
            buttons[2].setBackground(Color.GREEN);
        }
        else if(colours.get(count3) == 5){
            buttons[2].setBackground(Color.BLUE);
        }
        else if(colours.get(count3) == 6){
            buttons[2].setBackground(Color.PINK);
        }
        else if(colours.get(count3) == 7){
            buttons[2].setBackground(Color.CYAN);
        }
        else {
            buttons[2].setBackground(Color.MAGENTA);
        }   

        }
       
    }

static class colorChange4 implements ActionListener{//Action listener to chagne colour of button 4 when clicked
    public void actionPerformed(ActionEvent event){
        count4 +=1;
        if(count4 == numofColours){
            count4 = 0;
        }
        if(colours.get(count4) == 1 ){
            buttons[3].setBackground(Color.RED);
        }
        else if(colours.get(count4) == 2){
            buttons[3].setBackground(Color.ORANGE);
        }
        else if(colours.get(count4) == 3){
            buttons[3].setBackground(Color.YELLOW);
        }
        else if(colours.get(count4) == 4){
            buttons[3].setBackground(Color.GREEN);
        }
        else if(colours.get(count4) == 5){
            buttons[3].setBackground(Color.BLUE);
        }
        else if(colours.get(count4) == 6){
            buttons[3].setBackground(Color.PINK);
        }
        else if(colours.get(count4) == 7){
            buttons[3].setBackground(Color.CYAN);
        }
        else {
            buttons[3].setBackground(Color.MAGENTA);
        }   
        }
       
    }

static class Instructions implements ActionListener{ //Creates components for how to play panel
    public void actionPerformed(ActionEvent event){
    startPanel.removeAll();
    startPanel.setLayout(new GridLayout(2,0));
    instructions = new JLabel(intructionsString);
    backToMain = new JButton("Back");
    instructions.setForeground(Color.WHITE);
    instructions.setFont(new Font("Courier New",Font.ITALIC, 18));
    startPanel.add(instructions);
    startPanel.add(backToMain);
    backToMain.addActionListener(new backMain());
    startPanel.repaint();
    startPanel.revalidate();
   
   
   }
}

static class restart implements ActionListener{ //restart button 
    public void actionPerformed(ActionEvent event){
        frame.remove(hintPanel);
        frame.remove(gamePanel);
        frame.remove(enterPanel);
        frame.remove(test3);
        time = ""; //resets all values
        startTime = 0;
        finishTime = 0;
        guess = "";
        hintStr = "";
        win = false;
        count1=-1;
        count2=-1;
        count3=-1;
        count4=-1;
        colourCount = 1;
        lives = 0;
        rowCount = 0;
        gamePagePlayerGuess(); //called the playerguessing method
        frame.repaint();
        frame.revalidate();

   
   }
}

static class restart2 implements ActionListener{ //restart method for AI panel
    public void actionPerformed(ActionEvent event){
        frame.remove(hintPanel);
        frame.remove(gamePanel);
        frame.remove(enterPanel);
        frame.remove(test3);
        guess = "RROO"; //restarts values
        time = "";
        startTime = 0;
        finishTime = 0;
        bCount=0;
        wCount=0;
        hintStr = "";
        pHint = "";
        loop = false;
        comboList.clear();
        comboList = generateCombinations(1, valid_chars.length(), SIZE);//List of all possible combos (int)
        numTries = 0;
        hintStr = "";
        win = false;
        count1=-1;
        count2=-1;
        count3=-1;
        count4=-1;
        colourCount = 1;
        lives = 0;
        gamePageAIGuess();
        frame.repaint();
        frame.revalidate();

   
   }
}

static class backMain implements ActionListener{ //Action listener to go back to the main menu
    public void actionPerformed(ActionEvent event){
        frame.remove(startPanel);
        frame.remove(nullPanel);
        frame.remove(nullPanel2);
        frame.remove(titlePanel);
        mainMenu();
        frame.revalidate();
        frame.repaint();
    }
}

static class menu implements ActionListener{ //Mainmenu button actionlistener (allows users to go back to main menu)
    public void actionPerformed(ActionEvent event){
        frame.remove(startPanel);
        frame.remove(nullPanel);
        frame.remove(nullPanel2);
        frame.remove(titlePanel);
        frame.remove(hintPanel);
        frame.remove(gamePanel);
        frame.remove(enterPanel);
        frame.remove(test3);
        rowCount = 0; //reset values
        time = "";
        startTime = 0;
        finishTime = 0;
        bCount=0;
        wCount=0;
        hintStr = "";
        pHint = "";
        loop = false;
        comboList.clear();
        comboList = generateCombinations(1, valid_chars.length(), SIZE);
        numTries = 0;
        hintStr = "";
        win = false;
        count1=-1;
        count2=-1;
        count3=-1;
        count4=-1;
        colourCount = 1;
        lives = 0;
        mainMenu();
        frame.revalidate();
        frame.repaint();
    }
}
public static String[] createCode(){ //generates random code
    Random rand = new Random();

    String[] code = new String [SIZE];

    for (int i=0; i<SIZE; i++){
    int randIndex = rand.nextInt(valid_chars.length());
    String randColor = String.valueOf(valid_chars.charAt(randIndex));

    code[i] = randColor;
    }

    return code;
}

public static String[] findFullyCorrect(String[] code, String guess){ //compares code and guess and returns hints
    String black = ""; 
    String white = "";
    
    
    ArrayList<String> guessArr = new ArrayList<String>(); //converts guess and code into arraylists
    ArrayList<String> codeArr = new ArrayList<String>();
    for (int i=0; i<guess.length(); i++){
        guessArr.add(String.valueOf(guess.charAt(i)));
        codeArr.add(code[i]);
    }
    
    for (int i=0; i<guessArr.size(); i++){ //Black hints
        if (guessArr.get(i).equals(codeArr.get(i))){ //if guess at index equals to code at index
            black+="b";
            guessArr.remove(i);
            codeArr.remove(i);
            i--;
        }
    }
     
    int index=-1;
    
    for (int i=0; i<guessArr.size(); i++){ //White hints
        if (codeArr.contains(guessArr.get(i))){
            white+="w";
            index = codeArr.indexOf(guessArr.get(i));
            codeArr.remove(index);
        }
    }
    
    String hintChars = black+white;
    
    String[] hint = strToArr(hintChars);
    
    return hint;
    
}

public static String getUserGuess(){ //Get user guesses and turn into string
    guess = "";
    hintStr = "";
    for(int i = 0; i<4; i++){
        if(input[i] == 0){
            guess += "R";
        }
        else if(input[i] == 1){
            guess += "O";
        }
        else if(input[i] == 2){
            guess += "Y";
        }
        else if(input[i] == 3){
            guess += "G";
        }
        else if(input[i] == 4){
            guess += "B";
        }
        else if(input[i] == 5){
            guess += "P";
        }
        else if(input[i] == 6){
            guess += "C";
        }
        else{
            guess += "M";
        }
    }
    return guess;
}

public static String displayTime(double startTime, double finishTime){ //calculates time method
    int totalSeconds = (int) (finishTime - startTime)/1000;
    int seconds = totalSeconds % 60;
    int mins = (totalSeconds - seconds)/60;
    
    return ("TIME TAKEN: "+mins+" minutes "+seconds+" seconds");
}

public static void playerGuess(String[] code){ //compares players guess with code and determines if win

    String codeStr = "";
    for (int i=0; i<code.length; i++){
        codeStr += code[i];
    }
    guess = getUserGuess();

    if (guess.equals(codeStr)){
    win = true;
    }
    else{
        String[] hintArr = findFullyCorrect(code, guess);
        for (int i=0; i<hintArr.length; i++){
        hintStr += hintArr[i];
        }
    lives++;
    }

}

public static void aiGuess (){     //Ai guess method
    String pHint = hintStr;
    
    if(loop){ //check if win
        if (pHint.equals("bbbb")){
            loop = false; 
            win = true;
        }
        
        String[] playerHint = strToArr(pHint); //change hints to array

        for (int i=0; i<comboList.size(); i++){
            
            String [] colorCombo = numToColor(comboList.get(i));

            
            String[] aiHint = findFullyCorrect(colorCombo, guess); //compares hints with all possible answers to see which is a match
            
            boolean equal = checkEqual(aiHint, playerHint);
            
            if (equal ==  false){
                comboList.remove(i); //deletes from list if not a match
                i--;
            }
        }
        
        
        try{
            String[] guessArr = numToColor(comboList.get(0)); //takes the first from the index as test
            guess = arrToStr(guessArr);
        }
        catch(Exception e){
        }
        
        numTries++;
    }

}


public static boolean checkEqual(String[] arr1, String[] arr2){// checks if the 2 arrays passed are equal
    if(arr1.length != arr2.length){
        return false;
    }
    
    for (int i=0; i<arr1.length; i++){
        if (!arr1[i].equals(arr2[i])){
            return false;
        }
    }
    
    return true;
}


public static String[] numToColor (int nums){ //converts numbers to corrosponding colours
    String[] numArr = strToArr(String.valueOf(nums));
    String[] colorArr = new String[numArr.length];
    
    for (int i=0; i<numArr.length; i++){
        colorArr[i] = String.valueOf(valid_chars.charAt(Integer.parseInt(numArr[i])-1));
    }
    
    return colorArr;
}

public static ArrayList<Integer> generateCombinations(int min, int max, int length) { //Generates all possible combonations 
    ArrayList<Integer> comboList = new ArrayList<Integer>();
    
    int[] combination = new int[length];
    generateCombinationsHelper(min, max, length, combination, 0, comboList);
    
    return comboList;
}	


public static void generateCombinationsHelper(int min, int max, int length, int[] combination,
			int currentIndex, ArrayList<Integer> comboList){
        
		if (currentIndex == length) {
        	int tempCombo = addCombination(combination);
        	comboList.add(tempCombo);
            return;
        }

        for (int i = min; i <= max; i++) {
            combination[currentIndex] = i;
            generateCombinationsHelper(min, max, length, combination, currentIndex + 1, comboList);
        }
    }


    public static int addCombination(int[] combination) { //adds the combonation to the arraylist
		String tempStr = "";
        for (int i = 0; i < combination.length; i++) {
            tempStr+=String.valueOf(combination[i]);
        }
        return Integer.parseInt(tempStr);
    }

public static int colorToInt(String colors){
String numStr = "";

String[] validColorArr = strToArr(valid_chars);

ArrayList<String> validColors = new ArrayList<String>();
for (int i=0; i<validColorArr.length; i++){
validColors.add(validColorArr[i]);
}

String[] colorArr = strToArr(colors);
for (int i=0; i<colorArr.length; i++){
numStr += String.valueOf(validColors.indexOf(colorArr[i]+1));
}

return Integer.parseInt(numStr);
}



    public static String[] strToArr (String str){ //converts string to an array
        String[] arr = new String[str.length()];

    for (int i=0; i<arr.length; i++){
        arr[i] = String.valueOf(str.charAt(i));
    }

    return arr;
}


public static String arrToStr (String[] arr){ //converts array to string
    String str = "";

for (int i=0; i<arr.length; i++){
    str += arr[i];
}

    return str;
}

static class hintSubmit implements ActionListener{ //Submit button for AI Guessing
    public void actionPerformed(ActionEvent event){
        loop = true;
        hintStr = "";

        for(int i=0;i<bCount;i++){  //Checks for black and white hints and display it
            hintStr += "b";
        }

        for(int i=bCount; i<bCount+wCount; i++){
            hintStr += "w";
        }

        for(int i=0; i<bCount+wCount; i++){
            if (hintStr.charAt(i) == 'b'){
                hint[lives][i].setBackground(Color.BLACK);
            }
            else if (hintStr.charAt(i) == 'w'){
                hint[lives][i].setBackground(Color.WHITE);
            }
        }
        
        aiGuess();
        loop = false;

        for (int i=0; i<guess.length(); i++){
            if(guess.charAt(i) == 'R' ){
                answers[lives][i].setBackground(Color.RED);
            }
            else if(guess.charAt(i) == 'O' ){
                answers[lives][i].setBackground(Color.ORANGE);
            }
            else if(guess.charAt(i) == 'Y' ){
                answers[lives][i].setBackground(Color.YELLOW);
            }
            else if(guess.charAt(i) == 'G' ){
                answers[lives][i].setBackground(Color.GREEN);
            }
            else if(guess.charAt(i) == 'B' ){
                answers[lives][i].setBackground(Color.BLUE);
            }
            else if(guess.charAt(i) == 'P' ){
                answers[lives][i].setBackground(Color.PINK);
            }
            else if(guess.charAt(i) == 'C' ){
                answers[lives][i].setBackground(Color.CYAN);
            }
            else {
                answers[lives][i].setBackground(Color.MAGENTA);
            }    
        frame.repaint();
        frame.revalidate();
        }
        lives ++;
        if(win){ //if win, stop time and output results
            finishTime = System.currentTimeMillis(); 
            time = displayTime(startTime, finishTime);
            endLabel = new JLabel("AI GUESS THE CORRECT CODE!" + " TIME:" + time);
            enterPanel.add(endLabel);
            enterPanel.remove(submit);
            frame.repaint();
            frame.revalidate();
        }
    
        if(lives == 10){//if lose, stop time and output results
                finishTime = System.currentTimeMillis();
                time = displayTime(startTime, finishTime);
                endLabel = new JLabel("SADLY AI DID NOT GET IT CORRECT" + " TIME:" + time);
                enterPanel.add(endLabel);
                enterPanel.remove(submit);
                frame.repaint();
                frame.revalidate();
            }
    }
   }

static class toggleColours implements ActionListener{ //Changes the number of colours avaliable to use (on mainmenu)
    public void actionPerformed(ActionEvent event){
        colourCount +=1;
        if (colourCount == 1){
            numofColours = 4;
            difficultly.setText("(Click to Change) # of Colours: " + numofColours);
            frame.repaint();
        }
        else if (colourCount == 2){
            numofColours = 6;
            difficultly.setText("(Click to Change) # of Colours: " + numofColours);
            frame.repaint();
        }

        else if(colourCount == 3){
            numofColours = 8;
            colourCount=0;
            difficultly.setText("(Click to Change) # of Colours: " + numofColours);
            frame.repaint();
        }
    }
}

static class hintCountBlack implements ActionListener{ //Counts the black hints
    public void actionPerformed(ActionEvent event){
    bCount++;
    if (bCount == 5){
        bCount = 0;
    }
    if(bCount+wCount >= 5){
        wCount--;
    }
    hintBlack.setText("Black hints: " + bCount);
    hintWhite.setText("White hints: " + wCount);
    frame.repaint();
    frame.revalidate();
    }
}

static class hintCountWhite implements ActionListener{ //Counts the white hints
    public void actionPerformed(ActionEvent event){
    wCount++;
    if (wCount == 5){
    wCount = 0;
    }
    if(bCount+wCount >= 5){
        bCount--;
    }
    hintBlack.setText("Black hints: " + bCount);
    hintWhite.setText("White hints: " + wCount);
    frame.repaint();
    frame.revalidate();
   
    }
}
}