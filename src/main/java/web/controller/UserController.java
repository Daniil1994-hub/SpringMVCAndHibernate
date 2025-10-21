package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.User;
import web.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    private final UserService userServiceImpl;

    @Autowired
    public UserController(UserService userService) {
        this.userServiceImpl = userService;
    }

    @GetMapping("/")
    public String showAllUsers(Model model) {
        List<User> users = userServiceImpl.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute User user,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Возвращаемся на ту же страницу с сообщениями об ошибках
            model.addAttribute("users", userServiceImpl.getAllUsers());
            model.addAttribute("errorMessage", "Ошибка валиадции");
            return "users"; // остаемся на той же странице
        }
        userServiceImpl.saveUser(user);
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
            model.addAttribute("users", userServiceImpl.getAllUsers());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "edit-user";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute User inUser,
                             BindingResult bindingResult, Model model) {
        // Проверяем ошибки валидации
        if (bindingResult.hasErrors()) {
            // Возвращаемся на ту же страницу с сообщениями об ошибках
            model.addAttribute("users", userServiceImpl.getAllUsers());
            model.addAttribute("errorMessage", "Ошибка валиадции");
            return "users"; // остаемся на той же странице
        }
        try {
            User user = userServiceImpl.getUserById(inUser.getId());
            if (user != null) {
                user.setName(inUser.getName());
                user.setLastName(inUser.getLastName());
                user.setEmail(inUser.getEmail());
                userServiceImpl.saveUser(user);
            }
        } catch (EntityNotFoundException e) {
            model.addAttribute("users", userServiceImpl.getAllUsers());
            model.addAttribute("errorMessage", e.getMessage());
            return "users";
        }
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id, Model model) {
        try {
            userServiceImpl.deleteUser(id);
        } catch (EntityNotFoundException e) {
            model.addAttribute("users", userServiceImpl.getAllUsers());
            model.addAttribute("errorMessage", e.getMessage());
            return "users";
        }
        return "redirect:/";
    }
}

