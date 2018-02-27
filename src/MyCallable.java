import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MyCallable implements Callable<Integer>{

	int startP;
	int stopP;
	Column column; 
	ArrayList<Integer> selected;
	int type;		//1 - min; 2-max; 3- sum;
	
	
	public MyCallable(Column column, int startP, int stopP, ArrayList<Integer> selected, int type){
			this.column = column;
			this.startP = startP;
			this.stopP = stopP;
			this.selected = selected;
			this.type = type;
	}
	@Override
	public Integer call() {
		// TODO Auto-generated method stub
		int result = 0;
		try {
			if (type == 1)
				return result = column.getInfoMin(selected, startP, stopP);
			if (type == 2)
				return result = column.getInfoMax(selected, startP, stopP);
			if (type == 3)
				return result = column.getInfoSum(selected, startP, stopP);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void end(){
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
