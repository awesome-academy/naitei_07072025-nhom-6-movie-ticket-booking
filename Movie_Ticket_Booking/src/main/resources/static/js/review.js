async function toggleDisplay(button) {
    const reviewId = button.getAttribute("data-id");

    const token = document.querySelector("meta[name='_csrf']").content;
    const header = document.querySelector("meta[name='_csrf_header']").content;

    try {
        const response = await fetch(`/admin/movies/reviews/${reviewId}/toggle`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                [header]: token
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error ${response.status}`);
        }

        const data = await response.json();

        const parent = button.closest(".d-flex.flex-column");
        const statusSpan = parent.querySelector(".review-status");

        if (!statusSpan) {
            throw new Error("Không tìm thấy span trạng thái trong DOM");
        }

        statusSpan.textContent = data.isDisplay === 1 ? "Hiển thị" : "Đã ẩn";
        statusSpan.classList.remove("text-success", "text-danger");
        statusSpan.classList.add(data.isDisplay === 1 ? "text-success" : "text-danger");

        button.textContent = data.isDisplay === 1 ? "Ẩn" : "Hiện";

    } catch (err) {
        console.error("toggleDisplay error:", err);
        alert("Không thể cập nhật trạng thái: " + err.message);
    }
}
