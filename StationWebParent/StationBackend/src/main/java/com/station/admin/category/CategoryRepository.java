package com.station.admin.category;

import com.station.common.entity.Category;
import com.station.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Long countById(Integer id);

    @Query("SELECT c FROM Category c WHERE CONCAT(c.id,' ',c.name,' ',c.alias,' ') LIKE %?1%")
    Page<Category> search(String keyword, Pageable pageable);


}
