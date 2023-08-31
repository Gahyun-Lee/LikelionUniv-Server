package likelion.univ.project.dto.request;

import likelion.univ.domain.project.entity.ProjectMember;
import likelion.univ.domain.user.entity.User;
import likelion.univ.project.dto.response.ProjectMemberResponseDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Data
public class ProjectMemberRequestDto {
//    private User user;

    private Long id;
    private String name;
//    @Builder
//    public ProjectMemberRequestDto(ProjectMember projectMember){
//        this.user = projectMember.getUser();
//    }

    @Builder
    public ProjectMemberRequestDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}