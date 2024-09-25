$(document).ready(function () {
    function loadProducts() {
        const categoryID = $('input[name="categoryID"]').val();
        const categories = $('input[name="category"]:checked').val();
        const minPrice = $('input[name="price"]:checked').data('min');
        const maxPrice = $('input[name="price"]:checked').data('max');
        const searchQuery = $('#search-input').val();

        const url = "/products/filter_products";
        const params = {
            categoryID: categoryID,
            categories: categories,
            minPrice: minPrice,
            maxPrice: maxPrice,
            searchQuery: searchQuery,
        }

        $.get(url, params, function (data) {
            $('.product-list-block').html(data.productsHTML);
        })
    }

    $('input[name="category"]').change(function () {
        loadProducts();
    });

    $('input[name="price"]').change(function () {
        loadProducts();
    });

    $('#search-input').on('input', function () {
        loadProducts();
    });


    $('#resetFilters').on('click', function () {
        $('input[name="category"]').prop('checked', false);
        $('input[name="price"]').prop('checked', false);
        $('#search-input').val('');
        loadProducts();
    });
});