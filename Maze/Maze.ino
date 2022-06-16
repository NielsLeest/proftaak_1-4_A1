//wifi stuff
#include <WiFi.h>

const char* ssid     = "test_network";
const char* password = "123456789";
IPAddress local_IP(192, 168, 137, 22);
IPAddress gateway(192, 168, 137, 1);
IPAddress subnet(255, 255, 0, 0);
IPAddress primaryDNS(8, 8, 8, 8);   //optional
IPAddress secondaryDNS(8, 8, 4, 4); //optional
//maze stuff
#include <Wire.h>
#include <Adafruit_GFX.h>
#include "Adafruit_LEDBackpack.h"
WiFiClient client;
int command;


//displays
Adafruit_8x8matrix matrix1 = Adafruit_8x8matrix();
Adafruit_8x8matrix matrix2 = Adafruit_8x8matrix();

//player position
int xPos = 0;
int yPos = 0;

//pins for horizontal and vertical axis inputs (resp.)
int in1 = 33;
int in2 = 32;
//booleans for if the directions flip
bool flipIn1 = false;
bool flipIn2 = false;

//time that passes per refresh in millis
static int deltaTime = 5;
//time remaining before game is lost
//initialised in startGame();
int remainingTime = 0;

//span of a full blink on+off + counter tracking it
static int blinkDuration = 250;
int blinkTime = 0;

//buzzer pin 
int buzzAddress = 25;

//walls of the maze
//true = wall, false = passage
//below is a default
bool walls[8][16] = {
    {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true},
    {true,false,false,false,false,false,false,true,false,false,false,false,false,false,false,true},
    {true,false,true,false,false,false,false,false,false,false,true,false,false,true,false,true},
    {true,false,false,false,true,false,false,true,false,false,false,false,true,false,false,true},
    {true,false,false,true,false,false,false,false,true,false,false,true,false,false,false,true},
    {true,false,true,false,false,true,false,false,false,false,false,false,false,true,false,true},
    {true,false,false,false,false,false,false,false,true,false,false,false,false,false,false,true},
    {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true}
  };

//initialises connections and seed
void setup() {
  
  Serial.begin(9600);
  
  //wifi setup
  serverconnect();
  
  //get a randomisation seed using the sliders
  int seed = analogRead(in1) * 4096 + analogRead(in2);
  Serial.print("Seed: ");
  Serial.println(seed);
  randomSeed(seed);

  //boot matrices
  matrix1.begin(0x70);
  matrix2.begin(0x72);
}

//runs the full game loop including start and end
void loop() {
  //try to generate maze until success
  bool steady = true;
  while (steady)
    steady = !generateWalls();

  
  emptyScreens();
  //wait for a server signal to start the game
  while(command != 65){
    waitforresponse();
  }
  //start game
  displayAll();
  startGame();
}

void serverconnect(){
  if (!WiFi.config(local_IP, gateway, subnet, primaryDNS, secondaryDNS)) {
    Serial.println("STA Failed to configure");
  }
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
//    const uint16_t port = 80;
//    const char * host = "192.168.1.1"; // ip or dns
    const uint16_t port = 8080;
    const char * host = "192.168.137.1"; // ip or dns

    Serial.print("Connecting to ");
    Serial.println(host);

    // Use WiFiClient class to create TCP connections
 

   while (!client.connect(host, port)) {
        Serial.println("Connection failed.");
        Serial.println("Waiting 5 seconds before retrying...");
        delay(5000);
        return;
    }

    // This will send a request to the server
    //uncomment this line to send an arbitrary string to the server
    //client.print("Send this data to the server");
    //uncomment this line to send a basic document request to the server
    client.print("midas");

 

  //wait for the server's reply to become available
 
}
void waitforresponse(){
 
  while(client.connected()&& command != 65){  
       
      while(client.available()>0){
        // read data from the connected client
        char data = client.read();
        if (data != 0 && data != 9){
        command = data;
        Serial.write(data);
        client.write(data);
        }
        
      }
      
  }
}

//plays the game loop
void startGame() {
  //about 60 seconds
  remainingTime = 60000;
  
  while (gameRunning()) {
    int xValue = analogRead(in1);
    int yValue = analogRead(in2);
    if (flipIn1)
      xValue = 4095 - xValue;
    if (flipIn2)
      yValue = 4095 - yValue;
    int xTarget = xPos;
    int yTarget = yPos;
    if (xValue - 128 > xPos * 256)
      xTarget = (xValue - 128) / 256;
    else if (xValue + 128 < xPos * 256)
      xTarget = (xValue + 128) / 256;
    if (yValue - 256 > yPos * 512)
      yTarget = (yValue - 256) / 512;
    else if (yValue + 256 < yPos * 512)
      yTarget = (yValue + 256) / 512;
    tryMove(xTarget, yTarget);
    
    blinkTime += deltaTime;
    remainingTime -= deltaTime;
    displayAll();

    int wrongAxes = 0;
    if (xTarget != xPos)
      wrongAxes++;
    if (yTarget != yPos)
      wrongAxes++;
    buzzDelay(deltaTime, wrongAxes);
    
    Serial.println(remainingTime);
  }

  emptyScreens();
  
  endGame();
}

//attempts a movement towards the configured position and halts if this fails at any point
bool tryMove(int tx, int ty) {

  int dx = tx - xPos;
  int dy = ty - yPos;
  
  // one of the directions must be 0
  if (dx * dy != 0)
    return false;

  if (dx == 0 && dy == 0)
    return true;

  while (stepSingle(signum(dx), signum(dy))) {
    dx = tx - xPos;
    dy = ty - yPos;
  }

  if (dx == 0 && dy == 0)
    printMaze();

  return true;
}

//attempts a single step in the given direction
//acts as a helper method
bool stepSingle(int dx, int dy) {
  // if no more moves are needed or there happens to be a wall
  if ((dx == 0 && dy == 0) || walls[yPos + dy][xPos + dx])
    return false;
  xPos += dx;
  yPos += dy;
  //resets blinktimer for quick movement visibility
  blinkTime = 0;
  return true;
}

//a standard sign function
int signum(int value) {
  if (value == 0)
    return 0;
  if (value > 0)
    return 1;
  return -1;
}

//determines if the game should be stopped or not
bool gameRunning() {
  //time up
  if (remainingTime <= 0)
    return false;

  //exited maze
  if (xPos == 0 || xPos == 15 || yPos == 0 || yPos == 7)
    return false;

  //game is still going
  return true;
}

//all the maze randomisation happens here
//returns whether the generation was a success
bool generateWalls() {
  //clear the board
  for(int i = 0; i < 8; i++) {
    for(int j = 0; j < 16; j++) {
      walls[i][j] = true;
    }
  }

  //picks a direction for the exit
  if (random(2) == 0) {
    //random x top or bottom
    int x = random(14);
    int y = random(2);

    walls[y * 7][x + 1] = false;
    walls[y * 5 + 1][x + 1] = false;
  } else {
    //random y left or right
    int x = random(2);
    int y = random(6);

    walls[y + 1][x * 15] = false;
    walls[y + 1][x * 13 + 1] = false;
  }

  //while there are tiles available to expand to (always have exactly 1 open neighbour)
  while(hasNeighbourCount(1)) {
    int x = random(14) + 1;
    int y = random(6) + 1;
    
    int neighbours = getNeighbourCount(x, y);

    //not ready to be picked
    if (neighbours == 0)
      continue;
    //potential to expand, so it does
    if (neighbours == 1) {
      walls[y][x] = false;
      //sets the position so it's always in a cul-de-sac
      xPos = x;
      yPos = y;
    }
  }

  //if any walls in the maze do not directly neighbour path (to prevent large locked off areas)
  if (hasNeighbourCount(0)) {
    printMaze();
    Serial.println("attempt failed");
    return false;
  }

  //flip controls by swapping pins internally
  if (random(2) == 0) {
    int pinDiff = in1 - in2;
    in1 -= pinDiff;
    in2 += pinDiff;
  }
  flipIn1 = random(2) == 0;
  flipIn2 = random(2) == 0;
  
  printMaze();
  
  return true;
}

//counts the path pieces around a cell
int getNeighbourCount(int x, int y) {
    int neighbours = 0;
    //up
    if (y != 0 && !walls[y - 1][x])
      neighbours++;
    //down
    if (y != 7 && !walls[y + 1][x])
      neighbours++;
    //left
    if (x != 0 && !walls[y][x - 1])
      neighbours++;
    //right
    if (x != 15 && !walls[y][x + 1])
      neighbours++;
    return neighbours;
}

//checks if there is any wall not on the border with exactly that many neighbours
bool hasNeighbourCount(int count) {
  for(int i = 0; i < 6; i++)
    for(int j = 0; j < 14; j++)
      if(walls[i + 1][j + 1] && getNeighbourCount(j + 1, i + 1) == count) {
        return true;
      }

  return false;
}

void printMaze() {
  Serial.println();
  for(int i = 0; i < 8; i++) {
    for(int j = 0; j < 16; j++) {
      if (i == yPos && j == xPos) {
        Serial.print("p");
      } else if (walls[i][j]) {
        Serial.print("#");
      } else {
        Serial.print(" ");
      }
    }
    Serial.println();
  }
  Serial.println();
}

//for debugging purposes
void displayAll() {
  plotCutout(matrix1, 0, 0);
  plotCutout(matrix2, 8, 0);
}

//take a part of the grid to show on a single matrix
void plotCutout(Adafruit_8x8matrix matrix, int xOff, int yOff) {
  matrix.clear();  
  for(int i = 0; i < 8; i++) {
    for(int j = 0; j < 8; j++) {
      if (i + yOff == yPos && j + xOff == xPos) {
        matrix.drawPixel(i, j, (blinkTime % blinkDuration < blinkDuration / 2 ? LED_ON : LED_OFF)); 
      } else if (walls[i + yOff][j + xOff]) {
        matrix.drawPixel(i, j, LED_ON); 
      } else {
        matrix.drawPixel(i, j, LED_OFF); 
      }
    }
  }
  matrix.writeDisplay();
}

//clear the screens
void emptyScreens() {
  matrix1.clear();
  matrix2.clear();
  matrix1.writeDisplay();
  matrix2.writeDisplay();
}

//delay method with built-in buzzing
void buzzDelay(int duration, int buzzFreq) {
  //buzzFreq: 0 = none, 1 = low, 2 = high

  int freqValue[] = {1, 20, 2};
  int intensity[] = {0, 95, 255};
  for(int i = 0; i < duration; i++) {
    if (buzzFreq != 0)
      dacWrite(buzzAddress, intensity[buzzFreq]); // alternating current supplies the pitch
    else 
      dacWrite(buzzAddress, 0);
    buzzTimer++;
    delay(1);
  }
}

//terminate the game
void endGame(){
  digitalWrite(32,false);
  client.print("game end");
}
