package persistence;

import domain.Member;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * This implementation keeps records only for the current process. Insertion
 * order provides stable enumeration while keeping storage replaceable.
 */
public class InMemoryMemberRepository implements MemberRepository {
    private final Map<Integer, Member> members = new LinkedHashMap<>();

    @Override
    public void save(Member member) {
        members.put(member.getId(), member);
    }

    @Override
    public List<Member> findAll() {
        return List.copyOf(members.values());
    }
}
