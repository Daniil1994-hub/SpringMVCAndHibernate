package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.User;
import web.service.UserServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/")
    public String showAllUsers(Model model) {
        List<User> users = userServiceImpl.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam String name,
                          @RequestParam String lastName,
                          @RequestParam String email,
                          Model model) {
        try {
            User user = new User(name, lastName, email);
            userServiceImpl.saveUser(user);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String editUserPage(@RequestParam Long id, Model model) {
        try {
            User user = userServiceImpl.getUserById(id);
            if (user != null) {
                model.addAttribute("user", user);
                System.out.println("Editing user: " + user);
            }
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "edit-user"; // отдельная страница для редактирования
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User inUser, Model model) {
        try {
            User user = userServiceImpl.getUserById(inUser.getId());
            if (user != null) {
                user.setName(inUser.getName());
                user.setLastName(inUser.getLastName());
                user.setEmail(inUser.getEmail());
                userServiceImpl.saveUser(user);
            }
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id, Model model) {
        try {
            userServiceImpl.deleteUser(id);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/";
    }
}

