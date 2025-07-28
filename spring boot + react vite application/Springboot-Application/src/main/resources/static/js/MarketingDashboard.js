// --- DOM Elements and Data Initialization ---
const el = document.getElementById("dashboard-data");
const revenueMap = JSON.parse(el.dataset.revenueMap);
const ordersCountTrendAll = JSON.parse(el.dataset.ordersAll);
const ordersCountTrendSuccess = JSON.parse(el.dataset.ordersSuccess);
const ordersCountTrendDate = JSON.parse(el.dataset.ordersDate);
const apiUrl = el.dataset.apiUrl;

// --- Prompt Mapping ---
const promptMap = {
    marketer: `Role: Marketer – your goal is to optimize revenue and promotional strategies.
Current data:
- Total revenue: ${Number(el.dataset.totalRevenue).toLocaleString()}₫
- Revenue change compared to last period: ${el.dataset.changeRevenue}%
- Total successful orders: ${ordersCountTrendSuccess.reduce((a, b) => a + b, 0)} orders
- Top 3 highest-revenue courses: ${Object.entries(revenueMap).sort((a, b) => b[1] - a[1]).slice(0, 3).map(([subject, revenue]) => `${subject} (${Number(revenue).toLocaleString()}₫)`).join(', ')}

Task:
1. Identify which products (courses) generate the most revenue and why.
2. Should the marketing focus shift toward these top courses?
3. Do the low-revenue courses have potential?
4. Suggest marketing strategy adjustments for each group.
5. Answer concisely in 7–12 lines, avoid overly long responses.`,

    revenue: `Role: Data analyst – monitor order trends.

Overview:
- Total recorded orders: ${ordersCountTrendAll.reduce((a, b) => a + b, 0)}
- Peak order day: ${Math.max(...ordersCountTrendAll)} orders
- Number of days with no orders: ${ordersCountTrendAll.filter(x => x === 0).length} days

Analysis:
1. Are order trends increasing or decreasing?
2. When are the peak and low order times?
3. What factors are causing the fluctuations?
4. How to maintain growth momentum?
5. Answer concisely in 7–12 lines, avoid overly long responses.`,

    course: `Role: Analyze the effectiveness of new users.

Data:
- New users: ${el.dataset.newAccounts} (${el.dataset.changeNewAccounts}%)
- New orders: ${el.dataset.newBought} (${el.dataset.changeNewBought}%)
- Successful course enrollments: ${el.dataset.coursesSuccess}
- Top-revenue course: ${Object.entries(revenueMap).sort((a, b) => b[1] - a[1])[0][0]}

Please analyze:
1. Do new users have a high conversion rate?
2. Do they bring real value (revenue), or just trial signups?
3. Suggest nurturing strategies for new users.
4. Answer concisely in 7–12 lines, avoid overly long responses.`,

    timegap: `Role: Analyze buyer behavior over time.

Chart insights:
- Orders are not evenly distributed
- Some small spikes (2–3 orders), then stagnation
- Many days with no orders

Please analyze:
1. Are there patterns in buying time?
2. Are we missing “golden hours/days”?
3. Suggestions to fill the gaps – reminders, periodic promotions?
4. How to achieve a more consistent order flow?
5. Answer concisely in 7–12 lines, avoid overly long responses.`,

    gaps: `Role: E-commerce manager – ensure stable order flow.

Current situation:
- Many long gaps with no orders
- Order spike mid-month followed by sharp decline
- No sign of recovery in recent days

Required analysis:
1. What caused the order gaps?
2. What strategies can re-activate buying behavior?
3. Suggest marketing scenarios to compensate for lost sales periods.
4. Answer concisely in 7–12 lines, avoid overly long responses.`
};


// --- Chart Configurations ---
const orderChart = new Chart(document.getElementById('orderTrendChart'), {
    type: 'bar',
    data: {
        labels: ordersCountTrendDate,
        datasets: [
            {
                label: 'Order Count',
                data: ordersCountTrendAll,
                backgroundColor: 'rgba(6,187,204,0.5)',
                borderRadius: 8
            },
            {
                type: 'line',
                label: 'Order Trend',
                data: ordersCountTrendAll,
                borderColor: '#6366f1',
                backgroundColor: 'rgba(99,102,241,0.1)',
                borderWidth: 2,
                fill: false,
                tension: 0.4,
                pointRadius: 3,
                pointBackgroundColor: '#6366f1',
                order: 2
            }
        ]
    },
    options: {
        plugins: {legend: {display: false}},
        scales: {
            x: {
                ticks: {
                    maxRotation: 45,
                    minRotation: 45,
                    autoSkip: false,
                    align: 'start',
                    font: {size: 10}
                }
            },
            y: {beginAtZero: true}
        }
    },
    barPercentage: 0.6,
    categoryPercentage: 0.6
});

document.getElementById('orderType').addEventListener('change', function () {
    const selectedData = this.value === 'success' ? ordersCountTrendSuccess : ordersCountTrendAll;
    orderChart.data.datasets[0].data = selectedData;
    orderChart.data.datasets[1].data = selectedData;
    orderChart.update();
});

const revenueCategoryChart = new Chart(document.getElementById('revenueCategoryChart'), {
    type: 'pie',
    data: {
        labels: Object.keys(revenueMap),
        datasets: [{
                label: 'Revenue',
                data: Object.values(revenueMap),
                backgroundColor: [
                    'rgba(6,187,204,0.7)', 'rgba(24,29,56,0.7)', 'rgba(6,187,204,0.4)',
                    'rgba(255,193,7,0.7)', 'rgba(40,167,69,0.7)', 'rgba(255,99,132,0.7)',
                    'rgba(111,66,193,0.7)', 'rgba(13,202,240,0.7)', 'rgba(108,117,125,0.7)',
                    'rgba(220,53,69,0.7)'
                ],
                borderWidth: 1
            }]
    },
    options: {plugins: {legend: {display: false}}}
});

const legendContainer = document.getElementById('revenueCategoryLegend');
if (legendContainer) {
    legendContainer.innerHTML = revenueCategoryChart.data.labels.map((label, i) => `
        <li class="d-flex align-items-center mb-2">
            <span style="display:inline-block;width:16px;height:16px;background:${revenueCategoryChart.data.datasets[0].backgroundColor[i]};border-radius:3px;margin-right:8px;"></span>
            ${label}
            <span class="ms-auto text-muted small">${Number(revenueCategoryChart.data.datasets[0].data[i]).toLocaleString()}₫</span>
        </li>`).join('');
}

// Sidebar toggle
const sidebar = document.getElementById('sidebarNav');
const sidebarToggle = document.getElementById('sidebarToggle');
if (sidebarToggle)
    sidebarToggle.addEventListener('click', () => sidebar.classList.toggle('d-none'));

function renderChangePercent(containerId, percentValue) {
    const value = parseFloat(percentValue);
    const container = document.getElementById(containerId);
    if (!container || isNaN(value))
        return;
    const isPositive = value > 0;
    container.innerHTML = `
        <div class="small ${isPositive ? 'text-success' : 'text-danger'} d-flex justify-content-center align-items-center gap-1 mt-1">
            <i class="bi ${isPositive ? 'bi-arrow-up' : 'bi-arrow-down'}"></i>
            ${isPositive ? '+' : ''}${value.toFixed(2)}%
        </div>`;
}

// Chat logic
const chatPopup = document.getElementById("chatPopup");
const chatBox = document.getElementById("chatBox");
const chatForm = document.getElementById("chatForm");
const messageInput = document.getElementById("messageInput");
let previousMessage = "";
let fullText = "";
let botBubble = null;

function generateInsightContext() {
    const revenueMapSorted = Object.entries(revenueMap)
            .sort((a, b) => b[1] - a[1])
            .map(([subject, value]) => ({subject, revenue: value}));

    const revenueMapText = revenueMapSorted
            .map(item => `${item.subject}: ${Number(item.revenue).toLocaleString()}₫`)
            .join(", ");

    return {
        totalRevenue: el.dataset.totalRevenue,
        totalRevenueText: `Current total revenue is ${Number(el.dataset.totalRevenue).toLocaleString()}₫`,
        changeRevenue: el.dataset.changeRevenue,
        changeRevenueText: `Revenue change compared to last period is ${el.dataset.changeRevenue}%`,
        revenueMap,
        revenueMapSorted,
        revenueMapText,

        revenueInsight: {
            total: el.dataset.totalRevenue,
            change: el.dataset.changeRevenue,
            map: revenueMap,
            mapSorted: revenueMapSorted
        },

        newSubjects: el.dataset.newSubjects,
        newSubjectsText: `New subjects created: ${el.dataset.newSubjects}`,
        allSubjects: el.dataset.allSubjects,
        allSubjectsText: `Total subjects in the system: ${el.dataset.allSubjects}`,

        coursesSuccess: el.dataset.coursesSuccess,
        coursesSuccessText: `Successful enrollments: ${el.dataset.coursesSuccess}`,
        coursesCancel: el.dataset.coursesCancel,
        coursesCancelText: `Cancelled enrollments: ${el.dataset.coursesCancel}`,
        coursesSummited: el.dataset.coursesSummited,
        coursesSummitedText: `New courses created: ${el.dataset.coursesSummited}`,

        changeNewSubjects: el.dataset.changeNewSubjects,
        changeNewSubjectsText: `New subject change rate: ${el.dataset.changeNewSubjects}%`,
        changeCoursesSuccess: el.dataset.changeCoursesSuccess,
        changeCoursesSuccessText: `Successful enrollments change rate: ${el.dataset.changeCoursesSuccess}%`,
        changeCoursesCancel: el.dataset.changeCoursesCancel,
        changeCoursesCancelText: `Canceled enrollments change rate: ${el.dataset.changeCoursesCancel}%`,
        changeCoursesSummited: el.dataset.changeCoursesSummited,
        changeCoursesSummitedText: `New course creation rate: ${el.dataset.changeCoursesSummited}%`,

        coursesInsight: {
            success: el.dataset.coursesSuccess,
            cancel: el.dataset.coursesCancel,
            summited: el.dataset.coursesSummited,
            changeSuccess: el.dataset.changeCoursesSuccess,
            changeCancel: el.dataset.changeCoursesCancel,
            changeSummited: el.dataset.changeCoursesSummited
        },

        newAccounts: el.dataset.newAccounts,
        newAccountsText: `New user accounts: ${el.dataset.newAccounts}`,
        changeNewAccounts: el.dataset.changeNewAccounts,
        changeNewAccountsText: `New user change rate: ${el.dataset.changeNewAccounts}%`,

        userInsight: {
            newAccounts: el.dataset.newAccounts,
            changeNewAccounts: el.dataset.changeNewAccounts
        },

        newBought: el.dataset.newBought,
        newBoughtText: `New orders: ${el.dataset.newBought}`,
        changeNewBought: el.dataset.changeNewBought,
        changeNewBoughtText: `New order change rate: ${el.dataset.changeNewBought}%`,

        ordersInsight: {
            newOrders: el.dataset.newBought,
            changeNewOrders: el.dataset.changeNewBought
        },

        ordersCountTrendAll,
        ordersCountTrendSuccess,
        ordersCountTrendDate
    };
}

function toggleChat() {
    const isOpen = chatPopup.style.display === "flex";
    chatPopup.style.display = isOpen ? "none" : "flex";

    if (!isOpen && chatBox.innerHTML.trim() === "") {
        chatBox.innerHTML = "";
        fullText = "";
        previousMessage = "";

        const welcomeMsg = document.createElement("div");
        welcomeMsg.className = "bubble bot";
        welcomeMsg.innerText = "Can I help you? You can ask about revenue, users, or courses.";
        chatBox.appendChild(welcomeMsg);
        scrollToBottom();
    }
}


chatForm.addEventListener("submit", function (e) {
    e.preventDefault();
    const message = messageInput.value.trim();
    if (!message)
        return;

    appendMessage(message, "user");
    messageInput.value = "";

    botBubble = document.createElement("div");
    botBubble.className = "bubble bot";
    botBubble.innerText = "Miss is typing...";
    chatBox.appendChild(botBubble);
    scrollToBottom();

    fullText = "";
    previousMessage = message;

    fetch(apiUrl, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            prompt: message,
            previousMessage,
            insightContext: JSON.stringify(generateInsightContext())
        })
    })
            .then(response => {
                const reader = response.body?.getReader?.();
                const decoder = new TextDecoder("utf-8");
                if (!reader) {
                    botBubble.innerText = "Miss did not respond (no stream).";
                    return;
                }

                return readStream("", reader, decoder, botBubble);
            })
            .catch(() => {
                botBubble.innerText = "Sorry, Miss encountered an error while responding.";
            });
});

async function readStream(buffer, reader, decoder, targetDiv) {
    try {
        targetDiv.innerText = "";
        while (true) {
            const {done, value} = await reader.read();
            if (done)
                break;
            const chunk = decoder.decode(value, {stream: true});
            buffer += chunk;

            const lines = buffer.split("\n").filter(line => line.trim());
            buffer = ""; // reset buffer after splitting

            for (const line of lines) {
                try {
                    const parsed = JSON.parse(line);
                    if (parsed?.response) {
                        fullText += parsed.response;
                        targetDiv.innerText += parsed.response;
                        scrollToBottom();
                    }
                } catch {
                    buffer += line; // keep incomplete line
                }
            }
        }

        targetDiv.innerText = fullText;
        scrollToBottom();
    } catch (err) {
        console.error("Stream processing failed:", err);
        targetDiv.innerText = "Miss encountered an error while processing the response.";
    }
}

function appendMessage(text, type) {
    const msg = document.createElement("div");
    msg.className = `bubble ${type}`;
    msg.innerHTML = text.replaceAll("\n", "<br>");
    chatBox.appendChild(msg);
    scrollToBottom();
}

function sendDetailedPrompt(type) {
    if (chatPopup.style.display !== "flex") {
        toggleChat();
        setTimeout(() => sendDetailedPrompt(type), 300);
        return;
    }

    const prompt = promptMap[type] || "Please analyze the current data and suggest suitable marketing strategies.";
    appendMessage(prompt.split("\n")[0], "user");

    messageInput.value = "";
    const botBubble = document.createElement("div");
    botBubble.className = "bubble bot";
    botBubble.innerText = "Miss is typing...";
    chatBox.appendChild(botBubble);
    scrollToBottom();

    fullText = "";
    previousMessage = prompt;

    fetch(apiUrl, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            prompt,
            previousMessage,
            insightContext: JSON.stringify(generateInsightContext())
        })
    })
            .then(response => {
                const reader = response.body.getReader();
                const decoder = new TextDecoder("utf-8");
                return readStream("", reader, decoder, botBubble);
            })
            .catch(() => {
                botBubble.innerText = "Sorry, Miss encountered an error while responding.";
            });
}

function handleSuggestionClick(type) {
    sendDetailedPrompt(type);
}

function scrollToBottom() {
    chatBox.scrollTop = chatBox.scrollHeight;
}
