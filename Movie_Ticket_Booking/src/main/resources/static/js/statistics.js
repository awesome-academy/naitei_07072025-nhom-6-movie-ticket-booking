// Statistics Dashboard JavaScript - Original với debug
let movieChart, bookingTimeChart, revenueTimeChart, revenueChart;
let currentMovieData = [];
let currentTimeSlotData = [];
let currentRevenueData = [];

// Initialize page
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, initializing...');
    initializeCharts();
    updateCurrentDate();
    updateStatistics();

    // Event listeners
    document.querySelectorAll('input[name="periodOptions"]').forEach(radio => {
        radio.addEventListener('change', updateRevenueChart);
    });

    // Auto-refresh every 5 minutes
    setInterval(autoRefresh, 5 * 60 * 1000);
});

function initializeCharts() {
    console.log('Starting chart initialization...');

    // Movie Statistics Chart
    const movieCtx = document.getElementById('movieChart');
    console.log('Movie chart canvas found:', movieCtx ? 'YES' : 'NO');

    if (movieCtx) {
        movieChart = new Chart(movieCtx, {
            type: 'bar',
            data: {
                labels: [],
                datasets: [{
                    label: 'Doanh thu (VNĐ)',
                    data: [],
                    backgroundColor: 'rgba(0, 123, 255, 0.8)',
                    borderColor: 'rgba(0, 123, 255, 1)',
                    borderWidth: 2,
                    borderRadius: 8,
                    borderSkipped: false,
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return 'Doanh thu: ' + formatCurrency(context.parsed.y);
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return formatCurrency(value);
                            }
                        }
                    },
                    x: {
                        ticks: {
                            maxRotation: 45,
                            minRotation: 0
                        }
                    }
                },
                animation: {
                    duration: 1000,
                    easing: 'easeInOutQuart'
                }
            }
        });
        console.log('Movie chart initialized successfully');
    }

    // Booking Time Slot Chart
    const bookingTimeCtx = document.getElementById('bookingTimeChart');
    console.log('Booking time chart canvas found:', bookingTimeCtx ? 'YES' : 'NO');

    if (bookingTimeCtx) {
        bookingTimeChart = new Chart(bookingTimeCtx, {
            type: 'pie',
            data: {
                labels: [],
                datasets: [{
                    data: [],
                    backgroundColor: [
                        'rgba(255, 193, 7, 0.8)',
                        'rgba(0, 123, 255, 0.8)',
                        'rgba(108, 117, 125, 0.8)'
                    ],
                    borderColor: [
                        'rgba(255, 193, 7, 1)',
                        'rgba(0, 123, 255, 1)',
                        'rgba(108, 117, 125, 1)'
                    ],
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 20,
                            usePointStyle: true
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return context.label + ': ' + context.parsed + '%';
                            }
                        }
                    }
                },
                animation: {
                    duration: 1000,
                    easing: 'easeInOutQuart'
                }
            }
        });
        console.log('Booking time chart initialized successfully');
    }

    // Revenue Time Slot Chart
    const revenueTimeCtx = document.getElementById('revenueTimeChart');
    console.log('Revenue time chart canvas found:', revenueTimeCtx ? 'YES' : 'NO');

    if (revenueTimeCtx) {
        revenueTimeChart = new Chart(revenueTimeCtx, {
            type: 'doughnut',
            data: {
                labels: [],
                datasets: [{
                    data: [],
                    backgroundColor: [
                        'rgba(40, 167, 69, 0.8)',
                        'rgba(23, 162, 184, 0.8)',
                        'rgba(220, 53, 69, 0.8)'
                    ],
                    borderColor: [
                        'rgba(40, 167, 69, 1)',
                        'rgba(23, 162, 184, 1)',
                        'rgba(220, 53, 69, 1)'
                    ],
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 20,
                            usePointStyle: true
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return context.label + ': ' + context.parsed + '%';
                            }
                        }
                    }
                },
                cutout: '50%',
                animation: {
                    duration: 1000,
                    easing: 'easeInOutQuart'
                }
            }
        });
        console.log('Revenue time chart initialized successfully');
    }

    // Revenue Trend Chart
    const revenueCtx = document.getElementById('revenueChart');
    console.log('Revenue chart canvas found:', revenueCtx ? 'YES' : 'NO');

    if (revenueCtx) {
        revenueChart = new Chart(revenueCtx, {
            type: 'line',
            data: {
                labels: [],
                datasets: [{
                    label: 'Doanh thu',
                    data: [],
                    borderColor: 'rgba(0, 123, 255, 1)',
                    backgroundColor: 'rgba(0, 123, 255, 0.1)',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4,
                    pointBackgroundColor: 'rgba(0, 123, 255, 1)',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2,
                    pointRadius: 6,
                    pointHoverRadius: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return 'Doanh thu: ' + formatCurrency(context.parsed.y);
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return formatCurrency(value);
                            }
                        }
                    }
                },
                animation: {
                    duration: 1000,
                    easing: 'easeInOutQuart'
                }
            }
        });
        console.log('Revenue chart initialized successfully');
    }
}

function loadCinemas() {
    fetch("/api/cinemas")
        .then(response => response.json())
        .then(data => {
            let select = document.getElementById("cinemaSelect");
            select.innerHTML = '<option value="">-- Tất cả rạp --</option>';
            data.forEach(c => {
                let option = document.createElement("option");
                option.value = c.id;
                option.text = c.name;
                select.add(option);
            });
        })
        .catch(error => console.error("Error loading cinemas:", error));
}

function updateCurrentDate() {
    const now = new Date();
    const options = {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    };
    const formatted = now.toLocaleDateString('vi-VN', options);
    const currentDateElement = document.getElementById('currentDate');
    if (currentDateElement) {
        currentDateElement.textContent = formatted;
    }
}

function updateStatistics() {
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;
        const cinemaId = document.getElementById('cinemaSelect').value;

        console.log('updateStatistics called with:', { startDate, endDate, cinemaId });

        if (!startDate || !endDate) {
            showAlert('warning', 'Vui lòng chọn khoảng thời gian');
            return;
        }

        if (new Date(startDate) > new Date(endDate)) {
            showAlert('error', 'Ngày bắt đầu không thể lớn hơn ngày kết thúc');
            return;
        }

        showLoadingStates();

        Promise.all([
            fetchMovieStatistics(startDate, endDate, cinemaId),
            fetchTimeSlotStatistics(startDate, endDate, cinemaId),
            fetchRevenueStatistics('day', startDate, endDate, cinemaId)
        ])
            .then(([movieData, timeSlotData, revenueData]) => {
                currentMovieData = movieData;
                currentTimeSlotData = timeSlotData;
                currentRevenueData = revenueData;

                updateSummaryCards(movieData, timeSlotData, revenueData);
                updateMovieChart(movieData);
                updateTimeSlotCharts(timeSlotData);
                updateRevenueChart();

                hideLoadingStates();
                showAlert('success', 'Dữ liệu đã được cập nhật');
            })
            .catch(error => {
                console.error('Error fetching statistics:', error);
                hideLoadingStates();
                showAlert('error', 'Có lỗi xảy ra khi tải dữ liệu');
            });
}

function fetchMovieStatistics(startDate, endDate, cinemaID) {
    let url = `/manager/statistics/movies?startDate=${startDate}&endDate=${endDate}`;
    if (cinemaID && cinemaID.trim() !== "") {
        url += `&cinemaId=${cinemaID}`;
    }
    return fetch(url)
        .then(response => {
            console.log('Movie statistics response:', response.status, response.statusText);
            if (!response.ok) throw new Error('Failed to fetch movie statistics');
            return response.json();
        })
        .then(data => {
            console.log('Movie statistics data received:', data);
            return data;
        });
}

function fetchTimeSlotStatistics(startDate, endDate, cinemaID) {
    let url = `/manager/statistics/timeslots?startDate=${startDate}&endDate=${endDate}`;
    if (cinemaID && cinemaID.trim() !== "") {
        url += `&cinemaId=${cinemaID}`;
    }

    return fetch(url)
        .then(response => {
            console.log('Timeslot statistics response:', response.status, response.statusText);
            if (!response.ok) throw new Error('Failed to fetch time slot statistics');
            return response.json();
        })
        .then(data => {
            console.log('Timeslot statistics data received:', data);
            return data;
        });
}

function fetchRevenueStatistics(period, startDate, endDate, cinemaID) {
    let url = `/manager/statistics/revenue?period=${period}&startDate=${startDate}&endDate=${endDate}`;
    if (cinemaID && cinemaID.trim() !== "") {
        url += `&cinemaId=${cinemaID}`;
    }

    return fetch(url)
        .then(response => {
            console.log('Revenue statistics response:', response.status, response.statusText);
            if (!response.ok) throw new Error('Failed to fetch revenue statistics');
            return response.json();
        })
        .then(data => {
            console.log('Revenue statistics data received:', data);
            return data;
        });
}

function updateSummaryCards(movieData, timeSlotData, revenueData) {
    console.log('Updating summary cards with data:', movieData, timeSlotData, revenueData);

    // Calculate totals
    const totalRevenue = movieData.reduce((sum, item) => sum + (item.totalRevenue || 0), 0);
    const totalTickets = movieData.reduce((sum, item) => sum + (item.totalTickets || 0), 0);
    const totalMovies = movieData.length;
    const avgTicketPrice = totalTickets > 0 ? totalRevenue / totalTickets : 0;

    console.log('Calculated totals:', { totalRevenue, totalTickets, totalMovies, avgTicketPrice });

    // Update cards
    updateElement('totalRevenue', formatCurrency(totalRevenue));
    updateElement('totalTickets', totalTickets.toLocaleString('vi-VN'));
    updateElement('totalMovies', totalMovies.toLocaleString('vi-VN'));
    updateElement('avgTicketPrice', formatCurrency(avgTicketPrice));

    // Animate numbers
    animateNumber('totalRevenue', 0, totalRevenue, formatCurrency);
    animateNumber('totalTickets', 0, totalTickets, (val) => Math.round(val).toLocaleString('vi-VN'));
    animateNumber('totalMovies', 0, totalMovies, (val) => Math.round(val).toLocaleString('vi-VN'));
    animateNumber('avgTicketPrice', 0, avgTicketPrice, formatCurrency);
}

function updateMovieChart(data) {
    console.log('updateMovieChart called with data:', data);
    console.log('movieChart exists:', movieChart ? 'YES' : 'NO');

    if (!movieChart || !data || data.length === 0) return;

    const top10Movies = data.slice(0, 10);
    const labels = top10Movies.map(item => truncateText(item.movieTitle, 15));
    const revenues = top10Movies.map(item => item.totalRevenue || 0);

    console.log('Chart will update with:', { labels, revenues });

    movieChart.data.labels = labels;
    movieChart.data.datasets[0].data = revenues;
    movieChart.update('active');

    // Update table
    updateMovieTable(data);
}

function updateMovieTable(data) {
    console.log('updateMovieTable called with data:', data);
    const tbody = document.getElementById('movieTableBody');
    if (!tbody) return;

    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3" class="text-center text-muted">Không có dữ liệu</td></tr>';
        return;
    }

    tbody.innerHTML = data.slice(0, 15).map(item => `
        <tr>
            <td title="${item.movieTitle}">
                ${truncateText(item.movieTitle, 20)}
                <small class="text-muted d-block">${item.cinemaName || ''}</small>
            </td>
            <td class="text-center">
                <span class="badge bg-primary">${(item.totalTickets || 0).toLocaleString('vi-VN')}</span>
            </td>
            <td class="text-end">
                <strong>${formatCurrency(item.totalRevenue || 0)}</strong>
            </td>
        </tr>
    `).join('');
}

function updateTimeSlotCharts(data) {
    console.log('updateTimeSlotCharts called with data:', data);

    if (!data || data.length === 0) {
        updateTimeSlotTable([]);
        return;
    }

    // Update booking percentage chart
    if (bookingTimeChart) {
        const labels = data.map(item => item.timeSlot);
        const bookingPercentages = data.map(item => item.bookingPercentage || 0);

        bookingTimeChart.data.labels = labels;
        bookingTimeChart.data.datasets[0].data = bookingPercentages;
        bookingTimeChart.update('active');
    }

    // Update revenue percentage chart
    if (revenueTimeChart) {
        const revenuePercentages = data.map(item => item.revenuePercentage || 0);

        revenueTimeChart.data.labels = data.map(item => item.timeSlot);
        revenueTimeChart.data.datasets[0].data = revenuePercentages;
        revenueTimeChart.update('active');
    }

    // Update table
    updateTimeSlotTable(data);
}

function updateTimeSlotTable(data) {
    const tbody = document.getElementById('timeSlotTableBody');
    if (!tbody) return;

    if (!data || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">Không có dữ liệu</td></tr>';
        return;
    }

    tbody.innerHTML = data.map(item => `
        <tr>
            <td><strong>${item.timeSlot}</strong></td>
            <td class="text-center">
                <span class="badge bg-info">${(item.totalBookings || 0).toLocaleString('vi-VN')}</span>
            </td>
            <td class="text-center">
                <div class="progress" style="height: 20px;">
                    <div class="progress-bar bg-warning" style="width: ${item.bookingPercentage || 0}%">
                        ${(item.bookingPercentage || 0).toFixed(1)}%
                    </div>
                </div>
            </td>
            <td class="text-end">
                <strong>${formatCurrency(item.totalRevenue || 0)}</strong>
            </td>
            <td class="text-center">
                <div class="progress" style="height: 20px;">
                    <div class="progress-bar bg-success" style="width: ${item.revenuePercentage || 0}%">
                        ${(item.revenuePercentage || 0).toFixed(1)}%
                    </div>
                </div>
            </td>
        </tr>
    `).join('');
}

function updateRevenueChart() {
    const period = document.querySelector('input[name="periodOptions"]:checked').value;
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
    const cinemaId = document.getElementById('cinemaSelect').value;

    if (!startDate || !endDate) return;

    fetchRevenueStatistics(period, startDate, endDate, cinemaId)
        .then(data => {
            if (!revenueChart || !data || data.length === 0) return;

            const labels = data.map(item => item.label);
            const revenues = data.map(item => item.totalRevenue || 0);

            revenueChart.data.labels = labels;
            revenueChart.data.datasets[0].data = revenues;
            revenueChart.update('active');
        })
        .catch(error => {
            console.error('Error updating revenue chart:', error);
        });
}

function showLoadingStates() {
    const loadingElements = [
        'movieTableBody',
        'timeSlotTableBody'
    ];

    loadingElements.forEach(id => {
        const element = document.getElementById(id);
        if (element) {
            element.innerHTML = '<tr><td colspan="5" class="loading">Đang tải...</td></tr>';
        }
    });
}

function hideLoadingStates() {
    // Loading will be hidden when data is updated
}

function exportData() {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (!startDate || !endDate) {
        showAlert('warning', 'Vui lòng chọn khoảng thời gian trước khi xuất dữ liệu');
        return;
    }

    // Create CSV data
    let csvContent = "data:text/csv;charset=utf-8,";

    // Movie statistics
    csvContent += "THỐNG KÊ THEO PHIM\n";
    csvContent += "Tên phim,Số vé bán,Doanh thu,Thể loại,Rạp\n";
    currentMovieData.forEach(item => {
        csvContent += `"${item.movieTitle}",${item.totalTickets},${item.totalRevenue},"${item.movieGenre}","${item.cinemaName}"\n`;
    });

    csvContent += "\n\nTHỐNG KÊ THEO KHUNG GIỜ\n";
    csvContent += "Khung giờ,Số lượt đặt,Tỷ lệ đặt vé (%),Doanh thu,Tỷ lệ doanh thu (%)\n";
    currentTimeSlotData.forEach(item => {
        csvContent += `"${item.timeSlot}",${item.totalBookings},${item.bookingPercentage},${item.totalRevenue},${item.revenuePercentage}\n`;
    });

    // Download file
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", `thong-ke-doanh-thu-${startDate}-${endDate}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    showAlert('success', 'Đã xuất dữ liệu thành công');
}

// Utility functions
function formatCurrency(amount) {
    if (amount == null || isNaN(amount)) return '0 VNĐ';
    return new Intl.NumberFormat('vi-VN').format(Math.round(amount)) + ' VNĐ';
}

function truncateText(text, maxLength) {
    if (!text) return '';
    return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
}

function updateElement(id, value) {
    const element = document.getElementById(id);
    if (element) {
        element.textContent = value;
    }
}

function animateNumber(elementId, start, end, formatter) {
    const element = document.getElementById(elementId);
    if (!element) return;

    const duration = 1000; // 1 second
    const startTime = performance.now();

    function updateNumber(currentTime) {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);
        const current = start + (end - start) * easeOutQuart(progress);

        element.textContent = formatter ? formatter(current) : current.toFixed(0);

        if (progress < 1) {
            requestAnimationFrame(updateNumber);
        }
    }

    requestAnimationFrame(updateNumber);
}

function easeOutQuart(t) {
    return 1 - (--t) * t * t * t;
}

function showAlert(type, message) {
    console.log('Alert:', type, message);

    // Create alert element
    const alertElement = document.createElement('div');
    alertElement.className = `alert alert-${type === 'error' ? 'danger' : type} alert-dismissible fade show position-fixed`;
    alertElement.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    alertElement.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    document.body.appendChild(alertElement);

    // Auto remove after 5 seconds
    setTimeout(() => {
        if (alertElement.parentNode) {
            alertElement.remove();
        }
    }, 5000);
}

function autoRefresh() {
    const now = new Date();
    const hours = now.getHours();

    // Only auto-refresh during business hours (8 AM - 11 PM)
    if (hours >= 8 && hours <= 23) {
        updateStatistics();
    }
}

// Date validation
function validateDateRange() {
    const startDate = document.getElementById('startDate');
    const endDate = document.getElementById('endDate');

    if (startDate && endDate) {
        const start = new Date(startDate.value);
        const end = new Date(endDate.value);
        const today = new Date();

        // Set max date to today
        const todayString = today.toISOString().split('T')[0];
        startDate.max = todayString;
        endDate.max = todayString;

        // Validate range
        if (start > end) {
            endDate.value = startDate.value;
        }

        // Maximum range is 1 year
        const oneYearAgo = new Date(today);
        oneYearAgo.setFullYear(today.getFullYear() - 1);
        const oneYearAgoString = oneYearAgo.toISOString().split('T')[0];
        startDate.min = oneYearAgoString;
        endDate.min = oneYearAgoString;
    }
}

// Add event listeners for date validation
document.addEventListener('DOMContentLoaded', function() {
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');

    if (startDateInput) {
        startDateInput.addEventListener('change', validateDateRange);
    }

    if (endDateInput) {
        endDateInput.addEventListener('change', validateDateRange);
    }

    // Initial validation
    validateDateRange();
});

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    // Ctrl + R: Refresh data
    if (e.ctrlKey && e.key === 'r') {
        e.preventDefault();
        updateStatistics();
    }

    // Ctrl + E: Export data
    if (e.ctrlKey && e.key === 'e') {
        e.preventDefault();
        exportData();
    }
});

// Handle window resize for charts
window.addEventListener('resize', function() {
    if (movieChart) movieChart.resize();
    if (bookingTimeChart) bookingTimeChart.resize();
    if (revenueTimeChart) revenueTimeChart.resize();
    if (revenueChart) revenueChart.resize();
});

// Error handling for failed chart initialization
function handleChartError(chartName, error) {
    console.error(`Error initializing ${chartName}:`, error);
    showAlert('error', `Không thể tải biểu đồ ${chartName}`);
}

// Print functionality
function printReport() {
    const printContent = document.querySelector('[th\\:fragment="content"]');
    if (!printContent) return;

    const printWindow = window.open('', '_blank');
    printWindow.document.write(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Báo cáo Thống kê Doanh thu</title>
            <link href="/bootstrap/css/bootstrap.min.css" rel="stylesheet">
            <link href="/css/statistics.css" rel="stylesheet">
            <style>
                @media print {
                    .no-print { display: none !important; }
                    .chart-container { height: 300px !important; }
                    body { font-size: 12px; }
                }
            </style>
        </head>
        <body>
            ${printContent.outerHTML}
        </body>
        </html>
    `);

    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
    printWindow.close();
}
