package org.inflearngg.duo.service;

import lombok.RequiredArgsConstructor;
import org.inflearngg.duo.dto.request.DuoRequestDto;
import org.inflearngg.duo.entity.DuoPost;
import org.inflearngg.duo.repository.DuoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DuoService {

    private final DuoRepository duoRepository;

    public void getDuoList(int page, DuoRequestDto.DuoSearch duoSearch) {
        if(duoSearch.getLane() == null) {
            Page<DuoPost> duoPostList = duoRepository.findAll(PageRequest.of(page, 10));
        } else {
            Page<DuoPost> duoPostList = duoRepository.findByNeedPosition(duoSearch.getLane(), PageRequest.of(page, 10));
        }
    }
}
