package org.inflearngg.duo.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.inflearngg.duo.dto.request.DuoRequestDto;
import org.inflearngg.duo.dto.response.DuoResponseDto;
import org.inflearngg.duo.service.DuoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/duo")
@RequiredArgsConstructor
public class DuoController {

    private final DuoService duoService;

    @GetMapping("/{page}")
    public DuoResponseDto.PageInfo getDuoList(@PathVariable int page, @RequestBody DuoRequestDto.DuoSearch duoSearch) {
        duoService.getDuoList(page, duoSearch);

        return null;
    }

    @GetMapping("/post/{postId}")
    public String getDuoPost(Long postId) {
        return "hello";
    }

    @PatchMapping("/post/{postId}")
    public String updateDuoPost(Long postId) {
        return "hello";
    }

    @DeleteMapping("/post/{postId}")
    public String deleteDuoPost(Long postId) {
        return "hello";
    }

}
