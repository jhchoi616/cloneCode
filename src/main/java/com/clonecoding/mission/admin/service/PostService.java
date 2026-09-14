package com.clonecoding.mission.admin.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.clonecoding.mission.admin.repository.PostRepository;
import com.clonecoding.mission.global.entity.Post;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /*
     * 실제 파일이 저장될 경로
     *
     * application.properties에서 설정 가능
     */
    @Value("${file.upload-dir:uploads/notices}")
    private String uploadDir;


    /*
     * 공지/자료 전체 조회
     */
public Page<Post> findAll(
        Integer type,
        String keyword,
        Pageable pageable
) {
    if (type != null && !keyword.isBlank()) {
        return postRepository
                .findByTypeAndTitleContainingIgnoreCaseOrderByCreatedAtDescTypeDesc(
                        type,
                        keyword,
                        pageable
                );
    }

    if (type != null) {
        return postRepository
                .findByTypeOrderByCreatedAtDescTypeDesc(
                        type,
                        pageable
                );
    }

    if (!keyword.isBlank()) {
        return postRepository
                .findByTitleContainingIgnoreCaseOrderByCreatedAtDescTypeDesc(
                        keyword,
                        pageable
                );
    }

    return postRepository
            .findAllByOrderByCreatedAtDescTypeDesc(pageable);
}


    /*
     * 새 글 작성
     */
    @Transactional
    public void save(
            Post post,
            MultipartFile uploadFile
    ) throws IOException {

        /*
         * 새 글 기본값
         */

        if (post.getViewCount() == null) {
            post.setViewCount(0);
        }

        post.setCreatedAt(
                java.time.LocalDateTime.now()
        );


        /*
         * 파일이 첨부된 경우
         */

        if (uploadFile != null && !uploadFile.isEmpty()) {

            saveFile(post, uploadFile);

        }


        postRepository.save(post);
    }


    /*
     * 기존 글 수정
     */
    @Transactional
    public void update(
            Long id,
            Post formPost,
            MultipartFile uploadFile
    ) throws IOException {

        Post post =
                postRepository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "존재하지 않는 게시글입니다."
                                )
                        );


        /*
         * 기본 데이터 수정
         */

        post.setType(formPost.getType());
        post.setTitle(formPost.getTitle());
        post.setContent(formPost.getContent());


        /*
         * 파일을 새로 첨부했다면
         * 기존 파일 삭제 후 새 파일 저장
         */

        if (uploadFile != null && !uploadFile.isEmpty()) {

            deleteFile(post);

            saveFile(post, uploadFile);

        }


        post.setModifiedAt(
                java.time.LocalDateTime.now()
        );


        postRepository.save(post);
    }


    /*
     * 게시글 삭제
     */
    @Transactional
    public void delete(Long id) throws IOException {

        Post post =
                postRepository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "존재하지 않는 게시글입니다."
                                )
                        );


        /*
         * 첨부파일이 있으면 실제 파일도 삭제
         */

        deleteFile(post);


        postRepository.delete(post);
    }


    /*
     * 파일 저장
     */
    private void saveFile(
            Post post,
            MultipartFile file
    ) throws IOException {

        /*
         * 업로드 디렉터리 생성
         */

        Path uploadPath =
                Paths.get(uploadDir)
                        .toAbsolutePath()
                        .normalize();

        Files.createDirectories(uploadPath);


        /*
         * 원본 파일명
         *
         * 예:
         * bpoint-guide.pdf
         */

        String originalFilename =
                file.getOriginalFilename();


        /*
         * UUID 파일명
         *
         * 예:
         * 550e8400-e29b-41d4-a716-446655440000.pdf
         */

        String uuid =
                UUID.randomUUID().toString();


        String extension = "";

        if (originalFilename != null) {

            int index =
                    originalFilename.lastIndexOf(".");

            if (index >= 0) {

                extension =
                        originalFilename.substring(index);

            }
        }


        String storedFilename =
                uuid + extension;


        /*
         * 실제 저장 경로
         */

        Path targetPath = uploadPath.resolve(storedFilename);


        /*
         * 파일 저장
         */

        Files.copy(
                file.getInputStream(),
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
        );


        /*
         * DB에 파일 정보 저장
         */

        post.setFileName(originalFilename);

        post.setFileUuid(uuid);

        post.setFilePath(targetPath.toString());

        post.setFileSize(
                String.valueOf(file.getSize())
        );

        post.setFileContentType(
                file.getContentType()
        );

        post.setFileUploadDir(
                uploadPath.toString()
        );

        post.setFileExtension(extension);

        /*
         * 다운로드 URL
         */

        post.setFileDownloadUri(
                "/admin/notices/file/" + post.getId()
        );
    }


    /*
     * 실제 파일 삭제
     */
    private void deleteFile(Post post)
            throws IOException {

        if (post.getFilePath() == null) {
            return;
        }


        Path path =
                Paths.get(post.getFilePath());


        if (Files.exists(path)) {

            Files.delete(path);

        }
    }


    @Transactional(readOnly = true)
    public Post findById(Long id) {

        return postRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "존재하지 않는 게시글입니다."
                        )
                );
    }

}