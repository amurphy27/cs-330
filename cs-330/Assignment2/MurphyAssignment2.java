package assignment2;

import java.util.*;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;

class Assign2Skeleton {
	
	private static DecimalFormat df = new DecimalFormat("0.00");

	static class StockData {	   
		// Creates a class which should contain the information  (date, open price, high price, low price, close price) for a particular ticker
		public String date;
		public double openPrice;
		public double highPrice;
		public double lowPrice;
		public double closePrice;
		public StockData(String date, double openPrice, double highPrice, double lowPrice, double closePrice) {
			this.date = date;
			this.openPrice = openPrice;
			this.highPrice = highPrice;
			this.lowPrice = lowPrice;
			this.closePrice = closePrice;
		}
	}

	static Connection conn;
	static final String prompt = "Enter ticker symbol [start/end dates]: ";

	public static void main(String[] args) throws Exception {
		String paramsFile = "readerparams.txt";
		if (args.length >= 1) {
			paramsFile = args[0];
		}

		Properties connectprops = new Properties();
		connectprops.load(new FileInputStream(paramsFile));
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String dburl = connectprops.getProperty("dburl");
			String username = connectprops.getProperty("user");
			conn = DriverManager.getConnection(dburl, connectprops);
			System.out.printf("Database connection %s %s established.%n", dburl, username);

			Scanner in = new Scanner(System.in);
			System.out.print(prompt);
			String input = in.nextLine().trim();

			while (input.length() > 0) {
				String[] params = input.split("\\s+");
				String ticker = params[0];
				String startdate = null, enddate = null;
				if (getName(ticker)) {
					if (params.length >= 3) {
						startdate = params[1];
						enddate = params[2];
					}               
					Deque<StockData> data = getStockData(ticker, startdate, enddate);
					System.out.println();
					System.out.println("Executing investment strategy");
					doStrategy(ticker, data);
				} 

				System.out.println();
				System.out.print(prompt);
				input = in.nextLine().trim();
			}
			System.out.print("Database connection closed.");
			in.close();
			// Close the database connection

		} catch (SQLException ex) {
			System.out.printf("SQLException: %s%nSQLState: %s%nVendorError: %s%n",
					ex.getMessage(), ex.getSQLState(), ex.getErrorCode());
		}
	}

	static boolean getName(String ticker) throws SQLException {
		// Execute the first query and print the company name of the ticker user provided (e.g., INTC to Intel Corp.) 
		// Using a prepared statement
		PreparedStatement pstmt = conn.prepareStatement(
				"select Name"+
						"	from company"+
				"	where Ticker = ?");
		pstmt.setString(1, ticker);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			System.out.println(rs.getString("Name"));
			return true;
		}
		System.out.println(ticker+" not found in database.");
		return false;
	}

	static Deque<StockData> getStockData(String ticker, String start, String end) throws SQLException {
		// Execute the second query which will return stock information of the ticker (descending on the transaction date)
		// using a prepared statement
		ResultSet rs;
		PreparedStatement pstmt;
		if (start == null) {
			pstmt = conn.prepareStatement(
					"select TransDate, OpenPrice, HighPrice, LowPrice, ClosePrice"+
							"	from company natural join pricevolume"+
					"	where Ticker = ? order by TransDate DESC");
			pstmt.setString(1, ticker);
			rs = pstmt.executeQuery();
		}
		else {
			pstmt = conn.prepareStatement(
					"select TransDate, OpenPrice, HighPrice, LowPrice, ClosePrice"+
							"	from company natural join pricevolume"+
					"	where Ticker = ? and ? <= TransDate and ? >= TransDate order by TransDate DESC");
			pstmt.setString(1, ticker);
			pstmt.setString(2, start);
			pstmt.setString(3, end);
			rs = pstmt.executeQuery();
		}

		Deque<StockData> result = new ArrayDeque<>();

		// Loop through all the dates of that company (descending order)
		// Find split if there is any (2:1, 3:1, 3:2) and add to the total count of splits. Then adjust the split accordingly
		// using a running divider that changes each split. Include the adjusted data to result (which is a Deque)
		if (rs.next()) {
			result.addFirst(new StockData(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5)));
		}
		double divider = 1;
		int splits = 0;
		while (rs.next()) {
			double splitVal = (rs.getDouble(5)/divider) / result.peek().openPrice;
			if (Math.abs(splitVal - 2.0) < 0.20) {
				System.out.println("2:1 split on "+rs.getString(1)+" "+rs.getDouble(5)+" --> "+result.peek().openPrice*divider);
				divider *= 2;
				splits++;
			}
			else if(Math.abs(splitVal - 3.0) < 0.30) {
				System.out.println("3:1 split on "+rs.getString(1)+" "+rs.getDouble(5)+" --> "+result.peek().openPrice*divider);
				divider *= 3;
				splits++;
			}
			else if(Math.abs(splitVal - 1.5) < 0.15) {
				System.out.println("3:2 split on "+rs.getString(1)+" "+rs.getDouble(5)+" --> "+result.peek().openPrice*divider);
				divider *= 1.5;
				splits++;
			}
			result.addFirst(new StockData(rs.getString(1), rs.getDouble(2)/divider, rs.getDouble(3)/divider, rs.getDouble(4)/divider, rs.getDouble(5)/divider));
		}
		System.out.println(splits+" splits in "+result.size()+" trading days");

		pstmt.close();
		return result;
	}

	static void doStrategy(String ticker, Deque<StockData> data) {
		//To Do: 
		// Apply Steps 2.6 to 2.10 explained in the assignment description 
		// data (which is a Deque) has all the information (after the split adjustment) you need to apply these steps
		if (data.size() <= 50) {
			System.out.println("Transactions executed: 0");
			System.out.println("Net Cash: 0");
		}
		else {
			int shares = 0;
			double cash = 0;
			int transactions = 0;
			double runningAvg = 0;
			int day = 0;
			Deque<StockData> last50 = new ArrayDeque<>();
			/*structure
			 * condition(){
			 * 		today
			 * 		update stuff
			 * 		next day
			 * }
			 */
			//first 50 days to fill last50 deque
			while(day <= 49) {
				last50.addFirst(data.removeFirst());
				runningAvg += last50.getFirst().closePrice/50;
				day++;
			}
			//everything past the first 50 days
			while(data.size() >= 2) {
				//buy
				if((data.getFirst().closePrice < runningAvg) && ((data.getFirst().closePrice/data.getFirst().openPrice) < 0.97000001)) {
					runningAvg -= last50.removeLast().closePrice/50;
					last50.addFirst(data.removeFirst());
					runningAvg += last50.getFirst().closePrice/50;
					shares += 100;
					cash -= (100*data.getFirst().openPrice)+8.0;
					transactions++;
				}
				//sell
				else if((shares >= 100) && (data.getFirst().openPrice > runningAvg) && ((data.getFirst().openPrice/last50.getFirst().closePrice) > 1.00999999)) {
					shares -= 100;
					cash += 100*((data.getFirst().openPrice+data.getFirst().closePrice)/2);
					cash -= 8.0;
					transactions++;
					runningAvg -= last50.removeLast().closePrice/50;
					last50.addFirst(data.removeFirst());
					runningAvg += last50.getFirst().closePrice/50;
				}
				//no transaction
				else {
					runningAvg -= last50.removeLast().closePrice/50;
					last50.addFirst(data.removeFirst());
					runningAvg += last50.getFirst().closePrice/50;
				}
				day++;
			}
			cash += shares*data.getFirst().openPrice;
			System.out.println("Transactions executed: "+transactions);
			System.out.println("Net cash: "+df.format(cash));
		}
	}
}
