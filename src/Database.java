import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Database implements MyDatabase{
	ArrayList<Table> tables = new ArrayList<Table>();
	Semaphore scriitor = new Semaphore(1);
	Semaphore cititor = new Semaphore(1);
	int citire = 0;
	ArrayList<Integer> clients = new ArrayList<Integer>();
	int noThread;
	ExecutorService tpe;

	@Override
	public void initDb(int numWorkerThreads) {
		// TODO Auto-generated method stub
		tpe = Executors.newFixedThreadPool(numWorkerThreads);
		this.noThread = numWorkerThreads;
	}

	@Override
	public void stopDb() {
		// TODO Auto-generated method stub
		for (int i = 0; i< tables.size(); i++){
			tables.get(i).shutdown();
		}
		tpe.shutdown();
		return;
	}

	@Override
	public  void createTable(String tableName, String[] columnNames, String[] columnTypes) {
		// TODO Auto-generated method stub
		tables.add(new Table(tableName, columnNames, columnTypes));	
	}

	@Override
	public ArrayList<ArrayList<Object>> select(String tableName, String[] operations, String condition) {
		// TODO Auto-generated method stub
		
		for (int i=0; i<tables.size(); i++){
			if (tables.get(i).getName().equals(tableName)){
				try {
					return tables.get(i).select(operations, condition, noThread);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
		
	}

	@Override
	public void update(String tableName, ArrayList<Object> values, String condition) {
		// TODO Auto-generated method stub
	
		for (int i=0; i<tables.size(); i++){
			if (tables.get(i).getName().equals(tableName)){
				try {
					tables.get(i).update(values, condition);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void insert(String tableName, ArrayList<Object> values) {
		// TODO Auto-generated method stub

		for (int i=0; i<tables.size(); i++){
			if (tables.get(i).getName().equals(tableName)){
				tables.get(i).addLine(values);
			}
		}

	}

	@Override
	public void startTransaction(String tableName) {
		// TODO Auto-generated method stub

		
	}

	@Override
	public void endTransaction(String tableName) {
		// TODO Auto-generated method stub

	}

}
