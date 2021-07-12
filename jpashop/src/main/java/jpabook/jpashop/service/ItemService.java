package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    //준영속 엔티티 수정하는 방법
    //1. 변경감지 기능 사용
    //2. Merge(병합) 기능
    // 아래의 updateItem은 변경 감지를 사용한 코드이다. 병합은 10개의 요소중
    // 5개만 수정하더라도 전부 JPA가 값을 가져가기 때문에 없는 값은 null로 인식해버린다.
    // 실무에서는 10개의 값중 일부분만 수정하게 만들기 때문에 그냥 변경감지를 통해 준영속
    // Entity를 수정하면 된다고 알면 된다.
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItem(){
        return  itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
