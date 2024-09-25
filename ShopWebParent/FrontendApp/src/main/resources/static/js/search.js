$(document).ready(function(){
    $(".input-search-product").on("input", function(){
        let keyword = $(this).val();
        const resultSearchBlock = $(".inner-search-result");
        const url = "/product/search/" + keyword;

        if(keyword != ""){
            $.ajax({
                type: "GET",
                url: url,
            }).done(function (data) {
                resultSearchBlock.html(data.searchView);
            })
        }else{
            resultSearchBlock.html("");
        }
    })
})