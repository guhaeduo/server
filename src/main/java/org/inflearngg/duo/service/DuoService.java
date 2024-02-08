package org.inflearngg.duo.service;

import lombok.RequiredArgsConstructor;
import org.inflearngg.duo.entity.DuoPost;
import org.inflearngg.duo.repository.DuoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static org.inflearngg.duo.dto.request.DuoRequestDto.*;
import static org.inflearngg.duo.dto.response.DuoResponseDto.*;

@Service
@RequiredArgsConstructor
public class DuoService {

    private final DuoRepository duoRepository;
    private static final int PAGE_SIZE = 10;

    public  Page<DuoInfo> getDuoList(int page, DuoSearch duoSearch) {
        Page<DuoInfo> duoInfos = duoRepository.searchDuoList(duoSearch, PageRequest.of(page, PAGE_SIZE));
        return duoInfos;
    }
    // 상세 조회
    public DuoPost getDuoPost(Long postId) {

        return duoRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));
    }

    public DuoPost createDuoPost(DuoPost makeDuoPost) {
        DuoPost save = duoRepository.save(makeDuoPost);
        return save;
    }

}
