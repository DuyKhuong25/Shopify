const container = document.getElementById('input-container');

document.querySelector('.add-row-btn').addEventListener('click', function() {

    // Add remove button to all existing rows using querySelectorAll
    const rows = document.querySelectorAll('.input-row');
    rows.forEach((row) => {
        if (!row.querySelector('.remove-row-btn')) {
            const removeBtn = document.createElement('button');
            removeBtn.className = 'remove-row-btn';
            removeBtn.innerHTML = '<i class="fa-solid fa-circle-xmark"></i>';
            removeBtn.onclick = function() {
                row.remove();
            };
            row.appendChild(removeBtn);
        }
    });

    // Create a new row
    const newRow = document.createElement('div');
    newRow.className = 'input-row';
    newRow.innerHTML = `
            <input type="hidden" name="detailID" value="0">
            <div class="input-group">
                <label for="detailName">Tên thuộc tính:</label>
                <input type="text" id="detailName" name="detailName" placeholder="Tên thuộc tính">
            </div>
            <div class="input-group">
                <label for="detailValue">Giá trị:</label>
                <input type="text" id="detailValue" name="detailValue" placeholder="Giá trị thuộc tính">
            </div>
    `;

    container.appendChild(newRow);
});

const btnRemoveDetail = document.querySelectorAll(".remove-detail");

btnRemoveDetail.forEach(item => {
    item.addEventListener("click", function() {
        const rowRemoveId = this.dataset.id;
        const rowContainer = document.querySelector(`#row-${rowRemoveId}`);
        container.removeChild(rowContainer)
    })
})