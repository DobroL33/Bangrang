package com.ssafy.bangrang.domain.inquiry.api;

import com.ssafy.bangrang.domain.inquiry.api.request.CommentMakeDto;
import com.ssafy.bangrang.domain.inquiry.api.request.CommentUpdateDto;
import com.ssafy.bangrang.domain.inquiry.api.response.CommentDto;
import com.ssafy.bangrang.domain.inquiry.entity.Comment;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.inquiry.repository.CommentRepository;
import com.ssafy.bangrang.domain.inquiry.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/web")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;


    // 답변 작성하기
    @PostMapping("/comment")
    public ResponseEntity<?> postComment(@RequestBody CommentMakeDto commentMakeDto){
        log.info("답변 생성");
        try {
            commentService.save(commentMakeDto);
            return ResponseEntity.ok().body("답변 작성 완료");
        } catch (Exception e) {
            log.info("작성 실패");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //    답변 수정하기
    @PutMapping("/comment/{idx}")
    public ResponseEntity<?> updateComment(@RequestBody CommentUpdateDto commentUpdateDto, @PathVariable Long idx){
        log.info("답변 수정 시작");
        try {
            commentService.update(idx,commentUpdateDto);
            return ResponseEntity.ok().body("수정완료.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //답변 삭제하기
    @DeleteMapping("/comment/{idx}")
    public ResponseEntity<?> deleteComment(@PathVariable Long idx) {
        log.info("답변 삭제");
        try {
            commentService.deleteComment(idx);
            return ResponseEntity.ok().body("삭제완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
