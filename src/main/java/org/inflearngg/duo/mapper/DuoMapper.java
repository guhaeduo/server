package org.inflearngg.duo.mapper;

import lombok.extern.slf4j.Slf4j;
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
                duoPost.getNeedTier(),
                duoPost.getMyMainLane(),
                duoPost.getMyMainChampionName(),
                duoPost.getMyMainChampionIconNumber(),
                duoPost.getMySubLane(),
                duoPost.getMySubChampionName(),
                duoPost.getMySubChampionIconNumber(),
                duoPost.isMicOn(),
                duoPost.getMemo());
    }

    //requestDto -> entity
    public DuoPost duoPostSaveToDuoPost(DuoRequestDto.DuoPostSave duoPostSave) {
        DuoPost duoPost = new DuoPost();
        duoPost.setPUuid(duoPostSave.getPuuid());
        setDuplicateDuoPost(duoPostSave, duoPost);
        log.info("isLogin : " + duoPostSave.getLogin());
        if (duoPostSave.getLogin()) {
            log.info("memberId : " + duoPostSave.getMemberId());
            duoPost.setUser(new Member(duoPostSave.getMemberId()));
        } else
            duoPost.setPostPassword(duoPostSave.getPassword());
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
        duoPost.setNeedPosition(duoPostSave.getNeedPosition());
        duoPost.setNeedQueueType(duoPostSave.getNeedQueueType());
        duoPost.setNeedTier(duoPostSave.getNeedTier());
        duoPost.setMyMainLane(duoPostSave.getMyPosition().getMain().getLane());
        duoPost.setMyMainChampionName(duoPostSave.getMyPosition().getMain().getChampionName());
        duoPost.setMyMainChampionIconNumber(duoPostSave.getMyPosition().getMain().getChampionIconNumber());
        duoPost.setMySubLane(duoPostSave.getMyPosition().getSub().getLane());
        duoPost.setMySubChampionName(duoPostSave.getMyPosition().getSub().getChampionName());
        duoPost.setMySubChampionIconNumber(duoPostSave.getMyPosition().getSub().getChampionIconNumber());
        duoPost.setMicOn(duoPostSave.isMicOn());
        duoPost.setMemo(duoPostSave.getMemo());
    }
}
