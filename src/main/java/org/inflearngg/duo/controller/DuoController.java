package org.inflearngg.duo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.inflearngg.client.riot.dto.RiotApiResponseDto;
import org.inflearngg.duo.dto.Tier;
import org.inflearngg.duo.mapper.DuoMapper;
import org.inflearngg.duo.service.DuoService;
import org.inflearngg.summoner.service.SummonerService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import static org.inflearngg.duo.dto.request.DuoRequestDto.*;
import static org.inflearngg.duo.dto.response.DuoResponseDto.*;

@Slf4j
@RestController
@RequestMapping("/api/duo")
@RequiredArgsConstructor
public class DuoController {

    private final DuoService duoService;
    private final SummonerService summonerService;
    private final DuoMapper mapper;

    @GetMapping
    public PageInfo getDuoList(@Valid DuoSearch duoSearch) {
        // 추후 mapper 사용하기
        return mapper.duoPageToDuoResponseDtoPageInfo(duoService.getDuoList(duoSearch.getPage(), duoSearch));
    }

    @GetMapping("/{postId}")
    public DuoInfo getDuoPost(@PathVariable("postId") Long postId) { // 상세조회
        return mapper.duoPostToDuoResponseDtoDuoInfo(
                duoService.getDuoPost(postId)
        );
    }

    // 인증된 소환사 리스트 보기


    @PostMapping("/post")
    public DuoInfo createDuoPost(
            @RequestAttribute("memberId") Long memberId,
            @Valid @RequestBody DuoPostSave makeDuoPost) {
        //일단 puuid 및 티어 확인
        RiotApiResponseDto.RiotPuuidAndTierInfo riotPuuidAndTierInfo = summonerService.checkAndGetTier(makeDuoPost.getRiotGameName(), makeDuoPost.getRiotGameTag(), makeDuoPost.getRegion());

        if (memberId == -1L) { //비회원로그인
            if (makeDuoPost.isRiotVerified()) {
                //비회원인데 인증된 소환사로 등록하려고 할 때
                throw new IllegalArgumentException("인증된 소환사가 아닙니다.");
            }
            if (makeDuoPost.getPassword() == null) {
                throw new IllegalArgumentException("비밀번호를 입력해주세요.");
            }
        }
        return mapper.duoPostToDuoResponseDtoDuoInfo(
                duoService.createDuoPost(
                        mapper.duoPostSaveToDuoPost(memberId, makeDuoPost, riotPuuidAndTierInfo)));
    }


    @PutMapping("/post/{postId}")
    public DuoInfo updateDuoPost(@PathVariable("postId") Long postId,
                                 @RequestAttribute("memberId") Long memberId,
                                 @Valid @RequestBody DuoPostUpdate duoPostUpdate) {
        //일단 puuid 및 티어 확인
        RiotApiResponseDto.RiotPuuidAndTierInfo riotPuuidAndTierInfo = summonerService.checkAndGetTier(duoPostUpdate.getRiotGameName(), duoPostUpdate.getRiotGameTag(), duoPostUpdate.getRegion());


        if (duoPostUpdate.getIsGuestPost()) { //비밀번호 필요한 게시판
            if (duoPostUpdate.isRiotVerified()) {
                //비회원인데 인증된 소환사로 등록하려고 할 때
                throw new IllegalArgumentException("인증된 소환사가 아닙니다.");
            }
            if (duoPostUpdate.getPasswordCheck() == null) {
                throw new IllegalArgumentException("비밀번호를 입력해주세요.");
            }
        }
        return mapper.duoPostToDuoResponseDtoDuoInfo(
                duoService.updateDuoPost(
                        mapper.duoPostUpdateToDuoPost(postId, memberId, duoPostUpdate, riotPuuidAndTierInfo)
                ));
    }

    @DeleteMapping("/post/{postId}")
    public String deleteDuoPost(@PathVariable("postId") Long postId,
                                @RequestAttribute("memberId") Long memberId,
                                @Valid @RequestBody DuoPostDelete duoPostDelete) {
        if (duoPostDelete.getIsGuestPost()) { // 비회원게시물
            if (duoPostDelete.getPasswordCheck() == null) {
                throw new IllegalArgumentException("비밀번호를 입력해주세요.");

            }
        }
        if (!duoPostDelete.getIsGuestPost()) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        log.info("passwordCheck : " + duoPostDelete.getPasswordCheck());
        // 비밀번호가 있다면 비회원 게시물
        if (duoService.deleteDuoPost(postId, memberId, duoPostDelete.getPasswordCheck())) {
            return "delete success";
        }
        return "delete fail";
    }


}
