package edu.byu.cs.tweeter.client.model.net;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.SecureRandom;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

class ServerFacadeTest {

    ServerFacade serverFacade;
    AuthToken mockToken;

    User mockUser;


    @BeforeEach
    void setup() {
        serverFacade = new ServerFacade();
        mockToken = Mockito.mock(AuthToken.class);
        mockUser = Mockito.mock(User.class);
        Mockito.when(mockUser.getAlias()).thenReturn("@allen");
    }



    @Test
    void testRegister() {
        String path = "/register";
        RegisterRequest request = new RegisterRequest("carson", "baughan",
                "password", "@username", "image");
        try {
            RegisterResponse response = serverFacade.register(request, path);
            assertTrue(response.isSuccess());
            assertEquals("@allen", response.getUser().getAlias());
            assertNotNull(response.getAuthToken());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Test
    void testGetFollowers() {
        String path = "/getfollowers";
        FollowersRequest request = new FollowersRequest(mockToken, "@frank", 5, "@allen");
        try {
            FollowersResponse response = serverFacade.getFollowers(request, path);
            List<User> followers = response.getFollowers();
            assertTrue(response.isSuccess());
            assertEquals(5, followers.size());
            assertEquals("@amy", followers.get(0).getAlias());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testGetFollowersUserIncluded() {
        String path = "/getfollowers";
        FollowersRequest request = new FollowersRequest(mockToken, "@amy", 5, "@allen");
        try {
            FollowersResponse response = serverFacade.getFollowers(request, path);
            List<User> followers = response.getFollowers();
            assertTrue(response.isSuccess());
            assertEquals(4, followers.size());
            assertEquals("@bob", followers.get(0).getAlias());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testGetFollowersCount() {
        String path = "/getfollowerscount";
        FollowersCountRequest request = new FollowersCountRequest(mockToken, mockUser);
        try {
            FollowersCountResponse response = serverFacade.getFollowersCount(request, path);
            assertTrue(response.isSuccess());
            assertEquals(20, response.getCount());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testGetFollowingCount() {
        String path = "/getfollowingcount";
        FollowingCountRequest request = new FollowingCountRequest(mockToken, mockUser);
        try {
            FollowingCountResponse response = serverFacade.getFollowingCount(request, path);
            assertTrue(response.isSuccess());
            assertEquals(20, response.getCount());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}