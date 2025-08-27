function logout() {
    const modal = new bootstrap.Modal(document.getElementById('logoutModal'));
    modal.show();
}

function confirmLogout() {
    const modal = bootstrap.Modal.getInstance(document.getElementById('logoutModal'));
    modal.hide();

    // submit form logout Spring Security
    document.getElementById('logoutForm').submit();
}
