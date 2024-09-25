const subImageUpload = document.getElementById('subImageUpload');
const additionalThumbnails = document.getElementById('additional-thumbnails');
let fileArray = [];

function updateDataInputFiles(){
    const dataTransfer = new DataTransfer();
    fileArray.forEach(file => dataTransfer.items.add(file));
    subImageUpload.files = dataTransfer.files;
}


subImageUpload.addEventListener('change', function() {
    fileArray = [...fileArray, ...Array.from(this.files)];
    updateDataInputFiles();

    additionalThumbnails.innerHTML = '';

    // console.log(fileArray.length)

    fileArray.forEach((file, index) => {
        const reader = new FileReader();

        reader.addEventListener('load', function() {
            const thumbnailContainer = document.createElement('div');
            thumbnailContainer.classList.add('thumbnail-container');

            const thumbImage = document.createElement('img');
            thumbImage.src = this.result;
            thumbImage.classList.add('thumb-image');

            const deleteIcon = document.createElement('span');
            deleteIcon.innerHTML = '&times;';
            deleteIcon.classList.add('delete-icon');
            deleteIcon.setAttribute('data-index', index);

            deleteIcon.addEventListener('click', function() {
                const index = parseInt(this.getAttribute('data-index'), 10);
                fileArray.splice(index, 1);
                additionalThumbnails.removeChild(thumbnailContainer);

                updateDataInputFiles()
            });

            thumbnailContainer.appendChild(thumbImage);
            thumbnailContainer.appendChild(deleteIcon);
            additionalThumbnails.appendChild(thumbnailContainer);
        });

        reader.readAsDataURL(file);
    });
});

