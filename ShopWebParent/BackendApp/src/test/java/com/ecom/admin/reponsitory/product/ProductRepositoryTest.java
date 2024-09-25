package com.ecom.admin.reponsitory.product;

import com.ecom.admin.repository.product.ProductRepository;
import com.ecom.common.entity.Brand;
import com.ecom.common.entity.Category;
import com.ecom.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.text.Normalizer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void testCreateProduct() {
        Brand brand = entityManager.find(Brand.class, 4);
        Category category = entityManager.find(Category.class, 5);
        Product product = new Product();
        product.setName("Dell Inspiron 16");
        product.setAlias("dell_inspiron_16");
        product.setShortDescription("Short description for Dell Inspiron 16");
        product.setFullDescription("Full description for Dell Inspiron 16");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(800);
        product.setCost(750);
        product.setEnabled(true);
        product.setInStock(true);

        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());

        System.out.println(product);
        productRepository.save(product);
    }


    private static final Map<Character, Character> VIETNAMESE_CHAR_MAP = new HashMap<>();

    static {
        // Khởi tạo bảng ánh xạ cho các ký tự đặc biệt
        String specialChars = "àáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ";
        String mappedChars = "aaaaaaăăăăăăeeeeeeeeeeeiiiiiooooooooooơơơơơuuuuuưưưưưyyyyyd";

        for (int i = 0; i < specialChars.length(); i++) {
            VIETNAMESE_CHAR_MAP.put(specialChars.charAt(i), mappedChars.charAt(i));
        }
    }

    @Test
    public void toSlug(String input) {
        input = input.trim();

        input = input.replaceAll("[()]", "").replaceAll("[-]+", " ").replaceAll("/", "");

        // Chuyển đổi ký tự có dấu thành không dấu
        StringBuilder slugBuilder = new StringBuilder();
        for (char c : input.toCharArray()) {
            slugBuilder.append(VIETNAMESE_CHAR_MAP.getOrDefault(c, c));
        }
        String slug = slugBuilder.toString();

        // Chuẩn hóa chuỗi
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // Xóa các dấu còn sót lại
                .toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "");

        System.out.println(slug);
    }

    @Test
    public void createProductWithImage(){
        Product product = productRepository.findById(3);
        String mainImage = "main-image.png";
        String subImage1 = "sub-image1.png";
        String subImage2 = "sub-image2.png";
        String subImage3 = "sub-image3.png";

        product.setMainImage(mainImage);
        product.addImage(subImage1);
        product.addImage(subImage2);
        product.addImage(subImage3);

        productRepository.save(product);
    }

}
