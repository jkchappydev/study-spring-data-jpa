package com.studyspringdatajpa.repository;

import com.studyspringdatajpa.dto.MemberDto;
import com.studyspringdatajpa.entity.Member;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    // ==== 쿼리 메서드 ====
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // ==== NamedQuery ====
    // Entity에 NamedQuery 정의되어 있어야 함
    /*
        @Param
        - 아래처럼 @NamedQuery에 :username 처럼 JPQL을 명확하게 작성했을 경우에 해당 파라미터에 대해 사용
        @NamedQuery(
            name = "Member.findByUsername",
            query = "select m from Member m where m.username = :username"
        )
    */
    @Query(name = "Member.findByUsername") // 메서드명이랑 NamedQuery명이 일치할경우, 생략 가능 (내부적으로 NamedQuery 우선 찾고, 없으면 쿼리 메소드로 실행하게 되어있음)
    List<Member> findByUsername(@Param("username") String username);

    // ==== 리포지토리 메서드에 쿼리 정의하기 ====
    // 실무에서 많이 사용
    // 쿼리 메서드의 단점인 조건이 많아지면 메서드명의 길이가 길어지는 단점 극복
    // NamedQuery의 단점인 Entity측에 쿼리를 작성해야하는 단점 극복 및 NamedQuery의 장점(애플리케이션 로딩 시점에서 체크)를 그대로 사용할 수 있음
    // 메서드명 관례에 영향받지 않음
    // 동적 쿼리는 QueryDSL 사용해야 함
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // ==== 값 조회하기 ====
    @Query("select m.username from Member m")
    List<String> findUsernameList(); // 리턴 타입에 주목

    // ==== DTO 조회하기 ====
    // new com.studyspringdatajpa.dto.MemberDto 이것도 QueryDSL 사용하면 편해진다.
    @Query("select new com.studyspringdatajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // ==== 파라미터 바인딩 (리스트) ====
    // IN 절 조회할 때 사용
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    // ==== 반환 타입 ====
    List<Member> findListByUsername(String username); // 컬렉션 (결과가 없으면 null이 아닌 Empty Collection을 반환함)
    Member findMemberByUsername(String username); // 단건 (결과가 없으면 null을 반환)
    Optional<Member> findOptionalByUsername(String username); // 단건 Optional (데이터가 있을지 없을지 모를때 해당 리턴타입 사용)

    // ==== 스프링 데이터 JPA 페이징과 정렬 ====
//    Page<Member> findByAge(int age, Pageable pageable); // 쿼리 메서드

    // 슬라이스는 전체 데이터 수, 전체 페이지 개수 없다.
    // limit + 1 한다. (더보기 같은 메뉴)
//    Slice<Member> findByAge(int age, Pageable pageable); // 쿼리 메서드

    // 리스트는 딱 해당 페이지의 데이터만 출력하고 끝낸다. (기타 페이징 관련 메서드 제공 안함)
//    List<Member> findByAge(int age, Pageable pageable);

    // 카운트 쿼리 (실무에서 중요)
    // 따로 분리 이유. 원래 쿼리가 left join 이어도 count 쿼리는 left join 할 필요가 없기 때문에 (총 토탈 건수는 같기 때문)
    // @Query(value = "select m from Member m left join m.team t") // 이 방식은 count 쿼리도 left join을 하기 때문에 데이터가 많아지면 성능이 안 좋아진다.
    @Query(
            value = "select m from Member m left join m.team t where m.age = :age",
            countQuery = "select count(m.username) from Member m where m.age = :age"
    ) // 이렇게 따로 분리를 할 수 있다.
    Page<Member> findByAge(@Param("age") int age, Pageable pageable);

    // 기타
    // Page<Member> findTop3ByAge(int age); // Top3: 단순히 3건만 가져오고 싶다. Pageable 안 넘겨도 됨.

    // ==== 벌크성 수정 쿼리 ====
    @Modifying(clearAutomatically = true) // 해당 어노테이션 작성 필수 (있어야 executeUpdate() 호출)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // @EntityGraph
    /* 기본 패치 조인
        @Query("select m from Member m left join fetch m.team")
        List<Member> findMemberFetchJoin();
    */

    // Spring Data JPA에서 패치 조인
    @Override
    @EntityGraph(attributePaths = {"team"}) // 패치 조인 할 대상을 명시
    List<Member> findAll();

    // 반드시 직접 쿼리를 작성해야하는 상황에서 패치 조인만 적용하고 싶을 때
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 쿼리 메서드도 가능하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    // NamedQuery도 가능
    @EntityGraph("Member.all")
    List<Member> findNamedQueryEntityGraphByUsername(@Param("username") String username);

    // ==== JPA Hint & Lock ====
    // JPA Hint
    @QueryHints(value = {
            @QueryHint(name = "org.hibernate.readOnly", value = "true")
    })
    Member findReadOnlyByUsername(String username);

    // Lock
    @Lock(LockModeType.PESSIMISTIC_WRITE) // select for update
    List<Member> findLockByUsername(String username);

    // Projections
    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

    // 동적 Projections + 중첩 구조 처리
    <T> List<T> findProjections2ByUsername(@Param("username") String username, Class<T> tClass);

}
