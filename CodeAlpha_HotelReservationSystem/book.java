

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
    urlPatterns = "/book",
    initParams = {
        @WebInitParam(name = "driver",   value = "com.mysql.cj.jdbc.Driver"),
        @WebInitParam(name = "url",      value = "jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC"),
        @WebInitParam(name = "user",     value = "root"),
        @WebInitParam(name = "password", value = "root1214")
    }
)
public class book extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=UTF-8");
        PrintWriter out = res.getWriter();

        // 1) Read form fields
        String name     = req.getParameter("name");
        String email    = req.getParameter("email");
        String address  = req.getParameter("address");
        String mobile   = req.getParameter("mobile");
        String roomRaw  = req.getParameter("room");     // might be "standard" / "Standard"
        String inStr    = req.getParameter("checkIn");  // yyyy-MM-dd
        String outStr   = req.getParameter("checkOut"); // yyyy-MM-dd

        // 2) Normalize & validate inputs
        String roomType = normalizeRoomType(roomRaw);
        if (roomType == null) {
            renderError(out, "Invalid room type. Choose Standard / Deluxe / Suite.", "book");
            return;
        }

        LocalDate checkIn, checkOut;
        try {
            checkIn  = LocalDate.parse(inStr);
            checkOut = LocalDate.parse(outStr);
        } catch (DateTimeParseException e) {
            renderError(out, "Invalid date format. Please select valid dates.", "book");
            return;
        }

        if (!checkOut.isAfter(checkIn)) {
            renderError(out, "Check-out date must be after check-in date.", "book");
            return;
        }

        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);

        // 3) Compute price (simulation)
        double pricePerRoom = switch (roomType) {
            case "Standard" -> 2000.0;
            case "Deluxe"   -> 3500.0;
            case "Suite"    -> 5000.0;
            default         -> 0.0;
        };
        double totalAmount = pricePerRoom * totalDays;

        // 4) DB insert
        String driver   = getServletConfig().getInitParameter("driver");
        String url      = getServletConfig().getInitParameter("url");
        String user     = getServletConfig().getInitParameter("user");
        String password = getServletConfig().getInitParameter("password");

        String sql = "INSERT INTO customerdetail " +
                     "(fullname, mobile, address, email, room_type, check_in, check_out) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Class.forName(driver);
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setString(2, mobile);
                ps.setString(3, address);
                ps.setString(4, email);
                ps.setString(5, roomType);
                ps.setDate(6, Date.valueOf(checkIn));
                ps.setDate(7, Date.valueOf(checkOut));

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    renderSuccess(out, name, email, roomType, totalDays, totalAmount);
                } else {
                    renderError(out, "Booking failed. Please try again.", "book");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderException(out, e.getMessage(), "book");
        }
    }


    private static String normalizeRoomType(String roomRaw) {
        if (roomRaw == null) return null;
        String r = roomRaw.trim().toLowerCase();
        return switch (r) {
            case "standard" -> "Standard";
            case "deluxe"   -> "Deluxe";
            case "suite"    -> "Suite";
            default         -> null;
        };
    }

    private static void renderSuccess(PrintWriter out, String name, String email,
                                      String roomType, long totalDays, double totalAmount) {
    	out.println("<!DOCTYPE html>");
    	out.println("<html lang='en'>");
    	out.println("<head>");
    	out.println("<meta charset='UTF-8'>");
    	out.println("<title>Booking Confirmation</title>");
    	out.println("<style>");
    	out.println("body { font-family: 'Segoe UI', Arial, sans-serif; background: linear-gradient(to right, #fdfbfb, #ebedee); margin: 0; padding: 40px; display: flex; justify-content: center; align-items: center; min-height: 100vh; }");
    	out.println(".confirmation-box { background: #ffffff; padding: 30px 40px; border-radius: 12px; box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1); text-align: center; max-width: 500px; width: 100%; }");
    	out.println(".confirmation-box h2 { color: #2e7d32; margin-bottom: 20px; font-size: 26px; }");
    	out.println(".confirmation-box p { font-size: 16px; margin: 8px 0; color: #444; }");
    	out.println(".confirmation-box p strong { color: #000; }");
    	out.println(".amount { margin-top: 15px; font-size: 20px; font-weight: bold; color: #e65100; }");
    	out.println(".btn { display: inline-block; margin-top: 25px; padding: 12px 22px; background: #2e7d32; color: #fff; text-decoration: none; font-size: 15px; border-radius: 6px; transition: background 0.3s ease; }");
    	out.println(".btn:hover { background: #1b5e20; }");
    	out.println("</style>");
    	out.println("</head>");
    	out.println("<body>");

    	


    	// Payment Form (Cash or Card)
    	out.println("<div class='confirmation-box'>");
    	out.println("<h2>🎉 Booking Successful!</h2>");
    	out.println("<p><strong>Name:</strong> " + name + "</p>");
    	out.println("<p><strong>Email:</strong> " + email + "</p>");
    	out.println("<p><strong>Room Type:</strong> " + roomType + "</p>");
    	out.println("<p><strong>Total Days:</strong> " + totalDays + "</p>");
    	out.println("<p class='amount'>Total Amount: ₹" + String.format("%.2f", totalAmount) + "</p>");

    	out.println("<form action='Payment' method='post'>");
    	out.println("<h3>Choose Payment Method:</h3>");
    	out.println("<input type='radio' name='paymentMode' value='Cash' required> Cash on Arrival<br>");
    	out.println("<input type='radio' name='paymentMode' value='Card'> Pay by Card<br><br>");
    	out.println("<button type='submit' class='btn'>Proceed to Pay</button>");
    	out.println("</form>");

    	out.println("</div>");

    }

    private static void renderError(PrintWriter out, String msg, String backHref) {
        out.println("""
            <html><head><title>Booking Error</title>
            <style>
            body { font-family: Arial, sans-serif; background-color: #fff0f0; text-align: center; padding: 30px; }
            .error-box { background: #f8d7da; color: #721c24; padding: 20px; border-radius: 8px; max-width: 520px; margin: auto; box-shadow: 0 0 15px rgba(0,0,0,0.1); }
            .error-box h3 { margin-top: 0; }
            .back-btn { display: inline-block; margin-top: 15px; padding: 10px 20px; background: #721c24; color: white; text-decoration: none; border-radius: 5px; }
            .back-btn:hover { background: #501217; }
            </style></head><body>
            <div class='error-box'>
        """);
        out.println("<h3>" + escape(msg) + "</h3>");
        out.println("<a href='" + backHref + "' class='back-btn'>Go Back</a>");
        out.println("</div></body></html>");
    }

    private static void renderException(PrintWriter out, String msg, String backHref) {
        renderError(out, "Error: " + msg, backHref);
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
                .replace("\"","&quot;").replace("'","&#39;");
    }
}
