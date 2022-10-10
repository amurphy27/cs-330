package assignment1;
import java.io.File;
//import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
public class MurphyAssignment1 {
	private static DecimalFormat df2 = new DecimalFormat("#.00");
	public static void main(String[] args) throws IOException {
		//blah
        Scanner input = new Scanner(System.in);
        File file = new File("StockmarketInput.txt");
        input = new Scanner(file);
        Tuple[] relation = new Tuple[(int) file.length()];
        int row = 0;
        while (input.hasNext()) {
            relation[row] = new Tuple(row, input.next(), input.next(), input.nextDouble(), input.nextDouble(), input.nextDouble(), input.nextDouble(), input.nextInt(), input.nextDouble());
            row++;
        }
        File output = new File("StockmarketOutput.txt");
        output.createNewFile();
        FileWriter writer = new FileWriter("StockmarketOutput.txt");
        //next we find all the crazy days and put them into buckets based on ticker
        ArrayList<Tuple> aaplCrazy = new ArrayList<Tuple>();
        ArrayList<Tuple> fbCrazy = new ArrayList<Tuple>();
        ArrayList<Tuple> googCrazy = new ArrayList<Tuple>();
        ArrayList<Tuple> ibmCrazy = new ArrayList<Tuple>();
        ArrayList<Tuple> intcCrazy = new ArrayList<Tuple>();
        ArrayList<Tuple> msftCrazy = new ArrayList<Tuple>();
        ArrayList<Tuple> yhooCrazy = new ArrayList<Tuple>();
        row = 0;
        while (relation[row] != null) {
        	if (relation[row].crazyNum >= 15) {
        		if (relation[row].ticker.equals("AAPL")) {
        			aaplCrazy.add(relation[row]);
        		}
        		else if (relation[row].ticker.equals("FB")) {
        			fbCrazy.add(relation[row]);
        		}
        		else if (relation[row].ticker.equals("GOOG")) {
        			googCrazy.add(relation[row]);
        		}
        		else if (relation[row].ticker.equals("IBM")) {
        			ibmCrazy.add(relation[row]);
        		}
        		else if (relation[row].ticker.equals("INTC")) {
        			intcCrazy.add(relation[row]);
        		}
        		else if (relation[row].ticker.equals("MSFT")) {
        			msftCrazy.add(relation[row]);
        		}
        		else if (relation[row].ticker.equals("YHOO")) {
        			yhooCrazy.add(relation[row]);
        		}
        	}
        	row++;
        }
        //last real step is to find all the split days
        ArrayList<Tuple> aapl21 = new ArrayList<Tuple>();
        ArrayList<Tuple> aapl31 = new ArrayList<Tuple>();
        ArrayList<Tuple> aapl32 = new ArrayList<Tuple>();
        ArrayList<Tuple> fb21 = new ArrayList<Tuple>();
        ArrayList<Tuple> fb31 = new ArrayList<Tuple>();
        ArrayList<Tuple> fb32 = new ArrayList<Tuple>();
        ArrayList<Tuple> goog21 = new ArrayList<Tuple>();
        ArrayList<Tuple> goog31 = new ArrayList<Tuple>();
        ArrayList<Tuple> goog32 = new ArrayList<Tuple>();
        ArrayList<Tuple> ibm21 = new ArrayList<Tuple>();
        ArrayList<Tuple> ibm31 = new ArrayList<Tuple>();
        ArrayList<Tuple> ibm32 = new ArrayList<Tuple>();
        ArrayList<Tuple> intc21 = new ArrayList<Tuple>();
        ArrayList<Tuple> intc31 = new ArrayList<Tuple>();
        ArrayList<Tuple> intc32 = new ArrayList<Tuple>();
        ArrayList<Tuple> msft21 = new ArrayList<Tuple>();
        ArrayList<Tuple> msft31 = new ArrayList<Tuple>();
        ArrayList<Tuple> msft32 = new ArrayList<Tuple>();
        ArrayList<Tuple> yhoo21 = new ArrayList<Tuple>();
        ArrayList<Tuple> yhoo31 = new ArrayList<Tuple>();
        ArrayList<Tuple> yhoo32 = new ArrayList<Tuple>();
        row = 0;
        while (relation[row + 1] != null) {
        	if (Math.abs((relation[row+1].closePrice/relation[row].openPrice)-2.0) < 0.2) {
        		if (relation[row].ticker.equals("AAPL")) {
        			aapl21.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("FB")) {
        			fb21.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("GOOG")) {
        			goog21.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("IBM")) {
        			ibm21.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("INTC")) {
        			intc21.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("MSFT")) {
        			msft21.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("YHOO")) {
        			yhoo21.add(relation[row+1]);
        		}
        	}
        	else if (Math.abs(relation[row+1].closePrice/relation[row].openPrice-3.0) < 0.3) {
        		if (relation[row].ticker.equals("AAPL")) {
        			aapl31.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("FB")) {
        			fb31.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("GOOG")) {
        			goog31.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("IBM")) {
        			ibm31.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("INTC")) {
        			intc31.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("MSFT")) {
        			msft31.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("YHOO")) {
        			yhoo31.add(relation[row+1]);
        		}
        	}
        	else if (Math.abs(relation[row+1].closePrice/relation[row].openPrice-1.5) < 0.15) {
        		if (relation[row].ticker.equals("AAPL")) {
        			aapl32.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("FB")) {
        			fb32.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("GOOG")) {
        			goog32.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("IBM")) {
        			ibm32.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("INTC")) {
        			intc32.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("MSFT")) {
        			msft32.add(relation[row+1]);
        		}
        		else if (relation[row].ticker.equals("YHOO")) {
        			yhoo32.add(relation[row+1]);
        		}
        	}
        	row++;
        }
        //now it is just a bunch a printing
        //print out aapl stuff
        df2.setRoundingMode(RoundingMode.HALF_EVEN);
        writer.write("Processing AAPL"+"\n");
        writer.write("======================"+"\n");
        for (int j = 0; j < aaplCrazy.size(); j++) {
        	writer.write("Crazy day: "+aaplCrazy.get(j).date+"\t"+df2.format(aaplCrazy.get(j).crazyNum)+"\n");
        }
        writer.write("Total crazy days= "+aaplCrazy.size()+"\n");
        if (aaplCrazy.size() != 0) {
        	Tuple max = aaplCrazy.get(0);
        	for (int i = 1; i < aaplCrazy.size(); i++) {
        		if (max.crazyNum < aaplCrazy.get(i).crazyNum) {
        			max = aaplCrazy.get(i);
        		}
        	}
        	writer.write("The craziest day: "+max.date+"\t"+df2.format(max.crazyNum)+"\n");
        }
        writer.write("\n");
        df2.setRoundingMode(RoundingMode.DOWN);
        for (int j = 0; j < aapl21.size(); j++) {
        	writer.write("2:1 split on "+aapl21.get(j).date+"\t"+df2.format(aapl21.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(aapl21.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < aapl31.size(); j++) {
        	writer.write("2:1 split on "+aapl31.get(j).date+"\t"+df2.format(aapl31.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(aapl31.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < aapl32.size(); j++) {
        	writer.write("2:1 split on "+aapl32.get(j).date+"\t"+df2.format(aapl32.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(aapl32.get(j).row)-1].openPrice)+"\n");
        }
        writer.write("Total number of splits: "+(aapl21.size()+aapl31.size()+aapl32.size())+"\n");
        writer.write("\n");
        //print out the fb stuff
        df2.setRoundingMode(RoundingMode.HALF_EVEN);
        writer.write("Processing FB"+"\n");
        writer.write("======================"+"\n");
        for (int j = 0; j < fbCrazy.size(); j++) {
        	writer.write("Crazy day: "+fbCrazy.get(j).date+"\t"+df2.format(fbCrazy.get(j).crazyNum)+"\n");
        }
        writer.write("Total crazy days= "+fbCrazy.size()+"\n");
        if (fbCrazy.size() != 0) {
        	Tuple max = fbCrazy.get(0);
        	for (int i = 1; i < fbCrazy.size(); i++) {
        		if (max.crazyNum < fbCrazy.get(i).crazyNum) {
        			max = fbCrazy.get(i);
        		}
        	}
        	writer.write("The craziest day: "+max.date+"\t"+df2.format(max.crazyNum)+"\n");
        }
        writer.write("\n");
        df2.setRoundingMode(RoundingMode.DOWN);
        for (int j = 0; j < fb21.size(); j++) {
        	writer.write("2:1 split on "+fb21.get(j).date+"\t"+df2.format(fb21.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(fb21.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < fb31.size(); j++) {
        	writer.write("2:1 split on "+fb31.get(j).date+"\t"+df2.format(fb31.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(fb31.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < fb32.size(); j++) {
        	writer.write("2:1 split on "+fb32.get(j).date+"\t"+df2.format(fb32.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(fb32.get(j).row)-1].openPrice)+"\n");
        }
        writer.write("Total number of splits: "+(fb21.size()+fb31.size()+fb32.size())+"\n");
        writer.write("\n");
        //print out the goog stuff
        df2.setRoundingMode(RoundingMode.HALF_EVEN);
        writer.write("Processing GOOG"+"\n");
        writer.write("======================"+"\n");
        for (int j = 0; j < googCrazy.size(); j++) {
        	writer.write("Crazy day: "+googCrazy.get(j).date+"\t"+df2.format(googCrazy.get(j).crazyNum)+"\n");
        }
        writer.write("Total crazy days= "+googCrazy.size()+"\n");
        if (googCrazy.size() != 0) {
        	Tuple max = googCrazy.get(0);
        	for (int i = 1; i < googCrazy.size(); i++) {
        		if (max.crazyNum < googCrazy.get(i).crazyNum) {
        			max = googCrazy.get(i);
        		}
        	}
        	writer.write("The craziest day: "+max.date+"\t"+df2.format(max.crazyNum)+"\n");
        }
        writer.write("\n");
        df2.setRoundingMode(RoundingMode.DOWN);
        for (int j = 0; j < goog21.size(); j++) {
        	writer.write("2:1 split on "+goog21.get(j).date+"\t"+df2.format(goog21.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(goog21.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < goog31.size(); j++) {
        	writer.write("2:1 split on "+goog31.get(j).date+"\t"+df2.format(goog31.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(goog31.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < goog32.size(); j++) {
        	writer.write("2:1 split on "+goog32.get(j).date+"\t"+df2.format(goog32.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(goog32.get(j).row)-1].openPrice)+"\n");
        }
        writer.write("Total number of splits: "+(goog21.size()+goog31.size()+goog32.size())+"\n");
        writer.write("\n");
        //print out the ibm stuff
        df2.setRoundingMode(RoundingMode.HALF_EVEN);
        writer.write("Processing IBM"+"\n");
        writer.write("======================"+"\n");
        for (int j = 0; j < ibmCrazy.size(); j++) {
        	writer.write("Crazy day: "+ibmCrazy.get(j).date+"\t"+df2.format(ibmCrazy.get(j).crazyNum)+"\n");
        }
        writer.write("Total crazy days= "+ibmCrazy.size()+"\n");
        if (ibmCrazy.size() != 0) {
        	Tuple max = ibmCrazy.get(0);
        	for (int i = 1; i < ibmCrazy.size(); i++) {
        		if (max.crazyNum < ibmCrazy.get(i).crazyNum) {
        			max = ibmCrazy.get(i);
        		}
        	}
        	writer.write("The craziest day: "+max.date+"\t"+df2.format(max.crazyNum)+"\n");
        }
        writer.write("\n");
        df2.setRoundingMode(RoundingMode.DOWN);
        for (int j = 0; j < ibm21.size(); j++) {
        	writer.write("2:1 split on "+ibm21.get(j).date+"\t"+df2.format(ibm21.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(ibm21.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < ibm31.size(); j++) {
        	writer.write("2:1 split on "+ibm31.get(j).date+"\t"+df2.format(ibm31.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(ibm31.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < ibm32.size(); j++) {
        	writer.write("2:1 split on "+ibm32.get(j).date+"\t"+df2.format(ibm32.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(ibm32.get(j).row)-1].openPrice)+"\n");
        }
        writer.write("Total number of splits: "+(ibm21.size()+ibm31.size()+ibm32.size())+"\n");
        writer.write("\n");
        //print out the intc stuff
        df2.setRoundingMode(RoundingMode.HALF_EVEN);
        writer.write("Processing INTC"+"\n");
        writer.write("======================"+"\n");
        for (int j = 0; j < intcCrazy.size(); j++) {
        	writer.write("Crazy day: "+intcCrazy.get(j).date+"\t"+df2.format(intcCrazy.get(j).crazyNum)+"\n");
        }
        writer.write("Total crazy days= "+intcCrazy.size()+"\n");
        if (intcCrazy.size() != 0) {
        	Tuple max = intcCrazy.get(0);
        	for (int i = 1; i < intcCrazy.size(); i++) {
        		if (max.crazyNum < intcCrazy.get(i).crazyNum) {
        			max = intcCrazy.get(i);
        		}
        	}
        	writer.write("The craziest day: "+max.date+"\t"+df2.format(max.crazyNum)+"\n");
        }
        writer.write("\n");
        df2.setRoundingMode(RoundingMode.DOWN);
        for (int j = 0; j < intc21.size(); j++) {
        	writer.write("2:1 split on "+intc21.get(j).date+"\t"+df2.format(intc21.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(intc21.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < intc31.size(); j++) {
        	writer.write("2:1 split on "+intc31.get(j).date+"\t"+df2.format(intc31.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(intc31.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < intc32.size(); j++) {
        	writer.write("2:1 split on "+intc32.get(j).date+"\t"+df2.format(intc32.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(intc32.get(j).row)-1].openPrice)+"\n");
        }
        writer.write("Total number of splits: "+(intc21.size()+intc31.size()+intc32.size())+"\n");
        writer.write("\n");
        //print out the msft stuff
        df2.setRoundingMode(RoundingMode.HALF_EVEN);
        writer.write("Processing MSFT"+"\n");
        writer.write("======================"+"\n");
        for (int j = 0; j < msftCrazy.size(); j++) {
        	writer.write("Crazy day: "+msftCrazy.get(j).date+"\t"+df2.format(msftCrazy.get(j).crazyNum)+"\n");
        }
        writer.write("Total crazy days= "+msftCrazy.size()+"\n");
        if (msftCrazy.size() != 0) {
        	Tuple max = msftCrazy.get(0);
        	for (int i = 1; i < msftCrazy.size(); i++) {
        		if (max.crazyNum < msftCrazy.get(i).crazyNum) {
        			max = msftCrazy.get(i);
        		}
        	}
        	writer.write("The craziest day: "+max.date+"\t"+df2.format(max.crazyNum)+"\n");
        }
        writer.write("\n");
        df2.setRoundingMode(RoundingMode.DOWN);
        for (int j = 0; j < msft21.size(); j++) {
        	writer.write("2:1 split on "+msft21.get(j).date+"\t"+df2.format(msft21.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(msft21.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < msft31.size(); j++) {
        	writer.write("2:1 split on "+msft31.get(j).date+"\t"+df2.format(msft31.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(msft31.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < msft32.size(); j++) {
        	writer.write("2:1 split on "+msft32.get(j).date+"\t"+df2.format(msft32.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(msft32.get(j).row)-1].openPrice)+"\n");
        }
        writer.write("Total number of splits: "+(msft21.size()+msft31.size()+msft32.size())+"\n");
        writer.write("\n");
        //print out the yhoo stuff
        df2.setRoundingMode(RoundingMode.HALF_EVEN);
        writer.write("Processing YHOO"+"\n");
        writer.write("======================"+"\n");
        for (int j = 0; j < yhooCrazy.size(); j++) {
        	writer.write("Crazy day: "+yhooCrazy.get(j).date+"\t"+df2.format(yhooCrazy.get(j).crazyNum)+"\n");
        }
        writer.write("Total crazy days= "+yhooCrazy.size()+"\n");
        if (yhooCrazy.size() != 0) {
        	Tuple max = yhooCrazy.get(0);
        	for (int i = 1; i < yhooCrazy.size(); i++) {
        		if (max.crazyNum < yhooCrazy.get(i).crazyNum) {
        			max = yhooCrazy.get(i);
        		}
        	}
        	writer.write("The craziest day: "+max.date+"\t"+df2.format(max.crazyNum)+"\n");
        }
        writer.write("\n");
        df2.setRoundingMode(RoundingMode.DOWN);
        for (int j = 0; j < yhoo21.size(); j++) {
        	writer.write("2:1 split on "+yhoo21.get(j).date+"\t"+df2.format(yhoo21.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(yhoo21.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < yhoo31.size(); j++) {
        	writer.write("2:1 split on "+yhoo31.get(j).date+"\t"+df2.format(yhoo31.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(yhoo31.get(j).row)-1].openPrice)+"\n");
        }
        for (int j = 0; j < yhoo32.size(); j++) {
        	writer.write("2:1 split on "+yhoo32.get(j).date+"\t"+df2.format(yhoo32.get(j).closePrice)+" -->"+"\t"+df2.format(relation[(yhoo32.get(j).row)-1].openPrice)+"\n");
        }
        writer.write("Total number of splits: "+(yhoo21.size()+yhoo31.size()+yhoo32.size())+"\n");
        writer.write("\n");
        writer.close();
    }
}