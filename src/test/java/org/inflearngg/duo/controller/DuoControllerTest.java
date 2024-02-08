package org.inflearngg.duo.controller;

import jakarta.transaction.Transactional;
import org.inflearngg.duo.dto.request.DuoRequestDto;
import org.inflearngg.duo.dto.response.DuoResponseDto;
import org.inflearngg.duo.mapper.DuoMapper;
import org.inflearngg.duo.service.DuoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DuoController.class)
class DuoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DuoMapper duoMapper;

    @MockBean
    DuoService duoService;


    @Test
    void getDuoList() throws Exception {
        // given
        String duoSearchJson = "{\n" +
                "  \"lane\": \"\",\n" +
                "  \"queueType\": \"\",\n" +
                "  \"tier\": \"\",\n" +
                "  \"isRiotVerified\": false\n" +
                "}";
        DuoRequestDto.DuoSearch duoSearch = new DuoRequestDto.DuoSearch();
        duoSearch.setLane("");
        duoSearch.setQueueType("");
        duoSearch.setTier("");
        duoSearch.setRiotVerified(false);
        PageRequest pageable = PageRequest.of(1, 10);
        ArrayList<DuoResponseDto.DuoInfo> list = new ArrayList<>();
        given(duoService.getDuoList(1, duoSearch)).willReturn(new PageImpl<>(list, pageable, list.size()));


        // when
        ResultActions results = mockMvc.perform(get("/api/duo/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(duoSearchJson)
                .characterEncoding("utf-8")
        );

        // then
        results.andExpect(status().isOk())
                .andDo(print());


    }

    @Test
    void getDuoPost() {
    }

    @Test
    void createDuoPost() {
    }

    @Test
    void updateDuoPost() {
    }

    @Test
    void deleteDuoPost() {
    }
}