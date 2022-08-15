package servers.services;

import servers.handlers.ClientHandler;

public interface AuthenticationService {
    String getUsernameByLoginAndPassword(String login, String password);
    public void createNewUSer(String user);
    public void updateUsernameByNickname(ClientHandler handler, String newUsername);

}
