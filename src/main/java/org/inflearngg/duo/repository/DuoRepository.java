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
public interface DuoRepository extends JpaRepository<DuoPost, Long> {

    // 전체 검색
    @Override
    Page<DuoPost> findAll(@NotNull Pageable pageable);

    Page<DuoPost> findByNeedPosition(String needPosition, Pageable pageable);

    Page<DuoPost> findByNeedQueueType(String needQueueType, Pageable pageable);

    Page<DuoPost> findByNeedTier(String needTier, Pageable pageable);

    Page<DuoPost> findByIsRiotVerified(boolean isRiotVerified, Pageable pageable);

    Page<DuoPost> findByIsMicOn(boolean isMicOn, Pageable pageable);

    //2개 조합
    Page<DuoPost> findByNeedPositionAndNeedQueueType(String needPosition, String needQueueType, Pageable pageable);
    Page<DuoPost> findByNeedPositionAndNeedTier(String needPosition, String needTier, Pageable pageable);
    Page<DuoPost> findByNeedPositionAndIsRiotVerified(String needPosition, boolean isRiotVerified, Pageable pageable);
    Page<DuoPost> findByNeedPositionAndIsMicOn(String needPosition, boolean isMicOn, Pageable pageable);
    Page<DuoPost> findByNeedQueueTypeAndNeedTier(String needQueueType, String needTier, Pageable pageable);
    Page<DuoPost> findByNeedQueueTypeAndIsRiotVerified(String needQueueType, boolean isRiotVerified, Pageable pageable);
    Page<DuoPost> findByNeedQueueTypeAndIsMicOn(String needQueueType, boolean isMicOn, Pageable pageable);
    Page<DuoPost> findByNeedTierAndIsRiotVerified(String needTier, boolean isRiotVerified, Pageable pageable);
    Page<DuoPost> findByNeedTierAndIsMicOn(String needTier, boolean isMicOn, Pageable pageable);
    Page<DuoPost> findByIsRiotVerifiedAndIsMicOn(boolean isRiotVerified, boolean isMicOn, Pageable pageable);

    //3개 조합
    Page<DuoPost> findByNeedPositionAndNeedQueueTypeAndNeedTier(String needPosition, String needQueueType, String needTier, Pageable pageable);
    Page<DuoPost> findByNeedPositionAndNeedQueueTypeAndIsRiotVerified(String needPosition, String needQueueType, boolean isRiotVerified, Pageable pageable);
    Page<DuoPost> findByNeedPositionAndNeedQueueTypeAndIsMicOn(String needPosition, String needQueueType, boolean isMicOn, Pageable pageable);
    Page<DuoPost> findByNeedPositionAndNeedTierAndIsRiotVerified(String needPosition, String needTier, boolean isRiotVerified, Pageable pageable);
    Page<DuoPost> findByNeedPositionAndNeedTierAndIsMicOn(String needPosition, String needTier, boolean isMicOn, Pageable pageable);
    Page<DuoPost> findByNeedPositionAndIsRiotVerifiedAndIsMicOn(String needPosition, boolean isRiotVerified, boolean isMicOn, Pageable pageable);
    Page<DuoPost> findByNeedQueueTypeAndNeedTierAndIsRiotVerified(String needQueueType, String needTier, boolean isRiotVerified, Pageable pageable);
    Page<DuoPost> findByNeedQueueTypeAndNeedTierAndIsMicOn(String needQueueType, String needTier, boolean isMicOn, Pageable pageable);
    Page<DuoPost> findByNeedQueueTypeAndIsRiotVerifiedAndIsMicOn(String needQueueType, boolean isRiotVerified, boolean isMicOn, Pageable pageable);
    Page<DuoPost> findByNeedTierAndIsRiotVerifiedAndIsMicOn(String needTier, boolean isRiotVerified, boolean isMicOn, Pageable pageable);

    // 4개 조합
    Page<DuoPost> findByNeedPositionAndNeedQueueTypeAndNeedTierAndIsRiotVerified(String needPosition, String needQueueType, String needTier, boolean isRiotVerified, Pageable pageable);
    Page<DuoPost> findByNeedPositionAndNeedQueueTypeAndNeedTierAndIsMicOn(String needPosition, String needQueueType, String needTier, boolean isMicOn, Pageable pageable);
    Page<DuoPost> findByNeedPositionAndNeedQueueTypeAndIsRiotVerifiedAndIsMicOn(String needPosition, String needQueueType, boolean isRiotVerified, boolean isMicOn, Pageable pageable);
    Page<DuoPost> findByNeedPositionAndNeedTierAndIsRiotVerifiedAndIsMicOn(String needPosition, String needTier, boolean isRiotVerified, boolean isMicOn, Pageable pageable);
    Page<DuoPost> findByNeedQueueTypeAndNeedTierAndIsRiotVerifiedAndIsMicOn(String needQueueType, String needTier, boolean isRiotVerified, boolean isMicOn, Pageable pageable);

}
