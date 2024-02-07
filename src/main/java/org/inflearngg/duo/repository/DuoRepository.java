package org.inflearngg.duo.repository;

//import org.hibernate.query.Page;
import org.inflearngg.duo.entity.DuoPost;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DuoRepository extends JpaRepository<DuoPost, Long>, DuoRepositoryCustom{

    // 전체 검색
    @Override
    Page<DuoPost> findAll(@NotNull Pageable pageable);

    Page<DuoPost> findByNeedPosition(String needPosition, Pageable pageable);
}
