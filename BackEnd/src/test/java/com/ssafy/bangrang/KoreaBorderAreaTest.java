package com.ssafy.bangrang;

import com.ssafy.bangrang.domain.map.repository.KoreaBorderAreaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class KoreaBorderAreaTest {
    
    @Autowired
    private KoreaBorderAreaRepository koreaBorderAreaRepository;
    
    @Test
    void Test(){
        koreaBorderAreaRepository.findAll().stream().forEach(koreaBorderArea -> {
            System.out.println("koreaBorderArea = " + koreaBorderArea);
            System.out.println("koreaBorderArea.getShape().getArea() = " + koreaBorderArea.getShape().getArea());
            System.out.println("koreaBorderArea.getShape().getGeometryType() = " + koreaBorderArea.getShape().getGeometryType());
            System.out.println("koreaBorderArea.getShape().getLength() = " + koreaBorderArea.getShape().getLength());
        });
    }
    
}
