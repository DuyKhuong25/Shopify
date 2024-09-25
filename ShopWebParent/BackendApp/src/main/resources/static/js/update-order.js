$(document).ready(function(){
    const tempTotalOrder = $(".temp-total");
    const totalCheckout = $(".total-checkout");

    $(".minus_change-order-item").on("click", function(){
        const dataIdOrderDetail = this.dataset.order;
        const quantityInput = $(`#quantity-order-${dataIdOrderDetail}`);
        const subTotalOrderDetail = $(`.sub-total-${dataIdOrderDetail}`);
        let quantityInputValue = parseInt(quantityInput.val());

        quantityInputValue -= 1;

        if(quantityInputValue <= 1) {
            quantityInputValue = 1;
        }

        quantityInput.val(quantityInputValue);

        const url = "/order/update/" + dataIdOrderDetail +  "/" + quantityInputValue;

        $.ajax({
            type: "GET",
            url: url,
        }).done(function (data) {
            subTotalOrderDetail.text(data.subTotal)
            tempTotalOrder.text(data.totalCheckout)
            totalCheckout.text(data.totalCheckout)
        })
    })

    $(".plus_change-order-item").on("click", function (){
        const dataIdOrderDetail = this.dataset.order;
        const quantityInput = $(`#quantity-order-${dataIdOrderDetail}`);
        const subTotalOrderDetail = $(`.sub-total-${dataIdOrderDetail}`);
        let quantityInputValue = parseInt(quantityInput.val());

        quantityInputValue += 1;
        quantityInput.val(quantityInputValue);

        const url = "/order/update/" + dataIdOrderDetail +  "/" + quantityInputValue;

        $.ajax({
            type: "GET",
            url: url,
        }).done(function (data) {
            subTotalOrderDetail.text(data.subTotal)
            tempTotalOrder.text(data.totalCheckout)
            totalCheckout.text(data.totalCheckout)
        })
    })

    $(".input-search_add-product").on("input", function(){
        let keyword = $(this).val();
        const resultSearchBlock = $(".inner-search-result");
        const url = "/order/update/search/" + keyword;

        if(keyword != ""){
            $.ajax({
                type: "GET",
                url: url,
            }).done(function (data) {
                resultSearchBlock.html(data.searchView);
                const addAction = document.querySelectorAll(".action-add")
                addAction.forEach(action => action.addEventListener("click", function() {
                    handleAddProduct(this)
                }))
            })
        }else{
            resultSearchBlock.html("");
        }
    })

    function handleAddProduct(action){
        const orderId = $(".data-order").data("order")
        const productId = action.dataset.product

        const url = "/order/update/add/" + orderId + "/" + productId;

        $.ajax({
            type: "GET",
            url: url,
        }).done(function (data) {
            $(".input-search_add-product").val("");
            $(".inner-search-result").html("");
            $(".wrapper-list").html(data.listOrderDetail);
            tempTotalOrder.text(data.totalCheckout);
            totalCheckout.text(data.totalCheckout);
        })
    }
})
