package assignment3;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.io.*;

class Assignment3Skeleton {

    static final String defaultReaderParams = "readerparams.txt";
    static final String defaultWriterParams = "writerparams.txt";
    static Connection readerConn = null;   
    static Connection writerConn = null;
	// To Do: Other variables
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
	    
    static final String getDatesQuery =
        "select max(startDate), min(endDate)" +
        "  from (select Ticker, min(TransDate) as StartDate, max(TransDate) as endDate," +
        "            count(distinct TransDate) as tradingDays" +
        "          from company natural join pricevolume" +
        "          where Industry = ?" +
        "          group by Ticker" +
        "          having tradingDays >= ?) as TickerDates";
    
    static final String getTickerDatesQuery = 
        "select Ticker, min(TransDate) as StartDate, max(TransDate) as endDate," +
        "      count(distinct TransDate) as tradingDays" +
        "  from company natural join pricevolume" +
        "  where Industry = ?" +
        "    and TransDate >= ? and TransDate <= ?" +
        "  group by Ticker" +
        "  having tradingDays >= ?" +
        "  order by Ticker";
        
    static final String getIndustryPriceDataQuery =
        "select Ticker, TransDate, OpenPrice, ClosePrice" +
        "  from pricevolume natural join company" +
        "  where Industry = ?" +
        "    and TransDate >= ? and TransDate <= ?" +
        "  order by TransDate, Ticker";

	static final String getAllIndustries = 
            "select distinct Industry" +
            "  from company" +
            "  order by Industry";		
        
    static final String dropPerformanceTable =
        "drop table if exists Performance;";

    static final String createPerformanceTable =
        "create table Performance (" +
        "  Industry char(30)," +
        "  Ticker char(6)," +
        "  StartDate char(10)," +
        "  EndDate char(10)," +
        "  TickerReturn char(12)," +
        "  IndustryReturn char(12)" +
        "  );";
    
    static final String insertPerformance =
        "insert into Performance(Industry, Ticker, StartDate, EndDate, TickerReturn, IndustryReturn)" +
        "  values(?, ?, ?, ?, ?, ?);";

    
    static class IndustryData {
		// Create this class which  contains the list of the tickers, the common days, start date, and end date
    	public List<String> tickerList;
    	public String startDate;
    	public String endDate;
    	public int commonDays;
    	public IndustryData(List<String> tickers, int numDays, String startDate, String endDate) {
    		tickerList = tickers;
    		commonDays = numDays;
    		this.startDate = startDate;
    		this.endDate = endDate;
    	}
    }

    public static void main(String[] args) throws Exception {
    
        // Get connection properties
        Properties readerProps = new Properties();
        readerProps.load(new FileInputStream(defaultReaderParams));
        Properties writerProps = new Properties();
        writerProps.load(new FileInputStream(defaultWriterParams));

        try {
            // Setup Reader and Writer Connection
            setupReader(readerProps);
            setupWriter(writerProps);
            
            List<String> industries = getIndustries();
            System.out.printf("%d industries found%n", industries.size());
            for (String industry : industries) {
                System.out.printf("%s%n", industry);
            }
            System.out.println();            
            PreparedStatement writer = writerConn.prepareStatement(dropPerformanceTable);
        	writer.executeUpdate();
        	writer = writerConn.prepareStatement(createPerformanceTable);
        	writer.executeUpdate();
			for (String industry : industries) {
                System.out.printf("Processing %s%n", industry);
                IndustryData iData = processIndustry(industry);
                if (iData != null && iData.tickerList.size() > 2) {
                    System.out.printf("%d accepted tickers for %s(%s - %s), %d common dates%n",
                        iData.tickerList.size(), industry, iData.startDate, iData.endDate, iData.commonDays);
                    processIndustryGains(industry, iData);
                }
                else
                    System.out.printf("Insufficient data for %s => no analysis%n", industry);
                System.out.println();
            }
            
            // Close everything you don't need any more 
			
            System.out.println("Database connections closed");
        } catch (SQLException ex) {
            System.out.printf("SQLException: %s%nSQLState: %s%nVendorError: %s%n",
                                    ex.getMessage(), ex.getSQLState(), ex.getErrorCode());
        }
    }
    
    static void setupReader(Properties connectProps) throws SQLException {
        String dburl = connectProps.getProperty("dburl");
        String username = connectProps.getProperty("user");
        readerConn = DriverManager.getConnection(dburl, connectProps);
        System.out.printf("Reader connection %s %s established.%n", dburl, username);

       /* PreparedStatement getDates = readerConn.prepareStatement(getDatesQuery);
        * PreparedStatement getTickerDates = readerConn.prepareStatement(getTickerDatesQuery);
        * PreparedStatement getIndustryPriceData = readerConn.prepareStatement(getIndustryPriceDataQuery);*/
    } 
    
    
    static void setupWriter(Properties connectProps) throws SQLException {
        String dburl = connectProps.getProperty("dburl");
        String username = connectProps.getProperty("user");
        writerConn = DriverManager.getConnection(dburl, connectProps);
        System.out.printf("Writer connection %s %s established.%n", dburl, username);

        // Create Performance Table; If this table already exists, drop it first
        Statement tstmt = writerConn.createStatement();
        tstmt.execute(dropPerformanceTable);
        tstmt.execute(createPerformanceTable);
        tstmt.close();
        
        //PreparedStatement insertPerformanceData = writerConn.prepareStatement(insertPerformance);
    } 
    
    static List<String> getIndustries() throws SQLException {
		List<String> result = new ArrayList<>();
		//Execute the appropriate query (you need one of them) and return a list of the industries
		PreparedStatement pstmt = readerConn.prepareStatement(getAllIndustries);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			result.add(rs.getString(1));
		}
		pstmt.close();
		return result;
    }
    
    static IndustryData processIndustry(String industry) throws SQLException {
		//Execute the appropriate SQL queries (you need two of them) and return an object of IndustryData
    	PreparedStatement pstmt = readerConn.prepareStatement(getDatesQuery);
    	pstmt.setString(1, industry);
    	pstmt.setInt(2, 150);
    	ResultSet rs = pstmt.executeQuery();
    	rs.next();
    	String startDate = rs.getString(1);
    	String endDate = rs.getString(2);
    	pstmt = readerConn.prepareStatement(getTickerDatesQuery);
    	pstmt.setString(1, industry);
    	pstmt.setString(2, startDate);
    	pstmt.setString(3, endDate);
    	pstmt.setInt(4, 150);
    	rs = pstmt.executeQuery();
    	List<String> tickers = new ArrayList<>();
    	int numDays = 0;
    	while(rs.next()) {
    		tickers.add(rs.getString(1));
    		numDays = rs.getInt(4);
    	}
    	pstmt.close();
        return new IndustryData(tickers, numDays, startDate, endDate);
    }
    
    static void processIndustryGains(String industry, IndustryData data) throws SQLException {
		// To Do: 
		// In this method, you should calculate the ticker return and industry return. Look at the assignment description to know how to do that 
		// Don't forget to do the split adjustment
    	PreparedStatement writer = writerConn.prepareStatement(insertPerformance);
    	int numTickers = data.tickerList.size();
    	List<List<StockData>> dataList = new ArrayList<>(numTickers);
    	for (String tickerName : data.tickerList) {
    		Deque<StockData> deque = processSplits(tickerName, data.startDate, data.endDate);
    		List<StockData> list = new ArrayList<>();
    		while (!deque.isEmpty()) {
    			list.add(deque.pollFirst());
    		}
    		dataList.add(list);
    	}
    	/* Now dataList has a list of lists of split processed stock data like the previous assignment to work with since the tickers are in alphabetical order
    	 * we know which list is for which ticker. So now we can process them.*/
    	//String startFirst = dataList.get(0).get(0).date;
    	String endFirst = dataList.get(0).get(dataList.get(0).size() - 1).date;
    	int maxIndex = 999999999;
    	for (int j = 0; j < numTickers; j++) {
    		if (maxIndex > dataList.get(j).size()) {
    			maxIndex = dataList.get(j).size();
    		}
    	}
    	for (int i = 0; i < numTickers; i++) {
    		int day = 0;
    		//now we skip to the first day of the first ticker alphabetically to make sure everyone starts on the same day or later, not before
    		/*while (dataList.get(i).get(day).date.compareTo(startFirst) < 0) {
    			day++;
    		}*/
    		while ((maxIndex >= day + 59) && (dataList.get(i).get(day + 59).date.compareTo(endFirst) <= 0)) {
    			writer.setString(1, industry);
    			writer.setString(2, data.tickerList.get(i));
    			writer.setString(3, dataList.get(i).get(day).date);
    			writer.setString(4, dataList.get(i).get(day + 59).date);
    			writer.setString(5, String.format("%10.7f", calcTickerReturn(dataList.get(i), day)));
    			writer.setString(6, String.format("%10.7f", calcIndustryReturn(dataList, i, day)));
    			writer.executeUpdate();
    			day += 60;
    		}
    	}
    	
		// After those calculations, insert the data into the Performance table you created earlier. You may use the following way to do that for each company (or ticker) of an indsutry: 
			// insertPerformanceData.setString(1, industry);
			// insertPerformanceData.setString(2, ticker);
			// insertPerformanceData.setString(3, startdate);
			// insertPerformanceData.setString(4, enddate);
			// insertPerformanceData.setString(5, String.format("%10.7f", tickerReturn);
			// insertPerformanceData.setString(6, String.format("%10.7f", industryReturn);
			// int result = insertPerformanceData.executeUpdate();
	}
    
    static double calcTickerReturn(List<StockData> data, int start) {
    	return (data.get(start + 59).closePrice/data.get(start).openPrice) - 1.0;
    }
    
    static double calcIndustryReturn(List<List<StockData>> data, int tickerIndex, int start) {
    	double runningReturn = 0;
    	for (int i = 0; i < data.size(); i++) {
    		if (tickerIndex != i) {
    			runningReturn += (data.get(i).get(start + 59).closePrice/data.get(i).get(start).openPrice);
    		}
    	}
    	return (((1.0/(data.size() - 1.0)) * runningReturn) - 1.0);
    }
    
    static Deque<StockData> processSplits(String ticker, String start, String end) throws SQLException {
		PreparedStatement pstmt = readerConn.prepareStatement(
				"select TransDate, OpenPrice, HighPrice, LowPrice, ClosePrice"+
						"	from company natural join pricevolume"+
				"	where Ticker = ? and ? <= TransDate and ? >= TransDate order by TransDate DESC");
		pstmt.setString(1, ticker);
		pstmt.setString(2, start);
		pstmt.setString(3, end);
		ResultSet rs = pstmt.executeQuery();
		Deque<StockData> result = new ArrayDeque<>();
		if (rs.next()) {
			result.addFirst(new StockData(rs.getString(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5)));
		}
		double divider = 1;
		int splits = 0;
		while (rs.next()) {
			double splitVal = (rs.getDouble(5)/divider) / result.peek().openPrice;
			if (Math.abs(splitVal - 2.0) < 0.20) {
				divider *= 2;
				splits++;
			}
			else if(Math.abs(splitVal - 3.0) < 0.30) {
				divider *= 3;
				splits++;
			}
			else if(Math.abs(splitVal - 1.5) < 0.15) {
				divider *= 1.5;
				splits++;
			}
			result.addFirst(new StockData(rs.getString(1), rs.getDouble(2)/divider, rs.getDouble(3)/divider, rs.getDouble(4)/divider, rs.getDouble(5)/divider));
		}
		pstmt.close();
		return result;
    }
}
