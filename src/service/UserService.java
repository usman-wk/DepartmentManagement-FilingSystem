package service;

import model.User;
import model.Role;
import utils.FileHandler;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class UserService {
    private static UserService instance;
    private static final String USER_FILE = "data/users.txt";
    private List<User> users;

    private UserService() {
        users = FileHandler.loadFromFile(USER_FILE, User::fromString);
        if (users == null) {
            users = new ArrayList<>();
        }
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User login(String username, String password) {
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getUsername().equals(username) && 
                           u.getPassword().equals(password) && 
                           u.isActive())
                .findFirst();
        return userOpt.orElse(null);
    }

    public boolean addUser(User user) {
        // Check if username already exists
        if (users.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
            return false;
        }
        
        // Generate new ID
        int newId = users.stream().mapToInt(User::getId).max().orElse(0) + 1;
        user.setId(newId);
        
        users.add(user);
        saveUsers();
        return true;
    }

    public boolean updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                saveUsers();
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(int userId) {
        boolean removed = users.removeIf(u -> u.getId() == userId);
        if (removed) {
            saveUsers();
        }
        return removed;
    }

    public User getUserById(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public User getUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public List<User> getUsersByRole(Role role) {
        return users.stream()
                .filter(u -> u.getRole() == role && u.isActive())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<User> getUsersByDepartment(int departmentId) {
        return users.stream()
                .filter(u -> u.getDepartmentId() == departmentId && u.isActive())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);
        if (user != null && user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            updateUser(user);
            return true;
        }
        return false;
    }

    public boolean resetPassword(String username, String email, String newPassword) {
        User user = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        
        if (user != null) {
            user.setPassword(newPassword);
            updateUser(user);
            return true;
        }
        return false;
    }

    private void saveUsers() {
        FileHandler.saveToFile(USER_FILE, users);
    }
}