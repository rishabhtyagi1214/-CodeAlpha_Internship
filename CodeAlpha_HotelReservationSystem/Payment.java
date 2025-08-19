

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Payment")
public class Payment extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html; charset=UTF-8");
        PrintWriter out = res.getWriter();

        String paymentMode = req.getParameter("paymentMode");

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'><head><meta charset='UTF-8'>");
        out.println("<title>Payment</title>");
        out.println("<style>");
        out.println("body { font-family: 'Segoe UI', Arial; background: #f5f5f5; margin: 0; padding: 40px; text-align: center; }");
        out.println(".box { background:#fff; padding:25px 35px; border-radius:10px; box-shadow:0 6px 15px rgba(0,0,0,0.1); display:inline-block; max-width:500px; }");
        out.println("h2 { color:#1565c0; }");
        out.println("p { font-size:16px; color:#444; }");
        out.println(".btn { display:inline-block; margin-top:20px; padding:12px 22px; background:#1565c0; color:#fff; text-decoration:none; border-radius:6px; }");
        out.println("</style></head><body>");

        out.println("<div class='box'>");

        if ("Cash".equals(paymentMode)) {
            out.println("<h2>✅ Booking Confirmed</h2>");
            out.println("<p>You chose <b>Cash on Arrival</b>.</p>");
            out.println("<p>Please pay at the hotel during check-in.</p>");
            out.println("<a class='btn' href='index.html'>Thank You</a>");
            
        } else if ("Card".equals(paymentMode)) {
            // simulate card payment
            out.println("<h2>💳 Card Payment</h2>");
            out.println("<form action='Payment' method='get'>");
            out.println("<label>Card Number: </label><input type='text' name='cardNumber' required><br><br>");
            out.println("<label>Name on Card: </label><input type='text' name='cardName' required><br><br>");
            out.println("<label>Expiry: </label><input type='month' name='expiry' required><br><br>");
            out.println("<label>CVV: </label><input type='password' name='cvv' maxlength='3' required><br><br>");
            out.println("<button type='submit' class='btn'>Pay Now</button>");
            out.println("</form>");
        } else {
            out.println("<h2>⚠ Invalid Payment Option</h2>");
        }

        out.println("</div></body></html>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html; charset=UTF-8");
        PrintWriter out = res.getWriter();

        // Final payment success simulation after card details
        String cardNumber = req.getParameter("cardNumber");

        out.println("<!DOCTYPE html><html><head><title>Payment Success</title>");
        out.println("<style>");
        out.println("body { font-family:Arial; background:#e8f5e9; text-align:center; padding:50px; }");
        out.println(".box { background:#fff; padding:30px; border-radius:10px; box-shadow:0 6px 15px rgba(0,0,0,0.1); display:inline-block; }");
        out.println("h2 { color:#2e7d32; }");
        out.println("</style></head><body>");
        out.println("<div class='box'>");

        if (cardNumber != null) {
            out.println("<h2>✅ Payment Successful</h2>");
            out.println("<p>Your card ending with ****" + cardNumber.substring(cardNumber.length() - 4) + " has been charged.</p>");
            out.println("<p>Transaction ID: TXN" + System.currentTimeMillis() + "</p>");
        } else {
            out.println("<h2>⚠ No Card Info Provided</h2>");
        }

        out.println("<a class='btn' href='index.html'>Thank You</a>");
        out.println("</div></body></html>");
    }
}
