package study.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testMember() throws Exception {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
//when
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC,
                "username"));
        Page<Member> page = memberRepository.findByAge(10, pageRequest);

        page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
//then
        List<Member> content = page.getContent();
        //조회된 데이터
        assertThat(content.size()).isEqualTo(3);
        //조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5);
        //전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0);
        //페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2);
        //전체 페이지 번호
        assertThat(page.isFirst()).isTrue();
        //첫번째 항목인가?
        assertThat(page.hasNext()).isTrue();
    }
}
