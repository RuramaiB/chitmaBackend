package me.ruramaibotso.umc.auth;

import me.ruramaibotso.umc.user.User;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String s) {
        User user = new User();
        user.isEnabled();
        // TODO: Regex to validate email
        return true;
    }
}
