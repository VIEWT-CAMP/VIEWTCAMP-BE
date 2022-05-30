package com.week8.finalproject.security;


import com.week8.finalproject.model.user.User;
import com.week8.finalproject.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    //userPk // 토큰에서의 username
    public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userPk)
                .orElseThrow(() -> new UsernameNotFoundException(userPk + "은 존재하지 않는 아이디입니다."));
        return new UserDetailsImpl(user);//
    }
}
