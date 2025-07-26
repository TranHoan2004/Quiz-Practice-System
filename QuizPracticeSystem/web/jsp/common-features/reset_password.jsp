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
        <link href="${pageContext.request.contextPath}/css/reset_password.css" rel="stylesheet">
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
                                    <div id="emailForm">
                                        <div class="mb-3">
                                            <label for="reset_email" class="form-label">Email
                                                <span class="text-danger">*</span>
                                            </label>
                                            <input type="email" class="form-control" name="email" id="reset_email"
                                                   placeholder="name@example.com" required>
                                        </div>
                                        <div class="d-grid">
                                            <button class="btn bsb-btn-xl btn-primary" type="button" id="send-code">
                                                Send Code
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <!-- Step 2: Choose method -->
                                <div id="step2" hidden>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="mb-4 text-center">
                                                <h3>Choose Authentication Method</h3>
                                                <p class="text-secondary">Select how you want to authenticate.</p>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <form id="authMethodForm">
                                                <div class="mb-3">
                                                    <div class="d-flex flex-column align-items-stretch gap-2">
                                                        <button
                                                            data-method="google_auth"
                                                            class="btn btn-outline-primary text-start d-flex align-items-center"
                                                            type="button" onclick="selectAuthMethod('google_auth')">
                                                            <i class="bi bi-google me-2"></i>
                                                            <span class="flex-grow-1 text-center">Google Authenticator</span>
                                                        </button>
                                                        <button
                                                            data-method="magic_link"
                                                            class="btn btn-outline-primary text-start d-flex align-items-center"
                                                            type="button" onclick="selectAuthMethod('magic_link')">
                                                            <i class="bi bi-link-45deg me-2"></i>
                                                            <span class="flex-grow-1 text-center">Magic Link</span>
                                                        </button>
                                                        <button
                                                            data-method="otp"
                                                            class="btn btn-outline-primary text-start d-flex align-items-center"
                                                            type="button" onclick="selectAuthMethod('otp')">
                                                            <i class="bi bi-one-time-password me-2"></i>
                                                            <span class="flex-grow-1 text-center">OTP 6-digit</span>
                                                        </button>
                                                    </div>
                                                    <input type="hidden" id="auth_method" name="auth_method" value="">
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <div class="d-grid">
                                        <button class="btn bsb-btn-xl btn-primary" type="submit" disabled
                                                id="continueBtn">Continue
                                        </button>
                                    </div>
                                </div>

                                <!-- Google Authenticator - step 1 -->
                                <div id="googleauth-step1" hidden>
                                    <div class="mb-4 text-center">
                                        <h3>Scan Authenticator QR</h3>
                                        <p class="text-secondary">Please enter the 6-digit code sent to your email.</p>
                                    </div>
                                    <div class="text-center mb-4">
                                        <img src="" alt="Authenticator QR Code"
                                             class="img-fluid">
                                    </div>
                                    <div class="d-grid">
                                        <button class="btn bsb-btn-xl btn-primary" type="submit" id="scan-success">
                                            Continue
                                        </button>
                                    </div>
                                </div>

                                <!-- Step 3: Verify 6-digit Code -->
                                <div id="step3" hidden>
                                    <div id="step2">
                                        <div class="mb-4 text-center">
                                            <h3>Enter Verification Code</h3>
                                            <p class="text-secondary">Please enter the 6-digit code sent to your email.</p>
                                        </div>
                                        <form id="codeForm">
                                            <div class="mb-3">
                                                <div class="d-flex justify-content-center gap-2">
                                                    <input type="text" class="form-control text-center" maxlength="1"
                                                           pattern="\d" inputmode="numeric" name="1"
                                                           style="width: 48px; font-size: 1.5rem; border-width: 2px;"
                                                           required>
                                                    <input type="text" class="form-control text-center" maxlength="1"
                                                           pattern="\d" inputmode="numeric" name="2"
                                                           style="width: 48px; font-size: 1.5rem; border-width: 2px;"
                                                           required>
                                                    <input type="text" class="form-control text-center" maxlength="1"
                                                           pattern="\d" inputmode="numeric" name="3"
                                                           style="width: 48px; font-size: 1.5rem; border-width: 2px;"
                                                           required>
                                                    <input type="text" class="form-control text-center" maxlength="1"
                                                           pattern="\d" inputmode="numeric" name="4"
                                                           style="width: 48px; font-size: 1.5rem; border-width: 2px;"
                                                           required>
                                                    <input type="text" class="form-control text-center" maxlength="1"
                                                           pattern="\d" inputmode="numeric" name="5"
                                                           style="width: 48px; font-size: 1.5rem; border-width: 2px;"
                                                           required>
                                                    <input type="text" class="form-control text-center" maxlength="1"
                                                           pattern="\d" inputmode="numeric" name="6"
                                                           style="width: 48px; font-size: 1.5rem; border-width: 2px;"
                                                           required>
                                                </div>
                                            </div>
                                            <div class="d-grid">
                                                <button class="btn bsb-btn-xl btn-primary" type="submit" id="verify-code">
                                                    Verify Code
                                                </button>
                                            </div>
                                            <div class="mt-3 text-center" id="previous">
                                                <a id="resendCode"
                                                   class="link-primary text-decoration-none">
                                                    Resend Code
                                                </a>
                                            </div>
                                        </form>
                                    </div>
                                </div>

                                <!-- Step 4: Enter New Password -->
                                <div id="step4" hidden>
                                    <div class="mb-4 text-center">
                                        <h3>Set New Password</h3>
                                        <p class="text-secondary">Enter your new password below.</p>
                                    </div>
                                    <form id="passwordForm">
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
                                </div>

                                <!-- Thong bao cho Magic link -->
                                <div id="magicLinkMessageContainer" hidden>
                                    <div class="mb-4 text-center">
                                        <h3>Magic Link Sent</h3>
                                        <p class="text-secondary">A magic link has been sent to your email. Please check your
                                            inbox to proceed.</p>
                                    </div>
                                </div>

                                <div class="row mt-4">
                                    <div class="col-12">
                                        <div class="d-flex gap-2 gap-md-4 flex-column flex-md-row justify-content-md-end">
                                            <a href="${pageContext.request.contextPath}/user/login"
                                               class="link-secondary text-decoration-none">
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

        <jsp:include page="../../component/notification.html"/>

        <script src="${pageContext.request.contextPath}/js/Notification.js"></script>
        <script>
            window.contextPath = '${pageContext.request.contextPath}';
        </script>
        <script src="${pageContext.request.contextPath}/js/ResetPassword.js"></script>
    </body>

</html>
