package org.inflearngg.duo.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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

public class DuoRepositoryImpl implements DuoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public DuoRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // entitiy 필요
    @Override
    public Page<DuoResponseDto.DuoInfo> searchDuoList(DuoRequestDto.DuoSearch duoSearch, Pageable pageable) {
        List<DuoResponseDto.DuoInfo> results = queryFactory.select(Projections.constructor(DuoResponseDto.DuoInfo.class,
                        duoPost.postId,
                        duoPost.riotGameName,
                        duoPost.riotGameTag,
                        duoPost.pUuid,
                        duoPost.isRiotVerified,
                        duoPost.needPosition,
                        duoPost.needQueueType,
                        duoPost.needTier,
                        duoPost.myMainLane,
                        duoPost.myMainChampionName,
                        duoPost.myMainChampionIconNumber,
                        duoPost.mySubLane,
                        duoPost.mySubChampionName,
                        duoPost.mySubChampionIconNumber,
                        duoPost.isMicOn,
                        duoPost.memo))
                .from(duoPost)
                .where(
                        needPositionEq(duoSearch.getLane()),
                        needQueueTypeEq(duoSearch.getQueueType()),
                        needTierEq(duoSearch.getTier()),
                        isRiotVerifiedEq(duoSearch.isRiotVerified())
                )
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    private BooleanExpression needPositionEq(String lane) {
        if (StringUtils.hasText(lane)) {
            return duoPost.needPosition.eq(lane);
        }
        return null;
    }

    private BooleanExpression needQueueTypeEq(String queueType) {
        if (StringUtils.hasText(queueType)) {
            return duoPost.needQueueType.eq(queueType);
        }
        return null;
    }

    private BooleanExpression needTierEq(String tier) {
        if (StringUtils.hasText(tier)) {
            return duoPost.needTier.eq(tier);
        }
        return null;
    }

    private BooleanExpression isRiotVerifiedEq(boolean riotVerified) {
        return duoPost.isRiotVerified.eq(riotVerified);
    }
}
