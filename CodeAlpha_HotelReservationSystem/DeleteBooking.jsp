<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%
    String message = "";
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "root1214");

            String sql = "DELETE FROM customerdetail WHERE email=? OR mobile=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, mobile);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                message = "<p style='color:green;'>✅ Booking deleted successfully!</p>";
                
            } else {
                message = "<p style='color:red;'>❌ No booking found with given details.</p>";
            }

            con.close();
        } catch (Exception e) {
            message = "<p style='color:red;'>⚠ Error: " + e.getMessage() + "</p>";
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Cancel Reservation</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f0f8ff; }
        .container { max-width: 450px; margin: 50px auto; background: #fff; padding: 25px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.2);}
        h2 { text-align:center; color:#333; }
        label { display:block; margin-top:10px; font-weight:bold; }
        input { width:95%; padding:8px; margin-top:5px; border:1px solid #ccc; border-radius:5px;}
        
        button, .btn {
            display: inline-block;
            margin-top:15px;
            padding:10px 20px;
            background:#d9534f;
            color:#fff;
            border:none;
            border-radius:5px;
            cursor:pointer;
            text-decoration:none;
            font-size: 14px;
        }
        button:hover, .btn:hover {
            background:#c9302c;
        }

        .msg { margin-top:15px; text-align:center; font-size:14px; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Cancel Reservation</h2>
        <form method="post">
            <label>Email:</label>
            <input type="email" name="email" required>
            <label>Mobile:</label>
            <input type="text" name="mobile">
            <button type="submit">Delete Booking</button>
            <a class="btn" href="index.html">Thank You</a>
        </form>
        <div class="msg"><%= message %></div>
    </div>
</body>
</html>
