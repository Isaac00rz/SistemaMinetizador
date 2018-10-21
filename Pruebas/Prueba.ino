 int led = 3;
 int input = 0;
void setup() {
  // put your setup code here, to run once:
  pinMode(led,OUTPUT);
  digitalWrite(led,HIGH);
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(Serial.available()>0){
    input = Serial.read();
    if(input == '1'){
      digitalWrite(led,HIGH);
      Serial.println("Se encendio");
    }
    if(input == '0'){
    digitalWrite(led,LOW);
    Serial.println("Se apago");
    }
    
  }
}
