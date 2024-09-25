$(document).ready(function () {
    function loadProducts() {
        const keyword = $('input[name="keyword"]').val();
        const minPrice = $('input[name="price"]:checked').data('min');
        const maxPrice = $('input[name="price"]:checked').data('max');
        const searchQuery = $('#search-input').val();

        const url = "/products/filter_search";
        const params = {
            keyword: keyword,
            minPrice: minPrice,
            maxPrice: maxPrice,
            searchQuery: searchQuery,
        }

        $.get(url, params, function (data) {
            $('.product-list-block').html(data.productsHTML);
        })
    }


    $('input[name="price"]').change(function () {
        loadProducts();
    });

    $('#search-input').on('input', function () {
        loadProducts();
    });


    $('#resetFilters').on('click', function () {
        $('input[name="price"]').prop('checked', false);
        $('#search-input').val('');
        loadProducts();
    });
});