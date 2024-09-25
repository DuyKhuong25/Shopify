const citySelectBlock = document.querySelector(".select-city");
const districtSelectBlock = document.querySelector(".select-district");
const wardSelectBlock = document.querySelector(".select-ward");

(async function loadCity() {
    const res = await fetch("https://vapi.vnappmob.com/api/province/");
    const data = await res.json();
    const results = data.results;

    let html = "";
    for (let i = 0; i < results.length; i++) {
        html += `<option value="${results[i].province_name}" data-cityid="${results[i].province_id}">${results[i].province_name}</option>`;
    }

    citySelectBlock.insertAdjacentHTML("beforeend", html);
})();

async function loadDistrict(provine_id) {
    const res = await fetch(
        `https://vapi.vnappmob.com/api/province/district/${provine_id}`
    );
    const data = await res.json();
    const results = data.results;

    let html = "";
    for (let i = 0; i < results.length; i++) {
        html += `<option value="${results[i].district_name}" data-districtid="${results[i].district_id}">${results[i].district_name}</option>`;
    }

    resetDistrict();
    districtSelectBlock.insertAdjacentHTML("beforeend", html);
}

async function loadWard(district_id) {
    const res = await fetch(
        `https://vapi.vnappmob.com/api/province/ward/${district_id}`
    );
    const data = await res.json();
    const results = data.results;

    let html = "";
    for (let i = 0; i < results.length; i++) {
        html += `<option value="${results[i].ward_name}" data-districtid="${results[i].ward_id}">${results[i].ward_name}</option>`;
    }

    resetWard();
    wardSelectBlock.insertAdjacentHTML("beforeend", html);
}

citySelectBlock.addEventListener("change", function () {
    const optionSelected = this.options[this.selectedIndex];
    const idCity = optionSelected.dataset.cityid;

    resetWard();
    loadDistrict(idCity);
});

districtSelectBlock.addEventListener("change", function () {
    const optionSelected = this.options[this.selectedIndex];
    const idDistric = optionSelected.dataset.districtid;
    loadWard(idDistric);
});

function resetWard() {
    wardSelectBlock.textContent = "";
    wardSelectBlock.innerHTML = "<option value=''>Chọn phường/xã</option>";
}

function resetDistrict() {
    districtSelectBlock.textContent = "";
    districtSelectBlock.innerHTML = "<option value=''>Chọn quận/huyện</option>";
}
