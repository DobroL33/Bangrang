package com.ssafy.bangrang.domain.inquiry.api;

import com.ssafy.bangrang.domain.inquiry.api.request.AddCommentRequestDto;
import com.ssafy.bangrang.domain.inquiry.api.request.DeleteCommentRequestDto;
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
                                        @RequestBody AddCommentRequestDto request) throws Exception{
        commentService.save(userDetails, request);
        return ResponseEntity.ok().body("");
    }

    @PutMapping
    public ResponseEntity<?> updateComment(@RequestBody UpdateCommentRequestDto request,
                                           @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        commentService.updateCommentV2(request,userDetails);
        return ResponseEntity.ok().body("");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteComment(@RequestBody DeleteCommentRequestDto deleteCommentRequestDto,
                                           @AuthenticationPrincipal UserDetails userDetails){
        commentService.deleteCommentV2(deleteCommentRequestDto.getCommentIdx(),userDetails);
        return ResponseEntity.ok().body("");
    }
}
