package org.inflearngg.duo.mapper;

import lombok.extern.slf4j.Slf4j;
import org.inflearngg.client.riot.dto.RiotApiResponseDto;
import org.inflearngg.duo.dto.request.DuoRequestDto;
import org.inflearngg.duo.dto.response.DuoResponseDto;
import org.inflearngg.duo.entity.DuoPost;

import org.inflearngg.member.entity.Member;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DuoMapper {

    //entity -> responseDto
    public DuoResponseDto.DuoInfo duoPostToDuoResponseDtoDuoInfo(DuoPost duoPost) {
        return new DuoResponseDto.DuoInfo(
                duoPost.getPostId(),
                duoPost.getRiotGameName(),
                duoPost.getRiotGameTag(),
                duoPost.getPUuid(),
                duoPost.isRiotVerified(),
                duoPost.getNeedPosition(),
                duoPost.getNeedQueueType(),
                duoPost.getMyMainLane(),
                duoPost.getMyMainChampionName(),
                duoPost.getMySubLane(),
                duoPost.getMySubChampionName(),
                duoPost.isMicOn(),
                duoPost.getMemo());
    }

    //requestDto -> entity
    public DuoPost duoPostSaveToDuoPost(DuoRequestDto.DuoPostSave duoPostSave, RiotApiResponseDto.RiotPuuidAndTierInfo riotPuuidAndTierInfo) {
        DuoPost duoPost = new DuoPost();
        duoPost.setRiotVerified(duoPostSave.isRiotVerified());
        duoPost.setRiotGameName(duoPostSave.getRiotGameName());
        duoPost.setRiotGameTag(duoPostSave.getRiotGameTag());
        duoPost.setPUuid(riotPuuidAndTierInfo.getPuuid());
        duoPost.setNeedPosition(duoPostSave.getNeedPosition());
        duoPost.setNeedQueueType(duoPostSave.getQueueType().name());

        duoPost.setMyMainLane(duoPostSave.getMyMainLane().name());
        duoPost.setMyMainChampionName(duoPostSave.getMyMainChampionName());
        duoPost.setMySubLane(duoPostSave.getMySubLane().name());
        duoPost.setMySubChampionName(duoPostSave.getMySubChampionName());

        duoPost.setMySoloRankTier(riotPuuidAndTierInfo.getSoloRank().getTier());
        duoPost.setMySoloRankLevel(riotPuuidAndTierInfo.getSoloRank().getRank());
        duoPost.setMyFreeRankTier(riotPuuidAndTierInfo.getFreeRank().getTier());
        duoPost.setMyFreeRankLevel(riotPuuidAndTierInfo.getFreeRank().getRank());

        duoPost.setMicOn(duoPostSave.isMicOn());
        duoPost.setMemo(duoPostSave.getMemo());

        if(!duoPostSave.isRiotVerified()){ //비회원
            duoPost.setPostPassword(duoPostSave.getPassword());
        }
        return duoPost;
    }


    public DuoPost duoPostUpdateToDuoPost(DuoRequestDto.DuoPostUpdate duoPostUpdate) {
        DuoPost duoPost = new DuoPost();
        duoPost.setPostId(duoPostUpdate.getPostId());
        setDuplicateDuoPost(duoPostUpdate, duoPost);
        if (duoPostUpdate.getMemberId() != null)
            duoPost.setMember(new Member(duoPostUpdate.getMemberId()));
        if (duoPostUpdate.getPasswordCheck() != null)
            duoPost.setPostPassword(duoPostUpdate.getPasswordCheck());
        return duoPost;
    }

    private static void setDuplicateDuoPost(DuoRequestDto.DuoPostSaveData duoPostSave, DuoPost duoPost) {
        duoPost.setRiotGameName(duoPostSave.getRiotGameName());
        duoPost.setRiotGameTag(duoPostSave.getRiotGameTag());
        duoPost.setRiotVerified(duoPostSave.isRiotVerified());
        duoPost.setMicOn(duoPostSave.isMicOn());
        duoPost.setMemo(duoPostSave.getMemo());
    }
}
