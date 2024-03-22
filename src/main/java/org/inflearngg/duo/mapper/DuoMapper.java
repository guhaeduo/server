package org.inflearngg.duo.mapper;

import lombok.extern.slf4j.Slf4j;
import org.inflearngg.client.riot.dto.RiotApiResponseDto;
import org.inflearngg.duo.dto.request.DuoRequestDto;
import org.inflearngg.duo.dto.response.DuoResponseDto;
import org.inflearngg.duo.entity.DuoPost;

import org.inflearngg.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Slf4j
@Component
public class DuoMapper {

    //page<entity> -> pageInfo
    public DuoResponseDto.PageInfo duoPageToDuoResponseDtoPageInfo(Page<DuoResponseDto.DuoInfo> duoPosts) {
        DuoResponseDto.PageInfo pageInfo = new DuoResponseDto.PageInfo();
        pageInfo.setContent(duoPosts.getContent());
        pageInfo.setHasNextPage(duoPosts.hasNext());
        pageInfo.setHasPreviousPage(duoPosts.hasPrevious());
        pageInfo.setCurrentPageNumber(duoPosts.getNumber() + 1);
        pageInfo.setNextPageNumber(duoPosts.getNumber() + 2);
        pageInfo.setPreviousPageNumber(duoPosts.getNumber());
        return pageInfo;
    }

    //entity -> responseDto
    public DuoResponseDto.DuoInfo duoPostToDuoResponseDtoDuoInfo(DuoPost duoPost) {
        return new DuoResponseDto.DuoInfo(
                duoPost.isGuestPost(),
                duoPost.getPostId(),
                duoPost.getCreatedAt(),
                duoPost.getMember() == null ? null : duoPost.getMember().getMemberId(),
                duoPost.getProfileIconId(),
                //회원이면 닉네임, 비회원이면 null
                duoPost.getRiotGameName(),
                duoPost.getRiotGameTag(),
                duoPost.getNeedPosition(),
                duoPost.getNeedQueueType(),

                duoPost.getMyMainLane(),
                duoPost.getMyMainChampionName(),
                duoPost.getMySubLane(),
                duoPost.getMySubChampionName(),

                duoPost.getMySoloRankTier(),
                duoPost.getMySoloRankLevel(),
                duoPost.getMyFreeRankTier(),
                duoPost.getMyFreeRankLevel(),

                duoPost.getMemo(),
                duoPost.getPUuid(),
                duoPost.isMicOn(),
                duoPost.isRiotVerified()
        );
    }

    //requestDto -> entity
    public DuoPost duoPostSaveToDuoPost(Long memberId, DuoRequestDto.DuoPostSave duoPostSave, RiotApiResponseDto.RiotPuuidAndTierInfo riotPuuidAndTierInfo) {
        DuoPost duoPost = new DuoPost();
        duoPost.setPUuid(riotPuuidAndTierInfo.getPuuid());
        setDuplicateDuoPost(duoPostSave, duoPost);
        setRankTierDuoPost(riotPuuidAndTierInfo, duoPost);

        if (memberId != -1L) { // 회원상태인 경우
            duoPost.setMember(new Member(memberId));
            duoPost.setGuestPost(false);
        } else { //비회원시 비밀번호 저장
            duoPost.setPostPassword(duoPostSave.getPassword());
            duoPost.setGuestPost(true);
        }
        return duoPost;
    }


    public DuoPost duoPostUpdateToDuoPost(Long postId, Long memberId, DuoRequestDto.DuoPostUpdate duoPostUpdate, RiotApiResponseDto.RiotPuuidAndTierInfo riotPuuidAndTierInfo) {
        DuoPost duoPost = new DuoPost();
        duoPost.setPostId(postId);
        setDuplicateDuoPost(duoPostUpdate, duoPost);
        setRankTierDuoPost(riotPuuidAndTierInfo, duoPost);


        if (!duoPostUpdate.getIsGuestPost()) { // 비밀번호가 필요없는 경우
            duoPost.setMember(new Member(memberId)); //회원시 memberId로 연결
            duoPost.setGuestPost(false);
        } else { //비회원시 비밀번호 저장
            duoPost.setPostPassword(duoPostUpdate.getPasswordCheck());
            duoPost.setGuestPost(true);
        }

        return duoPost;
    }

    private static void setRankTierDuoPost(RiotApiResponseDto.RiotPuuidAndTierInfo riotPuuidAndTierInfo, DuoPost duoPost) {
        duoPost.setMySoloRankTier(riotPuuidAndTierInfo.getSoloRank().getTier());
        duoPost.setMySoloRankLevel(riotPuuidAndTierInfo.getSoloRank().getRank());
        duoPost.setMyFreeRankTier(riotPuuidAndTierInfo.getFreeRank().getTier());
        duoPost.setMyFreeRankLevel(riotPuuidAndTierInfo.getFreeRank().getRank());

        duoPost.setProfileIconId(riotPuuidAndTierInfo.getProfileIconId());
    }

    private static void setDuplicateDuoPost(DuoRequestDto.DtoDuoPost postDto, DuoPost duoPost) {
        duoPost.setRiotGameName(postDto.getRiotGameName());
        duoPost.setRiotGameTag(postDto.getRiotGameTag());
        duoPost.setRiotVerified(postDto.isRiotVerified());

        duoPost.setNeedPosition(postDto.getNeedPosition());
        duoPost.setNeedQueueType(postDto.getQueueType());

        duoPost.setMyMainLane(postDto.getMyMainLane());
        duoPost.setMyMainChampionName(postDto.getMyMainChampionName());
        duoPost.setMySubLane(postDto.getMySubLane());
        duoPost.setMySubChampionName(postDto.getMySubChampionName());

        duoPost.setMicOn(postDto.isMicOn());
        duoPost.setMemo(postDto.getMemo());
    }
}
