import java.sql.*;
import java.util.ArrayList;

class Sales {
    int orderNumber;
    String customerName;
    String customerCity;
    String salesmanName;
    double amount;
    double commissionAmount;

    public Sales(int orderNumber, String customerName, String customerCity,
                 String salesmanName, double amount, double commissionAmount) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerCity = customerCity;
        this.salesmanName = salesmanName;
        this.amount = amount;
        this.commissionAmount = commissionAmount;
    }
// Ol' reliable toString() 
    public String toString() {
        return "ORDER NUMBER: " + orderNumber +
               " Customer: " + customerName +
               " City: " + customerCity +
               " Salesman: " + salesmanName +
               " Amount: $" + String.format("%.2f", amount) +
               " Commission: $" + String.format("%.2f", commissionAmount);
 }
}

public class DBConnection {
    public static void main(String[] args) {
        if (args.length != 3) {
        // simple guard so program doesn’t run with missing login info
            System.out.println("Usage: java DBConnection <username> <password> <database>");
            return;
        }
        
        
        String username = args[0];
        String password = args[1];
        String database = args[2];
        String url = "jdbc:mariadb://localhost:3306/" + database;
         // this just stores everything from the DB so we can print it after
        ArrayList<Sales> salesList = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database: " + database);

            String query =
                "SELECT o.order_no, c.customer_name, c.city, s.name, " +
                "o.purchase_amt, s.commission " +
                "FROM orders o " +
                "JOIN customer c ON o.customer_id = c.customer_id " +
                "JOIN salesman s ON o.salesman_id = s.salesman_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int orderNumber        = rs.getInt("order_no");
                String customerName    = rs.getString("customer_name");
                String customerCity    = rs.getString("city");
                String salesmanName    = rs.getString("name");
                double amount          = rs.getDouble("purchase_amt");
                double commission      = rs.getDouble("commission");
                double commissionAmt   = amount * commission;
                salesList.add(new Sales(orderNumber, customerName, customerCity,
                                        salesmanName, amount, commissionAmt));
            }

            System.out.println("\nSales Records: ");
            for (Sales sale : salesList) {
                System.out.println(sale);
            }
            System.out.println("\nTotal records: " + salesList.size());

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage());
        }
   
   
   
    }
}
