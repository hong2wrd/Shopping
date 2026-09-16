package com.shopping.item.controller;

import com.shopping.item.dto.request.CategorySaveReqDto;
import com.shopping.item.dto.request.CategoryUpdateReqDto;
import com.shopping.item.dto.response.CategoryRespDto;
import com.shopping.item.entity.culture.Book;
import com.shopping.item.entity.Category;
import com.shopping.item.entity.clothes.Top;
import com.shopping.item.entity.clothes.Size;
import com.shopping.item.repository.CategoryRepository;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class CategoryControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    CategoryRespDto categoryRespDto;

    Long categoryId;

    @BeforeEach
    void setUp() {
        Member member = Member.create(
                "tester",
                "testName",
                passwordEncoder.encode("123456")
                );
        memberRepository.save(member);

        Book book1 = itemRepository.save(Book.create(10000, 100, "test book1", "tester1"));
        Book book2 = itemRepository.save(Book.create(20000, 200, "test book2", "tester1"));
        Category bookCategory = Category.create("도서");
        bookCategory.addItem(book1);
        bookCategory.addItem(book2);

        Category category = categoryRepository.save(bookCategory);

        Category clothesCategory = Category.create("옷");
        Top top1 = itemRepository.save(Top.create(1000, 10, "test etc1", Size.S));
        Top top2 = itemRepository.save(Top.create(1000, 10, "test etc1", Size.M));
        Top top3 = itemRepository.save(Top.create(1000, 10, "test etc1", Size.L));
        Top top4 = itemRepository.save(Top.create(1000, 10, "test etc1", Size.XL));
        clothesCategory.addItem(top1);
        clothesCategory.addItem(top2);
        clothesCategory.addItem(top3);
        clothesCategory.addItem(top4);

        Category category2 = categoryRepository.save(clothesCategory);
        category.addChildCategory(category2);

        categoryId = clothesCategory.getId();

        categoryRespDto = CategoryRespDto.builder()
                .id(category.getId())
                .name(category.getName())
                .children(null)
                .items(null)
                .build();
    }

    @Test
    @DisplayName("모든 카테고리 조회 성공")
    @WithUserDetails(value = "tester", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void success_get_all_categories() throws Exception {

        // when
        ResultActions resultActions = mvc.perform(get("/category"));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(1))
            .andExpect(jsonPath("$.msg").value("모든 카테고리를 조회하였습니다."))
            .andExpect(jsonPath("$.data[0].id").value(categoryRespDto.getId()))
            .andExpect(jsonPath("$.data[0].name").value(categoryRespDto.getName()))
            .andExpect(jsonPath("$.data[0].children").isArray())
            .andExpect(jsonPath("$.data[0].items").isArray());
    }

    @Test
    @DisplayName("특정 카테고리 조회 성공")
    @WithUserDetails(value = "tester", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void success_get_categories() throws Exception {

        // when
        ResultActions resultActions = mvc.perform(get("/category/" + (categoryRespDto.getId() + 1L)));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.msg").value("요청한 카테고리를 조회하였습니다."))
                .andExpect(jsonPath("$.data.id").value(categoryRespDto.getId() + 1L))
                .andExpect(jsonPath("$.data.name").value("옷"))
                .andExpect(jsonPath("$.data.children").isArray())
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.items[*].itemType",
                        everyItem(is("TOP"))))
                .andExpect(jsonPath("$.data.items[*].size",
                        containsInAnyOrder(
                                "S", "M", "L", "XL"
                        )));;
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 조회 실패")
    @WithUserDetails(value = "tester", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void fail_get_categories_not_found() throws Exception {

        // when
        ResultActions resultActions = mvc.perform(get("/category/" + 0L));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("카테고리 저장1 성공")
    @WithUserDetails(value = "tester", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void success_save_category1() throws Exception {

        // when
        ResultActions resultActions = mvc.perform(post("/category/" + "수선" ));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("카테고리 저장2 성공")
    @WithUserDetails(value = "tester", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void success_save_category2() throws Exception {
        CategorySaveReqDto saveDto = new CategorySaveReqDto();
        saveDto.setName("수선");
        saveDto.setParentId(categoryId);

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(saveDto);

        // when
        ResultActions resultActions = mvc.perform(post("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("카테고리 변경 성공")
    @WithUserDetails(value = "tester", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void success_change_category2() throws Exception {
        CategoryUpdateReqDto saveDto = new CategoryUpdateReqDto();
        saveDto.setId(categoryId);
        saveDto.setName("수선");

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(saveDto);

        // when
        ResultActions resultActions = mvc.perform(put("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        categoryRepository.flush();

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    @WithUserDetails(value = "tester", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void success_delete_category2() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(delete("/category/" + categoryId));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        categoryRepository.flush();

        // then
        resultActions.andExpect(status().isOk());
    }
}