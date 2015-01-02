#include <SoftwareSerial.h>
#include <LiquidCrystal.h>

/* 
  This Arduino project uses a Bluetooth serial modem, a parallel LCD display, and your Android phone to 
  demonstrate how an Arduino can communicate with an Android device.
  
  I have added nothing new here; just taken other people's existing work to guide you on how 
  Arduino and Android can talk to each other through Bluetooth.  I created this from the Arduino LCD Display example (Igoe, Fried, Mellis),
  NewSoftSerial (Fried, Hart), and Google Bluetooth Chat.  Anyone is free to use this software as they wish. Mark Bieschke, 4/2011.
  
  Instructions for this project:
  1. Download Google's "Bluetooth Chat" Android example for your target Android version.  In the BluetoothChatService class, change the MY_UUID value to :00001101-0000-1000-8000-00805F9B34FB as follows:
      private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
      Save, Run, and plug your Android phone into your USB so the code deploys to your Android phone (can't use a Virtual device).  
      On your phone, go to "Settings - Applications - Development" and make sure you turned on USB debugging.
  2. Hook up your parallel LCD and BlueSMiRF Gold Bluetooth modem to your Arduino and deploy this code to your Arduino.  
      See my diagrams on how to wire up everything on instructables.com.
  3. Through hyperterminal (or TeraTerm), make sure you have changed your BlueSMiRF Gold BAUD rate to 57.6k.  It defaults to 115k.
  4. Open up the Android Chat native app on your phone: press the Android menu button, "Connect a Device", then select "Firefly".  
      Now you have paired Android to Arduino.  You may need to type passcode: 1234.
  5. Type and send a message from Android phone to Arduino through the text message box in the Bluetooth Chat Android app.
  6. See an Arduino-generated String ("Hello from Arduino") appear on your Android device's LCD about 30 seconds after you connect, then again 30 seconds later.
      I decided to do this as an easy, maybe lazy, way to send a test message from Arduino to Android.  

Parts list:
1. Arduino Board - I used an Uno (328, 16MHz, 5V)
2. Bluetooth Modem - BlueSMiRF Gold (Sparkfun sku: WRL-00582)
    Set BAUD to 57.6k using Hyperterminal or TeraTerm
    commands to set BAUD: www.sparkfun.com/datasheets/Wireless/Bluetooth/rn-bluetooth-um.pdf
3. Parallel LCD - Basic 16x2 Character LCD (Sparkfun sku: LCD-09051)
    Don't forget a 10k potentiometer so you can adjust contrast (a must have)
4. Android device with Bluetooth - I used a Motorola Droid for test
5. wires to connect Arduino, Bluetooth modem, LCD
6. Power supply for your Arduino
    
Software:
1. Arduino software
2. NewSoftSerial library from Mikal Hart.  http://arduiniana.org/libraries/newsoftserial/
    His library is better than the built-in "Serial." implementation.
3. Eclipse with Android Development Kit.  Make sure you follow Google's directions for downloading and installing ADK.
4. "Bluetooth Chat" sample code from Google. http://developer.android.com/resources/samples/BluetoothChat/index.html
    Super important: In the BluetoothChatService class, change the MY_UUID value to :00001101-0000-1000-8000-00805F9B34FB
    - this is a common machine UUID that allows Android to communicate with the FireFly Bluetooth module
    - save and then run; deploy to actual Android phone because you can't use a virtual device because a virtual device doesn't have Bluetooth
*/



/**************** LCD instructions *****************************
 * How to connect the typical Parallel 16x2 LCD (pins shown in parenthesis) to an Arduino Uno board:
 * LCD VSS (pin 1 of LCD) to Arduino Ground
 * LCD VDD (pin 2) to Arduino 5V
 * 10K resistor:
 *   ends to +5V and ground, respectively
 *   wiper to LCD VO (pin 3 of LCD)
 * LCD RS (pin 4) to Arduino digital pin 9
 * LCD R/W (pin 5) to Arduino Ground
 * LCD Enable (pin 6) to Arduino digital pin 8
 * LCD D4 (pin 11) to Arduino digital pin 7
 * LCD D5 (pin 12) to Arduino digital pin 6
 * LCD D6 (pin 13) to Arduino digital pin 5
 * LCD D7 (pin 14) to Arduino digital pin 4
 * LCD LED+ (pin 15) to Arduino 5V (only if LCD has backlight capability)
 * LCD LED- (pin 16) to Arduino Ground (only if LCD has backlight capability)
*/
// initialize the LCD library with the numbers of the Arduino digital pins above
LiquidCrystal lcd(9, 8, 7, 6, 5, 4);

/**************** Serial Port instructions *****************************
  NewSoftSerial allows us to create a serial communications port out of thin air on pins 2 (RX) and 3 (TX) to communicate with
  the Bluetooth modem (which then communicates to Android device via Bluetooth).  
  The built-in Arduino Serial communication to the computer is on pins 0 and 1 and we don't need to mess with 
  those pins because we may want to communicate with the computer through pins 0 and 1 so that's why we create 
  another serial port on pins 2 and 3.
  
  The Bluetooth modem I used defaults to 115k BAUD which is too fast for Arduino to receive from Android
  At low BUAD, like 9600, Arduino sends a "choppy" message to Android (I don't 
  know how to better articulate it but go ahead and try to connect at 9600 and see what I mean).  
  To change from 115k default baud, using hyperterminal, 
  put the Bluetooth modem in "command" mode ($$$) and change to 57.6k BAUD (su,57).  Get back in "data" mode (---) for Arduino to communicate with the BlueSMiRF Gold.
*/
SoftwareSerial nss(2, 3); // (RX, TX)

// **************** these are variables and constants for sending a test message from Arduino to the Android device
// the interval to send a pre-defined message from Arduino to the Droid
int const chatInterval = 30000; // after 30 seconds, send a message to the connected Android device.
int numberOfTimesToSend = 2; // the number of times to send the message (spaced out by number of chatInterval above)
int lastChatSendTime = 0; //the last time - in millis() - that we sent a message to the Droid
String chatMessage = "Hello from Arduino"; // change this to anything you want to send to the Android device
int chatSendCounter = 0; //the number of times we've sent the message
// **************** end section on sending messages from Arduino to the Android device

void setup()
{
  
  // set up the LCD's number of columns and rows: 
  lcd.begin(16, 2);

  // serial port communications from Arduino to Bluetooth Firefly module (BlueSMiRF Gold)
  // BlueSMiRF Gold defaults to 115.2k BAUD.  Change to 57.6k BAUD.
  // Don't ever use low BAUD like 4800 and 9600.  
  nss.begin(57600);
  
  //clear parallel LCD and set the cursor to the first row, first column.
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Waiting for bt..");
  //reset the timer so we can send a message to the Droid in "chatInterval" seconds from now (30 seconds unless you change it in initialization above)
  lastChatSendTime = millis(); 
  
}

void loop()
{
  //*************receiving a message from Android and printing on Arduino************************* 
  if (nss.available()) 
   {
     lcd.print(nss.read()); //get message from Android device on the Arduino NewSoftSerial serial port and print it out on the LCD
   }

   //*************sending a message from Arduino to Android *************************   
   if (((millis() - lastChatSendTime) > chatInterval) && (chatSendCounter < numberOfTimesToSend)) 
   //send a predefined message to the Android device after 'chatInterval' seconds and do this 'numberOfTimesToSend' times.
    {
      lastChatSendTime = millis();  
      chatSendCounter++; //increment chat counter
      nss.print(chatMessage); //print out the message on the NewSoftSerial serial port which then travels via Bluetooth to the Android device   
    }

}


