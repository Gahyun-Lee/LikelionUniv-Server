package likelion.univ.project.controller;

import io.swagger.annotations.Api;
import likelion.univ.project.dto.request.ProjectRequestDto;
import likelion.univ.project.dto.response.ProjectResponseDto;
import likelion.univ.project.usecase.*;
import likelion.univ.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/project")
//swagger UI에 보일 컨트롤러 이름
@Api(tags = {"프로젝트 API"})
public class ProjectController {

    private final GetProjectUsecase getProjectUsecase;
    private final GetAllPorjectUsecase getAllPorjectUsecase;
    private final CreateProjectUsecase createProjectUsecase;
    private final UpdateProjectUsecase updateProjectUsecase;
    private final DeleteProjectUsecase deleteProjectUsecase;
    private final ArchiveProjectUsecase archiveProjectUsecase;
    private final GetProjectByUsecase getProjectByUsecase;
    private final GetOrdinalUsecase getOrdinalUsecase;

    //-----------프로젝트 한 개 조회 --------//
    @GetMapping("/{projectId}")
//    @Operation(summary = " .")
    public SuccessResponse<ProjectResponseDto> getProject(@PathVariable("projectId") Long projectId) {
        ProjectResponseDto projectResponseDto = getProjectUsecase.excute(projectId);
        return SuccessResponse.of(projectResponseDto);
    }

    //-----------프로젝트 목록 5 -> 12 로 수정--------//
    @GetMapping("/")
    public SuccessResponse<List<ProjectResponseDto>> getAllProject(@PageableDefault(page=0, size=12, sort="id" ,direction = Sort.Direction.DESC) Pageable pageable){
        List<ProjectResponseDto> projectList = getAllPorjectUsecase.excute(pageable);
        return SuccessResponse.of(projectList);
    }

    //--------  기수별 프로젝트 -----//
    @GetMapping("/ordinal/{ordinal}")
    public SuccessResponse<List<ProjectResponseDto>> getProjectByOrdinal(
            @PageableDefault(page=0, size=12, sort="id" ,direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable int ordinal) {
        int recentOrdinal = getOrdinalUsecase.excute();
        if (ordinal > recentOrdinal - 5) {
            List<ProjectResponseDto> projectListByOrdinal = getProjectByUsecase.excute(ordinal, pageable);
            return SuccessResponse.of(projectListByOrdinal);
        } else {
            List<ProjectResponseDto> projectList = archiveProjectUsecase.excute(ordinal);
            return SuccessResponse.of(projectList);
        }
    }

    //--------- 프로젝트 등록 ------------//
    @PostMapping("/post")
    public SuccessResponse<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto projectRequestDto){

        ProjectResponseDto projectResponseDto = createProjectUsecase.excute(projectRequestDto);
        return SuccessResponse.of(projectResponseDto);
    }

    //-----------프로젝트 수정 --------//
    @PutMapping("/edit/{projectId}")
    public SuccessResponse<ProjectResponseDto> updateProject(@PathVariable("projectId") Long projectId, @RequestBody ProjectRequestDto projectRequestDto) {
        ProjectResponseDto projectResponseDto = updateProjectUsecase.excute(projectId, projectRequestDto);
        return SuccessResponse.of(projectResponseDto);
    }

    //-----------프로젝트 삭제 --------//
    @DeleteMapping("/delete/{projectId}")
    public SuccessResponse<Objects> deleteProject(@PathVariable("projectId") Long projectId) {
        deleteProjectUsecase.excute(projectId);
        return SuccessResponse.empty();
    }
}