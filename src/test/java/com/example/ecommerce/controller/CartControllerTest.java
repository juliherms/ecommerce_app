package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ModifyCartRequestDTO;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Item;
import com.example.ecommerce.repositories.CartRepository;
import com.example.ecommerce.repositories.ItemRepository;
import com.example.ecommerce.repositories.UserRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static com.example.ecommerce.utils.TestUtils.*;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

        @InjectMocks
        private CartController cartController;

        @Mock
        private UserRepository userRepository;

        @Mock
        private CartRepository cartRepository;

        @Mock
        private ItemRepository itemRepository;

        @Before
        public void setup(){

            when(userRepository.findByUsername("juliherms")).thenReturn(createUser());
            when(itemRepository.findById(any())).thenReturn(Optional.of(createItem(1)));

        }

        @Test
        public void verifyAddToCartSuccess(){

            //create a item to cart
            ModifyCartRequestDTO request = new ModifyCartRequestDTO();
            request.setQuantity(3);
            request.setItemId(1);
            request.setUsername("juliherms");

            //call endpoint operation
            ResponseEntity<Cart> response = cartController.addToCart(request);
            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());

            Cart actualCart = response.getBody();
            Cart generatedCart = createCart(createUser());

            assertNotNull(actualCart);

            Item item = createItem(request.getItemId());
            BigDecimal itemPrice = item.getPrice();

            BigDecimal expectedTotal = itemPrice.multiply(BigDecimal.valueOf(request.getQuantity())).add(generatedCart.getTotal());

            assertEquals("juliherms", actualCart.getUser().getUsername());
            assertEquals(generatedCart.getItems().size() + request.getQuantity(), actualCart.getItems().size());
            assertEquals(expectedTotal, actualCart.getTotal());

            verify(cartRepository, times(1)).save(actualCart);

        }

        @Test
        public void verifyRemoveFromCartSuccess(){

            ModifyCartRequestDTO request = new ModifyCartRequestDTO();
            request.setQuantity(1);
            request.setItemId(1);
            request.setUsername("juliherms");

            ResponseEntity<Cart> response = cartController.removeFromCart(request);
            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());

            Cart actualCart = response.getBody();

            Cart generatedCart = createCart(createUser());

            assertNotNull(actualCart);

            Item item = createItem(request.getItemId());
            BigDecimal itemPrice = item.getPrice();

            BigDecimal expectedTotal = generatedCart.getTotal().subtract(itemPrice.multiply(BigDecimal.valueOf(request.getQuantity())));

            assertEquals("juliherms", actualCart.getUser().getUsername());
            assertEquals(expectedTotal, actualCart.getTotal());

            verify(cartRepository, times(1)).save(actualCart);

        }

        @Test
        public void verifyInvalidUsernameSuccess(){

            ModifyCartRequestDTO request = new ModifyCartRequestDTO();
            request.setQuantity(1);
            request.setItemId(1);
            request.setUsername("invalidUser");

            ResponseEntity<Cart> removeResponse = cartController.removeFromCart(request);
            assertNotNull(removeResponse);
            assertEquals(404, removeResponse.getStatusCodeValue());
            assertNull(removeResponse.getBody());

            ResponseEntity<Cart> addResponse = cartController.addToCart(request);
            assertNotNull(addResponse);
            assertEquals(404, addResponse.getStatusCodeValue());
            assertNull(addResponse.getBody());

            verify(userRepository, times(2)).findByUsername("invalidUser");

        }
}
