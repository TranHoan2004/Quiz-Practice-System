<%-- 
    Document   : reset_password
    Created on : May 19, 2025, 4:53:14 PM
    Author     : TranHoan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <link rel="stylesheet" href="https://unpkg.com/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lib/login-5.css">
    <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <style>
        .bsb-btn-xl {
            background-color: #00BCD4;
        }
    </style>
</head>

<body>
<section class="p-3 p-md-4 p-xl-5 bg-light min-vh-100 d-flex align-items-center">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                <div class="card border-light-subtle shadow-sm">
                    <div class="card-body p-3 p-md-4 p-xl-5">
                        <!-- Step 1: Verify Email -->
                        <c:if test="${code eq null && reset eq null}">
                            <div class="mb-4 text-center">
                                <h3>Reset Password</h3>
                                <p class="text-secondary">Enter your email to receive a verification code.</p>
                            </div>
                            <form id="emailForm" action="${pageContext.request.contextPath}/user" method="post">
                                <div class="mb-3">
                                    <label for="reset_email" class="form-label">Email
                                        <span class="text-danger">*</span>
                                    </label>
                                    <input type="email" class="form-control" name="email" id="reset_email"
                                           placeholder="name@example.com" required>
                                </div>
                                <c:if test="${error1 ne null}">
                                    <p class="text-danger">${error1}</p>
                                </c:if>
                                <div class="d-grid">
                                    <button class="btn bsb-btn-xl btn-primary" type="submit">Send Code</button>
                                </div>
                            </form>
                        </c:if>

                        <!-- Step 2: Verify 6-digit Code -->
                        <c:if test="${code ne null}">
                            <div id="step2">
                                <div class="mb-4 text-center">
                                    <h3>Enter Verification Code</h3>
                                    <p class="text-secondary">Please enter the 6-digit code sent to your email.</p>
                                </div>
                                <form id="codeForm" action="${pageContext.request.contextPath}/user" method="post">
                                    <div class="mb-3">
                                        <div class="d-flex justify-content-center gap-2">
                                            <input type="text" class="form-control text-center" maxlength="1"
                                                   pattern="\d" inputmode="numeric" name="1"
                                                   style="width: 48px; font-size: 1.5rem; border-width: 2.5px;"
                                                   required>
                                            <input type="text" class="form-control text-center" maxlength="1"
                                                   pattern="\d" inputmode="numeric" name="2"
                                                   style="width: 48px; font-size: 1.5rem; border-width: 2.5px;"
                                                   required>
                                            <input type="text" class="form-control text-center" maxlength="1"
                                                   pattern="\d" inputmode="numeric" name="3"
                                                   style="width: 48px; font-size: 1.5rem; border-width: 2.5px;"
                                                   required>
                                            <input type="text" class="form-control text-center" maxlength="1"
                                                   pattern="\d" inputmode="numeric" name="4"
                                                   style="width: 48px; font-size: 1.5rem; border-width: 2.5px;"
                                                   required>
                                            <input type="text" class="form-control text-center" maxlength="1"
                                                   pattern="\d" inputmode="numeric" name="5"
                                                   style="width: 48px; font-size: 1.5rem; border-width: 2.5px;"
                                                   required>
                                            <input type="text" class="form-control text-center" maxlength="1"
                                                   pattern="\d" inputmode="numeric" name="6"
                                                   style="width: 48px; font-size: 1.5rem; border-width: 2.5px;"
                                                   required>
                                        </div>
                                    </div>
                                    <c:if test="${error2 ne null}">
                                        <p class="text-danger">${error2}</p>
                                    </c:if>
                                    <div class="d-grid">
                                        <button class="btn bsb-btn-xl btn-primary" type="submit">Verify Code</button>
                                    </div>
                                    <div class="mt-3 text-center">
                                        <a href="${pageContext.request.contextPath}/user" id="resendCode"
                                           class="link-primary text-decoration-none">
                                            Resend Code
                                        </a>
                                    </div>
                                </form>
                            </div>
                        </c:if>

                        <!-- Step 3: Enter New Password -->
                        <c:if test="${reset ne null}">
                            <div class="mb-4 text-center">
                                <h3>Set New Password</h3>
                                <p class="text-secondary">Enter your new password below.</p>
                            </div>
                            <form id="passwordForm" action="#!">
                                <div class="mb-3">
                                    <label for="new_password" class="form-label">New Password <span
                                            class="text-danger">*</span></label>
                                    <input type="password" class="form-control" name="new_password"
                                           id="new_password" required>
                                    <div id="passwordHelp" class="form-text text-danger d-none"></div>
                                </div>
                                <div class="mb-3">
                                    <label for="confirm_new_password" class="form-label">Confirm New Password <span
                                            class="text-danger">*</span></label>
                                    <input type="password" class="form-control" name="confirm_new_password"
                                           id="confirm_new_password" required>
                                    <div id="confirmPasswordHelp" class="form-text text-danger d-none"></div>
                                </div>
                                <div class="d-grid">
                                    <button class="btn bsb-btn-xl btn-primary" type="submit">Reset Password</button>
                                </div>
                            </form>
                        </c:if>

                        <div class="row mt-4">
                            <div class="col-12">
                                <div class="d-flex gap-2 gap-md-4 flex-column flex-md-row justify-content-md-end">
                                    <a href="login.html" class="link-secondary text-decoration-none">Back to
                                        Login</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../../component/notification.html"/>

<script src="${pageContext.request.contextPath}/js/Notification.js"></script>
<script>
    document.querySelectorAll('#step2 input[type="text"]').forEach((input, idx, arr) => {
        input.addEventListener('input', function () {
            if (this.value.length === 1 && idx < arr.length - 1) {
                arr[idx + 1].focus();
            }
        });
        input.addEventListener('keydown', function (e) {
            if (e.key === "Backspace" && this.value === "" && idx > 0) {
                arr[idx - 1].focus();
            }
        });
    });

    function validatePasswordFormat(password) {
        return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/.test(password);
    }

    document.getElementById('passwordForm').onsubmit = function (e) {
        e.preventDefault();
        const password = document.getElementById('new_password').value;
        const confirmPassword = document.getElementById('confirm_new_password').value;
        const passwordHelp = document.getElementById('passwordHelp');
        const confirmPasswordHelp = document.getElementById('confirmPasswordHelp');
        let valid = true;

        // Check password format
        if (!validatePasswordFormat(password)) {
            passwordHelp.textContent = "Password must be at least 8 characters, include uppercase, lowercase, number, and special character.";
            passwordHelp.classList.remove('d-none');
            valid = false;
        } else {
            passwordHelp.classList.add('d-none');
        }

        // Check confirm password
        if (password !== confirmPassword) {
            confirmPasswordHelp.textContent = "Passwords do not match.";
            confirmPasswordHelp.classList.remove('d-none');
            valid = false;
        } else {
            confirmPasswordHelp.classList.add('d-none');
        }

        if (valid) {
            console.log('Go here')
            fetch(`${pageContext.request.contextPath}/user`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({password: password})
            })
                .then(response => response.text())
                .then(data => {
                    console.log(data)
                    showNotification("Reset successfully", "success")
                    setTimeout(() => {
                        window.location.href = `${pageContext.request.contextPath}/jsp/common-features/login_account.jsp`
                    }, 3000)
                })
                .catch(err => {
                    console.log(err)
                    showNotification(err.message, "not success")
                })
        }
    };
</script>
</body>

</html>
