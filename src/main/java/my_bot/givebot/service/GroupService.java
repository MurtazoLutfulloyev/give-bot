package my_bot.givebot.service;


import lombok.RequiredArgsConstructor;
import my_bot.givebot.dto.Request;
import my_bot.givebot.model.Group;
import my_bot.givebot.repository.GroupRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Base64;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GroupService {


    private final GroupRepository groupRepository;
    public void saveGroup(Group group) {
        groupRepository.save(group);
    }
}
