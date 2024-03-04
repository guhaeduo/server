//package org.inflearngg.duo.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.transaction.Transactional;
//import org.inflearngg.duo.dto.request.DuoRequestDto;
//import org.inflearngg.duo.dto.response.DuoResponseDto;
//import org.inflearngg.duo.entity.DuoPost;
//import org.inflearngg.duo.mapper.DuoMapper;
//import org.inflearngg.duo.service.DuoService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.BDDMockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = DuoController.class)
//class DuoControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    DuoMapper duoMapper;
//
//    @MockBean
//    DuoService duoService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//
//    @BeforeEach
//    public void setUp() {
//        // 구현체 주입
////        mockMvc = MockMvcBuilders.standaloneSetup(new DuoController(duoService, duoMapper)).build();
//
//
//    }
//
//    //필터를 다 적용했을때
//    @Test
//    @DisplayName("필터를 다 적용했을때")
//    void getDuoListAllFilter() throws Exception {
//        // given
//        DuoRequestDto.DuoSearch duoSearch = new DuoRequestDto.DuoSearch("TOP", "440", "IRON", false);
//        String json = objectMapper.writeValueAsString(duoSearch);
//        PageRequest pageable = PageRequest.of(1, 10);
//        int page = 1;
//
//
//        //service 응답값
//        ArrayList<DuoResponseDto.DuoInfo> list = new ArrayList<>();
//        given(duoService.getDuoList(1, duoSearch)).willReturn(new PageImpl<>(list, pageable, 11));
//
//
//        // when
//        ResultActions results = mockMvc.perform(get("/api/duo/{page}", page)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)
//                .characterEncoding("utf-8")
//        );
//
////        // then
////        results.andExpect(status().isOk())
////                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("필터를 일부 적용 했을때")
//    void getDuoListPartFilter() throws Exception {
//        // given
//        DuoRequestDto.DuoSearch duoSearch = new DuoRequestDto.DuoSearch("TOP", "SOLO");
//        String json = objectMapper.writeValueAsString(duoSearch);
//        PageRequest pageable = PageRequest.of(1, 10);
//
//        //service 응답값
//        ArrayList<DuoResponseDto.DuoInfo> list = new ArrayList<>();
//        given(duoService.getDuoList(1, duoSearch)).willReturn(new PageImpl<>(list, pageable, 11));
//
//
//        // when
//        ResultActions results = mockMvc.perform(get("/api/duo/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)
//                .characterEncoding("utf-8")
//        );
//
////        // then
////        results.andExpect(status().isOk())
////                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("잘못된 요청값이 들어왔을 때")
//    void getDuoListBadRequest() throws Exception {
//        // given
//        DuoPost duoPost = new DuoPost();
//        String json = objectMapper.writeValueAsString(duoPost);
//        PageRequest pageable = PageRequest.of(1, 10);
//
//        //service 응답값
//        ArrayList<DuoResponseDto.DuoInfo> list = new ArrayList<>();
//        // 잘못된 값이라면 여기서 이미 에러가 나야됨.
//        // validate 를 통해 검증을 해야함.
//        given(duoService.getDuoList(1, null)).willReturn(new PageImpl<>(list, pageable, 11));
//
//    }
//
//
//    @Test
//    @DisplayName("필터를 아무것도 적용하지 않았을때")
//    void getDuoListNothingFilter() throws Exception {
//        // given
//        DuoRequestDto.DuoSearch duoSearch = new DuoRequestDto.DuoSearch();
//        String json = objectMapper.writeValueAsString(duoSearch);
//        PageRequest pageable = PageRequest.of(1, 10);
//
//        //service 응답값
//        ArrayList<DuoResponseDto.DuoInfo> list = new ArrayList<>();
//        given(duoService.getDuoList(1, duoSearch)).willReturn(new PageImpl<>(list, pageable, 11));
//
//
//        // when
//        ResultActions results = mockMvc.perform(get("/api/duo/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)
//                .characterEncoding("utf-8")
//        );
//
////        // then
////        results.andExpect(status().isOk())
////                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("듀오 게시판 상세 조회")
//    void getDuoPost() {
//        // given
//    }
//
//    @Test
//    @DisplayName("듀오 게시판 생성")
//    void createDuoPost() throws Exception {
//        String saveDuoPostJson = "{\n" +
//                "  \"postId\": 1,\n" +
//                "  \"riotGameName\": \"test\",\n" +
//                "  \"riotGameTag\": \"test\",\n" +
//                "  \"pUuid\": \"test\",\n" +
//                "  \"isRiotVerified\": false,\n" +
//                "  \"needPosition\": \"test\",\n" +
//                "  \"needQueueType\": \"test\",\n" +
//                "  \"needTier\": \"test\",\n" +
//                "  \"myPosition\": {\n" +
//                "    \"main\": {\n" +
//                "      \"lane\": \"test\",\n" +
//                "      \"championName\": \"test\",\n" +
//                "      \"championIconNumber\": 1\n" +
//                "    },\n" +
//                "    \"sub\": {\n" +
//                "      \"lane\": \"test\",\n" +
//                "      \"championName\": \"test\",\n" +
//                "      \"championIconNumber\": 1\n" +
//                "    }\n" +
//                "  },\n" +
//                "  \"isMicOn\": false,\n" +
//                "  \"memo\": \"test\"\n" +
//                "}";
//        DuoRequestDto.DuoPostSave duoPostSave = new DuoRequestDto.DuoPostSave();
//        duoPostSave.setRiotGameName("test");
//        duoPostSave.setRiotGameTag("test");
//        duoPostSave.setPuuid("test");
//        duoPostSave.setRiotVerified(false);
//        duoPostSave.setNeedPosition("test");
//        duoPostSave.setNeedQueueType("test");
//        duoPostSave.setNeedTier("test");
//        DuoRequestDto.Position position = new DuoRequestDto.Position();
//        DuoRequestDto.Position.Main main = new DuoRequestDto.Position.Main("test", "test", 1);
//        DuoRequestDto.Position.Sub sub = new DuoRequestDto.Position.Sub("test", "test", 1);
//        position.setMain(main);
//        position.setSub(sub);
//        duoPostSave.setMyPosition(position);
//        duoPostSave.setMicOn(false);
//        duoPostSave.setMemo("test");
//        DuoResponseDto.DuoInfo duoInfo = new DuoResponseDto.DuoInfo(1L, "test", "test", "test", false, "test", "test", "test", "test", "test", 1, "test", "test", 1, false, "test");
//        given(duoMapper.duoPostSaveToDuoPost(duoPostSave)).willReturn(new DuoPost());
//        given(duoMapper.duoPostToDuoResponseDtoDuoInfo(new DuoPost())).willReturn(duoInfo);
//        given(duoService.createDuoPost(new DuoPost())).willReturn(new DuoPost());
//        // when
//        ResultActions results = mockMvc.perform(post("/api/duo/post")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(saveDuoPostJson)
//                .characterEncoding("utf-8")
//        );
////        // then
////        results.andExpect(status().isOk())
////                .andDo(print());
//
//    }
//
////    @Test
////    void updateDuoPost() throws Exception {
////        // given
////        String updateDuoPostJson = "{\n" +
////                "  \"postId\": 1,\n" +
////                "  \"riotGameName\": \"test\",\n" +
////                "  \"riotGameTag\": \"test\",\n" +
////                "  \"isRiotVerified\": false,\n" +
////                "  \"needPosition\": \"test\",\n" +
////                "  \"needQueueType\": \"test\",\n" +
////                "  \"needTier\": \"test\",\n" +
////                "  \"myPosition\": {\n" +
////                "    \"main\": {\n" +
////                "      \"lane\": \"test\",\n" +
////                "      \"championName\": \"test\",\n" +
////                "      \"championIconNumber\": 1\n" +
////                "    },\n" +
////                "    \"sub\": {\n" +
////                "      \"lane\": \"test\",\n" +
////                "      \"championName\": \"test\",\n" +
////                "      \"championIconNumber\": 1\n" +
////                "    }\n" +
////                "  },\n" +
////                "  \"isMicOn\": false,\n" +
////                "  \"memo\": \"test\"\n" +
////                "}";
////        DuoRequestDto.DuoPostUpdate duoPostUpdate = new DuoRequestDto.DuoPostUpdate();
////        duoPostUpdate.setPostId(1L);
////        duoPostUpdate.setRiotGameName("test");
////        duoPostUpdate.setRiotGameTag("test");
////        duoPostUpdate.setIsRiotVerified(false);
////        duoPostUpdate.setNeedPosition("test");
////        duoPostUpdate.setNeedQueueType("test");
////        duoPostUpdate.setNeedTier("test");
////        DuoRequestDto.Position position = new DuoRequestDto.Position();
////        DuoRequestDto.Position.Main main = new DuoRequestDto.Position.Main("test", "test", 1);
////        DuoRequestDto.Position.Sub sub = new DuoRequestDto.Position.Sub("test", "test", 1);
////        position.setMain(main);
////        position.setSub(sub);
////        duoPostUpdate.setMyPosition(position);
////        duoPostUpdate.setIsMicOn(false);
////        duoPostUpdate.setMemo("test");
////
////        DuoResponseDto.DuoInfo duoInfo = new DuoResponseDto.DuoInfo(1L, "test", "test", "test", false, "test", "test", "test", "test", "test", 1, "test", "test", 1, false, "test");
////        given(duoMapper.duoPostUpdateToDuoPost(duoPostUpdate)).willReturn(new DuoPost());
////        given(duoMapper.duoPostToDuoResponseDtoDuoInfo(new DuoPost())).willReturn(duoInfo);
////        given(duoService.updateDuoPost(1L, 1L, "test", new DuoPost())).willReturn(new DuoPost());
////
////        // when
////        ResultActions results = mockMvc.perform(post("/api/duo/post/1")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(updateDuoPostJson)
////                .characterEncoding("utf-8")
////        );
////
////        // then
////        results.andExpect(status().isOk())
////                .andDo(print());
////
////    }
//
//    @Test
//    void deleteDuoPost() {
//
//    }
//}