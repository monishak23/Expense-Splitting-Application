package com.example.expensesplit.service;

import com.example.expensesplit.model.Group;
import com.example.expensesplit.model.User;
import com.example.expensesplit.repository.GroupRepository;
import com.example.expensesplit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Transactional
    public Group createGroup(String name, String description, String creatorEmail, List<String> memberEmails) {
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        group.setCreatedBy(creator);

        Set<User> members = new HashSet<>();
        members.add(creator); // Add creator as member

        // Add other members
        if (memberEmails != null) {
            for (String email : memberEmails) {
                userRepository.findByEmail(email).ifPresent(members::add);
            }
        }

        group.setMembers(members);

        return groupRepository.save(group);
    }

    public List<Group> getUserGroups(String userEmail) {
        return groupRepository.findByMemberEmail(userEmail);
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }

    @Transactional
    public Group addMember(Long groupId, String memberEmail) {
        Group group = getGroupById(groupId);
        User member = userRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        group.getMembers().add(member);
        return groupRepository.save(group);
    }

    @Transactional
    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }
}