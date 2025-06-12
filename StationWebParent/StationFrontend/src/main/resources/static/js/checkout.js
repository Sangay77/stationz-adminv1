document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("checkoutForm");
    const submitButton = form.querySelector("button[type='submit']");
    const bankSection = document.getElementById("bankSelectionSection");
    const otpSection = document.getElementById("otpSection");
    const select = document.getElementById("bankSelect");
    const csrfToken = document.querySelector("input[name='_csrf']").value;

    form.addEventListener("submit", function (e) {
        e.preventDefault();

        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
        }

        submitButton.disabled = true;

        const data = {
            accountNumber: document.getElementById("accountNumber").value.trim(),
            bfs_paymentDesc: document.getElementById("bfs_paymentDesc").value.trim(),
            txnAmount: parseFloat(document.getElementById("txnAmount").value)
        };

        fetch("/authorise", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "X-CSRF-TOKEN": csrfToken
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) throw new Error("Network response was not ok");
                return response.json();
            })
            .then(result => {
                if (result.bfs_responseDesc?.toLowerCase() === "success") {
                    alert("✅ Payment authorised successfully!");

                    form.classList.add("d-none");
                    bankSection.classList.remove("d-none");

                    // Clear previous options except the placeholder
                    select.innerHTML = '<option value="">-- Select a Bank --</option>';

                    const bankList = result.bfs_bankList?.split("#") || [];
                    bankList.forEach(bank => {
                        const [code, name] = bank.split("~");
                        if(code && name) {
                            const option = document.createElement("option");
                            option.value = code;
                            option.textContent = `${name} (${code})`;
                            select.appendChild(option);
                        }
                    });

                    document.getElementById("txnIdDisplay").value = result.bfs_bfsTxnId || "";
                    document.getElementById("userAccountNumber").value = data.accountNumber;

                    form.reset();
                    form.classList.remove("was-validated");
                } else {
                    alert("❌ Payment failed: " + (result.bfs_responseDesc || "Unknown error."));
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("❌ Failed to authorise payment.");
            })
            .finally(() => {
                submitButton.disabled = false;
            });
    });

    // Proceed Button Logic (select bank & send enquiry)
    document.getElementById("proceedBtn").addEventListener("click", function () {
        const selectedBank = select.value;
        const accountNumber = document.getElementById("userAccountNumber").value.trim();
        const txnId = document.getElementById("txnIdDisplay").value.trim();

        if (!selectedBank || !accountNumber || !txnId) {
            alert("⚠️ Please complete all fields before proceeding.");
            return;
        }

        const payload = {
            bfs_remitterBankId: selectedBank,
            bfs_remitterAccNo: accountNumber,
            bfs_bfsTxnId: txnId
        };

        fetch("/enquiry", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "X-CSRF-TOKEN": csrfToken
            },
            body: JSON.stringify(payload)
        })
            .then(res => {
                if (!res.ok) throw new Error("Failed to confirm payment.");
                return res.json();
            })
            .then(resp => {
                if (resp.bfs_responseDesc?.toLowerCase() === "success") {
                    alert("✅ Payment step confirmed! Please enter your OTP.");

                    // Hide bank selection, show OTP input
                    bankSection.classList.add("d-none");
                    otpSection.classList.remove("d-none");
                } else {
                    alert("❌ Payment error: " + (resp.bfs_responseDesc || "Unknown error."));
                }
            })
            .catch(err => {
                console.error(err);
                alert("❌ Failed to complete payment.");
            });
    });

    // Verify OTP button logic
//    document.getElementById("verifyOtpBtn").addEventListener("click", function () {
//        const otpInput = document.getElementById("otpInput");
//        const otp = otpInput.value.trim();
//        if (otp.length !== 6) {
//            otpInput.classList.add("is-invalid");
//            alert("⚠️ Please enter a valid 6-digit OTP.");
//            return;
//        } else {
//            otpInput.classList.remove("is-invalid");
//        }
//
//        // Prepare OTP verification payload
//        const otpPayload = {
//            bfs_remitterOtp: otp,
//            bfs_bfsTxnId: document.getElementById("txnIdDisplay").value.trim()
//        };
//
//        fetch("/station/debit", {
//            method: "POST",
//            headers: {
//                "Content-Type": "application/json",
//                "X-CSRF-TOKEN": csrfToken
//            },
//            body: JSON.stringify(otpPayload)
//        })
//        .then(res => {
//            if (!res.ok) throw new Error("OTP verification failed");
//            return res.json();
//        })
//        .then(resp => {
//            if (resp.bfs_responseDesc?.toLowerCase() === "success") {
//                alert("✅ OTP verified successfully! Your payment is complete.");
//                otpSection.classList.add("d-none");
//                // Optionally redirect or show success page here
//            } else {
//                alert("❌ OTP verification failed: " + (resp.bfs_responseDesc || "Invalid OTP."));
//            }
//        })
//        .catch(err => {
//            console.error(err);
//            alert("❌ Failed to verify OTP.");
//        });
//    });

// Verify OTP button logic
document.getElementById("verifyOtpBtn").addEventListener("click", function () {
    const otpInput = document.getElementById("otpInput");
    const otp = otpInput.value.trim();
    if (otp.length !== 6) {
        otpInput.classList.add("is-invalid");
        alert("⚠️ Please enter a valid 6-digit OTP.");
        return;
    } else {
        otpInput.classList.remove("is-invalid");
    }

    // Prepare OTP verification payload
    const otpPayload = {
        bfs_remitterOtp: otp,
        bfs_bfsTxnId: document.getElementById("txnIdDisplay").value.trim()
    };

    fetch("/debit", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": csrfToken
        },
        body: JSON.stringify(otpPayload)
    })
    .then(res => {
        if (!res.ok) throw new Error("OTP verification failed");
        return res.json();
    })
    .then(resp => {
         if (resp.status === "success") {
                alert("✅ OTP verified successfully! Your payment is complete.");

                otpSection.classList.add("d-none");

                // Show the final success page/section
                const successSection = document.getElementById("paymentSuccessSection");
                successSection.classList.remove("d-none");
            } else {
                const failCode = resp.failedCode || "Invalid OTP";
                alert(`❌ OTP verification failed: ${failCode}`);
            }
    })
    .catch(err => {
        console.error(err);
        alert("❌ Failed to verify OTP.");
    });
});


});
