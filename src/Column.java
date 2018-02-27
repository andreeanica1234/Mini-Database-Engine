import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Column {
	private String name;
	private String type;
	private ArrayList<Object> info = new ArrayList<Object>();
	
	public Column(String name, String type){
		this.name = name;
		this.type = type;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getType(){
		return this.type;
	}
	
	public ArrayList<Object> getInfo(){
		return this.info;
	}

	public void addElement(Object object) {
		// TODO Auto-generated method stub
		info.add(object);
	}

	public void update(int position, Object value) {
		// TODO Auto-generated method stub
		info.set(position, value);
	}

	public ArrayList<Object> selectedInfo(ArrayList<Integer> positions){
		ArrayList<Object> selectedInfo = new ArrayList<Object>();
		int k = 0;
		
		for (int i=0; i<info.size(); i++){
			if (k < positions.size()){
				if (i == positions.get(k)){
					selectedInfo.add(info.get(i));
					k++;
				}
			}
			else
				break;
		}
		
		return selectedInfo;
	}

	public ArrayList<Integer> getLinesToModify(String condition) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<Integer> linesToModify = new ArrayList<Integer>();
		Iterator<Object> it = info.iterator();
		int k = 0;
		StringTokenizer str = new StringTokenizer(condition);
		String comparator = str.nextToken();
		if ((comparator.equals("<") || comparator.equals(">") ) && (this.type.equals("string") || this.type.equals("bool"))){
			throw new Exception("Wrong columnType in update");
		}
		else{
			String value = str.nextToken();
			if (this.type.equals("string") || this.type.equals("bool")){
				while(it.hasNext()){
					if (it.next().equals(value)){
						linesToModify.add(k);
					}
					k++;
				}
			}
			else{
				if (this.type == "int"){
					int valueInt = Integer.parseInt(value);
					if (comparator.equals("==") && type.equals("int")){
						while(it.hasNext()){
							if ( (int)it.next() == valueInt){
								linesToModify.add(k);
							}
							k++;
						}
					}
					else{
						if (comparator.equals("<")  && type.equals("int") ){
							while (it.hasNext()){
								if ( (int)it.next() < valueInt){
									linesToModify.add(k);
								}
								k++;
							}
						}
						else{
							if (comparator.equals(">")  && type.equals("int")){
								while(it.hasNext()){
									if ( (int)it.next() > valueInt){
										linesToModify.add(k);
									}
									k++;
								}
							}
						}
					}
				}
			}
		}
		return linesToModify;
	}

	public int getInfoMin(ArrayList<Integer> selectedLines, int startP, int stopP) throws Exception {
		// TODO Auto-generated method stub
		if (!type.equals("int")){
			throw new Exception("Max function called on a String/Bool column");
		}
		int min = (int) info.get(selectedLines.get(startP));
		for (int i=startP; i<stopP; i++){
			if (min > (int)info.get(i)){
				min = (int)info.get(i);
			}
		}
		return min;
	}

	public int getInfoMax(ArrayList<Integer> selectedLines, int startP, int stopP) throws Exception {
		// TODO Auto-generated method stub
		if (!type.equals("int")){
			throw new Exception("Max function called on a String/Bool column");
		}
		int max = (int) info.get(selectedLines.get(startP));
		for (int i=startP; i<stopP; i++){
			if (max < (int)info.get(i)){
				max = (int)info.get(i);
			}
		}
		return max;
	}

	public int getInfoSum(ArrayList<Integer> selectedLines, int startP, int stopP) throws Exception {
		// TODO Auto-generated method stub
	
		if (!type.equals("int")){
			throw new Exception("Sum function called on a String/Bool column");
		}
		int sum = 0;
		for (int i=startP; i<stopP; i++){
			sum += (int)info.get(i);
		}
		
		return sum;
	}

	public int getInfoAvg(ArrayList<Integer> selectedLines, int startP, int stopP) throws Exception {
		// TODO Auto-generated method stub
		if (!type.equals("int")){
			throw new Exception("Avg function called on a String/Bool column");
		}
		int avg = 0;
		avg = (int)Math.round(getInfoSum(selectedLines, startP, stopP) / selectedLines.size());
		return avg;
	}
	
}
