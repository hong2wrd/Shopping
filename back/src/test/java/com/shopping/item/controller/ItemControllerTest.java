package com.shopping.item.controller;

import com.shopping.item.dto.request.ItemSaveReqDto;
import com.shopping.item.entity.culture.Book;
import com.shopping.item.entity.ItemType;
import com.shopping.item.repository.ItemRepository;
import com.shopping.member.entity.Member;
import com.shopping.member.entity.Role;
import com.shopping.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class ItemControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    Long saveId;

    @BeforeEach
    void setUp() {
        Member member = Member.create(
                "tester",
                "testName",
                passwordEncoder.encode("123456")
                );

        Member seller = Member.create(
                "seller",
                "sellerName",
                passwordEncoder.encode("123456")
                );
        seller.changeRole(Role.SELLER);
        memberRepository.saveAll(List.of(member, seller));

        Book book = Book.create(1000, 5, "testName", "tester");
        saveId = itemRepository.save(book).getId();
    }

    @Test
    @DisplayName("상품 조회 성공")
    @WithUserDetails(value = "tester", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void success_search_item_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/item/" + saveId));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        //than
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 조회 실패")
    @WithUserDetails(value = "tester", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void fail_search_item_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/item/" + saveId + 1000));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // than
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("상품 저장 성공")
    @WithUserDetails(value = "seller", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void success_save_item_test() throws Exception {
        // given
        ItemSaveReqDto itemSaveReqDto = new ItemSaveReqDto();
        itemSaveReqDto.setAuthor("AuthorTest");
        itemSaveReqDto.setItemtype(ItemType.valueOf("BOOK"));
        itemSaveReqDto.setName("testName");
        itemSaveReqDto.setPrice(100000);
        itemSaveReqDto.setQuantity(1234);

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(itemSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(
                post("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("상품 저장 실패(수량이 마이너스일 경우)")
    @WithUserDetails(value = "seller", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void fail_save_item_test1() throws Exception {
        // given
        ItemSaveReqDto itemSaveReqDto = new ItemSaveReqDto();
        itemSaveReqDto.setAuthor("AuthorTest");
        itemSaveReqDto.setItemtype(ItemType.valueOf("BOOK"));
        itemSaveReqDto.setName("testName");
        itemSaveReqDto.setPrice(100000);
        itemSaveReqDto.setQuantity(-1);

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(itemSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(
                post("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 저장 실패(가격이 마이너스일 경우)")
    @WithUserDetails(value = "seller", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void fail_save_item_test2() throws Exception {
        // given
        ItemSaveReqDto itemSaveReqDto = new ItemSaveReqDto();
        itemSaveReqDto.setAuthor("AuthorTest");
        itemSaveReqDto.setItemtype(ItemType.valueOf("BOOK"));
        itemSaveReqDto.setName("testName");
        itemSaveReqDto.setPrice(0);
        itemSaveReqDto.setQuantity(10000);

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(itemSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(
                post("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
    }
}