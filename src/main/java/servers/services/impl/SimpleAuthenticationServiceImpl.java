package servers.services.impl;

import servers.handlers.ClientHandler;
import servers.models.User;
import servers.services.AuthenticationService;

import java.util.List;

public class SimpleAuthenticationServiceImpl implements AuthenticationService {

    private static final List<User> clients = List.of(
      new User("martin", "1111","Matrin_Superstar"),
      new User("batman","1111", "Brus"),
      new User("mario","1111","Super_Mario"),
      new User("bender", "1111", "Iron_bender")
    );



    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        String clientz ="";

        for (User client: clients){
            if (client.getLogin().equals(login) && client.getPassword().equals(password)){
                clientz = client.getUsername();
                return client.getUsername();
            }
        }
        return clientz;
    }

    @Override
    public void createNewUSer(String user) {

    }

    @Override
    public void updateUsernameByNickname(ClientHandler handler, String newUsername) {

    }
}
