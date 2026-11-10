package persistence;

import domain.Member;

import java.util.List;

public interface MemberRepository {
    void save(Member member);

    List<Member> findAll();
}
