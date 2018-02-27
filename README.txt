NICA ANDREEA-MARIA
334CC
	
	Am implementat scriitori/ cititori folosind 2 semafoare pe clasa tabela, astfel ca, pe o tabela, toate operatiile sa se faca pe rand (exceptand selectul). Atata timp cat se
citeste din tabela, nu se mai scrie.
	Am creat clasele Tabel si Column (am reprezentat un tabel ca o lista de coloane).
	Functia select (secvential) mai intai cauta liniile care indeplinesc condition si le retine pozitiile intr-un array de pozitii. Apoi, pentru fiecare operatie din operations
aplica operatia doar pe liniile de pe pozitile retinute deja. In clasa Column, exista metoda pentru fiecare coloana, de min, max, sum, avg. Pentru cazul count sau cand 
la operations este doar numele tabelei, se foloseste de lista formata cand s-au cautat liniile care indeplinesc conditia.

	Exista 2 situatii: (in ambele, primele 2 teste din main functioneaza)
	
	1. Selectul nu este paralelizat. (partea cu ExecutorService este complet comentata); Programul se comporta in felul urmator: 
	
		Primele doua teste din main merg corect (sanityCheck si testConsistency), desi, pentru cel de-al doilea test (testConsistency), local, pentru valoarea implicita data in
ConsistencyReaderThread, merge foarte incet (nu am asteptat niciodata sa se termine pe local), dar rulat pe fep programul se incheie in 2-3 min. Local, am testat cu valori de 10 000
sau 1 000 si programul termina in 4-5 min pentru 10 000 sau  ~1 min pentru 1000 (de fiecare data, toate cele 3 teste trec).
	Pe fep:  In acest caz, insertul si update ul scaleaza, dar selectul nu. Toate testele trec.
	Local(unde valorile din Consistency si Scalability sunt mai mici, 10.000 / 100.000): Toate 3 scaleaza in majoritatea rularilor. (Insertul mereu). Selectul uneori are valori foarte
mari pe 4threaduri comparativ cu 1th si 2th (dupa multe rulari), desi de la 1 la 2 threaduri scaleaza (banuiesc ca o cauza ar putea fi garbage collectorul).
		
	2. Selectul este paralelizat folosind ExecutorService.
	
		Am utilizat obiecte Future pentru ExecutorService, pentru a putea obtine rezultatele selectului. Ideea este ca in momentul in care una din operatii este pentru int, se cauta min/max/sum (avg foloseste sum)
pe bucati care sunt calculate folosind numarul de threaduri curente. 

		Primele 2 teste merg corect. Am testat ConsistencyReader cu valoarea 1000(10 000 dureaza prea mult) si testele trec.
		In cazul celui de-al treilea test:
			- local (1000 in ConsistencyReader, 100_000 in Scalability, pentru valori mai mari dureaza foarte mult): insert si update scaleaza, select scaleaza de la 1 thread la 2 si ramane constant de la 2th la 4th (sau creste cu o valoare);
oricum, variatile sunt mult mai mici comparativ cu primul caz
			
				[[0, 1, 2, 20, 4, 5, 6, 7, 8, 9], [Ion0, Ion1, Ion2, Ion20, Ion4, Ion5, Ion6, Ion7, Ion8, Ion9], [0, 1, 2, 20, 4, 5, 6, 7, 8, 9], [false, true, false, false, false, true, false, true, false, true], [0, 1, 2, 20, 4, 5, 6, 7, 8, 9]]
				[[62], [0], [20], [6], [10], [0]]
				Select/Insert Consistency PASS
				Select/Update Consistency PASS
				Transactions Consistency PASS
				There are now 1 Threads
				Insert time 165
				[[-1475036480]]
				Update time 5
				Select time 17
				There are now 2 Threads
				[[-1475036480]]
				Insert time 69
				Update time 1
				Select time 14
				There are now 4 Threads
				Insert time 39
				[[-1475036480]]
				Update time 1
				Select time 15
				
				[[0, 1, 2, 20, 4, 5, 6, 7, 8, 9], [Ion0, Ion1, Ion2, Ion20, Ion4, Ion5, Ion6, Ion7, Ion8, Ion9], [0, 1, 2, 20, 4, 5, 6, 7, 8, 9], [false, true, false, false, false, true, false, true, false, true], [0, 1, 2, 20, 4, 5, 6, 7, 8, 9]]
				[[62], [0], [20], [6], [10], [0]]
				Select/Insert Consistency PASS
				Select/Update Consistency PASS
				Transactions Consistency PASS
				There are now 1 Threads
				Insert time 119
				[[-1475036480]]
				Update time 8
				Select time 146
				There are now 2 Threads
				[[-1475036480]]
				Insert time 65
				Update time 1
				Select time 16
				There are now 4 Threads
				Insert time 38
				Update time 1
				Select time 11
				[[-1475036480]]

				
			-pe fep (1000 in ConsistencyReader, 100_000 in Scalability):
			
			
				[[0, 1, 2, 20, 4, 5, 6, 7, 8, 9], [Ion0, Ion1, Ion2, Ion20, Ion4, Ion5, Ion6, Ion7, Ion8, Ion9], [0, 1, 2, 20, 4, 5, 6, 7, 8, 9], [false, true, false, false, false, true, false, true, false, true], [0, 1, 2, 20, 4, 5, 6, 7, 8, 9]]
				[[62], [0], [20], [6], [10], [0]]
				Select/Insert Consistency PASS
				Select/Update Consistency PASS
				Transactions Consistency PASS
				There are now 1 Threads
				[[-1475036480]]
				Insert time 206
				Update time 10
				Select time 29
				There are now 2 Threads
				[[-1475036480]]
				Insert time 142
				Update time 1
				Select time 22
				There are now 4 Threads
				[[-1475036480]]
				Insert time 111
				Update time 0
				Select time 19
				
				[[0, 1, 2, 20, 4, 5, 6, 7, 8, 9], [Ion0, Ion1, Ion2, Ion20, Ion4, Ion5, Ion6, Ion7, Ion8, Ion9], [0, 1, 2, 20, 4, 5, 6, 7, 8, 9], [false, true, false, false, false, true, false, true, false, true], [0, 1, 2, 20, 4, 5, 6, 7, 8, 9]]
				[[62], [0], [20], [6], [10], [0]]
				Select/Insert Consistency PASS
				Select/Update Consistency PASS
				Transactions Consistency PASS
				There are now 1 Threads
				[[-1475036480]]
				Insert time 180
				Update time 10
				Select time 30
				There are now 2 Threads
				[[-1475036480]]
				Insert time 130
				Update time 1
				Select time 19
				There are now 4 Threads
				[[-1475036480]]
				Insert time 127
				Update time 1
				Select time 19
				
				
				!!PROBLEMA: in acest caz, in care folosesc si cititori scriitori si Executor Service, programul nu se incheie, ramane agatat. Banuiala mea a fost ca ramane un
			thread care nu se inchide pe undeva (desi cand se apeleaza stopDb, inchid toate executorul de pe fiecare tabela, daca a fost folosit), deoarece in cazul in care comentez ExecutorService,
			programul se termina fara probleme. Asadar, in acecst caz, sunt afisate rezultatele, dar programul inca ruleaza si trebuie oprit.
				De asemenea, am observat ca dupa ce rulez de mai multe ori, rezultatele devin foarte diferite de cele initiale (primele 1, 2, 3 rulari) in cazul scalarii timpii devenind 
			din ce in ce mai haotici(primele 2 teste trec, in orice caz), fapt care banuiesc caare legatura cu garbage collectorul.