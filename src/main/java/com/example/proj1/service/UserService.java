package com.example.proj1.service;

import com.example.proj1.exceptions.NullArgumentException;
import com.example.proj1.exceptions.UserCoreException;
import com.example.proj1.model.UserPrivateDto;
import com.example.proj1.model.UserLoginDto;
import com.example.proj1.model.UserRegisterDto;
import com.example.proj1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.proj1.repository.entity.User;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordHashingService passwordHashingService;

    /**
     * Returns User entity given userId, userId must be current logged-in user
     * @param userId - id of a currently logged-in user
     * @return User entity
     */
    protected User getAuthenticatedUserEntity(Long userId){
        Optional<User> user = userRepository.findByUserId(userId);
        if(user.isEmpty())
            throw new UserCoreException(HttpStatus.NOT_FOUND,"Logged in user can't be found in database");
        return user.get();
    }

    protected User getUserEntity(Long userId){
        Optional<User> user = userRepository.findByUserId(userId);
        if(user.isEmpty())
            throw new UserCoreException(HttpStatus.NOT_FOUND,"User with given userId not found in database");
        return user.get();
    }

    public long registerUser(UserRegisterDto userRegister) {
        if (userRegister == null || userRegister.getUsername() == null || userRegister.getMail() == null || userRegister.getPassword() == null || userRegister.getPasswordAgain() == null)
            throw new NullArgumentException();
        if (!Objects.equals(userRegister.getPassword(), userRegister.getPasswordAgain()))
            throw new UserCoreException(HttpStatus.CONFLICT,"Passwords are not equal");
        //TODO PASSWORDS SECURITY LEVEL
        if (userRepository.existsByMail(userRegister.getMail()))
            throw new UserCoreException(HttpStatus.CONFLICT,"User with given mail already exists.");
        //TODO MAIL REGEX
        //TODO USERNAME check
        if (userRepository.existsByUsername(userRegister.getUsername()))
            throw new UserCoreException(HttpStatus.CONFLICT,"User with given username already exists");

        User u = userRepository.save(User.builder()
                .username(userRegister.getUsername())
                .mail(userRegister.getMail())
                .hashedPassword(
                        passwordHashingService
                                .hashPassword(userRegister.getPassword()))
                .build());

        return u.getUserId();
    }

    public long loginUser(UserLoginDto userLogin){
        if (userLogin == null || userLogin.getMail() == null || userLogin.getPassword() == null)
            throw new NullArgumentException();
        Optional<User> u = userRepository.findUserByMail(userLogin.getMail());
        if (u.isEmpty())
            throw new UserCoreException(HttpStatus.NOT_FOUND,"User with given mail address doesn't exists");
        if (!passwordHashingService.verifyPassword(userLogin.getPassword(), u.get().getHashedPassword()))
            throw new UserCoreException(HttpStatus.UNAUTHORIZED,"Password is invalid");
        return u.get().getUserId();
    }

    public UserPrivateDto getUserById(long userId) {
        Optional<UserPrivateDto> userDto = userRepository.findByUserId(userId).map(user -> UserPrivateDto.builder()
                .userId(userId)
                .mail(user.getMail())
                .username(user.getUsername())
                .build());
        if(userDto.isEmpty())
            throw new UserCoreException(HttpStatus.NOT_FOUND,"No user with given ID found");
        return userDto.get();
    }
}
