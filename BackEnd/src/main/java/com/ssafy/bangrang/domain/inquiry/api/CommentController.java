package com.ssafy.bangrang.domain.inquiry.api;

import com.ssafy.bangrang.domain.inquiry.api.request.CommentMakeDto;
import com.ssafy.bangrang.domain.inquiry.api.request.CommentUpdateDto;
import com.ssafy.bangrang.domain.inquiry.service.v2.CommentService;
import com.ssafy.bangrang.domain.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/api/web")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final InquiryService inquiryService;


    // 답변 작성하기
    @PostMapping("/comment/regist")
    public ResponseEntity<?> postComment(@RequestBody CommentMakeDto commentMakeDto,
                                         @AuthenticationPrincipal UserDetails userDetails){
        log.info("답변 생성");
        try {
            commentService.save(commentMakeDto, userDetails);
            return ResponseEntity.ok().body("답변 작성 완료");
        } catch (Exception e) {
            log.info("작성 실패");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //    답변 수정하기
    @PutMapping("/comment/update/{commentIdx}")
    public ResponseEntity<?> updateComment(@RequestBody CommentUpdateDto commentUpdateDto,
                                           @PathVariable Long commentIdx,
                                           @AuthenticationPrincipal UserDetails userDetails){
        log.info("답변 수정 시작");
        try {
            commentService.update(commentIdx,commentUpdateDto,userDetails);
            return ResponseEntity.ok().body("수정완료.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //답변 삭제하기
    @DeleteMapping("/comment/{idx}")
    public ResponseEntity<?> deleteComment(@PathVariable Long idx,@AuthenticationPrincipal UserDetails userDetails) {
        log.info("답변 삭제");
        try {
            commentService.deleteComment(idx);
            return ResponseEntity.ok().body("삭제완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
