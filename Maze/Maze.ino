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



Adafruit_8x8matrix matrix1 = Adafruit_8x8matrix();
Adafruit_8x8matrix matrix2 = Adafruit_8x8matrix();

int xPos = 6;
int yPos = 3;

int in1 = 33;
int in2 = 32;

//blinking logic
static int deltaTime = 5;
static int blinkDuration = 250;
int blinkTime = 0;
int remainingTime = 0; //startGame initialises

//buzzing logic
int buzzAddress = 25;
int buzzTimer = 0;

//default
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

void setup() {
  
  Serial.begin(9600);
  
  //wifi setup
//  serverconnect();

  //maze setup
  //pinMode(buzzAddress, OUTPUT);

  //get a randomisation seed using the sliders
  int seed = analogRead(in1) * 4096 + analogRead(in2);
  Serial.print("Seed: ");
  Serial.println(seed);
  randomSeed(seed);

  bool steady = true;
  while (steady)
    steady = !generateWalls();
    
  matrix1.begin(0x70);
  matrix2.begin(0x72);
  displayAll();
//  while(command != 65){
//  waitforresponse();
//  }
  startGame();
}

void loop() {
  return;
}
/*
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
*/
void startGame() {
  remainingTime = 30000;
  
  while (gameRunning()) {
    int xValue = analogRead(in1);
    int yValue = analogRead(in2);
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

// movement
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

bool stepSingle(int dx, int dy) {
  // if no more moves are needed or there happens to be a wall
  if ((dx == 0 && dy == 0) || walls[yPos + dy][xPos + dx])
    return false;
  xPos += dx;
  yPos += dy;
  blinkTime = 0;
  return true;
}

int signum(int value) {
  if (value == 0)
    return 0;
  if (value > 0)
    return 1;
  return -1;
}

bool gameRunning() {
  if (remainingTime <= 0)
    return false;

  if (xPos == 0 || xPos == 15 || yPos == 0 || yPos == 7)
    return false;

  return true;
}

bool generateWalls() {
  bool finished[6][14] = {};
  int toFinish = 6 * 14;

  for(int i = 0; i < 8; i++) {
    for(int j = 0; j < 16; j++) {
      walls[i][j] = true;
    }
  }

  if (random(2) == 0) {
    //random x top or bottom
    int x = random(14);
    int y = random(2);

    walls[y * 7][x + 1] = false;
    walls[y * 5 + 1][x + 1] = false;
    finished[y * 5][x] = true;
    toFinish--;
  } else {
    //random y left or right
    int x = random(2);
    int y = random(6);

    walls[y + 1][x * 15] = false;
    walls[y + 1][x * 13 + 1] = false;
    finished[y][x * 13] = true;
    toFinish--;
  }

  while(toFinish != 0 && isExpandable()) {
    int x = random(14) + 1;
    int y = random(6) + 1;

    if (finished[y - 1][x - 1])
      continue;

    int neighbours = getNeighbourCount(x, y);

    if (neighbours == 0)
      continue;
    if (neighbours == 1) {
      walls[y][x] = false;
    }
    //if neighbourcount exceeds never accessible
    finished[y - 1][x - 1] = true;
    toFinish--;
  }

  Serial.println("attempt failed");
  
  if (toFinish != 0)
    return false;

  bool steady = true;
  while (steady) {
    xPos = random(14) + 1;
    yPos = random(6) + 1;
    steady = walls[xPos][yPos];
  }
  
  printMaze();
  
  return true;
}

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

bool isExpandable() {
  for(int i = 0; i < 6; i++)
    for(int j = 0; j < 14; j++)
      if(walls[i + 1][j + 1] && getNeighbourCount(j + 1, i + 1) == 1) {
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

void displayAll() {
  plotCutout(matrix1, 0, 0);
  plotCutout(matrix2, 8, 0);
}

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

void emptyScreens() {
  matrix1.clear();
  matrix2.clear();
  matrix1.writeDisplay();
  matrix2.writeDisplay();
}

void buzzDelay(int duration, int buzzFreq) {
  //buzzFreq: 0 = none, 1 = low, 2 = high

  int freqValue[] = {1, 20, 2};
  for(int i = 0; i < duration; i++) {
    dacWrite(buzzAddress, (buzzTimer % freqValue[buzzFreq]) * 255 / (freqValue[buzzFreq]));
    buzzTimer++;
    delay(1);
  }
}

void endGame(){
  client.print("game end");
}
