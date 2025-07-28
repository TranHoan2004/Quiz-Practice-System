function showAddQuizForm() {
    document.getElementById('addQuizForm').style.display = 'flex';
}

function hideAddQuizForm() {
    document.getElementById('addQuizForm').style.display = 'none';
}

function showDeleteConfirm(quizId) {
    document.getElementById('quizIdToDelete').value = quizId;
    document.getElementById('deleteConfirmModal').style.display = 'flex';
}

function hideDeleteConfirm() {
    document.getElementById('deleteConfirmModal').style.display = 'none';
}