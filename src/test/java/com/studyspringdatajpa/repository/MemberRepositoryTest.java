package com.studyspringdatajpa.repository;

import com.studyspringdatajpa.dto.MemberDto;
import com.studyspringdatajpa.entity.Member;
import com.studyspringdatajpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// Spring Data JPA 기반 테스트
@SpringBootTest
@Transactional
@Rollback(value = false)
@ActiveProfiles("local")
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        // 여기서는 Optional (saveMember가 있을수도 있고, 없을수도 있기 때문에)
        Optional<Member> byId = memberRepository.findById(savedMember.getId());

        // then
        assertThat(byId).isPresent(); // Optional이 비어있지 않은지 검증
        Member foundMember = byId.get();

        assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
        assertThat(foundMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(foundMember).isEqualTo(savedMember); // equals(), hashCode() 오버라이드 필요
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        Long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        Long deletedCount = memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        Assertions.assertThat(findMember).isEqualTo(member1);
    }

    @Test
    public void testQuery() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(member1);
    }

    @Test
    public void testFindUsernameList() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        assertThat(usernameList.size()).isEqualTo(2);
    }

    @Test
    public void testFindMemberDto() {
        Team team1 = new Team("teamA");
        teamRepository.save(team1);

        Member member1 = new Member("AAA", 10);
        member1.changeTeam(team1);
        memberRepository.save(member1);

        List<MemberDto> usernameList = memberRepository.findMemberDto();
        for (MemberDto memberDto : usernameList) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void testFindByNames() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> usernameList = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : usernameList) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void testReturnType() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");
        System.out.println("aaa = " + aaa);
        Member bbb = memberRepository.findMemberByUsername("BBB");
        System.out.println("bbb = " + bbb);
        Optional<Member> member = memberRepository.findOptionalByUsername("AAA");
        System.out.println("member = " + member);
    }

    @Test
    public void testPaging() {
        // given
        Team team1 = new Team("teamA");
        teamRepository.save(team1);
        Team team2 = new Team("teamB");
        teamRepository.save(team2);

        Member member1 = new Member("member1", 10);
        member1.changeTeam(team1);
        memberRepository.save(member1);

        Member member2 = new Member("member2", 10);
        member2.changeTeam(team1);
        memberRepository.save(member2);

        Member member3 = new Member("member3", 10);
        member3.changeTeam(team1);
        memberRepository.save(member3);

        Member member4 = new Member("member4", 10);
        member4.changeTeam(team2);
        memberRepository.save(member4);

        Member member5 = new Member("member5", 10);
        member5.changeTeam(team2);
        memberRepository.save(member5);

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        // 반드시 DTO로 변환해서 넘겨야함 (toMap는 외부로 넘겨도 된다.)
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

//        Slice<Member> pageSlice = memberRepository.findByAge(age, pageRequest);
//        List<Member> pageList = memberRepository.findByAge(age, pageRequest);

        // then
        // ==== Page<Member> ====
        List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member.team = " + member.getTeam().getName());
        }

        assertThat(content.size()).isEqualTo(3); // 현재 페이지의 콘텐츠 개수
        assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); // 현재 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 개수
        assertThat(page.isFirst()).isTrue(); // 현재 페이지가 첫번째 페이지인지
        assertThat(page.hasNext()).isTrue(); // 현재 페이지의 다음 페이지가 있는지

        // ==== Slice<Member> ====
//        List<Member> contentSlice = pageSlice.getContent();
//
//        assertThat(contentSlice.size()).isEqualTo(3); // 현재 페이지의 콘텐츠 개수
//        assertThat(pageSlice.getNumber()).isEqualTo(0); // 현재 페이지 번호
//        assertThat(pageSlice.isFirst()).isTrue(); // 현재 페이지가 첫번째 페이지인지
//        assertThat(pageSlice.hasNext()).isTrue(); // 현재 페이지의 다음 페이지가 있는지

        // ==== List<Member> ====
        // 페이징 관련 메서드 제공 안함
    }

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);

        // em.flush(); // 영속성 컨텍스트에 남아 있던 내용 DB 반영
        // em.clear(); // 영속성 컨텍스트 비움

        List<Member> result = memberRepository.findByUsername("member5");
        Member findMember = result.get(0);
        System.out.println("findMember = " + findMember);

        // then
        Assertions.assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        // given
        Team team1 = new Team("teamA");
        teamRepository.save(team1);

        // member1 -> teamA
        Member member1 = new Member("member1", 10);
        member1.changeTeam(team1);
        memberRepository.save(member1);

        // member2 -> teamB
        Team team2 = new Team("teamB");
        teamRepository.save(team2);

        Member member2 = new Member("member1", 20);
        member2.changeTeam(team2);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when
//        List<Member> members = memberRepository.findAll();
//        List<Member> members = memberRepository.findMemberFetchJoin();
//        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        List<Member> members = memberRepository.findNamedQueryEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void beforeQueryHint() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findById(member1.getId()).get(); // 다시 DB에서 조회
        findMember.setUsername("member2");
        // JPA는 변경 감지를 위해 원본과 변경된 객체를 비교한다.
        // 즉, 내부적으로 member1(저장 시점 상태)과 findMember(변경된 객체) 둘을 비교하여 UPDATE 쿼리를 생성함.
        // 이로 인해 불필요한 스냅샷 유지 비용이 발생할 수 있다.

        // 하지만, 만약 이 객체가 단순히 '조회 목적'이라면? (즉, 수정할 필요가 없는 경우)
        // 원본과 비교할 필요가 없기 때문에 스냅샷을 보관할 이유가 없다.
        // 이런 경우에는 JPA가 제공하는 '읽기 전용 힌트(Read-only Hint)'를 사용하면 성능을 최적화할 수 있다.
        // -> Hibernate 구현체에서는 @QueryHints({ @QueryHint(name = "org.hibernate.readOnly", value = "true") }) 사용 가능

        em.flush();  // 변경 감지로 인해 UPDATE 쿼리가 나감 (readOnly가 아니기 때문)
    }

    @Test
    public void queryHintReadOnly() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        // findReadOnlyByUsername()는 Hibernate에 readOnly 힌트를 전달했기 때문에,
        // Hibernate는 해당 엔티티에 대해 스냅샷을 만들지 않음.
        // 따라서 변경 감지가 동작하지 않고, flush 시에도 Update 쿼리가 실행되지 않는다.
        em.flush();
    }

    @Test
    public void lock() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        List<Member> findMember = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    public void specBasic() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 1, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void queryByExample() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 1, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        // -- Probe --
        Member member = new Member("m1"); // Member 엔티티 자체가 검색 조건이 됨
        Team team = new Team("teamA");
        member.changeTeam(team);
        // ----

        // age 무시
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");

        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);
        // 조회할 때 null인 값은 무시하고 조건으로 넣지 않는다.
        // 그런데 실제 쿼리에서 where m1_0.username='m1' and m1_0.age=0; 으로 되어있다.
        // 왜 age=0이 자동으로 나오나면, age는 int 타입이라 (자바 기본타입) null이 아니고 0이라서 무시되지 않는다.
        // 따라서, age를 무시해야한다.

        assertThat(result.get(0).getUsername()).isEqualTo("m1");
    }

    @Test
    public void projections() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");

        for (UsernameOnly usernameOnly : result) {
            System.out.println("usernameOnly = " + usernameOnly.getUsername());
            System.out.println("usernameOnly.getAge() = " + usernameOnly.getAge());
        }
        // 결과
        // 1. close projection -> usernameOnly = m1
        // 2. open projeciton -> usernameOnly = m1 0
    }

    @Test
    public void projections2() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        List<NestedClosedProjections> result = memberRepository.findProjections2ByUsername("m1", NestedClosedProjections.class);

        for (NestedClosedProjections nestedClosedProjections : result) {
            System.out.println("nestedClosedProjections = " + nestedClosedProjections.getUsername());
            System.out.println("nestedClosedProjections.getTeam().getName() = " + nestedClosedProjections.getTeam().getName());
        }
    }

    @Test
    public void nativeQuery() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        List<Member> result = memberRepository.findByNativeQuery("m1");
        for (Member member : result) {
            System.out.println("member = " + member.getUsername());
        }
    }

    @Test
    public void nativeQueryProjection() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        for (MemberProjection member : result) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getTeamName() = " + member.getTeamName());
        }
    }
}