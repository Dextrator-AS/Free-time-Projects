/**********************************************************************************/
/* A. Schwarz 2024, basierend auf einer Idee und Vorlage von M.Plischke März 2011 */
/**********************************************************************************/
import java.util.Scanner;
import java.util.*;
import java.util.Random;
/*MP 3 sounds hinzufügen*/
public class DungeonsV4AS{
// Labyrinth erzeugen und füllen
  static int mazeSetupRows = 11, mazeSetupCols = 11;
  static char[ ][ ] mazeLayout = {
 {'#','a','a','a','a','a','a','a','a','a','#'},
 {'a','.','.','.','.','.','.','.','.','.','a'},
 {'a','.','.','#','#','d','#','d','#','.','a'},
 {'a','.','#','#','V',':','B',':','Q','#','#'},   
 {'a','.','#','S',':',':',':',':',':','#','#'},
 {'a','.','#',':',':',':',':',':',':','#','#'},
 {'a','.','d',':','H',':','H',':','H','#','#'},
 {'a','.','.','d','#','d','#','d','#','.','a'},
 {'a','.','.','.','.','.','.','.','.','.','a'},
 {'a','.','#','.','.','.','.','#','.','.','a'},
 {'#','a','#','a','a','a','a','a','a','a','#'}
 };
  
// Elemente im Labyrinth
  static char mazeSetupCharAisle = '.';
  static char mazeSetupCharExit = 'A';
  static char mazeSetupCharExitOption = 'a';
  static char mazeSetupCharBar = 'B';
  static char mazeSetupCharVillage = 'D';
  static char mazeSetupCharVillageDoorOption = 'd';
  static char mazeSetupCharHouseViktor = 'V';
  static char mazeSetupCharHouse = 'H';
  static char mazeSetupCharKey = 'K';
  static char mazeSetupCharMagician = 'M';
  static char mazeSetupCharNPC = 'T';
  static char mazeSetupCharShop = 'S';
  static char mazeSetupCharQuestGiver = 'Q';
  static char mazeSetupCharVillageAisle = ':';
  static char mazeSetupCharWall = '#';
  static int mazeSetupKeysToBePlaced = 1;
  static int mazeSetupNPCsToBePlaced = 1;
  static int mazeSetupMagicianLikelihood = 20;
  static int mazeSetupMagiciansToBePlaced = 1;
  static int mazeSetupVillageDoorsToBePlaced = 1;
  
// Texte für Ausrichtung des Spielers (0=N, 1=O, 2=S, 3=W)
  static String textOrientation[] ={"Norden", "Osten", "Süden", "Westen"};
    
// Abbildung des Laufwegs und der aktuellen Position / Ausrichtung
  static int pathSetupRows = mazeSetupRows * 2 + 1, pathSetupCols = mazeSetupCols * 2 + 1;
  static char[][] path = new char [pathSetupRows][pathSetupCols];
  static int pathCurrentRow, pathCurrentCol;
  static int pathInViewRow, pathInViewCol;
  static int pathOrientation = 0;
 
// Wo ist der Spieler, was hat er alles schon erreicht?  
  static int playerLocation = 1;
  static String playerName;
  static int playerHealth = 100;
  static int playerTurnsLeft, playerTurnsRight;
  static int playerCurrentRow, playerCurrentCol;
  static int playerInViewRow, playerInViewCol;
  static int playerOrientation;
  static boolean playerStatusEasterEggFound = false;
  static int playerStatusGameEnds = 0;
  static int playerStatusKey = 0;
  static int playerStatusMagician = 0;
  static int playerStatusNPC = 0;
  static int playerStatusViktor = 0;
  static boolean playerStatusVillageFound = false;
  static char playerSteppedOn = '.';
  
  static String textLevel[] = {"Das Labyrinth", "Das Dorf"};
  static Scanner console;
  
  public static void main (String[] args){
    boolean stepForward;
    String input;
    int row, col;

// Spielstart
    System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    System.out.println("DDDDDDDDDDDDD       UUUUUUUU     UUUUUUUUNNNNNNNN        NNNNNNNN        GGGGGGGGGGGGGEEEEEEEEEEEEEEEEEEEEEE     OOOOOOOOO     NNNNNNNN        NNNNNNNN   SSSSSSSSSSSSSSS ");
    System.out.println("D::::::::::::DDD    U::::::U     U::::::UN:::::::N       N::::::N     GGG::::::::::::GE::::::::::::::::::::E   OO:::::::::OO   N:::::::N       N::::::N SS:::::::::::::::S");
    System.out.println("D:::::::::::::::DD  U::::::U     U::::::UN::::::::N      N::::::N   GG:::::::::::::::GE::::::::::::::::::::E OO:::::::::::::OO N::::::::N      N::::::NS:::::SSSSSS::::::S");
    System.out.println("DDD:::::DDDDD:::::D UU:::::U     U:::::UUN:::::::::N     N::::::N  G:::::GGGGGGGG::::GEE::::::EEEEEEEEE::::EO:::::::OOO:::::::ON:::::::::N     N::::::NS:::::S     SSSSSSS");
    System.out.println("  D:::::D    D:::::D U:::::U     U:::::U N::::::::::N    N::::::N G:::::G       GGGGGG  E:::::E       EEEEEEO::::::O   O::::::ON::::::::::N    N::::::NS:::::S            ");
    System.out.println("  D:::::D     D:::::DU:::::D     D:::::U N:::::::::::N   N::::::NG:::::G                E:::::E             O:::::O     O:::::ON:::::::::::N   N::::::NS:::::S            ");
    System.out.println("  D:::::D     D:::::DU:::::D     D:::::U N:::::::N::::N  N::::::NG:::::G                E::::::EEEEEEEEEE   O:::::O     O:::::ON:::::::N::::N  N::::::N S::::SSSS         ");  
    System.out.println("  D:::::D     D:::::DU:::::D     D:::::U N::::::N N::::N N::::::NG:::::G    GGGGGGGGGG  E:::::::::::::::E   O:::::O     O:::::ON::::::N N::::N N::::::N  SS::::::SSSSS    "); 
    System.out.println("  D:::::D     D:::::DU:::::D     D:::::U N::::::N  N::::N:::::::NG:::::G    G::::::::G  E:::::::::::::::E   O:::::O     O:::::ON::::::N  N::::N:::::::N    SSS::::::::SS  "); 
    System.out.println("  D:::::D     D:::::DU:::::D     D:::::U N::::::N   N:::::::::::NG:::::G    GGGGG::::G  E::::::EEEEEEEEEE   O:::::O     O:::::ON::::::N   N:::::::::::N       SSSSSS::::S "); 
    System.out.println("  D:::::D     D:::::DU:::::D     D:::::U N::::::N    N::::::::::NG:::::G        G::::G  E:::::E             O:::::O     O:::::ON::::::N    N::::::::::N            S:::::S");
    System.out.println("  D:::::D    D:::::D U::::::U   U::::::U N::::::N     N:::::::::N G:::::G       G::::G  E:::::E       EEEEEEO::::::O   O::::::ON::::::N     N:::::::::N            S:::::S");
    System.out.println("DDD:::::DDDDD:::::D  U:::::::UUU:::::::U N::::::N      N::::::::N  G:::::GGGGGGGG::::GEE::::::EEEEEEEE:::::EO:::::::OOO:::::::ON::::::N      N::::::::NSSSSSSS     S:::::S");
    System.out.println("D:::::::::::::::DD    UU:::::::::::::UU  N::::::N       N:::::::N   GG:::::::::::::::GE::::::::::::::::::::E OO:::::::::::::OO N::::::N       N:::::::NS::::::SSSSSS:::::S");
    System.out.println("D::::::::::::DDD        UU:::::::::UU    N::::::N        N::::::N     GGG::::::GGG:::GE::::::::::::::::::::E   OO:::::::::OO   N::::::N        N::::::NS:::::::::::::::SS ");
    System.out.println("DDDDDDDDDDDDD             UUUUUUUUU      NNNNNNNN         NNNNNNN        GGGGGG   GGGGEEEEEEEEEEEEEEEEEEEEEE     OOOOOOOOO     NNNNNNNN         NNNNNNN SSSSSSSSSSSSSSS   ");                                             
    System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    System.out.println("Du weißt nicht wie du hier hin gekommen bist, du weißt nur noch, dass du im Himalaya wandern warst.");
    System.out.println("Du siehst ein Loch über dir, dort bist du wahrscheinlich hindurchgefallen. Du musst wohl oder übel einen anderen Weg heraus finden.");
    System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    
    console = new Scanner(System.in);
    System.out.print("Wie möchtest du genannt werden? ");
    playerName = console.nextLine();
    System.out.println("Verarbeite...");
    wait(2000);
    System.out.println("...");
    wait(2000);
    System.out.println("Initialisiere Spielstart...");
    System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
// Ermittelt zufällig, in welche Richtung der Spieler schaut nachdem er im Labyrinth aufgwacht ist 
// Im Uhrzeigersinn: 0 = Norden; 1 = Osten; 2 = Süden; 3 = Westen
    playerOrientation = (int)(Math.random() * 4);

// Für die Ausrichtung der Laufwegs-Matrix ist "oben" immer die Richtung in welche der Spieler nach dem Erscheinen schaut. 
// Der Laufweg wird zunächst komplett auf leer gesetzt. leere Zeilen werden später nicht angezeigt, um dem Spieler keine Orientierung an "Rändern" zu geben.    
// Der Spieler steht anfangs genau in der Mitte der Laufweg-Matrix (da er von dort dann in alle Richtungen über die Maximalwerte des Labyrinths laufen können muss).
    for (row=0; row<pathSetupRows; row++){
      for (col=0; col<pathSetupCols; col++){
        path[row][col] = ' ';
      }
    }   
    pathCurrentRow = mazeSetupRows + 1;
    pathCurrentCol = mazeSetupCols + 1;

// Spawnen des Spielers
    int countPlacements = 0;
    while (countPlacements < 1){ 
// Zufallszahl nur zwischen zweiter Zeile/Spalte und vorletzter Zeile/Spalte ermitteln, da spawnen in der Außenwand ohnehin nicht geht
      row = (int)(Math.random() * (mazeSetupRows - 2) + 1);
      col = (int)(Math.random() * (mazeSetupCols - 2) + 1);
      if (mazeLayout[row][col] == mazeSetupCharAisle){
        playerCurrentRow = row;
        playerCurrentCol = col;
        countPlacements = 1;
      } 
    } 

// Spawnen werden Keys gespawnt
    countPlacements = 0;
    while (countPlacements < mazeSetupKeysToBePlaced){ 
// Zufallszahl nur zwischen zweiter Zeile/Spalte und vorletzter Zeile/Spalte ermitteln, da spawnen in der Außenwand ohnehin nicht geht
      row = (int)(Math.random() * (mazeSetupRows - 2) + 1);
      col = (int)(Math.random() * (mazeSetupCols - 2) + 1);
      if (mazeLayout[row][col] == mazeSetupCharAisle){
        mazeLayout[row][col] = mazeSetupCharKey;
        countPlacements++;
      } 
    } 
    
// Spawnen der NPCs
    countPlacements = 0;
    while (countPlacements < mazeSetupNPCsToBePlaced){ 
// Zufallszahl nur zwischen zweiter Zeile/Spalte und vorletzter Zeile/Spalte ermitteln, da spawnen in der Außenwand ohnehin nicht geht
      row = (int)(Math.random() * (mazeSetupRows - 2) + 1);
      col = (int)(Math.random() * (mazeSetupCols - 2) + 1);
      if (mazeLayout[row][col] == mazeSetupCharAisle){
        mazeLayout[row][col] = mazeSetupCharNPC;
        countPlacements++;
      } 
    } 
    
// Wird der Erzmagier gespawnt, und wenn ja, wohin
    double randomProbability = Math.random()*100 + 1;
    if (randomProbability < mazeSetupMagicianLikelihood){ 
      countPlacements = 0;
      while (countPlacements < mazeSetupMagiciansToBePlaced){ 
// Zufallszahl nur zwischen zweiter Zeile/Spalte und vorletzter Zeile/Spalte ermitteln, da spawnen in der Außenwand ohnehin nicht geht
        row = (int)(Math.random() * (mazeSetupRows - 2) + 1);
        col = (int)(Math.random() * (mazeSetupCols - 2) + 1);
        if (mazeLayout[row][col] == mazeSetupCharAisle){
          mazeLayout[row][col] = mazeSetupCharMagician;
          countPlacements++;
        } 
      } 
    } 
    
// Spawnen des Eingangs des Dorfes
    countPlacements = 0;
    while (countPlacements < mazeSetupVillageDoorsToBePlaced){ 
      row = (int)(Math.random() * (mazeSetupRows - 2) + 1);
      col = (int)(Math.random() * (mazeSetupCols - 2) + 1);
      if (mazeLayout[row][col] == mazeSetupCharVillageDoorOption){
        mazeLayout[row][col] = mazeSetupCharVillage;
        countPlacements++;
      } 
    } 
    
// Spawnen des Ausgangs
    countPlacements = 0;
    while (countPlacements < 1){ 
      row = (int)(Math.random() * (mazeSetupRows));
      col = (int)(Math.random() * (mazeSetupCols));
      if (mazeLayout[row][col] == mazeSetupCharExitOption){
        mazeLayout[row][col] = mazeSetupCharExit;
        countPlacements = 1;
      } 
    }     
    
// Wandeln aller Wände die ein möglicher Ausgang gewesen wären zu festen Wänden
    for (row=0;row<mazeSetupRows;row++){
      for (col=0;col<mazeSetupCols;col++){
        if (mazeLayout[row][col] == mazeSetupCharExitOption){
          mazeLayout[row][col] = mazeSetupCharWall;
        }
      }
    }
    
// Wandeln aller Wände die ein möglicher Dorfeingang gewesen wären zu festen Wänden
//&&&&&& Nachfolgenden Kommentar hattest Du nur gelöscht, aber nicht verarbeitet
//§§§§ Auch hier könnte man optimieren, da die möglichen Dorfeingänge nie am Rand sein können
    for (row=0;row<mazeSetupRows;row++){
      for (col=0;col<mazeSetupCols;col++){
        if (mazeLayout[row][col] == mazeSetupCharVillageDoorOption){
          mazeLayout[row][col] = mazeSetupCharWall;
        }
      }
    }    
    
    while(playerStatusGameEnds == 0){

// Ermittlung der Koordinate im Labyrinth auf die der Spieler schaut
      if (playerOrientation == 0){          // Norden
        playerInViewCol = playerCurrentCol;
        playerInViewRow = playerCurrentRow-1;
      } 
      else if (playerOrientation == 1){    // Osten
        playerInViewCol = playerCurrentCol+1;
        playerInViewRow = playerCurrentRow;
      } 
      else if (playerOrientation == 2){   // Süden
        playerInViewCol = playerCurrentCol;
        playerInViewRow = playerCurrentRow+1;
      } 
      else if (playerOrientation == 3){  // Westen
        playerInViewCol = playerCurrentCol-1; 
        playerInViewRow = playerCurrentRow;
      } 

// Ermittlung der Koordinate im Laufweg auf die der Spieler schaut    
      if (pathOrientation == 0){         // Norden
        pathInViewRow = pathCurrentRow - 1;
        pathInViewCol = pathCurrentCol;
      } 
      else if (pathOrientation == 1){   // Osten
        pathInViewRow = pathCurrentRow;
        pathInViewCol = pathCurrentCol + 1;
      } 
      else if (pathOrientation == 2){   // Süden
        pathInViewRow = pathCurrentRow + 1;
        pathInViewCol = pathCurrentCol;
      } 
      else if (pathOrientation == 3){   // Westen
        pathInViewRow = pathCurrentRow;
        pathInViewCol = pathCurrentCol - 1;
      } 

// In den Laufweg wird aufgenommen, was der Spieler vor sich sieht      
      path[pathInViewRow][pathInViewCol] = mazeLayout[playerInViewRow][playerInViewCol];
      
// Grafische Ausgabe dessen, was der Spieler im Feld vor sich sieht      
      if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharWall){
        looksAtWall(); 
      } 
      else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharAisle){
        looksAtHallway(); 
      } 
      else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharKey){
        looksAtKey(); 
      } 
      else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharExit){
        looksAtExit(); 
      } 
      else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharNPC){
        looksAtNPC();       
        talkToNPC();
      } 
      else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharVillage){
        looksAtVillageDoor();
      } 
      else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharMagician){
        looksAtMagician();
        talkToMagician();
      } 
      else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharVillageAisle){
//bleibt aus, da zu nervig für den Spieler
      } 
      else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharHouseViktor){
        looksAtHouse();
      } 
      else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharBar){
        looksAtBar();
      } 
      else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharHouse){
        //kommt noch
      } 
      else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharQuestGiver){
        //kommt noch
      } 
      else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharShop){
        //kommt noch
      } 
      else{
        System.out.println("In Zeile " + playerInViewRow + " und Spalte " + playerInViewCol + " steht das Zeichen " + mazeLayout[playerInViewRow][playerInViewCol]);
        System.out.println("Das gehört dort nicht hin.");
        playerStatusGameEnds = 99;
      }  
// Zeige an der Stelle des Spielers seine Ausrichtung an, und ersetze das nach der Ausgabe des Laufwegs die Ausrichtung durch einen breadcrumb
      if (pathOrientation == 0){
        path[pathCurrentRow][pathCurrentCol] = '^';
      }
      else if (pathOrientation == 1){
        path[pathCurrentRow][pathCurrentCol] = '>';
      }
      else if (pathOrientation == 2){
        path[pathCurrentRow][pathCurrentCol] = 'v';
      }
      else{
        path[pathCurrentRow][pathCurrentCol] = '<';
      }
      pathOutput();
      path[pathCurrentRow][pathCurrentCol] = playerSteppedOn;

// Frage was der Spieler machen möchte
      System.out.print("Wohin willst du dich drehen/dich bewegen? ");
      input = console.nextLine();
//!!!! Kommt noch/ gerade in Arbeit     §§§§ Auf erlaubte Eingaben muss noch abgefragt werden

   
// Eingabe: Einen Schritt nach vorne gehen
      if (input.equals("w")){
        stepForward = false;
        if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharWall){
          playerHealth--;
          if (playerHealth == 0){
            playerStatusGameEnds = 2;
          } 
          else {
            System.out.println("Du bist in eine Wand gelaufen, deine Nase leidet darunter.");
          } 
        }
        else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharExit){
          if (playerStatusKey < 2){
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
            System.out.println("DU HAST DEN AUSGANG gefunden...");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
            looksAtDoorLock();
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
            System.out.println("...der richtige Schlüssel fehlt jedoch");
          }
          else{
            System.out.println("DU HAST DEN AUSGANG und beide Schlüssel GEFUNDEN!!!");
            playerStatusGameEnds = 88;
          }
        } 
        else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharHouseViktor){
                 
          String goToHouse;
          
          goToHouse = console.nextLine();
//&&&&&&&&& wie oben. Du hast auch hier die zweite Lauflogik nicht beseitigt, sonden nur die Abfrage geändert. Auch das führt zu einem Unterschied zwischen Laufweg und Position, denn da ich ein w eingegeben habe, erwarte ich jetzt eigentlich, im Haus zu stehen
//&&&&&&&&&& ausserdem bin ich völlig verwirrt als Spieler, weil ich nicht mal zu einer Eingabe aufgefordert werde. Das Spiel scheint zu hängen             
          if (goToHouse.equals("w")){
            System.out.println("Du schaust dir das Schild an");
            System.out.println("-----------------------------------------");
            looksAtSign(); 
            stepForward = true;
          } // end of if
          else {
            System.out.println("Du ignorierst das Haus vorerst");
            System.out.println("-----------------------------------------");    
          } // end of if
        }
        else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharBar){      
          String goIntoBar;
          if (playerStatusNPC > 0){
            System.out.println("Dies muss die Bar sein, über die Tiberius sprach. Willst du eintreten?");
          } // end of if
          else {
            System.out.println("Das ist nun das letzte, was du erwartet hast - eine Bar hier unten??");
          } // end of if-else
          goIntoBar = console.nextLine();
//&&&&&&&&&& Das löst das Problem des Laufens an verschiedenen Stellen auch nicht. Es sich einfach zu machen bedeutet nicht dass man das Programm dadurch besser macht. Das was ich im Laufweg sehe entspricht so nicht mehr den bBewegungen die ich per Tastatureingabe gemacht habe
//&&&&&&&&&&& ich bin maximal verwirrt. Ich habe ja geantwortet, betrete die Bar aber nicht. Wie gesagt, diese Art der Fortbewegung ruiniert die Programmlogik        
          if (goIntoBar.equals("w")){
            System.out.println("Du betrittst die sehr vornehm aussehende Bar");
            System.out.println("--------------------------------------------");
            insideBarInVillage();
            wait(4000); 
            if (playerStatusViktor == 0){
//&&&&&&&& und auch hier hast Du das Problem nicht gelöst. Du kannst mit Viktor sprechen und er redet über Tiberius auch dann wenn Du Tiberius noch nicht gesehen hast. Manchmal muss man eine lieb gewonnene Logik doch komplett zerlegen um ein Problem zu lösen          
              talkToViktor();
              playerStatusViktor++;
            } // end of if         
            else {
              System.out.println("Viktor scheint gegangen zu sein. Ob du ihn wieder sehen wirst?");
              wait(3000);
            } // end of if-else
          } 
          else {
            System.out.println("Du betrittst die Bar vorerst nicht");
            System.out.println("----------------------------------");    
          } // end of if 
        }           
        else if (mazeLayout[playerInViewRow][playerInViewCol] == mazeSetupCharVillage){
          if (playerStatusKey < 1){
            looksAtDoorLock();
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Der Schlüssel fehlt...");  
          }
          else{
            //villageBeforePlayer();
            System.out.println("Das Tor öffnet sich und du trittst ein");
            looksAtAisle();
            playerStatusVillageFound = true;
            stepForward = true;
          }  
        } // end of if-else
        else{
          stepForward = true;
        }
        
        if (stepForward){
          playerCurrentCol = playerInViewCol;
          playerCurrentRow = playerInViewRow;
          pathCurrentRow = pathInViewRow;
          pathCurrentCol = pathInViewCol;

// Geht der Spieler mit dem Schritt durch die Tür zum Dorf?
          if (mazeLayout[playerCurrentRow][playerCurrentCol] == mazeSetupCharVillage){
//!!!Wird noch überarbeitet. Wenn ich auf das Feld mit der Tür gehe, dann erhöht sich der Zähler. Drehe ich mich aber wieder um (also gehe nicht in das Dorf) dann bleibt der Zähler auf 2. Eine bessere Lösung muss her
            if (playerLocation == 2){
              playerLocation--;  
            } // end of if 
            else {
              playerLocation++;
            } // end of if-else
          }
          
// Nimmt der Spieler mit dem Schritt einen Schlüssel auf?          
          if (mazeLayout[playerCurrentRow][playerCurrentCol] == mazeSetupCharKey){
            if (playerStatusKey < 2){
              playerStatusKey++;
              mazeLayout[playerCurrentRow][playerCurrentCol] = '.';
              System.out.println("Du hast das Item SCHLÜSSEL aufgenommen.");
            } // end of if
            else {
              System.out.println("Du hebst den vor dir liegenden Schlüssel nicht auf, da du schon beide benötigten Schlüssel hast.");
            } // end of if-else
          }

// Merken auf welches Symbol der Spieler mit dem Schritt gelaufen ist, dieses wird später im Laufweg erscheinen 
          playerSteppedOn = mazeLayout[playerCurrentRow][playerCurrentCol];
          
// Bei einem normalen Schritt vorwärts wird der Easter-Egg Counter zurückgesetzt          
          playerTurnsLeft = 0;
          playerTurnsRight = 0;
        
        }  // end of stepForward
      
      } // end of "w"
      
// Eingabe: Nach links drehen
      if (input.equals("a")){
        playerOrientation--;
        if (playerOrientation < 0){
          playerOrientation = 3;
        }
        pathOrientation--;
        if (pathOrientation < 0){
          pathOrientation = 3;
        }

// Easter Egg: Wer sich zwei mal im Kreis dreht, sieht das Labyrinth von oben
        playerTurnsLeft++;
        if (playerTurnsLeft == 1){
          playerTurnsRight = 0;
        }
        else if (playerTurnsLeft == 8){
          mazeLayout[playerCurrentRow][playerCurrentCol] = playerName.charAt(0);
          mazeOutput();
          mazeLayout[playerCurrentRow][playerCurrentCol] ='.';
          playerTurnsLeft = 0;
          playerStatusEasterEggFound = true;
        }
      } // end of "a"
      
// Eingabe: Nach Rechts drehen
      if (input.equals("d")){
        playerOrientation++;
        if (playerOrientation > 3){
          playerOrientation = 0;
        }
        pathOrientation++;
        if (pathOrientation > 3){
          pathOrientation = 0;
        }

// Easter Egg: Wer sich zwei mal im Kreis dreht, sieht das Labyrinth von oben
        playerTurnsRight++;
        if (playerTurnsRight == 1){
          playerTurnsLeft = 0;
        }
        else if (playerTurnsRight == 8){
          mazeLayout[playerCurrentRow][playerCurrentCol] = playerName.charAt(0);
          mazeOutput();
          mazeLayout[playerCurrentRow][playerCurrentCol] ='.';
          playerTurnsRight = 0;
          playerStatusEasterEggFound = true;
        }
      } // end of "d"
    } // end of while (playerStatusGameEnds == 0)

// Auswertung des Spielergebnisses   
    if (playerStatusGameEnds == 1){ // Tod durch Viktor
      gameEnding1();
    } 
    else if (playerStatusGameEnds == 2){ // Tod durch wiederholtes Laufen gegen die Wand
      gameEnding2();
    }  
    else if (playerStatusGameEnds == 88){ // Ausgang gefunden
      gameEnding3();
    }
    else if (playerStatusGameEnds == 99){ // Programabbruch wegen erkanntem Fehler im Programm
      System.out.println("Bitte verständige den Programmierer. Danke!");
      System.out.println("Der Programmierer entschuldigt sich hiermit in aller Form für das enttäuschende Spielerlebnis.");
    }

  } // end of main
      
  static private void talkToNPC(){
    String answerNPC;  
    if (playerStatusNPC > 1){
      System.out.println("Ist noch was? Ich habe Dir doch schon alles gesagt...");
      } 
    else if (playerStatusNPC < 2){
//&&&&&&&&& stimmt so nicht. Wenn ich Tiberius ein zweites mal ansehen, behauptet er immer noch, mich nie erblickt zu haben
      System.out.println("Ich bin Tiberius, der Weise. Dich haben meine Augen noch nie erblickt. Weißt du, wo du hier bist?");
      System.out.println("(1)ja");
      System.out.println("(2)nein");
      answerNPC = console.nextLine();
      if (answerNPC.equals("2")){
        System.out.println("Warum kommt man denn hier her wenn man keinen Plan hat? Naja egal. Du bist hier in Graceland, naja in dem magischen Wald zumindest. Hier gibts irgendwo nen Ausgang, der geht aber nur mit dem richtigen Schlüssel auf.");
//&&&&&&&&&&&&&&&& Wie soll der Goldfang denn heißen? Konsistenz wäre gut
        System.out.println("Da kann dir Hektor Goldfang helfen, der ist meistens am Saufen in der Kneipe im Dorf. Viel Spass beim Suchen, " + playerName + ".");
        System.out.println("(Du fragst dich, woher dieser Mann deinen Namen kennt)");
        System.out.println("(1)Woher kennst du meinen Namen?");
        System.out.println("(2)Bis dann");
        answerNPC = console.nextLine();
        if (answerNPC.equals("1")){
          System.out.println("Ich weiß so einiges. Wirst du noch sehen.");
          System.out.println("-----------------------------------------");
        } // end of if
        else if(answerNPC.equals("2")){
          System.out.println("Bis sehr bald, mein Freund.");
          System.out.println("-----------------------------------------");
        } // end of if
      } // end of if
      else if (answerNPC.equals("1")){
        System.out.println("Ach ist das so? Gut, dann muss ich den üblichen Scheiß nicht erklären. Bis bald, " +playerName+ ".");
        System.out.println("(Du fragst dich, woher dieser Mann deinen Namen kennt)");
        System.out.println("(1)Woher kennst du meinen Namen?");
        System.out.println("(2)Bis dann");
        answerNPC = console.nextLine();
        if (answerNPC.equals("1")){
          System.out.println("Ich weiß so einiges. Wirst du noch sehen.");
        } // end of if
        if(answerNPC.equals("2")){
          System.out.println("Bis sehr bald, mein Freund.");
        } // end of if
      }
    playerStatusNPC++;
    }
  }
  
  static private void talkToViktor(){
    playerStatusViktor++;
    String talkToViktor;
    System.out.println("Auf einmal steht ein seltsamer Mann vor dir, möglicherweise Goldfang?");
    System.out.println("-----------------------------------------------------------------------");
    looksAtViktor(); 
    System.out.println("Wer bist du denn? Siehst aus wie der letzte Penner.");
    System.out.println("(1)Wie war das?");
    System.out.println("(2)Heißt du Goldfang?");
    talkToViktor = console.nextLine();
    if (talkToViktor.equals("1")){
      System.out.println("Du hast mich schon verstanden. Willst du dich mit mir anlegen, du Abschaum?");
      System.out.println("(1)Ich lass mich nicht beleidgen");
      System.out.println("(2)Nein lieber doch nicht, Entschuldigung.");
      talkToViktor = console.nextLine();
      if (talkToViktor.equals("1")){
        System.out.println("Du Volldidiot.");
        System.out.println("(Er holt aus und schlägt auf dich ein.)");
        System.out.println("-----------------------------------------------------------------------");  
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------");  
        System.out.println("-----------------------------------------------------------------------");
        playerHealth = 0;
        playerStatusGameEnds = 1;
      } // end of if
      else if (talkToViktor.equals("2")){
        System.out.println("Goldrichtig. Brauchst du meine Hilfe oder wieso bist du hier? Kann es sein, dass du im Labyrinth gelandet bist?");
        System.out.println("(1)Ja ich brauche Hilfe und so ein komischer halbnackter Mann sagte mir du kannst helfen.");
        talkToViktor = console.nextLine();
        if (talkToViktor.equals("1")){
          if (playerStatusKey == 2){
            System.out.println("Du hast den Schlüssel doch schon. Kann es sein, dass du bescheuert bist oder so?");  
          } // end of if
          else {
            System.out.println("Tiberius ist etwas komisch drauf, ja. Aber klar helf ich dir. Hab hier den Ausgangsschlüssel, nimm ihn ruhig");
            playerStatusKey++;
            looksAtKey();  
          } // end of if-else
        } // end of if
      }
    } // end of if
    else if (talkToViktor.equals("2")){
      System.out.println("Goldrichtig. Brauchst du meine Hilfe oder wieso bist du hier? Kann es sein, dass du im Labyrinth gelandet bist?");
      System.out.println("(1)Ja ich brauche Hilfe und so ein komischer halbnackter Mann sagte mir du kannst helfen.");
      talkToViktor = console.nextLine();
      if (talkToViktor.equals("1")){
        if (playerStatusKey == 2){
          System.out.println("Du hast den Schlüssel doch schon. Kann es sein, dass du bescheuert bist oder so?");  
        } // end of if
        else {
          System.out.println("Tiberius ist etwas komisch drauf, ja. Aber klar helf ich dir. Hab hier den Ausgangsschlüssel, nimm ihn ruhig");
          playerStatusKey++;
          looksAtKey();  
        } // end of if-else
      } // end of if 
    }     
  }  
    
  static private void talkToMagician(){
    if (playerStatusMagician == 0){
      System.out.println("Seid gegrüsst der Herr. Ich bin der allmächtige Erzmagier, sie hatten wirklich Glück dass ich gerade Lust hatte her zu kommen. Kommt nicht oft vor.");
      System.out.println("Ich habe hier ein Geschenk vom Entwickler höchstpersönlich an sie.");
      System.out.println("(Du hast beide Schlüssel vom Erzmagier erhalten, du Glückspilz.)"); 
      playerStatusKey = 2;
      playerStatusMagician++;
    } // end of if
    else {
      System.out.println("Du hast alle Schlüssel, du kannst den Wald jetzt verlassen " +playerName+ ".");
    } // end of if-else
  }
  
  static private void mazeOutput(){
    String outputLine = "";
    System.out.println();
    System.out.println("Du hast das Easter Egg gefunden. Hier siehst Du das Labyrinth von oben:");
    int row, column;
    for (row=0;row<mazeSetupRows;row++){
      outputLine = "";
      for (column=0;column<mazeSetupCols;column++){
        outputLine = outputLine + mazeLayout[row][column] + " ";
      }
      System.out.println(outputLine); 
    }   
    System.out.println("Du schaust in Richtung " + textOrientation[playerOrientation] + " und stehst in Zeile " + playerCurrentRow + " / Spalte" + playerCurrentCol);
    System.out.println();
  }  
  
  static private void pathOutput(){
    String outputLine;
    int rowsPrinted = 0;
    System.out.println();
    System.out.print("Gelaufener Weg                                    Status von " + playerName);
    if (playerStatusEasterEggFound){
      System.out.println(" (Easter-Egg-Jäger)");
      }
    else {
      System.out.println();
    } 
    System.out.println("=============================================================================================================");
    int row, column;
    for (row=0; row<pathSetupRows; row++){
      outputLine = "";
      for (column=0; column<pathSetupCols; column++){
        outputLine = outputLine + path[row][column] + " ";
      }
// Unterdrücke Leerzeilen, aber nur wenn schon alle Statusinformationen ausgegeben wurde      
      if (!outputLine.isBlank() || ( rowsPrinted > 0 && rowsPrinted < 6) ){
// Ergänze Statusinformationen und Rucksackinhalt ab Spalte 50      
        outputLine = outputLine + "                                                           ";
        outputLine = outputLine.substring(0,50);
        if (rowsPrinted == 0){
          outputLine = outputLine + "Standort: " + playerLocation + " / " + textLevel[playerLocation - 1];
        }
        else if (rowsPrinted == 1){
          outputLine = outputLine + "Gefunden: ";
          if (playerStatusKey == 1){
            outputLine = outputLine + "Ein Schlüssel ";
          }
          else if (playerStatusKey == 2){
            outputLine = outputLine + "Der Ausgangsschlüssel ";
          } 
        }
        else if (rowsPrinted == 2){
//&&&&&&&&&& Mit dem absoluten Gesundheitswert kann man wenig anfangen
//&&&&&&&&&& Wie wäre es, denn Prozentual zu machen, und für jede 5% einen "Balken" anzuzeigen (ähnlich wie auch in modernen Spielen)
          outputLine = outputLine + "Gesundheit: ";
          if (playerHealth > 100){
            outputLine = outputLine + "Exzellente Gesundheit (" +playerHealth+ ")";
          } // end of if
          else if (playerHealth >= 50 && playerHealth <= 100){
            outputLine = outputLine + "Gute Gesundheit (" +playerHealth+ ")";
          } // end of if
          else {
            outputLine = outputLine + "Schlechte Gesundheit (" +playerHealth+ ")";
          } // end of if
          
        }
        else if (rowsPrinted == 3){
          outputLine = outputLine + "Wissen: ";
//&&&&&& das Treffen mit dem Magier könnte noch als Wissen auftauchen (muss abernicht)
          if (playerStatusNPC == 1){
            outputLine = outputLine + "mit Tiberius ein mal gesprochen";
          }
          else if (playerStatusNPC > 1){
            outputLine = outputLine + "alle Infos von Tiberius erhalten";
          } 
          if (playerStatusViktor == 1){
            outputLine = outputLine + " und den Schlüssel von Viktor bekommen";
          } 
        }
        else if (rowsPrinted == 4){
          outputLine = outputLine + "";
        }
        else if (rowsPrinted == 5){
          outputLine = outputLine + "";
        }
        else if (rowsPrinted == 6){
          outputLine = outputLine + "";
        }
        else if (rowsPrinted == 7){
          outputLine = outputLine + "";
        }
        System.out.println(outputLine); 
        rowsPrinted++;
      }
    } // for row   
    System.out.println();
  } // end of pathOutput
  
  public static void wait(int ms) {
    try{
        Thread.sleep(ms);
    }
    catch(InterruptedException ex){
        Thread.currentThread().interrupt();
    }
  }
  
  static private void looksAtWall(){
         System.out.println(" _____________________________________________________________________");
         System.out.println("|####################################################################|");
         System.out.println("|####################################################################|");
         System.out.println("|####################################################################|");
         System.out.println("|####___##################################_______|_____|______|______|");
         System.out.println("|___ ##_|_________|____ ########### ######_|_______ #####____________|");
         System.out.println("|___##### __|_________ ############# ######   | _ ########  |   |    |");
         System.out.println("|__#########_____|___ ############## ####### __ ###########___|____|_|");
         System.out.println("| ############_______################ ######## ##############        |");
         System.out.println("|##############____################### ######_################___|___|");
         System.out.println("|################  #################### ####_###################     |");
         System.out.println("|################_#####################_##_######################____|");
         System.out.println("|############## ####### ########## ##### _####### ########## #####   |");
         System.out.println("|############_######_____######_____### #######_____######_____####  |");
         System.out.println("|______|_______|_______|_____|______|_______|_______|______|_____|___|");
         System.out.println("|___|______|_______|______|______|______|______|_______|______|______|");
         System.out.println("Vor Dir ist eine überwucherte Mauer.");
  }

  static private void looksAtExit(){
         System.out.println(  "88888888888888888888888888888888888888888888888888888888888888888888888");
         System.out.println(  "88.._|      | `-.  | `.  -_-_ _-_  _-  _- -_ -  .'|   |.'|     |  _..88");              
         System.out.println(  "88   `-.._  |    |`!  |`.  -_ -__ -_ _- _-_-  .'  |.;'   |   _.!-'|  88");              
         System.out.println(  "88      | `-!._  |  `;!  ;. _______________ ,'| .-' |   _!.i'     |  88");
         System.out.println(  "88..__  |     |`-!._ | `.| |               | '|  _!.;'   |     _|..  88");
         System.out.println(  "88   |`` ..__ |    |` ;.| i|_|MMMMMMMMMMM|_|'| _!-|   |   _|..-|'    88");
         System.out.println(  "88   |      |``--..|_ | `;!|l|MMoMMMMoMMM|1|.'j   |_..!-'|     |     88");
         System.out.println(  "88   |      |    |   |`-,!_|_|MMMMP'YMMMM|_||.!-;'  |    |     |     88");
         System.out.println(  "88___|______|____!.,.!,.!,!|d|MMMo * loMM|p|,!,.!.,.!..__|_____|_____88");
         System.out.println(  "88      |     |    |  |  | |_|MMMMb,dMMMM|_|| |   |   |    |      |  88");
         System.out.println(  "88      |     |    |..!-;'i|r|MPYMoMMMMoM|r| |`-..|   |    |      |  88");
         System.out.println(  "88      |    _!.-j'  | _!, |_|M<>MMMMoMMM|_||!._|  `i-!.._ |      |  88");
         System.out.println(  "88     _!.-'|    | _. |  !;|1|MbdMMoMMMMM|l|`.| `-._|    |``-.._  |  88");
         System.out.println(  "88..-i'     |  _.''|  !-| !|_|MMMoMMMMoMM|_|.|`-. | ``._ |     |`` ..88");
         System.out.println(  "88   |      |.|    |.|  !| |u|MoMMMMoMMMM|n||`. |`!   | ` .    |     88");
         System.out.println(  "88   |  _.-'  |  .'  |.' |/|_|MMMMoMMMMoM|_|! |`!  `,.|    |-._|     88");
         System.out.println(  "88  _! '|     !.'|  .'| .'|[@]MMMMMMMMMMM[@]  |  `. | `._  |   `-._  88");
         System.out.println(  "88-'    |   .'   |.|  |/| /                  |`.  |`!    |.|      |`-88");
         System.out.println(  "88      |_.'|   .' | .' |/                        |  `.  | `._-   |  88");
         System.out.println(  "88     .'   | .'   |/|  /                       |`!   |`.|    `.  |  88");
         System.out.println(  "88  _.'     !'|   .' | /                        |  `  |  `.    |`.|  88");
         System.out.println(  "88888888888888888888888888888888888888888888888888888888888888888888888");
         System.out.println(  "Du siehst eine große Tür vor dir, womöglich der Ausgang.");
  }

  static private void looksAtDoorLock(){
         System.out.println(";;;cloolc:;,,'.,:::;;,;,,,,;,,,;;,;;;,;;,,;;;;;;:::;;;;;:;;::;::::c::::::;::::::::c:;:::::::::::::::cc:ccc:;;;,....',,;:cccll");
         System.out.println("::lxdool:;,'..,llccc::::::::;;;;;::;;;;;;;;;:::::::::;;::::::ccccccccllllc:cc:::::;;:;;::;;:;;;;;,,;;;::c:;;;'.....,;;::ccclc");
         System.out.println("::loddoo:;;,'.,clcc;,'''''''....''''...''..'''''';c::::::;;;:::::::clllllc;;,,'''''''''.'.....''''..''';cc:;;'.....,;;;:cccll");
         System.out.println("::cloddoc;,,'',::cc,'.....'''......''..'..''.'',:ll:;;;,'...',,,;;;;:::ccc::,,,,,;::cc;,',,;:ll:,,,;;,;colc:;'.....';;;::clll");
         System.out.println("::coddddc;,,'',colc;...',;;;;,...',,;:,.',;,,;clll:;,'...',;;::::cc::::::cc:;',;:ccoxdc,';:;cddo;.';,,:lool:;,.....',;;:cclll");
         System.out.println("::clodddc;;;,''colc;'.',,,,;c:,..',,cl;..'',;:cllc;,..',;;;::::::::ccc;'';cc:,',;:coodo;'.,;:looc'.';;clooc;,'.....',;;::clxo");
         System.out.println(";:lodxxoc;;,,',cdo:,'.',,,,:lc;..',,::;'.';:clccc;'.';;:::::::::cc:,'.. .,:;;;'',:clloo:'.';clol:,..';clooc:;'....'',;;;:ccod");
         System.out.println(";coodxxdc;;;,''col:;'..'''':c:,..',;c:;,..;dxolc:,.,::::::cccccc;'......;c;,;:;'.,:ccoo:,.';clll:,..,;:cloc;;,.....',;;;:ccll");
         System.out.println(":clooddoc;;,'',clc:;'..'''';c:,'..',;:;,..,:lccc;'';cc:::ccc:,......';:ccc:,,;:'.';:cll:,..:lcclc,..,,:looc;,,.....',;;;::cll");
         System.out.println("c:ldooll:;;,'',:lc:;'..'''';lc;...'';:;,..';cccc:,;c:::cc:,......,:cllc::c:,';;'..';:cl:'..;::cl:,..,;:looc:;,.....,,;;;:::cl");
         System.out.println("c:coolcc::;,,,;lol:;'..'''',ll;...'',::,..';:;:c::clcc:;.....,;;:looolccc::,.,;...',,:c;...;c::c:'..,;::lolc;,....',,;;;:ccll");
         System.out.println("c:cooc::::;,'';oxoc;'..'''',;:'..',,,,;'..';;;,:ccloc,. ..';:clllllllllccc;..,'..,,,,,,,..':;:;;,..';;::lol:;,'...',,;:::cllc");
         System.out.println(":codoc;::;;,,,,ldoc;'..''.',,'...''''''...'',;;:lcc;....';cllclllcccccllc,..,,'..',,,,,,'',,;:;;,'',,,;:loc:;,....',,;:;clllc");
         System.out.println(":cddo:;:::;;,';loc:;,'''''''''''''''','''''',,,:c:c:',:cllloolllllllccc:'..';;;,,,,;,,;;;;,,;;;;;;;;;;;:lc:;;,....',,;;;:llll");
         System.out.println(":cdxl:;;::;,,,;colc:;;;;;:::;;:;;::;::;;;:;:;::;:::::clllooddolllllc:,'...,,;:::::::::;;;::;;;;;;:::::;:c:;;;'.....',;;;clccl");
         System.out.println("loxxl:,,::;;,,;cllc:;,,,,,,,,,,''',''''''''''...'',;::::;;:c:;;;;;,....',,,,''..'''...'...............';c:,;;,.....',;;:ccclo");
         System.out.println("clooc:;,;:;;,;;ldoc:,'..........................',;clc::::::;,,,,,,,,;;;;,''',,,,,,,'''''''.'''''''''',:lc::;,....',,;;::cloo");
         System.out.println("c:ccccl:;;;;;::oxoc:,'....'',,,,,,,,,,,;,,',;,,:ccllllccccc::::::::::;;:;;;;,;::ccccc:;;::::;;;;;;;;;;;:ll::;,....',,;;;:cloo");
         System.out.println("cccllccc:::;;::lxol:,...',,,;;;;;;;;;;;;,,,,,,,:ccccc::cc:;;,,,,,;;;:;;;;::;;,,;::cccc::::::;;;;;,;;;;;:llc:;'....',,;;::clll");
         System.out.println("cclolcll:;;;;::ldol:,...',,,,;;;,,,,;;;,,,,;;;:::::::;,'....     ....',,;::;:::;,,;:ccc::c::::;,'',;;;;;ccc:;'....,,,;;::codo");
         System.out.println("ccllllllc::;;::lool:,....',,,,''',,,,;,,,;:::cc:;;:;'.               ....,;:::::;,',:cccc:::::cc,''''',;cl:;;'....',,;;;:lodo");
         System.out.println("cclllccc:::;;:;cdol:,'.''',,,;;,'''''',;;;:cloc;:;'.                  ....',::::::,';::cl:;;;;cl:;;'',,;clc:;'....',,;;;clodl");
         System.out.println("cllllcc:;:c;;::lxdl:;,''',,:cccc:::;'..,;:cllc::,.                     ..',',::::::;',:lollcccc:;;::;;;;clc:;'....,,;;;:clodl");
         System.out.println("cclllc:::l:;:::lxdl:;;,;;:cccccc:::;,..;::ooc:;,.                       ..''',:::::;'.,cc:;;;::::;::::;;;:::;,....',;;:::clol");
         System.out.println("cclcllccc::;;::lxdl::;,;;;;;::::;,'...'cllol::;.                         ..,;;;c::::,.'::,'',:cc:,,'...';::;;'....',;;;::cloo");
         System.out.println("ccllclllc::;;;;lxdl:,.......'',,.....';:ldo:::,.                         ..';;;cc:::,..;;;;,;c;'.......,:c::;,....',,;;;:codd");
         System.out.println("cclccoxoc::;;::lddl:;'..'''''''....,;;::lolc:;'                           ..;::ll:::,..,;;;;,'...'',,,,,;cc:;'....',,;;;:ldxd");
         System.out.println("lllclxdoccc:;::lxdl:,..',;;;,,,,'',,;;;:clc:::,.                     .... ..'cddlc:;,..,::;;,''',;;;;;;;:ll:;'....',,;;;:oddl");
         System.out.println("cllcoxxl:cl:;::cddl:,'.,,,;;;;,,,,,',,;::llc::;.                     ....  .':odl::;'..,;;;;:;'''',',,,,;cc:;'....',,;;;codoo");
         System.out.println("clcldxdc:cl:;:::ddl:;..',,,;;;;;;,'',,,;cllc:::'.                     ......;cll:;:,..',,,,;cl:;,'''''',;:c:;,....',;;;;:cldd");
         System.out.println("clccxOo:cll:;:;cxdc:;''',,',,;;:c:;,,,',:c:cc:::'.                    ......;cc::;;'.,:::;;;cc::::;;,,,,:cc;;,....',;;,;:lodd");
         System.out.println("lc:cxkocll:;;:;ckdlc;,,,;;;::cccc::cc:;,,;;:c:c:;,.                    ....,cc::;;'..;ccc::::::;::::;;;;;cc;;,....',,;;;:lddd");
         System.out.println("ccccooccll:;;::lkxlc:;;::ccccccccc:::;,..',,:cc:;:;'.                  ..':lc;;;,...',,,,,,;:;;;;;,''...;cc:;,....',,;;;:lddd");
         System.out.println("cccc::llcl:;:c:lkxc:;,'',,,;;:::::;,.....',,,;:::;::,.                ...;c::;;,...';;,,'',::;,'.......':ol:;,....',;;,;:lodd");
         System.out.println("ccccclolll:;:cclkxc:;'..'''''',,,;,....',;;;;,,,;:::;.                ...;c:;,'...,;;;;;;;,,,......',;;;:ll:;,....,;,;;;:lodd");
         System.out.println("lclcclolcl:::c:cxdcc:'..,,;;;,,'''...,,,;;;;;::;:cc:;.               ....;c:;,..',;;;::;;;,'.''',,;;:;;;:ll:;,....,;,;;;;lodd");
         System.out.println("lcc:coocccc:;::cddlc:'..,;:c:::,,,'',,;;;;;;:cccoolc;.             ......;l::,..';;:::;;;;,,,;:;,,,,;:;;:ll:;,'...,;;;;;:cood");
         System.out.println("lcc:ldocccc::lccxxlc:'..',::;:::;;;,,,;;:;;;;cllooc:;.            .......;lc;,..';;::;;;;;;,,;:;,,,,,;;;;ll:;,'...';;;,;:cood");
         System.out.println("lcc:odl:clc:;ccckkoc:,..',:::::;;;,'',;;;::;;:clooc:;.            .......,l:;,'.';:;;;;;;;;::,,,'',;,,,;;cl:;,'....,;;;;:clld");
         System.out.println("lcccoolcccc:;cclkxlc:,',,,,,;::;;:;,''',;::;;:cloo::;.             .... .'c:;;'.';;;;;;;;,;llc:;,,,,''',,:c:;,.....',;;;:ccld");
         System.out.println("lcccddoc:cc::cclxxlc:;,;;;::ccc:::::;,,,',;;:::loo::;.              ..   'cc:;'.',;;::c::;;cc::::::;;;,,;cc:;;'....,,;;;:clod");
         System.out.println("lcccddocclc;:lccxxlc::;:c:ccccccc:::cc:;'',;:::coo:;;.              ......cc;;'.',;:codxoc::::::;:::;;,,,::;;;'....,,;;;:clod");
         System.out.println("lc:cdkdcclc::lclxxlc:;'',,;;;::cc:::::;,..,;;::loo::;.              ......cc;;'..,;::ccc:;;,;:;;;;'.....,cc:;,'....,,,;;:cloo");
         System.out.println("lccldkdc:cc:clclkxoc:;'',,,,,,,,;;,'''...',;;:coooc:;.              ......:c:;,..,,;;;,''..',,''.....',,;cl:;;'....,,,;;::ldo");
         System.out.println("lccoxxlccc:;clccxxoc:,'';:c::;,'''...'..,;;;;:clooc:;.                ....cl:;,..',;;;;,,''''.....',,;;;:loc;;'....,,,;;;:lol");
         System.out.println("lccdxdlllc::cl:cdxoc:,',;;::::;,,''',;;;;;;;;:clooc;;.               .....:l:;,..',;;;;;,,,,'..',,;;;;:;;loc:,'....,;,;;;clll");
         System.out.println("c:cdxolcllc:cl:cdxl::,',;;;;;;;,,,;;;;;;;;;;;:clooc;;.              ......:l:;,..',;;;;;;,;;,,,',,;;;;;;;coc:;,....,;;;;:cloo");
         System.out.println("c:cdolocllc:cl::dxlc:,',;::::::;,,,;;;;;;,;;;;:looc:;.              ......;c;;,..',;;;;;;,;;;cc;,,,,,;:::clc;;,....,;;;;;:loo");
         System.out.println("cccllllcllc;:lc:oxlc:,,;:::;;::::;,,,,,,,,,;;;clooc:;.              ......;c:;,..',;;;;::;,;;clc::;;,;;;;:ll;,'....,;;;;;:lol");
         System.out.println("c::lloxdolc;:lc:dxlcc;;;::::cccc:::;;,'',,;;;:clool:;.              ......;c;;,..',,;::cllc::cc::::::::;;:cc;,'....',;;;;:loo");
         System.out.println("c:codx0kdoc::oc:odolcc:::::cc::c:::::;'.',,;;;:cddl:,.             .......;l:;,..',,;:;:cllc::;:::::;,'',:c:;;'....',;;;;:coo");
         System.out.println("c::dkO0Odlc:col:odocc:,',,,;:::c:;;;,'..',;,;;:cdxl:,.             .......,c:,,...,;;;;;:,,,,'',;,'.....'clc:;'....',,;;;::lo");
         System.out.println("c:coO00Oxlccclcldxoc:,''',''',,,'......',;;,;::codc;;.              ......,:;,;'..',;:;::;,''.........',;clc:;,'...',,;;,:ldd");
         System.out.println(":::dO00Odllcclccxxol:,'',,,,,,'....''',,,;;,;;;:loc;;.             .......,c:;,'..',;;;;;,;;;,,,'.'',,;::col:;,....',;;;;:llo");
         System.out.println(":::ok00Oxolccollxxoc:,..',,;;;,,,,,,;;;;;,,,;;;cllc;;'.            .......;c:;,'..',,;;;;;;;;;;;;;,,;::::lol:;,....',,;;;cclo");
         System.out.println(":::ldO0Okoc:colcxkoc:;'.,,,;:;,;;:::;;;;;,,,;,;:llc::,'............'''''';:c:;,'..',,;;;;;;;;;;;;;;;;;;;cooc;;,.....,,;;;:clo");
         System.out.println("occcoO000koccolcxklc:;'',,,;;;,;;;;,,;;;,,,,,;;cllc;::;;:;;;;;::;;;;;;;;;;;;,,,'..',,,,,,;;,;,;;;;;;:;;;:clc:;;'....,,;;;::lo");
         System.out.println("lccclxOO0Oxlloccddl::,',,;;;;;,;;;;,,;,,,,,,,;;cllc;;;;::;;;;;::;;;;;;;,;;;;;;,'..',,',,,,,,,,,,,;;;,,;;;col:;,,....,,,;;:cod");
         System.out.println(";lc:ccdkOOkdlloccdkoc:;,,;;;,,,,,',,,,,,,,,,,,;;;cc:;;;;;;;:cllllllllcc:;,,,,;,,,,',,,,,,,,,,,,,,,,,;;;;;:clc:;,'....,,;;;:co");
         System.out.println("cccccloxdocccol:oxocc:::::::;:;;;;;;;:::;:::;::::;;;;;;;cloolcc::::::c:::;,,;,,,;;;;;:;;;;;::;;:;;;;;;;;:::::;,'....',;;;::lo");
         System.out.println("ccccclcloc::colcoxocc:;;;;,;;,,,;,,,,;;,,,,''''',;::;,,clc:;:;;;;;:;;:;;:::,',,'''''''''..'''...'''...''',:::;;'....',,;;;:cl");
         System.out.println("lccccccllcc:colcdxol:;,''...'''.....'','.......',cl:'....';;;::::cc:;;;;;;;;''.....',,,,......'',,'...''',cl:;,'....'',,;;:cl");
         System.out.println("lc:cc:cool::coccdxoc:;'..',;cc;'..',;:ol;...',;:llc,......',,,;;:::;;;;;;;;;;.....',;:::;'..',;:cl:'.',;;:cl:;;'....'',,,;:cl");
         System.out.println("lc:cc:cooc::ldc:okdc:,...';:llc,..';:col:'..',:col;..::;,,',''''''''',,,,,,','.....',:;:c,..',::cl:,..,;;;:::,,,....,,,;;;:cl");
         System.out.println("occclc:oocc:loccdkdc:;'..,;:llc;..';:clc:'..',;clc;.'::::::;,,,,,,,,,,,''''''.......';:cc;'.';:::c:,..',;::c:,;,....',,,,;:cc");
         System.out.println("lcccc::oolccooccdxoc:;'..,:cloc;..',;:lc:,..',;::c;.';::::::::;;;,,',,,,,..'........';:c:;'..,::cc;,..',;:cc:,,'....',,,,;::c");
         System.out.println("lc:c::clol::oo:cdxdc:;'..,:cclc;'..,,,:c;;...,;;:c:'.';;:;;;;;;;;;;,,'',,'';;'''....';;:c;'..',,;:;,..',;::cc;,'....',,,,;;::");
         System.out.println("lc:ccllllc;:ll:ldxoc:;'..,:::cc;'..,,,:c:;...,;::::;'',:::;;;;;;;,,,,,;;;,'.,::'...',;,;:;'..,;::::'..',;;:ll:,,....',,;;;;:c");
         System.out.println("lcccccllll::lc:cxkoc;;'..,;:c::,...',,;::,..',;;;;:::,'';::;;;;,,,,,,,,,''..';;...',,;;;;;'..,;;;::'..'',;:lc;;,'...',,;;;;::");
         System.out.println("lcclccllllccll:cxxlc;;'..',:c:;'...,,;,;,...',,,,;::::;,,',;;;;;;;;,'''..''',,....,,,,,,,'..''',,,,...',,,;::;;,....',,,,,:cc");
         System.out.println("clccccllllcllc:cdxdc;;'..',;c:,'..''',,,'..''',,,,;;;::;;;,,,;;;;,,,,,,,;;;;;'..'',,,,,;,,,,;;;,,,;;,'',,;:c:;;,.....,,,,,;c:");
         System.out.println("lc:c::lolclol:;cdxoc:;,.',,;::;;;,'',,,;,,,,;;;;,;;;::;;;;;:;::::::::;;;;;;;;,,,,,,,,,,;;;;;;:;;;::;;;,;;;::;;;,.....',,,,;::");
         System.out.println("lc:cccldlclolc::oolc:;;;:::::::::::;::;;;;::;;;;;;;;;::;;:;;;;:;;;;;;;;,;;;;;;;;;:::;:::;;;;::;::;;;;:;;;;;;;,,''....',,,,;::");
         System.out.println("ll:cccloc:ccc:::ll:;,,,;;;;::;;;;;;;;;;;;;;;;;,,,,,,;;,,,,,,,,,,,,,,,,,,,,,,,,;;;;;;;;,,;;;;;,''''''.'''..'''........',,',;;:");
         System.out.println("Ein altes Schlüsselloch...");
  } 
   static private void looksAtAisle(){
         System.out.println(";;;;;;::::;:cccccllllcccclxxdddooddddxxxkkkxddxxxddoooddddddoodddddoclolllcclllcccclllooooooooddddoooollcllllcccccloollcccllollllollcc::::dxl:;;;;;;,,");
         System.out.println(",,,,,;::;;:clloooddooollloxxdlcccloddxxxkkOkkxxxxdolloodxxxxdocccoolc:::ccccccc:;;::::ccclloddooooooddooollllcllllllll:,,;;;;;;;;,,,,,''';ldl;,'',;;..");
         System.out.println(";,,,,;;;::cllloxxxxxxdddddxxdlccc:clodxxxkOkxxdoolc::;;:cclool:;;:;,,,,,;::::c::;;:cccc:;;,;:ccccc:cccccclllcccccc:;::;;;;,'..,'.''',,,;::ccl::::clc:,");
         System.out.println(":::::c:ccclodxxxxddddddddxddoolcc;;:cllloddooooc:;;:::,',,;;:;'',,'...,;:::cc::c::::clc:cc::;;;;;,,;;;;;;;;;::;;;;,',,'''''''.........'''''''''';cc:::");
         System.out.println(":coooolccloxxxdddlcooooooddddoolccccc:;:cloolcc:::,,',;;;,,,;,;cc::;,,;:;,,;;c::::;,::,',,''''''''''''''',;;;:c::::;:ccllllllcllooddddc'.'''....,cc:cc");
         System.out.println("lxOOOkxddxkkxxdolc:clccclooolc::ccc::::;clccc:;;:dx:,;;;;;;;;;:cccc:,,',,'..'...'',,;:::cclllloodddddddxxxkkkOOOOOOOO000kocdO00xxkkkkOd'........,:clll");
         System.out.println("KKK00K0000Okxdoc::;:::cllloolcc:cccc::;;;:cllll:coocc::c:;;,',;,''''...,;,',,,;looodxxkkkkOOOOOOOOOOOO000000000KK0000000kddxkxl::lodol:,',...',,;;:cll");
         System.out.println("XKKOOOOOOOkdolccccccloooddddooodddollolllcccclollooc:;;,',,,'',;;:;;:cloocclldxO00OOOOOOOOOOO0000000000KKKKK0KKKKKKKKKKKKK00kdlolc::;'..',...';;;;;;:c");
         System.out.println("kxoc:coddol:;;::clodxkOOO000000OOkkkkOOkxoodddlcllccccldooooooodollccccccccccclodxkO00O000000000OOO000000KK0OO0KKKKKKK0Okxddxdddol:,...','....,,;;;;;:");
         System.out.println("lc::;;:cc::;;;:cdxxxkOOOOOOOOkxxxdddxddxdooooo:;;;;;;;:ooodddollcccccccc::::::::ccloxO0000000OkxxkOOOOOOOOOkxxkkxk000kdooooolcc:;'....,,,.....',,,,,;:");
         System.out.println("lllllllolllllodkOOkkkkOkkkkxxdlcllclllllllllcc;,,,'''',;:cllccc:::::::::::::::::ccc::ldO0KK0kxxxkOOOkOkkkkkxxxddoloollodddoc:;,'....',,,'.....'',,,,,;");
         System.out.println(";;;;;;cccccoxkkxxxooodxxddolcc::::c::::::cc:;'',;;;,,;::::::::::::::::::;::::::::::cc:cldxkkkO0Oxdxxxkxddxxxxxoccll::coolc:,'.....',,,,,,.....'',,,,,,");
         System.out.println(".....':c:;codooooddooddllc:;;;;;;::;;:;;:::,'..',,;,;::::cc::;;;;;;;::;;;;:::::::ccc:::cccloxOOxc;cxxdooddddxxo:,'';:::;;,'.....',,;,,,,,....'',,,,,,;");
         System.out.println(",;,,''''',,,,''',oxddoc:::;,;;,',,:::cc:;,'......'',,;;;;:cc::;::;;::;;:::::::::::::;::::::;:clc;';lolcllllooool:,;:c:,,'.....',;;;;;,,,,'...'',,,,,,,");
         System.out.println("oddoolcc::;;:cclldxdoddddl:;;;,,,;:cc;,'......''''',::;',;::;;;::;;::;;;;:cc:::::::;,;;;:::;''',;;;;::;;;;;::;;cllc:;,''...',,,,;:;;;;;,,'...'',,,,,,,");
         System.out.println("ooooooooooolc::c:::cc::::,,,''',;::;;'.......';::'..',;;'',;;;;;::;;::;;::cc:::;,''',,''',c:...';c:;;;,,;,,,,;:cc:;,'.'''',,,,,,;;;;,,,,''....'',,,,,,");
         System.out.println("llllooolllll:'...........','',,;;;,'...........,;,'';;:;',;;,,;:::::::::::;;;,,'..'''.....;;...':lllllcllcllcc::;'.''',,,,,;,,,,,;;,,,,,,'...'',,,,,,,");
         System.out.println("cccccllooolc;.....';;;,,;cl;,,;,,'...........'''';;;,;;'..',''',,;::::;;;',,,'''..........,'....cxkkkkkkkxxl;,,'..',,;:lc'','..,;,,,,,;,,'...'',,,,,,,");
         System.out.println("ccccccclloc;;.....,llc::c::::;'.....,;'..........;c:;::'....'.....,;;,'''.',,'''..........',....;xOOOOOOOOd,.',''',;;;cxo'...  ,:,;;;;;,,''.'',,,,,,,,");
         System.out.println("ccccccccllc;;,.....;::;;;,'';::;,'...''...........''...''...''',..,,''''..................,,..',;lodxxxkkko'.,;;;,;::;cko'.''..;:;;;;;;,,'..''',,,,,,,");
         System.out.println("cccccccccc:,::,'.',,,,''...,,;:ccc;'.',........'''''...',,;,.',,',;,,,''..................',,;:cloddxxxxxxl'.,;;;;;;;;ckd,';,..:c,,,,,,,,'..'''',,,,,'");
         System.out.println(":ccccc:::::;:;'''',,,,..,,',:::;;;:c:;,'.....','........,,,,';;;,'.....''.............',;:cclodddxxxxxxxxxl'.',,,,,;;;cko..'.  ,:,,,,,,,''...'''',,,''");
         System.out.println("::::::::::;,,'....'''......';;,','';:cc:;,.......'''...',:::::::,.....''.........'',;:loddxxxkkxxxxxkkkkxxc..'',,'',,,ckd'.,,..;:,,,,,,''....'''''''''");
         System.out.println("c::::;;,''.........''..............'',;::c:;'...'',,,''.';;:::c:'...',,,'',,;::clloodxxxkkkkkkxxxkkkkkkxdl;.'''',''',;ckOkkkkkkxc,,,,,,''....''''.....");
         System.out.println("::;;,'....................''......'....',,;::::;::;::::::c:ccllcccllllooddxxxxxxxxxkkkkkkkkxxxxkkkkkkxdlc:;''',,,''',:oxxkkkkkOko:,,''''........''''',");
         System.out.println(";,''...............''.....'..............',,,;:cllllooooddoddddddddoodxkkkkkkxxkkkkkkkkkkkkkxkkkkkkkxocccl:'''''''''',;:;:;;;;:::;,,,,''','',,,,;;;;;;");
         System.out.println("'.................''''''.....'.............'''',:cccloddoooodollccccloxxkxxxxxkkkkkOOOkkkkxxxkkOOkkxdl:;:;...'''''.''',,,,,,,;;:;;;:::::::::;;;;;;;;;,");
         System.out.println("........ ............'..''..,;,.................',;::cccccllcccclloddxxkkkxxxkkkkkkOkkkkkkkxxkkkOkkkxoc;,'.....''',,,;,,;;;;;:::::;;:::;;:;;;;,,,,,,''");
         System.out.println(".................''''.....'',;;,''................';;::::clllccccllodddxxxxxxkkkkkkkkkkkkkkkkkkkkkkkxdoc:::,,;;,;:c::::;;;:cc::;;,,,;;,',;,,,'...',,,'");
         System.out.println("....................''.....',,,,''................''',,;:::;::::;;;:codxxxxxxkkkOOOkkkkkkkkkkkkkkOOOOkxdlccccc::::;;:cccllc:;,,;;;,,;,''''.''.''';:;,,");
         System.out.println(".............'...'''......'',;,'...................'...',;;;;;;;;;:;::coxxxxxkkkkkkkkkkkkkkkkkkkOOOOOOOkxxdlc::::cc;:cclllolc:cc::cc:,,''.....'',::;;,");
         System.out.println("..''.'.....';;,;;,,,'.',,..'............'''..,;'........''',,;,,,,,;:;;coxxxxxxkkkkkkkkkkkkkkkkOOOOOOOOOOkkxdoc:::c::cc:;colccccclllc::;;;;,,'.';;,,''");
         System.out.println(".....'......''''',;;:;;,'..''....''',,,......,;;;,............''',,,;;;:coddxxxxkkkkkOOkkkkkkkkkkOOOOOOOOkkkxxdlc::;:::::::;;;:cllllollllc;,';,,lc''',");
         System.out.println("...............';clllllc,.'.....';;;;:;'.....',''................'',;;,;:lodxxxxkkOkkOOOOOkkkkkkkkkOOOOOOOOkkxxddooc:::::c:,'',:llloooodl;',::,,ll:;;;");
         System.out.println(".....'.........';:clllc:,'.......,,,,,'..',''''..''...''......   ...',;;::cloddxkkkkkkkkkkkkkkkkkkkkOOOOOOOkkkkxxddoc:;::::::;;::ccclooc;,,,::;;:cc:;,");
         System.out.println("........''........';;,'''.........',,'...''...'..''''',,,'............'',;:::ldxxkkkkkkkkkkxxxxkkkkkkkkkOOOkkkkxxxddolc:;;::::;;:cllllc::::cllcclol:;,");
         System.out.println("..'....',,..................'''.'',,,,........'''...';;;;,...............';::ldxxxkkkkkkkkxxxxxxxkxxkkkkkkkkkkkkxxxxdoolc:;;;::cclclolclolcllooollccc:");
         System.out.println("''''..................','......''..''..............'','''..........'''.'',;:coddxxxkkkkkkkkxxxxxkkxxkkkkkkkkkkkkkxxxddollc:;;:ccllc:ccclolclc:ccc:::cc");
         System.out.println("'...................,;:c:'....''.................''''''.''...........',',,;:coddxxkkkkkkkkkkxxxkkkkkkkkkkkOOOkkkkkxxddoolc:;::ccccllc::clc::;;::::;;:c");
         System.out.println(":,..................,;;,'................................'...........,,,,,::coddxxkkkkxxxxxxxxxxkkkkkkkkkOkkkkkkxxdddoool:;;;;::cclll:;;::,,',:cc:;,,;");
         System.out.println("l;',''......................................''.......................',,,;::coddxxxxxxxxxxxxxdddxxxxxxxkkkkkxddoollllcccc:;;;:c:;::clc;,,,,'';:cc:::::");
         System.out.println("o:''',...............................''''......................... ...,;:clloddxxxxxxkkxxxxxxxxxxxxxxxxxddol:,''''',;,,,;;:cc:::;:c:::::::;,';;:clllll");
         System.out.println("l;.................................................... ....... ......';clodxxxkkkxxxxkkkxxxxxxxxxkkxxxdl:;,'.....''','',;codl:,,;;;,,;;;:::;;;;;;ccc:;");
         System.out.println("oc,...........................................................',::;:cloodxxxxxxxxxxxxxxxxxxxxkkkkkxdc::;',,;,'',,,,''';:llol;''',;,'..'.','...',:c:;..");
         System.out.println("o:........................................................':cccccclooddxxxxxxxxxxxxxxxxxxxxxxxxxxl;;;:;''',;;;;'''..,;:::;,'..,,'''............',,,'..");
         System.out.println("l:........................................ ..........';:ccllooooodddxxxxxxxxxxxxxxxxxxxxxxxxxxdl:,;cc;,',,''','...'',;:;,,'...,''...............'.....");
         System.out.println("o;.................'...','''''.........'',,,,;cllllllodddddddxxxxxxxxxxxxxxxxxkkkxxxxxxxxxxddl::cl:;;,,,,'...''....''.........'''... .............''''");
         System.out.println(",..........''......''..,,,'',,,,,,,;:cloddddddddddddddddddxxxxxxxkkkxxxxkkkkkkkkkkkkkkkkxxddocccc:;;;;;;,'..,,'''.',''........''..........''..  ':,,;:");
         System.out.println("...........,,;;,;;::::cllllloooooooddddddddddddxxddddddddddxxxxxxxkkkkkkkxxxxxxxkkkkkkkxxddl:,;;;;;;;;;:,,''''''',''......,,'''...........''....,,....");
         System.out.println(".'''',;;:::ccclllloolooooodddddddddxxddxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxkkkkkkxxdoooc'..,,,,,,,;;;,'....'''...''....','...................'.....");
         System.out.println("ccccllllllloooooooooooddddddddddxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxkxxxddoolol:'....'',;;'',,,,'..''......'...'......',..............'.....");
         System.out.println("oooooooooooooooddoooddddddddxxxxxxxxxxxxxxxxxxxxxxxxxxxddddxxxxxxxxxxxxxxxxxxdddddlllccl:'.....',,,,'';;,'''''..'................,,',;;,.......''. ...");
         System.out.println("ooooooooooddddddddddddddxxxxxxxxxxxxxkxxxxxxxxxxxxxddddxxxxxxxxxxxddxxxxddddddddoccllccc:,.....,,;;''''''.........................',;:;'.......,'.....");
         System.out.println("ooooooooooddddddddddddxxxxxxxxxxxxxxxxxxxxdxxxxdddddddxxxxxxxxxdxxxdddddddddolc:::::clc:'.....'',,;'''.............................,;;;,.....':'......");
         System.out.println("ooooooooddddddddddddddddxxxxxxxxxxxxxxxdddddddddddddxxddddddxxxdddddddddddol:;;;:::::;;....'....,,,;;;;,..........................',,''.......,'......");
         System.out.println("ooooooodoodddddddddddddxdddxxxxxxdddddddddoodddddddddxxxxxxxxxxxddddddxdlc;,,,,;cc:;,,....''''..'',;;,,,,................... ....'''..................");
         System.out.println("Du siehst eine kleinen Weg, welcher von Häusern umgeben ist");
  }
   static private void looksAtHouse(){
         System.out.println("ccclllloooooooddddddxxxxxkkkO0OkkkkkOOkkOOOOOOOOOOOkkkkkkkkkkkkOOOOO00000KXNXOxoc:cxKXNNNXXNXXXXXXXXNNNNNNNNNNNNNNNXXXXXKXXK0OkkkxxxxdddoooooooolooooooooooooooooooooddxOkddxxxxdddollooodoooc;;:ccc;;, ");
         System.out.println("ccccllloooooodddddddxxxxxxxkk00OkkkOOOOOOOOOOOOOOkkkkkkkkkkkkkkkkOOOO00000KK0xlcc:clloxxx0XNNNNNXXXXXXXXXXNNNNNNNNXXXXXXKKXXKOOkkxxxxdddoooooollllloooolooooooooooooooddkOkxdxxxxxxdolc:clc::::;;::;,,,,");
         System.out.println(":ccccllloooddddddddxxxxxxxkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkOOOO000Oxdolc,......,:;:odxkOKXXXXXXXXXXXXXXXXXXXXXKKKXKKKK0OOkkxxxddddooooooollllllllllooooooooooddddddxxddxxxxxxxddllllc::::cccc;';c;");
         System.out.println("::cccclloooodddddxxxxxxxxxxxxxxxxxxxxxxxxkkkkkkkkkkkkkkkkkkkkkkOOOO00Oxdolc;.....      ..';::coddx0KKXXXXXKXXKKXXXKKKKKKKKKK00OOkxxxxdddddooooooooooooolooooooooooddddddddxxxxxxxkkkxdlllccc:cllcc:;,,;:");
         System.out.println(":::cccllllooooodddxxxxxxxxxddxxxxddxxxxxxxxxxxxkkkkkkkkkkkkkOOOO00Oxdoc:;.......          ....';;:loodkOO0XXXXKXXKKKKKKKKKKKK00OOkkxxxxdddddoooooooooooooooooooodddddddxxxxxxxkkkkkkdoollodddolc:;,,;;:c");
         System.out.println(":::::ccccllllooooddddddddddddddxxxdddddxxxxxxxxxxxxkkkkkkkkOOOOkxdolc;'....                    ....',;:cclxkOKKXXXXXXXKKKKKKKK000OOOkkkxxdddddddddddooooooooddddddxxxxxxxxkkkkkkkkOOOOOkxkkdddxoc::clccc");
         System.out.println(";::::ccccllllloooooooodddddddddddddddddddxxxxxxxxxxxkkkkkkOOxddoc:;......          ...               ...',;:clodk0KNNNXXXXKKXK0000OOOkkkxxxxxxdddddddddddddddxxxxxxkkkkkkkkkOOOOOOOOO0Okkxxxkkxdddoc:cld");
         System.out.println(";:::::cccclllloooooooooddddddddddddddxxxxxxxxxxxxxxkkkkOOkxoll:,.......        ....':c:,..             ......',;:ldxkk0XXXXXXXKKK000OOOkkkkkkkkkkkkkkkxxxxxkkkkkOOOOOOOOO000000000000O00OkOOOOOkxxoclodo");
         System.out.println(":::::::ccccllllloooooooooddddddddddxxxxxxxxxxxxkkkkkOOkxdoc:;.....          .........',:;..                    ...',;:cdxkOKXNXKKKK0000OOOOOOOO0000OOOOkkOOOO000000000KKKK0KKK000000OkxkkO0OkOOkkxoool:;");
         System.out.println(":;::::cccccclllloooooddddddddddddxxxxxxxxxxxxkkkkkkkxdol:,'..             .........;,. .........                   ...',;:cldkkO0KKKK000OO000KKXXXXKKK0000KKKKKKKKKKKXXKK0O0K0Okkk00OxodkOkkkOOOkdoo:;;,");
         System.out.println(":::::codlcccllllllooooddddddddddxxxxxxxkkxxkkkkOkdoodo:'....          ...........',c;...'....... .';;.              ... ...',;:look0KKXKKKKXXNNNNNN0xkKNXXXXXXXXXXXXXXX0OO00kxooddkkddloooxkO00OOdc:,',;");
         System.out.println(":::::cddlc:ccllllloooooodddddddxxxxxxkkkkkkOkkddoc:,::.             ........'..',;:l;....''.......'::.  .                 ......',;:ldkO0XNNNNNNWNXo':0NNNNNNNNNNNNNNXXXK00kxxdol:::clcclclodooxkdl:::::");
         System.out.println("::::::cccccccclllllooooodddddddxxxxxkkkkkOkdolc:,.....         .... .....'',;:,';:co:... ..........','..    ....             .........';:ldxOKNNWKo',kNNNNNNNNNNNNNNNNNNNXK0OOxlcclcc:;col:lolcloollcclo");
         System.out.println("::::::ccccccllllllooooodddddddxxxxxxkkkdool:;'....         ..........',,,;;,:l;,lccdl;,''';;'........''''.......  ....                  ....':oO0d,,xNNNNNNNNNNNNNNNNNNNWWWXOxoooxkolllllc:cc::c::cc;,;:");
         System.out.println("::::cccccllllllloooooddddddddxxxxxkxdolc:;'.....       ..........'',;;;;,'..;l:cdlcdl:;,;,c:...''''''........''..... .....                    .,c:'cO0KXNNNNNWNNWWNNWWWWWWN0xk0OOOkkxddoooxkdlccooolc::c");
         System.out.println("cccccccllllllooooooddddddddddxxxxxkko;,.....        ..........,;;::;,'...,ldOd;cdc:ol:::;,cdc;,.......''''''...'',''............               .:'.,;cloxO0KNWNWWNNNNNNWNWWXKXNXOk00kxoodkOOkdc:ll::c;,,");
         System.out.println("cccllllllooooooodddddddxxxxxxxxxxkkkxxdc,'...     .........'',,,:c'..,clcd00Ko:dkl:l:;;','ckOOOko;;;;,'...,c'.....'.'','''......              .;'.   ...,;:ldkO0XNNNNNNNNWWWWWWNxxXOkkxxO00Okdcclc,''...");
         System.out.println("clllllooooooodddddddddxxxxxxxxxxxkkkkOOkkkkddl,.    .....',,;'.,c:..;xOkoOXXXo;lxoclc:,...:xkOOkxccdool:,':c'....,:,',''',,''''.....     .  ....          ...,,:oxxOKXNWWNNWWWNWOxXXK00OkkOOkd:,,,..;;,,");
         System.out.println("lllloooooooddddddddxxdxxxxxkxxkkkkkkkOOO00Okxo;....'',;;;,.':,';l:'.:OKkdKNNKl,ldoooc:::,'cxxkkxd:cdolc'.'cc'....;c......,,',,,''''..          .               ..'';lddO0KXWWNNW0x00kkxxxdolllc;cl;,c:,,");
         System.out.println("llooooooddddddddxxxxxxxkOkO0OOOOOOO0000Oxdl:,...',,;:;;,.':oo,;loc''c0XOxKNNKc,coddoc;,,''lkO0OOkloOOkd;.'cl'....;l,..........,,.....    ...........             .,';c:cllkOKXNXOlcodocclloxOKklod:,::,,");
         System.out.println("ooooodddddddxxxxxxxxxxk0K0KK0KKKKKKK0Oko;'.. ..',,c:.':l;o00d'';l:.'l0XkxXNNXl':lodd:,;;,'lxO0OOkod00Okc''cl'....;dl;,',;',;''c:..... ....................       'c:c::lcclodxOO0d;clloxk00KKK0oloc,;c;,");
         System.out.println("ooooddddddxxxxxxkkkkkkO0KKKKKXKK00Okxoo;.     .,;:l:'lOxcxXKo',:l:.'l0XxxXNNKc':lllc,.....cxkOOOklo000Ol'':c'....;kkxdc:clll;cd:............'..'..'...........  .;c::::cc:::;:lldoclxkkkO00K00Kdcoo::c:,");
         System.out.println("dddddddddxxxxxxxkkkkkkO00000Okxoc:;,;:c'      .,;:c:,o0ko0NXo,;cl:.,o0KddXXXk;:dxdc,''''..:dxOOOxloO00Ol'':l'....;kOkkdcoOOl'cd:..................'..''...........,'..........';c,..',:lxkO00O0xcdxc:c:,");
         System.out.println("dddddddddxxxxxxkkkkkkOOOOOOkd;'...  ....     .';:cl;;dKkoKNKc,ldd:.,o0Kxodl:c;;cooc;::;::,:c:loddldOOOOl'.:o;... 'xOkkxclO0o,cdc'.......''..............',,'''........         ..      ..,:lookOlodc;:c:");
         System.out.println("dddddddddxxxxxkkkkkOOOOOOOOOxl;:oo,   . ......',;cc,:xKxdKN0:;ooo:';ll:;',coo;,cooc:'',,,':c,'',',;ccclc,';l:,,,.,dOkkxllO0d,:dl::;,........,'....'........','.,,''.......                 ..,col:c;',:;");
         System.out.println("ddddddddxxxxkkkkkkOOOOOOOOOOOOOO0k:..........',,;cc,:k0ddXNO;.,;l:'...:o:c0Kx,;odl;'......:dxdoc,,;,,''...;l;..'.'dkkOxc:x0x;:ol,,;;::,''',',,'';;;,;;,,,...'.','.''''''.....             ...',;:::;:;,'");
         System.out.println("ddddddxxxxxkkkkkOOOOOOOOOOOOOOOO0x,...''..';,',,;cc':k0dd0xo,.,:l:...ck0ooKXx,;odo:'......:k000Oc:ddool;..,l;....'oocooccxOk;,lc. ...'''',;;;;::::,.',,;;,''''''.   ...,,'.........      ....',,:ol;,'..");
         System.out.println("dddxxxxxxkkkkkOOOOOOOOOOOOOOOOOO0d;',,''',:c''',;cc;cooc;:ll,';:l:'..oKKdxNNx;cdxl;,''''..;xKKK0ock00OOd'.,c:,,,''ll',,..,::,,lc.... .......',;;:::::;'''.',,,,;. ..   ',..';..........   . .....,;.....");
         System.out.println("ddddxxdl:cloxkkOOOOOOOOOOOOOO0000o'.;,.;:lxo,,;:cc:'.,llcxKd,';:l:;''oXXdxNNx,:odl,''.''..;xKKKKockKK0Kk;.,:,.....ldcdko:'....:c'     . ...........',;,,,,,''',:'....  ':..,;. ..........    ...........");
         System.out.println("ddddxxxdlc:;:cldkOOOOO00000000000l.';,'odoko;,,:cc,..c0OdOXd,,;cc;;',dNKokNNd,:oo:'''',,,.;xKXXXkoOXKKX0l;:coc:,..lkxO0KKxdo'.:c'....    . .. ...........'',,,,:,....  .:'.,;.     ...........  ........");
         System.out.println("ddxxxkOOkxxxdlloddxkO0000000O00KO:.';,,ddoOl,;',:;:c:dKOxKXd';:cl,,',kNKokNKo';lo:'';:::,';oO0KXOx0NNNNKxooodlclc,ckkOKKKK0d,,cc,......  .       .;'...........;,..... .;;.,:' ..  .............;;......");
         System.out.println("xxxxkk00OkkkOOOOkxxddxxk0000Oddxo,.':,;xodOc;;';;;clckXOkXXo';::c,'.;kKOodOk:.;oo:,'';cc:';okkOOkdOKKXXKxoodc.....:xkkOKKXKx',cc,     .    ......,:'...........,;..... .,;.':'.    .........  ..::......");
         System.out.println("kkkkkOOOOOOOOOOOOOOOkxxddO0OOo,;;..'c,:xodkc,,',;;::;xKkxNXl';;:c;;;cxOd:cdo:.,lo:.''.....;cccllccloodxo;;cdc... .;dxOxk0KXO;':c,....     ........'.. .. .  ...';..... .,,..,'.     ........   .... ....");
         System.out.println("xkkkOOOOOOO0OOOOO000000kcdKOxl,;,..,:;cdccoc:;',:;c:,xKxxX0:.,,,;;,';c:;'..,,';ll:,,,'....;c:::;;;;;;;;'..':,... .,odc;:xKKKo';:'.';:'..  ..          ........ .,.  .. .';..''.     .......      .......");
         System.out.println("kkkkkkOOOOOOOOOO00000O0koxKOdc''...,c;,,,;c;;;,::;::ckOdx0d'.;cc:,'...''',;;;;:cl:,.....  .....'..',,,''.',;'..   .c:..':kKKO:',,..';'.... ..       ....  ......,'...   .,...,.     .......     ........");
         System.out.println("xxkkkkkkOOOOOOOO00000kOxlx0xoc''...,c,.,:od;;;;c:;:clddldxl,'coc:;;;;;::clllcc:cl;'....'........         ..'....  .''....,d00o;;,'...             ..............,'....  .,...'.     ..  ....   .........");
         System.out.println("xxxkkkkkOOOOOO0000O0000xlxOol:,'...;dc,cldo,;;;c;';:cccccll:;clllccccccc::c::c:::,...................''....................:oc,,'..   .      ....   ............''..... .....'.     ... ....     .......");
         System.out.println("xxkkkkkkOOOO0OOOO00OO00kodo;;,,'...;docoodo',;:l:;:clooddxdlc:cc:::;,;:cccllloool,............. ....................  ........ ...         ......... ...   ......'. .   .''..'.     ... ....     ..... .");
         System.out.println("kkkkkkOOOOOOOkkOOkOkxxxocxkl;,,'...:olcccdl,;:cc:loddxxxdddl:::::::ccoxxxdddolllc,'................. ......... .  . ..............           ....................,..  .  ....'.      ..  ....    .. ... ");
         System.out.println("kkOkkOOOkkkdkOkxdxkkxxdocdkl;,,'. .:l::lcoc,;,;:codooodddxdollllooooooooollolc;,,'.'lllc:;,''............................................ ......  ...     ..........     .....'.     .................''");
         System.out.println("OOOOOOOOkkxxOOxdodOkodOdcdko:cl,..';;,;:;::;;,;:lddddxxddddolllllccclooll:;cl;',;'.':cccccc::::::;;::::::;::::;,...'.'',,,coc,,,,;;;;;,;,,,;;;,'.............. ..       .;,...........',,:cccclllooooddd");
         System.out.println("OO00000O0K0OOOkocdkdlooc:oxl:lo,..';,....',;;;ccoddoddooooolcclll::;;,,,,',lolclc'.'lddooddddddddddoodddooolllolc::cllllloxOkxxxdxxxxddddxxxxxddollllllcccccc;........',ldoccccodddxocokkkkkOOOOkkkkxxxd");
         System.out.println("KK0000KKKKKK0OOxooxxxoc;,col;;;...,,....'',,';ccooooooooollc:;,,,'.....',,,,,'....  .:lddddddddxxxxxxxdxxxxdolloooolllodddxddoodoodddddodoodddxxxxxdoooolclddxdlodxxkkkkOOkkOOOOOOOOdclxxddoollc::;;;,''");
         System.out.println("K000KKKKKKK00Okxddxdc,;,,:ol;,,...','''.''''';::clccclc::;,'...........                .;codddddddddddddddoooddddddxxxxxxkxol:,;;;;;:::cclloddododkkxxkkOOO0000xdOOOOkkxxdoolccc::;;'.......            ");
         System.out.println("KKK000000KKXXXK0xl:'','',:l:,,'...''.''''''..',,;,''........                              .':looooolllllooc:ckX0dodddodddo:;,.';,',,:oxddk000OOkdx00OOOkxdoolcc;,,,,''.....                             ");
         System.out.println("dxxxkOO0KK0OOkoc,..',;;,':o:,,'.......'....''....                                            .',;,,,,''',:::c:dOxllooooollxdoc;;;coooodooollc:::,',,''.....            ..                               ");
         System.out.println("',;;:ccclooollc::::loc::,cl;,,.............''.                                                  .''..,clodxxdloddxdxxddl::ccc:;''''.......                      ...   .......     .                     ");
         System.out.println(",;:;:codxO0KKXXOdddkkooc;:c,',..........''........                                             .;ol;';::;,,,''''.........                     .        ..        .... ..............                    ");
         System.out.println("loxO0KXNXXKKKKXOxxxkkd:,;l:'',. ........''.......''.                   .  ...   .               ....             .   ...         .......... ...    . ....... ...........................  . ........    ");
         System.out.println("Ein modernes Haus mit einem Schild davor.");
  }
  static private void looksAtSign(){
         System.out.println("  ___________________ ");
         System.out.println("/                     ");
         System.out.println("!                    !");
         System.out.println("!  Viktor Goldfang   !");            
         System.out.println("!                    !");
         System.out.println(" ____________________/");
         System.out.println("         !  !         ");
         System.out.println("         !  !         ");
         System.out.println("         !  !         ");
         System.out.println("         !  !         ");
         System.out.println("         !  !         ");             
         System.out.println("         !  !         ");  
         System.out.println("         !  !         ");             
         System.out.println("         !  !         ");
         System.out.println("          __/         "); 
         System.out.println("----------------------");
         System.out.println("Der Name kommt dir bekannt vor...");                          
  }
  static private void insideBarInVillage(){
         System.out.println("              ..................................           .      .....   ..... ....',,,,,,,''',ldxkkxkkkxooolcccccc::::,''.....cxo;,,,,''',;c:'.........               .'.    ...  ................  ");
         System.out.println("                                              ........      ..     .,,'  ......... .....',;:cc;,',lxxkkkkkkkxxdlllloollc:;''....,ld:'''''''':loc:cccc:;'''.              ','    . ................... ");  
         System.out.println("                                      ....    ........             .','  ................,;clool:,':lodddoolc;;,,,,,;;:::,'.....;c;........,;;;:lk0Okl;;,,.              ';'.   ...'.''.............. ");  
         System.out.println("                                                                  .....  ......'. .......,:loddol:,'';cooollc::;;,;;;:;;,'......,;.............,dKNWXOo,';.              ''.    ..............'...... ");  
         System.out.println("                            ...   ........................           ............ . .....';ccllcc:,...,;::::;,,,'',,,,'''......................cKNNNNNO:',.              ..  .   .....'''''''''...... ");  
         System.out.println("                   .........''......',,,,;;,'................ .........'''....... ...',,,;::;;;;;,'............................... .......  ...:dxkkkxo;...                  .  ....',,,,,''''....... ");  
         System.out.println("..               ....''',;:lc;,'',',;cxOkxxdxkxlc:'...............,;,................';'.';::,......     .......                            ......',,......                     ....',,,;,,,'...''... ");  
         System.out.println("'...            ....,'.';lkXKd:,,,;;;oKNNkokKWNx:;,............';:col,','''..........,,.....,;..                                            .......''..'.                ...    ....,;,;:::;,...''... ");  
         System.out.println(".,,.           ...'::,...;dkko;,;clc;:ldxk0XKkxl,'............,lOxc:;,;cool;''....'',,'....''.......................              .       ..,;,....''..'... ..'''','....';,.    ....:c:codlc,...'.... ");  
         System.out.println(".,;.         . ..',,''.....'.....::,'''',cxkxl::;;;,...'.....';oOx:;;:lxKXkl:;,,'...','...'''...................'.......      .............'d0k:..'''..;:,..';clooc:;;,';:,.    ....loloxxoc,........ ");  
         System.out.println(".,:. ..     .''.;c;...''....... .:;..,'.';;,''''',::;';dd;..',,co:,,:xO0XXkolloc,,,'''',,,;:::;'.........,,,............      ....'','.....c0NNO;',,'..:c;..,looc:;....,:;,.    ....clccllc:,........ ");  
         System.out.println(".....      .':;':o:,ccc:;,';:.  .:;..'',:cc,'.......';cxOc.....';;;;l0N0dlcoxxdoool;;;,,;:codoc,,;:;,;cclddlccc:,..........   ...',;;''....':loc,',;,..;:;'',:::cdl.  ,od:,.    ....;c:;;::,''....... ");  
         System.out.println(".....     .......,'';;;cl:,;;.  .;,.,;;cl:''..':,...'c:,cc'.....,::::ldl::ckX0xO0xdol;,',,;coxxxx00dcd0X0OKKK000ocxlclc,....  ....';,'...... .,,.',:;..';:,','cxkkd.  .,,;,. .  ....,cc;;:;''''''.... ");  
         System.out.println(".','..   ..........  ............,..................,,;,;lc'.  ..,:ccc:;,,;coodO0OK0o;''..'';d0KXXNkcxKXK00X0kkkllOO0XX0l,'.. .............. .,,...;;..,cc;:l;:xddo' .  .;;...  ....,:lc::,'''',,'''. ");  
         System.out.println("..,;,.   .'',,,'....  ......    .'.  ....   ........'''':ooc.  .';:clc;,'',,;;;:clddc;,''.'',:looloc;;:cc:::;;;,',;::cc:,...  ..',;:c;....   .''...,;..:dl;oOc;ooll'..  .',;,.  ....,:c::;,,,,,,,;,'. ");  
         System.out.println("..,:'.  ..,,;::;,,,'...............  ....   .,::::ccccc:;,','  .';:clll;.....'',,,,,,,'..',,,;codl:,''''......'...',,;;,'.'.....';lxdc'...  ..,;'..c:..;oc;lOl,clcc,    .'cl'   ....,:::::;,,,;;;;;'. ");  
         System.out.println(";',:..  ..,;;::;;;;,'','...,'...''. ..'.    .,cc;;:ccccc,..:;. ...';,','...,clloooo:;;;;''loldKNNXx;,;oolollldk:.,d0xx0Kd,.......';:;'....  ..:l,..c;. .:;,lkl,clll;.   ..:c.   ....:c::c:;,,,;;;;;,. ");  
         System.out.println(",.',... ..''',;;;',,.,;,'.';'...''. .',..  ..;::::c:::;;,,:l,..',',;;:c,...,clllllc;;cc;'':co0WWWXx:,:kOk0KOxO0l.,d000XXo,'...':clool;.......':l,..:,. .ll,cko;cood:.    ....   ....:c:::;;,,,;:c:;,. ");  
         System.out.println("....... .'......'.','''''..'....',. ....  ...,,;;;;;,,''',:c'  ;lc::cod:...':lccll:,;:;'..,:o0NXK0o,';looxxoldx:',oKWWWXo''...':clol:'........:l,..;'. .do,;kx;cddxc. .  ....  .....;;:::;;;,;:cc:;,. ");  
         System.out.println("   ..    ..       ...           .'...'...,;..,cllllcccc::::,.  .::::clo;. ..,:::c:;cloc'..;:dKWNWKl,,'ck0OkOOkl'',oKNXNXo,'....,;;;;,.......'.;c,',::,.'kx,;xx:oOOOo..   ...   .....;;:::;;;;;cc:;;,. ");  
         System.out.println("  ....   ....................   ..''',...;;';xXXKkxxxxxxxxxc.  'ccccccl;. ....,:,;lc:cc,..,;oKWNW0l,,,lKWNXNWNx,',oXWWWXo,'......''''.......'.;c,,;,:;.'OO;,dd:xXXXk'.   .;:.  .....::;;;;;;;:c:;::,. ");  
         System.out.println(" .....  .',,,,,,,,,,,,;;;;,,,,,............,ldxdc';loxxdxkxo' .:xxxo:,,.. ...':c;,:;:c:'..,;l0WNW0l,,,oKNKO0XXd,';dXWWWXd,'.......'''.........,c,.'.,;.'xO;.,:;d000x,.   .,;'. ....';;;;;,;;:c:;;::,. ");  
         System.out.println("..'.'.  .';;;:;;;:::::::::::;;,.......  .''''.....lxxkkkkkxo' .:xxxd;.... .....'''..',;'..';l0NNNO:'''l0XkclO0o,':OWWWWNx;'...................,c,.'.,;..:o;....,::;:...  ..;c. ....';;;;,,,;:::;;;;'. ");  
         System.out.println("......   .;cllcclllc,,,,,,;;,,'......  .','.,'   ..',',;;;,'.  .,,''......................'';xK00x;.'.c0N0xOKKo'';OWWWWXd,'................  .':,.',;'  ':;''. .,'';'.. ...;c. ...,clcc::;;;;;::;;;'  ");  
         System.out.println("'.''.    .,lddoodxxdl:cccclllc,..........,,;:;,'''.''..',....  .,,'','..........'',,;;:::c:,,o000Olcccodoc:clc;'';okOOOkc'...........  ....   ..',;c;. .;lllc:,;;;;::;. .,',,. ..':llllccccc:::;',,.  ");  
         System.out.println(";,,'.    .,:::::cllc:;;;;;;;;,,,'''.................. .,;...    ';',cc::;,;;::ccccllllollloox0XKOxolcc:;,',,''.,c;''''''...................    ..';:;. .,clllllllllll:. .'','.',;:::::::::::;;;,',;'  ");  
         System.out.println(".....    ........''......''''..,''''',,,,;;::::::;;;;;;cc;'..  .'clllllllcc::;;;;,;;,,:odddolc;,'.......,,;;,,',;,',,.................         ..,;;,. .',;;;,;:;,;::,.  .....,,,,,,,,,,,,,,,,'''''.  ");  
         System.out.println("                      .....................'',,,,,,,;;;,,'.. ..........................,,',,,,,'....................................''...........',;'  .;:;,'''''..''.    ..  .',,,;;;;,,''..........."); 
         System.out.println("                                                  ........           .............. .';;;:::::'.','...'.............''''....       .'''.,,,,''''''.'.  .,::c:;;,,.....    ..   ....'',''',,,'''''.....");
         System.out.println("                                                                             .......'cl:'.;::,..,cllllll:....... ....',,. ... ......   ..,,,,,,,,,,;,'.','.',;:clc:;,.    ..   .......................");
         System.out.println("                   ........  ................................       ................:dl,....'..;oxdooc:,'.....    ...............'........   .. ...............',,,;;'    ..    ....... ............  ");
         System.out.println("                                ....................                   ........  ...,c;. ...',;:c:,',.......... .................'''......   .. .'.........'''','.....    .    .......  ...........   ");
         System.out.println("                                      ...........               ..............   ....''...',,;:,.......,,.;c;'...,,'',;;....     ......... .... .............''''.....    .   ..''.....  ......       ");
         System.out.println("                                                                 ............   ..''''',,,;,..........';;;dxxdlllollodxo,..      ..   .........            ................   ..'',,,,''''.....       ");  
         System.out.println("                                                                  ...........   ...''''''',,..........',;cok00OOkOOOOkkxl:,.........   ....  ..           .',;;::;;;;,,,,,,''..''',,;;;,,,,,''.....   ");  
         System.out.println("                                                                  .........      ......    ............',;oOK000O00KK0Okxdoc:;,,'..   ...    ......          ....''''''''''',,,,;;;;;;;::;;;;,;;,''..."); 
         System.out.println("                                                                  ......         ....'. .................;OKK0000OOOOOOkxxxdlc::;..   ...   ..    .           ..             ............  ...',,',,,'"); 
         System.out.println("Du siehst die ziemlich leere Bar vor dir...");                       
  } 
  static private void looksAtBar(){
         System.out.println("......              ......  ....                         ..      .. .          ...                                                ..                                              .                     ");
         System.out.println("....... .......               ........................ .. ..  ..  .....  ...            ....................                                 .......   ... .                                            ");
         System.out.println("....... .......               ........................... .......    ........  ..........               ......................    .          ..        ..                 .  ..     .                   ");
         System.out.println("......  ... ..                ........... .  ...................... .......... ..........   ...,,''.........      ..................         ...       ...  .     ... ..... .........  ...      ...  ...");
         System.out.println("..............                ........ ...  ..'...'............... ..... .........  ....    .,:ododddddolclc;;,'''.......       ......  .... ...  ................... ...............  .................");
         System.out.println("..............                ......'.  ..................................... ..........   .',cxkkkOOOxddxxkxddoollc::;;;,...   .....  .........................................................  ......");
         System.out.println(".....''.....'.               ....'....  .'........................ ..........  .........   .,,:dOOOOOOkxxdxxxxkkkkxxxxxdol;..  ......  .............................. ..................................");
         System.out.println("...........'......  .     ............  .........................   .........  .........  .':,;okkkOOkkxxddxxdxxxkkkkOOkkl;'  ... .......................  .......... ..................................");
         System.out.println("......'.......'.....'...'.';'......... .''...''..''..  .............'''...... ..........  .;c,,oOkkdoooooolcdoloodxkxxkOkc,. .... .......'.............'....'...''... ..................................");
         System.out.println("..................'........,.......'.. ...'..'..,;;;. ...''..'.... ..''.''..........'..  ..:c,,oO0kdlloollccc::cclodkxxOx:'..............''.......'............';'''....................................");
         System.out.println("'''','''''''''....'''.....,;'',,,,,,'..',,,,','',;,,. ..'''..'.'.. ..''',,''..........  ..':c,,lO0Oxxddddddoolccclloxkkko;'...''....'...'.'...''.''''''...'....';,,;....................................");
         System.out.println(",,,'.........'.....'......,;,,,,,,;;' .,,,;,,,'.,;,,'.....'..''''. .''''..... ..''........,cc;,lO0Okkoodlcoddocloodddxxdc,....................'..''..''...''..',;,;,.............'......................");
         System.out.println(".....'''',''''....''''''.,:'..'.''''. ..'''..'.',;,''.....'.........''.''.....''''..'''''.,cc,'cO0Okxodd::lool;;lloxddxd:,...'''..'''.''''...''.''.......,,,...',;;'......''.'''''''''....''.''....''...");
         System.out.println("'....'..''...,'...,,,'''';;'''''''''. .'','..'''........'''.''..''',...',,..',;'....''.''',cl;'ck0Oxdoxdldddddcclodxdxxo;'...'''...'.','''.';,'..','.'''.,;'.''......'........''''''''..''''....'''''...");
         System.out.println("'.............'..,'......;;.''''''''. .'........''''...''''.','''',,....,,..',,'.....'...';cl;':xOkxxddxxolkdoxdoodxxkdc,'...'''...'.','.'.','..'''''',,.',',;,,,,'........'...''''''....'''...''''.'...");
         System.out.println("'',''''...''''''',''''''';;.''','''...',,,,,,,'',:;;;,,''''.','.'','...';,..',,''''..'''.',lo:';xOkkkxodo;,:;;ldooxdxxo:'......''.''.'''''.,'..',,,,,,,;'''',,'.',','.''''','''''''.''',''''''''',''''',");
         System.out.println("'.''.,'.'..'',''''..'','',,',,'''''. .,,,'''.',',:,,;;,,.'''..,,'',,'''','..'.............,cl;.,dOkddxdoooc::loooddxkxl;'.  ..........'''..,;,'',,',;,',,,,'',;,,'''..'....''''........''..'..'.','''.''");
         System.out.println(".....'...........'..'''.';'',,'...'. ..''''''','';;;c;,,''''..............................,cl;.,okkxxxxddk00kooddodkkd:,.      .....................'..',',.'::,,.....''''''''....'''.',''......'''''..'");
         System.out.println("'',,,,,,,'''''...''.','.,;..''.'','. .',,,,;;,,',;,',.....................       .........,cl;.'lkxxxkkddxkkxoddodkkxo:'. ......          ...................'''...''',,''''',,'',,,,,''''',,,,,'',,,,,,");
         System.out.println(",,;,,,;;;;;;,'....'''''';;.,,'.',,'...,,,,,,;,;;'........  ...  ................'''',,',,',cl;..ckdlllclllcccldddxkxxl;'........................................ .,,,'',''''',,.,;,,,,'''''''',''''''','");
         System.out.println(",,,,,;;::;;;;,'...;:'.'':;.,,,',,,'..';;,,',;;;,..   ............'',,,,,,,,;;,,,;:lllllllc;co:'':ddllc:;;,,;:c:codddxc,'.',,',''''''''....''.....................''....'...''''';;,''',''''''''.''''..''");
         System.out.println("',',::;;;,,;;,,,,,:;.':;:;,,;;;;;,'..,;;;;,;:;;,..   ..',;;;::cclllloooooooddddollllododkxccoc''lkkxxxkdlcccll:cooloo;',;;::::::ccccccccccccccccccccc:;;;;;;;,'',;,'''''''';;',,,,',,',,'','..'..''.....");
         System.out.println("',',:,'''''''',;;;:,';;;;;,,,;,,''...,;,;:,,;;;;......,;;::cccllloooodoooooooooooooooooodo:cdc,,lkOO000OOkxxxddxxddo:,'''',,;;;;;;::::ccc::ccccccccc::::c:;::::c;'.'..'''..''',,''''''..................");
         System.out.println("''',;'''''''',,;;;;'',,;:;;,,''',,. ....,,'''',,. ..'',::cclloodddddddddddddddddddddddddddccdc,'cxxxkkkOOOOOOOkkkxxo:,',,,,;;;::;::::cccclllllllllooolooooooooodc''........,,'''.''....',''',,'.........");
         System.out.println(",'',;;;;'........... .......',,;;,. .',,,,,,,,::.  .,:codxkkxolloooooooooooooloooooloooolc:coc,.;ooloododxdxxxkkxkOo;''''',,',;,,,,,,,,;;;;;;;;;;;;:coxkkkOOOOOkc',,''..',',,,,,,;;,''',,''',,,,,,,'''',");
         System.out.println(":;,;:::;,'.','....'......    .,,,'. .,,,,,..,,;;. ...';:ldxkd:,,;ldocc::;ldl,.;odoc;;:;cldl::;,',:c:;:cc;;::ccloloo;'..'';:;,;::c:,'................'lxkkkOOOO0kc,''''...,'',,''.','''''...'''',,''.....");
         System.out.println(";,',;;::c:,::,'..,,............','...',,'.....''. .',:cldkOOdc:ccodol:;,,cdc..lOd:''colldkl;,'..'.....',''''',,,;;;,..,'';c:;,,coc,'................'lxkkkOOOkdl;'.',;...,'',,''.'''''''''',''.''.''''',");
         System.out.println(";;:cc:c::;,:c:::cc;;,''.....,,,,,. .',',:;'...'....,:codxOOOd::;::;,''...;c,..;lc::lOkl::c:,.. .........................','.,;:c:,'................ .cxkkxdxkoloc,';::,'::;;;;,,,,,,,,;cc;,;c:'.''''''''");
         System.out.println(",coc;:;;:;,:c::cdl;:;'.......'''.. .','':;.',..'...,:lodkOOOd:,;,';:;'.,.',..',,ckkxxdddoc;'. .''''''......''.....   .......';:c,........ ..... .   .:xkOkxkkxxkd:,,;::;::,,'',',,''''',:;,,:;'....'....");
         System.out.println(";lc:;;;;;;,;:;;cdc,,,,'...  .....  .''.,:,',;;;,...;clodkO0Oocol,.,;;;cl,...':,,lddl::oxddl.  ......''.''..''.''.   ..........,c:..........  ...   ..:xkOOOOOOOOx:'.',,,;,,'','''''.''.';;,,,,'.''''''..");
         System.out.println(":cc::::;:;,;:;;::,..cl'.'. ......  .''.',,,,,:c,...;cldxkO0Ol;:cc::::;;c;.....'::codxkOkdl:...   . ...  . .....     ...  ......,'....................;dkOOOOOOkkxc',;;,,;;;,,,,,;,,::,..''',,''''',,,'''");
         System.out.println(":c::::::::;;:;:::,.'c:',........   ....'''''',;....,coxxkO0kl,';cc;;,,';l,.',,lo::oxlccllc;;:;. ...                 ... ....... ............. .......;dkOOOOkkxddl,,,',,,,,,,''''..;;..'...,,'..',''''..");
         System.out.println(",,,;:;;;;,;,...... .............   ......''',,'...';codxxkOkc,',;;'..::''..c:;c:cdkkxddl:cdkd:. ...     .   ...     ......   ....  ......... ......  ,oxkOOOOkxkkl'...''','','''',;;,'....'...'''''''''.");
         System.out.println(";ccccccccc:;,'.''.  ............  ..''',''',,,'...,:lddkO00kc,;;ll::lodl;..,,;:,:odxxxkl,;cl;'. ...........  ..    ........................  .,,...  'oxkOOOOkddkl'''''''''.,;;,,,;;,,,,;;;,,,,,,,;,,,,,");
         System.out.println(";cc:cc::cc;;c::l;..  .....  ..'.  .',,,;'..'',....,:ldxkO00d:,,;;::;;:::,...,od:,;c:,;;'...''.. ............... .. ...........................,'.....,oxxOOkkkooxl''..''''.':;'',,'','..;;.',,''..''''''");
         System.out.println(";:;;;:;;;;;;;;lo;..     ..   ...  ..'',,',,,,;'..'';ldxkO0kl:,',:oxolxkd,...cdoc;''............ ..............  .  ..................................,lddxkkkOxxkl...''',,',;;'','',,,,;:;'','..','',,,'");
         System.out.println(":::cc:cccl:clclc,..'.   .   ....  ...',,,;;;,;'....,:ldxkkxc;;;;:oxxdxkx;..;c;;',;,'...........  . .......         ...................................;ldkOxdkOOOo,';:;,,,,,;,,;,,;;;::,,,,,,,,:ol;,,,,,");
         System.out.println("ccccccclll;';:;....,,...  ......  ..',,''',,,,. ...',:ccloo:;c:coo:,,;:;...''...,;'....................... ...    ..................... .... .... ....,oxkOkxxxkkx:',;,''''''''''''',c:,,,'','.,::,'',,'");
         System.out.println("c:::;;::cc::cc:;;:;,'........... ...'',,,,,;;'....',;::;;lo::l:;cc:;;;;;;;;:;;;;;;;,;;,;;,,,,,,,,'''''''''''','................................''.'''':oxkkkOkkxxd;.',;;,,,,,,;,,;,,;;;;,;;,''',,;,'''',");
         System.out.println("ccc;,:cclccccllllccc:;,''.',.... ..',';:,',;;,....';::clloolooooodddddddxdxxxdddoooooooddddddddddodolooodooloolllcccc:::;;;;;::::;;;:::::::::ccllclllcllllodkOkxddl,,:cc:::;:clc;;;;:;;;,cl,'''','....''");
         System.out.println("ccc;;lccc:cccllcc::::::;,'',.... ..'',cc''',;;. ...';cclllloddooddddooooooooooooooooooodddddddddoloollooollcccccc:::;,,,,,,''',,,,,,,;;;,,;;;;;::::ccccc::::ccccccc;;::;;;;;:cc:,',,;;;,':l;....'...''''");
         System.out.println(";;;:olc::::ccclllc::ccc;;,,,.... .''',lo:;:;,.. ...';;;;;:::;;;;;;;:::;;;;::;;;;;::;;;;:;::::::;;,;;;;,,,'''',,'...................................',''''',''''''',',:::;;::;,;::;,,,;;;,;c:;,,,;;;;;;;;");
         System.out.println(":::clccc;;cccccllc::ccc;,;,',,'..',,,,,;;,;;,'.   ..................................................................................................................;c:;;;::;;:ccc:;;:c:;,,;;;;;,;;,,,,;");
         System.out.println("ccc:cllc::llccllcccc:cc:::;;;,. .;;;;,,,,'',;'........''...',..','..,,'..,;,.',,'.',,..','..','..,'....''.........................................'....'........'...;::;;;::;;::c:;;,',;''''',,,'',''',,");
         System.out.println(":;;;;::;,;c:;::::::;;:::;;::;'. .','',;;;;;;,...  ........'....'''..'''..''..''...','..','..''....'....'............................................................''',,,;;;;;;:;;:;,;:;;,,,;,;;;;;;;,,");
         System.out.println(",';::;;;;:::;;;:c::;;:;;;:::;,. .''':c;:::::;'.     ..................................'.........,,,''..............................................................',;;;;;,,;;;;;,;;,;:;;;,;:;,;;,;:::;,");
         System.out.println(";;lc:::::cc:::;::c:::::,;:;;:,...;,,cc,;;;,;;... .....',,;;;;::::::;;,,,,,;;;;;,;;;;:ccc:,,::c::lolc;'';:;::;;;:;''',,';c:;;,,,,,''''''''''''''''',,,,,,,,:;,;,',,.';:;,;;,,,,,'.,:,,,;,,,..;,,,,'',,,,,");
         System.out.println(";cdl::cllllllllllllcllc:colc:,. ';;:c;,,'.;:'..  ....';::colclllc:,'.'',,;;;;:ccc::ccllc:,;ccolc;;;,'..,;cll:::;;..':c::l:;;,,,,,,,,,,''''',,;;;;;::;;;;;:lc:::;::'.,,''',;;,,,,,''...',,',::,,,'.''..''");
         System.out.println("loolcclooddooooooooloocllccc:'..';;:;,,,,',;,.   ..''';::lollloc,'.',;:ccclccccccc::clll;.,ccllc:::;,..,;:lc,..''...,:c::;,',,,,,'..'...'',,',,;;;;;;,;;;:cc::c::;'.;:,',,;:::::c:,'..,;,,,;;'''.       ");
         System.out.println("c:ccccccclllllllcccclc;:cc::;. .,;;;;,,,,'.''.  ...'',;;;lolcc;'.',cllloooocclodolc;:llo:';cclc:cclcc,,cc:cc;,,::,..,;cl:,,,,;;,...'',,;;;:ccc:::::::;;;,:cccccl:;'.,;;,,;;,,;;;,,'',';;'''...,,'.      ");
         System.out.println(";:c::cc;,:lccccc:c:clc;;:::;,..':cc:c:;;;;,'..  ....',;;:cllc;..';cldxddxdooollxxoc;;cllc;;:ll;;c:cc:,;cc:ccc:;::;..;:cc:;;;;:;...,:::c::cc;cllc:cc::;;;;;:c::cl::,',;,,,,,,,,,;,''',,co,''..',,'.      ");
         System.out.println(";;,,,;;,,:c;;;;;;;;:lc:ldlcc,..;looollllllc:'......,;;;,,,,,,,'';cloddllllc:lllddl::;;;;;'.':c:clloc;,:lc:llc::;:,',:c:;,','','..,:;:cc;;:;,,:c::cl:;;,,,,,..';:;:;',:::;,'''',,::,,,,loc:::::::;,......");
         System.out.println("c:ccc::cc:;::::::::cc::odlcc,..'::;,;;,,,,;;........'',,,.......',,,,''''''''',,,,'.........;cccllcc:,,cc:ll:cc:;;',c::cc,.......',,,,,'''''.''',,'......''.';::::;',,;:;;;;;,';ol;,;;;;;:,,;,,,',;'''.'");
         System.out.println(":cccc:ccc::::c::cc;::;cooc::,..':;';c,,,,,;,...  .....,:c,..'''''',''''''''',,''',''''';lc;,;::lol;;:,,cc:cc:clc;,'';;:cc;..............................;lc::cc::c:'',,,,,,,,'';do,,;;,;;:,......,,.....");
         System.out.println(":::::::c:::::c::::::;;:llccc,..,:::oo:::cc:,... .....';:c,',;::cclolcc:c::clolccllc::;:lol::::cloc;;:,,cccccccol,',,:::c:,...'''..'...........''.......'clc;;:c::::,,;:::::cc::coocccccc::;..',,::,.',..");
         System.out.println("::::c;;llclcccc::ccc::;::;::' .,;;::;,,;;;;;... ....',,::'';:clllodolccoccoddoloddoclccollc:c:codl;;:,,cccccclol;,,;:;;;;:,..';:,,::,,::c;;::col::;,,;',cl:;::cc:::,';;;,;;;:;;;;;;;,,;;;:;.....',,.,,'.");
         System.out.println("cc:cc;,cccllc:::::c::::;;;:;. .,:;;;,,,;;,;'... ..'.',,::'';clllloxdollolloddlloddollcllllc:cccodl;;:',cc::cclol;,,;:;;:::;'',cl;;lc,;:cl:;:cldoll:;::,;ll::c:cl:::;''',,,,,'''''''''''';:,'''.....,,'..");
         System.out.println(";;;:;,;c::c::;;:;;:cc:;:c;,'...::;:c:ccc::,'..  .'''',,c;',;clloooddoloolloddoloddocollllc::cccldl;,:',cc::cclll;,,:lc::::,'';cl::oc;::ll:::clddllc:::;,cl:cc:cc:;;;'',;;,''',,,,,,,,,,':l,..'''..';'..'");
         System.out.println("::::::ccccccc:;:lcc::clllc:,..;l;,clcccc:cc,.. ...'',,;:,.';clloooodoloocloddoloddocolllcc::cccool;,;',cc:cccol:;,,:lc::::,'';ll::ol::ccoc:ccokxolc:c:;;clcllccllc::,';c:;;;:::::c;;::::::;'.....':c;...");
         System.out.println(":cll:::c:ccc::;;lcc,;olccc;. .;;.,c::::;;:c'..  ..''',;:'.,:llloooddollocloddoloddlcolllcc:cccclol;;:,,cc:cccolc;,,:lc;;;:,.';cl::ol:cccoccc:lxxollccc;:clc:c;;c:::,.';;:;,;;,,,;;,',;;,,,;;;,;;,,,:l,..");
         System.out.println(":cllccccccc:;',clc:';occcc;. .;,'cl;;;::::;......','',;;'';cllloodxollooclddxoloddlcoollcc:::cclol;,;',cccccclol;,,:l:;;;:;',;co::ol:cccolcc:lddollcc:;;cc:;:,,,,;;,.':;',,;;;::;;,',::;,'';,,cc,,,;ol;,");

  }
    static private void looksAtVillageDoor(){
         System.out.println("XXXXNNNNNNNXXXXXXXXKKXKKKXXXKXKKKNNNXKOkkOdclkkxxkkkkkkkxdddoclllcc:;;,,,'.......',,;;;:cclooodxxxkOO00000kkOkdO0xdx00KXKKNNNNNNNNXXXKXXXXXXXKKKKXXNNX");
         System.out.println("XKKKXNXXXXXNXXXXXXXK0KKXXXXXNWX0OkxkOxookkxxkOOkxxxxdoolc::,......    ....  ..  .............,:cldxxkxkOOO000OOOxxOkddxxk0KXNWNNNXNNNXKXXXKKKKKXXKXNXX");
         System.out.println("OOXNNXKKXXXKXXXKKKK0XXXKXNWWNX0xxxocoxOkxxkOOkxxddolc:,'...  ........',,,,.............'...........',:loxkkkOO00K000koodddoxOKNNNNNXXNNXKK0OOO0KKXXKKX");
         System.out.println("OKNX00KKKK000000OO0KK0XNNNX0OkxddddxkkkOOkxxdoolc;'.......',,;;;,,,,,::::;'.','..'''''..''''''''........';:ldkkkO000OkkkkxxkOOkOXNNWK00OOOOkkkkkO0K0Ok");
         System.out.println("OKNK00KKKKKKKKK0KKK0kOXNK0kloxxxdodkkxddddolc;'......';;;;;;,,,,,,'',::::;'.';'..,,,,'..'''''''''''''''......,:ldkkxllk0OO0OkkxOKKNNXKXXXKXXXXXXXX0OkO");
         System.out.println("0NWNNXXXXNNNNWNNWWNKOKNXOkxdxOxodxkxdlcc:;'.....',;;,,,,,,,,,;;;;;,',::::;'.';'.',,,,,'',;;;;;,,''..'''''''......,:cloddddkkO0OOk0NWNXXNNNXNNNXXKOO0XX");
         System.out.println("XNWNWWWNXXXKKKKKKK00KNWXkookkdxkkdoll:,. ...',;;,,,,''';::::::;;;;;,,::;;;,',,'.',,,,,',;;,;;;;;;;,,'..''''''''.....':lxxdok00K0OKNNNNKOkkkkkxkO0KKXXX");
         System.out.println("0KK0KKKKXK0Okkkkkkkk0XX0kxdkxdddolc;.....';;;,,',,;,;;::::::;;;;::;,,:;;;;,',;'.',,,;,',;;;,,,;;;;;;,'''''''''''''.....':oook00000O0XXKK0000000KXKKKKX");
         System.out.println("KKKKKKKXXXXXXXXXNNNXXNN0kdloolcc;'. ..';;;;,',;;;;;::::::::;;;::::;,,:;;;;,',;'.',,,;,',,,;;;,,,;;;,,,,''',,'''''','''....':dOOk0KKXNNWNNNNNNNNNNNNNNN");
         System.out.println("NNNNNNNNNNWNNNNWWWWWWNKkxkd::;'. ..';::;,'',,;:;;;:::::::;;;::::;;,',;;;;,,',;'.',,,,,'',,,,;;;,,,,,,,,,,,''',,,',,,''''....'cx00OOKNNNNWWNNWNNNNNNNNN");
         System.out.println("NNNNNWWWWWWWWWWWWNWWNWKxooxd;  ..,;:cc:;;;;:::;;:::;;:;;;;;:;;;;,,'',;;;,,,',;,.',,,,,'',,,;;,,,,,,,,,,,,,'';;;,,,;,,;;,'.....o0kOXWWWWWWWWWNNWWNNNNNN");
         System.out.println("WWWWWWWWWWWWWWWWXXXXNNXOkdlc'..',;;:::::;;;;:cc:;;:::;;;;;:;;,,,,,,',;;;,,,',;,.',,;;,',,,,,,,,,,,,,,,,,,,;::,''',,'',::;''...;kKK00KK0000XNNNWWNWNNNN");
         System.out.println("WWWWWWWWWWWWWWN0kkxOK0xloxkl...''';c:;;;;,;;;;cl::;;;;;;;;;;,,,,;;,',;;;;,,',;,'',,;;,,,,,,,,,,,;;;;,,,,,;::,.''',,,,''::,''..l00kkO0kxdxO0XNWNWWNNWWW");
         System.out.println("NNNNNNNNNNNNNNXOxocldxddollo;....,cl;;:;'',;;,;cc;,;;::::;,,,,;;;;,,,;;;;,,,,;,'',,;;;,',,,,,,'.',;;;;;,,:c;,,',;'.','';:,'...;ld00kxolloxO0XWNXXXXNNN");
         System.out.println("XXXXXXXXXXXXNXOoc:;,,:clc:......',:l:;,,,,c:,;,;c;:c:::;,''',;;;,,,',;;;;,,,,;,'',;;;,'',,,,,''..',,,;:::cc,''',;,''',,:;'......':;,';oxxkO0XXXXKXXKXN");
         System.out.println("WWWWNXKKKXXXK0xooc:;..     ....''',;::::::;,,,,coc::;;;,,;;;;;;;,,'';;;;,,',;;,.',;;;,'',''',,,'''',,,,,:ll:,'',,::;;;;,'''......    .;x00Okk0000OkOKN");
         System.out.println("WWWWNK0OOOkkOOxddxd:.     ....''',,,,,;;,,,,,;loc;;;;;,;;;;;;,,,',,,;;;;;,;;::;'',;;;,'',''',,,'',,,,,,,,,;c:;',,,,,''''''''''........'kXKOO00OOOxxOKX");
         System.out.println("WWWNNK0kkOdok0OkO0ko.  .....,,,'',,,,,,,,,;:cc:;;;;,,,;;;;;,,,,,,;,,;;;;;;:c:;,'',,;;,',,,,,'',,,,,,,,,,,,,,;::;,,,,,'','',,,,,'','...;OK0kOOkkOkdxOOK");
         System.out.println("WWWWX0OkkOkkkkdlodkx'......,:;;;;;;;::;:clllc;,;,,,,;;,,,,,''',,,,,,,;;:c:;;::,.',,;,,'',,,,'''',,,,,,,,',,,,,;:clc:;::;:cc:;::,',;,..:00kkkO0OOOkk0XN");
         System.out.println("NNWWNKKKXXKKKOOxddOd'......;loolllodo:;;oxdolcclllllllllc:::::::::;,;::cc:;,;;'.',,,,,,,,;;;;;;;;:::::cc::::::::loc,';clooodddl,.''''.:0XKK00KXK00KXXN");
         System.out.println("NNNNNKKKKXXKXKOO00Od,......'clllcloooc;;lxdl:;;::::::;:;;;::;;;;;;;;;;;::;,',,'.',,,'''''',,,',,,,,,,,;,,,,'',;;cl:,';c:;:loool;..'''.c0KK0OKXNX0O0KXX");
         System.out.println("NNNNNXKXXNNNNXOO00Ok:...'..',;;;;,,;:::::cllc;',,,''''',,,;;;;,;;;;;::::;,,',,..',;;,'',,,,,,',,,''''.'''''';:c;''','';,'',;:::;;::;'.:OKKKKXWWWNXXNNN");
         System.out.println("NNNNNXXNNWWNNKOOK00k,..'''.',,,,,,,,,;;;;,,;clc;,,''',;,;;;;;;,;:::;::;;;;,',,..',,,,,',;,,,,''',,''''''',,;c;,'''',,,,;,''',,;:c:,'..l0XXXXNWWWNNNWNN");
         System.out.println("NNNWNXXNNWWNNKO0K0Ox;..''..,;,',,,;;::::,,,,,;ll:,',,;;;;;;;;;::;;;,;;;;;;,',,..',;,,,',;;,,,,,'''''''''';c:,''',,,,,,,;,;,'';c:'..'''lKXXXXNWWWNNNWWN");
         System.out.println("NNNWNXXXNWWNNKOOK0Ok;..''...;:;,,;:;,,,;c;,,,,;ll::;;;;;;,,,;:cc:;,',;;;;;,',,..';;,;,',;;,,,,,,''',,,,;;;:,''',;,''..,;,,;;,,,'.'''''lKXXXXXNWNNXNNNN");
         System.out.println("NNNWNXXXNWWNNKOO00Ok;..'''.',;:;;lc'','',',,,,,cc,;;;:;,,,,,,;::;,'',;;;;;,',,..,;,,;,'',,,,,,;,,,,,;;,,',:,''',;'.'''.,:,''''''',''''oKXXXKXNNNNXNNNN");
         System.out.println("XXXXXKKKXNNXXKOOK0Ok;...',',,;;;:lc'',''.',,',:c;,,,,,;,,,'.,;;;;;,',;;;;;,,,,..';;;;,'','',;,'..',,,'',',;;,''''...''';:,.''.''','''.ck0KK0KXXXK0KXK0");
         System.out.println("00XXKK00KXXXXKOkOkkx;..'''';;;;,,:c;,'''''',;::;,,,,,',,,,,;;;;;;;,',;;;;,,',,..',,;;,''',,,',,,'''''''''',,,'''....',,,'..'''''','''.ck0KKKXNNNKKXNXK");
         System.out.println("XKNWNNXXNWWNNX0OK0Ok;..','';;,,,'';::;;;,;;::;,,,,,,;;,,,,,,,;::;;,',;;;;,,'',..',,,;,'''',,,''',,,,,''''''',,''',,,,,'..'''''''',,'',dKXNNXXNNNNXNNNX");
         System.out.println("XKXWNXXXNWWNNKOOK0Ok:..',,',,,,,,,,,,,;;;;,,,,,,,,;;,;,,,,,;;::;;;,',;;;;,,'',..',,,,,'',,''','''',,',,,,,,,,''''''''...''''''''',,,',dKXNXXXNNNXXNWNN");
         System.out.println("XKNWNXXXNWWNNKOOK0Ok:..','.',,,,,,,''''',,,,,,,,;;;;;,,,;;:::::;;;;',;;;;,,,''..',,,,,'',,,'',,,'''',,,,,,,,''''''''.....'''''''',,,',dKXXXXXNNNXXNNNN");
         System.out.println("K0XWNXXXNWWNNKOOK0OO:..'''.,,,,,'''',,,,,,,,,,;;;;;,,,,;::::::;;;:;,,;;;;,,,''...,,,,,',,,'','''''''',,,,,,,'''''''''''...''''''',,,',dKKXXXXNNNXXNNNN");
         System.out.println("KOXWNXXXNWWWNKOOK0OO:..''..,,'''''',,,,,,,,,;;;;;,,,,;::::::;;;:::;',;;;,,,,''...',,,,',,;;,',,'''',,'''',,,,,'''''''''''...''''',,,',dKXXXXXNNNXXNNNN");
         System.out.println("0OXNXKKKKXNNX0kkOOkk:..'''',,''''',,,,,,,,,;;;;;,,,,;:::::;;;;:::;,',;;,,,''''...,,,,'',;;;;;,'''''''''''',,,,,'''''''''''''.'''',,,',dKXXXKXNNNXXNNNN");
         System.out.println("K0XNNKKXXNNNX0kk0Okx;..'''.''''''',,,,,,,;;;,,,,,,;;;::;;;;::::::;,',,,,,,''',...',,,,',,,;;,,,''''''''''..''''''''.''''''''....',,,''oKKXXKKNNNXXNNNN");
         System.out.println("KKXWNXXXNWWNXKOOK0Ok:..'''...'''''''''',,,,,,,,',,,,,,,,,,,,;;,,,,'',,,,,,'''''..'',,,''',,,,,,,,''',''''''.....''''''''''''''''''',''dKXXXKXNNNXXNNNN");
         System.out.println("KKXNNXXXNNWNNKOOK0Ok:..''''''''',,,,,,,,,,,,''....',,,,,;;;;;;;;,,,,,,,,,,'''''..''',,',,,;;;;;,,,,,,,,,,.......'',,,'''',''''''''''',dKXXXKXNNNXXNNNN");
         System.out.println("KXNNNXXXNWWNNKOOK0Ok:...'..''',,,,,,,,,,,,'''..'....,,,,;;;;;;;;,,;;,,,,,,'''.'...',,'',,,;;,,,,,,,,,,''.........'',,'''''''''''''''',dKXXXKXNNNXXNNNN");
         System.out.println("XXNWNXXXNWWNNKOOK0Ok:...''.',,,,,,,;,,,,,,,,.. .....',,;;;;;;;;;;;;,,,,,,,'.','..'',,''',,,,,,,,,,,,,'''''..  .'..''''''''''''''''''',dKXXXKXNNXXKXNNN");
         System.out.println("KXNWNXXXXNWNNKOO00Ok;......''',,,,,'',,''';,......,'.',,,;;;;;;;,,,,,,,',,''''...'',,''''.'''''''''','.'.......''.....'''''''''..'''''oOO00O0XXXK00KKK");
         System.out.println("XXNNNXKKXNNNXKkkOOkx;.......',,,,'''',,''';,'..'','..'',,,;;;;;,,;;,,,,,,,''''....',,''''','''''''''''..''....''.........''''''..'''.'o00K00KXXX00KKKK");
         System.out.println("NNNNXKKKKXNNX0kk00Ox;......',,''''',,,,,,'','........',;;,,,;,,,;::,',,','''''.........',,,'..'''''''''..'.........'''.'..'''''..'''.'oKKXXKXNNXKKXNNX");
         System.out.println("NNNNNKKKKXNNX0kO0Okx;......''''',,,,,,,,''''.......'',,;;;;,,,;;:;;,,,''''',''.................''',''''.............'''''''..''..'''.'dKKXXKXNNXKKXNNX");
         System.out.println("NNNNNKKKKNNNX0kO0Okx:.........',,,,,,,''''''''''''',,,;,,,,,;;;::;;,',''''''''..........'''''...''''''''.............'''''''.''..','''dKKXXKXNXXKKXNNX");
         System.out.println("NNNNNKKKXXNXX0OO0Okkc.......'',,,,,,''''''''''''',,,,,,,,,,;;;;;:;;,,;,,,,,,''..........'''''''..'''''''''...'''''.....''''''...',;;,'dKKXXKXNXXKKXNNX");
         System.out.println("NNNNNKKKXNNXXKkk0OOkc'.....''',,,'''',''''',,''',,,,,,,,,;;;;;;;;::;,,,,,,,;,'.............''''''.'''''','''............''''''',,;;;,,dKKXXKXXXXKKXNXX");
         System.out.println("XNNNNKKKKNNXXKOk0OOk:......'''''''',,'..''.'',,,,,,,,,,,;;;;;;;;::;,,;,''',,;,...............''''''''''''''................'',;;;;,,''dKKXXKXNXXKKXXXX");
         System.out.println("XXXNXKKKKXNXXKOO0OOx,......'''''''''..'''''''''''',,,,,,,,;;,,;:;,;:ccc;''''',,.....'.........',''''''''''....'','..'......'',;;,,'..'dKKXXKXNXXKKXXXX");
         System.out.println("XXXXXKKKKXXXXKkk0Okd'.......'.''''..'''.',..''.'''',,,''','''';:,,;:ccc;,,;:;,'.........''...''................'..........','.','''..,dKKXKKXNXXKKXXXX");
         System.out.println("XXXXXKK0KXNXXKOkOOkx,.......'''....'........''..'''''''''...',;;;,,,,;;;::;;,'..........'''''............................'',,''..'''.'dKKXKKXNNXKKXXXX");
         System.out.println("XXXXXK00KXXXK0kkOkkx,...........''''''''''''''''''''',,,''',,,,;;;,'',::;,'..............''''''''........................'''.........'dKKKKKXNNXKKXXXX");
         System.out.println("XXKXXK0KKXXXX0kkOOkd,.........''''..''''''''',''''',,,',,',,,'',,;:,',;,'..................'''''''''.................,;,'''..........'o0KKKKXNXXKKXXXX");
         System.out.println("XXKXKK0KKXXXXKkkOkxo. ........'...........',,'.''',''',,'''''',,,,,,,,'....''................''''''''...............'',,'.............o0KKKKXXXXKKXXKX");
         System.out.println("XKKKK000KXXXK0kxOkxl. ...........''.'''''''''.''''''''''''''''',''''.'..'............'.........................................'..''''o0KKKKKXXK00KXKK");
         System.out.println("XXXXKKKKXNNXK0OkOkkl.......''''''''''''''''''''''''''''''''''''''''...................................................................l0KKKKXXXK00KXXK");
         System.out.println("NNNNXXKXNNNNXKOkOkko. ......''''.........'''...'''......''''''''.....'..'.............................................................o0KKKKXXXK00KKKK");
         System.out.println("NNNNNXXXNNNNXKOkOOkl. ............................'''''',,,,,''''''...................................................................l0000KXXXK00KXXK");
         System.out.println("NNNNNXKXNNNNXKOkOOkl. ................'...........''',,,,,,,''''','..''.................'.............................................l000000KKK00KKKK"); 
         System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
         System.out.println("Ein mächtiges Tor erhebt sich vor dir...du scheinst einen Schlüssel zu benötigen...");             
  }
  static private void looksAtKey(){
        System.out.println("                                                                                                                                             ");         
        System.out.println("                                                                                                        111111111111                         "); 
        System.out.println("                                                                                                    11111111111111111111                     "); 
        System.out.println("                                                                                                 111111111111111111111111                    "); 
        System.out.println("                                                                                               111111        11111111111111                  ");
        System.out.println("                                                                                             111111           1111111111111111111            ");
        System.out.println("                                                                                            1111111             111111111111111111           "); 
        System.out.println("                                                                                            111  11111111           111111111111111          "); 
        System.out.println("                                                                                            111   1111111             11111111111111         "); 
        System.out.println("                                                                                            111                        111111111111111       "); 
        System.out.println("                                                                                            111                          1111111111111111    "); 
        System.out.println("                                                                                            11111                             111111111111   "); 
        System.out.println("                                                                                             11111111111111                    111111111111  "); 
        System.out.println("                                                                                              111111111111111                  1111111111111 "); 
        System.out.println("                                                                                                111111111111111                    111111111 "); 
        System.out.println("                                                                                                 1111111111111111                    111111  "); 
        System.out.println("                                                                                                 111111111111101111       111        11111   "); 
        System.out.println("                                                                                         1111111111111111111111111       1111        11111   "); 
        System.out.println("                                                                                        1111111100011111111111111        111        111111   "); 
        System.out.println("                                                                                      1111111111110011111111111111        111111111111111    "); 
        System.out.println("                                                                                      1111111111111001     11111111         11111111111      "); 
        System.out.println("                                                                                    1111111111111111         11111111111 11111111111         ");
        System.out.println("                                                                                   111111111111111111             111111111111111            "); 
        System.out.println("                                                                                111111011111111111111                                        "); 
        System.out.println("                                                                              11111111111111111111                                           "); 
        System.out.println("                                                                            1111111111111111                                                 "); 
        System.out.println("                                                                           111111111111111                                                   "); 
        System.out.println("                                                                        1111111111111111                                                     "); 
        System.out.println("                                                                      1111111111111111                                                       "); 
        System.out.println("                                                                    111111111111111                                                          "); 
        System.out.println("                                                                 111111111111111                                                             "); 
        System.out.println("                                                               1111111 1111111                                                               "); 
        System.out.println("                                                             111111111111111                                                                 "); 
        System.out.println("                                                           111111 1111111                                                                    "); 
        System.out.println("                                                         11111111111111                                                                      "); 
        System.out.println("                                                      111111111111111                                                                        "); 
        System.out.println("                                                    111111111111111                                                                          "); 
        System.out.println("                                                  11111111111111                                                                             "); 
        System.out.println("                                               111111111111111                                                                               "); 
        System.out.println("                                             111111111111111                                                                                 "); 
        System.out.println("                                           11111111111111                                                                                    "); 
        System.out.println("                                         11111111111111                                                                                      "); 
        System.out.println("                                       11111111111111                                                                                        "); 
        System.out.println("                                    111111111111111                                                                                          "); 
        System.out.println("                                  111111111111111                                                                                            "); 
        System.out.println("                                111111111111111                                                                                              "); 
        System.out.println("                              11111111111111                                                                                                 "); 
        System.out.println("                           111111111111111                                                                                                   "); 
        System.out.println("                         111111111111111                                                                                                     "); 
        System.out.println("                       111111111111111                                                                                                       "); 
        System.out.println("                    1111111111111111                                                                                                         "); 
        System.out.println("                  111111111111111                                                                                                            "); 
        System.out.println("                11111111111111111                                                                                                            "); 
        System.out.println("              1111111111110111111                                                                                                            "); 
        System.out.println("           111111111111111111111                                                                                                             "); 
        System.out.println("         11111111111111111111111                                                                                                             "); 
        System.out.println("       11111111111111111111111111   1                                                                                                        "); 
        System.out.println("     11111111111111111111111111111111111                                                                                                     "); 
        System.out.println("   111111111111111111111 1111111111111111                                                                                                    "); 
        System.out.println("111111111111111111111      111111111111111                                                                                                   "); 
        System.out.println("111111111111111111111         111   111111111                                                                                                ");   
        System.out.println(" 1111111111111111111111               1111111                                                                                                ");    
        System.out.println("1111111111 11111111111111111       11111111                                                                                                  ");  
        System.out.println(" 111111    11111111111111111     11111111111 111                                                                                             "); 
        System.out.println("             11111111111111111    11111111111111                                                                                             "); 
        System.out.println("              11111111111111111    111111111111                                                                                              "); 
        System.out.println("                     111111111      111111111                                                                                                "); 
        System.out.println("                      1111111         1111                                                                                                   ");
        System.out.println("                     1111111111  111                                                                                                         ");
        System.out.println("                     1111111111111111                                                                                                        ");
        System.out.println("                      1111111111111                                                                                                          ");
        System.out.println("                       111111111                                                                                                             ");
        System.out.println("                        111111                                                                                                               ");
        System.out.println("Du hast einen Schlüssel erhalten");
  }

  static private void looksAtHallway(){
        System.out.println("--------------------------------");
        System.out.println("                           /    ");
        System.out.println("                          /     ");
        System.out.println("   ]                     [    ,'");
        System.out.println("   ]                     [   /  ");
        System.out.println("   ]___               ___[ ,'   ");
        System.out.println("   ]  ]              /[  [ |:   ");
        System.out.println("   ]  ]             / [  [ |:   ");
        System.out.println("   ]  ]  ]         [  [  [ |:   ");
        System.out.println("   ]  ]  ]__     __[  [  [ |:   ");
        System.out.println("   ]  ]  ] ]     [ [  [  [ |:   ");
        System.out.println("   ]  ]  ] ]     [ [  [  [ :====");
        System.out.println("   ]  ]  ]_]     [_[  [  [      ");
        System.out.println("   ]  ]  ]         [  [  [      ");
        System.out.println("   ]  ] /             [  [      ");
        System.out.println("   ]  ]/              [  [      ");
        System.out.println("   ]                     [      ");
        System.out.println("   ]                     [      ");
        System.out.println("   ]                     [      ");
        System.out.println("  /                             ");
        System.out.println("/                               ");
        System.out.println("/                               ");                                                
        System.out.println("--------------------------------");
        System.out.println("Du siehst einen Gang vor dir");
  }

  static private void looksAtNPC(){
        System.out.println("....................................................................................................");
        System.out.println("....................................................................................................");
        System.out.println("....................................................................................................");
        System.out.println("....................................................................................................");
        System.out.println("....................................................................................................");
        System.out.println("....................................................................................................");
        System.out.println("....................................................................................................");
        System.out.println(".................................................-=++++=-::.........................................");
        System.out.println(".........................................:+%@@@@@@@@@%%%%%%%%@#=....................................");
        System.out.println(".......................................%@@@@@@@@%%%%%%%@#%@%@%%@%%-.................................");
        System.out.println("....................................:@@@@@@@@@@@@@@@%@%%%%%%%@%@%%@%-...............................");
        System.out.println("....................................@%%%@%@@@@%%%%%%###%%%%%%%@@@@%%%#..............................");
        System.out.println("..................................:%%*####%%%%########*###%%%%%%%%%%%%%.............................");
        System.out.println(".................................=%#**###%%%%%#####****####%%@@@%%%%@@@=............................");
        System.out.println(".................................%***####%%%%%#%######**####%%%%%%%%%%@@............................");
        System.out.println("................................+#+*#####%%%%%#######***#####%%%%%%@%%%%-...........................");
        System.out.println("................................%+**#####%%%%%%%%%%#*##****#%%%%%%%%@@%%#...........................");
        System.out.println("................................#+*#*%*##%%%@@@@@@%%%##******#%%%%%%@@@@@...........................");
        System.out.println("...............................:***###%%%@@@@@%%%%%######*****#%%%%%%@@@%...........................");
        System.out.println("..............................-##%%%%#%%%%%@@@@@@@@@%###*##****#%%%%%%%%*...........................");
        System.out.println(".............................*%%@@@@@*##*#%@@@@%%%%%%%%##******#%%%##*+%#...........................");
        System.out.println(".............................+#*%@@@@+##*#%@@@@@@@%%%#####*****##%#***#*%+..........................");
        System.out.println("..............................+%@%@@@###**##%@%%%%%####********#%%#**%%###..........................");
        System.out.println("...............................-*#%@###****#%%%%%%%%##*###**###%%%****%%%#..........................");
        System.out.println("...............................+#%#*########%%@%%%%%###########%%%****#%#+..........................");
        System.out.println("...............................=*##*%#*#%%##%%%@@%####%%#####%%#%%#*##*##...........................");
        System.out.println("...............................-+#@#%##%%%@@@%%@@@@@###@%%%%%%#%%%%##**=............................");
        System.out.println("...............................=+#@%%#%@%@@@@@%@@@@@@#%@@%%%%%##%%%#***:............................");
        System.out.println("...............................-**%%%##%%%%%%%%%%@@@%%%@@@%%%%%%#%#%**#:............................");
        System.out.println("...............................:#*%#*#####**#*%%%@@@%%%@@@%%%%%##%#%#*#:............................");
        System.out.println("................................#%%@@@=*#%%%%%%%%@@@%%@@@@@%%%%####%#**:............................");
        System.out.println("................................=%%@*++#%%%#%%%@@@@@%%@@@@@%%%%%#%%%#**-............................");
        System.out.println(".................................*%%###%@@@@%@@@@@@@%%@@@@@%@%%%%%%%****............................");
        System.out.println("..................................#%%##%%@@@@@@@@@@@%%@@@@@@%@%%%%%*****............................");
        System.out.println("..................................##%%%%%@@@@@@@@@%@%%@@@@%@@@@%#*******:...........................");
        System.out.println("..................................*#%%%@@@@@@@@@@@%%%%@@@@@@%##**********...........................");
        System.out.println(".................................:*##%%%@@@@@@%%%%%%%@@@@%####*#*********-..........................");
        System.out.println("..................................#*#%%%@@@%%@%%%%%@@@@%#########********#:.........................");
        System.out.println("..................................-##%%@@@@@@@@@@@@@@%#########*#*********#=........................");
        System.out.println("...................................-#%%@@@@@@@@@@@@%##########************###*=:....................");
        System.out.println("......................................+##%#####%%%%%%###*#######***********#########=:..............");
        System.out.println("............................................==+*#%%%%###**#####************##############*-.........");
        System.out.println("............................................+==+*#%%####**#####*************############%###%%#.....");
        System.out.println("............................................===+*#%%%##########***************##%%%%%%%%%%%%#%%#%%+:");
        System.out.println(".............................................==++*#%%%#########**************###%%%%%%%%%%%%%%%%%%%%");
        System.out.println("............................................:=++++*#%%%####%####************####%%%%%%%%%%%%%%%%%%%%");
        System.out.println("..........................................:++=++***##%%%##%%####*###********####%%%%%@%%%%%%%%%%%%%%");
        System.out.println(".......................................=***++=++****#%%%%%%%########**#*#######%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("....................................=********==+**###%%%%%%%#################%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("..................................********###==++**##%%%%%#%#####%%%%%%%%%%%%%%%%%%%%#########%%%%@@");
        System.out.println("...............................:++***#######*==++++*%%%%%###%###%%%%%%######%%#%%#%##########%%@@%%%");
        System.out.println("..............................++++***##*###**+++++++#%%%%#+#%%%%########%%%%%%%%%%%%%%##%#%%%%@@%%%#");
        System.out.println("..........................:=+*****##*########*##*+++*%%%#+*##*###%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%###");
        System.out.println("......................=***######***+****###########**#%%***++*####%%%%%%%%%%%%%%%%%%#%%#%%%%%%%#####");
        System.out.println("...................:**#####%%%%##*+********######%%%###****#######%%%%%%%%#%#%##%%%%%%%%%%%%%%%#####");
        System.out.println("...........-+**********#####%%%%#**##****######%%%%%###**#####%%%%%%%#%##########%%%%%%%%%%%%%######");
        System.out.println("........++++*********######%###**#######*######%%%%%%%#***####%%%%%%###########%#%%%%%%%%%%%%%%#####");
        System.out.println(".....=++++**********#########***##############%%%%%%%%#**##%#%##%%%############%%%%%%%%%%%%%%%%%%%##");
        System.out.println("...++++++**++*****########*#*#################%%%%%%%#***####%#%##################%%%%%%%%%%%%%%%%%%");
        System.out.println(".-=++++++++*****#######****#**#################%%%%%%****####%%###############%%%%%#%%%%%%%%%%%%%%%%");
        System.out.println("+==++++**+***#*#######**##*####################%%%%%%***#####################%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("===+++*++***########******######*############%%%%%%@@#*####%%%##############%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("==+++*****#########******##################%%%%%%%@@@%####################%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("=+++****######%%%#*********###############%%%%%%%%@@@%##################%%%%%%%%%%%%%%%%%%%%%%%###%%");
        System.out.println("=++****#####%%%%%***+******##########%%%%%%%%%%%%%@@%#***#############%%%%%%%%%%%%%%%%%%%%%%%######%");
        System.out.println("--=====++++++++++=============+++++++++++++++++++++++======+==+++=++++++++++++++++++++++++++++++++++");
  }
  static private void looksAtViktor(){
        System.out.println("MMNXXWMWXXNMMWXXWMWNXNWMWXXNMMNXXWMWNXNWMWX0kxdllokKXXXNWMWXXWMWNXNWMWXXNMMMNXXWMWNXNWMWXXNMMNXXWMWNXNMMWXXWMMNXNWMWXXNMMNXXWMWNXNWMWXXNMMNXXWMWNXNMMW");
        System.out.println("WWNNNWWWNNNWWNNNWWWNNNWWWNNWWWWNNWWWX0OOxoc;,,'''.,::cccokKNNNWWNNNWWWNNNWWWWNNWWWNNNWWWNNNWWNNNWWWNNNWWNNNWWWNNNWWWNNWWWWNNWWWNNNWWWNNNWWWNNWWWNNNWWN");
        System.out.println("XNWMWNXNWMWNXNWMNXNWMWNXNWMWXXWMWNkl;'''...','............:KMNXNWMWNXNWMWNXNWMWNXNWMWNXNWWNXNWMWNXNWMWNXNMWNXNWMWNXNWMWXXWMWNXNWMWNXNWMNXNWMWNXNWMWNXN");
        System.out.println("WWNNNWWWNXNWWWNNWWWNXNWWWNNNWWNN0l............          ...xNWWWNNNWWWNXNWWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWNNNWWWNXNWWWNNNWWNXNWWWNXNWWW");
        System.out.println("NNNWWNNNNNWNNWWWWNNNWNNNNWWWNNXd'   ............          .xNNNNWNNNNNNWWNNNNNWNNNWNNNNNNNWNNNNWNNNNNNNNNWNNNNNNNNNNNNWNNNNWNNNNNNNNNNNNNWWNNNNNNNWNNW");
        System.out.println("NNWWWNNNWWWNNNWWNNNWWWNNNWWWNO;         ...............',co WNNNWWWNNNWWWNNNWWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWNNNWWWNNNWWWNNN");
        System.out.println("MMNXNWMWNXNWMWXXWMWNXNWMWNXNNc    .................,cdkOXWW NNWWNXNWMWNXNMMWNXNWMWNXNWMWXXWMWNXNWMWNXNWMWXNWMWNXNWMWNXNMWNXXWMWNXNWMWXXWMMNXNWMWNXNWMW");
        System.out.println("NNWWWNNNWWWNNNWWNNNWWWNNNWWNo.         ...''';:coodxOKXNNWWW XNNWWWNNNWWWNNNNWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWWNNWWWNNNWWWNNW");
        System.out.println("NNWWWNNNWWWNNWWWNNNWWWNNNWWO'          ..,:oxkOKXXNXXXKKKKKK0 KXNWWNNNWWWNNNWWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWNNNWWWNNNWWNNNWWWNNNWWWNNNWWWNNWWWNNNWWWNNW");
        System.out.println("MWNXNWMWNXNWMWXXWMWNXNWMWNXd.          .,ok0KXXXXXXKK00Okkkk0K NNXNWMWNXNWMWNXXWMWNXNWMWXXNMMNXNWMWNXNWMWXNWMWNXNWMWNXNWMNXNWMWNXNWMWXXWMWNXNWMWNXNWMW");
        System.out.println("NNWWWNNNWWWNNNWWNNNWWWNNNWWo           .'oO000000OOkkkkOOOO0KXNN XKXXNWWWNNNNWWNNNWWWNNNWWNNNWWWNNNWWWNNNWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWNNNWWWNNNWWWNNW");
        System.out.println("NNNNNNNNNNNNNNNNNNNNNNNNNNWk.           .':dkOOkkkkxxkO00000OkxdoclONNNNWNNNNNNNNNNNNNNNNNNNNNNNNNWWNNNNWWNNNNNNNNNNNNNNNNNNNNNNNNNNNNWWNNNNNNNNNNNNNN");
        System.out.println("WWNNNNNXKKXNNNXXNWWNNNWWWNNXo.            .'cdkkxxkkO0Okxdolc;..., KWWNNNWWWNNNWWWNNNWWWNNNWWNNNWWWNNNWWNNNWWWNNNWWWNNNWWNNNWWWNNNWWWNNNWWNNNWWWNXNWWN");
        System.out.println("XNWWNKOkkO000O0KXXKXNXK0KNWW0;             .;odxxkOOOxc,'..'lo;..; XXNWMWNXNWMWNXNWMWNXNMMNXNWMWNXNWMWNXNWWNXNWMWNXNWMWXXWMWNXNWMWNXNWMNXNWMWNXNWMWNXN");
        System.out.println("WNX0kkOkdkO0K0OOOxO0O00O0XNXXk,            .codxxxoc;,''...;dKKdco0 XNNNNWWWNNNWWWNNNWWWNNNWWWNNWWWNNNWWWNNWWWNNNWWWNNNWWNNNWWWNNNWWWNNNWWNNNWWWNNNWWN");
        System.out.println("WNkllodloxxxxkxxxxxldxokOOO00Ox'  ...      'oxxxoc;;;,,'..:okO0KKKK KNNNNWWWNNNWWWNNNWWWNNNWWNNNWWWNNNWWNNNWWWNNNWWWNNNWWWNNWWWNNNWWWNNWWWNNNWWWNNNWWW");
        System.out.println("NKd:;,;;;;cc;:lloocldloxdok0kkOd;,:::;,'.. .cxkkxoodoc;,;coxkOO00KKKKNWMWXXNWMWNXNWMWNXNMMNXNWMWNXNWMWNXNMMNXNWMWNXNWWXkdolc::coOXXXNMMNXXWMWNXNWMWNXN");
        System.out.println("WWO:...,..,,'''','.';,;l::llcccodc;:oc;cdc..:oxxkkOOkxdddxdoxkkkdllxK NNNWWWNNNWWWNNNWWWNNNWWNNNWWWNNNWWWNNNWNWWWWNWWWKOxolc:'. .';coOXNWWNNNWWWNNNWWW");
        System.out.println("WWN0o:;'...............''''''.';;;;co::oxxc,,cloooooooddddllol;'..;d0NN NNNNNNNNNNXKXXNNXXNNNNNNNNNNNNNNWNNNNXXXXKO0NWWWWWWWWKx,     .xNWWWNNWNNNNNWWN");
        System.out.println("NNWWWNXK0OOkkxdoooooolclc;'''...'..':oxkkdl:;clollcc::cloooxxl:,'..';d0KO kkOO00000OOOO000000XNNNXNWWWNNNWNXKxxKKkxOXK00NWNNNXKk'     ;KNNWWWNXNWWWNXN");
        System.out.println("MWNXNWWWNXNWMWNNWMMNNNWMNK00K0Okkkkxddxxkxo:;:ldollc::clddddol:,''';,,xKOxlc:,,;:cloodxkkO000KXXXNWWNNXKKXX0l:kX0dd0Kkd0XK0KKkxx:     ,0MWNXNWMWNXNWWW");
        System.out.println("NNWWWNNNWWNNNNWWNNNWWWNNNWWNNNNWWNNNWNKkc;::::codoolllcldddl;''',::;::oddxolc;'........',;:ccloodxxkOOxx00kocxK0xx0KkokXKkoc:;:ll;   .;0NNWWWNNNWWWNNW");
        System.out.println("NNWWWNNNWWWNNNWWNNNWWWNNNWWWNNWWWNNNWWNo..':::cllooooddxkko;..,,,,;:okxclolc;,;,'...   ........'',,;;:lk0kc:x00dd00xcdOko;....,:c:.  .,xNNWWWNNNWWWNNW");
        System.out.println("MWNXNWMWNXNWMWXXWMWNXNWMWNXNMMNXXWMWNXx'. .,:::cccclodxxddc',:cllok00N0:;:;,,',,,;,,...............''':O0d;;cl::odl,;l:,...,'...,;;'...lXWNXNWMWNXNWMW");
        System.out.println("NNWWWNNNWWWNNNWWNNNWWWNNNWWWNNWWWNNNNkc,.. .;:::::;;::lc::,,cdxk0O0K0Ol.','...',,;;:;,',,,'...........oOxc:lc;,,lc,.';..   ..''',;::'..:kKXWWNNNWWWNNN");
        System.out.println("NNWWWNNNWWWNNWWWWNNWWWNNNWWNNNNWWNNN0l;,'.....';:::;,......';cloddol;'...''...'',;:::lc;;,''.........'k0l',::;,,cc'.          .':c:;'.:oollxKNNNWWWNNN");
        System.out.println("WWNXNWWWNXNWWWNNWWWNXNWWWNXNWWNNNWWKdl;'..'''...';:::;,''............. ..''...'',;:;:kx;,'............::....';;,:;'.            ...  ..':,.':kNWNXNWWW");
        System.out.println("XNWMWNXNWMWNXNWWNXNWWWNXNWMWNNWWWN0dolc,...',,'....',;;,'..... ......  ..'.......'''',.......................:c:c;..                   .;....,lOWMWNXN");
        System.out.println("WWNNNWWWNNNWWNNNWWWNNNWWWNNWWWNNNXxlc:cc...''.........'........','..  ..'..    ..............................''....'::.                ...  ..':kNNWWN");
        System.out.println("WWNNNWWWNNNWWWNNWWWNNNWWWNNNWWXOkxolc::o:......................''... ....'......',,,,'...  ..................   ..,::;...';;;.         ...  ....,xNWWN");
        System.out.println("XNWMWNXNWMWXXNMMNXNWMWNXNWMWKOdccolclcclccc,............. ..'...................,,'...   .................. ........ ..;codoo:.                ..cKNXN");
        System.out.println("WWNNNWWWNNNWWWNNWWWNNNWWWNN0ko::odccllclllo:;;'......... .',...................','..    ....''''............'..................                 ..dNWW");
        System.out.println("WWNNNWWWNNNWWNNNWWWNNNWNXOoll:,ldlcllccllcc;,''''....... ... .........'''....'','.    ...',,,,'''...................      .                       ;0WN");
        System.out.println("XNWMWNXNWMWNXNWWNXNWWNOl::loc,:oolloolc:;;:;'''........  ...........',,,,,,,,'ckd;....';:c:,'''''................ .....                        ....cKW");
        System.out.println("WWNXNWWWNXNWWWNNWWN0koccoxdoc;clllodoc'..,;:,'................;,...',;;;;;;;,,,;;''',:cc:;,,,;;;;,''......... .......                            ..'xN");
        System.out.println("NNNWWNNNWWNNNNNWNOdlclxkxdolc:ccldxdc'.''',,'.'..............::,,,,;;;;;,,,,,,,'...';;;,,;::::;,'''',''.....  ..                                  ..;0");
        System.out.println("NNWWWNNNWWWNNNWNxclclxxdddolc:dOkxd:,;:;'',,''''............,;,,,,,;;:::;;;;,'......'',,:cclllc:;,,,'.......                                        .k");
        System.out.println("MWNXNWMWNXNWMWNk::lodollloolccll;'.'lc,'''''.',''''',,,,,'..';::;,,,'...      ....':dkkk0kxoc;'...     .''.                                         .x");
        System.out.println("NNWWWNNNWWWNNWNo,cooc:;;;:::;'.....::,..,,'...';;;;;;;;,'',,''...      ...........;dkxoolc,......    ..',.                                          .x");
        System.out.println("NNWWWNNNWWWNNWXc.;;;;,,;,,,;;'.';::;...':do,..,;cc:;;,,',;'.  ..................',::;'',,'.....  ....',,..  .,clc:..                                'O");
        System.out.println("MWNXNWMWNXNWMWKc..'',,,,,,,,,'',:l;...';lxl,',,;;:;,''',:.....''''............'',,,,''''.....  ...',,,'.   .dNNNNNXOxdoollloxxo;..               .'l0N");
        System.out.println("NNWWWNNNWWWNXWNl....'',,,,,,,,,,c:'...,;'........''''';od,.,,''...............''',,''...........,;;;;'.   .lXNWWWNXNWMWNNWMWNNNWXOocc:::;::ccccokKXNXN");
        System.out.println("NNNNNWNNNNWNNWXl......',''..'';:c;..';;,'...',;;;;;,,'lkxc','.............'',,,;,,'...........',,,;;'.    :KWNNNNNNWNNWWNWNNWNNNNNWWWNNNWWNNNWWWWNNNNW");
        System.out.println("WWNNNWWWNNNWWWXo......'''....,;:;'',,,,;;,,,'';ll:;,,''ckk;','''...'',,,,,,,,,,,'...   .....',,,,,,..    'OWWWNNNWWWNNNWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWW");
        System.out.println("XNWMWNXNWMWNXNWk'.......''...','..',;;cc;',cokkkxl;'..,:xKl';;,'''',;;;;,,,'''....   .....,;,,,,,'.     .xWNXNWMWNXNWMWXXWMWNXNWMWNXNMMNXNWMWNXNWMWNXN");
        System.out.println("WWNNNWWWNNNWWNNO,..............',:cllc::coxkxoc:;'.  .',:kk;,:,'''',,,,,''......  ......';;,'','..      :KNNWWNNNWWWNNNWWNNNWWWNNNWWWNNNWWNNNWWWNNNWWN");
        System.out.println("WWNNNWWWNNNWWNN0;.....'''......';::;coxkdl:;,;;'.   ...,,c0d'''...''',,'..............,;;,,,'...       .xNNWWWNNNWWWNNNWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWW");
        System.out.println("XNWMWNXNWMWNXNMNc.....','........'cool:,'',;;'.. .......,,dO;.'...'''''............',;;;,,'....        ;KMWNXNWMWNXNWMWXXWMWNXNWMWNXNWMWXXWMWNXNWMWNXN");
        System.out.println("WWNNNWWWNNNWWWNXl.....','.......;c;....,;;,'.    .   ...'',xo...................'',;;;,;;,...         .lNNNWWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWNNNWWWNNNWWW");
        System.out.println("WWWNNWNNNNWWNNWNl.....','..................         .......;d; ...............''',;;;;;;,....         .lNNNWWWNNNNNWNNWNNNNNWNNNNNWWNNNNNNWNNNNWNNNNNN");
        System.out.println("NNWWWNNNWWWNNNWWo.....',.. .........           ..   ........cl. .............,,,,,:cc;,'......       ..lNWWNNNWWWNXNWWWNNWWWNXNWWWNNNWWWNNWWWNXNWWWNNN");
        System.out.println("MWNXNWMWNXNWMWXNd.....',..  ....              ..   ........ .l;  ..........'',;;;:c:,........   .  ....oNNXWMWNXNWMWNXNWMNXNWMWNXNWMWNXWMWNXNWMWNXNWMW");
        System.out.println("NNWWWNNNWWWNNWWWk.....',..   ...             ...  ....       ;l' .........',,;:c:,,'........   ........cXWWWNNWWWNNNWWWNNNWWNNNWWWNNNWWNNNWWWNNNWWWNNW");
        System.out.println("NNWWWNNNWWWNNWWW0,....''...  ....               ....         .ll....'....',;:c:;'......'...  ..........:XWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWNNNWWWNNNWWWNNW");
        System.out.println("MMNXNWMWNXNWMWXNX:...'''...   ...   .:,       ....         .  'o:.......':cc:;'.....'''... ............cXNXWMWNXNWMWNXNMMNXXWMWNXNWMWNXWMMNXNWMWNXNWMW");
        System.out.println("NNWWWNNNWWWNNNWWNl...','...   ...   '0d.    ....         ......lk;.'''';::;,'......'''..   .... .......cXWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWNNNWWWNNNWWWNNN");
        System.out.println("NNNWWNNNWWNNNWWNNk....''...    .    'O0;.     ...'ll'..........c0l.,;:c:,'.............     ... .......lXWWNNNNWWNNNNWNNNWWNNNNNWWNNNWWNNNNWWNNNWWNNNN");
        System.out.println("WWNXNWWWNXNWWWNXWK;...''...    .    'OXo. ....''cko:;,,''....;,;l:;cl:,...........'...      .'. .......lXNNWWWNXNWWWNXNWWNNNWWWNXNWWWNNWWWNXNWWWNXNWWW");
        System.out.println("XNWWWNXNWMWNXNWWNXl...''....        ;XXl. ..'',lkc.,;;,,,,',dkcclcc:,............'...       .,;. ......:XMWNXNWMWNXNWMWXNWWWNXNWMWNXNWWNNNWWWNXNWMWNXN");
        System.out.println("WWNNNWWWNNNWWWNNWWx.........        :XWXx......ll....'',,;;lOd:ll,..............'...         .co;......:KWNNWWNNNWWWNNNWWNNNWWWNNNWWWNNNWWNNNWWWNNNWWW");
        System.out.println("WWNNNWWWNNNWWWNNWWx...''....        cXNXk;.....c,........''cOl,'..............'''..     .......,ll,..'.;0NNWWWNNNWWWNNNWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWN");
        System.out.println("XXWMWNXNWMWNXNMMNXo...'......       dWO;.......;.........''ckc.............'',,'...  ............;ll;..'kMWNXNWMWNXNWMWXXWMWNXNWMWNXNMMNXXWMWNXNWMWNXN");
        System.out.println("WWNNNWWWNNNWWNNNWWd....'.....       oXc........,.....',,,,':x:...........'',,,'.... .........'''''.;oc..dXNWWWNNNWWWNNNWWNNNWWWNNNWWWNNWWWNNNWWWNNNWWN");
        System.out.println("WWNNNWWWNNNWWNNNWWx....'.....      .dNl. ....,c:..'',,''',''dc.... ........'''...   ........',,,,,'..::.lKNNWWNNNWWWNNWWWNNNWWWNNNWWWNNNWWNNNWWWNNNWWN");
        System.out.println("XNWMWNXNWMWNXNWWNXo..........      .xWd.  .:cc:'.''......'..oc...   ......';;,..   .........,,,,,'....,,;OWNXNWMWNXNWMWNNWMWNXNWMWNXNWWNXNWMWNXNWMWNXN");
        System.out.println("WWNXNWWWNXNWWWNNWWd..........      .dNd. .cc'...........''..ll...     ...,:c;..    ........';;;,'..   .'.lXWWWNXNWWWNXNWWNXNWWWNXNWWWNXWWWNNNWWWNXNWXN");
  }
  static private void looksAtMagician(){
        System.out.println("....................................................................................................................................11111111111111001.");
        System.out.println("....................................................................................................................................11111111...111111.");
        System.out.println(".................................................................................................................................11111111.........111.");
        System.out.println("....................................................00000000011111111111110................................................111111111111.........01111.");
        System.out.println("...............................................00001111111              11111111.........................................1111111111............1111...");
        System.out.println("...........................................00011111                    111111111111...................................0111111111............11111.....");
        System.out.println(".......................................10111111                        1111111111111111......................1101111111111111111111111111111111..11...");
        System.out.println(".....................................1111111111                        11111111111111111....................111111111111111111111111111111111111111...");
        System.out.println("...................................1111111111                        1111111111111111111111................111111111111111111111111111111111111111....");
        System.out.println("..................................111111111                      1111111111111111111111111111.............111111111111111111111111111111111111111.....");
        System.out.println(".................................1111111111                   1111111111111111111111111111101.............11111111111111111111111111111.....11111.....");
        System.out.println("...............................111111111111           1111  11111111111111111111111111111100011..........1111111111111111111111.............1111......");
        System.out.println("..............................111111111111          100111  111111111111111111111111111110000011........111111111111111111111..............11111......");
        System.out.println("............................111111111111          11011111111111111111111111111111111111100000111111110111111111111111111111..............111111......");
        System.out.println("...........................111111111111111111111111111111111111111111111111111111111111110000001111111111111111111111111111.............11111111......");
        System.out.println("..........................111111111111111111111111111111111111111111111111111111111111000001100011111111111111111111111111..........11111111111.......");
        System.out.println("..........................11111111111111111111111111111111111111111111111111111111111100111111101111111111111111111111111111..1111111111111111........");
        System.out.println("..........................1111111111111111111111111111111111111111111001111111111111111111000000111111111111111111111111111111111111111111111.........");
        System.out.println("..........................1111111111111111111111111  .111111111111111111111111111000111100000000000111111111111111111111111111111111111111............");
        System.out.println("..........................111111111111111111111111111111111111111111111111111111000110100000..00000011111111111111111111111111111111111...............");
        System.out.println("...........................001111111111111111111111111111111111111111111111111100011100000000000000011111111111111111111111111111111111...............");
        System.out.println("...........................000000111111111111111111000011111111111111111111111100010000000000000.0000111111111111111111111111111111...................");
        System.out.println("...........................000000011111111111111000000000000000011111111111111100000000000000000.0000111111111111111111111111111111...................");
        System.out.println("............................010110111111111111100000000000000000111111111111111100000000000000000..00111111111111111111111111111111111................");
        System.out.println("............................00111101110001111000000000..1000001111111111111110111000000000000000000001111111111111111111111111111111111111..111.....11");
        System.out.println("............................00001100111000000000000....000000111111111111100000000000000000.0000000001111111000001111111111111111111111111111111111111");
        System.out.println("............................00000000000000000000000...0000001111.1111100000000000000000000000000000001100000000000011111111111111111111111111111111111");
        System.out.println("............................000000.00000000000000000000000001111111111000011111110000000000..000000001100000000000001111101111111111111111111111111111");
        System.out.println("...........................0000...0001111100000000000000011111111000000001111110001000000...0000000000100000000000000110000011111111111111111111111111");
        System.out.println("...........................000000000111111110000011111111111111000000000111111110000000000000000000000000000000000000000000001111111111111111111111111");
        System.out.println(".............................000000001111111111111111.111111110000000000111111111100000000000000000000000000000000000000000001111110011111111111111111");
        System.out.println("................................0000011.11111111001111111111111110011101111111111111100000000000000000000000000000000000000000000000000111111111111111");
        System.out.println("...............................10000011111111111110000111111111111111111111111111111111100000000000000000000000000000000000000000000000111111111111111");
        System.out.println("...............................10000011111111111111000000001111001111111111111100000001111111111000000000000000000000000000000000000001111111111111111");
        System.out.println("................................1000101111111111000111111000011101111111111111111100000000000001100000000000000000000000000000000000011111111111111111");
        System.out.println(".................................101101111110000000111111111100111111111111111111111000000000000000000000000000000000000000000000000011111111111111000");
        System.out.println(".................................111100011000000011111111111111111011111111111111111100000000000000000000000000000000000000000000000001111100000000000");
        System.out.println("..................................11110000000000111111111111111111110000111111111110011111100000000000000000000000000000000000000000001000000000000000");
        System.out.println("....................................111000000000111111111111111111111110000111111111111110000000000000000000000000000000000000000000000000000000000000");
        System.out.println(".....................................10000000000000000000000000000011111100000111111111111000000000000000000000000000000000000000000000000000000000000");
        System.out.println("....................................100000000000000000000000000000000011110000000000001111000000000000000000000000000000000000000000000000000000000000");
        System.out.println(".....................................10000000000000000000000000000000000011000000000000011000000000000000000000000000000000000000000000000000000000000");
        System.out.println("......................................0000000000000111011111111111110000000111000000000000110000000000000000000000000000000000000000000000000000000000");
        System.out.println("........................................00000000001110011111111110111000000000000000000000011000000000000000000000000000000000000000000000000000000000");
        System.out.println(".......................................000000000000000011111111111111000011100000000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("..........................................000000000000110111111111111100000011110000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("........................................10000000000001111111111111111100000000000110000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("...........................................00000000000111111111111111110100000000000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("...........................................00000000000111111111111111111100000000000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("...........................................00000000100011111111111111111110000000000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("...........................................00000000111011111111111111101010000000000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println(".............................................000000001111111111111111101000000000000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println(".............................................000000000011111111111111001000000000000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("............................................0000000000001111111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("............................................0000000000001111111111110000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("..............................................00000000000101111111100000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("..............................................00000000000001111111000000000000000000000000000000000000010000000000000000000000000000000000000000000000");
        System.out.println(".............................................000000000000001111111000000000000000000000000000000001000110000000000100000000000000000000000000000000000");
        System.out.println(".............................................000000000000001101010000000000000000000000000000000010001100000000000000000000000000000000000000000000000");
  }
  static private void gameEnding1(){
        System.out.println(" _____ _____ _____ _____    _____ _____ _____ _____ ");
        System.out.println("|   __|  _  |     |   __|  |     |  |  |   __| __  |");
        System.out.println("|  |  |     | | | |   __|  |  |  |  |  |   __|    -|");
        System.out.println("|_____|__|__|_|_|_|_____|  |_____| ___ |_____|__|__|");
        System.out.println("                  Death by Viktor                   ");
        System.out.println("----------------------------------------------------");
        System.out.println("------------Ende 1/4: Tod durch Viktor.-------------");
  }
  static private void gameEnding2(){
        System.out.println("-------Du hast keine Kraft mehr und stirbst---------");
        System.out.println(" _____ _____ _____ _____    _____ _____ _____ _____ ");
        System.out.println("|   __|  _  |     |   __|  |     |  |  |   __| __  |");
        System.out.println("|  |  |     | | | |   __|  |  |  |  |  |   __|    -|");
        System.out.println("|_____|__|__|_|_|_|_____|  |_____| ___ |_____|__|__|");
        System.out.println("                 Death by wall....                  ");
        System.out.println("----------------------------------------------------");
        System.out.println("-------Ende 2/4: Tod durch...Dummheit---------------");
  }
  static private void gameEnding3(){
        System.out.println("0OOOkxddddddddddddxxxxkkkOOOOOO00000OkxkOOOkxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxkkkkkkkkkkkkkkkkkkO00000000KKKKKKKKK000OOOOOO000000OOO000000000000KKKKKKKKKKKKKKKK000000OOOOOO0KKXXNNXNNNNNNXXXXXXXXXXKKKK000KKKXXXXNNNNNNNNNNNNNNXKK000KKKKK0OOOO000K0Ok");
        System.out.println("kkkkxdddddooooodddxxxxkkkkOOOOOOO00000O0000K0kxxkkkkkkxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxkkkkkOOOOOO000KOk0K000OOkkkkkkkkkkkkkkkOOOOOOOOOOOOOO0000KKKKKKKKKKKKKKK00OOOO0KXXNNNNNNNNNNNXXXXXXXXXXXXXKKKKXXXXXNNXNNNNNNNNNNNNXXKK00KKKKKXXK000OOOOO00K");
        System.out.println("kkkkkxxdddddddddxxxxxxxkkkkOOOOOOO00OOO000000OOOO00000OOkkkOOOkkkOkkkkkOOkxxxxxxxxxxxxxxxxxxxxkkkkkkkkkOOOOOOOdokOOOkkkkkkkkkkkkkkkkkkkkkkkOOOOOOOOOOOO0000000KKKKKXXXXXKKKKKXXXNNNNNNNNNNNNNNNNNNXXXNNNNNXXNNNNXXXXXXXXXNNNNNNNNX0000KKKXXXXXXXK00000OO0K");
        System.out.println("kkkkkxxddxxxxxxxxxxxxxxxxxxkkkkkkkkOOOOO0000OOOOOOOOOOOOOkkkOOOOOkkxxkkkOkxxxxxxxxxxxxxxxkkkkkkkkkkkkkkOOOOOOkooxkkkkkkkkkkOOOOkkkkkkkkkkkkOO0OOOOOOOOO00OO00KKKKKKKXXXXXXXXXXXNNNNNNNNNNNNNNNNNNNNNNNNNNNNNXXXXXXXXXXXKKKKKKKXXXXXXKK00KXXXK0kOKK0000000K");
        System.out.println("kkkkkxxxxxxxxxxxxxxxxxxxxxxxxxxxxkkkOOOO00O0OOOOOOOOOOOkkkkkkkOOkkkkxxkxkkkkkxxxxkkkkkkkkkkkkkkkkkkkOOOOOOkkko;cxkkkkkkkkkkkkOkkkkkkkkkkkkkkkOOOOOOOOOOOOOO0000KKKKKKKKKKXXXXXXNXXXXXNNNNNNNNNXXXXXNNNNNNNXXXXKKKKKK0000000000KKKXXXX0kkkOOOkdloOKKK0KKKXX");
        System.out.println("xxxkkkkkxxxxxxxxxxxxxxxxxxxxxxxxxxxkkkkkOOOOOOOO000OOOOOkkkkkkOOOkkkxxxxkkkkkkkkkkOOOOOkkkkkkkkkkOOkkkkkOOOOkc,lxkOkkkkkkkkkkkkkkkkkkkkkkkkkkkkkOOOOOOOOOOO000000000KKKKKKKKKXXXXXXXXXXXXXXXXXXXXXXXXNXXXXKKKKK000OOOOOOOOO0000000000OkkkkkkdllldO0KKXXXXK");
        System.out.println("xxxxkkkkkkxxxxxxxxxxxxxxxxxxxxxxxkxxkkkxxxkkOOOOOOOOOkkkkOOOOOOOOOOkkxxxxxxxxxxxkkkkOOOOkkkkkkkkkkkkkkkkkkkOkc;lkOOOOOOkxkOOOOOkkkkkkkkkkkkkkkkkkkOOOOOkkOOOO000000KKKKKXXKKKKKXXKKKKKKKXXXXXXXXXXXXXXXKKK0000OOOOOOOOOOOOO000000OOOOkkkkkkxolcloxOO000KK0");
        System.out.println("kkxxxxxxxkxxxxxxxxxxxxxxxxxxxxxkkxxxxxxxxxxxkkkkOkkkkkkkkOOO00000OOkkxxxxxxxxxxxkkkkkkOOkkkkkkkkkkkkkkkkkkkkkc;d00OOOOdccokOkkkkkkkkkkkkkkkkkkkkkkkkkkkkkOOOOO00000000KKKK00000KKKKKKKKKKKKKKKKKKKKK0000OOOOOOOOOOOOOOOOOOO00000OOOOkkkkkkkdlllooxkOkkkkOO");
        System.out.println("kkkkkkxxxxxxxxxxxxxxxxxxxxxxxxxkkxxxxxxxxxxxxxkkkkkkkkkkkkOO0000000kkxxxxxxxxxxxxxxkkkkkkkkkkkkkkkkkkkkkkkkkkc:kK0OOOdccoOK0OkkkkkkkkkkkkkkkkkkkkkkkkkkkkkOOO0000000000000000000KKK0000KK00000000OOOOOOOkkkOOOOOOOOOOOOOOOO0000OOOOkkkkkkkxolloodddddddddd");
        System.out.println("kkkkkkkkxxxxkxxxxxxxxxxxxxxxxxxkkkxxxxxxxxxxxxxxxxxxkkkkkkOOOOOOO0OOkxxxxxxxxxxxxxxxxxxxxxxxkkkkkkkkkkkkkkkOkc:kK0OOkoccdKNKOkOOOkkkkkkkkkkkkkkkkkkkkkkOOOOOO000000000000000000000000000000000OOOkkkkkkkkkOOOOOOOOOOOOOOOOOO00OOOOOkkkkkkkxolloooddddddddd");
        System.out.println("kkkkkkkkkkxxxxxxxxkkxxxxxxxxxxkkkxxxxxxxxxxxxxxxxxxxxxxxxxxxxkkkkkkkkxxxxxxxxxxxxxxxxxxxxxxxxxkkkkkkkkkkkkkko;cOKOOkkoclxXNKOkOOOOkkkkkkkkkkkkkkkkkkkkOOOOOOOO000000000000000000000000000000OOOOkkkkkkkkOOOOOOOOO00000000OOO00OOOOOOOOOOkkxoooooddooddxxdd");
        System.out.println("OOOkkkkkOkkxxxxxxxkkkkkxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxdddxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxkkkkkkkkkkkxoodl;cOKOkkkoclxXNKOkOOOOOOOOOOkkkkkOOOOkOOOOOOOOOO00000KKK000000000000000000OOOOOOOOOOOxxkOOOOOOOOO00OO0000OO0OOO000OOOOOOOOOOOxooodddddoodxxxxx");
        System.out.println("OOOOOOOOOOOkkxxxxkkkkkkkxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxkkkkkkkkkkxxkkkkkkkkkkkkkxc;:c:,:kXKOkkolokXNKOkOOOOOOOOOOOkkOOOOOOOOOOOOOOOO000KKKKK000000000000000OOOOOOOOOOOOOOkodOOOOOOOO0OOOOOOO000OOOO0000OOOOOOOOOOOxoooodddoooddxxxx");
        System.out.println("OOOOOkOOOOOOOkkkkkkkkkkkxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxkkkkxxxxxxxxkkkkkkkkkkkkkkkkkkkkkkkkkko;;;:;;:kXXOkkoloxKNKOOOOOOOOOOOOOOOOOOOOOOOOOOOOO000000OOO000000OOOOOO000OOOOOOOOOOOOOOOkodOOOOOOOOkxxxxxxxxkkkkkO0000000OOOOOOOOxoooooddooooddxdd");
        System.out.println("kkOOkxkOOOOOOOOOkkkkkkkkkkkkkkxkkkxxxxxxxxxxkkxkkkkkkkxxxkkkxkkkkkkkkkkkkkkxkkkkkkkkkkkkkkkkkkkkkkkkkkko;,,,;;:kXKOkkoclxKXK00OOOOOOOOOOOOOOOOOOOOOOOOOOOOO0OOOOOOOOOOOOOOOOOOO00OOOOOOOOO0000OkdodxdddxxxdoooooooooooooodxkOOO00OOOOOOOxollooooolloooddoo");
        System.out.println("ccloollodxxddxkOkxxkOOOOOOkkkkkkkkkkkkkkkxxkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkOkkkkxdolllllc;,,,;;cOKKOkxlclxKXK000OOOOOOOOOOOOOOOOOOOOOOOOOOOOOkxkkkkkkkkkkkOOOO00000000000000000OdooooooooooooooooooooooooolodxkOOkkkOOOOxoccllllllcclllolll");
        System.out.println(";;:::::::cc:::oxxxxkOOOOOOOkkkkkkkkkOkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkOOOd:;:;',,,,,,,;;;lkKK0kl;ck0KKK000O000OOOOOOOOO000OOOOOOOOxodxxdooooooooooodddddxxkOO000000000000kolllloooooooooooooooooolllllooddooooddddolcccccclccccllllcc");
        System.out.println(",;;;;;;;;;;;;;;cdxxkkOOOOOOOkOOOkOOOOOOOOOOOOOOOkkkkkkkkkOOOOkkkOkkkOOOOOkkkkkkkkkkkkkkkkkkkxl;,;:,'''',,,,;;,ck000kl;lk00KK000000000000000000000OO0OOkdlllllllllllooooooooooooodxkOOOOOOOOO0Oxllllllllooooooooooooooollllllllllllllcccccccccccldxkkkxxocc");
        System.out.println(",,,,,,,,,,,,,,;;cdkkkkkkkOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOkkOkkkkko;,,;;,','',,,,,,;okOO0x:;okO000000000000000000000000000OOkdollllllllllllllllolloooodddxxxxxxxxkkkolllllllloooooooooooooooollccccccccccccccccccccccldxxxkxdocc");
        System.out.println(",,,'',,,,,,,,,,;;lxkkkkkkkkkOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOkddkxlccc:;;,;;,,,,,,,,,,,:dkOOOx:;lxkkO00000000000000000000000000Oxooooooooooollllllllolooodddxxxkkkkxxxxxolllllllooooooooooooooooolllclcccccllllllllccccccclllllllccc");
        System.out.println(",,,,,,,,,,,,,;,,;:okOOkkkkkkkkOOOOOOOOOOOOOO000OOOOO0OOOOO00OOOOOOOOOOOOOOOOOOOOOOkl;;ll:;;;;;;;;;;,,,,,,,,,,;ldxkkd:;cxxxkO0000000000000000000000000kdddodddddddooloooooooooddxxxkkkkkkkkkkkkdlllllllloooooooooooooooollllllcclllllllllllllcccccccllcccc:");
        System.out.println(",,,,,,,,,,,,,;,;;:cdOOOkkkkkkkOOO000000OOO000000000000000000000O0OOOOOOOOOO000000Oo:;;;;;,,;;;;;;::;;;;;;;;,,;cloxxol:cdkkkO000000000000000000000000OkxxxxxxxxxxddooddddddddxxxkkOOOOOOOOOOOkxollllllllloooooooooooooooolllllllllllcllccllcccccccllllcccc:");
        System.out.println(",,,,,,,,,,,,,;;;;::cdOOkkkkkkkkkkO00000000000000000000000000000000OOOOOOOO0000000Od:;;;;;;;;;::::::::::;;;;;;:cclolooc:lodxkOO00000000OOOOOOO0000000OOkkkkxxxxxxxxdxxxxkkkkkkOOOOOOOOOOOOOOOxdollllllcllooooooooooooolooolllllcccllcccccclllcccccllccccccc");
        System.out.println(",,,,,,,,,,,;;;;;;;;:cdkkxxkkkkkkkO000000000000000000000000000000000000000000000000xc;;;;;;;;;::::::::::;;::cclolcc:cl:;:coodxxk00000OOOOOOkOOOO00OOOOOOkxddddddddddxkkkOOOOOOOOOOOOOOOOOOOOOkxddollllllooooodooodooooooolllllllllllllllclllllcllllcccccccc");
        System.out.println(",,;;,;;;;;;;;;::::::ccoollokOOOOO000KKKK000000000000000000000000000000000000000000xc:;;::::::::::::::::;:::cldxdlloooc::coooodk0OOOkkkkkkkkkkkkkOOkkOOkkxxxddddxxxdxxkkOOOOOOOOOOOOOOOOOOOOOOOkdolllllooooooddddddoooooollllllllllllllllllllllllllcccccccc");
        System.out.println("do:;::::::;;;;;:::::cccccclxO00000KKKK0000000OO000000000000000000000000000KK000KKKkl:::::::::::::::::::::::::cooccdxxoccodddddkOOkkxxxxxxxxxkxxxxxxkkkkkxxxxxxxxxxxxxkkOOOOkOOOOOOOOOOOOOOOOOOkxoooooooooodddddddddddoooollllllllooooooooooollooolllllllll");
        System.out.println("OOd::cclllcc:::::::::::::ccokO0000KK000000000OOOOOOOOO0000000000000000000000OOO000kollccccccccccccccccccccccccodlcldddlcooooodxOkkxxxxxxxxxxxxxxxxxxxxxkkkkkkxxxxxxxkkOOOOOOOOOOOOOOOOOOOOOkkkkxxxddddooodddddddddddddddoollllloooooodddddddddddddddoollll");
        System.out.println("kxdlloooooollcccc::::::;;::lxOkkO00000000KK000000000000000K000K000OO000KK00000000koolllcccccclllllllllllllcccccooccllllclloooodkkxxxxxxxxxxxxxxxxxxxxkOO000OOkkkkkkkOOOOO0OOOOOOOOOOOOOOOOOkkkkkxxxxxddddddddxddddddddxxddddoolooooddddddxxxxxxxxxxxxddddd");
        System.out.println("xxdddddddddddoollllcccc::::ldxxxkkkkOOO000KKKK0000000000KKKK0000000000KKKKKKKKKKKkooolllllllllloooooooooooolllcllllloooccllooodxkxxxxxxxxxxkkkkkkkkkkOO0000000O000000000OkkOOOOOOOOOOOOOOOOOOOkkkkkkkkxxkkkkkkkxxxxxkkkkkxxkkxxdxxxxxxxxxxxxxkkkkkkkkxxxxx");
        System.out.println("xddddddddddddddddddooooolccodxxxxxxxkkOO000KKK0000000000KKKKK00000000000KKKKKKKKKkoooooooooooodddddddoodddddoooooolllodoooooxkkkkkkkkkkkkkkkkkOOkkkOOOO0000000KKKKKKK000xlokOOOOOOOOOOOOOOOOOOOO000OOOOOOOOOOOOOkkkkkOOOOkkOOOOkkkkkkkkkkkkkkkkkkkkkkkkkkk");
        System.out.println("dddddddddddxxxxxddddddddoolodxxdddxxkkkOO00000000000000000KKK00000000000000KKKKK0kddddddddxxxxddddddddddxxxxdddooolllooolooxOOOOOOOOOOOOOOOOOOOOOOOOOO00000KKKKKKKKKKK00dldk0OOO0000OOOOOO0000000000000000000000OOOOO0000OOOkOOOOOOkkOOkkkkkkkkkkkkkkOkkkk");
        System.out.println("dddddddxxxxxxxxxxxddddddddddxxxxxxxxxkkkOOOOOOOO000000000000000000OOOOOOO00000000kddddddxxxxxxxxxxxxxxxxxxxxxdddooooooooodk000OOOO0000OOOOOOOOOOOOO0000000KKKKKKKKKKK0K0dldO0000000000OOO00000000KKKKKK0000000000000KKKK00kolx00000000000OOOOOOkxxkOOOOOOO");
        System.out.println("cdxddxxxxxxxxxxxxxxdxxxxdxxxxxxxxxxxxxkkkOOOOOO00000000000000000OOOOOOOOOOOO00OOOkxxxxxxxxxxxxxxxkkkkkkkkkxxxddddooooooddxOKKKK000KK0000000000000KKKKK0000KKKKK00KKK000koldO000000000000000000000KKKKKKK00000KKKKKKKKKKKK0xcok0KKKKKKK00000000OxddkOOOOOOO");
        System.out.println(",oxxxxxxxxxxxxxxxxxxddxxxxxxxxxxxxxxkkkkkOOOOOO000000K00OOOOOOO0OOOOOOOOOOOOOOkkkkxxxxxxxkkkkkkkkkkkkkkkkkxxxdddddddddddddkO0000KKKKKKK00000KKKKKKKXXXKKKKKKKKKKKKKK0K0xlodk00000000000000000000KKKKKKKKKK0KKKKKKKKKKKKKK0dclkKKKKKKKKKKKKK000OdodxkOOOOkx");
        System.out.println(".:dxxxxxxxxxxxxxxxxdddxxxxxkkkkkkkkkkkkkkOOOOO0000000OxooodddxkkkkOOOOOOOOOOOkkkkkkkkkkkkkkkOOOOkkkkkkkkkkkxxxxdddddddooodkxdoxkkxxkkkkkkkO000KKKKKKKXXKKKKKKKKKKKKKKX0xoodxxxkkkkkkkOOOOO000000KKKKKKKKKKKKKKKKKKKKKKKK0klclx0KKKKKKKKKKKKK00koooddddxxxd");
        System.out.println(".'lxxxxxxxxxxxxxkxxxxkkkkkkkkkkkkkkkOkkOOOOOO0000000kollloodddxxxxkkkOOOOOOOOOOOOOOOkkkkkkkOOOOkkkkkkkkkkkkxxxxxddddddddox0OdoxKOddOOdoxxdodxxxxkkkkkOOO0000KKKKXXXXXX0xoodkxdO0OddkOkooxkxddxkkxxkkOOOOO000000KKKKKKKKKOocclx0KKKKKKKKKKKKKK0xooooooooooo");
        System.out.println(".';lxkkkkkkkkkkkkkkkkkkkOOOOOOOOOOOOOxxkOOOOOOOO00OkolllloodxxxkkkkkOOOkkkkkkOOOOOOOOOOOkOOOOOkkkkxkkkkkkkkkkkxxxxdddddddxOkddk0Odx00ddO0xox0OddOOxodkkddxkkxxkkOOOOOkxdoodOxxKX0xxKXKxxKX0ddOK0xoxOOxooxkkdodxkxxxkkkkkdlcccokOO000KKKKKKKKK0xoooooooooll");
        System.out.println(",,,:dOkOOOOOOOOOOOOOOOOOOOOOOOOOOOOOkddkOOOkkkkOOOkdlllooloxxxxxxxkkkkkkxkkkkOOOOOOOOOOOOOOOOOOkkkkkOOOOOOOOOOkkkkxxxxxxxk0Oddk0OdxOOddO0xoxOOddO0kdx00xokK0xdk0OdokOkooooxOxkKX0xkKX0dkKK0dx0KKxd0XXOodO00xokKKOolxOOxlcclllokOkxddkOOkkkkOOxooooolllllll");
        System.out.println(",,,:lkOOO0000000OOOOOOOOOOOOOOOOOOOOkxxkkkkkkkkkkOkxoooooooxxxdddddxkkkxxxkkOOOOOOOOOOOOOO000OOOOOOOO000000OOOOOOkkxxxxxxO0Oxdk0OdxOkddkOxdkOkddOOxdx0Oxok00xdOK0xdO0xooodkOxOKKOxOKKOdkKKOdxKK0dd0KKkooddkkk0KK0od0KKklcclllkKKKOdxKKKOddkOkdooollllllllc");
        System.out.println("',,,;oO0000OOOOOOOOOOOOOOkOOOOOOkkkkxdxkkkkkkkkkkkkkdooooooxkkxxxxxxkkkkkkOOOOOOOOOOOO00O00000000000000000000OOOOOkkkkkkkkOkxxkOkdlc::coxddk0kdxOOxdkOOxdO0OxdOKOddOOdooddkOkOKKOxOKKOxOKKOdkKK0dx000xldxxxxO0KKOoxKK0dccccloOKKKkdOKKKOdx00xooolllllllccc");
        System.out.println(",,,'';lxOOOOkkkkOOOO000kdddkOOkxdddolloxxxxxkkkkkkkkxdoooodxkkkkkkkkkkkkOOOOOOOOOOOOO000000000000000000000OO0OOOOOOOOOOOOOOkkkkOx:'',:codddkOkdxkkxdkOkddO0OxxO0Odx0OddddxOOkOK0kxOK0kxO00kdO0KOdx00OdoxkdookO00xlx00OoccccldO0K0xx0KKKkokOxollllllllccccc");
        System.out.println(",,,,',,cxkkkkkkkkkOOOOOdlllodxdolcccccooooxxxxkkxxxxdddoddxxkkkkxkkkkkkOOO00000OO00O00000000000000000000OOxkOOOOOO00000OOOOOOOOkl,',;coddddxOkxxkkxdkOkxxO0OxxO0Odx0OddddxOOk0K0kk0K0kx00OxdxkkxodxxdocodxooO0OOdlxOkxlccclldkOOkdxO00OxdOkllllllllccccccc");
        System.out.println("',,,,,,;coooooooooddddolllccccllcccccclllldxxxxxxxxxxxddddxxxxxxxkkkkkOO000000000000O000000000000000000OkxxxkOOOOO00OOOOOOOOOOOk:.';ccldddxkkkkkkkxxkOkxxO0OxxO0kxkOkddddxOOkOOOkkkOkxxkkxddddddooooolccllldO00OooOOOdcclllodxxxxddkkkxodkdllccccccccccccc");
        System.out.println("..','',,,,,;;::::lodoolllcccc::cccccccllloodxkxxkkkkkxxddxxdxxxxxkkkkkkOOOOOOO000000000000000000000000OkkkkkkOOOOOOOkkkkkkkkkkOx,..,::clldkOkkkkkkkkkOkxkOOOxkOOkxkOkxxxxxkxxxxxxdddddddddddddoooolllllll:cxO0OkodO0Odccloodxxxxddxxxxdodkxlllcccccccccccc");
        System.out.println("...'''',,',,;;;;;cdxddoollllllcllllcclllodooddxxxkkkxxxxxxxxxxxxxkkkkxxkkOOkkOOO000000000000000000000OOkkkkkOOOOkkkkkkxddoolccc;'..',,,;cdkkOOOOOkkkkkkxkkkkxxxxxxkkkkxxxxxddddddddddddddddoooolllllodddoclxkkxolooddlclooodxxxxddxxxxdodxxollllllllllllcc");
        System.out.println("'..''''''',,,,,;;:lodoolllodddoooollloooodddoodxxkkkkkkkkkkkkkkkkkkkkkkkkkOkkkOOO00000000000000000OOOOOkkkkkkOkkkkkolc::::;,,,,'....'....'';lxOOkkxxxxxxxxxxxxxxxkkkkkkxxxxdddddddddddddddddoollllcldxxooodxdoooolccccclooddxxxxxxxxxxddddoooooooooollllll");
        System.out.println("'..''''''',,,;;;;,,;clllllllooollccllddddddoooddxxxxkkkkkkkkkkOOO0O00OOOOO0OO000000000000OOOOOOO00O0OOkkkkkkkkkkkkkxl,,',;;,;::,............':loddxxxxxxxxxxxxxxxxxxxxxxxxxdddddddddddodoooooolllccccllollllcccllc::cclooddxxxxxxxxxxxxddddoodddxxxddddooo");
        System.out.println("''.''''''',,,;;;,,,;::ccccccllcc:::cloddddddddddxxxkkxxxkkkkkOOOO00OOOO00000000000000OOOOOOOOOOO00000Okkkkkkkkkkkkkkc....,c;,'''..............';:cldxxxxddddxxxxxxxxxxxxxxxddddddddddddoooooooool:::::::::::::::::::oddxxxxxxxxxxxxxxxxxdddddxkkkkkkkkkkxd");
        System.out.println("...'''''''',,;;,,,,,;;;;:::cc;;:::ccllodddxxxkkkkkkkkxxxxxxxxkkkkOOOOOOOOOOO000OOOkkOOOOOOOOkkkkOOOOOkkkkkkkkkkkkkkd,....,c:''..................',:ldxxxddddxxxxxxkkkxxxxxxddddddddddddooooooool:::::;;:ccccccccccloxkxxxxkkkxxxxxxxxxxxxxxxkkxxxxkkkkkkkx");
        System.out.println("....'''''','',,,;;,,,;;;::cc:;;;:cllllodddddxkOOOOOkkkkkkkxxxxxkkkkkkOOOOOOOOOOkkxxxkOOkkkkkkkkkkkkkkkkkkkkkkkkkkko,..'..';,''...................':oxxxddxxdxxxxxxkkxxxxxxxxxddddddddddoooollll::::::;:cllllccllcccoodxxxkkkkkkkkkkkxxxxxxxxxdodxxxkkkOOkk");
        System.out.println(".''''''''''''''''''',,;ccccc:::ccllllodxdlcloxkOOOOOOOOOkkkkkkkkkkkOOOOOOOOOOOkxxxxxxxxxxxxxxxxkkkkkOOkOOkOOOOOOx:'.''''.',......................,':dxxdxxxxxkkkkkkkkkkkkkkxxddxxxdddddooooooc:::cc::::clool:ccc:;;::clodxkkkkxxddxkkkxxxdddolccdkkkkkOkkk");
        System.out.println("'',,,,,,,,,,'''''''',,,::::cccccllllloddlloooodxOOOOOOOkkkkkkOkkkkOO0OOOOOOOkkxxxxxdxxxxxxxxxxkkOOOOOkkOOkOOOkxc,.'''',,........................'..'lxxxxkkkkkkkOOOOkkkkkkkkkkxxxxxxddddoooolc::cccccccclodl:::;;;;;:cloddxkkxlcccdxkkxdddolll::lxkkkkkkkk");
        System.out.println(",,,,,;;;,,,,,'''',,,,,,,;;:clllllooolllloodooloxkOOOOOOOOOOOOOOOOOOOOOkkOOOkkkxxxxdxxxxxxxxxkkkkOOOkkkkkkkkkd:'..''',,,.............................,ldxkkkkOOOOOOOOOOOOkOOOOkkkkkkxxdddddocccccccccccllcllc:::;;;;;:lloodxkkl:;;:clooooddolllclodxkkkkkkk");
        System.out.println(",,,,,,,,,,,'''''',,;;;;;;:::ccccclllllooooooodddxkkkkOOOO0000000OO00OOkkkkkkkkkxxxxkkkxxkkkxkOOkkkkkkkkkkkd:...'''',,;'.'. ...............':::'.....',cxOOOOOOOOOOOOOOOOOOOOOOOkkkkkxxxxxxocccc::::cccllc:::::::;;;;;::ccoxkxlcc:;;;;;;;;::clooooodddxkkkk");
        System.out.println("'',,,,''',''''''',,;;::::cccccccccclllllooooooodddddddddxkO0000OOO0OOOkkkkkkkkkxxxkkkkkkkkkkkkkkkkkxkkkkkd,...''',,,;,.;,. ...............'okkl......',:dkOOOOOkkkOOO00OOOOO00OOOOOkkkkkkxlccc::::::cccc:::::c::;;;;;;;:cllllc:::;;,,,,;;;;:clllllllloxkkk");
        System.out.println("'''''''''''''''',,,;;;;::ccccc:ccccllllllooooddddddddddxxxk0OOOOOOOOOOkkkkkkkkkxxxxkkkkxxxxxxxxxxxxxkkkkkkc...''',,,;c:........ ...........'lkx;......',,lkOOOOOOOOOO00000000000000OOOOkkxlccc::::::cc:::::::::::;;;::;;;::;::::::;,,,;:;,;;:::cclllloxkkk");
        System.out.println(",''''''''''''',,,,;;;;;:::::::::ccllllllllloddddddxxddddxxkOOOOOOkkkkkkkkkkkkkkkkkkkkkkxkkkxxxxxxxkkkkkkkl'....',,,,;xd......................ckd'......',ckOOOOOOOOOOO00000000000000OOOOkdccccc:::::::::::::::c::;;;;:::;;;;;;::;,,,,,,,,',,,,,,;::cccoxkk");
        System.out.println("'''''',,,,,,,,;;;;;;;;;::ccllcccccccclllllloddooodddddxxdxkkOOOkOOOOOOOOOOOOOOOOOOOkkkkkkkkkkkkkkkkkkkkkd,..',;coo:;lo;'...........''........,dOo......;okOOOOOOOOOOO000000000000000OOOOkoccccc:::::::::::::::::;,,,,,,;;,;,,,;,',,;,,,,,''''''',,,;;:lddx");
        System.out.println(",,,'',,,,,,,,;::::::;;;:ccclollcloolllllooodddolooddddxxddkOOOOOOOOO0000OOOOOOOOOOOOOOOOkkkkkkkkkkkOOOkd,.,;:cxOOOOxl'....''..,,...c:.    .'..;dkc....,oOOOOOOOOOOOOOOOOOO000000000OOOOOkoccccc::::::::::::::::;,,,,,,,;,,;;;;;;,,,,,;,,'''',,',,,,,;;;;:l");
        System.out.println(",,,,,;;,,,,,;:ccclllllodddolllllooooodddoooddooooodddddxxxkOOOOOOOOOOOO000000OOOOOOOOOOOOOOkkkOOOOOOOkkc.;dxxxOOOOkc..........,,...;,...  .....'ld;...'lOOOOOOOkddkOOOOOOOOO0000O00OOOOOxlcccccccccc:::::::::::;,,,,;;,,;,,;;;:::::;,,,,,''''''''',,,'',,,");
        System.out.println(",,,,,;:;,,,;:cclloodddxxxxdddooooooooddxxxxddooooddddddxxxkOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOkOOOOOOOOOdlxOOOOOOOd;......,,',,;'...,;,'..........',..',lOOOOOOkc,,:xOOOOOOOOOOOOOOOOkkkdlcccccccccccc:::ccc:::;,,,,,,,,,,,,,;;;::::::;,,'''''''..'''......");
        System.out.println(";;;,,;;:::cloooooooddkkkkxddddoooooooooodxxxdooooddddddxxxkkkkkkkkkkkkkOOOOOOOkkkOOOOOOOOOOOkkkOOOOOOOOOOOOOOOOko'.......''.,;,. ...',,;,........,'....:k0OOOOx;';:lkOOOOOOOOOOOOOOOOkxlcccccccc:::clc:cccccc:;,,,,,,,,,,,,,,,,;;;;;;;;'''.'''...'........");
        System.out.println(";;;;;;;:coodddddddddxkxxdddxddooooooooooodooddoddddddxxkxxxkkkkkkkkkkkkkkkkkkkkkkOOOOOO000OOOOOOOkkkkOOOOOOOOOxc....... ..,;,,'....':ccc;......:dkkl;'..,cokOOk:;dOkO0OO00000OOOOOOOOkdlcccccccc::cllcc::cccc:;;,,''',,,,,,''''',',,,,,'..'''.............");
        System.out.println(",,;:::::coooddxkdodxxxdxxxxxdooollloddddxxxxxdddddxxxxxxxxxkkkkkkkkkkkkkkkkkkkkkkOOOOOOO000000OOOkkkkOOOOOOOOd;.......  .:l;'......'::lo:,'....,oO00Okxdl;,,cddc:dxkO0000000000OOOOOOkdllllllcccccclccc::::c::;,''''''''''''''''..''',,''''...............");
        System.out.println(",,;::::;:loolokkxlclodxO00OkxdoolooddxkkkkkkkxxxxxxxxxxxxddxxkkkkkkkkkkkkkkkkOOOOOOOOOOOOOOOOO0OOOOkOOOOOOOko'.......  .,lc'.'.....,::cc,:c:,...,oO000000Okdoc,',,,;oO000000O0OOOOOOOkdlllllllcccccccc:::::::,,,''''''''''''.......'','''''...............");
        System.out.println(",,,;;;;;::c::coolcccldxxxkkkxxxddxxxkOOOOkOOkOkkkkkkxxxxddddxkkkkkOkkkkOOOOOOOOOkkOkkkkxdddxkOOOOOOOOOOOOOkc........   .;c,,;'.....;llllccodd:...:x0000000000Od:,'..'o00000OOO0OOOkkkxoooollllllcccccc::::::;,,,'''''''''''...''''..'''.'.................");
        System.out.println(";;;;;:;;;;;:::cccllllloooodxxxkkkkkkO0OOOOOOOOOOkkkkkkxxdxxxxkkOOOOOOOOOOOOOOOOOOOOkdlc;,,,;cooodxkkOOOOOOxl,........ .,c:'''......,:oxxxclxdl;'.:kK0KKKKKKK0kc:oc'.,o0000000OOOOOkxxdddddoooollllcccc::::::;,'''.'..''''''...',;;,'''....................");
        System.out.println(",,,;:::;;;:::cccclllodxxkkkkOOOOOOkO00OOOO00000OOkkkkkkkkkkkkkkOOOOOOOOOOOOOkkxdlcc;'....''''''',,;::clooddxc....  ...;:'....';:;'.';cokxclkOxo:',d0K00OkOOOkl,:xOxdxO0000000OOkxxxxdddddddoooollllccc::::::;'''..''....'''....',,;;,'....................");
        System.out.println("',,,,;;;::::cccclloddxxkkkOOOO00kxk0OkkO0KK000OOkkkkkkkkkkkkkkkkkkkkkkOkxdo:,,'.....................''''''',..... ...':'.   .,;::'.;lc:dxocdxxxo:,:xOkkdloodd:'':lodxOOOkkOOOOkxxddddddddddooooolllcccc::::;,,,'..'''............''''.........'''''...''..");
        System.out.println(",,,;;;;;:;;:::ccllloddxxddxkOOOOxloxxdxOkkO0000OOkkkkkkkkkkkkOOOOkkkkxdxxdl;.........................''''''.....  ...''..   .;;:;'.:c,.;lo::dkkxo;,;coxxoc:lkd;..;ldxOO0OOOOOkkkxxdddddddddoooollllccccc::;,,,'''''...............................''....'.");
        System.out.println(",,;;:;;;;;;:::cclllodxkkxxxxkkOkxdoooldxkxkkO000OkkkkkkkkkkOO00OkkkdodkOkko:'.................................    ..'...   .,::;,..,,..,:ol:cooddooc:dkkxxdloxxooxxkkxdlcd0K0xoddoddddddddooooollllllcccc:;,,,''.''''....'....................'.....''...'");
        System.out.println(",,,;:;;;;;;::cccllodxxkkxdoodxxdddxxxddxxddxxxOOOOOOkkkxxkkkOkkxxxlcdxxxdoc:,.................................   .','..    .,::;,..:;..'';odloddxdxd;,ldoddkdldOOOOOkxxl;clokko:,,:loddddddooooooooolllc:;,,'''''''''...''''...........................'''");
        System.out.println("',,;;;;;:cccccllllooddxxkxoclllcloodxxxxoodxxxxkkOOOkkxxddxxxxdxd:,lxxolllc:,.    .....    .............   ...   .,;'..    .;;;;,'.,,...'',oxdooxxxdl::ldxxdlcoO000OOOxlccccll;,;:c::clcloddoooooooolllc:;,,,'''''''....'''.............................''");
        System.out.println(",;;;;;::llccccclllloxxkkkkoc::::clloodddddxxxkkkkkkkkxxddooooool,.,cldddoc;,,'.                 ......     ..    .:;;'.   .';;;;;,','....'.,coccdkkkx:,lkkollldkkO0Okxddc;:::c;,'','....',;:c;;:cloolllc;;;;,,,,,,''''..'''...............................");
        System.out.println(";;;;;;:cllcc::::cccldkkkkxdolccccoodoooddxxxxxkxxxxxxxxddollllc'..',,;;;,,,,;;,..              ...   .          .,::l:.   .',,,,;:::,.......;kkooolddlc;;loodkOOkkOK0Okdlc::::;;,','...'''''.....'',:clc:;;;,,,,,,,,'''''''...............................");
        System.out.println(";;;;;;:cllcc::::cllodkOOkxddooolloddddddxxxxkkkxkkxdddddolllc;........',;cl::::'.            ..           ..    .c,'::.   .',,,,;;:;'........lxoooddxxx:'ckxooddxk0KK0Oolllc::::::;;,,,,,,''.........';::;,,,,,,,,,,,,'''.................................");
        System.out.println("cc::;;::cccccccclllldkOOxddddddoooddxxxxxkkkkkkxkkxxxdolllll;.....',,;cccooccl:'.......... ...            ..    .''';:.   .,;;,,''''......'..;ddc:lodoc,.,ooclloddO00Oxoloddlc:::::c:;'','''''.........',;;;;,,,,,,,,,,,'................................'");
        System.out.println(";;;;;;:::::cllooollloddddddddddddddddddxxxxkkkxxkkxdddoolll,.....',,;:::::;;:::;,'.. .  ......           ..    ....'::.   .'''''''.........''.'oxl::c;',;cclodxkOkxdolcclodkkkxxxdood:';:;;::;,.....,'',;;;;;;,,,,,,,,,,''................................");
        System.out.println("',,,,;;::ccllooodooooooloddxxxxdddddddddddddddddddollloool'....',;;,,'',,,,,,;:::,..  ..''''''...        ..    ....','.   ''...''''...    .....:xd:'';lxkOkxxxOOOkoc::::ccodk000KKkdo::cccl:,,;',:;',;;;;,;;;,;;;,,,,,,,'.................................");
        System.out.println("'',,,,;;:ccclloooollllllodxxxxxdddddxxdoloddddoollllllll:....'';cll:,,,,,,''',,,'..  .,colclodo:..      ..        ....    ......'...............',,'',coox00OOOkdllcc:;::ccloxkOOkkxoccdxdoc:;;,,,,;'..,;:c:;,;;;,,;;,,,'.'...............................");
        System.out.println("',,,,,;;::cccllllc::cclllooodddddddkOxoodxxdooolllllccc;..',;;;:clc:,,,,''........ ..;coxdodxkkxl'.              ..      .......'......  ...........,:cclx00OOkxol:;:;;;::::coxxxkkkxoodkkxdl;.':,,::,'',,:c;;;;;,,,,,,'..................................");
        System.out.println(",,,,,,,;;::::cc:::;;:::cccccloooodxkdlokkOOxolllccccc:'..;cc;''''''..''......... ..';clooddodxdol;...      ...   .       .,'...''........  ......':codddxOK0Okkxl:;,,,;;;:::::::::lkOkdlx0OOkko:,'.;cddo:',::;;;;,,,,,,...................................");
        System.out.println("''''',,,,;;;;;;;;;;;;::cccclooolcldoccoxxxkxlllccc::;..',,,'....   ..'...........',;;;::cooccoc;,'.........''... ......  .:c;,'.. .........''',,;:oddkOO0K0Ododxl::,'',,,;;:::;,;;:coxdcdOOkkxko,....cddl,..,cl:;;,,,''...................................");
        System.out.println("''''',,;;;;;;;;;;;;;;::ccllloolcccc::oxkxkOdccc::::,. .....      ...'........',,;;;:::cllodc;;,'','''',;,....'''...'',;:;,cdddc.  .,cclc;,,;;,,,,;:cdO00Oxxxxdoc;:ll:;,',;:::::coolcccccdkkkkkkx:'....,cl:;,.'ll:;,,,'....................................");
        System.out.println(",,,,,;;;;;;:;;;;,,,,,;;:cccccllooolc:dkkOOkl::::;;'.         .........   ....',,;;;;;:loolc;,,;;,'',:oooolc,',;,,;coc;:ooodddddc,;lxOOxl:,,;,,,,,,;:cclllodooxkxolc:cllcccclodxkOOkdcccclooddoccc:;,'..',,;:;.'cc;,,'.......  ............................");
        System.out.println(":::;;;:::::;;,''''..'',;;;;:cc:clol::okOOOxc;:;;,.        ..........     .......,;;;;:oo:;;,,,::::;,;:ldxxxdlclcclol:;:llol:::cllldxO0kdc;,,,,,,;;;:;;:clddl:lodoooloodxdoldk000kol::cllxxxxkxddol::;,,;;,,,,''lo:;,'.....................................");
        System.out.println(":::::::;;;;,,'''......'''',;:c:::::::lxkxxxc;;'..      ....'''...       ........',,;:;;;;,,;;;::cc:'.'';clloodxxdlc:;,,,,,;,,,;coxxxkOOko:,,,'';::::::ccoxxoc;;;;;;clloolodxkkkxdddc:dkOd;col:;coocc:cllcc::,..:oo:'......................................");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        wait(2000);
        System.out.println("---------------------------------------------------------------Endlich frische Luft...du hast es geschafft und bestaunst nun die Weiten der Freiheit vor dir...und bist frei.-----------------------------------------------------------------------------");
        wait(2000);
        System.out.println("------------------------------------------------------------------------------------------------------------------Ende 4/4: Entkommen.--------------------------------------------------------------------------------------------------------------------");
  }  
}
