package org.inflearngg.duo.service;

import lombok.RequiredArgsConstructor;
import org.inflearngg.duo.dto.request.DuoRequestDto;
import org.inflearngg.duo.dto.response.DuoResponseDto;
import org.inflearngg.duo.entity.DuoPost;
import org.inflearngg.duo.repository.DuoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DuoService {

    private final DuoRepository duoRepository;

    private final int PAGE_SIZE = 10;

    public  Page<DuoResponseDto.DuoInfo> getDuoList(int page, DuoRequestDto.DuoSearch duoSearch) {
        Page<DuoResponseDto.DuoInfo> duoInfos = duoRepository.searchDuoList(duoSearch, PageRequest.of(page, PAGE_SIZE));
        return duoInfos;
    }
}
