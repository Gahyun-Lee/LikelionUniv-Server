package likelion.univ.domain.comment.entity;

import likelion.univ.common.entity.BaseTimeEntity;
import likelion.univ.domain.post.entity.Post;
import likelion.univ.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true) // 안정성 체크해봐야됨
    private List<Comment> childComments = new ArrayList<>();

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    private Comment(Post post, User user, String body, Comment parentComment) {
        this.post = post;
        this.author = user;
        this.body = body;
        this.setParent(parentComment);
        isDeleted = false;
    }

    public Comment updateBody(String body) {
        this.body = body;
        return this;
    }
    public Comment delete() {
        this.isDeleted = true;
        return this;
    }

    /* 연관관계 편의 메서드 */
    public void setParent(Comment parent) {
        this.parentComment = parent;
        parent.getChildComments().add(this);
    }
}