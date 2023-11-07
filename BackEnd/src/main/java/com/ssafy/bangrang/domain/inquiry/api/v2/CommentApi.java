package com.ssafy.bangrang.domain.inquiry.api.v2;

import com.ssafy.bangrang.domain.inquiry.api.request.AddCommentRequestDto;
import com.ssafy.bangrang.domain.inquiry.api.request.UpdateCommentRequestDto;
import com.ssafy.bangrang.domain.inquiry.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/web")
@RequiredArgsConstructor
@Slf4j
public class CommentApi {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AddCommentRequestDto request){
        commentService.save(userDetails, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/comment")
    public ResponseEntity<?> updateComment(@RequestBody UpdateCommentRequestDto request){
        commentService.updateCommentV2(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(@RequestBody Long commentIdx){
        commentService.deleteCommentV2(commentIdx);
        return ResponseEntity.ok().build();
    }
}
