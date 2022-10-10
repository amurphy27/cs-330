package assignment1;
public class Tuple {
	public int row;
	public String ticker;
	public String date;
	public double openPrice;
	public double highPrice;
	public double lowPrice;
	public double closePrice;
	public int numShares;
	public double adjustedClosePrice;
	public double crazyNum;
	public Tuple(int row, String ticker, String date, double openPrice, double highPrice, double lowPrice, double closePrice, int numShares, double adjustedClosePrice) {
		this.row = row;
		this.ticker = ticker;
		this.date = date;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.closePrice = closePrice;
		this.numShares = numShares;
		this.adjustedClosePrice = adjustedClosePrice;
		this.crazyNum = ((highPrice-lowPrice)/highPrice)*100;
	}
	public void print() {
		System.out.println(ticker+"   "+date+"   "+openPrice+"   "+highPrice+"   "+lowPrice+"   "+closePrice+"   "+numShares+"   "+adjustedClosePrice);
	}
}