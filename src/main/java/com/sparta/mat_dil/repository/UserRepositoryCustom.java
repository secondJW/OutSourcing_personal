package com.sparta.mat_dil.repository;

import com.sparta.mat_dil.dto.ProfileResponseDto;
import com.sparta.mat_dil.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<ProfileResponseDto> findTop10ByOrderByFollowersCntDesc();
}
