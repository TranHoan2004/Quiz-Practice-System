<%-- 
    Document   : reset_password
    Created on : May 19, 2025, 4:53:14 PM
    Author     : TranHoan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <%--        <link rel="stylesheet" href="https://unpkg.com/bootstrap@5.3.3/dist/css/bootstrap.min.css">--%>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <%--        <link rel="stylesheet" href="../../css/lib/login-5.css">--%>
    <link href="${pageContext.request.contextPath}/css/lib/login-5.css" rel="stylesheet">
</head>

<body>
<section class="p-3 p-md-4 p-xl-5 bg-light min-vh-100 d-flex align-items-center">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                <div class="card border-light-subtle shadow-sm">
                    <div class="card-body p-3 p-md-4 p-xl-5">
                        <!-- Step 1: Verify Email -->
                        <div id="step1">
                            <div class="mb-4 text-center">
                                <h3>Reset Password</h3>
                                <p class="text-secondary">Enter your email to receive a verification code.</p>
                            </div>
                            <form id="emailForm" action="#!">
                                <div class="mb-3">
                                    <label for="reset_email" class="form-label">Email <span
                                            class="text-danger">*</span></label>
                                    <input type="email" class="form-control" name="reset_email" id="reset_email"
                                           placeholder="name@example.com" required>
                                </div>
                                <div class="d-grid">
                                    <button class="btn bsb-btn-xl btn-primary" type="submit">Send Code</button>
                                </div>
                            </form>
                        </div>

                        <!-- Step 2: Verify 6-digit Code -->
                        <div id="step2" style="display:none;">
                            <div class="mb-4 text-center">
                                <h3>Enter Verification Code</h3>
                                <p class="text-secondary">Please enter the 6-digit code sent to your email.</p>
                            </div>
                            <form id="codeForm" action="#!">
                                <div class="mb-3">
                                    <div class="d-flex justify-content-center gap-2">
                                        <input type="text" class="form-control text-center" maxlength="1"
                                               pattern="\d" inputmode="numeric"
                                               style="width: 48px; font-size: 1.5rem; border-width: 2.5px;" required>
                                        <input type="text" class="form-control text-center" maxlength="1"
                                               pattern="\d" inputmode="numeric"
                                               style="width: 48px; font-size: 1.5rem; border-width: 2.5px;" required>
                                        <input type="text" class="form-control text-center" maxlength="1"
                                               pattern="\d" inputmode="numeric"
                                               style="width: 48px; font-size: 1.5rem; border-width: 2.5px;" required>
                                        <input type="text" class="form-control text-center" maxlength="1"
                                               pattern="\d" inputmode="numeric"
                                               style="width: 48px; font-size: 1.5rem; border-width: 2.5px;" required>
                                        <input type="text" class="form-control text-center" maxlength="1"
                                               pattern="\d" inputmode="numeric"
                                               style="width: 48px; font-size: 1.5rem; border-width: 2.5px;" required>
                                        <input type="text" class="form-control text-center" maxlength="1"
                                               pattern="\d" inputmode="numeric"
                                               style="width: 48px; font-size: 1.5rem; border-width: 2.5px;" required>
                                    </div>
                                </div>
                                <div class="d-grid">
                                    <button class="btn bsb-btn-xl btn-primary" type="submit">Verify Code</button>
                                </div>
                                <div class="mt-3 text-center">
                                    <a href="#!" id="resendCode" class="link-primary text-decoration-none">
                                        Resend Code
                                    </a>
                                </div>
                            </form>
                        </div>

                        <!-- Step 3: Enter New Password -->
                        <div id="step3" style="display:none;">
                            <div class="mb-4 text-center">
                                <h3>Set New Password</h3>
                                <p class="text-secondary">Enter your new password below.</p>
                            </div>
                            <form id="passwordForm" action="#!">
                                <div class="mb-3">
                                    <label for="new_password" class="form-label">New Password
                                        <span class="text-danger">*</span>
                                    </label>
                                    <input type="password" class="form-control" name="new_password"
                                           id="new_password" required>
                                </div>
                                <div class="mb-3">
                                    <label for="confirm_new_password" class="form-label">
                                        Confirm New Password
                                        <span class="text-danger">*</span>
                                    </label>
                                    <input type="password" class="form-control" name="confirm_new_password"
                                           id="confirm_new_password" required>
                                </div>
                                <div class="d-grid">
                                    <button class="btn bsb-btn-xl btn-primary" type="submit">Reset Password</button>
                                </div>
                            </form>
                        </div>

                        <div class="row mt-4">
                            <div class="col-12">
                                <div class="d-flex gap-2 gap-md-4 flex-column flex-md-row justify-content-md-end">
                                    <a href="login.html" class="link-secondary text-decoration-none">
                                        Back to Login
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<script>
    // Auto focus next input for code fields
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
    // Simple step navigation (for demo only)
    document.getElementById('emailForm').onsubmit = function (e) {
        e.preventDefault();
        document.getElementById('step1').style.display = 'none';
        document.getElementById('step2').style.display = '';
    };
    document.getElementById('codeForm').onsubmit = function (e) {
        e.preventDefault();
        document.getElementById('step2').style.display = 'none';
        document.getElementById('step3').style.display = '';
    };
    document.getElementById('resendCode').onclick = function (e) {
        e.preventDefault();
        alert('Verification code resent!');
    };
</script>
</body>

</html>
