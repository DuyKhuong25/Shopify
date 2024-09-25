$(document).ready(function(){
    $(".menu-item-block").click(function(){
        if ($(this).hasClass("active")) {
            $(this).removeClass("active");
            $(this).siblings(".sub-menu").slideUp();
        } else {
            $(".menu-item-block").removeClass("active");
            $(".sub-menu").slideUp();
            $(this).addClass("active");
            $(this).siblings(".sub-menu").slideToggle();
        }
    })

    $(".user-cancel").on("click", function(){
        window.location = "/users"
    })

    $(".category-cancel").on("click", function(){
        window.location = "/categories";
    })

    $(".brand-cancel").on("click", function(){
        window.location = "/brands";
    })

    $(".product-cancel").on("click", function(){
        window.location = "/products";
    })


    $("#form-submit").on("submit", function(){
        checkEmailUnique(this)
        return false;
    })

    $("#form-category").on("submit", function(){
        checkUniqueCategory(this);
        return false
    })

    $("#form-brand").on("submit", function(){
        checkUniqueBrand(this);
        return false;
    })

    $(".clear-search-input").on("click", function (){
        $(".search-input").val("")
        window.location = $(this).data("url");
    })

    var categoryChoose = $(".category-choose");
    var dropDownCategory = $(".select-category");
    dropDownCategory.on("change", function(){
        categoryChoose.empty();
        showBrandCategorySelected()
    })

    function showBrandCategorySelected(){
        dropDownCategory.children("option:selected").each(function(){
            var selectedCategory = $(this);
            var categoryName = selectedCategory.text().replace(/→/g, "");
            categoryChoose.append("<span style='display: inline-block; margin-right: 5px' class='badge bg-warning text-dark'>" + categoryName + "</span>");
        })
    }
    showBrandCategorySelected();

    function checkEmailUnique(form){
         url = "/users/check_email";
         emailValue = $("#email").val();
         inputId = $("#id").val();
         csrfvalue = $("input[name='_csrf']").val();
         params = {id: inputId, email: emailValue, _csrf: csrfvalue};

         $.post(url, params, function(response){
            if(response == "OK"){
                form.submit();
                // alert("OK")
            }else if(response == "Duplicated"){
                alert("Email already exists with another user!");
            }
         })
    }


    $("#form-product").on("submit", function(){
        checkUniqueProduct(this);
        return false;
    })
    function checkUniqueProduct(form){
        url= "/products/check_unique";
        idValue = $("input[name='id']").val();
        nameValue = $("input[name='name']").val();
        csrfvalue = $("input[name='_csrf']").val();

        params = {id: idValue, name: nameValue, _csrf: csrfvalue};
        $.post(url, params, function(response){
            if(response == "OK"){
                form.submit();
            }else if(response == "Duplicated"){
                alert("Tên sản phẩm đã tồn tại trên hệ thống!");
            }
        })
    }

    function checkUniqueCategory(form){
        url = "/categories/check_unique";

        idValue = $("input[name='id']").val();
        nameValue = $("input[name='name']").val();
        alias = $("input[name='alias']").val();
        csrfvalue = $("input[name='_csrf']").val();

        params = {id: idValue, name: nameValue, alias: alias, _csrf: csrfvalue};

        $.post(url, params, function(response){
            if(response == "OK"){
                form.submit();
            }else if(response == "DuplicateName"){
                alert("Category already exists with same name!");
            }else if(response == "DuplicateAlias"){
                alert("Category already exists with same alias!");
            }
        })
    }

    function checkUniqueBrand(form){
        url = "/brands/check_unique";

        idValue = $("input[name='id']").val();
        nameValue = $("input[name='name']").val();
        csrfvalue = $("input[name='_csrf']").val();

        params = {id: idValue, name: nameValue, _csrf: csrfvalue};

        $.post(url, params, function(response){
            if(response == "OK"){
                form.submit();
            }else if(response == "Duplicate"){
                alert("Brand already exists with same name!");
            }
        })
    }

    $("#imageUpload").on("change", function(){
        loadImage(this);
    })

    $("#mainImageUpload").on("change", function(){
        loadImage(this);
    })

    function loadImage(inputImage){
        var fileSize = inputImage.files[0].size;

        if (fileSize > 1048576){
            $(".error-file").text("Hình ảnh phải có kích cỡ nhỏ hơn 1MB!")
        }else{
            var file = inputImage.files[0];
            var reader= new FileReader();
            reader.onload = function(e){
                $("#thumbnail").attr("src", e.target.result);
            }
            reader.readAsDataURL(file);
        }
    }

    $(".logout-link").on("click", function(e){
        e.preventDefault();
        document.formLogout.submit();
    })

    $(".drop-brand").on("change", function(){
        $(".drop-category").empty();
        brandIdValue = $(this).val();
        url = "/brands/" + brandIdValue + "/categories";
        $.get(url, function (responJson){
            $.each(responJson, function (key, category){
                $("<option>").val(category.id).text(category.name).appendTo($(".drop-category"));
            })
        })
    })

    $(".category-filter-product").on("change", function (){
        $(".search-form_product").submit();
    })

    $("#shortDescription").richText();
    $("#fullDescription").richText();
    $("#content_mail").richText();
    $("#content_mail_order").richText();
})