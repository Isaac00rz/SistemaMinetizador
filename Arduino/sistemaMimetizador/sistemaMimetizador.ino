// Libreria que permite realizar lectura mediante el sensor DHT11
#include "DHT11.h"
/*
 <------- Libreria LiquidCrystal ---------->
Permite manipular la pantalla LCD
Incluye algunas de las siguientes funciones:
- "autoscroll()" mueve a la izquierda el texto cuando ya no cabe y "noAutoscroll" lo desactiva
- Para simular o mostrar una especie de puntero de escritura que parpadea se utiliza "blink()"
- "cursor()" hace referencia con un "_" en la posicion en la que estas, se puede indicar donde se desea
  escribir pasandole de parametro una columna y un renglon con "setCursor(x,y)"
- "nodisplay()" esconde los mensajes y "dispplay()" los vuelve a mostrar
-  "scrollDisplayRight()" mueve el texto una vez a la derecha y "scrollDisplayLeft()" una vez a la izquierda
- Para elegir la direccion en la que quieres escribir "rightToLeft()" de derecha a izquierda y "leftToRight()" de izquierda a derecha
- Para posicionar el cursos en (0,0) -> home()

*/
#include <LiquidCrystal.h>

// Pines para pantalla LCD
#define RS 3
#define E 5
#define D4 6
#define D5 9
#define D6 10
#define D7 11

// Pin para el sensor de humedad
#define pinTh 2

// Pin para el sensor de temperatura
#define pinTe A5

// Pines para Joystick
#define ejeX A4
#define ejeY A3

// Variables para imprimir mensajes
String mensaje = "";

// Variables necesarias para calcular la humedad
float te = 0;
float humedad = 0;

// Variables necesarias para calcular la luminosidad
const long A = 1000;     //Resistencia en oscuridad en KΩ 
const int B = 15;        //Resistencia a la luz (10 Lux) en KΩ 
const int Rc = 10;       //Resistencia calibracion en KΩ 
const int LDRPin = A0;   //Pin del LDR 
int V; 
int ilum;

// Variables para calcular temperatura y caracter personalizado
float voltaje=0;
float  temperatura=0;
byte celsius[8] = {
  0b11000,
  0b11011,
  0b00100,
  0b01000,
  0b01000,
  0b00100,
  0b00011,
  0b00000
};

// Variables Joystick
int axisX = 0;
int axisY = 0;
boolean lcdEmpty = true;

LiquidCrystal lcd(RS,E,D4,D5,D6,D7);
DHT11 dht11(pinTh);

void setup() {
  lcd.begin(16,2); // Se inicializa la pantalla con su medida respectiva
  lcd.createChar(0, celsius); // Se incluye el caracter personalizado  
  Serial.begin(9600);
}

void loop() {
  // Lectura de Joystick
  axisX = analogRead(ejeX);
  axisY = analogRead(ejeY);
  
  //Realiza la lectura de humedad por medio de DHT11, es capaz de medir la temperatura pero es impreciso
  dht11.read(humedad, te);

  //Se realiza el calculo de la temperatura por medio de LM35
  voltaje=(analogRead(pinTe)*3.3)/1023;
  temperatura=voltaje*100;
  
  // Se realiza el calculo para la luminosidad con el foto resistor
  V = analogRead(LDRPin);
  ilum = ((long)V*A*10)/((long)B*Rc*(1024-V));

  // Se realiza la lectura de los mensajes e la impresion del mismo a la pantalla LCD
  if(Serial.available()){
    delay(100);
    lcdEmpty=false;
    lcd.clear();
    // Convirtiendo los decimales recibidos a alfabeto y concatenandolo en una variable String
    while(Serial.available()){
      mensaje = mensaje + decimalALetras(Serial.read());
    }
    // Se imprime el mensaje con todos los datos requeridos
    if(mensaje.equals("1")){
      lcd.clear();
      lcd.print(" T:"+(String)temperatura);
      lcd.write((byte)0);
      lcd.print(" H:"+(String)humedad+"% L:"+(String)ilum);
    }else{
      lcd.setCursor(0,0);
      lcd.println(mensaje.substring(0,16));
      lcd.setCursor(0,1);
      lcd.println(mensaje.substring(16,mensaje.length()));
    }
  }
  // Lista de condicionales que permite cambiar de mensaje o desplazar el texto en pantalla
  if(!lcdEmpty){
    if(axisX == 0){
      Serial.println("2");
      delay(500);
    }else if(axisX > 1000 ){
      Serial.println("3");
      delay(500);
    }else if(axisY == 0){
      Serial.println("0");
      delay(800);
    }else if(axisY > 1000 ){
      Serial.println("1");
      delay(800);
    }
  }
  // Se vacia variable para evitar imprimir el mismo mensaje y evitar concatenarlo con los demas mensajes
  mensaje = "";
}

// Tranduccion de decimales a letras ya que al recibir cadenas desde Java obtenemos demacilaes
char decimalALetras (int entrada){
    char salida=' ';
    switch(entrada){
      case 32: 
      salida=' '; 
      break; 
      case 33: 
      salida='!'; 
      break; 
      case 34: 
      salida='"'; 
      break; 
      case 35: 
      salida='#'; 
      break; 
      case 36: 
      salida='$'; 
      break; 
      case 37: 
      salida='%'; 
      break; 
      case 38: 
      salida='&'; 
      break; 
      case 39: 
      salida=' '; 
      break; 
      case 40: 
      salida='('; 
      break; 
      case 41: 
      salida=')'; 
      break; 
      case 42: 
      salida='*'; 
      break; 
      case 43: 
      salida='+'; 
      break; 
      case 44: 
      salida=','; 
      break; 
      case 45: 
      salida='-'; 
      break; 
      case 46: 
      salida='.'; 
      break; 
      case 47: 
      salida='/'; 
      break; 
      case 48: 
      salida='0'; 
      break; 
      case 49: 
      salida='1'; 
      break; 
      case 50: 
      salida='2'; 
      break; 
      case 51: 
      salida='3'; 
      break; 
      case 52: 
      salida='4'; 
      break; 
      case 53: 
      salida='5'; 
      break; 
      case 54: 
      salida='6'; 
      break; 
      case 55: 
      salida='7'; 
      break; 
      case 56: 
      salida='8'; 
      break; 
      case 57: 
      salida='9'; 
      break; 
      case 58: 
      salida=':'; 
      break; 
      case 59: 
      salida=';'; 
      break; 
      case 60: 
      salida='<'; 
      break; 
      case 61: 
      salida='='; 
      break; 
      case 62: 
      salida='>'; 
      break; 
      case 63: 
      salida='?'; 
      break; 
      case 64: 
      salida='@'; 
      break; 
      case 65: 
      salida='A'; 
      break; 
      case 66: 
      salida='B'; 
      break; 
      case 67: 
      salida='C'; 
      break; 
      case 68: 
      salida='D'; 
      break; 
      case 69: 
      salida='E'; 
      break; 
      case 70: 
      salida='F'; 
      break; 
      case 71: 
      salida='G'; 
      break; 
      case 72: 
      salida='H'; 
      break; 
      case 73: 
      salida='I'; 
      break; 
      case 74: 
      salida='J'; 
      break; 
      case 75: 
      salida='K'; 
      break; 
      case 76: 
      salida='L'; 
      break; 
      case 77: 
      salida='M'; 
      break; 
      case 78: 
      salida='N'; 
      break; 
      case 79: 
      salida='O'; 
      break; 
      case 80: 
      salida='P'; 
      break; 
      case 81: 
      salida='Q'; 
      break; 
      case 82: 
      salida='R'; 
      break; 
      case 83: 
      salida='S'; 
      break; 
      case 84: 
      salida='T'; 
      break; 
      case 85: 
      salida='U'; 
      break; 
      case 86: 
      salida='V'; 
      break; 
      case 87: 
      salida='W'; 
      break; 
      case 88: 
      salida='X'; 
      break; 
      case 89: 
      salida='Y'; 
      break; 
      case 90: 
      salida='Z'; 
      break; 
      case 91: 
      salida='['; 
      break; 
      case 92: 
      salida=' '; 
      break; 
      case 93: 
      salida=']'; 
      break; 
      case 94: 
      salida='^'; 
      break; 
      case 95: 
      salida='_'; 
      break; 
      case 96: 
      salida='`'; 
      break; 
      case 97: 
      salida='a'; 
      break; 
      case 98: 
      salida='b'; 
      break; 
      case 99: 
      salida='c'; 
      break; 
      case 100: 
      salida='d'; 
      break; 
      case 101: 
      salida='e'; 
      break; 
      case 102: 
      salida='f'; 
      break; 
      case 103: 
      salida='g'; 
      break; 
      case 104: 
      salida='h'; 
      break; 
      case 105: 
      salida='i'; 
      break; 
      case 106: 
      salida='j'; 
      break; 
      case 107: 
      salida='k'; 
      break; 
      case 108: 
      salida='l'; 
      break; 
      case 109: 
      salida='m'; 
      break; 
      case 110: 
      salida='n'; 
      break; 
      case 111: 
      salida='o'; 
      break; 
      case 112: 
      salida='p'; 
      break; 
      case 113: 
      salida='q'; 
      break; 
      case 114: 
      salida='r'; 
      break; 
      case 115: 
      salida='s'; 
      break; 
      case 116: 
      salida='t'; 
      break; 
      case 117: 
      salida='u'; 
      break; 
      case 118: 
      salida='v'; 
      break; 
      case 119: 
      salida='w'; 
      break; 
      case 120: 
      salida='x'; 
      break; 
      case 121: 
      salida='y'; 
      break; 
      case 122: 
      salida='z'; 
      break; 
      case 123: 
      salida='{'; 
      break; 
      case 124: 
      salida='|'; 
      break; 
      case 125: 
      salida='}'; 
      break; 
      case 126: 
      salida='~'; 
      break; 
    }
    return salida;
}
