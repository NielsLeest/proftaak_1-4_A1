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

int in1 = 26;
int in2 = 25;

//blinking logic
static int deltaTime = 5;
static int blinkDuration = 250;
int blinkTime = 0;
int remainingTime = 0;


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
  Serial.begin(115200);
  //wifi setup
  
  serverconnect();
  //maze setup
  Serial.println("Testing movement...");

 generateWalls();
  matrix1.begin(0x70);
  matrix2.begin(0x72);
  displayAll();
  while(command != 65){
  waitforresponse();
  Serial.println("starting game");
  }
  startGame();
}

void loop() {
  return;
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
void startGame() {
  remainingTime = 5000;
  Serial.println("starting game loop");
  while (gameRunning()) {
    int xTarget = analogRead(in1)/256;
    int yTarget = analogRead(in2)/512;
  
    delay(deltaTime);
    blinkTime += deltaTime;
    remainingTime -= deltaTime;
    tryMove(xTarget, yTarget);
    displayAll();
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

void generateWalls() {
  return;
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
    Serial.println();
  }
  Serial.println();
  matrix.writeDisplay();
}

void emptyScreens() {
  matrix1.clear();
  matrix2.clear();
  matrix1.writeDisplay();
  matrix2.writeDisplay();

 
}

void endGame(){
  client.print("game end");
}

}
