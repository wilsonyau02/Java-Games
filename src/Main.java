import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import static java.lang.Math.random;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    private static final String rowFormat = "%-12s";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int menuOption=0;
        boolean reenterOption = true;

        while (true){
            // Main menu
            System.out.println("\n\n\n======================================");
            System.out.println("        Welcome to Java Games");
            System.out.println("======================================");
            System.out.println("(1) Hangman");
            System.out.println("(2) Scissor, Rock, Paper");
            System.out.println("(3) Tic-Tac-Toe");
            System.out.println("(4) Flip and Match");
            System.out.println("(5) Five Dice");
            System.out.println("(0) Exit game");
            System.out.println("Which games would you like to play ?");
            //Do the validation to the menu input
            //Use do-while to let the user enter again if the input is not correct
            do{
                System.out.print("Enter the option: ");

                //Use try, catch and throw to catch the exception
                try {
                    menuOption = scanner.nextInt();
                    //Throw the exception if the input is not in the option list (1/2/3/4/5)
                    if (menuOption < 0 || menuOption > 5){
                        throw new Exception();
                    }
                    reenterOption = false;
                }
                //Catch the exception when the user entered others characters or symbols
                catch (InputMismatchException e){
                    System.out.println("Incorrect input. Only digit is allowed.");
                    scanner.nextLine();

                }
                //Catch the exception when the user entered the number other than 1-5
                catch (Exception e){
                    System.out.println("Incorrect input. Others option is not allowed.");
                    scanner.nextLine();
                }
            }while (reenterOption);

            switch (menuOption){
                case 1:
                    //Call to Hangman game
                    Hangman();
                    break;
                case 2:
                    //Call to Scissor, rock, paper game
                    PlayRPS();
                    break;
                case 3:
                    //Call to Tic-tac-toe game
                    TicTacToe();
                    break;
                case 4:
                    //Call to Flip and match game
                    FlipAndMatch();
                    break;
                case 5:
                    //Call to Five Dice game
                    fiveDice();
                    break;
                default:
                    System.out.println();
                    System.out.println("Thank you. See you again");
                    System.exit(0);
            }
        }

    }


    //Hangman
    public static char[] alphabetsGuessed = new char[11];
    public static String ans;
    public static int[] cumulateScore = {0,0,0,0};
    //{total words, total letters, total correct words, total correct letters}


    public static void Hangman() {
        Scanner sc = new Scanner(System.in);
        char userInput;
        int numCorrect, numAlphabet, attempt;
        String cont;

        Arrays.fill(cumulateScore, 0);
        //run the hangman game and reset values
        do {
            numCorrect = attempt = 0;
            Arrays.fill(alphabetsGuessed, '\u0000');
            hangmanMenu();
            int mode = getMode();
            getWord(mode);
            if (ans.equals("")) return;
            int field = (10 - ans.length() + 10);
            numAlphabet = countAlphabet(ans) - 1;
            cumulateScore[0]++;


            System.out.println("\n\n\nEnter one letter to guess");
            displayHints();
            //loop the guessing process 10 times
            for (; numCorrect != numAlphabet && attempt < 10; attempt++) {
                System.out.print(String.format("%" + field + "s", (9 - attempt)) + " attempts left" + "\n");
                userInput = getInput();
                if (checkAnswer(userInput)) {
                    alphabetsGuessed[attempt] = userInput;
                    numCorrect++;
                } else {
                    alphabetsGuessed[attempt] = 0;
                }
                displayHints();
            }
            cumulateScore[1] += attempt;
            cumulateScore[3] += numCorrect;
            displayMessage(numCorrect, numAlphabet, attempt);

            //ask whether to continue or not
            System.out.print("continue? (Y/N)\n>>");
            cont = sc.nextLine();
            cont = cont.toUpperCase();
            while (!(cont.equals("Y") || cont.equals("N"))) {
                System.out.println("Please enter only Y or N");
                cont = sc.nextLine();
                cont = cont.toUpperCase();
            }
            System.out.println("\n\n");
        } while (cont.equals("Y"));
        endScore();
        System.out.println("Press Enter key to go back to Main menu ...");
        String s = sc.nextLine();

    }

    public static void getWord(int mode) {
        if (mode == 1){
            ans = selectWord();
        }else {
            ans = selectWord(mode);
        }
    }

    public static String selectWord() {
        String word;
        do {
            word = "";
            try {
                File read = new File("./src/word.txt");
                Scanner sc = new Scanner(read);
                int numOfWords = getNumWords();
                int randomNumber = (int) (numOfWords * Math.random());
                for (int i = 0; i < randomNumber; i++) {
                    sc.nextLine();
                }
                word = sc.nextLine();
                randomNumber = (int)(Math.random() * word.length());
                alphabetsGuessed[10] = word.charAt(randomNumber);
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred");
                System.out.println("Please check if there is a file named word.txt");
            }
        }while (word.length() > 10);
        return word;
    }

    public static String selectWord(int mode) {
        String word;
        do {
            word = "";
            try {
                File read = new File("./src/word.txt");
                Scanner sc = new Scanner(read);
                int[] wordDetails = getNumWords(mode);
                int randomNumber = (int)(wordDetails[1] * Math.random() + wordDetails[0]);
                for (int i = 0; i < randomNumber; i++) {
                    sc.nextLine();
                }
                word = sc.nextLine();
                randomNumber = (int)(Math.random() * word.length());
                alphabetsGuessed[10] = word.charAt(randomNumber);
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred");
                System.out.println("Please check if there is a file named word.txt");
            }
        }while (word.length() > 10);
        return word;
    }

    //get number of words in word.txt file for calculation
    public static int getNumWords() {
        int numLines = 0;
        try {
            File read = new File("./src/word.txt");
            Scanner sc = new Scanner(read);
            while(sc.hasNextLine()){
                numLines++;
                sc.nextLine();
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return numLines;
    }

    public static int[] getNumWords(int mode) {
        int lineNum = 0;
        int[] modeDetails = {0,0};
        String word;
        try {
            File read = new File("./src/word.txt");
            Scanner sc = new Scanner(read);
            while (sc.hasNextLine()) {
                lineNum++;
                word = sc.nextLine();
                if (word.length() == mode) {
                    modeDetails[1]++;
                    if (modeDetails[0] == 0) {
                        modeDetails[0] = lineNum;
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return modeDetails;
    }

    //count the number of alphabets in the word
    public static int countAlphabet(String word) {
        int numAlphabet = word.length();
        char[] c = word.toCharArray();
        Arrays.sort(c);
        for (int i = 0; i < word.length() - 1; i++) {
            if (c[i] == c[i+1]) {
                numAlphabet--;
            }
        }
        return numAlphabet;
    }

    //show the asterisk and letters guessed
    public static void displayHints() {
        for (int i = 0; i < ans.length(); i++) { //for letters in the answer
            for (int j = 0; j < alphabetsGuessed.length; j++) { //for letters guessed
                if (ans.charAt(i) == alphabetsGuessed[j]) {
                    System.out.print(ans.charAt(i));
                    break;
                }
                else {
                    if (j == alphabetsGuessed.length - 1) {
                        System.out.print("*");
                    }
                }
            }
        }
    }

    //get one alphabet only from the user
    public static char getInput() {
        Scanner sc = new Scanner(System.in);
        System.out.print(" -");
        String validation = sc.nextLine();
        boolean invalid;
        char c = 0;
        do {
            //start with false
            //become true if any requirement is not met
            invalid = false;
            if (validation.length() != 1) {
                System.out.println("Please enter one character at a time");
                invalid = true;
            }
            else {
                c = validation.charAt(0);
                if (!Character.isAlphabetic(c)) {
                    System.out.println("Only alphabets are accepted");
                    invalid = true;
                }else {
                    c = Character.toLowerCase(c);
                    for (char d : alphabetsGuessed) {
                        if (c == d) {
                            System.out.println("Cannot repeat used letters");
                            invalid = true;
                            break;
                        }
                    }
                }
            }
            if (invalid) {
                System.out.print(" -");
                validation = sc.nextLine();
            }
        }while (invalid);
        return c;
    }

    //check if letter is in answer
    public static boolean checkAnswer(char input) {
        for (int j = 0; j < ans.length(); j++) {
            if (input == ans.charAt(j)) {
                return true;
            }
        }
        return false;
    }

    //display congratulating message or motivating message
    //based on win or lose
    public static void displayMessage(int score, int totalScore, int attempt) {
        String[][] message = {{
                "Losing is part of the game.",
                "Losing is not a big deal,\njust win next time.",
                "Try better next time."},
                {
                        "Congratulations!",
                        "Nice work, Keep it up!",
                        "Cheers to you for winning!"
                }};
        if (score != totalScore) {
            System.out.println("\n\nAnswer: " + ans);
            System.out.println(message[0][(int)(Math.random() * message[0].length)]);
        } else {
            cumulateScore[2]++;
            System.out.println("\n\n" + message[1][(int)(Math.random() * message[1].length)]);
            System.out.println("Number of misses: " + (attempt - totalScore));
        }
    }

    public static void hangmanMenu() {
        System.out.println("""
                Type 1 to play random words

                     3 to play 3 letter words
                     4 to play 4 letter words
                     .
                     .
                     .
                     10 to play 10 letter words""");
    }

    public static int getMode() {
        Scanner sc = new Scanner(System.in);
        int i = 0;
        do {
            try{
                System.out.print(" -");
                i = sc.nextInt();
                if (!(i == 1 || (i >= 3 && i <= 10))){
                    System.out.println("Please enter numbers listed only");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter integers only");
                sc.nextLine();
            }
        }while (!(i == 1 || (i >= 3 && i <= 10)));
        return i;
    }

    public static void endScore() {
        System.out.printf(
                """
                        ----------------------------
                        | correct words  : %3d/%-3d |
                        | correct guesses: %3d/%-3d |
                        ----------------------------%n""",
                cumulateScore[2], cumulateScore[0], cumulateScore[3], cumulateScore[1]);
    }



    //Scissor, rock, paper
    public static void PlayRPS() {
        Scanner scanner = new Scanner(System.in);
        //Initialize scores at 0
        int userScore = 0;
        int compScore = 0;
        int drawScore = 0;
        //Set array list for userMoveset and compMoveset
        ArrayList <String> userMoveset = new ArrayList<>();
        ArrayList <String> compMoveset = new ArrayList<>();

        //Loop the game 10 times
        for (int i = 0; i < 10; i++) {
            //Set 0,1 and 2 option only for the game
            String[] rps = {"0", "1", "2"};
            String compMove = rps[new Random().nextInt(rps.length)];
            String userMove;

            //Prompts the user to enter a move (0 = Scissor, 1 = Rock, or 2 = Paper)
            //Then accept the user input and compare it
            //The game continues if the correct input is entered and restart if incorrect input is entered
            while (true) {
                System.out.println("Please enter your move (0 = Scissor, 1 = Rock, or 2 = Paper)");
                userMove = scanner.nextLine();
                if (userMove.equals("0") || userMove.equals("1") || userMove.equals("2")) {
                    break;
                }
                if (userMove.equals("")) {
                    System.out.println("Please enter a move!");
                }else {
                    System.out.println(userMove + " is not a valid move.");
                }
            }

            //If input and computer have the same move, it is a draw
            if (compMove.equals(userMove)) {
                System.out.println("This was a draw!\nNo points will be awarded.");
                drawScore++;
                if (userMove.equals("0")){
                    userMoveset.add("Scissors");
                    compMoveset.add("Scissors");
                }
                else if (userMove.equals("1")){
                    userMoveset.add("Rock");
                    compMoveset.add("Rock");
                }
                else {
                    userMoveset.add("Paper");
                    compMoveset.add("Paper");
                }
                System.out.println();

                //0 = Scissor beats 2 = Paper but loses to 1 = Rock
            } else if (userMove.equals("0")) {
                if (compMove.equals("2")) {
                    System.out.println("Computer chose paper!\nYou have won this round.");
                    userScore++;
                    userMoveset.add("Scissor");
                    compMoveset.add("Paper");
                    System.out.println();

                } else if (compMove.equals("1")) {
                    System.out.println("Computer chose rock!\nYou have lost this round.");
                    compScore++;
                    userMoveset.add("Scissor");
                    compMoveset.add("Paper");
                    System.out.println();
                }

                //1 = Rock beats 0 = Scissor but loses to 2 = Paper
            } else if (userMove.equals("1")) {
                if (compMove.equals("0")) {
                    System.out.println("Computer chose scissors!\nYou have won this round.");
                    userScore++;
                    userMoveset.add("Rock");
                    compMoveset.add("Scissor");
                    System.out.println();

                } else if (compMove.equals("2")) {
                    System.out.println("Computer chose paper!\nYou have lost this round.");
                    compScore++;
                    userMoveset.add("Rock");
                    compMoveset.add("Paper");
                    System.out.println();
                }

                //2 = Paper beats 1 = Rock but loses to 0 = Scissor
            } else {
                if (compMove.equals("1")) {
                    System.out.println("Computer chose rock!\nYou have won this round.");
                    userScore++;
                    userMoveset.add("Paper");
                    compMoveset.add("Rock");
                    System.out.println();

                } else if (compMove.equals("0")) {
                    System.out.println("Computer chose scissors!\nYou have lost this round.");
                    compScore++;
                    userMoveset.add("Paper");
                    compMoveset.add("Scissor");
                    System.out.println();
                }
            }
        }
        //Users score, computers score and amount of rounds draw is showed
        //as well as moves made each round
        System.out.printf("""
                The game has ended, these are your scores.
                Computer score: %d
                User score: %d
                Draws: %d

                """, compScore, userScore, drawScore);
        System.out.print("Rounds:                  Round 1     Round 2     Round 3     " +
                "Round 4     Round 5     Round 6     Round 7     Round 8     Round 9     Round 10\n");
        System.out.print("Moves made by user:      ");
        for (int i = 0; i < 10; i += 1) {
            System.out.printf(rowFormat, userMoveset.get(i));
        }
        System.out.print("\nMoves made by computer:  ");
        for (int i = 0; i < 10; i += 1) {
            System.out.printf(rowFormat, compMoveset.get(i));
        }
        Restart();
    }

    //Prompt user on whether to continue playing or end the game
    public static void Restart() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        //Do while loop to loop the question until a proper answer is provided
        do {
            System.out.print("\nDo you wish to continue? Enter a number (1 = Yes/2 = No): ");
            try {
                userInput = scanner.nextInt();
                if (userInput < 1 || userInput > 2) {
                    System.out.println("\nPlease enter a valid number!");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nMust enter an integer!");
                //Remove the incorrect input
                scanner.next();
            }
        }while (userInput < 1 || userInput > 2);

        scanner.nextLine();
        if (userInput == 1) {
            PlayRPS();
        }
        else {
            System.out.println("Thank you for playing.");
            System.out.println("Press Enter key to go back to main menu...");
            String s = scanner.nextLine();
        }
    }

    //Tic-tac-toe
    public static void TicTacToe(){

        int playerNum;
        int compNum;
        char continueGameInput;
        boolean continueGame = true;
        int[] positionMarked = {-1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] gameResult = {0, 0, 0}; // {WIN, LOSE, DRAW}
        char[] markChosen = new char[2]; // { Player's Symbol, Computer's Symbol }

        Scanner continueInput = new Scanner(System.in);

        //To print out the game instruction before the game start
        TicTacToeInstruction();

        while (continueGame) {

            int round = 0;

            //Make users can choose their own symbol by calling playerMarkChoose() function
            playerMarkChoose(markChosen);

            //Display the Tic-Tac-Toe board every time by calling boardDisplay() function
            boardDisplay(positionMarked, markChosen);

            do {
                //Ask user enter the position wanted by calling playerPlay() method.
                //positionMarked array was passed to prevent user choose the position has been chosen by validation in playerPlay().
                playerNum = playerPlay(positionMarked);

                //If user choose position 2 on the board, the number in array with index number 1 will change to 1.
                //It is to determine the position has been chosen by user.
                positionMarked[playerNum - 1] = 1;
                //Display the Tic-Tac-Toe board by calling boardDisplay() function with positionMarked and markChosen parameters
                boardDisplay(positionMarked, markChosen);

                //Round in a game for computer play is maximum of 4.
                //resultCheck == -1 means there is no winner decided yet.
                //if resultCheck is -1 but round has come to 4, computer will not be play anymore as last position has chosen by player.
                if (round < 4 && resultCheck(positionMarked) == -1) {
                    compNum = computerPlay(positionMarked);

                    //If computer choose position 3 on the board, the number in array with index number 2 will change to 0.
                    //It is to determine the position has been chosen by computer.
                    positionMarked[compNum - 1] = 0;
                    boardDisplay(positionMarked, markChosen);
                }

                //To calculate the number of round in a game
                round++;

                //Checking result by calling resultCheck() function with positionMarked as parameter
                //If the resultCheck is -1 AND the round is more than 4, the game will stopped
                //If the resultCheck is -1 AND the round is less than and equal to 4, the game will continue
            } while (resultCheck(positionMarked) == -1 && round <= 4);

            System.out.print("\nRESULT: ");

            //If the resultCheck return 1, player win.
            if (resultCheck(positionMarked) == 1) {
                System.out.println("Congratulations! You have WIN the game!");
                gameResult[0]++;
            }

            //If the resultCheck return 0, computer win.
            else if (resultCheck(positionMarked) == 0) {
                System.out.println("Sorry... You have LOSE the game...");
                gameResult[1]++;
            }

            //If the resultCheck return -1, player win.
            else {
                System.out.println("DRAW GAME!");
                gameResult[2]++;
            }

            //Ask user whether they want to continue another game or not.
            do {
                System.out.println("\nWIN  : " + gameResult[0]);
                System.out.println("LOSE : " + gameResult[1]);
                System.out.println("DRAW : " + gameResult[2]);
                System.out.print("\nDo you want to play another round? ('Y'-Yes / 'N'-No): ");
                continueGameInput = continueInput.next().charAt(0);
                continueInput.nextLine();

                if (continueGameInput == 'Y' || continueGameInput == 'y') {
                    continueGame = true;
                } else if (continueGameInput == 'N' || continueGameInput == 'n') {
                    System.out.println("\nBye-bye...See you again!");
                    System.out.println("Press Enter key to go back to main menu...");
                    String s = continueInput.nextLine();
                    continueGame = false;

                } else {
                    System.out.println("ERROR: Please enter 'Y' or 'N' only!");
                }

                //If user didn't input N,n,Y or y, they have to input again.
            } while (continueGameInput != 'N' && continueGameInput != 'n' && continueGameInput != 'Y' && continueGameInput != 'y');

            //Clear the number in the array by filling all number with -1
            Arrays.fill(positionMarked, -1);
        }
    }

    public static void TicTacToeInstruction() {

        System.out.println("\nWelcome to the Tic-Tae-Toc Game!!");
        System.out.println("\nInstruction:");
        System.out.println("The board is mapped as below: ");
        System.out.println();
        System.out.println("  " + "=".repeat(13) + "\n   TIC-TAE-TOE \n" + "  " + "=".repeat(13));
        for (int i = 0; i < 9; i++) {
            if (i == 0 || i == 3 || i == 6) {
                System.out.print("  ");
            }
            System.out.print("| ");

            System.out.print(i + 1);

            System.out.print(" ");
            if (i == 2 || i == 5 || i == 8) {
                System.out.println("|");
                System.out.println("  " + "-".repeat(13));
            }
        }
        System.out.println("\n1. Player is ALLOWED to choose any position on the board based on the range (1 - 9).");
        System.out.println("2. Player is NOT BE ALLOWED to choose the position which has been placed and marked.");
        System.out.println("3. WIN game when there is 3 matching symbols in a row, either horizontally, vertically, or diagonally.");
        System.out.println("4. TIE game when there is no 3 matching symbols in a row after all position has been exhausted.");
        System.out.println("\nFirst... Let's decide the symbol...");
    }

    public static void playerMarkChoose(char[] markChosen) {

        char marker;

        Scanner input = new Scanner(System.in);

        do {
            System.out.print("\nChoose your symbol ('X' or 'O'): ");
            marker = input.next().charAt(0);

            //markChosen[0] will store player symbol
            //markChosen[1] will store computer symbol

            if (Character.toUpperCase(marker) == 'X') {
                markChosen[0] = 'X';
                markChosen[1] = 'O';
            } else if (Character.toUpperCase(marker) == 'O') {
                markChosen[0] = 'O';
                markChosen[1] = 'X';
            } else {
                //Display error message
                System.out.println("ERROR: Please enter 'X' or 'O' only! ");
            }

            //If user didn't input X, x ,O or o, they have to input again.
        } while (marker != 'X' && marker != 'x' && marker != 'O' && marker != 'o');

        System.out.println("\nPlayer   : '" + markChosen[0] + "'");
        System.out.println("Computer : '" + markChosen[1] + "'");
        System.out.println(("\nLet's start the game!"));

    }

    public static int resultCheck(int[] positionMarked) {

        int count_O = 0;
        int count_X = 0;
        //k = i * 3 is because the starting number for rows will be 0 (1), 3 (4), 6 (7).
        //The end number for each row will be 2 (3), 5 (6), 8 (9).
            /*
            E.g.
            k = 0; k <= 2, k++
            It means position of 0 (1), 1 (2) and 2 (3) will be checked
            k = 3; k <= 5, k++
            It means position of 3 (4), 4 (5) and 5 (6) will be checked
            */
        //Check for ROWS position
        for (int i = 0; i < 3; i++) {
            for (int k = i * 3; k <= (i * 3 + 2); k++) {
                if (positionMarked[k] != -1) {
                    if (positionMarked[k] == 0){
                        count_O++;
                    }
                    else {
                        count_X++;
                    }
                }
                else {
                    break;
                }
            }

            //If count_O reach 3, it means computer win hence direct return 0 and won't run anymore for this method.
            if (count_O == 3) {
                return 0;
            }
            //If count_X reach 3, it means player win hence direct return 1 and won't run anymore for this method.
            else if (count_X == 3) {
                return 1;
            }
            //If count_O and count_X doesn't reach 3, they will set to 0 again.
            else {
                count_O = 0;
                count_X = 0;
            }
        }

        //Check for COLUMNS position
        for (int i = 0; i < 3; i++){

            //position in array (position on board)
            //k = i is because the starting number for each column will be 0 (1), 1 (2), 2 (3).
            //k <= i + 6, the answer of k will be the end position of each column.
            //k+=3, the answer of k will be the position that need to check.
            //The end number for each column will be 6 (7), 7 (8), 8 (9).
            /*
            E.g.
            k = 0; k <= 6, k+=3
            It means position of 0, 3 and 6 will be checked
            */
            for(int k = i; k <= i + 6; k+=3){
                if (positionMarked[k] != -1) {
                    if (positionMarked[k] == 0){
                        count_O++;
                    }
                    else {
                        count_X++;
                    }
                }
                else {
                    break;
                }
            }
            if (count_O == 3) {
                return 0;
            }
            else if (count_X == 3) {
                return 1;
            }
            else {
                count_O = 0;
                count_X = 0;
            }
        }

        //Check for 1st DIAGONAL position (1, 5, 9)
        //i = 0 is because the starting number for the first diagonal pattern will be 0 (1).
        //i <= 8, the answer of i will be the end position of first diagonal pattern.
        //i+=4, the answer of i will be the position that need to check.
        //The position that need to be checked for first diagonal pattern will be 0 (1), 4 (5), 8 (9).
            /*
            E.g.
            i = 0; i <= 8, i+=4
            It means position of 0 (1), 4 (5) and 8 (9) will be checked
            */
        for (int i = 0; i <= 8; i+=4){
            if (positionMarked[i] == 0) {
                count_O++;
            }
            else if (positionMarked[i] == 1) {
                count_X++;
            }
            else {
                break;
            }
        }
        if (count_O == 3) {
            return 0;
        } else if (count_X == 3) {
            return 1;
        } else {
            count_O = 0;
            count_X = 0;
        }

        //Check for 2nd DIAGONAL position (3, 5, 7)
        //i = 2 is because the starting number for the second diagonal pattern will be 2 (3).
        //i <= 6, the answer of i will be the end position of second diagonal pattern.
        //i+=2, the answer of i will be the position that need to check.
        //The position that need to be checked for first diagonal pattern will be 2 (3), 4 (5), 6 (7).
            /*
            E.g.
            i = 2; i <= 6, i+=2
            It means position of 2 (3), 4 (5) AND 6 (7) will be checked
            */
        for (int i = 2; i <= 6; i+=2){
            if (positionMarked[i] == 0) {
                count_O++;
            }
            else if (positionMarked[i] == 1) {
                count_X++;
            }
            else {
                break;
            }
        }
        if (count_O == 3) {
            return 0;
        } else if (count_X == 3) {
            return 1;
        } else {
            return -1;
        }

    }

    public static int playerPlay(int[] positionMarked){

        boolean playerNumCheck = true;
        boolean positionError;
        int playerNum = 0;

        Scanner positionInput = new Scanner(System.in);

        do{
            do {
                try {
                    System.out.print("\nChoose the position (1 - 9): ");
                    playerNum = positionInput.nextInt();

                    playerNumCheck = false;
                }
                catch (Exception ex){
                    System.out.println("ERROR: Please Enter Number In Range (1 - 9) only!");
                    positionInput.nextLine();
                }
            }while (playerNumCheck);

            //Check whether the position has been placed by calling positionCheck() function with playerNum and positionMarked as parameter
            positionError = positionCheck(playerNum, positionMarked);

        } while (!positionError);

        return playerNum;
    }

    public static int computerPlay(int[] positionMarked){

        final int MAX = 9;
        final int MIN = 1;

        boolean compNumCheck;
        int compNum;

        System.out.println("\nComputer turn...");

        do {
            compNumCheck = true;

            //Generate random number between 1 and 9
            compNum = MIN + (int) (Math.random() * MAX);

            //If generated number which is the index of the array is not -1, computer has to generate again
            if (positionMarked[compNum - 1] != -1){
                compNumCheck = false;
            }

        }while (!compNumCheck);

        return compNum;
    }

    public static boolean positionCheck(int numInput, int[] positionMarked){

        if (numInput < 1 || numInput > 9){
            System.out.println("ERROR: The position must be chosen between 0 and 9!");
            return false;
        }
        else if (positionMarked[numInput-1] != -1){
            System.out.println("ERROR: The position has been chosen! Choose another number!");
            return false;
        }
        else {
            return true;
        }
    }

    public static void boardDisplay (int[] positionMarked, char[] markChosen){

        String PATTERN_1 = "  " + "=".repeat(13);
        String PATTERN_2 = "  " + "-".repeat(13);

        System.out.println();
        System.out.println(PATTERN_1 + "\n   TIC-TAE-TOE \n" + PATTERN_1);
        for(int i = 0; i < positionMarked.length; i ++){
            if (i == 0 || i == 3 || i == 6){
                System.out.print("  ");
            }
            System.out.print("| ");

            if (positionMarked[i] == 1){
                System.out.print(markChosen[0]);
            }
            else if (positionMarked[i] == 0) {
                System.out.print(markChosen[1]);
            }
            else {
                System.out.print(i+1);
            }
            System.out.print(" ");
            if (i == 2 || i == 5 || i == 8){
                System.out.println("|");
                System.out.println(PATTERN_2);
            }
        }
    }

    //Flip and match
    public static void FlipAndMatch(){
        //Use Alt + Tab to use the JOptionPane if necessary
        //Scroll the console to the most bottom so that it can be "refreshed"
        int option = JOptionPane.showConfirmDialog(null, "Do you want to start now?", "Welcome to Flip and Match", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.NO_OPTION){
            String[] args = {""};
            main(args);
        }
        String[][] board = new String[4][5];
        String[][] index = new String[4][5];
        ArrayList<String> correctChar = new ArrayList<>();
        ArrayList<String> usedPos = new ArrayList<>();


        //initialize index or position
        int posIndex = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                index[i][j] = String.valueOf(posIndex);
                posIndex++;
            }
        }

        //character list
        char boardChar1 = 'A';
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = String.valueOf(boardChar1);
                boardChar1++;
            }
        }
        char boardChar2 = 'A';
        for (int i = 2; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = String.valueOf(boardChar2);
                boardChar2++;
            }
        }

        //randomise the character position
        Random random = new Random();
        for (int i = board.length - 1; i > 0; i--) {
            for (int j = board[i].length - 1; j > 0; j--) {
                int m = random.nextInt(i + 1);
                int n = random.nextInt(j + 1);
                String temp = board[i][j];
                board[i][j] = board[m][n];
                board[m][n] = temp;
            }
        }
        inGame(index, correctChar, board, usedPos);
    }

    public static void inGame(String[][] index, ArrayList<String> correctChar, String[][] board, ArrayList<String> usedPos){
        //display index position or grid
        gridLine();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.printf("%9s", index[i][j]);
            }
            System.out.println("\n");
        }
        gridLine();
        while(correctChar.size() != 10){
            String position1, position2;

            try{
                position1 = JOptionPane.showInputDialog("Enter position 1");
                pos1Validate(position1, index, board, correctChar, usedPos);

                position2 = JOptionPane.showInputDialog("Enter position 2");
                pos2Validate(position2, index, board, correctChar, usedPos);

                if (Objects.equals(position1, position2)){
                    JOptionPane.showMessageDialog(null, "Cannot be same positions!", "Invalid", JOptionPane.WARNING_MESSAGE);
                    inGame(index, correctChar, board, usedPos);
                }

                //match the user position with the board characters
                String char1 = pos1(position1, board);
                String char2 = pos2(position2, board);

                if (Objects.equals(char1, char2)) {
                    correctChar.add(char1);
                    usedPos.add(position1);
                    usedPos.add(position2);

                    if(correctChar.size() == 10){
                        JOptionPane.showMessageDialog(null, "Yay! You have finished the game!","Congratulations", JOptionPane.WARNING_MESSAGE);
                        correctResult(char1, index, position1, position2);
                        int option = JOptionPane.showConfirmDialog(null, "Do you want to start a new game?", "Continue", JOptionPane.YES_NO_OPTION);
                        if(option == JOptionPane.YES_OPTION){
                            FlipAndMatch(); //new game
                        }
                        else if(option == JOptionPane.NO_OPTION){
                            String[] args = {""};
                            main(args);
                        }
                    }
                    //display the position index again but flipped the correct answer cards
                    correctResult(char1, index, position1, position2);
                }
                else {
                    //display the position index again but flipped the correct answer cards
                    wrongResult(char1, char2, index, position1, position2);
                }
            }
            catch (NullPointerException e){
                int option = JOptionPane.showConfirmDialog(null, "Are you sure want to quit the game?", "Back to menu", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION){
                    String[] args = {""};
                    main(args);
                }
            }
            catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "The position must be number!", "Invalid", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    public static String pos1(String position1, String[][] board){ //input matching purpose
        switch (position1) {
            case "1":
                position1 = board[0][0];
                break;
            case "2":
                position1 = board[0][1];
                break;
            case "3":
                position1 = board[0][2];
                break;
            case "4":
                position1 = board[0][3];
                break;
            case "5":
                position1 = board[0][4];
                break;
            case "6":
                position1 = board[1][0];
                break;
            case "7":
                position1 = board[1][1];
                break;
            case "8":
                position1 = board[1][2];
                break;
            case "9":
                position1 = board[1][3];
                break;
            case "10":
                position1 = board[1][4];
                break;
            case "11":
                position1 = board[2][0];
                break;
            case "12":
                position1 = board[2][1];
                break;
            case "13":
                position1 = board[2][2];
                break;
            case "14":
                position1 = board[2][3];
                break;
            case "15":
                position1 = board[2][4];
                break;
            case "16":
                position1 = board[3][0];
                break;
            case "17":
                position1 = board[3][1];
                break;
            case "18":
                position1 = board[3][2];
                break;
            case "19":
                position1 = board[3][3];
                break;
            case "20":
                position1 = board[3][4];
                break;
        }

        return position1;
    }

    public static String pos2(String position2, String[][] board){
        switch (position2) {
            case "1":
                position2 = board[0][0];
                break;
            case "2":
                position2 = board[0][1];
                break;
            case "3":
                position2 = board[0][2];
                break;
            case "4":
                position2 = board[0][3];
                break;
            case "5":
                position2 = board[0][4];
                break;
            case "6":
                position2 = board[1][0];
                break;
            case "7":
                position2 = board[1][1];
                break;
            case "8":
                position2 = board[1][2];
                break;
            case "9":
                position2 = board[1][3];
                break;
            case "10":
                position2 = board[1][4];
                break;
            case "11":
                position2 = board[2][0];
                break;
            case "12":
                position2 = board[2][1];
                break;
            case "13":
                position2 = board[2][2];
                break;
            case "14":
                position2 = board[2][3];
                break;
            case "15":
                position2 = board[2][4];
                break;
            case "16":
                position2 = board[3][0];
                break;
            case "17":
                position2 = board[3][1];
                break;
            case "18":
                position2 = board[3][2];
                break;
            case "19":
                position2 = board[3][3];
                break;
            case "20":
                position2 = board[3][4];
                break;
        }
        return position2;
    }

    public static void wrongResult(String char1, String char2, String[][] index, String position1, String position2) {
        gridLine();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if(index[i][j].equals(position1)){//assign the character to the index and display to the user
                    index[i][j] = char1;
                }
                if(index[i][j].equals(position2)){
                    index[i][j] = char2;
                }
                System.out.printf("%9s", index[i][j]);
            }
            System.out.println("\n");
        }
        gridLine();
        int option = JOptionPane.showConfirmDialog(null, "Continue?", "Incorrect answer", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION){
            gridLine();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    if(Objects.equals(index[i][j], char1)){ //assign back the index number and display it, some sort like closing the card
                        index[i][j] = position1;
                    }
                    if(Objects.equals(index[i][j], char2)){
                        index[i][j] = position2;
                    }
                    System.out.printf("%9s", index[i][j]);
                }
                System.out.println("\n");
            }
            gridLine();
        }
    }

    public static void correctResult(String matched, String[][] index,  String position1, String position2) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (index[i][j].equals(position1)){
                    index[i][j] = matched; //assign matched character to the correct position
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (index[i][j].equals(position2)){
                    index[i][j] = matched;
                }
            }
        }

        gridLine();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.printf("%9s", index[i][j]);
            }
            System.out.println("\n");
        }
        gridLine();
    }

    public static void pos1Validate(String p1, String[][] index, String[][] board, ArrayList<String> correctChar, ArrayList<String> usedPos){
        if (p1.length() == 0){ //no input from the user
            JOptionPane.showMessageDialog(null, "Please enter the position!", "Invalid", JOptionPane.WARNING_MESSAGE);
            inGame(index, correctChar, board, usedPos);
        }
        for (String i: usedPos) { //make sure the position is not the guessed position
            if (Objects.equals(i, p1)){
                JOptionPane.showMessageDialog(null, "This position has guessed correct already!", "Invalid", JOptionPane.WARNING_MESSAGE);
                inGame(index, correctChar, board, usedPos);
            }
        }
        if(!(Integer.parseInt(p1) >= 1 && (Integer.parseInt(p1) <= 20))){ //ensure the position in the range
            JOptionPane.showMessageDialog(null, "The position is out of range!","Invalid", JOptionPane.WARNING_MESSAGE);
            inGame(index, correctChar, board, usedPos);
        }
    }


    public static void pos2Validate(String p2, String[][] index, String[][] board, ArrayList<String> correctChar, ArrayList<String> usedPos){
        if (p2.length() == 0){
            JOptionPane.showMessageDialog(null, "Please enter the position!", "Invalid", JOptionPane.WARNING_MESSAGE);
            inGame(index, correctChar, board, usedPos);
        }
        for (String i: usedPos) {
            if (Objects.equals(i, p2)){
                JOptionPane.showMessageDialog(null, "This position has guessed correct already!", "Invalid", JOptionPane.WARNING_MESSAGE);
                inGame(index, correctChar, board, usedPos);
            }
        }
        if(!(Integer.parseInt(p2) >= 1 && (Integer.parseInt(p2) <= 20))){
            JOptionPane.showMessageDialog(null, "The position is out of range!","Invalid", JOptionPane.WARNING_MESSAGE);
            inGame(index, correctChar, board, usedPos);
        }
    }

    public static void gridLine(){
        System.out.println("\n\n\n---------------------------------------------------\n\n\n");
    }







    //Five dice
    public static  void fiveDice() {

        Scanner scanner = new Scanner(System.in);

        //Declare variable and arrays
        //Use 2D arrays because there are 10 rounds in each game
        //and 5 numbers generated in each round
        int[][] userDice = new int[10][5];
        int[][] computerDice = new int[10][5];
        int userWonCount = 0;
        int compWonCount = 0;
        int tieCount = 0;

        int[][] diceNumOccurUser = new int[10][6];
        int[][] diceNumOccurComp = new int[10][6];
        char[] gameStatus = new char[10];
        int userIndex =0;
        int compIndex =0;

        String continueStatus;


        displayGameRules();
        System.out.print("So, would you like to start the game? (Y-yes /N-no): ");
        continueStatus = scanner.nextLine();
        continueStatus = checkInput(continueStatus);

        if (continueStatus.equals("Y")){
            do {
                //Call the function to generate the random dice numbers
                generateRandomDiceNumber(userDice, computerDice);



                //Call the function to find the highest occurrence of number
                //by using the generated dice number as the index
                //to store in the new array
                storeOccurNumber(userDice, computerDice, diceNumOccurUser, diceNumOccurComp);


                //Decide who is the winner        //Find the highest number of occurrence to determine the winner
                //Do the increment to number of user win, computer win or tie
                int highestUser;
                int highestComp;
                System.out.println();
                System.out.println();
                System.out.println();
                try{
                    for (int i = 0; i < 10; i++) {
                        //Sort the arrays elements to make the finding winner process later to be easier if the number of occurrence between
                        //the user and the computer is the same
                        Arrays.sort(userDice[i]);
                        Arrays.sort(computerDice[i]);

                        highestUser = diceNumOccurUser[i][0];
                        highestComp = diceNumOccurComp[i][0];
                        System.out.println("Round " + (i+1)  + " :");
                        //Display user's five dice
                        System.out.print("User's five dice = ");
                        System.out.print("{ ");
                        //Call the function to call each die value and also get the highest value for the user
                        highestUser = eachRoundValues(i, highestUser, userDice, diceNumOccurUser);
                        System.out.println("The combination of result: "+ highestUser);

                        //Display computer's five dice
                        System.out.print("Computer's five dice = ");
                        System.out.print("{ ");
                        //Call the function to call each die value and also get the highest value for the computer
                        highestComp = eachRoundValues(i, highestComp, computerDice, diceNumOccurComp);
                        System.out.println("The combination of result: "+ highestComp);

                        //Determine the winner and do the increment to the winner or to the tie count if the game is tie
                        if (highestUser > highestComp) {
                            System.out.println(" < User wins >");
                            gameStatus[i] = 'U';
                            userWonCount++;
                        }
                        else if (highestUser < highestComp) {
                            System.out.println(" < Computer wins >");
                            gameStatus[i] = 'C';
                            compWonCount++;
                        }
                        else {
                            if (highestUser == 1){
                                System.out.println(" < Tie game >");
                                gameStatus[i] = 'T';
                                tieCount++;
                            }
                            else{
                                //Find the index of the number to see which the same numbers are higher
                                for (int j = 0; j < 6; j++){
                                    if (diceNumOccurUser[i][j] == highestUser){
                                        userIndex = j;
                                    }
                                    if (diceNumOccurComp[i][j] == highestComp){
                                        compIndex = j;
                                    }
                                }
                                //For the same number of occurrence of dice number, if the dice number is higher, then it will become the winner
                                if (userIndex > compIndex){
                                    System.out.println(" < User wins >");
                                    gameStatus[i] = 'U';
                                    userWonCount++;
                                }
                                else if (userIndex < compIndex){
                                    System.out.println(" < Computer wins >");
                                    gameStatus[i] = 'C';
                                    compWonCount++;
                                }
                                else{
                                    System.out.println(" < Tie game >");
                                    gameStatus[i] = 'T';
                                    tieCount++;
                                }

                            }


                        }
                        System.out.println();

                        //A small function to temporary break each round and let the user to press Enter key to continue
                        System.out.println("Press Enter key to continue...");
                        String s = scanner.nextLine();
                    }
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    System.out.println("Exception caught! The index of array out of bounds exception.");
                }


                //Display the die values for both players in each round
                displayDieValue(userDice, computerDice, gameStatus);


                //Display number of times the user won, computer won and tie games
                String str = "=";
                System.out.println(str.repeat(55));
                System.out.println("The number of times the user won: " + userWonCount);
                System.out.println("The number of times the computer won: " + compWonCount);
                System.out.println("The number of tie games: " + tieCount);

                //Ask the user whether to continue 10 more rounds
                System.out.print("Would you like to continue the next 10 games? (Y-yes / N-no): ");
                continueStatus = scanner.nextLine();
                continueStatus = checkInput(continueStatus);

                //Clear the variables and array data by initializing them to 0
                userWonCount = 0 ;
                compWonCount = 0;
                tieCount = 0;
                //Call the function to clear the array element
                clearData(diceNumOccurUser, diceNumOccurComp);
            } while (continueStatus.equals("Y"));

            System.out.println("Thanks for playing this game. Goodbye.");
            System.out.println("Press Enter key to go back to Main menu ...");
            String s = scanner.nextLine();
        }
    }

    public static void generateRandomDiceNumber(int[][] user, int[][] computer){
        //Use try-catch to handler the exceptions if the array index out of bounds exception
        try{
            //Generate random five dice for the user and computer
            for (int i = 0; i < 10; i++){
                for (int  j = 0; j < 5; j++){
                    user[i][j] = 1 + (int)(random() * 6);
                    computer[i][j] = 1 + (int)(random() * 6 );
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Exception caught! The index of array out of bounds exception.");
        }

    }

    public static void storeOccurNumber(int[][] userDice, int[][] computerDice,
                                        int[][] diceNumOccurUser, int[][] diceNumOccurComp){
        //Store the number of occurrence of the dice number into an array by
        //using the generated number as the column index of the array
        int index;
        try{
            for (int i = 0; i < 10; i++){
                for (int  j = 0; j < 5; j++){
                    index = userDice[i][j];
                    diceNumOccurUser[i][index-1]++;

                    index = computerDice[i][j];
                    diceNumOccurComp[i][index-1]++;
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Exception caught! The index of array out of bounds exception.");
        }

    }

    public static int eachRoundValues(int i, int highest, int[][] dice, int[][] occurNum){
        try{
            for (int j = 0; j < 6; j++) {

                //Determine the highest number of occurrence in each row for user's dice
                if (occurNum[i][j] >= highest) {
                    highest = occurNum[i][j];
                }

                //Print the dice value
                //Use if-else to make sure the array index is not out of bound exception
                if (j != 5){
                    System.out.print(dice[i][j]);
                    if (j != 4){
                        System.out.print(", ");
                    }
                }
            }
            System.out.println(" }");

        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Exception caught! The index of array out of bounds exception.");
        }
        return highest;
    }

    public  static void displayDieValue(int[][] userDice, int[][] compDice, char[] status){
        String lineSign = "-";
        String equalSign = "=";
        System.out.println(equalSign.repeat(55));
        System.out.println("     The Die values for both players in each round");
        System.out.println(equalSign.repeat(55));
        System.out.printf("%6s %12s %23s %10s", "ROUND", "USER","COMPUTER", "RESULT");
        System.out.println();

        try{
            for (int i = 0; i < 10; i++){
                System.out.printf("%4s %6s", i+1," " );
                try{
                    for (int  j = 0; j < 5; j++){
                        System.out.print(userDice[i][j] + "  ");
                    }
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    System.out.println("Exception caught! The index of array out of bounds exception.");
                }

                System.out.printf("%6s", " ");

                try{
                    for (int  j = 0; j < 5; j++){
                        System.out.print(compDice[i][j] + "  ");
                    }
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    System.out.println("Exception caught! The index of array out of bounds exception.");
                }

                System.out.printf("%4c", status[i]);

                System.out.println();
                if (i==9){
                    break;
                }
                System.out.println(lineSign.repeat(55));
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Exception caught! The index of array out of bounds exception.");
        }

    }

    public static void displayGameRules(){
        String equalSign = "=";
        System.out.println(equalSign.repeat(90));
        System.out.printf("%60s", "Welcome to Five Dice Game\n");
        System.out.println(equalSign.repeat(90));
        System.out.println("Before start the game, you should know the following rules: ");
        System.out.println("- For each game start, five dice will be throw randomly to the user and the computer");
        System.out.println("- The following hierarchy of Die values decides the winner in each round ");
        System.out.println("   (any higher combination beats a lower one) ");
        System.out.println("- For example, five of a kind beats four of a kind");
        System.out.println("  ~ Five of a kind");
        System.out.println("  ~ Four of a kind");
        System.out.println("  ~ Three of a kind");
        System.out.println("  ~ A pair");
        System.out.println("* For the same pair of kind, the higher dice number will be the winner *");
        System.out.println("- After 10 complete rounds of the game, the following information will be shown : ");
        System.out.println("  ~ Die values of both players in each round");
        System.out.println("  ~ The number of times the user won");
        System.out.println("  ~ The number of times the computer won");
        System.out.println("  ~ The number of tie games");
        System.out.println("- After that, the user can choose either continue to the next 10 games");
        System.out.println(equalSign.repeat(90));
        System.out.println();
    }

    public  static String checkInput(String input){
        Scanner scanner = new Scanner(System.in);
        while (true){
            if (input.length() != 1){
                System.out.println("Incorrect input! Only 1 letter is required.");
            }
            else{
                char convertInput = Character.toUpperCase(input.charAt(0));
                if (Character.isDigit(convertInput)){
                    System.out.println("Incorrect input! Digit is not allowed to enter.");
                }
                else if (Character.isLetter(convertInput)){
                    if (convertInput != 'Y' && convertInput != 'N'){
                        System.out.println("Incorrect input! Only 'Y' or 'N' is accepted.");
                    }
                    else {
                        return Character.toString(convertInput);
                    }
                }
                else {
                    System.out.println("Incorrect input! Others symbol is not allowed.");
                }
            }
            System.out.print("Please enter again. ('Y' for YES or 'N' for No): ");
            input = scanner.nextLine();
        }
    }

    public static void clearData(int[][] userDiceOccur, int[][] compDiceOccur){
        //Initialize the elements in the array to 0;
        try{
            for (int i=0; i < 10; i++){
                for(int j = 0; j < 6; j++){
                    userDiceOccur[i][j] = 0;
                    compDiceOccur[i][j] = 0;
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Exception caught! The index of array out of bounds exception.");
        }

    }
}
