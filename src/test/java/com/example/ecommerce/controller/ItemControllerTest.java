package com.example.ecommerce.controller;

import com.example.ecommerce.controller.ItemController;
import com.example.ecommerce.model.Item;
import com.example.ecommerce.repositories.ItemRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static com.example.ecommerce.utils.TestUtils.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setup(){

        when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem(1)));
        when(itemRepository.findAll()).thenReturn(createItems());
        when(itemRepository.findByName("item")).thenReturn(Arrays.asList(createItem(1), createItem(2)));

    }

    @Test
    public void verifyGetItensSuccess(){

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();

        assertEquals(createItems().size(), items.size());

        verify(itemRepository , times(1)).findAll();
    }

    @Test
    public void verifyGetItemByIdSuccess(){

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertEquals(createItem(1L).getDescription(), item.getDescription());
        assertEquals(createItem(1L).getId(), item.getId());

        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    public void verifyGetItemByIdInvalidSuccess(){

        ResponseEntity<Item> response = itemController.getItemById(10L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        assertNull(response.getBody());
        verify(itemRepository, times(1)).findById(10L);
    }

    @Test
    public void verigyGetItemByNameSuccess(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = Arrays.asList(createItem(1), createItem(2));

        assertEquals(createItems().size(), items.size());

        verify(itemRepository , times(1)).findByName("item");
    }

    @Test
    public void verifyGetItemByNameInvalidSuccess(){

        ResponseEntity<List<Item>> response = itemController.getItemsByName("invalid name");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        assertNull(response.getBody());

        verify(itemRepository , times(1)).findByName("invalid name");
    }
}
