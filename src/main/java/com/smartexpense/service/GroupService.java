package com.smartexpense.service;

import com.smartexpense.model.Group;
import com.smartexpense.repository.GroupRepository;
import com.smartexpense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public boolean addMember(Long groupId, Long userId) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isPresent() && userRepository.existsById(userId)) {
            Group group = groupOpt.get();
            if (!group.getMemberIds().contains(userId)) {
                group.getMemberIds().add(userId);
                groupRepository.save(group);
            }
            return true;
        }
        return false;
    }

    public boolean removeMember(Long groupId, Long userId) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isPresent()) {
            Group group = groupOpt.get();
            if (group.getMemberIds().contains(userId)) {
                group.getMemberIds().remove(userId);
                groupRepository.save(group);
                return true;
            }
        }
        return false;
    }

    public boolean deleteGroup(Long id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
            return true;
        }
        return false;
    }
}