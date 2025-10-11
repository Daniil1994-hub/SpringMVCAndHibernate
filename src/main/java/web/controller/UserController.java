package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.User;
import web.service.UserService;

import java.util.List;

@Controller
    public class UserController {

        @Autowired
        private UserService userService;

        @GetMapping("/")
        public String showAllUsers(Model model) {
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("user", new User()); // для формы добавления
            return "users";
        }

        @PostMapping("/add")
        public String addUser(@RequestParam String name,
                              @RequestParam String lastName,
                              @RequestParam String email) {
            User user = new User(name, lastName, email);
            userService.saveUser(user);
            return "redirect:/";
        }

        @PostMapping("/update")
        public String updateUser(@RequestParam Long id,
                                 @RequestParam String name,
                                 @RequestParam String lastName,
                                 @RequestParam String email) {
            User user = userService.getUserById(id);
            if (user != null) {
                user.setName(name);
                user.setLastName(lastName);
                user.setEmail(email);
                userService.saveUser(user);
            }
            return "redirect:/";
        }

        @GetMapping("/delete")
        public String deleteUser(@RequestParam Long id) {
            userService.deleteUser(id);
            return "redirect:/";
        }

        @GetMapping("/edit")
        public String editUser(@RequestParam Long id, Model model) {
            User user = userService.getUserById(id);
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("user", user);
            return "users";
        }
    }

