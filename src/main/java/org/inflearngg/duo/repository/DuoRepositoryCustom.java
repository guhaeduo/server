package org.inflearngg.duo.repository;

import org.inflearngg.duo.dto.request.DuoRequestDto;
import org.inflearngg.duo.dto.response.DuoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DuoRepositoryCustom {
    Page<DuoResponseDto.DuoInfo> searchDuoList(DuoRequestDto.DuoSearch duoSearch, Pageable pageable);
}
