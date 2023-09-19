package com.sparta.first_project.controller;

import com.sparta.first_project.dto.*;
import com.sparta.first_project.entity.User;
import com.sparta.first_project.error.ParameterValidationException;
import com.sparta.first_project.security.UserDetailsImpl;
import com.sparta.first_project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Operation(hidden = true)//swagger에서 보이지 않게 설정
    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse> signup(@RequestBody @Valid SignupRequestDto requestDto,
                                               BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
        userService.signup(requestDto);
        return ResponseEntity.ok().body(new SuccessResponse("회원 가입 완료"));
    }

    // 회원정보 조회
    @GetMapping("/profile")
    public ResponseEntity<BaseResponse> getProfile(@RequestParam String username) {
        User user = userService.getProfile(username);
        return ResponseEntity.ok().body(new SuccessResponse("회원정보 조회 성공", user));
    }

    // 회원정보 수정
    @PutMapping("/profile")
    public ResponseEntity<BaseResponse> updateProfile(@RequestBody @Valid ProfileRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            // 현재 로그인한 사용자의 정보를 가져옴
            String username = userDetails.getUsername();
            User user = userService.findByUsername(username);

            // 입력한 비밀번호를 BCryptPasswordEncoder를 사용하여 검사
            if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            // 회원 정보 수정
            user.setUsername(requestDto.getUsername());
            user.setEmail(requestDto.getEmail());
            // 비밀번호를 업데이트할 경우, 인코딩해서 저장
            String newPassword = requestDto.getNewPassword();
            if (newPassword != null && !newPassword.isEmpty()) {
                user.setPassword(passwordEncoder.encode(newPassword));
            }
            user.setIntro(requestDto.getIntro());

            // 회원 정보 저장
            userService.update(user);

            return ResponseEntity.ok().body(new SuccessResponse("회원정보 수정 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> delete(@RequestParam String password,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 현재 로그인한 사용자의 정보를 가져옴
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);
        // 입력한 비밀번호를 암호화하여 저장된 비밀번호와 비교
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        userService.delete(password);
        return ResponseEntity.ok().body(new SuccessResponse("회원 탈퇴 성공"));
    }
}