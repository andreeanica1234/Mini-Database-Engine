import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

public class Table {
	private String name;
	private ArrayList<Column> columns = new ArrayList<Column>();
	Semaphore cititor = new Semaphore(1);
	Semaphore scriitor = new Semaphore(1);
	int citire = 0;
	ExecutorService tpe;
	
	
	public Table(String name, String[] columnNames, String[] columnTypes){
		this.name = name;
		for (int i=0; i<columnNames.length; i++){
			columns.add(new Column(columnNames[i], columnTypes[i]));
		}
	}
	
	public String getName(){
		return this.name;
	}
	
	public ArrayList<Column> getColumns(){
		return this.columns;
	}
	
	public Column getColumnByName(String columnName){
		Iterator it = columns.iterator();
		Column k;
		while (it.hasNext()){
			k = (Column) it.next();
			if (k.getName().equals(columnName)){
				return k;
			}
		}
		return null;
	}
	
	public void addLine(ArrayList<Object> values){
		try {
			scriitor.acquire();
			Iterator it = columns.iterator();
			int k=0;
			while(it.hasNext()){
				((Column) it.next()).addElement(values.get(k));
				k++;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scriitor.release();
		
	}

	public void update(ArrayList<Object> values, String condition) throws Exception {
		// TODO Auto-generated method stub
		scriitor.acquire();
		
		StringTokenizer str = new StringTokenizer(condition);
		ArrayList<Integer> linesToModify = new ArrayList<Integer>();
		String token = str.nextToken(); //nume coloana
		
		//gasesc liniile care trebuie modificate
		for(int i=0; i<columns.size(); i++){
			if (columns.get(i).getName().equals(token)){
				linesToModify.addAll(columns.get(i).getLinesToModify(str.nextToken()+" "+str.nextToken()));
			}
		}
	
		//ma duc in fiecare coloana si modific pe linia corespunzatoare valoare
		for (int i=0; i<linesToModify.size(); i++){
			for (int j=0; j<columns.size(); j++){
				columns.get(j).update(linesToModify.get(i), values.get(j));
			}
		}

		scriitor.release();
	}

	public ArrayList<ArrayList<Object>> select(String[] operations, String condition, int noThreads) throws Exception {
		// TODO Auto-generated method stub
		
		//CITITOR SCRIITOR: 
		cititor.acquire();
		citire++;
		if (citire == 1){
			scriitor.acquire(); //nu se poate scrie in timp ce se citeste
		}
		cititor.release();
		
		tpe = Executors.newFixedThreadPool(noThreads);
		
		int sizePart, restPart;
		int totalSize = this.columns.get(0).getInfo().size();
		int resultInt;
		String resultString;
		Boolean resultBool;
		
		sizePart = totalSize / noThreads;
		restPart = totalSize % noThreads;
		
		ArrayList<Integer> selectedLines = new ArrayList<Integer>(); 
		ArrayList<ArrayList<Object>> selected = new ArrayList<>();
		ArrayList<Future> futures = new ArrayList<Future>();
		
		Iterator it = columns.iterator();
		
		//caut liniile care indeplinesc conditia
		if (condition.length()>0){
			StringTokenizer str = new StringTokenizer(condition, " ");
			String token = str.nextToken(); 	//nume coloana
			while(it.hasNext()){
				Column urm = (Column) it.next();
				if (urm.getName().equals(token)){
					selectedLines.addAll(urm.getLinesToModify(str.nextToken()+" "+str.nextToken()));
				}
			}
		}
		else{
			for(int i=0; i<columns.size(); i++){
				selectedLines.add(i);
			}
		}
		
		for (int i=0; i< operations.length; i++){
			ArrayList<Object> sel = new ArrayList<Object>();
			if (!operations[i].contains("(")){	//=> e numele coloanei care trebuie extrasa
				Iterator itCol = columns.iterator();
				Column x;
				while (itCol.hasNext()){
					x = (Column) itCol.next();
					if (x.getName().equals(operations[i])){
						sel.addAll(x.selectedInfo(selectedLines));
					}
				}
			}
			else{
				StringTokenizer strCond = new StringTokenizer(operations[i], "()");
				String operation = strCond.nextToken();
				if (operation.equals("count")){
					sel.add(selectedLines.size());
				}
				else{
					if (operation.equals("min")){
						String colName = strCond.nextToken();
						for (int j=0; j<columns.size(); j++){
							if (columns.get(j).getName().equals(colName)){
								//Selectare min folosind executor service: 
								int startP = 0, stopP = startP+restPart+sizePart;
								futures.clear();
								for (int k = 0; k < noThreads; k++){
									Future<Integer> x = tpe.submit(new MyCallable(columns.get(j), startP, stopP, selectedLines, 1));
									futures.add(x);
									startP = stopP;
									stopP = stopP + sizePart;
								}
								Boolean ok = true;
								while (ok){
									for (int k = 0; k < futures.size(); k++ ){
										if (!futures.get(k).isDone()){
											ok = true;
											break;
										}
										ok = false;
									}
								}
								int min = (int) futures.get(0).get();
								// caut minimul dintre valorile intoarse de fiecare thread
								for (int k = 1; k < futures.size(); k++ ){
									if (min > (int)futures.get(k).get())
										min = (int) futures.get(k).get();
								}
								sel.add(min);
								//Comanda pentru rularea selectului fara executorservice: 
							//	sel.add(columns.get(j).getInfoMin(selectedLines, 0, columns.get(j).getInfo().size()));
							}
						}
					}
					else{
						if (operation.equals("max")){
							String colName = strCond.nextToken();
							for (int j=0; j<columns.size(); j++){
								if (columns.get(j).getName().equals(colName)){
									//Selectare max folosind executor service: 
								  int startP = 0, stopP = startP+restPart+sizePart;
									futures.clear();
									for (int k = 0; k < noThreads; k++){
										Future<Integer> x = tpe.submit(new MyCallable(columns.get(j), startP, stopP, selectedLines, 2));
										futures.add(x);
										startP = stopP;
										stopP = stopP + sizePart;
									}
									Boolean ok = true;
									while (ok){
										for (int k = 0; k < futures.size(); k++ ){
											if (!futures.get(k).isDone()){
												ok = true;
												break;
											}
											ok = false;
										}
									}
									int max = (int) futures.get(0).get();
									// caut maximul dintre valorile intoarse de fiecare thread
									for (int k = 0; k < futures.size(); k++ ){
										if (max < (int)futures.get(k).get())
											max = (int) futures.get(k).get();
									}
									sel.add(max);
									//Comanda pentru rularea selectului fara executorservice: 
							//		sel.add(columns.get(j).getInfoMax(selectedLines, 0, columns.get(j).getInfo().size()));
								}
							}
						}
						else{
							if (operation.equals("sum")){
								String colName = strCond.nextToken();
								Iterator itCol = columns.iterator();
								for (int j=0; j<columns.size(); j++){
									if (columns.get(j).getName().equals(colName)){
										//Selectare sum folosind executor service: 
									  int startP = 0, stopP = startP+restPart+sizePart;
										futures.clear();
										for (int k = 0; k < noThreads; k++){
											Future<Integer> x = tpe.submit(new MyCallable(columns.get(j), startP, stopP, selectedLines, 3));
											futures.add(x);
											startP = stopP;
											stopP = stopP + sizePart;
										}
										Boolean ok = true;
										while (ok){
											for (int k = 0; k < futures.size(); k++ ){
												if (!futures.get(k).isDone()){
													ok = true;
													break;
												}
												ok = false;
											}
										}
										//calcularea sumei totale prin insumarea rezultatelor date de fiecare thread
										int sum = 0;
										for (int k = 0; k < futures.size(); k++ ){
											sum += (int) futures.get(k).get();
										}
										sel.add(sum); 
										//Comanda pentru rularea selectului fara executorservice: 
								//		sel.add(columns.get(j).getInfoSum(selectedLines, 0, columns.get(j).getInfo().size()));
									}
								}
							}
							else{
								if (operation.equals("avg")){
									String colName = strCond.nextToken();
									for (int j=0; j<columns.size(); j++){
										if (columns.get(j).getName().equals(colName)){
											sel.add(columns.get(j).getInfoAvg(selectedLines, 0, columns.get(j).getInfo().size()));
										}
									}
								}
							}
						}
					}
				}
			}
			selected.add(sel);
		}
		cititor.acquire();
		citire--;
		if (citire == 0){
			scriitor.release();
		}
		cititor.release();
		return selected;
	}

	public void shutdown() {
		// TODO Auto-generated method stub
		try{
			tpe.shutdown();
		}catch(Exception e)
		{ }
	}


}
