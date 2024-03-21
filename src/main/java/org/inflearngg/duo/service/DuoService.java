package org.inflearngg.duo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.duo.entity.DuoPost;
import org.inflearngg.duo.repository.DuoRepository;
import org.inflearngg.member.entity.Member;
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
        return duoRepository.searchDuoList(duoSearch, PageRequest.of(page -1 , PAGE_SIZE));
    }

    // 상세 조회
    public DuoPost getDuoPost(Long postId) {
        return duoRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));
    }

    public DuoPost createDuoPost(DuoPost makeDuoPost) {
        DuoPost save = duoRepository.save(makeDuoPost);
        return save;
    }

    public DuoPost updateDuoPost(DuoPost duoPostUpdate) {
        // 회원id 체크 or password 체크

        DuoPost getDuoPost = null;
        if (duoPostUpdate.getMember() != null) { //로그인이 필요한 경우
            getDuoPost = validateMember(duoPostUpdate.getPostId(), duoPostUpdate.getMember().getMemberId());
        }
        if (duoPostUpdate.getPostPassword() != null) {//비밀번호가 필요한 경우
            getDuoPost = validatePassword(duoPostUpdate.getPostId(), duoPostUpdate.getPostPassword());
        }
        if (getDuoPost == null)
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다. id=" + duoPostUpdate.getPostId());
        // update 된 부분만 적용
        DuoPost duoPost = getUpdateDuoPost(getDuoPost, duoPostUpdate);
        duoRepository.save(duoPost);
        return duoPost;
    }

    public boolean deleteDuoPost(Long postId, Long memberId, String passwordCheck) {
        DuoPost duoPost = verifiedPostId(postId);

        if (duoPost.getMember() != null) { // 회원게시글
            if (duoPost.getMember().getMemberId().equals(memberId)) {
                // 내가 로그인했고, 내 회원 게시물의 경우 (service)
                duoRepository.deleteById(postId);
                return true;
            }
            // 내가 로그인도 했지만, 다른 회원의 게시물인 경우(service)
            throw new IllegalArgumentException("해당 게시글의 주인이 아닙니다. id=" + postId);
        } else { // 비회원 게시글
            // 비밀번호가 없는 경우
            if (passwordCheck.isEmpty()) {
                throw new IllegalArgumentException("비밀번호가 필요합니다.");
            }
            if (duoPost.getPostPassword().equals(passwordCheck)) {
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
        if (!duoPostUpdate.getMyMainLane().equals(duoPost.getMyMainLane()))
            duoPost.setMyMainLane(duoPostUpdate.getMyMainLane());
        if (!duoPostUpdate.getMyMainChampionName().equals(duoPost.getMyMainChampionName())) {
            duoPost.setMyMainChampionName(duoPostUpdate.getMyMainChampionName());
        }
        if (!duoPostUpdate.getMySubLane().equals(duoPost.getMySubLane()))
            duoPost.setMySubLane(duoPostUpdate.getMySubLane());
        if (!duoPostUpdate.getMySubChampionName().equals(duoPost.getMySubChampionName())) {
            duoPost.setMySubChampionName(duoPostUpdate.getMySubChampionName());
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
        if(memberId < 0L)
            throw new IllegalArgumentException("로그인이 필요합니다.");
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
