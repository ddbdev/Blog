package com.example.springsecurityproject.service;

import com.example.springsecurityproject.entity.TokenEntity;
import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenEntity addToken(String key, UserEntity user){

        TokenEntity tokenCheck = tokenRepository.getKeyByUser(user);
        if (tokenCheck != null)
        {
            tokenRepository.deleteByUser(user);
        }
        //TODO Creare funzione per cancellare i token di quel principal esistenti nella tabella token_entity, così appena ne crea uno nuovo per quell'user si cancella
        TokenEntity token = new TokenEntity();
        token.setToken(key);
        token.setUser(user);
        return tokenRepository.save(token);
    }

    public TokenEntity getKey(UserEntity user){
        return tokenRepository.getKeyByUser(user);
    }

}
