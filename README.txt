NICA ANDREEA-MARIA

	Implementarea unui software minimalist de management al unei baze de date ce se va folosi
de tehnologia multi-threading pentru a obține timpi de execuție scazuți. Sistemul de management al bazei de date va avea suport pentru multiple tabele asupra carora se pot efectua operațiile insert, select și update.

	Operatii suportate: 
		- initDb(numWorkerThreads) - inițializeaza o baza de date
		- stopDb() - oprește serviciul de baza de date
		- createTable(tableName, columnNames, columnTypes) - creaza tabela tabelName ce va conține
coloanele columnName cu tipurile aferente columnTypes
		- select(tableName, operations, condition) - citește din tabela tableName și întoarce o listă cu atatea coloane câte elemente sunt în lista operations; fiecare coloana va conține un numar de elemente dependent de tipul operației, condiție și elementele din tabelă
		- update(tableName, values, condition) - scrie șn tabela tableName valorile values doar în locațiile în care condition este true
		- insert(tableName, values) - scrie la sfarșitul tabelei o noua linie cu valorile values
		- startTransaction(tableName) - începe o tranzacție
		- endTransaction(tableName) - finalizeaza tranzacția.
		
	Parametrii funcțiilor:
		- tableName - numele tabele asupra careia se va executa funcția  (String)
		- columnNames - numele coloanelor din tabela (String[]) 
		- columnTypes - tipurile coloanelor din tabela (String[], cu valori acceptate string, int, bool)
		- condition - o conditie (data ca String) de tipul ”columnName comparator value”Ș
			- coparatorul poate fi fi “<”, “>” sau “==”
			- value: numar/bool/String in functie de tipul coloanei
		- values - o lista de valori (ArrayList<Object>); se potrivește cu numarul și tipul coloanelor din tabelă
		- operations - variabila care reprezinta operațiile pentru select (String[])


	
	
