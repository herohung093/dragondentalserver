package com.example.repository;

import com.example.model.CodeDetail;
import com.example.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeDetailRepo extends JpaRepository<CodeDetail, Long> {

    CodeDetail save(CodeDetail codeDetail);

    @Query(nativeQuery = true, value = "select cd.code from code_detail cd where cd.type = :type ")
    List<String> findAllProductByType(@Param("type") String type);

    @Query(nativeQuery = true, value = "select * from code_detail cd where cd.code = :code and cd.type = :type")
    CodeDetail checkExist(@Param("code") String code, @Param("type") String type);
}
