const quantityOrder = document.querySelector(".input-quantity");
const minusBtn = document.querySelector(".minus");
const plusBtn = document.querySelector(".plus");

minusBtn.addEventListener("click", function (){
    let quantity = Number.parseInt(quantityOrder.value);
    if(quantity <= 1){
        quantityOrder.value = 1;
    }else{
        quantityOrder.value = quantity - 1;
    }
})

plusBtn.addEventListener("click", function (){
    let quantity = Number.parseInt(quantityOrder.value);
    quantityOrder.value = quantity + 1;
})

