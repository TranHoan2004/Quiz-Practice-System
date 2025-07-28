<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Success</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f4f4f4;
            margin: 0;
        }
        .success-message {
            background-color: #e6f3fa;
            padding: 20px;
            border: 1px solid #ccc;
            text-align: center;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .success-message h2 {
            color: #1e90ff;
            font-family: 'Comic Sans MS', cursive;
        }
        .back-btn {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #1e90ff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .back-btn:hover {
            background-color: #104e8b;
        }
    </style>
</head>
<body>
    <div class="success-message">
        <h2>${requestScope.message}</h2>
        <a href="jsp/common-features/edit_profile.jsp" class="back-btn">Back to Edit Profile</a>
    </div>
</body>
</html>