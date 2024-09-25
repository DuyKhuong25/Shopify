$(document).ready(function(){
    const totalPriceCart = $(".total-price")
    const quantityCart = $(".quantity-cart")

    $(".add-cart-action").on("click", function(){
        const quantity = $(".input-quantity").val();
        const productID = $(".add-cart-action").data("product");

        const url = "/cart/add/" + productID + "/" + quantity;

        $.ajax({
            type: "GET",
            url: url,
        }).done(function (data) {
            if(data.responseType == "Authentication"){
                alert("Bạn chưa đăng nhập. Vui lòng đăng nhập để tiếp mua sắm!")
            }else if(data.responseType == "Success"){
                $(".quantity-cart").text(data.totalQuantity)
                alert("Thêm sản phẩm vào giỏ hàng thành công!")
            }
        })
    })

    $(".minus-change-cart-item").on("click", function(){
        const dataProductId = this.dataset.product;
        const inputQuantity = $(`#quantity-product-` + dataProductId);
        const subTotalCurrentProductItem = $(`#subtotal-product-` + dataProductId);
        let currentQuantityProductItem = parseInt(inputQuantity.val())

        if(currentQuantityProductItem <= 1){
            currentQuantityProductItem = 1
            inputQuantity.val(currentQuantityProductItem)
        }else{
            currentQuantityProductItem -= 1;
            inputQuantity.val(currentQuantityProductItem)
        }

        const url = "/cart/update/" + dataProductId + "/" + currentQuantityProductItem;

        $.ajax({
            type: "GET",
            url: url
        }).done(function (data) {
            subTotalCurrentProductItem.text(data.subTotalPrice)
            totalPriceCart.text(data.cartTotalPrice)
            quantityCart.text(data.quantityCart)
        })
    })

    $(".plus-change-cart-item").on("click", function(){
        const dataProductId = this.dataset.product
        const inputQuantity = $(`#quantity-product-` + dataProductId);
        const subTotalCurrentProductItem = $(`#subtotal-product-` + dataProductId);
        let currentQuantityProductItem = parseInt(inputQuantity.val())

        currentQuantityProductItem += 1;
        inputQuantity.val(currentQuantityProductItem)

        const url = "/cart/update/" + dataProductId + "/" + currentQuantityProductItem;
        $.ajax({
            type: "GET",
            url: url
        }).done(function (data) {
            subTotalCurrentProductItem.text(data.subTotalPrice)
            totalPriceCart.text(data.cartTotalPrice)
            quantityCart.text(data.quantityCart)
        })
    })

    $(".remove-cart-item").on("click", function (){
        const parentCartItemId = this.dataset.item;
        const productItemDeleteId = parseInt(this.dataset.product);
        const parentCartItem = $(`#item-cart-` + parentCartItemId);
        parentCartItem.remove()

        const url = "/cart/delete/" + productItemDeleteId;
        $.ajax({
            type: "GET",
            url: url
        }).done(function (data) {
            if (data.quantityCart == 0){
                window.location = "/cart";
            }

            quantityCart.text(data.quantityCart)
            totalPriceCart.text(data.cartTotalPrice)

        })
    })


})