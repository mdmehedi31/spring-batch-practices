package com.sb.config;


import com.sb.entity.UserEntity;
import org.springframework.batch.infrastructure.item.ItemProcessor;


public class UserProcessor implements ItemProcessor<UserEntity, UserEntity> {

    @Override
    public UserEntity process(UserEntity item) throws Exception {

      item.setFirstName(item.getFirstName().toUpperCase());
      item.setLastName(item.getLastName().toUpperCase());
        return item;
    }
}
