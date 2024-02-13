package org.inflearngg.duo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.duo.entity.DuoPost;
import org.inflearngg.duo.repository.DuoRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static org.inflearngg.duo.dto.request.DuoRequestDto.*;
import static org.inflearngg.duo.dto.response.DuoResponseDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DuoService {

    private final DuoRepository duoRepository;
    private static final int PAGE_SIZE = 10;

    public Page<DuoInfo> getDuoList(int page, DuoSearch duoSearch) {
        return duoRepository.searchDuoList(duoSearch, PageRequest.of(page, PAGE_SIZE));
    }

    // 상세 조회
    public DuoPost getDuoPost(Long postId) {
        return duoRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));
    }

    public DuoPost createDuoPost(DuoPost makeDuoPost) {
        log.info("[save]memberId : " + makeDuoPost.getMember().getMemberId());
        return duoRepository.save(makeDuoPost);
    }

    public DuoPost updateDuoPost(Long postId, Long memberId, String password, DuoPost duoPostUpdate) {
        //memberId, passwordCheck 으로 게시판 주인인지 체크
        DuoPost getDuoPost = null;
        if (memberId != null) {
            getDuoPost = validateMember(postId, memberId);
        }
        if (password != null) {
            getDuoPost = validatePassword(postId, password);
        }
        if (getDuoPost == null)
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다. id=" + postId);
        // update 된 부분만 적용
        DuoPost duoPost = getUpdateDuoPost(getDuoPost, duoPostUpdate);
        duoRepository.save(duoPost);
        return duoPost;
    }

    public boolean deleteDuoPost(Long postId, boolean isLogin, String deleteInfo) {
        DuoPost duoPost = verifiedPostId(postId);

        if (isLogin) {
            //memberId
            Long memberId = Long.valueOf(deleteInfo);
            if (duoPost.getMember().getMemberId().equals(memberId)) {
                duoRepository.deleteById(postId);
                return true;
            }
            throw new IllegalArgumentException("해당 게시글의 주인이 아닙니다. id=" + postId);


        } else {
            if (duoPost.getPostPassword().equals(deleteInfo)) {
                duoRepository.deleteById(postId);
                return true;
            }
            throw new IllegalArgumentException("비밀번호가 틀렸습니다. id=" + postId);
        }
    }

    @NotNull
    private DuoPost getUpdateDuoPost(DuoPost duoPost, DuoPost duoPostUpdate) {
        if (duoPostUpdate.isRiotVerified() != duoPost.isRiotVerified())
            duoPost.setRiotVerified(duoPostUpdate.isRiotVerified());
        if (!duoPostUpdate.getRiotGameName().equals(duoPost.getRiotGameName()))
            duoPost.setRiotGameName(duoPostUpdate.getRiotGameName());
        if (!duoPostUpdate.getRiotGameTag().equals(duoPost.getRiotGameTag()))
            duoPost.setRiotGameTag(duoPostUpdate.getRiotGameTag());
        if (!duoPostUpdate.getNeedPosition().equals(duoPost.getNeedPosition()))
            duoPost.setNeedPosition(duoPostUpdate.getNeedPosition());
        if (!duoPostUpdate.getNeedQueueType().equals(duoPost.getNeedQueueType()))
            duoPost.setNeedQueueType(duoPostUpdate.getNeedQueueType());
        if (!duoPostUpdate.getNeedTier().equals(duoPost.getNeedTier()))
            duoPost.setNeedTier(duoPostUpdate.getNeedTier());
        if (!duoPostUpdate.getMyMainLane().equals(duoPost.getMyMainLane()))
            duoPost.setMyMainLane(duoPostUpdate.getMyMainLane());
        if (!duoPostUpdate.getMyMainChampionName().equals(duoPost.getMyMainChampionName())) {
            duoPost.setMyMainChampionName(duoPostUpdate.getMyMainChampionName());
            duoPost.setMyMainChampionIconNumber(duoPostUpdate.getMyMainChampionIconNumber());
        }
        if (!duoPostUpdate.getMySubLane().equals(duoPost.getMySubLane()))
            duoPost.setMySubLane(duoPostUpdate.getMySubLane());
        if (!duoPostUpdate.getMySubChampionName().equals(duoPost.getMySubChampionName())) {
            duoPost.setMySubChampionName(duoPostUpdate.getMySubChampionName());
            duoPost.setMySubChampionIconNumber(duoPostUpdate.getMySubChampionIconNumber());
        }
        if (duoPostUpdate.isMicOn() != duoPost.isMicOn())
            duoPost.setMicOn(duoPostUpdate.isMicOn());
        if (!duoPostUpdate.getMemo().equals(duoPost.getMemo()))
            duoPost.setMemo(duoPostUpdate.getMemo());
        return duoPost;
    }

    private DuoPost verifiedPostId(Long postId) {
        return duoRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));
    }

    private DuoPost validateMember(Long postId, Long memberId) {
        DuoPost duoPost = verifiedPostId(postId);
        if (duoPost.getMember().getMemberId().equals(memberId))
            return duoPost;
        throw new IllegalArgumentException("해당 게시글의 주인이 아닙니다. id=" + postId);

    }


    private DuoPost validatePassword(Long postId, String passwordCheck) {
        DuoPost duoPost = verifiedPostId(postId);
        if (duoPost.getPostPassword().equals(passwordCheck))
            return duoPost;
        throw new IllegalArgumentException("비밀번호가 틀렸습니다. id=" + postId);
    }


}
