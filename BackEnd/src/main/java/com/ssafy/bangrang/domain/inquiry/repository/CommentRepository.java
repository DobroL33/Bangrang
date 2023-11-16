package com.ssafy.bangrang.domain.inquiry.repository;

import com.ssafy.bangrang.domain.inquiry.api.response.CommentDto;
import com.ssafy.bangrang.domain.inquiry.entity.Comment;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    Optional<Comment> findByInquiry(Inquiry inquiry);

    //    Long deleteByIdx(Long idx);
    void deleteById(Long idx);


    Optional<Comment> findByIdx(Long idx);

    Optional<Comment> findAllByInquiry(Inquiry inquiry);
//    void delete(Optional<Comment> comment);
}

