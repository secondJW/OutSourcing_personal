package com.sparta.mat_dil.service;

import com.sparta.mat_dil.dto.LikeResponseDto;
import com.sparta.mat_dil.entity.*;
import com.sparta.mat_dil.enums.ErrorType;
import com.sparta.mat_dil.exception.CustomException;
import com.sparta.mat_dil.repository.CommentLikeRepository;
import com.sparta.mat_dil.repository.CommentRepository;
import com.sparta.mat_dil.repository.RestaurantLikeRepository;
import com.sparta.mat_dil.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final RestaurantLikeRepository restaurantLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final RestaurantRepository restaurantRepository;

    public LikeResponseDto updateRestaurantLike(Long contentId, User user) {

        Restaurant restaurant = restaurantRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (user.getId().equals(restaurant.getUser().getId())) {
            throw new CustomException(ErrorType.BLOCKED_USER);
        }

        RestaurantLike restaurantLike = restaurantLikeRepository.findByUserAndRestaurant(user, restaurant)
                .orElseGet(() -> new RestaurantLike(user, restaurant));

        restaurantLike.update();
        restaurantLikeRepository.save(restaurantLike);

        return calculateRestaurantLike(restaurantLike, restaurant);
    }

    public LikeResponseDto updateCommentLike(Long contentId, User user) {
        Comment comment = commentRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Review not found"));

        if (user.getId().equals(comment.getUser().getId())) {
            throw new CustomException(ErrorType.BLOCKED_USER);
        }

        CommentLike commentLike = commentLikeRepository.findByUserAndComment(user, comment)
                .orElseGet(() -> new CommentLike(user, comment));

        commentLike.update();
        commentLikeRepository.save(commentLike);

        return calculateCommentLike(commentLike, comment);
    }

    private LikeResponseDto calculateRestaurantLike(RestaurantLike restaurantLike, Restaurant restaurant) {
        Long cnt =  restaurant.updateLike(restaurantLike.isLiked());
        return new LikeResponseDto(restaurantLike.isLiked(), cnt);
    }

    public LikeResponseDto calculateCommentLike(CommentLike commentLike, Comment comment) {
        Long cnt =  comment.updateLike(commentLike.isLiked());
        return new LikeResponseDto(commentLike.isLiked(), cnt);
    }
}