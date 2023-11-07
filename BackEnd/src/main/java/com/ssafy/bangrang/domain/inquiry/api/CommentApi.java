package com.ssafy.bangrang.domain.inquiry.api;

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
@RequestMapping("/api/web/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentApi {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody AddCommentRequestDto request){
        commentService.save(userDetails, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> updateComment(@RequestBody UpdateCommentRequestDto request,
                                           UserDetails userDetails){
        commentService.updateCommentV2(request,userDetails);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteComment(@RequestBody Long commentIdx,
                                           UserDetails userDetails){
        commentService.deleteCommentV2(commentIdx,userDetails);
        return ResponseEntity.ok().build();
    }
}
