package com.example.expensesplit.controller;

import com.example.expensesplit.model.Group;
import com.example.expensesplit.service.GroupService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody CreateGroupRequest request,
                                             Authentication authentication) {
        Group group = groupService.createGroup(
                request.getName(),
                request.getDescription(),
                authentication.getName(),
                request.getMemberEmails()
        );
        return ResponseEntity.ok(group);
    }

    @GetMapping
    public ResponseEntity<List<Group>> getUserGroups(Authentication authentication) {
        List<Group> groups = groupService.getUserGroups(authentication.getName());
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroup(@PathVariable Long id) {
        Group group = groupService.getGroupById(id);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Group> addMember(@PathVariable Long id,
                                           @RequestBody AddMemberRequest request) {
        Group group = groupService.addMember(id, request.getEmail());
        return ResponseEntity.ok(group);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @Data
    static class CreateGroupRequest {
        private String name;
        private String description;
        private List<String> memberEmails;
    }

    @Data
    static class AddMemberRequest {
        private String email;
    }
}