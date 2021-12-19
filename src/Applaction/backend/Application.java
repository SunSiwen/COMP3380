package Applaction.backend;

import com.ibm.db2.cmx.internal.json4j.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

/**
 * @author Administrator
 */
public class Application {

    private final static String DRIVER = "com.ibm.db2.jcc.DB2Driver";
    private final static String URL = "jdbc:db2://localhost:25000/olist";
    private final static String USER_NAME = "siwen";
    private final static String PASS_WORD = "123456";

    public static void main(String[] args) throws Exception {
        Connection conn = null;
        Statement st;


//        ServerSocket ss = new ServerSocket(9000);
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER_NAME, PASS_WORD);
            while (true) {
                ServerSocket serverSocket = new ServerSocket(9000);
                Socket socket = serverSocket.accept();

                InputStream inputStream = socket.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                OutputStream outputStream = socket.getOutputStream();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);


                byte[] tmp = new byte[1024];
                int read = bufferedInputStream.read(tmp);
                String rev = new String(tmp, 0, read);
                System.out.println(rev);
                String substring = rev.substring(rev.indexOf("{"), rev.lastIndexOf("}") + 1);
                st = conn.createStatement();

                st.execute("set current schema siwen");

                String res;
                if (rev.contains("POST /query1")) {
                    res = query1(conn, substring);
                } else if (rev.contains("POST /query2")) {
                    res = query2(conn, substring);
                } else if (rev.contains("POST /query3")) {
                    res = query3(conn, substring);
                } else if (rev.contains("POST /query4")) {
                    res = query4(conn, substring);
                } else if (rev.contains("POST /query5")) {
                    res = query5(conn, substring);
                } else if (rev.contains("POST /query6")) {
                    res = query6(conn, substring);
                } else if (rev.contains("POST /query7")) {
                    res = query7(conn, substring);
                } else if (rev.contains("POST /query8")) {
                    res = query8(conn, substring);
                } else if (rev.contains("POST /query9")) {
                    res = query9(conn, substring);
                } else if (rev.contains("POST /query0")) {
                    res = query0(conn, substring);
                } else {
                    break;
                }

                res = "HTTP/1.1 200 OK\r\nContent-type: text/html\r\nAccess-Control-Allow-Origin: *\r\n\r\n" + res + "\r\n\r\n";

//                System.out.println(res);
                bufferedOutputStream.write(res.getBytes());
                bufferedOutputStream.flush();

                bufferedOutputStream.close();
                outputStream.close();
                bufferedInputStream.close();
                inputStream.close();
                socket.close();
                serverSocket.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    private static String query0(Connection conn, String para) throws Exception {
        ResultSet rs;
        StringBuilder res = new StringBuilder("[");
        PreparedStatement ps;

        int num = 10;
        JSONObject parse = JSONObject.parse(para);
        try {
            if (!"".equals(parse.get("num"))) {
                num = Integer.parseInt((String) parse.get("num"));
                if (num <= 0) {
                    num = 10;
                }
            }
        } catch (Exception ignored) {
        }

        ps = conn.prepareStatement("""
                select upper.ORDER_ID as orderId,
                       upper.distance,
                       lower.FREIGHT_VALUE                     as cost,
                       (lower.FREIGHT_VALUE / distance * 1000) as unitprice
                from (
                         select ORDER_ID,
                                round(6378.138 * 2 * asin(sqrt(sin((cla * 3.141592653 / 180 - sla * 3.141592653 / 180) / 2) *
                                                               sin((cla * 3.141592653 / 180 - sla * 3.141592653 / 180) / 2) +
                                                               cos(cla * 3.141592653 / 180) * cos(sla * 3.141592653 / 180) *
                                                               sin((cln * 3.141592653 / 180 - sln * 3.141592653 / 180) / 2) *
                                                               sin((cln * 3.141592653 / 180 - sln * 3.141592653 / 180) / 2))) *
                                      1000)+1 as distance
                         from (
                                  select w.ORDER_ID, cla, cln, e.LNG as sln, e.LAT as sla
                                  from (
                                           select q.ORDER_ID, cz, sz, g.LAT as cla, g.LNG as cln
                                           from (select tmp.ORDER_ID, tmp.cz, s.ZIPCODE as sz
                                                 from (select o.ORDER_ID, o.SELLER_ID, c.ZIPCODE as cz
                                                       from ORDERS o
                                                                inner join USERS c on c.USER_ID = o.CUSTOMER_ID) tmp
                                                          inner join USERS s on s.USER_ID = tmp.SELLER_ID) q
                                                    inner join GEOLOCATION g on q.cz = g.ZIPCODE) w
                                           inner join GEOLOCATION e on e.ZIPCODE = sz)
                     ) upper
                         inner join ORDERS lower on upper.ORDER_ID = lower.ORDER_ID
                order by cost desc
                limit ?
                """);
        ps.setInt(1, num);
        rs = ps.executeQuery();
        while (rs.next()) {
            res.append("{ \"orderId\": \"").append(rs.getString("ORDERID")).append("\", \"cost\": \"").append(rs.getDouble("cost")).append("\", \"distance\": \"").append(rs.getInt("DISTANCE")).append("\", \"unitPrice\": \"").append(rs.getDouble("UNITPRICE")).append("\"},");
        }

        if (res.length() > 2) {
            res = new StringBuilder(res.substring(0, res.length() - 1));
        }
        return res + "]";
    }

    private static String query9(Connection conn, String para) throws Exception {
        ResultSet rs;
        StringBuilder res = new StringBuilder("[");
        PreparedStatement ps;

        int num = 10;
        JSONObject parse = JSONObject.parse(para);
        try {
            if (!"".equals(parse.get("num"))) {
                num = Integer.parseInt((String) parse.get("num"));
                if (num <= 0) {
                    num = 10;
                }
            }
        } catch (Exception ignored) {
        }

        ps = conn.prepareStatement("""
                select SELLER_ID as sellerId, count(distinct CATEGORY) as num
                from (select o.SELLER_ID, p.CATEGORY
                      from ORDERS o
                               inner join PRODUCTS p on o.PRODUCT_ID = p.PRODUCTID
                      order by SELLER_ID) tmp
                group by SELLER_ID
                order by num desc
                limit ?""");
        ps.setInt(1, num);
        rs = ps.executeQuery();
        while (rs.next()) {
            res.append("{ \"sellerId\": \"").append(rs.getString("sellerId")).append("\", \"num\": \"").append(rs.getDouble("num")).append("\"},");
        }

        if (res.length() > 2) {
            res = new StringBuilder(res.substring(0, res.length() - 1));
        }
        return res + "]";
    }

    private static String query8(Connection conn, String para) throws Exception {
        ResultSet rs;
        StringBuilder res = new StringBuilder("[");
        PreparedStatement ps;

        int num = 10;
        JSONObject parse = JSONObject.parse(para);
        try {
            if (!"".equals(parse.get("num"))) {
                num = Integer.parseInt((String) parse.get("num"));
                if (num <= 0) {
                    num = 10;
                }
            }
        } catch (Exception ignored) {
        }

        ps = conn.prepareStatement("""
                select count(*) as num, CATEGORY
                        from (
                                 select *
                                 from PRODUCTS p
                                          join (
                                     select *
                                     from ORDERS o
                                              join
                                          (select distinct ORDER_ID as ORDER_ID
                                           from REVIEWS
                                           where REVIEWS.REVIEW_SCORE < 3
                                             and REVIEW_COMMENT_MESSAGE = ''
                                             and REVIEW_COMMENT_TITLE = '') d on o.ORDER_ID = d.ORDER_ID) q on p.PRODUCTID = q.PRODUCT_ID
                             ) tmp
                        group by tmp.CATEGORY
                        order by num desc
                        limit ?""");
        ps.setInt(1, num);
        rs = ps.executeQuery();
        while (rs.next()) {
            res.append("{ \"category\": \"").append(rs.getString("category")).append("\", \"num\": \"").append(rs.getInt("num")).append("\"},");
        }

        if (res.length() > 2) {
            res = new StringBuilder(res.substring(0, res.length() - 1));
        }
        return res + "]";
    }

    private static String query7(Connection conn, String para) throws Exception {
        ResultSet rs;
        StringBuilder res = new StringBuilder("[");
        PreparedStatement ps;

        int num = 10;
        JSONObject parse = JSONObject.parse(para);
        try {
            if (!"".equals(parse.get("num"))) {
                num = Integer.parseInt((String) parse.get("num"));
                if (num <= 0) {
                    num = 10;
                }
            }
        } catch (Exception ignored) {
        }

        ps = conn.prepareStatement("""
                select res.PRODUCTID, c.NAME, num
                from CATEGORY c
                         inner join (
                    select *
                    from PRODUCTS p
                             inner join (
                        select count(*) as num, PRODUCT_ID
                        from ORDERS
                        where FREIGHT_VALUE > ORDERS.PRICE
                        group by PRODUCT_ID
                    ) tmp on p.PRODUCTID = tmp.PRODUCT_ID
                ) res on res.CATEGORY = c.NAME
                order by num desc limit ?""");
        ps.setInt(1, num);
        rs = ps.executeQuery();
        while (rs.next()) {
            res.append("{ \"productId\": \"").append(rs.getString("PRODUCTID")).append("\", \"name\": \"").append(rs.getString("name")).append("\", \"num\": \"").append(rs.getInt("num")).append("\"},");
        }

        if (res.length() > 2) {
            res = new StringBuilder(res.substring(0, res.length() - 1));
        }
        return res + "]";
    }

    private static String query6(Connection conn, String para) throws Exception {
        ResultSet rs;
        StringBuilder res = new StringBuilder("[");
        PreparedStatement ps;
        int zipcode = 1;
        JSONObject parse = JSONObject.parse(para);
        try {
            if (!"".equals(parse.get("zipcode"))) {
                zipcode = Integer.parseInt((String) parse.get("zipcode"));
                if (zipcode <= 0) {
                    zipcode = 1;
                }
            }
        } catch (Exception ignored) {
        }


        ps = conn.prepareStatement("""
                select lower.rate as rate, upper.SELLER_ID as sellerId, lower.ZIPCODE as cityZipcode
                from (select tmp.SELLER_ID, USERS.ZIPCODE, avg(REVIEW_SCORE) as rate
                      from USERS
                               inner join (
                          select o.SELLER_ID as SELLER_ID, r.REVIEW_SCORE
                          from ORDERS o
                                   inner join REVIEWS r on o.ORDER_ID = r.ORDER_ID) tmp
                                          on USER_ID = tmp.SELLER_ID
                      group by SELLER_ID, ZIPCODE) upper
                         inner join (
                    select max(rate) as rate, res.ZIPCODE
                    from (
                             select USERS.ZIPCODE, avg(REVIEW_SCORE) as rate
                             from USERS
                                      inner join (
                                 select o.SELLER_ID as SELLER_ID, r.REVIEW_SCORE
                                 from ORDERS o
                                          inner join REVIEWS r on o.ORDER_ID = r.ORDER_ID) tmp
                                                 on USER_ID = tmp.SELLER_ID
                             group by SELLER_ID, ZIPCODE) res
                    group by res.ZIPCODE
                ) lower
                                    on upper.rate = lower.rate and upper.ZIPCODE = lower.ZIPCODE
                where lower.ZIPCODE = ?;""");
        ps.setInt(1, zipcode);
        rs = ps.executeQuery();
        while (rs.next()) {
            res.append("{ \"rate\": \"").append(rs.getInt("rate")).append("\", \"sellerId\": \"").append(rs.getString("sellerId")).append("\", \"cityZipcode\": \"").append(rs.getInt("cityZipcode")).append("\"},");
        }

        if (res.length() > 2) {
            res = new StringBuilder(res.substring(0, res.length() - 1));
        }
        return res + "]";
    }

    private static String query5(Connection conn, String para) throws Exception {
        ResultSet rs;
        StringBuilder res = new StringBuilder("[");
        PreparedStatement ps;

        int num = 10;
        JSONObject parse = JSONObject.parse(para);
        try {
            if (!"".equals(parse.get("num"))) {
                num = Integer.parseInt((String) parse.get("num"));
                if (num <= 0) {
                    num = 10;
                }
            }
        } catch (Exception ignored) {
        }

        ps = conn.prepareStatement("""
                select count(*)as num,pc.NAMEENG as category from ORDERS o inner join (
                    select p.PRODUCTID, c.NAMEENG
                    from PRODUCTS p
                             inner join CATEGORY c on p.CATEGORY = c.NAME
                ) pc on pc.PRODUCTID = o.PRODUCT_ID group by pc.NAMEENG order by num desc limit ?;""");
        ps.setInt(1, num);
        rs = ps.executeQuery();
        while (rs.next()) {
            res.append("{ \"category\": \"").append(rs.getString("category")).append("\", \"num\": \"").append(rs.getInt("num")).append("\"},");
        }

        if (res.length() > 2) {
            res = new StringBuilder(res.substring(0, res.length() - 1));
        }
        return res + "]";
    }

    private static String query4(Connection conn, String para) throws Exception {
        ResultSet rs;
        StringBuilder res = new StringBuilder("[");
        PreparedStatement ps;
        String startDate = "2015-09-09 00:00:00";
        String endDate = "2021-09-09 23:59:59";
        JSONObject parse = JSONObject.parse(para);
        if (!"".equals(parse.get("startDate"))) {
            startDate = parse.get("startDate") + " 00:00:00";
        }
        if (!"".equals(parse.get("endDate"))) {
            endDate = parse.get("endDate") + " 23:59:59";
        }


        ps = conn.prepareStatement("""
                select upper.ZIPCODE as cityZipcode, upper.income as income, lower.spend as cost
                from (
                         select sum(price) as income, zipcode
                         from orders
                                  inner join users u on orders.SELLER_ID = u.user_id
                         where order_purchase_timestamp >= ?
                           and order_purchase_timestamp <= ?
                         group by zipcode) upper
                         inner join(
                    select sum(price) as spend, zipcode
                    from orders
                             inner join users u on orders.customer_id = u.user_id
                    where order_purchase_timestamp >= ?
                      and order_purchase_timestamp <= ?
                    group by zipcode) lower
                                   on upper.ZIPCODE = lower.ZIPCODE
                where upper.income > lower.spend""");
        ps.setTimestamp(1, Timestamp.valueOf(startDate));
        ps.setTimestamp(2, Timestamp.valueOf(endDate));
        ps.setTimestamp(3, Timestamp.valueOf(startDate));
        ps.setTimestamp(4, Timestamp.valueOf(endDate));
        rs = ps.executeQuery();
        while (rs.next()) {
            res.append("{ \"cost\": \"").append(rs.getDouble("cost")).append("\", \"income\": \"").append(rs.getDouble("income")).append("\", \"cityZipcode\": \"").append(rs.getInt("cityZipcode")).append("\"},");
        }

        if (res.length() > 2) {
            res = new StringBuilder(res.substring(0, res.length() - 1));
        }
        return res + "]";
    }


    public static String query1(Connection conn, String para) throws Exception {
        ResultSet rs;
        StringBuilder res = new StringBuilder("[");
        PreparedStatement ps;
        String minPrice = "0";
        String maxPrice = "999999";
        String startDate = "2015-09-09 00:00:00";
        String endDate = "2021-09-09 23:59:59";
        JSONObject parse = JSONObject.parse(para);
        if (!"".equals(parse.get("minPrice"))) {
            minPrice = (String) parse.get("minPrice");
        }
        if (!"".equals(parse.get("maxPrice"))) {
            maxPrice = (String) parse.get("maxPrice");
        }
        if (!"".equals(parse.get("startDate"))) {
            startDate = parse.get("startDate") + " 00:00:00";
        }
        if (!"".equals(parse.get("endDate"))) {
            endDate = parse.get("endDate") + " 23:59:59";
        }


        ps = conn.prepareStatement("select order_id,price,order_purchase_timestamp from orders where price >= ? and price <= ? and order_purchase_timestamp >= ? and order_purchase_timestamp <= ? order by order_purchase_timestamp");
        ps.setDouble(1, Double.parseDouble(minPrice));
        ps.setDouble(2, Double.parseDouble(maxPrice));
        ps.setTimestamp(3, Timestamp.valueOf(startDate));
        ps.setTimestamp(4, Timestamp.valueOf(endDate));
        rs = ps.executeQuery();
        while (rs.next()) {
            res.append("{ \"order_id\": \"").append(rs.getString("order_id")).append("\", \"price\": \"").append(rs.getDouble("price")).append("\", \"order_purchase_timestamp\": \"").append(rs.getTimestamp("order_purchase_timestamp")).append("\"},");
        }

        if (res.length() > 2) {
            res = new StringBuilder(res.substring(0, res.length() - 1));
        }
        return res + "]";
    }

    public static String query2(Connection conn, String para) throws Exception {
        ResultSet rs;
        StringBuilder res = new StringBuilder("[");
        PreparedStatement ps;
        String startDate = "2015-09-09 00:00:00";
        String endDate = "2021-09-09 23:59:59";
        JSONObject parse = JSONObject.parse(para);
        if (!"".equals(parse.get("startDate"))) {
            startDate = parse.get("startDate") + " 00:00:00";
        }
        if (!"".equals(parse.get("endDate"))) {
            endDate = parse.get("endDate") + " 23:59:59";
        }

        ps = conn.prepareStatement("select upper.user_id as customerId, upper.spend as cost, upper.zipcode as cityZipcode from (select sum(price) as spend, user_id, zipcode from orders inner join users u on orders.customer_id = u.user_id where order_purchase_timestamp >= ? and order_purchase_timestamp <= ? group by user_id, zipcode ) upper inner join ( select max(tmp.spend) as maxcost, zipcode from (select sum(price) as spend, user_id, zipcode from orders inner join users u on orders.customer_id = u.user_id where order_purchase_timestamp >= ? and order_purchase_timestamp <= ? group by user_id, zipcode) tmp group by zipcode) lower on upper.spend = lower.maxcost and upper.zipcode = lower.zipcode order by lower.zipcode");
        ps.setTimestamp(1, Timestamp.valueOf(startDate));
        ps.setTimestamp(2, Timestamp.valueOf(endDate));
        ps.setTimestamp(3, Timestamp.valueOf(startDate));
        ps.setTimestamp(4, Timestamp.valueOf(endDate));
        rs = ps.executeQuery();
        while (rs.next()) {
            res.append("{ \"customerId\": \"").append(rs.getString("customerId")).append("\", \"cost\": \"").append(rs.getDouble("cost")).append("\", \"cityZipcode\": \"").append(rs.getInt("cityZipcode")).append("\"},");
        }

        if (res.length() > 2) {
            res = new StringBuilder(res.substring(0, res.length() - 1));
        }
        return res + "]";
    }

    public static String query3(Connection conn, String para) throws Exception {
        ResultSet rs;
        StringBuilder res = new StringBuilder("[");
        PreparedStatement ps;
        String startDate = "2015-09-09 00:00:00";
        String endDate = "2021-09-09 23:59:59";
        JSONObject parse = JSONObject.parse(para);
        if (!"".equals(parse.get("startDate"))) {
            startDate = parse.get("startDate") + " 00:00:00";
        }
        if (!"".equals(parse.get("endDate"))) {
            endDate = parse.get("endDate") + " 23:59:59";
        }


        ps = conn.prepareStatement("""
                select upper.user_id as customerId, upper.spend as cost, upper.zipcode as cityZipcode
                from (select sum(price) as spend, user_id, zipcode
                      from orders
                               inner join users u on orders.customer_id = u.user_id
                      where order_purchase_timestamp >= ?
                        and order_purchase_timestamp <= ?
                      group by user_id, zipcode) upper
                         inner join (select avg(tmp.spend) as avgcost, zipcode
                                     from (select sum(price) as spend, user_id, zipcode
                                           from orders
                                                    inner join users u on orders.customer_id = u.user_id
                                           where order_purchase_timestamp >= ?
                                             and order_purchase_timestamp <= ?
                                           group by user_id, zipcode) tmp
                                     group by zipcode) lower on upper.spend > lower.avgcost and upper.zipcode = lower.zipcode
                order by lower.zipcode""");
        ps.setTimestamp(1, Timestamp.valueOf(startDate));
        ps.setTimestamp(2, Timestamp.valueOf(endDate));
        ps.setTimestamp(3, Timestamp.valueOf(startDate));
        ps.setTimestamp(4, Timestamp.valueOf(endDate));
        rs = ps.executeQuery();
        while (rs.next()) {
            res.append("{ \"customerId\": \"").append(rs.getString("customerId")).append("\", \"cost\": \"").append(rs.getDouble("cost")).append("\", \"cityZipcode\": \"").append(rs.getInt("cityZipcode")).append("\"},");
        }

        if (res.length() > 2) {
            res = new StringBuilder(res.substring(0, res.length() - 1));
        }
        return res + "]";
    }
}
