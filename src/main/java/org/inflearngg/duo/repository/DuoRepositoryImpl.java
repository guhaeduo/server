package org.inflearngg.duo.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.duo.dto.QueueType;
import org.inflearngg.duo.dto.request.DuoRequestDto;
import org.inflearngg.duo.dto.response.DuoResponseDto;
import org.inflearngg.duo.entity.DuoPost;
import org.inflearngg.duo.entity.QDuoPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.inflearngg.duo.entity.QDuoPost.*;

@Slf4j
public class DuoRepositoryImpl implements DuoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public DuoRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // entitiy 필요
    @Override
    public Page<DuoResponseDto.DuoInfo> searchDuoList(DuoRequestDto.DuoSearch duoSearch, Pageable pageable) {
        List<DuoResponseDto.DuoInfo> results = queryFactory.select(Projections.constructor(DuoResponseDto.DuoInfo.class,
                        duoPost.isGuestPost,
                        duoPost.postId,
                        duoPost.createdAt,
                        duoPost.member.memberId,
                        duoPost.profileIconId,
                        duoPost.riotGameName,
                        duoPost.riotGameTag,
                        duoPost.needPosition,
                        duoPost.needQueueType,


                        duoPost.myMainLane,
                        duoPost.myMainChampionName,
                        duoPost.mySubLane,
                        duoPost.mySubChampionName,

                        duoPost.mySoloRankTier,
                        duoPost.mySoloRankLevel,
                        duoPost.myFreeRankTier,
                        duoPost.myFreeRankLevel,

                        duoPost.memo,
                        duoPost.pUuid,
                        duoPost.isMicOn,
                        duoPost.isRiotVerified
                        ))
                .from(duoPost)
                .where(
                        myMainLaneEq(duoSearch.getLane()),
                        needQueueTypeEq(duoSearch.getQueueType()),
                        needTierEq(duoSearch.getTier(), duoSearch.getQueueType()),
                        isRiotVerifiedEq(duoSearch.isRiotVerified())
                )
                .offset(pageable.getOffset()) // 페이지네이션 시작점
                .limit(pageable.getPageSize()) // 페이지당 항목 수
                .orderBy(duoPost.createdAt.desc())
                .fetch();

        // 전체 결과 수를 구하는 쿼리
        Long total = queryFactory
                .select(duoPost.count())
                .from(duoPost)
                .where(
                        myMainLaneEq(duoSearch.getLane()),
                        needQueueTypeEq(duoSearch.getQueueType()),
                        needTierEq(duoSearch.getTier(), duoSearch.getQueueType()),
                        isRiotVerifiedEq(duoSearch.isRiotVerified())
                )
                .fetchOne();

        return new PageImpl<>(results, pageable,total== null?0L:total);
    }

    private BooleanExpression myMainLaneEq(String lane) {
        log.info("lane : " + lane);
        if (StringUtils.hasText(lane)) {
            log.info("lane 조건문 실행");
            return duoPost.myMainLane.eq(lane);
        }
        return null;
    }

    private BooleanExpression needQueueTypeEq(String queueType) {
        log.info("queueType : " + queueType);
        if (StringUtils.hasText(queueType)) {
            log.info("queueType 조건문 실행");
            return duoPost.needQueueType.eq(queueType);
        }
        return null;
    }

    private BooleanExpression needTierEq(String tier, String queueType) {
        log.info("tier : " + tier);
        if (StringUtils.hasText(tier)) {
            if(queueType.equals(QueueType.SOLO.name()))
                return duoPost.mySoloRankTier.eq(tier);
            if (queueType.equals(QueueType.FREE.name()))
                return duoPost.myFreeRankTier.eq(tier);
            return duoPost.mySoloRankTier.eq(tier);
        }
        return null;
    }

    private BooleanExpression isRiotVerifiedEq(boolean riotVerified) {
        if (riotVerified)
            return duoPost.isRiotVerified.eq(riotVerified);
        return null;
    }
}
