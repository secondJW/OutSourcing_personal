package com.sparta.mat_dil.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.mat_dil.dto.ProfileResponseDto;
import com.sparta.mat_dil.entity.QUser;
import com.sparta.mat_dil.entity.User;
import com.sparta.mat_dil.repository.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProfileResponseDto> findTop10ByOrderByFollowersCntDesc(){
        QUser user = QUser.user;
        List<ProfileResponseDto> users = queryFactory
                .select(Projections.constructor(ProfileResponseDto.class, user))
                .from(user)
                .orderBy(user.followersCnt.desc())
                .limit(10)
                .fetch();

//        return users
//                .stream()
//                .map(ProfileResponseDto::new)
//                .toList();
        return  users;

        //        List<User> users=queryFactory
//                .selectFrom(user)
//                .orderBy(user.followersCnt.desc())
//                .limit(10)
//                .fetch();
//        List<ProfileResponseDto> results=users.stream().map(ProfileResponseDto::new).toList();
    }
}
