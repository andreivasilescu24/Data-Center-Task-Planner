Vasilescu Andrei
334 CD
Tema 2 APD

*** MyHost ***
Am creat doua cozi de tip PriorityBlockingQueue, una fiind coada principala de task-uri, iar cealalata fiind coada de task-uri preemptate. In ambele cozi se foloseste un comparator 
pe care l-am facut eu, in asa fel incat sa sorteze mai intai descrescator dupa prioritatea task-urilor, iar daca prioritatile sunt egale sa sorteze crescator dupa timpul de start
al task-ului. De asemenea, am creat niste variabile pentru task-ul care ruleaza, pentru momentul actual de timp in milisecunde, una pentru momentul de timp din task-ul care ruleaza 
la care suntem (aceaste doua variabile ma vor ajuta sa calculez cat timp a trecut din task pentru a updata timpul ramas de executie al acestuia). De asemenea, am creat o variabila
pentru workLeft ce va ajuta la algoritmul LWL si o variabila booleana volatile care arata daca a fost apelata metoda de "shutdown" a host-ului in urma careia trebuia sa oprim executia acestuia.
Descrierea implementarii metodelor:

* Metoda "run":
Aceasta metoda are in interiorul sau o bucla while care se opreste atunci cand este apelata metoda "shutdown" pe host. Pentru inceput aceasta va actualiza momentul actual de timp in host. 
Apoi, verifica intai daca exista un task care ruleaza, iar daca nu exista va extrage din coada de task-uri preemptate, daca nu este goala, primul task si va updata variabila unde retinem 
momentul de timp din rularea task-ului cu momentul actual de timp. Daca coada de task-uri preemptate este goala, se va extrage primul task din coada "main" de task-uri, daca nu este si aceasta 
goala si se va initializa in acelasi mod variabila unde retinem momentul de timp din rularea task-ului cu momentul actual de timp. Apoi, daca task-ul actual nu mai are de rulat (valoarea lui "left" 
din task este mai mica sau egala cu 0) se va apela metoda "finish" a task-ului si se va seta variabila unde se retine task-ul care ruleaza pe null. Daca task-ul inca mai are de rulat vom actualiza 
variabila "left" a task-ului prin scaderea timpului trecut din rularea acestuia din valoarea anterioara a timpului ramas si se va updata momentul de timp din task-ul care ruleaza cu valoarea variabilei 
unde retinem momentul de timp "timeMoment". Dupa aceste actualizari, se va verifica daca task-ul care ruleaza este preemptabil si primul task din coada (daca exista) are o prioritate mai mare ca acesta, 
iar daca se valideaza aceste conditii, task-ul care este executat in acel moment va fi introdus in coada de task-uri preemptate si se va extrage din coada task-ul cu prioritate mai mare, acesta fiind 
noul task care ruleaza.

* Metoda "addTask":
Metoda introduce in coada noul task.

* Metoda "getQueueSize":
Aceasta metoda returneaza cate task-uri are in coada host-ul inclusiv task-ul care ruleaza in acel moment, daca este cazul.

* Metoda "getWorkLeft":
Returneaza valoarea obtinuta din adunarea tuturor valorilor "left" a task-urilor din host, din coada de task-uri, coada de task-uri preemptate dar si a taskului ce ruleaza daca exista.

* Metoda "shutdown":
Cand este apelata aceasta metoda, va seta valoarea variabilei de shutdown pe true pentru a se putea termina rularea host-ului.


*** MyDispatcher ***
In dispatcher-ul creat am adaugat "n" in care se retine numarul de hosturi din lista primita ca parametru in constructor, "previous_node_index" care
ma ajuta in algoritmul ROUND_ROBIN pentru a retine ultimul nod caruia i s-a asignat un task si un lock pentru functia "addTask" ce contine o sectiune sincronizata 
pentru a evita concurenta intre doua task-uri ce ajung la acelasi moment de timp la dispatcher. In functia "addTask" am tratat fiecare caz de algoritm posibil ce trebuie 
utilizat in asignarea task-urilor. Task-urile sunt asignate unui host prin apelarea metodei "addTask" din host ce introduce in coada noul task.

* Cazul SIZE_INTERVAL_TASK_ASSIGNMENT:
In acest caz stim ca avem tot timpul exact trei hosturi, asa ca verific in functie de tipul task-ului primit ca parametru carui host asignez task-ul. Astfel, daca este
un task SHORT voi asigna task-ul primului host, daca este MEDIUM celui de-al doilea, iar daca este LONG celui de-al treilea.

* Cazul SHORTEST_QUEUE:
In acest caz aflu ce host are cele mai putine elemente in coada la momentul primirii noului task in dispatcher (prin apelarea metodei getQueueSize()) si ii asignez acestuia 
task-ul primit.

* Cazul ROUND_ROBIN:
In acest caz voi asigna task-ul catre host-ul cu index-ul egal cu valoare lui "previous_node_index". Aceasta variabila incepe cu valoare 0 deoarece algoritmul asigneaza 
prima data catre index-ul 0, iar apoi, dupa fiecare asignare de task, valoarea variabilei este recalculata in functie de vechea valoare a acesteia dupa formula sugerata 
in enunt "(previous_node_index + 1) % n".

* Cazul LEAST_WORK_LEFT:
In acest caz voi calcula, similar ca la SHORTEST_QUEUE, ce host are cea mai putina munca de facut si ii voi asigna acestuia noul task. Calcularea acestui minim este bazata 
pe valoarea intoarsa de functia getWorkLeft() din fiecare host pe care o impart la 1000.0 si o rotunjesc mai apoi pentru a nu exista cazuri de genul 2.9 secunde vs 2.95 secunde,
asa cum este indicat si in enunt. Astfel compararea se va face la nivel de secunde.