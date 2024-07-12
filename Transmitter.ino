/*
* Arduino Wireless Communication Tutorial
*     Example 1 - Transmitter Code
*                
* by Dejan Nedelkovski, www.HowToMechatronics.com
* 
* Library: TMRh20/RF24, https://github.com/tmrh20/RF24/
*/

//AIR HUMIDITY AND TEMPERATURE SENSOR-------------------------
#include "dht.h"
#define dht_apin A1
dht DHT;

int check;

int start = 2525;
int data[8];

#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>
int test = 12;
RF24 radio(7, 8); // CE, CSN  

const byte address[6] = "00001";

void setup() {
  //Starts the Radio Transmission
  radio.begin();
  //Tells the Transmitter which adress to sent the data to
  radio.openWritingPipe(address);
  radio.setPALevel(RF24_PA_MIN);
  radio.stopListening();
   delay(1000);
}

void loop() {
   int start  = 2525;
    radio.write(&start, sizeof(start));
    delay(1000); 
    
    DHT.read11(dht_apin);
    
    //Air Humidity
    data[0] = DHT.humidity;
    radio.write(&data[0], sizeof(data));
    delay(2000);

    //Air Temperature
    data[1] = DHT.temperature;
    radio.write(&data[1], sizeof(data));
    delay(2000); 

    //Soil Humidity
    int mois = analogRead(A0);
    data[2] = map(mois,200,1028,100,0);
    radio.write(&data[2], sizeof(data));
    delay(2000); 
       
    //Soil Temperature
      
    data[3] = analogRead(A1);
    radio.write(&data[3], sizeof(data));
    delay(2000);
    
    //Sunlight
    int sunlight = analogRead(A2);
    data[4] = map(sunlight,0,1000,0,100);
    radio.write(&data[4], sizeof(data));
    delay(2000);
}
