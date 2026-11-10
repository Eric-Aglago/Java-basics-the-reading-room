package persistence;

import domain.Member;

import java.util.List;

/** Storage contract for members, independent of the chosen persistence mechanism. */
public interface MemberRepository {
    void save(Member member);

    List<Member> findAll();
}
