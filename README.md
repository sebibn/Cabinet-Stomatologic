# 💻 Cerințe
Dezvoltați o implementare Java bazată pe principiile arhitecturii stratificate (eng. _layered architecture_) pentru una din problemele enunțate mai jos. Cerințele de îndeplinit pentru laboratorul următor (această temă se va întinde pe durata întregului semestru) sunt:

## Cerințe non-funcționale
- Stratificarea aplicației va include **domeniul**, **repository**-ul, **serviciile**, și **interfața cu utilizatorul**. 
-  Definiți operațiile din **repository** folosind o interfață sau clasă abstractă. Implementarea de repository trebuie să fie generică.
-  Instanțiați câte un repository generic pentru fiecare entitate definită în cadrul programului (ex. `Repository<Masina>`, `Repository<Inchiriere>`). Adăugați cel puțin câte 5 instanțe în fiecare repository direct din codul sursă.
-  Toate entitățile din domeniul problemei trebuie să fie unic identificabile. Toate entitățile vor fi derivate din aceeași clasă abstractă sau interfață, fiecare obiect având un `ID` unic.

## Cerințe pentru următorul laborator
- Interfața cu utilizatorul va fi implementată sub forma unei aplicații în consolă cu meniu de utilizare.
- Interfața cu utilizatorul va permite efectuarea de operații CRUD (creare, citire, actualizare, ștergere - eng. create, read, update, delete) pentru cel puțin una din entitățile din domeniu (ex. `Mașină`).
-  Adăugați validări de bază (ex. obiectele din repository trebuie să aibă `ID` unic, un obiect nu a fost găsit în repository, validări legate de eventuale suprapuneri de date la programări și închirieri etc.) utilizând mecanismul de excepții din Java. Excepțiile aruncate vor fi prinse în interfața cu utilizatorul, unde se va afișa un mesaj corespunzător.

## Enunțurile problemelor
1. Implementați o aplicație Java pentru administrarea programărilor la un cabinet stomatologic. Entitățile din domeniul problemei sunt `Pacient` (**ID**, `nume`, `prenume`, `vârstă`) și `Programare` (**ID**, `pacient` : Pacient, `data`, `ora`, `scopul programării`). Fiecare programare are asociată exact un pacient, dar un pacient poate avea mai multe programări. Fiecare programare durează 60 minute.
2. Implementați o aplicație Java pentru administrarea comenzilor de torturi de la o cofetărie. Entitățile din domeniul problemei sunt `Comandă` (**ID**, `tort` : Listă, `data`) și `Tort` (**ID**, `tipul tortului`). Fiecare comandă are asociată cel puțin un tort. 
3. Implementați o aplicație Java pentru administrarea închirierilor de mașini. Entitățile din domeniul problemei sunt `Mașină` (**ID**, `marcă`, `model`) și `Închiriere` (**ID**, `mașina` : Mașină, `data început`, `data sfârșit`). Fiecare închiriere are asociată exact o mașină. O mașină poate avea înregistrate mai multe închirieri, dar acestea nu au voie să se suprapună ca timp.
