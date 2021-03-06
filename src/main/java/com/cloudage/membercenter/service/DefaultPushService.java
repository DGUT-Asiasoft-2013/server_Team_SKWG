package com.cloudage.membercenter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudage.membercenter.entity.Push;
import com.cloudage.membercenter.repository.IPushRepository;

@Component
@Service
@Transactional
public class DefaultPushService implements IPushService{

        @Autowired
        IPushRepository pushRepo;
        
        @Override
        public Push save(Push push) {
                return pushRepo.save(push);
        }

        @Override
        public Page<Push> findPushByUserId(int userId, int page) {
                Sort sort = new Sort(Direction.DESC, "createDate");
                PageRequest pageRequest = new PageRequest(page, 20, sort);
                return pushRepo.findPushByUserId(userId, pageRequest);
        }

}
