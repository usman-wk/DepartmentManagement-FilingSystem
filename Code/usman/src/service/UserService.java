package service;

import model.User;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final List<User> users = new ArrayList<>();

    public void addUser(User u)                { users.add(u); }
    public User getUserById(int id)            { return users.stream()
                                                  .filter(x -> x.getUserId()==id)
                                                  .findFirst().orElse(null); }
    public List<User> getAllUsers()            { return users; }
}
