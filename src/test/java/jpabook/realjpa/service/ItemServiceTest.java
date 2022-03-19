package jpabook.realjpa.service;

import jpabook.realjpa.domain.Item;
import jpabook.realjpa.domain.item.Album;
import jpabook.realjpa.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void add() throws Exception{
        Album item = new Album();
        item.setArtist("몰라 개시발롬아");
        Long saveId = itemRepository.save(item);


        assertEquals(item,itemRepository.findOne(saveId));
    }

}