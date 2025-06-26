package com.studyspringdatajpa.repository;

import com.studyspringdatajpa.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(value = "local")
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    public void save() {
        Item item = new Item("A001");
        itemRepository.save(item);
    }

}