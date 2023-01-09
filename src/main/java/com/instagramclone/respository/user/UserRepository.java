package com.instagramclone.respository.user;

import com.instagramclone.entity.user.User;
import com.instagramclone.payload.userDto.UserSuggestionsDto;
import com.instagramclone.respository.projectionInf.UserWithProfilesI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsernameOrEmailOrPhoneNumber(String username,String email,String phoneNumber);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Boolean existsByUsernameOrEmailOrPhoneNumber(String username, String email, String phoneNumber);

    @Query(value = "SELECT u FROM User u WHERE u.username!=:userName ORDER BY u.id DESC")
    List<UserWithProfilesI> getUserWithProfiles(@Param("userName") String userName, Pageable pageable);

    @Query(value = "SELECT new com.instagramclone.payload.userDto.UserSuggestionsDto(id, fullName, currentProfilePicUrl)" +
            " FROM User u WHERE u.username!=:userName ORDER BY u.id DESC")
    List<UserSuggestionsDto> getUserSuggestions(@Param("userName") String userName, Pageable pageable);
}
