package jpabook.realjpa.service;

import jpabook.realjpa.domain.Item;
import jpabook.realjpa.domain.item.Book;
import jpabook.realjpa.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item){
        return itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Book bookParam){
        Item findItem = findOne(bookParam.getId());
        findItem.setId(bookParam.getId());
        findItem.setPrice(bookParam.getPrice());
        findItem.setName(bookParam.getName());
        findItem.setStockQuantity(bookParam.getStockQuantity());
        /*findItem.setAuthor(bookParam.getAuthor());
        findItem.setIsbn(bookParam.getIsbn());*/
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
