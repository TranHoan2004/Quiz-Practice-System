let method = '';
let email;
const googleAuth = document.getElementById('googleauth-step1');
const step3 = document.getElementById('step3');
const step2 = document.getElementById('step2');

/**
 * Bước 1: Gửi mã xác thực đến email
 */
document.getElementById('send-code').addEventListener('click', function (e) {
    e.preventDefault();
    email = document.getElementById('reset_email').value;
    if (isValidEmail(email)) {
        fetch(`${window.contextPath}/user`, {
        method: 'POST', headers: {
            'Content-Type': 'application/json', 'Content': "email"
        }, body: JSON.stringify({email: email})
    })
        .then(response => {
            return response.text()
                .then(text => ({
                    status: response.status, data: text
                }));
        })
        .then(data => {
            if (data.status !== 200) {
                throw new Error(data.data);
            }
            step2.hidden = false;
            document.getElementById('step1').hidden = true;
        })
        .catch(err => {
            console.log(err);
            showNotification(err.message, "not success");
        });
    } else {
        showNotification('Email must follow the format', 'not success');
    }
});

function isValidEmail(email) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
}

/**
 * Bước 2: Người dùng chọn phương thức xác thực
 */
document.getElementById('continueBtn').addEventListener('click', function (e) {
    e.preventDefault();
    const authMethod = document.getElementById('auth_method').value;
    method = authMethod;

    if (authMethod === 'google_auth') {
        sendRequestToCreateQR();
        googleAuth.hidden = false;
    } else if (authMethod === 'magic_link') {
        sendRequestToSendEmail();
        document.getElementById('magicLinkMessageContainer').hidden = false;
    } else if (authMethod === 'otp') {
        sendRequestToCreateOtp();
        step3.hidden = false;
    }
    step2.hidden = true;
});

/**
 * Bước giữa Google Auth: xác nhận quét thành công để chuyển tiếp
 */
document.getElementById('scan-success').addEventListener('click', function (e) {
    e.preventDefault();
    googleAuth.hidden = true;
    step3.hidden = false;
    const div = document.getElementById('previous');
    if (method === 'google_auth') {
        div.innerHTML = `<a id="scan" class="link-primary text-decoration-none">Scan QR</a>`;
        document.getElementById('scan').addEventListener('click', function (e) {
            e.preventDefault();
            googleAuth.hidden = false;
            step3.hidden = true;
        });
    }
});

/**
 * Bước 3: Xác thực mã OTP (6 số) nhập từ người dùng
 */
document.getElementById('verify-code').addEventListener('click', function (e) {
    e.preventDefault();
    const codeInputs = document.querySelectorAll('#step2 input[type="text"]');
    const code = Array.from(codeInputs).map(input => input.value).join('');
    const key = method === 'google_auth' ? "otp" : "code";

    fetch(`${window.contextPath}/user`, {
        method: 'POST', headers: {
            'Content-Type': 'application/json', 'Content': method
        }, body: JSON.stringify({[key]: code})
    })
        .then(response => {
            return response.text()
                .then(text => ({
                    status: response.status, data: text
                }));
        })
        .then(data => {
            console.log(data);
            if (data.status !== 200) {
                throw new Error(data.data);
            }
            showNotification("Verify code successfully", "success");
            document.getElementById('step4').hidden = false;
            step3.hidden = true;
            googleAuth.hidden = true;
        })
        .catch(err => {
            console.log(err);
            showNotification(err.message, "not success");
            codeInputs.forEach(input => input.value = '');
        });
});

/**
 * Gửi lại mã OTP
 */
document.getElementById('resendCode').addEventListener('click', function (e) {
    e.preventDefault();
    sendRequestToCreateOtp();
});

/**
 * Tự động chuyển focus khi người dùng nhập 6 số OTP
 */
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

/**
 * Kiểm tra định dạng mật khẩu mới
 * @param {string} password
 * @returns {boolean}
 */
function validatePasswordFormat(password) {
    return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/.test(password);
}

/**
 * Chọn phương thức xác thực và cập nhật UI
 * @param {string} method
 */
function selectAuthMethod(method) {
    document.querySelectorAll('.btn-outline-primary').forEach(btn => {
        btn.classList.remove('active');
    });

    let query = '[data-method="' + method + '"]';
    const selectedBtn = document.querySelector(query);
    if (selectedBtn) {
        selectedBtn.classList.add('active');
    }

    document.getElementById('auth_method').value = method;
    document.getElementById('continueBtn').disabled = false;
}

/**
 * Gửi yêu cầu tạo OTP mới
 */
function sendRequestToCreateOtp() {
    fetch(`${window.contextPath}/user`, {
        method: 'POST', headers: {
            'Content-Type': 'application/json', 'Content': "sent_otp"
        }, body: JSON.stringify({status: true})
    })
        .then(response => {
            return response.text()
                .then(text => ({
                    status: response.status, data: text
                }));
        })
        .then(data => {
            if (data.status !== 200) {
                throw new Error(data.data);
            }
        })
        .catch(err => {
            showNotification(err.message, "not success");
        });
}

/**
 * Gửi yêu cầu tạo mã QR để cấu hình Google Authenticator
 */
function sendRequestToCreateQR() {
    fetch(`${window.contextPath}/user`, {
        method: 'POST', headers: {
            'Content-Type': 'application/json', 'Content': "qr"
        }, body: JSON.stringify({status: true})
    })
        .then(response => {
            return response.text()
                .then(text => ({
                    status: response.status, data: text
                }));
        })
        .then(data => {
            if (data.status !== 200) {
                throw new Error(data.data);
            }
            const qrCode = data.data.trim();
            document.querySelector('#googleauth-step1 img').src = 'data:image/png;base64,' + qrCode;
        })
        .catch(err => {
            showNotification(err.message, "not success");
        });
}

/**
 * Gửi magic link xác thực đến email
 */
function sendRequestToSendEmail() {
    fetch(`${window.contextPath}/user`, {
        method: 'POST', headers: {
            'Content-Type': 'application/json', 'Content': "magic_link"
        }, body: JSON.stringify({status: true})
    })
        .then(response => {
            return response.text()
                .then(text => ({
                    status: response.status, data: text
                }));
        })
        .then(data => {
            if (data.status !== 200) {
                throw new Error(data.data);
            }
        })
        .catch(err => {
            showNotification(err.message, "not success");
        });
}

(function () {
    let socket = null;
    let currentEmail = null;

    /**
     * Khởi tạo WebSocket và lắng nghe sự kiện xác thực magic link
     */
    function initWebSocket() {
        const path = `ws://localhost:8080${window.contextPath}/magic-link`;
        console.log('Initializing WebSocket:', path);

        socket = new WebSocket(path);

        socket.onopen = function () {
            console.log('WebSocket connected');
            if (currentEmail) {
                console.log('Resending email:', currentEmail);
                socket.send(currentEmail);
            }
        };

        socket.onmessage = function (event) {
            console.log('Received raw message:', event.data);

            switch (event.data) {
                case 'VALID_TOKEN':
                    console.log('Received VALID_TOKEN');
                    showNotification("Verify successfully", "success");
                    document.getElementById('magicLinkMessageContainer').hidden = true;
                    document.getElementById('step4').hidden = false;
                    break;

                case 'INVALID_TOKEN':
                    console.log('Received INVALID_TOKEN');
                    showNotification("Invalid token", "error");
                    break;

                case 'CONNECTED':
                    console.log('Server connection acknowledged');
                    break;

                default:
                    console.log('Unknown message:', event.data);
            }
        };

        socket.onerror = function (event) {
            console.error('WebSocket error:', event);
        };

        socket.onclose = function (event) {
            console.warn('WebSocket closed:', event);
            setTimeout(initWebSocket, 3000);
        };
    }

    document.addEventListener('DOMContentLoaded', initWebSocket);

    /**
     * Gửi lại email lên socket mỗi khi người dùng thay đổi địa chỉ
     */
    document.getElementById('reset_email')?.addEventListener('change', function () {
        currentEmail = this.value;
        console.log('Email changed to:', currentEmail);

        if (socket && socket.readyState === WebSocket.OPEN) {
            console.log('Immediately sending email:', currentEmail);
            socket.send(currentEmail);
        } else {
            console.log('Socket not ready, email will be sent when connected');
        }
    });
})();

/**
 * Xác thực và cập nhật mật khẩu mới
 */
document.getElementById('passwordForm').onsubmit = function (e) {
    e.preventDefault();
    const password = document.getElementById('new_password').value;
    const confirmPassword = document.getElementById('confirm_new_password').value;
    const passwordHelp = document.getElementById('passwordHelp');
    const confirmPasswordHelp = document.getElementById('confirmPasswordHelp');
    let valid = true;

    if (!validatePasswordFormat(password)) {
        passwordHelp.textContent = "Password must be at least 8 characters, include uppercase, lowercase, number, and special character.";
        passwordHelp.classList.remove('d-none');
        valid = false;
    } else {
        passwordHelp.classList.add('d-none');
    }

    if (password !== confirmPassword) {
        confirmPasswordHelp.textContent = "Passwords do not match.";
        confirmPasswordHelp.classList.remove('d-none');
        valid = false;
    } else {
        confirmPasswordHelp.classList.add('d-none');
    }

    if (valid) {
        console.log('Go here');
        fetch(`${window.contextPath}/user`, {
            method: 'PUT', headers: {
                'Content-Type': 'application/json'
            }, body: JSON.stringify({password: password})
        })
            .then(response => {
                return response.text()
                    .then(text => ({
                        status: response.status, data: text
                    }));
            })
            .then(data => {
                console.log(data);
                if (data.status !== 200) {
                    throw new Error(data.data);
                }
                showNotification("Reset successfully", "success");
                setTimeout(() => {
                    window.location.href = `login_account.jsp`;
                }, 3000);
            })
            .catch(err => {
                console.log(err);
                showNotification(err.message, "not success");
            });
    }
};