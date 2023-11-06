[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/WJYLe30Q)
# 💻 Cerințe
Continuați dezvoltarea aplicației de la tema precedentă. 

## Cerințe non-funcționale
- Se păstrează cerințele definite în tema **A2**.

## Cerințe pentru următorul laborator
- Implementați clase noi Repository pentru stocarea entităților din domeniul problemei. Acestea trebuie să fie derivate din implementarea generică de Repository (în memorie) creată pentru tema **A2**.
- Una din aceste clase va stoca entitățile într-un fișier text (ex. **TextFileRepository**), iar cealaltă (ex. **BinaryFileRepository**) într-un fișier binar, folosind mecanismul de serializare al obiectelor din platforma Java. Fiecare entitate din domeniul problemei va avea propria instanță de Repository.
- Programul va putea fi pornit folosind oricare din aceste implementări de Repository, iar straturile superioare ale aplicației (ex. *servicii*, *interfața cu utilizatorul*) trebuie să rămână independente de implementarea de repository utilizată.
- Decizia privind tipul de Repository utilizat, precum și locația pe disc a fișierelor de intrare (în cazul implementării ce utilizează fișiere) se va face prin intermediul unui fișier de setări (ex. *settings.properties*) care va fi citit de program prin intermediul clasei [Properties](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Properties.html). Vedeți exemplul de mai jos:

  ``Repository = binary``\
  ``Patients = “patients.bin”``\
  ``Appointments = “appointments.bin”``
  
- Interfața grafică va permite efectuarea de operații CRUD (creare, citire, actualizare, ștergere - eng. create, read, update, delete) pentru **toate** entitățile din domeniul problemei.
- Implementați teste unitare folosind [JUnit](https://www.jetbrains.com/help/idea/junit.html). Acoperirea codului cu teste (eng. *test coverage*) trebuie să fie peste 90%, cu excepția claselor din interfața cu utilizatorul.

Termenul limită pentru predarea cu notă maximă este **laboratorul din cadrul săptămânii 7 sau 8** (depinzând de orar).

## Bonus (0.1p)
Implementați un tip adițional de Repository care permite salvarea entităților în format JSON. Termenul limită pentru implementarea bonusului este același cu cel pentru predarea cu notă maximă.
## Bonus (0.1p)
Implementați un tip adițional de Repository care permite salvarea entităților în format XML. Termenul limită pentru implementarea bonusului este același cu cel pentru predarea cu notă maximă.
