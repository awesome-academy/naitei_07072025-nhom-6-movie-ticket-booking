function showDetail(id) {
    fetch(`/admin/request_cinemas/${id}`)
        .then(res => res.json())
        .then(data => {
            document.getElementById("modalCinemaName").textContent = data.cinemaName || "-";
            document.getElementById("modalAddress").textContent = data.address || "-";
            document.getElementById("modalDescription").textContent = data.description || "-";
            document.getElementById("modalUser").textContent = data.userName || "-";
            document.getElementById("modalEmail").textContent = data.email || "-";
            document.getElementById("modalDate").textContent =
                data.sentDate ? new Date(data.sentDate).toLocaleString() : "-";
            document.getElementById("modalStatus").textContent = data.status || "-";

            let actions = document.getElementById("modalActions");
            actions.innerHTML = "";
            if (data.status === "PENDING") {
                actions.innerHTML = `
                    <button class="btn btn-success" onclick="approve(${id})">Xác nhận</button>
                    <button class="btn btn-danger" onclick="reject(${id})">Từ chối</button>`;
            }

            let modal = new bootstrap.Modal(document.getElementById("detailModal"));
            modal.show();
        });
}
