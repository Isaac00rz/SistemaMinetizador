#include <LiquidCrystal.h>
// En una sola linea me deja utilizar 24 caracteres antes de saltar a la siguiente.
// lcd.autoscroll() mueve a la izquierda el texto cuando ya no cabe y "noAutoscroll" lo desactiva
// Para simular o mostrar una especie de puntero para escritura que parpadea se utiliza "lcd.blink()"
// "lcd.cursor()" hace referencia con un "_" en la posicion en la que estas, puedes indicar donde quieres escribir pasandole de parametro una columna y un renglon "setCursor"
// "lcd.nodisplay()" esconde los mensajes y "lcd.dispplay()" los vuelve a mostrar
// "scrollDisplayRight()" mueve el texto una vez a la derecha y "scrollDisplayLeft()" una vez a la izquierda
// Para elegir la direccion en la que quieres escribir "lcd.rightToLeft()" de derecha a izquierda y "lcd.leftToRight()" de izquierda a derecha
// Para posicionar el cursos en (0,0) lcd.home()
#define RS 3
#define E 5
#define D4 6
#define D5 9
#define D6 10
#define D7 11

LiquidCrystal lcd(RS,E,D4,D5,D6,D7);
void setup() {
lcd.begin(16,2);
Serial.begin(9600);
lcd.cursor();
}

void loop() {
if(Serial.available()){
  delay(100);
  lcd.clear(); 
  while(Serial.available()){
    lcd.setCursor(7,0);
    lcd.write(Serial.read());
  }
}
}
