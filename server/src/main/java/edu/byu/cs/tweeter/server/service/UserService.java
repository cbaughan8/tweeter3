package edu.byu.cs.tweeter.server.service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.beans.AuthTokenBean;
import edu.byu.cs.tweeter.server.dao.beans.UserBean;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.ImageDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {

    UserDAO userDAO;

    AuthTokenDAO authTokenDAO;
    ImageDAO imageDAO;

    byte[] universal_salt = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public UserService(UserDAO userDAO, AuthTokenDAO authTokenDAO) {
        this.authTokenDAO = authTokenDAO;
        this.userDAO = userDAO;
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserService(UserDAO userDAO, AuthTokenDAO authTokenDAO ,ImageDAO imageDAO) {
        this.userDAO = userDAO;
        this.imageDAO = imageDAO;
        this.authTokenDAO = authTokenDAO;
    }

    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        UserBean userBean = getUserDAO().get(request.getUsername());
        if (userBean.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Invalid username");
        }
        if (!validatePassword(request.getPassword(), userBean.getPassword_hash(), getUniversal_salt())) {
            throw new RuntimeException("[Bad Request] Incorrect password");
        }
        long timestamp = System.currentTimeMillis();
        AuthToken token = generateAuthToken(timestamp);

        AuthTokenBean authTokenBean = new AuthTokenBean(token.getToken(), timestamp, request.getUsername());
        System.out.println(authTokenBean);
        getAuthTokenDAO().create(authTokenBean);
        return new LoginResponse(new User(userBean.getFirst_name(), userBean.getLast_name(),
                userBean.getAlias(), userBean.getImage_url()), token);
    }

    private boolean validatePassword(String password, int password_hash, byte[] salt) {
        int new_hash = hashPassword(password, salt);
        return new_hash == password_hash;
    }

    public RegisterResponse register(RegisterRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if(request.getFirstName() == null) {
        throw new RuntimeException("[Bad Request] Missing a first name");
        } else if(request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        } else if(request.getImageBytesBase64() == null) {
            throw new RuntimeException("[Bad Request] Missing an image");
        }

        byte[] salt = getUniversal_salt();
        int hash = hashPassword(request.getPassword(), salt);
        String url = getImageDAO().addImage(request.getImageBytesBase64(), request.getUsername());

        UserBean userBean = new UserBean(request.getUsername(), request.getFirstName(),
                request.getLastName(), url,
                hash, 0 ,0);

        long timestamp = System.currentTimeMillis();
        AuthToken token = generateAuthToken(timestamp);

        AuthTokenBean authTokenBean = new AuthTokenBean(token.getToken(), timestamp, request.getUsername());
        System.out.println(authTokenBean);
        getAuthTokenDAO().create(authTokenBean);

        getUserDAO().create(userBean);
        AuthToken authToken = generateAuthToken(System.currentTimeMillis());
        User user = new User(request.getFirstName(), request.getLastName(),request.getUsername(), url);
        return new RegisterResponse(user, authToken);
    }

    //TODO: Check if logout deletes authtoken
    public LogoutResponse logout(LogoutRequest request) {
        if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authToken");
        }
        try {
            getAuthTokenDAO().delete(request.getAuthToken().getToken());
            return new LogoutResponse();
        } catch (Exception e) {
            return new LogoutResponse(e.getMessage());
        }
    }

    public GetUserResponse getUser(GetUserRequest request) {
        if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing an authToken");
        } else if(request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Missing an alias");
        }

        UserBean userBean = getUserDAO().get(request.getAlias());

        return new GetUserResponse(new User(userBean.getFirst_name(),userBean.getLast_name(),
                userBean.getAlias(), userBean.getImage_url()));

    }

    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }



    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
    public int hashPassword(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return ByteBuffer.wrap(hash).getInt();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    AuthToken generateAuthToken(long timestamp){
        byte[] array = new byte[12]; // length is bounded by 7
        new Random().nextBytes(array);
        String tokenString = new String(array, StandardCharsets.UTF_8);
        Date date = new Date(timestamp);
        String dateTime = String.valueOf(date.getTime()) + String.valueOf(date.getDate());
        return new AuthToken(tokenString, dateTime);
    }

    public byte[] getUniversal_salt() {
        return universal_salt;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
    public ImageDAO getImageDAO() {
        return imageDAO;
    }

    public AuthTokenDAO getAuthTokenDAO() {
        return authTokenDAO;
    }
}
