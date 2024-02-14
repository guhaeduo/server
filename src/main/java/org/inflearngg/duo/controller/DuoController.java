package org.inflearngg.duo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.inflearngg.duo.mapper.DuoMapper;
import org.inflearngg.duo.service.DuoService;
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
    private final DuoMapper mapper;

    @GetMapping("/{page}")
    public Page<DuoInfo> getDuoList(@PathVariable("page") int page, @Valid @RequestBody DuoSearch duoSearch) {
        // 추후 mapper 사용하기
        return duoService.getDuoList(page, duoSearch);
    }

    @GetMapping("/post/{postId}")
    public DuoInfo getDuoPost(@PathVariable Long postId) { // 상세조회
        return mapper.duoPostToDuoResponseDtoDuoInfo(
                duoService.getDuoPost(postId)
        );
    }

    @PostMapping("/post")
    public DuoInfo createDuoPost(@RequestBody DuoPostSave makeDuoPost) {
        log.info("makeDuoPost.isLogin : " + makeDuoPost.getLogin());
        log.info("makeDuoPost.isRiotVerified : " + makeDuoPost.isRiotVerified());
        return mapper.duoPostToDuoResponseDtoDuoInfo(
                duoService.createDuoPost(
                        mapper.duoPostSaveToDuoPost(makeDuoPost)));
    }


    @PatchMapping("/post/{postId}")
    public DuoInfo updateDuoPost(@PathVariable Long postId, @RequestBody DuoPostUpdate duoPostUpdate) {
        return mapper.duoPostToDuoResponseDtoDuoInfo(
                duoService.updateDuoPost(
                        postId,
                        duoPostUpdate.getMemberId(),
                        duoPostUpdate.getPasswordCheck(),
                        mapper.duoPostUpdateToDuoPost(duoPostUpdate)
                ));
    }

    @DeleteMapping("/post/{postId}")
    public String deleteDuoPost(@PathVariable Long postId, @RequestBody DuoPostDelete duoPostDelete) {
        if(duoService.deleteDuoPost(postId, duoPostDelete.getIsLogin(), duoPostDelete.getDeleteInfo())) {
            return "delete success";
        }
        return "delete fail";
    }



}
