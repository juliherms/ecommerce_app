package com.example.ecommerce.controller;


import com.example.ecommerce.model.User;
import com.example.ecommerce.model.UserOrder;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.repositories.UserRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import static com.example.ecommerce.utils.TestUtils.*;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Before
    public void setup(){
        User user = createUser();

        when(userRepository.findByUsername("juliherms")).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(createOrders());
    }

    @Test
    public void verifySubmitSuccess(){

        ResponseEntity<UserOrder> response = orderController.submit("juliherms");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(createItems().size(), order.getItems().size());
        assertEquals(createUser().getId(), order.getUser().getId());


        verify(orderRepository, times(1)).save(order);

    }

    @Test
    public void verifySubmitInvalidSuccess(){

        ResponseEntity<UserOrder> response = orderController.submit("invalid username");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        assertNull( response.getBody());

        verify(userRepository, times(1)).findByUsername("invalid username");
    }

    @Test
    public void verifyGetOrdersForUserSuccess(){

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("juliherms");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();

        assertNotNull(orders);
        assertEquals(createOrders().size(), orders.size());

    }

}
