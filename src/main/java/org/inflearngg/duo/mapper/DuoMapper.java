package org.inflearngg.duo.mapper;

import org.inflearngg.duo.dto.request.DuoRequestDto;
import org.inflearngg.duo.dto.response.DuoResponseDto;
import org.inflearngg.duo.entity.DuoPost;

import org.inflearngg.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class DuoMapper {

    //entity -> responseDto
    public DuoResponseDto.DuoInfo duoPostToDuoResponseDtoDuoInfo(DuoPost duoPost){
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
    public DuoPost duoPostSaveToDuoPost(DuoRequestDto.DuoPostSave duoPostSave){
        DuoPost duoPost = new DuoPost();
        duoPost.setPUuid(duoPostSave.getPUuid());
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
        if(duoPostSave.isLogin()) // 로그인 했다면.
            duoPost.setUser(new Member(duoPostSave.getMemberId()));
        else
            duoPost.setPostPassword(duoPostSave.getPassword());
        return duoPost;
    }

}
